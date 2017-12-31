package rchugunov.com.ipaddressnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import java.util.Objects;

public class ConnectivityChangedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        if (Objects.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) {
            new GetConnectionsAsyncTask(context).execute();
        }
    }
}
