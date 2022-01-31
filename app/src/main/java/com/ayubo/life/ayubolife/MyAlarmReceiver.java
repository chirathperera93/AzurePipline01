package com.ayubo.life.ayubolife;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



/**
 * Created by appdev on 12/21/2016.
 */

public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyDateChangeReceiver2.class);
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}
