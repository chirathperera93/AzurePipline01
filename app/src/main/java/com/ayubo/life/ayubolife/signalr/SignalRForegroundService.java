package com.ayubo.life.ayubolife.signalr;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionState;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Chirath Perera on 2021-08-22.
 */
public class SignalRForegroundService extends Service {

    public int counter = 0;
    private Timer timer;
    private TimerTask timerTask;
    SignalRSingleton signalRSingleton;
    Boolean isStartedConnection = false;
    public HubConnection hubConnection;

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

//        try {
//            startMyOwnForeground();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    private class createSignalRConnectionHub extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            isStartedConnection = false;
            signalRSingleton = SignalRSingleton.getInstance(getBaseContext());
            if (isNetworkAvailable()) {
                try {
                    signalRSingleton.startConnection();
                    isStartedConnection = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    Toast.makeText(getBaseContext(), "No internet connectivity", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            return null;
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String action = intent.getAction();
//        switch (action) {
//            case ACTION_START_FOREGROUND_SERVICE:
        if (hubConnection == null || hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
            try {
                new createSignalRConnectionHub().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        Toast.makeText(getApplicationContext(), "Foreground service is started.", Toast.LENGTH_LONG).show();
//                break;

//        }


        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimerTask();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, ReStartForegroundService.class);
        this.sendBroadcast(broadcastIntent);
    }


    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                if (isStartedConnection && signalRSingleton.hubConnection != null) {
                    Log.i(signalRSingleton.hubConnection.getConnectionState().toString(), "   =========  " + (counter++));

                }
            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
