package com.ayubo.life.ayubolife.ayubolifestepcounter;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.service.NewStepSenser;
import com.ayubo.life.ayubolife.utility.Util;
import com.ayubo.life.ayubolife.webrtc.App;

import java.text.NumberFormat;
import java.util.Locale;

public class SensorManagerHandlerService extends Service {
    private final static int NOTIFICATION_ID = 1;

    private static final String ACTION_START = "com.ayubo.life.ayubolife.ayubolifestepcounter.START";
    private static final String ACTION_STOP = "com.ayubo.life.ayubolife.ayubolifestepcounter.STOP";
    private static final String ACTION_PAUSE = "com.ayubo.life.ayubolife.ayubolifestepcounter.PAUSE";
    private static final String ACTION_RESUME = "com.ayubo.life.ayubolife.ayubolifestepcounter.RESUME";
    private static final String ACTION_UPDATE_NOTIFICATION = "com.ayubo.life.ayubolife.ayubolifestepcounter.UPDATE_NOTIFICATION";
    private static final String ACTION_SAVE_DATA = "com.ayubo.life.ayubolife.ayubolifestepcounter.SAVE_DATA";
    private static final String ACTION_START_SAVE_HOURLY_ALARM = "com.ayubo.life.ayubolife.ayubolifestepcounter.START_SAVE_HOURLY_ALARM";
    private static final String ACTION_START_SAVE_AT_MID_NIGHT_ALARM = "com.ayubo.life.ayubolife.ayubolifestepcounter.START_SAVE_AT_MID_NIGHT_ALARM";
    private static final String ACTION_SAVE_AT_MID_NIGHT = "com.ayubo.life.ayubolife.ayubolifestepcounter.START_SAVE_AT_MID_NIGHT";
    private SensorManagerAyuboLife stepManager;
    private NotificationManager notificationManager;
    private boolean isPaused;


    private PrefManager mPref;
    private String useridExistingUser;

    public static void startStepCounter() {
        Intent intent = new Intent(App.getInstance(), SensorManagerHandlerService.class);
        intent.setAction(ACTION_START);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            App.getInstance().startForegroundService(intent);
        }else{
            App.getInstance().startService(intent);
        }
        Log.d("BootupReceiver... ","============startService=============");
    }



    public static void startSaveHourlyAlarm() {
        Intent intent = new Intent(App.getInstance(), SensorManagerHandlerService.class);
        intent.setAction(ACTION_START_SAVE_HOURLY_ALARM);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            App.getInstance().startForegroundService(intent);
        }else{
            App.getInstance().startService(intent);
        }
    }

    public static void startSaveAtMidNightAlarm() {
        Intent intent = new Intent(App.getInstance(), SensorManagerHandlerService.class);
        intent.setAction(ACTION_START_SAVE_AT_MID_NIGHT_ALARM);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            App.getInstance().startForegroundService(intent);
        }else{
            App.getInstance().startService(intent);
        }
    }

    public static void updateStepsNotification() {
        Intent intent = new Intent(App.getInstance(), SensorManagerHandlerService.class);
        intent.setAction(ACTION_UPDATE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            App.getInstance().startForegroundService(intent);
        }else{
            App.getInstance().startService(intent);
        }
    }

    public static void stopStepCounter() {
        Intent intent = new Intent(App.getInstance(), SensorManagerHandlerService.class);
        intent.setAction(ACTION_STOP);
        App.getInstance().stopService(intent);
    }

    public static void saveDataToDB() {
        Intent intent = new Intent(App.getInstance(), SensorManagerHandlerService.class);
        intent.setAction(ACTION_SAVE_DATA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            App.getInstance().startForegroundService(intent);
        }else{
            App.getInstance().startService(intent);
        }
    }

    public static void saveMidNightData(){
        Intent intent = new Intent(App.getInstance(), SensorManagerHandlerService.class);
        intent.setAction(ACTION_SAVE_AT_MID_NIGHT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            App.getInstance().startForegroundService(intent);
        }else{
            App.getInstance().startService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPref = new PrefManager(this);
        useridExistingUser = mPref.getLoginUser().get("uid");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        stepManager = SensorManagerAyuboLife.getInstance();
        stepManager.setGoal(10000);

    }

    public SensorManagerHandlerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_START:
                    stepManager.updateTotalRealSteps();
                    stepManager.register();
                    updateNotificationState();
                    break;

                case ACTION_STOP:
                    //because we update when onDestroy calls
                    stepManager.updateTotalRealSteps();
                    stepManager.unRegister();
                    notificationManager.cancel(NOTIFICATION_ID);
                    stopForeground(true);
                    stopSelf();
                    break;

                case ACTION_PAUSE:
                    stepManager.updateTotalRealSteps();
                    stepManager.unRegister();
                    isPaused = true;
                    updateNotificationState();
                    break;

                case ACTION_RESUME:
                    stepManager.register();
                    isPaused = false;
                    updateNotificationState();
                    break;

                case ACTION_UPDATE_NOTIFICATION:
                    //TODO check whether a new date
                    if(Util.isANewDate(new PrefManager(this).getLastTodayDate(),Util.getTodayString())){
                        SensorManagerHandlerService.saveMidNightData();
                        break;
                    }

                    updateNotificationState();
                    stepManager.updateTotalRealSteps();
                    System.out.println("====Saving DB ======date=======================" + Util.getTodayString());
                    System.out.println("====Saving DB =============================" + stepManager.getLastTotalRealSteps());
                    DBRequest.updateToSummeryTable(this, useridExistingUser, Util.getTodayString(), stepManager.getLastTotalRealSteps(), 0, 0, 0, 0, 0);
                    stepManager.updateThreaHold();
                    //and notify
                    break;

                case ACTION_START_SAVE_HOURLY_ALARM:
                    if (!mPref.getHourlyAlarmStatus()) {
                        Log.d("START_SAVE", "ACTION_START_SAVE_HOURLY_ALARM");
                        Intent intent1 = new Intent(this, SensorManagerHandlerService.class);
                        intent1.setAction(ACTION_SAVE_DATA);
                        // countre start service every hour to save the current step c
                        ((AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE))
                                .setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_HOUR, PendingIntent
                                        .getService(getApplicationContext(), 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT));
                        mPref.setHourlyAlarmStatus(true);
                    }
                    break;

                case ACTION_START_SAVE_AT_MID_NIGHT_ALARM:
                    if (!mPref.getHourlyMidNightAlarmStatus()) {
                        Log.d("START_SAVE", "ACTION_START_SAVE_AT_MID_NIGHT_ALARM");
                        Intent intent1 = new Intent(this, SensorManagerHandlerService.class);
                        intent1.setAction(ACTION_SAVE_AT_MID_NIGHT);
                        // countre start service every hour to save the current step c
                        ((AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE))
                                .setRepeating(AlarmManager.RTC_WAKEUP, Util.getMillisecondsTillNextMidnight(), AlarmManager.INTERVAL_DAY, PendingIntent
                                        .getService(getApplicationContext(), 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT));
                        mPref.setHourlyMidNightAlarmStatus(true);
                    }
                    break;

                case ACTION_SAVE_DATA:
                    //TODO check whether a new date
                    if(Util.isANewDate(new PrefManager(this).getLastTodayDate(),Util.getTodayString())){
                        SensorManagerHandlerService.saveMidNightData();
                        break;
                    }

                    //save data to db
                    stepManager.updateTotalRealSteps();
                    System.out.println("====Saving DB ======date=======================" + Util.getTodayString());
                    System.out.println("====Saving DB =============================" + stepManager.getLastTotalRealSteps());
                    DBRequest.updateToSummeryTable(this, useridExistingUser, Util.getTodayString(), stepManager.getLastTotalRealSteps(), 0, 0, 0, 0, 0);
                    stepManager.updateThreaHold();
                    //and notify
                    updateNotificationState();
                    break;

                case ACTION_SAVE_AT_MID_NIGHT:
                    //save data to db
                    stepManager.updateTotalRealSteps();
                    DBRequest.updateToSummeryTable(this, useridExistingUser, Util.getTodayString(), stepManager.getLastTotalRealSteps(), 0, 0, 0, 0, 0);
                    stepManager.updateThreaHold();
                    //and reset current steps count and notify
                    stepManager.resetTotalSteps();
                    //update new steps count to tommorow
                    DBRequest.updateToSummeryTable(this, useridExistingUser, Util.getTommorowString(), stepManager.getLastTotalRealSteps(), 0, 0, 0, 0, 0);
                    stepManager.updateThreaHold();
                    new PrefManager(this).saveToday();
                    updateNotificationState();
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (stepManager != null) {
            stepManager.unRegister();
            stepManager.postSteps(true);
        }
    }


    private void updateNotificationState() {
        Notification.Builder notificationBuilder = new Notification.Builder(this);

        if (stepManager.getCurrentSensorSteps() > 0) {
            notificationBuilder.setProgress(stepManager.getGoal(), stepManager.getLastTotalRealSteps() + stepManager.getCurrentRealSteps(), false)
                    .setContentText(stepManager.getLastTotalRealSteps() + stepManager.getCurrentRealSteps() >= stepManager.getGoal() ? getString(R.string.goal_reached_notification,
                            NumberFormat.getInstance(Locale.getDefault()).format((stepManager.getLastTotalRealSteps() + stepManager.getCurrentRealSteps()))) :
                            getString(R.string.notification_text, NumberFormat.getInstance(Locale.getDefault())
                                    .format((stepManager.getLastTotalRealSteps() + stepManager.getCurrentRealSteps()))));
        } else { // still no step value?
            notificationBuilder
                    .setContentText(getString(R.string.your_progress_will_be_shown_here_soon));
        }
        notificationBuilder.setPriority(Notification.PRIORITY_MIN).setShowWhen(false)
                .setContentTitle(isPaused ? getString(R.string.ispaused) :
                        getString(R.string.notification_title)).setContentIntent(PendingIntent
                .getActivity(this, 0, new Intent(this, NewHomeWithSideMenuActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.mipmap.ic_launcher)
//                .addAction(isPaused ? R.drawable.ic_resume : R.drawable.ic_pause,
//                        isPaused ? getString(R.string.resume) : getString(R.string.pause),
//                        PendingIntent.getService(this, 4, new Intent(this, SensorManagerHandlerService.class)
//                                        .setAction(isPaused ? ACTION_RESUME : ACTION_PAUSE),
//                                PendingIntent.FLAG_UPDATE_CURRENT))
//                .addAction(android.R.drawable.ic_menu_close_clear_cancel, getString(R.string.stop), PendingIntent.getService(this, 4, new Intent(this, SensorManagerHandlerService.class)
//                                .setAction(ACTION_STOP),
//                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setOngoing(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            assert notificationManager != null;
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationChannel.enableVibration(false);
            notificationChannel.setVibrationPattern(new long[]{0L});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        startForeground(NOTIFICATION_ID, notificationBuilder.build());
//        createNotification();
    }

    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    public void createNotification() {
        {
            Notification.Builder mBuilder = new Notification.Builder(this);

            if (stepManager.getCurrentSensorSteps() > 0) {
                mBuilder.setProgress(stepManager.getGoal(), stepManager.getCurrentSensorSteps(), false).setContentText(
                        stepManager.getCurrentSensorSteps() >= stepManager.getGoal() ? getString(R.string.goal_reached_notification,
                                NumberFormat.getInstance(Locale.getDefault())
                                        .format((stepManager.getCurrentSensorSteps()))) :
                                getString(R.string.notification_text,
                                        NumberFormat.getInstance(Locale.getDefault())
                                                .format((stepManager.getGoal() - stepManager.getCurrentSensorSteps()))));
            } else { // still no step value?
                mBuilder
                        .setContentText(getString(R.string.your_progress_will_be_shown_here_soon));
            }

            /**Creates an explicit intent for an Activity in your app**/
            Intent resultIntent = new Intent(this, NewHomeWithSideMenuActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setPriority(Notification.PRIORITY_MIN).setShowWhen(false);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle(isPaused ? getString(R.string.ispaused) :
                    getString(R.string.notification_title))
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentIntent(resultPendingIntent);


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
//                notificationChannel.enableLights(true);
//                notificationChannel.setLightColor(Color.RED);
//                notificationChannel.enableVibration(true);
//                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                assert notificationManager != null;
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            assert notificationManager != null;
            notificationManager.notify(0 /* Request Code */, mBuilder.build());
        }
    }


}
