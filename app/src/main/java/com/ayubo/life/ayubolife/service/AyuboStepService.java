package com.ayubo.life.ayubolife.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;

import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.lifeplus.ProfileNew;
import com.ayubo.life.ayubolife.model.todaysummery;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.crash.FirebaseCrash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AppDev5 on 1/13/2016.
 */
public class AyuboStepService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    String server_URL;
    SharedPreferences prefs;
    PrefManager pref;
    Timer timer;
    TimerTask timerTask;
    private float WFINAL_FULL_MET_COUNT = 0;
    public float WFINAL_FULL_CAL_COUNT = 0;
    private int WFINAL_FULL_STEP_COUNT = 0;
    private int FINAL_FULL_DISTANCE_COUNT = 0;


    int wroteLastMinit_Steps = 0;
    int read_NewMinit_Steps = 0;
    private final float NOISE = (float) 1.0;
    ArrayList<HashMap<String, String>> formList;
    int Today, Yesterday;

    private Handler handler = new Handler();

    private float xCurrent;
    private float yCurrent;
    private float zCurrent;
    int a, b, c, d, e, f;
    int run = 0;
    int jump = 0;
    int reset_STEPCOUNT_FOR_A_MINIT;
    int st2;
    int run2;
    int step;
    int st;
    int stepCountWithWalkPlusRun;
    long lastUpdate;
    long lastUpdateNew;
    String de;
    private float FULL_MET_COUNT_DB = 0;
    private float FULL_CAL_COUNT_DB = 0;
    private float FULL_ENGY_PERSENT_DB = 0;
    private int FULL_STEP_DB = 0;
    int dbSteps;

    private double xPreviousAccel;
    private double yPreviousAccel;
    private double zPreviousAccel;
    double fdeltaX;
    double fdeltaY;
    double fdeltaZ;
    boolean first = false;
    /* Used to suppress the first shaking */
    private boolean firstUpdate = true;
    private SensorManager mySensorManager;
    private final float violence_walk = 1.2f;
    private final float violence_walk_end = 1.5f;
    private final float violence_run_start = 3.0f;
    private final float violence_run = 7.5f;
    TimerTask timer_foutTimePerDay_doAsynchronousTask;
    private boolean shakeInitiated = false;
    boolean mInitialized;
    private Sensor mSensor;
    double mLastX;
    double mLastY;
    double mLastZ;
    //  public static final String TAG = "BasicSensorsApi";
    private final IBinder mBinder = new MyBinder();
    DatabaseHandler db;
    float db_today_tot_steps = 0;
    float db_today_tot_mets = 0;
    float db_today_tot_cal = 0;
    todaysummery newObj;
    // DatabaseHandler_MyService sqlObj;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    float dailyTarget = 25;
    int sameId, sameStep, sameRun, sameJump;
    float sameMets, sameCals;
    float sameEnPersent;
    String sameDate;

    private static final boolean ADAPTIVE_ACCEL_FILTER = true;
    float lastAccel[] = new float[3];
    float accelFilter[] = new float[3];
    double linear_acceleration[];
    Timer timer_foutTimePerDay;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    String userid_ExistingUser, googlefitenabled;
    String gfitSteps;
    private boolean serviceRuningFirst = false;
    private boolean serviceRuningLater = false;
    SimpleDateFormat sdf;

    public float FINAL_ENERGY_PERSENTAGE = 0;

    int l = 10;
    // int st=0;
    String selectedDate;
    private final static String TAG = "StepDetector";
    public static float mLimit = 12;
    private float mLastValues[] = new float[3 * 2];
    private float mScale[] = new float[2];
    private float mYOffset;
    // SettingEntity setting5;
    private float mLastDirections[] = new float[3 * 2];
    private float mLastExtremes[][] = {new float[3 * 2], new float[3 * 2]};
    private float mLastDiff[] = new float[3 * 2];
    private int mLastMatch = -1;
    private Notification mNotification;
    private static int notificationId = 1;
    private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();

    private GoogleApiClient mClient = null;

    String manufacture, phoneModel;

    public AyuboStepService() {
        int h = 480; // TODO: remove this constant
        mYOffset = h * 0.5f;
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }

    public static void setSensitivity(float sensitivity) {
        mLimit = sensitivity;
    }


    private void sendBroadcast(int success) {
        Intent intent = new Intent("message"); //put the same message as in the filter you used in the activity when registering the receiver
        intent.putExtra("step", success);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            server_URL = ApiClient.BASE_URL_live;

            pref = new PrefManager(this);
            userid_ExistingUser = pref.getLoginUser().get("uid");


            //  System.out.println("===============onStartCommand========================"+userid_ExistingUser);

            db = new DatabaseHandler(this);
            server_URL = ApiClient.BASE_URL_live;

            lastUpdate = System.currentTimeMillis();

            Date dat = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(dat);

            sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dae = new Date();
            selectedDate = sdf.format(dae);

            loadDataFrom_TodaySummeryTable();
            phoneModel = Build.MODEL;
            manufacture = Build.MANUFACTURER;


        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.log("....Ayubo Service..............................");
            FirebaseCrash.report(e);
        }
        try {
            //  System.out.println("============saveStepsDataToLoclDB========================");
            saveStepsDataToLoclDB();
            //  System.out.println("============saveStepsDataToLoclDB========================");
        } catch (Exception e) {
            FirebaseCrash.report(e);
            e.printStackTrace();
        }


        try {
            startTimer_OnlineUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.log("....Ayubo Service....startTimer_OnlineUpdate..........................");
            FirebaseCrash.report(e);
        }

//        Intent intentt = new Intent(this, NewHomeWithSideMenuActivity.class);
//        Intent intentt = new Intent(this, LifePlusProgramActivity.class);
        Intent intentt = new Intent(this, ProfileNew.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intentt, 0);


        googlefitenabled = pref.isGoogleFitEnabled();

        if (googlefitenabled == null) {
            System.out.println("============isGFitActive===========false=====");
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Activity Tracking")
                    .setContentText("ayubo.life")
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(pIntent)
                    .setAutoCancel(false)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // notificationManager.notify(0, notification);

            // Notification notification = new Notification(R.drawable.icon, "ayubo.life", System.currentTimeMillis());
            notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            NotificationManager notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notifier.notify(1, notification);

        } else if (googlefitenabled.equals("false")) {
            String step = Integer.toString(st);

            if (step.length() > 5) {
                step = "0";
                //   st=0;
            }

            System.out.println("============isGFitActive===========false=====");
            Notification n = new Notification.Builder(this)
                    .setContentTitle("ayubo.life activity tracking")
                    .setContentText("Steps " + step)
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(pIntent)
                    .setAutoCancel(false)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, n);
        } else {
            if (googlefitenabled.equals("true")) {

            }
        }


        //  buildFitnessClient();


        return START_STICKY;
    }

    @Override
    public void onCreate() {

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySensorManager.registerListener(mySensorEventListener, mySensorManager
                        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        db = new DatabaseHandler(this);
        lastUpdate = System.currentTimeMillis();

        Date dat = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);

        Yesterday = cal.get(Calendar.DAY_OF_MONTH);
        prefs = this.getSharedPreferences("ayubolife", Context.MODE_PRIVATE);

        int savedYesterday = prefs.getInt("Yesterday", 35);
        if (savedYesterday == 35) {

        } else {
            if (Yesterday != savedYesterday) {
                st = 0;
            }
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Yesterday", Yesterday);
        editor.commit();


    }


    public void startTimer_OnlineUpdate() {
        timer_foutTimePerDay = new Timer();
        initializeTimerTaskS();
        timer_foutTimePerDay.schedule(timer_foutTimePerDay_doAsynchronousTask, 10, 60000);
        //  timer_foutTimePerDay.schedule(timer_foutTimePerDay_doAsynchronousTask, 10, 900000);
    }

    public void initializeTimerTaskS() {
        timer_foutTimePerDay_doAsynchronousTask = new TimerTask() {

            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        Date dat = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dat);

                        int toayint = cal.get(Calendar.DAY_OF_MONTH);
                        if (Yesterday != toayint) {
                            st = 0;
                            Yesterday = toayint;
                            Date dae = new Date();
                            de = sdf.format(dae);
                            DBRequest.updateToSummeryTable(AyuboStepService.this, userid_ExistingUser, de, 0, 0, 0, 0, 0, 0);
                            // updateToSummeryTable(userid_ExistingUser, de, 0, 0, 0, 0, 0, 0);
                            // System.out.println("-----------------NEW DAY DETECTED------------ -->"+st);
                        } else {
                            try {
                                //   System.out.println("-----------------SAME DAY DETECTED------------ -->"+st);
                                saveStepsDataToLoclDB();

                            } catch (Exception e) {
                                FirebaseCrash.log("....saveStepsDataToLoclDB.......in Ayubo Service...............");
                                FirebaseCrash.report(e);
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }

        };
    }


    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {

        } else {
            Log.e(TAG, "Error adding or removing activity detection: " + status.getStatusMessage());
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        // Log.i(TAG, "Connection suspended");
        //mGoogleApiClient.connect();
    }


    //==================SAVE DEFAULT ACTIVITY=========================================================
    private void checkDataFrom_TodaySummeryTable() {

        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        Date dae = new Date();
        String de = sdf.format(dae);

        if (de != null && userid_ExistingUser != null) {
            try {
                dbSteps = DBRequest.getTodayStepsCount(AyuboStepService.this, userid_ExistingUser);
                //  data = db.getTodaySummeryFromDB(de, userid_ExistingUser);
            } catch (Exception e) {
            }
        }

    }

    private void loadDataFrom_TodaySummeryTable() {

        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");

        Date dae = new Date();
        String de = sdf.format(dae);
        todaysummery data = null;
        if (de != null && userid_ExistingUser != null) {
            try {
                dbSteps = DBRequest.getTodayStepsCount(AyuboStepService.this, userid_ExistingUser);
                FULL_STEP_DB = dbSteps;
                stepCountWithWalkPlusRun = FULL_STEP_DB + run;
                FINAL_ENERGY_PERSENTAGE = FULL_ENGY_PERSENT_DB;
            } catch (Exception e) {
            }
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    public void saveStepsDataToLoclDB() {

        checkDataFrom_TodaySummeryTable();

        //=======================================================
        Date dat = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);

        int toayint = cal.get(Calendar.DAY_OF_MONTH);
        if (Yesterday != toayint) {
            st = 0;
            Yesterday = toayint;
        }
        try {
            Date dae = new Date();
            de = sdf.format(dae);
            if (st > 0) {
                if (step > dbSteps) {
                    DBRequest.updateToSummeryTable(AyuboStepService.this, userid_ExistingUser, de, step, 0, 0, 0, 0, 0);
                }
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
    }


    final SensorEventListener mySensorEventListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {

            Sensor portrait = se.sensor;
            long tim = se.timestamp;

            synchronized (this) {
                if (portrait.getType() == Sensor.TYPE_ORIENTATION) {
                } else {

                    if (googlefitenabled == null) {
                        //  System.out.println("===========================GOOLEFIT  NOT ACTIVATED=========");
                        int j = (portrait.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                        if (j == 1) {
                            float vSum = 0;
                            for (int i = 0; i < 3; i++) {
                                final float v = mYOffset + se.values[i] * mScale[j];
                                vSum += v;
                            }
                            int k = 0;
                            float v = vSum / 3;

                            float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                            if (direction == -mLastDirections[k]) {
                                // Direction changed
                                int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                                mLastExtremes[extType][k] = mLastValues[k];
                                float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

                                if (diff > mLimit) {

                                    boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                                    boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                                    boolean isNotContra = (mLastMatch != 1 - extType);

                                    if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {

                                        st = st + 1;

                                        st2 = st2 + 1;


                                        if (Integer.toString(st).length() > 5) {
                                            st = 25;
                                        }
                                        step = st;

                                        for (StepListener stepListener : mStepListeners) {
                                            stepListener.onStep();
                                            System.out.println("===============================step taken==========");
                                        }
                                        mLastMatch = extType;
                                    } else {
                                        mLastMatch = -1;
                                    }
                                }
                                mLastDiff[k] = diff;
                            }
                            mLastDirections[k] = direction;
                            mLastValues[k] = v;
                        } else {

                            // System.out.println("===========================GOOGLEFIT  WORKING========"+gfitSteps);
                            //  Ram.setCurrentWalkStepCount(st);
                            //   Ram.setCurrentRealStepCount(st);
                            //  sendBroadcast(st);
                        }
                        //================================================================================

                    } else if (googlefitenabled.equals("false")) {
                        //  System.out.println("===========================GOOLEFIT  NOT ACTIVATED=========");
                        int j = (portrait.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                        if (j == 1) {
                            float vSum = 0;
                            for (int i = 0; i < 3; i++) {
                                final float v = mYOffset + se.values[i] * mScale[j];
                                vSum += v;
                            }
                            int k = 0;
                            float v = vSum / 3;

                            float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                            if (direction == -mLastDirections[k]) {
                                // Direction changed
                                int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                                mLastExtremes[extType][k] = mLastValues[k];
                                float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

                                if (diff > mLimit) {

                                    boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                                    boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                                    boolean isNotContra = (mLastMatch != 1 - extType);

                                    if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {

                                        st = st + 1;

                                        st2 = st2 + 1;
                                        System.out.println("=======step=========" + st);
                                        if (Integer.toString(st).length() > 5) {
                                            st = 25;
                                        }
                                        step = st;

//                                        Ram.setCurrentWalkStepCount(st);
//
//                                        Ram.setCurrentRealStepCount(st);
//                                        //  Ram.setCurrentRealStepCount(0);
//
//                                        sendBroadcast(st);
                                        // }

                                        for (StepListener stepListener : mStepListeners) {
                                            stepListener.onStep();
                                            System.out.println("===============================step taken==========");
                                        }
                                        mLastMatch = extType;
                                    } else {
                                        mLastMatch = -1;
                                    }
                                }
                                mLastDiff[k] = diff;
                            }
                            mLastDirections[k] = direction;
                            mLastValues[k] = v;
                        } else {

                            // System.out.println("===========================GOOGLEFIT  WORKING========"+gfitSteps);
//                        Ram.setCurrentWalkStepCount(st);
//                        Ram.setCurrentRealStepCount(st);
//                        sendBroadcast(st);
                        }
                        //================================================================================
                    }


                }

            }


        }

        public void onAccuracyChanged(Sensor portrait, int accuracy) {
            /* can be ignored in this example */
        }


    };

    //===============================

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            @SuppressLint("MissingPermission") NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    @Override
    public void onStart(Intent intent, int startid) {
        //  Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        //  myPlayer.start();

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // (1)
        mySensorManager.registerListener(mySensorEventListener, mySensorManager
                        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST); // (2)
        mSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//=====================================================
        Bundle extras = intent.getExtras();
        if (extras == null)
            Log.d("Service", "null");
        else {
            Log.d("Service", "not null");
            String from = (String) extras.get("From");

        }

    }


    @Override
    public void onDestroy() {
        Log.d("AyuboService", "Killed");
        saveStepsDataToLoclDB();

        Intent intent = new Intent();
        intent.setAction("com.ayubo.life.ayubolife.MyServiceRestarter");
        sendBroadcast(intent);

    }

    public class MyBinder extends Binder {
        public AyuboStepService getService() {
            return AyuboStepService.this;
        }
    }


}



