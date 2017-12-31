package rchugunov.com.ipaddressnotification;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        if (savedInstanceState != null) {
            return;
        }

        new GetConnectionsAsyncTask(getApplication()).execute();

        finish();
    }

    public static PendingIntent getPendingIntent(Context context) {
        return PendingIntent.getActivity(
                context, 0, new Intent(context, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
