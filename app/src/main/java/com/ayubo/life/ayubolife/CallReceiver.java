package com.ayubo.life.ayubolife;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.ayubo.life.ayubolife.login.SplashScreen;


public class CallReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(TAG, "SMS =========================================");
        Log.e(TAG, "SMS =========================================");
        Log.e(TAG, "SMS =========================================");
        Log.e(TAG, "SMS =========================================");
        Log.e(TAG, "SMS =========================================");
        Log.e(TAG, "SMS =========================================");


        ComponentName comp = new ComponentName(context.getPackageName(),
                SplashScreen.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}