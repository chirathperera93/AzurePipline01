package com.ayubo.life.ayubolife.push;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Bundle;
import android.os.Vibrator;

import com.ayubo.life.ayubolife.activity.PrefManager;

public class NotificationActivity extends Activity {

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    public PrefManager prefManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Ringtone ringtone = prefManager.getRingtone();
        Vibrator vibrator = prefManager.getVibrator();
        if (ringtone != null) {
            ringtone.stop();
            prefManager.setRingingTone(null);
        }

        if (vibrator != null) {
            vibrator.cancel();
            prefManager.setVibrator(null);
        }
        manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));
        finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately

    }

    public static PendingIntent getDismissIntent(int notificationId, Context context) {
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent dismissIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        return dismissIntent;
    }

}