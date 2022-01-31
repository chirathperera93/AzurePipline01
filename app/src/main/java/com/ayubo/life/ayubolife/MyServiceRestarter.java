package com.ayubo.life.ayubolife;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ayubo.life.ayubolife.service.AyuboStepService;

/**
 * Created by appdev on 12/21/2016.
 */
public class MyServiceRestarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Intent myIntent = new Intent(context, AyuboStepService.class);
        context.startService(myIntent);
        Log.w("MyServiceRestarter", "========================================AyuboStepService");
     //   Toast.makeText(this, "Destroyed Method Called in Service", Toast.LENGTH_LONG).show();

    }
}
