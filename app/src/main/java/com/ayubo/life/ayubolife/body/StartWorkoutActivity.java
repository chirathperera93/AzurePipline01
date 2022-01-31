package com.ayubo.life.ayubolife.body;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class StartWorkoutActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    String dateInEndWorkout, sTimeDuration, status;
    private static final String TAG = StartWorkoutActivity.class.getSimpleName();
    int totWalkStepsDuringWorkout, totRunStepsDuringWorkout;

    private long startTime = 0L;
    boolean activityStart = true;
    boolean activityPause = false;
    boolean activityResume = false;
    //  List<LatLng> pointsNew = null;
    private Handler handler = new Handler();

    private Handler customHandler = new Handler();
    Timer timerRefresh;
    TimerTask timerTask;
    private ProgressDialog progressDialog;
    Timer timer;
    Timer timerDornut;
    Timer timerGps;
    TimerTask timerTaskGps;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    long timeInSeconds = 0L;
    int stepsWalkAtWorkoutStop = 0;
    //  int stepsRunAtWorkoutStop = 0;
    int stepsWalsAtWorkoutStart = 0;
    // int stepsRunAtWorkoutStart = 0;
    String startTimeNew;
    String endTimeNew;
    boolean wStarted = false;
    int STEPCOUNT_FOR_A_WORKOUT_TIME = 0;
    boolean checkloopturn = false;
    int stepDelta2 = 0;
    float energDelta2 = 0;
    float enegyPresntageDetla2 = 0;
    float caleryDetla2 = 0;
    int duration2 = 0;
    int distanceDetla2 = 0;
    float metsForWorkout_PerMinit;
    String mmets, activityName, activityId, statusFromServiceAPI_db;
    ImageButton workout_btn_stop;
    // ImageButton workout_btn_start_and_pause;
    int totalMinits;
    TextView txt_timer;
    boolean activityStartStated = false;
    boolean workoutSavedStatus = false;
    String sameDate;
    // int sameId, sameStep, sameRun, sameJump;
    float sameMets, sameCals;
    float sameEnPersent;
    java.util.Date date;
    DatabaseHandler db;
    String userid_ExistingUser;
    PowerManager pm = null;
    PowerManager.WakeLock wl = null;
    boolean screenSleepOn = false;
    SimpleDateFormat sdf;
    String current_day;
    int thisIsMyRealStepDetalt_Start = 0;
    int stepDelta = 0;
    int typeDetla = 0;
    ImageButton btn_backImgBtn;
    PrefManager pref;
    LocationManager locationManager;
    // CircleProgressView btn_iv;
    ImageButton workout_btn_resume;
    TextView txt_staps;
    com.google.android.gms.maps.MapView mapView;
    GoogleMap googleMap;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 432;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION_MANAGER = 433;
    GoogleMap mGoogleMap;
    //GPSTracker gps;
    private Polyline mMutablePolyline;
    private GoogleMap myMap;
    List<LatLng> points = null;
    LatLng sydney = null;
    boolean isStarted = false;
    boolean isWorkoutStarted = false;
    LatLng firstLocation = null;
    LatLng currentLocation = null;
    // LatLng startLocation = null;
    Double distance = 0.0;
    TextView txt_pressandhold, workout_heading;
    boolean isFistValue = false;
    TextView txt_mets, txt_dis, txt_ckal, txt_steps;
    LinearLayout mapView_lay;
    ImageView im;
    String sMets_ToIntent, sCals_ToIntent, googlefitenabled, sDis_ToIntent, sDur_ToIntent, stepsSentToServerStatusAtPause, step_deviceName;
    ImageButton workout_btn_start_and_pause;
    //    private DonutProgress donutProgress;
    private SensorManager mSensorManager;
    private Sensor mProximity;
    String workoutTimeDurations;
    private static String stepCount;
    private boolean isGoogleFitConnected = false;
    private GoogleApiClient mClient = null;
    int locatSteps_onEnd;
    int locatSteps_onStart;
    double myDisNew;
    double myDis = 0;
    float myDisFloat = 0;
    Sensor mSensor = null;
    LocationManager mLocationManager;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);
        //pointsNew = new ArrayList<LatLng>();


        //  setUpMap();

        isStarted = false;

        metsForWorkout_PerMinit = 6;

        points = new ArrayList<LatLng>();

        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        step_deviceName = pref.getDeviceData().get("stepdevice");
        googlefitenabled = pref.isGoogleFitEnabled();

        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        try {
            if (step_deviceName.equals("activity_AYUBO") && (googlefitenabled.equals("true"))) {
                buildFitnessClient();
                System.out.println("====AyuboLife and GoogleFit Enabled==========");
            } else {
                System.out.println("====AyuboLife and Native==========");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        boolean isGpsAvailable = isGpsAvailable();
        progressDialog = new ProgressDialog(this);
        //   gps = new GPSTracker(StartWorkoutActivity.this);
        // System.out.println("=====onResume==========" + gps.getLatitude());

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        txt_pressandhold = (TextView) findViewById(R.id.txt_pressandhold);
        workout_heading = (TextView) findViewById(R.id.workout_heading);

//        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);

        workout_btn_start_and_pause = (ImageButton) findViewById(R.id.workout_btn_start_and_pause);


        mapView_lay = (LinearLayout) findViewById(R.id.mapView_lay);

        txt_mets = (TextView) findViewById(R.id.txt_mets);
        txt_dis = (TextView) findViewById(R.id.txt_dis);
        txt_ckal = (TextView) findViewById(R.id.txt_ckal);
        txt_steps = (TextView) findViewById(R.id.txt_steps);
        //  im = (ImageView) findViewById(R.id.img);

        workout_btn_resume = (ImageButton) findViewById(R.id.workout_btn_resume);
        workout_btn_stop = (ImageButton) findViewById(R.id.workout_btn_stop);

        //  workout_btn_start_and_pause = (ImageButton) findViewById(R.id.workout_btn_start_and_pause);
        // btn_iv.setEnabled(false);

        txt_timer = (TextView) findViewById(R.id.txt_timer);
        txt_pressandhold.setTextColor(Color.parseColor("#f1f0ef"));

        workout_btn_resume.setVisibility(View.GONE);
        workout_btn_stop.setVisibility(View.GONE);

        activityName = Ram.getActivityName();

        if (activityName == null) {
            activityName = "a";
        }
        activityId = Ram.getActivityId();
        //mets = Ram.getMets();

        db = DatabaseHandler.getInstance(this);

        workout_heading.setText(activityName);

        try {
            pref = new PrefManager(StartWorkoutActivity.this);
            userid_ExistingUser = pref.getLoginUser().get("uid");

            if (activityName.equals("RUNNING") || activityName.equals("HIKING") || activityName.equals("CYCLING") || activityName.equals("WALKING")) {
                mapView = (com.google.android.gms.maps.MapView) findViewById(R.id.mapView);
                mapView.getMapAsync(this);
                mapView.onCreate(savedInstanceState);
                if (mapView != null) {
                    mapView.getMapAsync(this);

                }
            } else if (activityName.equals("BADMINTON")) {
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    mapView_lay.setBackgroundDrawable(getResources().getDrawable(R.drawable.bedm));
                } else {
                    mapView_lay.setBackground(getResources().getDrawable(R.drawable.bedm));
                }
            } else if (activityName.equals("GOLF")) {
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    mapView_lay.setBackgroundDrawable(getResources().getDrawable(R.drawable.golf_bg));
                } else {
                    mapView_lay.setBackground(getResources().getDrawable(R.drawable.golf_bg));
                }
            } else if (activityName.equals("BASKETBALL")) {
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    mapView_lay.setBackgroundDrawable(getResources().getDrawable(R.drawable.foodb));
                } else {
                    mapView_lay.setBackground(getResources().getDrawable(R.drawable.foodb));
                }
            } else if (activityName.equals("SWIMMING")) {
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    mapView_lay.setBackgroundDrawable(getResources().getDrawable(R.drawable.swim));
                } else {
                    mapView_lay.setBackground(getResources().getDrawable(R.drawable.swim));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (activityName.equals("RUNNING") || activityName.equals("HIKING") || activityName.equals("CYCLING") || activityName.equals("WALKING")) {

            Intent intent = new Intent(this, GPSService.class);
            startService(intent);
            //  mAlreadyStartedService = true;
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    new BroadcastReceiver() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onReceive(Context context, Intent intent) {

                            String latitude = intent.getStringExtra(GPSService.EXTRA_LATITUDE);
                            String longitude = intent.getStringExtra(GPSService.EXTRA_LONGITUDE);

                            //  System.out.println("====onReceive========" + latitude);

                            double lat = Double.parseDouble(latitude);
                            double lon = Double.parseDouble(longitude);

                            // = = FOCUS CURRENT LOCATION  START===============
                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, lon));
                            float zoomLevel = calculateZoomLevel(getScreenWidth());

                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomLevel);
                            mGoogleMap.moveCamera(center);
                            mGoogleMap.animateCamera(zoom);
                            // = = FOCUS CURRENT LOCATION  END===============
                            currentLocation = new LatLng(lat, lon);

                            //  Toast.makeText(StartWorkoutActivity.this, "gps  " + latitude, Toast.LENGTH_SHORT).show();

                            if (isWorkoutStarted) {

                                points.add(new LatLng(lat, lon));

                                if (latitude != null && longitude != null) {

                                    //  txt_view.setText( latitude + "\n Longitude: " + longitude);

                                    LatLng newLocation = new LatLng(lat, lon);

                                    if (currentLocation != null) {

                                        if (currentLocation.latitude != 0) {

                                            myDis += getDistance(currentLocation, newLocation);

                                            currentLocation = newLocation;

                                            DecimalFormat twoDForm = new DecimalFormat("#");
                                            Double d = Double.valueOf(twoDForm.format(myDis));
                                            //   Toast.makeText(getApplicationContext(), "Distance In Km  "+myDis, Toast.LENGTH_SHORT).show();

                                            System.out.println("==========my Walked Distance ==========" + myDis);

                                            if (!isFistValue) {
                                                distance = d;
                                                isFistValue = true;
                                            } else {
                                                if (distance < d) {
                                                    distance = d;
                                                } else {

                                                }
                                            }
                                        }
                                    }

                                }
                                if (lat == 0) {
                                } else {
                                    if (points != null) {
                                        for (int i = 0; i < points.size() - 1; i++) {
                                            LatLng src = points.get(i);
                                            LatLng dest = points.get(i + 1);

                                            mGoogleMap.addPolyline(new PolylineOptions()
                                                    .add(new LatLng(src.latitude, src.longitude),
                                                            new LatLng(dest.latitude, dest.longitude))
                                                    .width(13)
                                                    .startCap(getStartCap())
                                                    .endCap(getEndCap())
                                                    .jointType(getJointType())
                                                    .color(Color.BLUE)
                                                    .geodesic(true)
                                            );
                                            if (ActivityCompat.checkSelfPermission(StartWorkoutActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StartWorkoutActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                return;
                                            }
                                            mGoogleMap.setMyLocationEnabled(true);

                                        }
                                    }
                                }
                            }
                            //====================================
                        }
                    }, new IntentFilter(GPSService.ACTION_LOCATION_BROADCAST)
            );


            //=================================================

        }


        workout_btn_stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (Utility.isInternetAvailable(StartWorkoutActivity.this)) {

                    try {
                        stopService(new Intent(StartWorkoutActivity.this, GPSService.class));
                        // System.out.println("============stopService=================");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (duration2 > 0) {
                        stopClick();
                    } else {
                        showAlertDialog("Workout cannot be saved. Activity too short");
                    }

                } else {
                    getForgotPassword().show();

                }


            }
        });

        workout_btn_resume.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedResume();
            }
        });

//        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);

        workout_btn_start_and_pause.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                int myColor = Color.parseColor("#58b957");

//                donutProgress.setFinishedStrokeColor(myColor);
                isStarted = false;

                timerDornut = new Timer();
//                timerDornut.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                boolean a = false;
//                                if (a) {
//                                    @SuppressLint("ObjectAnimatorBinding") ObjectAnimator anim = ObjectAnimator.ofInt(donutProgress, ".", 0, 10);
//                                    anim.setInterpolator(new DecelerateInterpolator());
//                                    anim.setDuration(5000);
//                                    anim.start();
//                                } else {
//                                    AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(StartWorkoutActivity.this, R.animator.progress_anim);
//                                    set.setInterpolator(new DecelerateInterpolator());
//                                    set.setTarget(donutProgress);
//                                    set.start();
//                                }
//                            }
//                        });
//
//
//                    }
//
//                }, 0, 5000);


                //  workout_btn_start_and_pause.setVisibility(View.GONE);

                //   clickStart();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        txt_pressandhold.setVisibility(View.GONE);

                        workout_btn_resume.setVisibility(View.VISIBLE);
                        workout_btn_stop.setVisibility(View.VISIBLE);
                        workout_btn_start_and_pause.setVisibility(View.GONE);
                        txt_pressandhold.setTextColor(Color.parseColor("#f1f0ef"));
//                        donutProgress.setVisibility(View.GONE);
                        timerDornut.cancel();
                        clickedPause();
                    }
                }, 2500);

                return true;
            }
        });


        workout_btn_start_and_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (activityName.equals("RUNNING") || activityName.equals("HIKING") || activityName.equals("CYCLING") || activityName.equals("WALKING")) {
                    try {
                        turnGPSOn();

                        isWorkoutStarted = true;
                        //   startTimerGps();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {

                    if (isStarted) {
                        playAudioRecord("pressandhold");
                        txt_pressandhold.setTextColor(Color.parseColor("#000000"));

                    } else {
                        //Means clicked on Play
                        isStarted = true;
                        workout_btn_start_and_pause.setBackgroundResource(R.drawable.pause);

                        workout_btn_resume.setVisibility(View.GONE);
                        workout_btn_stop.setVisibility(View.GONE);

                        clickStart();

                        System.out.println("========Means clicked on Play======");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");

        sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date dae = new Date();
        current_day = sdf.format(dae);


        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isStarted) {

                } else {
                    stoperviceAtivities();
                    finish();
                }

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        if (activityName.equals("RUNNING") || activityName.equals("HIKING") || activityName.equals("CYCLING") || activityName.equals("WALKING")) {
            this.mapView.onResume();
            //   gps = new GPSTracker(StartWorkoutActivity.this);

        }

        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }


    private boolean isGpsAvailable() {

        boolean gpsCon = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Toast.makeText(this, "GPS is Enabled.", Toast.LENGTH_SHORT).show();
            gpsCon = true;
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("GPS is disabled!")
                    .setMessage("Without GPS this application will not work! Would you like to enable the GPS?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent callGpsSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGpsSetting);
                        }
                    })
                    .setNegativeButton("Exit.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    })
                    .show();
        }
        return gpsCon;
    }

    private double getDistance(LatLng first, LatLng second) {

        Location startPoint = new Location("locationA");
        startPoint.setLatitude(first.latitude);
        startPoint.setLongitude(first.longitude);

        Location endPoint = new Location("locationB");
        endPoint.setLatitude(second.latitude);
        endPoint.setLongitude(second.longitude);

        double distance = startPoint.distanceTo(endPoint);

        return distance;
    }


    @SuppressLint("MissingPermission")
    void drawMapPath(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(StartWorkoutActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("====drawMapPath===NO =PERMISSION_GRANTED=========");
            //    requestCameraPermission();
        } else {
            mGoogleMap.setMyLocationEnabled(true);
        }
        try {
            if (currentLocation != null) {

                System.out.println("====currentLocation========");
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
            } else {

                LatLng firstLocation = new LatLng(6.914401, 79.859785);
                //  currentLocation= new LatLng(6.914401, 79.859785);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 16));

                System.out.println("====currentLocation==Empty======");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<PatternItem> getSelectedPattern() {

        return null;
    }

    private Cap getEndCap() {

        return new RoundCap();

    }

    private Cap getStartCap() {

        return new RoundCap();

    }

    private int getJointType() {
        return JointType.ROUND;
    }


    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());

        int height = deviceScreenDimension.getDisplayHeight();
        int width = deviceScreenDimension.getDisplayWidth();
        return width;
    }

    private float calculateZoomLevel(int screenWidth) {
        float fZoom = 8.3f;
        int distance = (int) myDis;
        int dens = getScreenWidth();
        // double val
        if (500 > distance) {
            fZoom = 17.00f;
        } else if (0.298 * dens > distance) {
            fZoom = 19.00f;
        } else if (0.596 * dens > distance) {
            fZoom = 18.00f;
        } else if (1.193 * dens > distance) {
            fZoom = 17.00f;
        } else if (2.387 * dens > distance) {
            fZoom = 16.00f;
        } else if (4.773 * dens > distance) {
            fZoom = 15.00f;
        } else if (9.547 * dens > distance) {
            fZoom = 14.00f;
        } else if (19.093 * dens > distance) {
            fZoom = 13.00f;
        } else if (38.187 * dens > distance) {
            fZoom = 12.00f;
        }
        //=================================
        else if (76.373 * dens > distance) {
            fZoom = 11.00f;
        }

//        double equatorLength = 40075004; // in meters 40,075
//        double widthInPixels = screenWidth;
//        double metersPerPixel = equatorLength / 256;
//        Integer disNew = distance.intValue();
//        int scale = 0;   int zoomLevel = 1;
//        disNew=disNew*2;
//        if (disNew < 400) {
//            zoomLevel=17; //    150 M
//        }
//        else if ((disNew > 400) && (disNew < 1000)) {
//            zoomLevel=16;    //  1KM
//        }
//        else if ((disNew > 1000) && (disNew < 2000)) {
//            zoomLevel=15;   //  1.5 KM
//        }
//        else if ((disNew > 2000) && (disNew < 4800)) {
//            zoomLevel=14;
//        }
//        else if ((disNew > 4800) && (disNew < 9000)) {
//            zoomLevel=13; //  4KM
//        }
//        else if ((disNew > 9000) && (disNew < 14000)) {
//            zoomLevel=12; //  4KM
//        }
//        else if ((disNew > 14000) && (disNew < 20000)) {
//            zoomLevel=11;
//        }
//        else if ((disNew > 20000) && (disNew < 30000)) {
//            zoomLevel=10;
//        }
//        else if ((disNew > 30000) && (disNew < 50000)) {
//            zoomLevel=9;
//        }
//        else {
//            zoomLevel=8;
//        }

        Log.i("ADNAN", "=====================zoom level = " + fZoom);
        return fZoom;
    }

    @Override
    public void onMapReady(GoogleMap map) {

        try {
            if (activityName.equals("RUNNING") || activityName.equals("HIKING") || activityName.equals("CYCLING") || activityName.equals("WALKING")) {
                mGoogleMap = map;
                System.out.println("====onMapReady========");
                drawMapPath(mGoogleMap);
                System.out.println("====onMapReady========");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stoperviceAtivities();
    }

    private void stoperviceAtivities() {
        isStarted = false;
        try {
            mSensorManager.unregisterListener(this);
            stopService(new Intent(this, GPSService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        mSensorManager.unregisterListener(this);
    }

    private void buildFitnessClient() {
        // Create the Google API Client

        mClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.SENSORS_API)
                .addScope(Fitness.SCOPE_ACTIVITY_READ)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle bundle) {
                                Log.i(TAG, "Connected!!!");
                                pref.setGoogleFitEnabled("true");
                                // Now you can make calls to the Fitness APIs.  What to do?
                                // Look at some data!!
                                //  subscribe();
                                isGoogleFitConnected = true;


                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the portrait gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
                                }
                            }
                        }
                )
                .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.i(TAG, "Google Play services connection failed. Cause: " +
                                result.toString());
//                        Snackbar.make(
//                                FoogleFitConnect_Activity.this.findViewById(R.id.main_activity_view),
//                                "Exception while connecting to Google Play services: " +
//                                        result.getErrorMessage(),
//                                Snackbar.LENGTH_INDEFINITE).show();
                    }
                })
                .build();
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
    }


    private void showAlertDialog(String message) {

        new AlertDialog.Builder(this)
                .setTitle("Workout cannot be saved")
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(StartWorkoutActivity.this, StartWorkoutActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })

                .show();
    }

    public void startTimerGps() {

        timerGps = new Timer();

        //   initializeTimerTaskGps();

        timerGps.schedule(timerTaskGps, 10, 30000); //

    }


    public void initializeTimerTaskGps() {

        timerTaskGps = new TimerTask() {

            public void run() {


                handler.post(new Runnable() {

                    public void run() {

                        //   android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);

                        try {
                            //  gps = new GPSTracker(getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // String dis1 = Double.toString(myDis);
                        // String dis2=String.valueOf(myDisFloat);

                        //    Toast.makeText(getApplicationContext(), "Distance In Km  "+dis1+"   "+dis2, Toast.LENGTH_SHORT).show();


                        try {

                            System.out.println("  Timer Running =============================");
//                        if (gps.canGetLocation()) {
//                            double latitude = gps.getLatitude();
//                            double longitude = gps.getLongitude();
//
//                            if (longitude == 0) {
//                            } else {
//
//                                LatLng PERTH4 = new LatLng(latitude, longitude);
//                                pointsNew.add(PERTH4);
//                                Ram.setPointsNew(pointsNew);
//                                List<LatLng> pointsNew = Ram.getPointsNew();
//                                points = pointsNew;
//                                if (points != null) {
//                                    for (int i = 0; i < points.size() - 1; i++) {
//                                        LatLng src = points.get(i);
//                                        LatLng dest = points.get(i + 1);
//
//                                        mGoogleMap.addPolyline(new PolylineOptions()
//                                                .add(new LatLng(src.latitude, src.longitude),
//                                                        new LatLng(dest.latitude, dest.longitude))
//                                                .width(13)
//                                                .startCap(getStartCap())
//                                                .endCap(getEndCap())
//                                                .jointType(getJointType())
//                                                .color(Color.BLUE)
//                                                .geodesic(true)
//                                        );
//                                        if (ActivityCompat.checkSelfPermission(StartWorkoutActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StartWorkoutActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                            return;
//                                        }
//                                        mGoogleMap.setMyLocationEnabled(true);
//
//                                    }
//                                }
//                            }
//                        }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                });

            }

        };

    }


    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    public int getZoomLevel(Circle circle) {
        int zoomLevel = 0;
        if (circle != null) {
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            PowerManager.WakeLock wl;
            boolean a = false;
            if (event.values[0] == 0) {
                //near
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                wl = pm.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "MyWakeLock");
                wl.acquire();

                a = true;
                // Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
            } else {
                //far
                if (a) {


                    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    wl = pm.newWakeLock(PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
                    wl.acquire();
                } else {

                }

            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor portrait, int i) {

    }


//    public class GPSTracker extends Service implements LocationListener {
//
//        private final Context mContext;
//        boolean isGPSEnabled = false;
//        boolean isNetworkEnabled = false;
//        boolean canGetLocation = false;
//        Location location; // Location
//        double latitude; // Latitude
//        double longitude; // Longitude
//        List<LatLng> pointsNew=null;
//        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
//      //  private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
//       // private static final long MIN_TIME_BW_UPDATES = 1000 * 15 * 1; // 1 minute
//        private static final long MIN_TIME_BW_UPDATES = 1000 * 5 * 1; // 1 minute
//        //  private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
//        // Declaring a Location Manager
//        protected LocationManager locationManager;
//
//        public GPSTracker(Context context) {
//            this.mContext = context;
//            getLocation();
//            pointsNew = new ArrayList<LatLng>();
//        }
//
//        public Location getLocation() {
//            try {
//                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
//                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//                if (!isGPSEnabled && !isNetworkEnabled) {
//                    // No network provider is enabled
//                } else {
//                    this.canGetLocation = true;
//
//                    // If GPS enabled, get latitude/longitude using GPS Services
//                    if (isGPSEnabled) {
//                        if (location == null) {
//                            pointsNew = new ArrayList<LatLng>();
//
//                           // setUpMap();
//
//                            if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(StartWorkoutActivity.this,
//                                    Manifest.permission.ACCESS_FINE_LOCATION)
//                                    != PackageManager.PERMISSION_GRANTED)) {
//                                requestPermissions(new String[]{
//                                                Manifest.permission.ACCESS_FINE_LOCATION},
//                                        MY_PERMISSIONS_REQUEST_LOCATION_MANAGER);
//
//                                turnGPSOn();
//
//                            }
//
//                            locationManager.requestLocationUpdates(
//                                    LocationManager.GPS_PROVIDER,
//                                    MIN_TIME_BW_UPDATES,
//                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//
//                            Log.d("GPS Enabled", "GPS Enabled");
//                            if (locationManager != null) {
//                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                                if(location == null){
//                                    locationManager.requestLocationUpdates(
//                                            LocationManager.GPS_PROVIDER,
//                                            1000,
//                                            1, this);
//                                }
//                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                                if (location != null) {
//                                    latitude = location.getLatitude();
//                                    longitude = location.getLongitude();
//
//
//                                    Log.d("GPS Enabled", "GPS ========latitude=============="+latitude);
//                                    Log.d("GPS Enabled", "GPS ========longitude=============="+longitude);
//
//                                    //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        }
//                    }
//
//                    else if (isNetworkEnabled) {
//                        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(StartWorkoutActivity.this,
//                                Manifest.permission.RECORD_AUDIO)
//                                != PackageManager.PERMISSION_GRANTED)) {
//                            requestPermissions(new String[]{
//                                            Manifest.permission.RECORD_AUDIO,
//                                            Manifest.permission.CAMERA},
//                                    MY_PERMISSIONS_REQUEST_LOCATION_MANAGER);
//
//                        }
//                        locationManager.requestLocationUpdates(
//                                LocationManager.NETWORK_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        Log.d("Network", "Network");
//                        if (locationManager != null) {
//                            location = locationManager
//                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                            if (location != null) {
//                                latitude = location.getLatitude();
//                                longitude = location.getLongitude();
//                            }
//                        }
//                    }
//
//                }
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return location;
//        }
//
//
//        /**
//         * Stop using GPS listener
//         * Calling this function will stop using GPS in your app.
//         * */
//        public void stopUsingGPS(){
//            if(locationManager != null){
//                locationManager.removeUpdates(GPSTracker.this);
//            }
//        }
//        @Override
//        public void onCreate() {
//            super.onCreate();
//
//            //  startTimer();
//
//        }
//        @Override
//        public int onStartCommand(Intent intent, int flags, int startId) {
//            //TODO do something useful
//
//
//            //  startTimer();
//
//            return Service.START_NOT_STICKY;
//        }
//
//
//
//
//        /**
//         * Function to get latitude
//         * */
//        public double getLatitude(){
//            if(location != null){
//                latitude = location.getLatitude();
//            }
//
//            // return latitude
//            return latitude;
//        }
//
//
//        /**
//         * Function to get longitude
//         * */
//        public double getLongitude(){
//            if(location != null){
//                longitude = location.getLongitude();
//            }
//
//            // return longitude
//            return longitude;
//        }
//
//        /**
//         * Function to check GPS/Wi-Fi enabled
//         * @return boolean
//         * */
//        public boolean canGetLocation() {
//            return this.canGetLocation;
//        }
//
//
//        /**
//         * Function to show settings alert dialog.
//         * On pressing the Settings button it will launch Settings Options.
//         * */
//
//
//        @Override
//        public void onDestroy() {
//            super.onDestroy();
//            stopUsingGPS();
//
//        }
//
//        @Override
//        public void onLocationChanged(Location location) {
//
//            LatLng newLocation = new LatLng(location.getLatitude(),location.getLongitude());
//
//            if(startLocation!=null){
//
//                if(startLocation.latitude!=0){
//
//                    myDis += getDistance(startLocation, newLocation);
//
//                    startLocation=newLocation;
//
//                    DecimalFormat twoDForm = new DecimalFormat("#");
//                    Double d = Double.valueOf(twoDForm.format(myDis));
//
//                    if (!isFistValue) {
//                        distance = d;
//                        isFistValue=true;
//                    } else {
//                        if (distance < d) {
//                            distance = d;
//                        } else {
//
//                        }
//                    }
//
//                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
//
//                    float zoomLevel = calculateZoomLevel(getScreenWidth());
//
//                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomLevel);
//                    mGoogleMap.moveCamera(center);
//                    mGoogleMap.animateCamera(zoom);
//
//
//                }
//            }
//        }
//
//
//        @Override
//        public void onProviderDisabled(String provider) {
//        }
//
//
//        @Override
//        public void onProviderEnabled(String provider) {
//        }
//
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//
//
//        @Nullable
//        @Override
//        public IBinder onBind(Intent intent) {
//            return null;
//        }
//    }

    void playAudioRecord(String fileName) {

        int resID = getResources().getIdentifier(fileName, "raw", getPackageName());
        MediaPlayer mediaPlayer = MediaPlayer.create(this, resID);
        mediaPlayer.start();
    }


    void clickStart() {
        playAudioRecord("started");
        startTime = SystemClock.uptimeMillis();

        customHandler.postDelayed(updateTimerThread, 0);

        //  FirebaseAnalytics Adding
        Bundle bWorkoutParams = new Bundle();
        bWorkoutParams.putString("type", activityName);
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Workout_started", bWorkoutParams);
        }


        activityPause = true;
        wStarted = true;
        activityStartStated = true;
        thisIsMyRealStepDetalt_Start = stepDelta;

        if (activityStart) {
            workoutSavedStatus = true;

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            long sttTime = c.get(Calendar.MILLISECOND);

            date = new java.util.Date();
            startTimeNew = (new Timestamp(date.getTime())).toString();

            Ram.setWorkoutStarted(true);

            //  startTimer();
            startTime = SystemClock.uptimeMillis();


            activityStart = false;
            activityResume = false;
            activityPause = true;

            try {

                if (step_deviceName.equals("activity_AYUBO")) {

                    check_buildFitnessClient();

                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    private void check_buildFitnessClient() {
        // Create the Google API Client

        if (mClient != null) {
            System.out.println("==========mClient not null=====================");
            if (mClient.isConnected()) {
                isGoogleFitConnected = true;
                try {


                    isGoogleFitConnected = true;
                    pref.setGoogleFitEnabled("true");
                    googlefitenabled = pref.isGoogleFitEnabled();

                    if (!isFinishing()) {

                        try {
                            new InsertAndVerifyDataTaskStart().execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                mClient.connect();

                if (mClient.isConnected()) {
                    try {
                        new InsertAndVerifyDataTaskStart().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        } else {
            System.out.println("========== FIT Not Connected=====================");
            mClient = new GoogleApiClient.Builder(this)
                    .addApi(Fitness.RECORDING_API)
                    .addApi(Fitness.HISTORY_API)
                    .addApi(Fitness.SENSORS_API)
                    .addScope(Fitness.SCOPE_ACTIVITY_READ)
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                    .addConnectionCallbacks(
                            new GoogleApiClient.ConnectionCallbacks() {
                                @Override
                                public void onConnected(Bundle bundle) {
                                    Log.i(TAG, "Connected!!!");
                                    isGoogleFitConnected = true;

                                    try {
                                        new InsertAndVerifyDataTaskStart().execute();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onConnectionSuspended(int i) {
                                    // If your connection to the portrait gets lost at some point,
                                    // you'll be able to determine the reason and react to it here.
                                    pref.setGoogleFitEnabled("false");
                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                        Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                    } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                        Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
                                    }
                                }
                            }
                    )
                    .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.i(TAG, "Google Play services connection failed. Cause: " +
                                    result.toString());
                            pref.setGoogleFitEnabled("false");
                            Snackbar.make(StartWorkoutActivity.this.findViewById(android.R.id.content),
                                    "Unable to connect GoogleFit",
                                    Snackbar.LENGTH_INDEFINITE).setAction("DISMISS",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();

                        }
                    })
                    .build();
        }
    }


    void stopClick() {
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait. Saving workout..");
        playAudioRecord("workoutstopped");

        if (activityName.equals("RUNNING") || activityName.equals("HIKING") || activityName.equals("CYCLING") || activityName.equals("WALKING")) {
            takeSnapshotGoooooooooooooo();
        } else {
            Bitmap snapshot = takeScreenshotNew(mapView_lay);
            Ram.setMapSreenshot(snapshot);
        }
        date = new java.util.Date();
        endTimeNew = (new Timestamp(date.getTime())).toString();

        workoutSavedStatus = false;
        isStarted = false;

        saveWorkoutData();

        if (screenSleepOn) {
            wl.release();
        }

    }

    public static Bitmap takeScreenshotNew(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;


    }

    private void saveWorkoutData() {
        Ram.setWorkoutStarted(false);

        Date dae = new Date();

        String dateInEndWorkouto = sdf.format(dae);
        dateInEndWorkout = dateInEndWorkouto.trim();

        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);

        totWalkStepsDuringWorkout = 0;
        totRunStepsDuringWorkout = 0;

        updatedTime = 0;
        timeSwapBuff = 0;
        timeInMilliseconds = 0;

        activityStart = false;
        activityResume = false;
        activityPause = false;

        try {
            totWalkStepsDuringWorkout = stepsWalkAtWorkoutStop - stepsWalsAtWorkoutStart;
            status = "1";
            sTimeDuration = Integer.toString(duration2);

            saveWorkout();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void clickedResume() {


        playAudioRecord("workoutresumed");
        txt_pressandhold.setVisibility(View.VISIBLE);
        int myColor = Color.parseColor("#eeeeee");
        //   int color = Color.argb(238, 238, 238, Color.GRAY);
//        donutProgress.setFinishedStrokeColor(myColor);

        isStarted = true;
        workout_btn_start_and_pause.setBackgroundResource(R.drawable.work_pause);
        workout_btn_start_and_pause.setVisibility(View.VISIBLE);
        txt_pressandhold.setTextColor(Color.parseColor("#000000"));
//        donutProgress.setVisibility(View.VISIBLE);

        workout_btn_resume.setVisibility(View.GONE);
        workout_btn_stop.setVisibility(View.GONE);


        timeSwapBuff = updatedTime;
        customHandler.removeCallbacks(updateTimerThread);
        System.out.println("Resume Clicked=======================================================");
        // startTimer();
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);

        activityStart = false;
        activityResume = false;
        activityPause = true;
    }

    void clickedPause() {

        //  playAudioRecord("workoutpaused");
        timeSwapBuff = updatedTime;
        customHandler.removeCallbacks(updateTimerThread);
        System.out.println("Pause Clicked=======================================================");

        activityPause = false;
        activityStart = false;
        activityResume = true;
    }

    public static void printData(DataReadResult dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }

    private static void dumpDataSet(DataSet dataSet) {
        //  Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        if (dataSet.isEmpty()) {
            System.out.println("========Empty======GFit Steps============");
            stepCount = "0";
        } else {

            for (DataPoint dp : dataSet.getDataPoints()) {
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());
                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {
                    Log.i(TAG, "\tField: " + field.getName() +
                            "GFit Steps Value: " + dp.getValue(field));

                    stepCount = dp.getValue(field).toString();
                    System.out.println("==============GFit Steps Value:==================" + stepCount);
                }
            }
        }
    }


    private class InsertAndVerifyDataTaskStart extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            DataReadRequest readRequest = queryFitnessData();
            DataReadResult dataReadResult =
                    Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);
            printData(dataReadResult);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (stepCount != null) {

                if (stepCount.equals("0")) {
                    stepsWalsAtWorkoutStart = 0;
                } else {
                    stepsWalsAtWorkoutStart = Integer.parseInt(stepCount);
                    //    Toast.makeText(StartWorkoutActivity.this, "steps"+stepCount, Toast.LENGTH_SHORT).show();

                }

                customHandler.postDelayed(updateTimerThread, 0);
            }
        }
    }

    private class InsertAndVerifyDataTaskStop extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            DataReadRequest readRequest = queryFitnessData();
            DataReadResult dataReadResult =
                    Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);
            printData(dataReadResult);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("===================Got Data=============InsertAndVerifyDataTaskStop");
            if (stepCount != null) {

                if (stepCount.equals("0")) {
                    stepsWalkAtWorkoutStop = 0;
                } else {
                    stepsWalkAtWorkoutStop = Integer.parseInt(stepCount);
                }

                totWalkStepsDuringWorkout = stepsWalkAtWorkoutStop - stepsWalsAtWorkoutStart;

                // Toast.makeText(StartWorkoutActivity.this, "w steps"+Integer.toString(stepsWalkAtWorkoutStop), Toast.LENGTH_SHORT).show();

                status = "1";

                sTimeDuration = Integer.toString(duration2);

                saveWorkout();

                System.out.println("============================saveWorkout");
            }
        }
    }

    private class InsertAndVerifyDataTaskNow extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            DataReadRequest readRequest = queryFitnessData();
            DataReadResult dataReadResult =
                    Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);
            printData(dataReadResult);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (stepCount != null) {

                if (stepCount.equals("0")) {
                    stepsWalkAtWorkoutStop = 0;
                } else {
                    stepsWalkAtWorkoutStop = Integer.parseInt(stepCount);
                }

                if (stepsWalkAtWorkoutStop > stepsWalsAtWorkoutStart) {

                    int totWalkStepsDuringWorkout = stepsWalkAtWorkoutStop - stepsWalsAtWorkoutStart;

                    STEPCOUNT_FOR_A_WORKOUT_TIME = totWalkStepsDuringWorkout;

                    double h = (170 / 100);
                    Double stepLength = 0.45 * h;

                    Double stepDistance = stepLength * STEPCOUNT_FOR_A_WORKOUT_TIME;
                    Double distanceInKm = stepDistance / 1000;

                    stepDelta2 = STEPCOUNT_FOR_A_WORKOUT_TIME;

                    DecimalFormat twoDForm = new DecimalFormat("#.##");
                    // Double distanceInKmnew=myDis/1000;
                    Double distanceInKmm;

                    if (myDis == 0) {
                        double dSteps = (double) STEPCOUNT_FOR_A_WORKOUT_TIME;
                        double d78 = (double) 78;
                        double dLax = (double) 100000;
                        distanceInKmm = (dSteps * d78) / dLax;
                    } else {
                        distanceInKmm = myDis / 1000;
                    }

                    distanceDetla2 = distanceInKmm.intValue();
                    String numberAsString = twoDForm.format(distanceInKmm);

                    txt_dis.setText(numberAsString);
                    txt_steps.setText(Integer.toString(STEPCOUNT_FOR_A_WORKOUT_TIME));


                } else {
                    txt_dis.setText("0");
                    txt_steps.setText("0");
                }


            }
        }
    }

    public static DataReadRequest queryFitnessData() {

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();

        // today
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        long midNight = date.getTimeInMillis();


        java.text.DateFormat dateFormat = getDateInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(midNight));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(ESTIMATED_STEP_DELTAS, DataType.TYPE_STEP_COUNT_DELTA)
                // .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                // .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                // .aggregate(DataType.TYPE_ACTIVITY_SEGMENT, DataType.AGGREGATE_ACTIVITY_SUMMARY)
                .bucketByTime(24, TimeUnit.HOURS)
                .setTimeRange(midNight, endTime, TimeUnit.MILLISECONDS)
                .build();

        return readRequest;
    }


    @Override
    public void onBackPressed() {
        if (isStarted) {
        } else {
            stoperviceAtivities();
            finish();
        }
    }


    private void takeSnapshotGoooooooooooooo() {
        final GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                Ram.setMapSreenshot(snapshot);
            }
        };
        mGoogleMap.snapshot(callback);
    }


    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            // int hour = (int) (updatedTime / 3600000);
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
//==================================
            timeInSeconds = updatedTime / 1000;
            int hoursNew = (int) (timeInSeconds / 3600);
            int remainder = (int) timeInSeconds - hoursNew * 3600;
            int minsnNew = remainder / 60;
            remainder = remainder - minsnNew * 60;
            int secsNew = remainder;

            if (secs == 0) {
                updateWorkoutUI(mins);
            }
            workoutTimeDurations = String.format("%02d", hoursNew) + ":"
                    + String.format("%02d", minsnNew) + ":"
                    + String.format("%02d", secsNew);

            txt_timer.setText(workoutTimeDurations);

            totalMinits = mins;
            customHandler.postDelayed(this, 100);
        }
    };

    public float calculateMetValusForTime(int minit) {
        float metValue2 = 0;
        metValue2 = metsForWorkout_PerMinit * minit;
        return metValue2;
    }

    private void updateWorkoutUI(int mins) {

        DecimalFormat dfff = new DecimalFormat("0");

        float CurrentTotalMet = calculateMetValusForTime(mins);

        if (step_deviceName.equals("activity_AYUBO")) {

            try {
                new InsertAndVerifyDataTaskNow().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        float w = 70;
        double calrsTot = CaleriesBurnWithMets(CurrentTotalMet, w);
        float BurnedCaleries = (float) calrsTot;

        Ram.setMETS(CurrentTotalMet);

        String str_FINAL_FULL_MET_COUNT = dfff.format(CurrentTotalMet);
        String str_FINAL_FULL_CAL_COUNT = dfff.format(calrsTot);

        energDelta2 = CurrentTotalMet;
        enegyPresntageDetla2 = CurrentTotalMet / 500;
        caleryDetla2 = BurnedCaleries;
        duration2 = mins;

        sMets_ToIntent = str_FINAL_FULL_MET_COUNT;
        sCals_ToIntent = str_FINAL_FULL_CAL_COUNT;

        txt_mets.setText(str_FINAL_FULL_MET_COUNT);
        txt_ckal.setText(str_FINAL_FULL_CAL_COUNT);

    }


    public double CaleriesBurnWithMets(double mets, double weiget) {
        double newMets = mets;
        double newWeightInKg = weiget;
        double burnedCalories = (newMets * 3.5 * newWeightInKg) / 200;
        System.out.println("====================CALs in Start Activity=================     " + burnedCalories);
        return burnedCalories;
    }

    private AlertDialog getForgotPassword() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(StartWorkoutActivity.this);
        final LayoutInflater inflater = this.getLayoutInflater();
        final View layoutView = inflater.inflate(R.layout.no_internet_alert_workout, null, false);
        builder2.setView(layoutView);
        builder2.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {


            }
        });
        return builder2.create();
    }

    public void saveWorkout() {
        if (Utility.isInternetAvailable(this)) {
            System.out.println("===================Starting=============updateOnlineWorkoutDetails");
            updateOnlineWorkoutDetails task = new updateOnlineWorkoutDetails();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {

            Toast.makeText(getApplicationContext(), "Cannot find active internet connection",
                    Toast.LENGTH_LONG).show();
        }
    }


    private class updateOnlineWorkoutDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            makePostRequest_updateOnlineWorkoutDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            System.out.println("===================onPostExecute=============updateOnlineWorkoutDetails");
            //  Toast.makeText(StartWorkoutActivity.this, " saved0", Toast.LENGTH_SHORT).show();
            if (statusFromServiceAPI_db.equals("0")) {


                Double distnceInKm = 0.0;

                //Toast.makeText(StartWorkoutActivity.this, " saved", Toast.LENGTH_SHORT).show();
                DecimalFormat twoDForm = new DecimalFormat("#.##");
                if (myDis == 0) {
                    double dSteps = (double) totWalkStepsDuringWorkout;
                    double d78 = (double) 78;
                    double dLax = (double) 100000;
                    distnceInKm = (dSteps * d78) / dLax;
                } else {
                    distnceInKm = myDis / 1000;
                }

                String numberAsString = twoDForm.format(distnceInKm);

                Intent intent = new Intent(StartWorkoutActivity.this, ShareWorkoutActivity.class);
                intent.putExtra("mets", sMets_ToIntent);
                intent.putExtra("cals", sCals_ToIntent);
                intent.putExtra("steps", totWalkStepsDuringWorkout);
                intent.putExtra("dis", numberAsString);
                intent.putExtra("durWithDetails", txt_timer.getText());
                intent.putExtra("dur", sTimeDuration);
                intent.putExtra("activityName", activityName);

                startActivity(intent);
                finish();
            } else {

                Toast.makeText(StartWorkoutActivity.this, "Network error.Workout cannot be saved. This could possibly be a connection issue.", Toast.LENGTH_LONG).show();

            }
        }

        private void makePostRequest_updateOnlineWorkoutDetails() {
            //  prgDialog.show();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;


            String jsonStr =
                    "{" +
                            "\"userid\": \"" + userid_ExistingUser + "\"," +
                            "\"workout_type\": \"" + activityName + "\"," +
                            "\"start_time\": \"" + startTimeNew + "\"," +
                            "\"step_count\": \"" + totWalkStepsDuringWorkout + "\"," +
                            "\"energy_count\": \"" + energDelta2 + "\", " +
                            "\"calorie_burn\": \"" + caleryDetla2 + "\"," +
                            "\"distance\": \"" + distanceDetla2 + "\"," +
                            "\"end_time\": \"" + endTimeNew + "\"," +
                            "\"workout_date\": \"" + dateInEndWorkout + "\"," +
                            "\"updat_type\": \"" + typeDetla + "\"," +
                            "\"walk_count\": \"" + totWalkStepsDuringWorkout + "\"," +
                            "\"run_count\": \"" + totRunStepsDuringWorkout + "\"," +
                            "\"duration\": \"" + workoutTimeDurations + "\"" +
                            "}";


            nameValuePair.add(new BasicNameValuePair("method", "addWorkoutSummary"));
            nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));

            System.out.println("............WORKOUT ACTIVITY UPDATING.....To Server.............." + nameValuePair.toString());

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // log exception
                e.printStackTrace();
            }

            //making POST request.
            try {
                HttpResponse response = httpClient.execute(httpPost);
                int r = 0;
                if (response != null) {
                    r = response.getStatusLine().getStatusCode();
                    System.out.println("..........response.code.........." + r);

                    if (r == 200) {
                        statusFromServiceAPI_db = "";
                        String json = EntityUtils.toString(response.getEntity());
                        try {
                            JSONObject jsonObj = new JSONObject(json);
                            statusFromServiceAPI_db = jsonObj.optString("result").toString();
                            if (statusFromServiceAPI_db.equals("0")) {
                                System.out.println("............WORKOUT UPDATED..........." + statusFromServiceAPI_db);

                            } else {
                                statusFromServiceAPI_db = "55";
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        statusFromServiceAPI_db = "99";
                    }

                }


            } catch (ClientProtocolException e) {
                // Log exception
                e.printStackTrace();
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
            }


            //    prgDialog.cancel();


//====================================

        }
    }


    private void Service_sendAyuboStepsToServer() {


        sendAyuboStepsToServer task = new sendAyuboStepsToServer();
        task.execute(new String[]{ApiClient.BASE_URL_live});


    }

    private class sendAyuboStepsToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            sendAyuboSteps();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }
    }

    private void sendAyuboSteps() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));


        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

        Date dae = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(dae);

        float value = Float.parseFloat(Integer.toString(stepsWalkAtWorkoutStop));

        String sMets, sCals, sDis;
        double metsVal = (value / 130) * 4.5;
        sMets = String.format("%.0f", metsVal);
        double calories = (metsVal * 3.5 * 70) / 200;
        sCals = String.format("%.0f", calories);
        double distance = (value * 78) / (float) 100000;
        sDis = String.format("%.2f", distance);
        String versionName = BuildConfig.VERSION_NAME;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String dateFromDB_forADay0 = sdf.format(cal.getTime());

        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"," +
                        "\"activity\": \"" + "activity_AYUBO" + "\"," +
                        "\"energy\": \"" + sMets + "\"," +
                        "\"steps\": \"" + Integer.toString(stepsWalkAtWorkoutStop) + "\"," +
                        "\"calorie\": \"" + sCals + "\", " +
                        "\"duration\": \"" + "0" + "\"," +
                        "\"distance\": \"" + sDis + "\"," +
                        "\"date\": \"" + dateFromDB_forADay0 + "\"," +
                        "\"walk_count\": \"" + Integer.toString(stepsWalkAtWorkoutStop) + "\"," +
                        "\"version\": \"" + versionName + "\"," +
                        "\"osType\": \"" + "android" + "\"," +
                        "\"run_count\": \"" + "0"
                        + "\"" +
                        "}";


        //  addWorkoutSummary
        nameValuePair.add(new BasicNameValuePair("method", "addDailyActivitySummary"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("........addDailyActivitySummary.....@...Stop Workout..........." + nameValuePair.toString());
        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);
            int r = 0;
            if (response != null) {
                r = response.getStatusLine().getStatusCode();
                System.out.println("..........response.code.........." + r);

                if (r == 200) {
                    System.out.println("..........response..........." + response);

                    String responseString = null;
                    try {
                        responseString = EntityUtils.toString(response.getEntity());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(responseString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String number = jsonObj.optString("number").toString();

                    if (number.isEmpty()) {

                        String res = jsonObj.optString("result").toString();

                        int result = Integer.parseInt(res);


                        if (result == 11) {
                            stepsSentToServerStatusAtPause = "11";
                        }
                        if (result == 0) {
                            System.out.println("==================App steps Upldated===Result=>" + result);
                            try {
                                stepsSentToServerStatusAtPause = "0";
                            } catch (Exception e) {

                            }

                        }

                    } else {

                        stepsSentToServerStatusAtPause = "99";

                    }


//                        if (number.equals("10")) {
//                            statusFromServiceAPI_db = "11";
//                            System.out.println("=========================LOGIN FAIL=========>" + number);
//                        } else {


                }
            } else {
                stepsSentToServerStatusAtPause = "999";
            }
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }

    }

}
