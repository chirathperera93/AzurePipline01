package com.ayubo.life.ayubolife;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.ayubo.life.ayubolife.activity.PrefManager;

import com.ayubo.life.ayubolife.service.AyuboStepService;

/**
 * Created by appdev on 4/28/2017.
 */

public class BootupReceiverStepService extends BroadcastReceiver {
////    PrefManager pref;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BootupReceiver... ","Received BOOT COMPLETED");




    }
}
