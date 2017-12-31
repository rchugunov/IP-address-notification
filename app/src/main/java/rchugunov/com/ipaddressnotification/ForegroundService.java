package rchugunov.com.ipaddressnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class ForegroundService extends Service {
    private static final int NOTIFICATION_ID = 90001;
    public static final String NOTIFICATION_CHANNEL_ID = "ip_main_channel";
    public static final String NOTIFICATION_CHANNEL_NAME = "ip_main_channel";
    public static final String NOTIFICATION_CHANNEL_DESC = "ip_main_channel";

    public static final String IP_ADDRESS = "ip_address";

    private ConnectivityChangedBroadcastReceiver receiver;

    public static void startForegroundService(String ipaddress, Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, ForegroundService.class);
        intent.putExtra(ForegroundService.IP_ADDRESS, ipaddress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(ForegroundService.NOTIFICATION_CHANNEL_ID,
                    ForegroundService.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(ForegroundService.NOTIFICATION_CHANNEL_DESC);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(false);

            //noinspection ConstantConditions
            notificationManager.createNotificationChannel(channel);
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new ConnectivityChangedBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startForeground(NOTIFICATION_ID, buildNotification(intent.getStringExtra(ForegroundService.IP_ADDRESS)));

        return START_STICKY;
    }

    private Notification buildNotification(String data) {
        return new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setOngoing(true)
                .setContentTitle("Your ip address:")
                .setContentText(data)
                .setSmallIcon(R.drawable.ic_ip_app)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data))
                .setContentIntent(MainActivity.getPendingIntent(getApplicationContext()))
                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                .build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        startForegroundService(rootIntent.getStringExtra(IP_ADDRESS), getApplicationContext());
    }
}
