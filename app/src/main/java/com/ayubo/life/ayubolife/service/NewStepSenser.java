package com.ayubo.life.ayubolife.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by appdev on 5/14/2018.
 */
import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.model.todaysummery;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Util;
import com.google.firebase.crash.FirebaseCrash;




public class NewStepSenser extends Service implements SensorEventListener {


    private final static long MICROSECONDS_IN_ONE_MINUTE = 60000000;

    private int steps;
    DatabaseHandler db;
    PrefManager pref;
    String userid_ExistingUser;
    int Yesterday;
    SimpleDateFormat sdf;
    SharedPreferences prefs;
    int SR=0;
    int count=0;
    int LSR=-1;
    SensorManager sm=null;

    @Override
    public void onAccuracyChanged(final Sensor portrait, int accuracy) {
        // nobody knows what happens here: step value might magically decrease
        // when this method is called...
      //  if (BuildConfig.DEBUG) Logger.log(portrait.getName() + " accuracy changed: " + accuracy);
    }

    public String today() {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Calendar.getInstance().getTime());
    }


    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (event.values[0] > Integer.MAX_VALUE) {
            return;
        }else{
            SR =  (int) event.values[0];
            System.out.println("=======SR======="+SR);
        }
    }


    public void saveStepsDataToLoclDB(int currentStep,int finalStep) {
        Date dat = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);
        if(currentStep>0){
          //  pref.setSensorSteps(Integer.toString(currentStep));
        }

        try{
            Date dae = new Date();
            String de = sdf.format(dae);

           DBRequest.updateToSummeryTable(NewStepSenser.this,userid_ExistingUser, de, currentStep, 0, finalStep, 0, 0, 0);

        }catch (Exception e){
            FirebaseCrash.report(e);
        }
    }

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }


    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File file = new File(context.getFilesDir(), "ayubo");
            if (!file.exists()) {
                file.mkdir();
            }
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            System.out.println("======Text==Saved===============" + sBody);
           // Toast.makeText(context, "Saved"+sBody, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

         pref = new PrefManager(this);
         userid_ExistingUser=pref.getLoginUser().get("uid");
         sdf = new SimpleDateFormat("yyyy-MM-dd");
         db = new DatabaseHandler(this);
         prefs = this.getSharedPreferences("ayubolife", Context.MODE_PRIVATE);

         long executedTimeInLong= System.currentTimeMillis();
         pref.setNewServiceLastRunningTime(Long.toString(executedTimeInLong));
         System.out.println("======in====SERVICE====");

//        googlefitenabled=pref.isGoogleFitEnabled();
//
//        Intent intentt = new Intent(this, NewHomeWithSideMenuActivity.class);
//        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intentt, 0);
//
//
//
//            System.out.println("============isGFitActive===========false=====");
//            Notification notification  = new Notification.Builder(this)
//                    .setContentTitle("Activity Tracking")
//                    .setContentText("ayubo.life")
//                    .setSmallIcon(R.drawable.icon)
//                    .setContentIntent(pIntent)
//                    .setAutoCancel(false)
//                    .build();
//
//            notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
//            NotificationManager notifier = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
//            notifier.notify(1, notification);


        Log.d("New Steps Service.....","Service Started");
        String location=null;
      //  Toast.makeText(NewStepSenser.this, "Service Started", Toast.LENGTH_SHORT).show();

        //SR is Sensor Reading...
        //LSR is Lst Sensor Reading...

        LSR= prefs.getInt("LSR", -25);


       if(LSR!= -25){

           if(SR == LSR){
               location="SR ...2LSR";
               System.out.println("==========SERVICE Stop bcoz 0 steps====");
               stopSelf();

           }
           else if(SR < LSR){

             // (Device has restarted ) isSensorReset == Yes  OR App ReInstalled
               if(DBRequest.hasStepsToday(NewStepSenser.this,userid_ExistingUser)){
                   count=0;
                   count=SR+DBRequest.getTodayStepsCount(NewStepSenser.this,userid_ExistingUser);
                   saveStepsDataToLoclDB(count,SR);
                   location="SR < 3LSR......hasStepsToday";
                   if(SR>0){
                       saveLSR(SR);
                   }

               }else{
                   count=0;
                   count=SR;
                   saveStepsDataToLoclDB(count,SR);
                   if(SR>0){
                       saveLSR(SR);
                   }
                   location="SR < 4LSR......";
               }
           }else{
               // (Device has not restarted ) isSensorReset == No
               if(DBRequest.hasStepsToday(NewStepSenser.this,userid_ExistingUser)){
                // B
                   count=0;
                   count=(SR-LSR)+DBRequest.getTodayStepsCount(NewStepSenser.this,userid_ExistingUser);
                   saveStepsDataToLoclDB(count,SR);
                   if(SR>0){
                       saveLSR(SR);
                   }
                   location="SR > 5LSR......hasStepsToday";
               }else{
                   // A  (second day first run)
                   count=0;
                   count=SR-DBRequest.getYesterdayFinalReading(NewStepSenser.this,userid_ExistingUser);
                   saveStepsDataToLoclDB(count,SR);
                   if(SR>0){
                       saveLSR(SR);
                   }
                   location="SR > 6LSR......";
               }

           }


       }else{
           // E First time running ....
           saveStepsDataToLoclDB(0,SR);
           if(SR>0){
               saveLSR(SR);
           }
       }

        Date dae = new Date();
        String de = sdf.format(dae);
        String savingData=SR+"===="+LSR+"==="+de+"==="+count+"==="+location;
        Log.d("New Step Service....",savingData);


        return START_NOT_STICKY;
    }

    void saveLSR(Integer lsr){
        SharedPreferences.Editor editor2 = prefs.edit();
        editor2.putInt("LSR", lsr);
        editor2.commit();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Date da=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(da);

       int today = cal.get(Calendar.DAY_OF_MONTH);
        prefs = this.getSharedPreferences("ayubolife", Context.MODE_PRIVATE);

        Yesterday= prefs.getInt("Yesterday", 35);
        if(Yesterday==35){

        }else{
            if (Yesterday != today) {
                steps = 0;
            }
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Yesterday", Yesterday);
        editor.commit();


        reRegisterSensor();

    }

//    @Override
//    public void onTaskRemoved(final Intent rootIntent) {
//        super.onTaskRemoved(rootIntent);
//        // Restart service in 30 min
////        ((AlarmManager) getSystemService(Context.ALARM_SERVICE))
////                .set(AlarmManager.RTC, System.currentTimeMillis() + 1000 * 60 * 30, PendingIntent
////                        .getService(this, 0, new Intent(this, NewStepSenser.class), 0));
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    //    if (BuildConfig.DEBUG) Logger.log("SensorListener onDestroy");
        try {
             sm = (SensorManager) getSystemService(SENSOR_SERVICE);
            sm.unregisterListener(this);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) Logger.log(e);
            e.printStackTrace();
        }

    //    saveStepsDataToLoclDB();

//        Intent intent = new Intent();
//        intent.setAction("com.ayubo.life.ayubolife.MyServiceRestarter");
//        sendBroadcast(intent);
        stopSelf();
    }



    private void reRegisterSensor() {
      //  if (BuildConfig.DEBUG) Logger.log("re-register portrait listener");
         sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        try {
            sm.flush(this);
            sm.unregisterListener(this);
        } catch (Exception e) {
         //   if (BuildConfig.DEBUG) Logger.log(e);
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG) {
          //  Logger.log("step sensors: " + sm.getSensorList(Sensor.TYPE_STEP_COUNTER).size());
            if (sm.getSensorList(Sensor.TYPE_STEP_COUNTER).size() < 1) return; // emulator
          //  Logger.log("default: " + sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER).getName());
        }

        // enable batching with delay of max 5 min
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_NORMAL, (int) (5 * MICROSECONDS_IN_ONE_MINUTE));
    }
}
