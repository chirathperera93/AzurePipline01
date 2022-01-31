package com.ayubo.life.ayubolife;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by appdev on 4/28/2017.
 */

public class BootupReceiverVideoCall extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("======Receiver VideoCall=====================================");
        System.out.println("======Receiver VideoCall====================================");
        System.out.println("======Receiver VideoCall====================================");
        System.out.println("======Receiver VideoCall====================================");
       // Intent myIntent = new Intent(context, CallService.class);
      //  context.startService(myIntent);
    }
}