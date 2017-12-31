package rchugunov.com.ipaddressnotification;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GetConnectionsAsyncTask extends AsyncTask<Object, Object, String> {

    private static final String TAG = GetConnectionsAsyncTask.class.getSimpleName();

    private final WeakReference<Context> applicationContext;

    public GetConnectionsAsyncTask(Context context) {
        this.applicationContext = new WeakReference<>(context.getApplicationContext());
    }

    @Override
    protected String doInBackground(Object[] objects) {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String ipaddress) {
        Context context = applicationContext.get();
        if (context == null) {
            return;
        }
        ForegroundService.startForegroundService(ipaddress, context);
    }
}
