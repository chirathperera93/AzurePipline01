package com.ayubo.life.ayubolife.signalr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/**
 * Created by Chirath Perera on 2021-08-22.
 */
public class ReStartForegroundService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
//        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                context.startForegroundService(new Intent(context, SignalRForegroundService.class));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                context.startService(new Intent(context, SignalRForegroundService.class));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
