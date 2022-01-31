package com.ayubo.life.ayubolife;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.ayubo.life.ayubolife.service.AyuboStepService;

/**
 * Created by AppDev5 on 2/23/2016.
 */

public class MyDateChangeReceiver2 extends IntentService {
    public MyDateChangeReceiver2() {
        super("MyTestService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        startService(new Intent(this, AyuboStepService.class));
        Log.i("MyTestService", "Service running");
    }
        }