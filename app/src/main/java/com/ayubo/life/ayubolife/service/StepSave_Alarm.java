package com.ayubo.life.ayubolife.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;

/**
 * Created by appdev on 5/15/2018.
 */


public class StepSave_Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Put here YOUR code.
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
       // startService(new Intent(NewHomeWithSideMenuActivity.this, NewStepSenser.class));



    }

}