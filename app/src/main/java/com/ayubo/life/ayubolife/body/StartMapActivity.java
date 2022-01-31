package com.ayubo.life.ayubolife.body;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.model.todaysummery;
import com.ayubo.life.ayubolife.model.workoutEntity;
import com.ayubo.life.ayubolife.utility.Ram;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

public class StartMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private long startTime = 0L;
    boolean activityStart = true;
    boolean activityPause = false;
    boolean activityResume = false;
    List<LatLng> pointsNew = null;
    private Handler handler = new Handler();
    private Handler customHandler = new Handler();
    Timer timerRefresh;
    TimerTask timerTask;

    Timer timer;
    Timer timerGps;
    TimerTask timerTaskGps;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int stepsWalkAtWorkoutStop = 0;
    int stepsRunAtWorkoutStop = 0;
    int stepsWalsAtWorkoutStart = 0;
    int stepsRunAtWorkoutStart = 0;
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
    public static String stepCount;
    String mets, activityName, activityId, step_deviceName;
    ImageButton workout_btn_pause;
    Button workout_btn_start;
    int totalMinits;
    TextView txt_timer;
    boolean activityStartStated = false;
    boolean workoutSavedStatus = false;
    String sameDate;
    int sameId, sameStep, sameRun, sameJump;
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
    //    CircleProgressView btn_iv;
    ImageView btn_iv;
    com.google.android.gms.maps.MapView mapView;
    GoogleMap googleMap;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 432;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION_MANAGER = 433;
    GoogleMap mGoogleMap;
    GPSTracker gps;
    private Polyline mMutablePolyline;
    private GoogleMap myMap;
    List<LatLng> points = null;
    LatLng sydney = null;
    private GoogleApiClient mClient = null;

    @Override
    public void onResume() {
        super.onResume();
        this.mapView.onResume();

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
                                //     Log.i(TAG, "Connected!!!");
                                // Now you can make calls to the Fitness APIs.  What to do?
                                // Look at some data!!
                                //  subscribe();
                                //    isGoogleFitConnected = true;
                                pref.setGoogleFitEnabled("true");

                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the portrait gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    //Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    // Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
                                }
                            }
                        }
                )
                .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        // Log.i(TAG, "Google Play services connection failed. Cause: " +
                        result.toString();
//                        Snackbar.make(
//                                FoogleFitConnect_Activity.this.findViewById(R.id.main_activity_view),
//                                "Exception while connecting to Google Play services: " +
//                                        result.getErrorMessage(),
//                                Snackbar.LENGTH_INDEFINITE).show();
                    }
                })
                .build();
    }

    private boolean isGpsAvailable() {

        boolean gpsCon = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled.", Toast.LENGTH_SHORT).show();
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

    void drawMapPath(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(StartMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        } else {
            mGoogleMap.setMyLocationEnabled(true);
        }

        List<LatLng> pointsNew = Ram.getPointsNew();
        points = pointsNew;
        Toast.makeText(this, "GPS list : " + points.size(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < points.size() - 1; i++) {
            LatLng src = points.get(i);
            LatLng dest = points.get(i + 1);

            mMutablePolyline = map.addPolyline(
                    new PolylineOptions()
                            .add(new LatLng(src.latitude, src.longitude),
                                    new LatLng(dest.latitude, dest.longitude))
                            .width(8)
                            .color(Color.GREEN)
                            .geodesic(true)
            );
        }
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
        mGoogleMap.addMarker(new MarkerOptions()
                .title("")
                .snippet("")
                .position(sydney));
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        drawMapPath(map);

    }

    private void requestCameraPermission() {

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(StartMapActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(StartMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(StartMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerGps.cancel();
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_map);
        pointsNew = new ArrayList<LatLng>();


        sydney = new LatLng(6.918398, 79.858381);
        points = new ArrayList<LatLng>();

        //  points.add(sydney);

        gps = new GPSTracker(StartMapActivity.this);

//        btn_iv = (CircleProgressView) findViewById(R.id.vButton);
        workout_btn_pause = (ImageButton) findViewById(R.id.workout_btn_pause);
        workout_btn_start = (Button) findViewById(R.id.workout_btn_start);
        //btn_getGps= (Button) findViewById(R.id.btn_getGps);
        txt_timer = (TextView) findViewById(R.id.txt_timer);

        activityName = getIntent().getStringExtra("activityName");
        activityId = getIntent().getStringExtra("activityId");
        mets = getIntent().getStringExtra("mets");
        db = DatabaseHandler.getInstance(this);

        pref = new PrefManager(StartMapActivity.this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        step_deviceName = pref.getDeviceData().get("stepdevice");

        boolean aaap = isGpsAvailable();

        mapView = (com.google.android.gms.maps.MapView) findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        if (mapView != null) {
            mapView.getMapAsync(this);
        }

//        btn_iv = (CircleProgressView) findViewById(R.id.vButton);
        btn_iv = (ImageView) findViewById(R.id.vButton);
//        btn_iv = (com.criapp.circleprogresscustomview.CircleProgressView) findViewById(R.id.vButton);
        //  btn_iv.setColorDone(Color.BLUE);//color filled when done
//        btn_iv.setColorWrong(Color.WHITE);//color filled on error
//        btn_iv.setColorBaseFill(Color.GREEN);//color filled while on progress
//        btn_iv.setColorBaseStroke(Color.WHITE);//line color under the progress one
//        btn_iv.setColorWrongBaseStroke(Color.WHITE);//line color on error
//        btn_iv.setStrokeWidth(2); // the progress line width in px
//        btn_iv.setStartProgress(90);

        btn_iv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("==================================>>>");
                // Perform action on click

                if (!activityStartStated) {
                    System.out.println("===============================6===>>>");
                    AlertDialog.Builder builder = new AlertDialog.Builder(StartMapActivity.this);

                    builder.setTitle("Please start the workout");
                    // builder.setMessage("Please start activity");
                    DialogInterface.OnClickListener diocl = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    };

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            //  finish();
                            dialog.cancel();
                            // startActivity(getIntent());

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    stopClick();
                    // iv.setEnabled(false);
                }
            }
        });
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");

        sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date dae = new Date();
        current_day = sdf.format(dae);

        if (step_deviceName.equals("activity_AYUBO")) {
            SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            Sensor mSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

            if (mSensor == null) {
                buildFitnessClient();
            } else {

            }
        }


        btn_iv.setVisibility(View.GONE);
        workout_btn_pause.setVisibility(View.GONE);

        workout_btn_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickStart();
            }
        });

        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(StartMapActivity.this, StartWorkoutActivity.class);
                startActivity(in);
            }
        });

        workout_btn_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//                if(activityPause){
//                    clickedPause();
//                }else
//                {
//                    clickedResume();
//                }
            }
        });

    }

    public void startTimerGps() {

        timerGps = new Timer();

        initializeTimerTaskGps();

        timerGps.schedule(timerTaskGps, 10, 60000); //

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

    public void initializeTimerTaskGps() {

        timerTaskGps = new TimerTask() {

            public void run() {

                handler.post(new Runnable() {

                    public void run() {
                        gps = new GPSTracker(getApplicationContext());
                        if (gps.canGetLocation()) {
                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();
                            LatLng PERTH4 = new LatLng(latitude, longitude);
                            pointsNew.add(PERTH4);

                            Toast.makeText(getApplicationContext(), "Lat: " + pointsNew.size() + "  " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                            //   drawMapPath(map);


                        }
                    }

                });

            }

        };

    }


    public class GPSTracker extends Service implements LocationListener {

        private final Context mContext;
        boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;
        boolean canGetLocation = false;
        Location location; // Location
        double latitude; // Latitude
        double longitude; // Longitude
        List<LatLng> pointsNew = null;
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
        private static final long MIN_TIME_BW_UPDATES = 1000 * 15 * 1; // 1 minute
        //  private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GPSTracker(Context context) {
            this.mContext = context;
            getLocation();
            pointsNew = new ArrayList<LatLng>();
        }

        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);

                // Getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // Getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // No network provider is enabled
                } else {
                    this.canGetLocation = true;
//                    if (isNetworkEnabled) {
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
                    // If GPS enabled, get latitude/longitude using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            pointsNew = new ArrayList<LatLng>();
                            if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(StartMapActivity.this,
                                    Manifest.permission.RECORD_AUDIO)
                                    != PackageManager.PERMISSION_GRANTED)) {
                                requestPermissions(new String[]{
                                                Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION_MANAGER);

                                //   turnGPSOn();

                            }
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();


                                    Log.d("GPS Enabled", "GPS ======================");

                                    //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }


        /**
         * Stop using GPS listener
         * Calling this function will stop using GPS in your app.
         */
        public void stopUsingGPS() {
            if (locationManager != null) {
                locationManager.removeUpdates(GPSTracker.this);
            }
        }

        @Override
        public void onCreate() {
            super.onCreate();

            startTimer();

        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            //TODO do something useful


            //  startTimer();

            return Service.START_NOT_STICKY;
        }


        /**
         * Function to get latitude
         */
        public double getLatitude() {
            if (location != null) {
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }


        /**
         * Function to get longitude
         */
        public double getLongitude() {
            if (location != null) {
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/Wi-Fi enabled
         *
         * @return boolean
         */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }


        /**
         * Function to show settings alert dialog.
         * On pressing the Settings button it will launch Settings Options.
         */
        public void showSettingsAlert() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing the Settings button.
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });

            // On pressing the cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }


        @Override
        public void onLocationChanged(Location location) {
        }


        @Override
        public void onProviderDisabled(String provider) {
        }


        @Override
        public void onProviderEnabled(String provider) {
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }


        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    void clickedResume() {
        timeSwapBuff = updatedTime;
        customHandler.removeCallbacks(updateTimerThread);
        System.out.println("Resume Clicked=======================================================");
        startTimer();
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);

        activityStart = false;
        activityResume = false;
        activityPause = true;
    }

    void clickedPause() {
        timeSwapBuff = updatedTime;
        customHandler.removeCallbacks(updateTimerThread);
        System.out.println("Pause Clicked=======================================================");

        activityPause = false;
        activityStart = false;
        activityResume = true;
    }

    private static void dumpDataSet(DataSet dataSet) {
        //  Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        if (dataSet.isEmpty()) {
            System.out.println("========Empty======GFit Steps============");
            stepCount = "0";
        } else {

            for (DataPoint dp : dataSet.getDataPoints()) {
//                Log.i(TAG, "Data point:");
//                Log.i(TAG, "\tType: " + dp.getDataType().getName());
//                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {
//                    Log.i(TAG, "\tField: " + field.getName() +
//                            "GFit Steps Value: " + dp.getValue(field));

                    stepCount = dp.getValue(field).toString();
                    System.out.println("==============GFit Steps Value:==================" + stepCount);
                }
            }
        }
    }

    public static void printData(DataReadResult dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        if (dataReadResult.getBuckets().size() > 0) {
//          //  Log.i(TAG, "Number of returned buckets of DataSets is: "
//                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            //   Log.i(TAG, "Number of returned DataSets is: "
//                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }


    //===GET gOOGLEfIT sTEPS WHEN STARTING WORKOUNT=======================
    private class getGoogleFitStepsWhenStart extends AsyncTask<Void, Void, Void> {

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
                }

                System.out.println("==========================" + stepCount);

            } else {
//                    txt_dis.setText("0");
//                    txt_steps.setText("0");
            }


        }
    }

    private class getGoogleFitStepsWhenEnd extends AsyncTask<Void, Void, Void> {

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
            int totWalkStepsDuringWorkout = 0;
            int totRunStepsDuringWorkout = 0;
            Date dae = new Date();

            String dateInEndWorkouto = sdf.format(dae);
            String dateInEndWorkout = dateInEndWorkouto.trim();

            if (stepCount != null) {

                if (stepCount.equals("0")) {
                    stepsWalkAtWorkoutStop = 0;
                } else {
                    stepsWalkAtWorkoutStop = Integer.parseInt(stepCount);
                }

                System.out.println("==========================" + stepCount);

                totWalkStepsDuringWorkout = stepsWalkAtWorkoutStop - stepsWalsAtWorkoutStart;

                System.out.println("-----Steps within Workout---------------------" + totWalkStepsDuringWorkout);

                int walkDot = totWalkStepsDuringWorkout + sameStep;
                int runDot = totRunStepsDuringWorkout + sameRun;

                int returnStatus = db.updateTodaySummeryMets(userid_ExistingUser, dateInEndWorkouto, sameMets, sameCals);
                System.out.println("===================================jesssssyyy=======================================" + returnStatus);
                updateDefaultSummeryDuringWorkout2(sameId, userid_ExistingUser, sameDate, walkDot, runDot, sameJump, sameMets, sameCals, sameEnPersent);
                System.out.println("-----Workout Saving---------------------" + sameStep + "  " + sameRun + "  " + sameMets + "  " + sameCals);

                String status = "0";
                //   totWalkStepsDuringWorkout=50;totRunStepsDuringWorkout=30;
                String sTimeDuration = Integer.toString(duration2);

                if (duration2 > 0) {
                    addWorkoutRecordToTable2(userid_ExistingUser, activityName, totWalkStepsDuringWorkout, totRunStepsDuringWorkout, energDelta2,
                            caleryDetla2, distanceDetla2, startTimeNew, endTimeNew, dateInEndWorkout, typeDetla, sTimeDuration, status);

                } else {

                }

                updatedTime = 0;
                timeSwapBuff = 0;
                timeInMilliseconds = 0;

                activityStart = false;
                activityResume = false;
                activityPause = false;


            } else {
//                    txt_dis.setText("0");
//                    txt_steps.setText("0");
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
//        Log.i(TAG, "Range Start: " + dateFormat.format(midNight));
//        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

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


    void clickStart() {

        //  workout_btn_start.setVisibility(View.GONE);
        btn_iv.setVisibility(View.VISIBLE);
        workout_btn_pause.setVisibility(View.VISIBLE);

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);

        activityPause = true;

        wStarted = true;

        activityStartStated = true;
        thisIsMyRealStepDetalt_Start = stepDelta;


        if (activityStart) {
            workoutSavedStatus = true;


            if (pref.isGoogleFitEnabled().equals("true")) {

                try {
                    new getGoogleFitStepsWhenStart().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //After above method calling stepsWalsAtWorkoutStart will update steps
                // stepsWalsAtWorkoutStart = Ram.getCurrentWalkStepCount();

            } else {
                stepsWalsAtWorkoutStart = DBRequest.getTodayStepsCount(StartMapActivity.this, pref.getLoginUser().get("uid"));
            }

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            long sttTime = c.get(Calendar.MILLISECOND);

            date = new java.util.Date();
            startTimeNew = (new Timestamp(date.getTime())).toString();

            todaysummery data = db.getTodaySummeryFromDBNew1(current_day, userid_ExistingUser);

            if (data == null) {
                System.out.println("Timing cals==1=null==");
            } else {
                sameId = data.getId();
                sameDate = data.getDate();
                sameStep = data.getStep_tot();
                sameRun = data.getRun_tot();
                sameJump = data.getJump_tot();
                sameMets = data.getMet_tot();
                sameCals = data.getCal_tot();
                sameEnPersent = data.getEnegy_persent();
            }
            startTimer();
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);

            activityStart = false;
            activityResume = false;
            activityPause = true;


        }

    }

    void stopClick() {

        date = new java.util.Date();
        endTimeNew = (new Timestamp(date.getTime())).toString();

        workoutSavedStatus = false;
        saveWorkoutData();

        AlertDialog.Builder builder = new AlertDialog.Builder(StartMapActivity.this);
        builder.setTitle("Workout saved successfully");
        DialogInterface.OnClickListener diocl = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();
                dialog.cancel();
            }
        };
        // builder.setPositiveButton("OK", diocl);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, close
                // current activity
                //    Intent in = new Intent(StartActivity.this, Home_Activity_New.class);
                //   startActivity(in);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        if (screenSleepOn) {
            wl.release();
        }

    }

    private void saveWorkoutData() {


        Date dae = new Date();

        String dateInEndWorkouto = sdf.format(dae);
        String dateInEndWorkout = dateInEndWorkouto.trim();

        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);

        int totWalkStepsDuringWorkout = 0;
        int totRunStepsDuringWorkout = 0;


        if (pref.isGoogleFitEnabled().equals("true")) {

            try {
                new getGoogleFitStepsWhenEnd().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //After above method stepswill update to stepsWalkAtWorkoutStop
            // stepsWalkAtWorkoutStop = Ram.getCurrentWalkStepCount();
        } else {
            stepsWalkAtWorkoutStop = DBRequest.getTodayStepsCount(StartMapActivity.this, pref.getLoginUser().get("uid"));

            totWalkStepsDuringWorkout = stepsWalkAtWorkoutStop - stepsWalsAtWorkoutStart;

            System.out.println("-----Steps within Workout---------------------" + totWalkStepsDuringWorkout);

            int walkDot = totWalkStepsDuringWorkout + sameStep;
            int runDot = totRunStepsDuringWorkout + sameRun;

            int returnStatus = db.updateTodaySummeryMets(userid_ExistingUser, dateInEndWorkouto, sameMets, sameCals);
            System.out.println("===================================jesssssyyy=======================================" + returnStatus);
            updateDefaultSummeryDuringWorkout2(sameId, userid_ExistingUser, sameDate, walkDot, runDot, sameJump, sameMets, sameCals, sameEnPersent);
            System.out.println("-----Workout Saving---------------------" + sameStep + "  " + sameRun + "  " + sameMets + "  " + sameCals);

            String status = "0";
            //   totWalkStepsDuringWorkout=50;totRunStepsDuringWorkout=30;
            String sTimeDuration = Integer.toString(duration2);

            if (duration2 > 0) {
                addWorkoutRecordToTable2(userid_ExistingUser, activityName, totWalkStepsDuringWorkout, totRunStepsDuringWorkout, energDelta2,
                        caleryDetla2, distanceDetla2, startTimeNew, endTimeNew, dateInEndWorkout, typeDetla, sTimeDuration, status);

            } else {

            }

            System.out.println("==Workout Data===" + activityName + "  " + totWalkStepsDuringWorkout + "  " +
                    totRunStepsDuringWorkout + "    " + energDelta2 + "      "
                    + caleryDetla2 + "  " + distanceDetla2 + "  " + duration2 + "    " +
                    endTimeNew + "    " + dateInEndWorkout + "    " + typeDetla + "   " + startTimeNew);
            //  4  32.0    36.42  2  0

            // stopService(intent77);

            updatedTime = 0;
            timeSwapBuff = 0;
            timeInMilliseconds = 0;

            activityStart = false;
            activityResume = false;
            activityPause = false;

        }


    }


    private void addWorkoutRecordToTable2(String userid_ExistingUser, String type, int steps, int runs, float energy, float cal, int distance,
                                          String start_time, String end_time, String date, int update_type, String duration, String status) {
        try {
            db.addWorkoutRecord(new workoutEntity(1, userid_ExistingUser, type, steps, runs, energy, cal, distance, start_time,
                    end_time, date, update_type, duration, status));
            Log.d("Good Bye ", "Added New Workout successfully");
        } catch (SQLiteException e) {
            Log.d("Error: ", "Inserting Workout.." + e);
        }

    }

    private void updateDefaultSummeryDuringWorkout2(int sameId, String userid_ExistingUser, String sameDate, int sameStep, int sameRun, int sameJump, float sameMets, float sameCals, float sameEnPersent) {
        try {
            int resut = db.updateDefaultSummeryDuringWorkout2(new todaysummery(sameId, userid_ExistingUser, sameDate, sameStep, sameRun, sameJump, sameMets, sameCals, sameEnPersent));
            Log.d("Good Bye ", "Off Default Data During Workout" + resut);
        } catch (SQLiteException e) {
            Log.d("Error: ", "Off Default Data During Workout.." + e);
        }
    }


    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {

                handler.post(new Runnable() {

                    public void run() {

                        //Method body missing===================================================================

                    }

                });

            }

        };

    }

    public void startTimer() {

        timerRefresh = new Timer();
        startTime = SystemClock.uptimeMillis();
        initializeTimerTask();

        timerRefresh.schedule(timerTask, 0, 60000);
    }


    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            long systime = SystemClock.uptimeMillis();
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;
            //  System.out.println("Timing cals====="+systime+"  "+startTime+"   "+timeInMilliseconds+"   "+timeSwapBuff+"   "+updatedTime);
            int hour = (int) (updatedTime / 3600000);
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;


            if (secs == 0) {
                updateWorkoutUI(mins);
                System.out.println("****************************************************** Mins" + mins);
            }
            int milliseconds = (int) (updatedTime % 1000);
            txt_timer.setText("" + String.format("%02d", hour) + ":"
                    + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs)


            );
            totalMinits = mins;
            customHandler.postDelayed(this, 1000);
        }

    };

    public float getMetValus2(int mins) {
        float metValue = 0;
        // badminton 7
        // int m=Integer.parseInt(fMet);
        metValue = metsForWorkout_PerMinit * mins;

        return metValue;
    }

    public float calculateMetValusForTime(int minit) {
        float metValue2 = 0;
        //  float m=Integer.parseInt(fMet);
        metValue2 = metsForWorkout_PerMinit * minit;
        return metValue2;
    }

    private void updateWorkoutUI(int mins) {
        DecimalFormat dfff = new DecimalFormat("0");

        float CurrentTotalMet = getMetValus2(mins);


        if (pref.isGoogleFitEnabled().equals("true")) {
            stepsWalkAtWorkoutStop = Ram.getCurrentWalkStepCount();
        } else {
            stepsWalkAtWorkoutStop = DBRequest.getTodayStepsCount(StartMapActivity.this, pref.getLoginUser().get("uid"));
        }

        int totWalkStepsDuringWorkout = stepsWalkAtWorkoutStop - stepsWalsAtWorkoutStart;
        //  int totRunStepsDuringWorkout=stepsRunAtWorkoutStop-stepsRunAtWorkoutStart;
        STEPCOUNT_FOR_A_WORKOUT_TIME = totWalkStepsDuringWorkout;

        double h = (168 / 100);
        Double stepLength = 0.45 * h;

        Double stepDistance = stepLength * STEPCOUNT_FOR_A_WORKOUT_TIME;
        Double distanceInKm = stepDistance / 1000;

        String sss = new DecimalFormat("#.#").format(distanceInKm);

        float weight = 70;
        // float weight = 65;
        double calrsTot = CaleriesBurnWithMets(CurrentTotalMet, weight);
        float BurnedCaleries = (float) calrsTot;

        Ram.setMETS(CurrentTotalMet);

        String str_step = dfff.format(STEPCOUNT_FOR_A_WORKOUT_TIME);
        String str_FINAL_FULL_MET_COUNT = dfff.format(CurrentTotalMet);
        String str_FINAL_FULL_CAL_COUNT = dfff.format(calrsTot);

        distanceDetla2 = distanceInKm.intValue();
        stepDelta2 = STEPCOUNT_FOR_A_WORKOUT_TIME;
        energDelta2 = CurrentTotalMet;
        enegyPresntageDetla2 = CurrentTotalMet / 500;
        caleryDetla2 = BurnedCaleries;
        duration2 = mins;

//        L21_lblStepCount.setText(str_step);
//        L23_lblEncount.setText(str_FINAL_FULL_MET_COUNT);
//        L22_lblCalCount.setText(str_FINAL_FULL_CAL_COUNT);
//        L24_lblDistanceCount.setText(sss);

    }

    public double CaleriesBurnWithMets(double mets, double weiget) {
        double newMets = mets;
        double newWeightInKg = weiget;
        double burnedCalories = (newMets * 3.5 * newWeightInKg) / 200;
        System.out.println("====================CALs in Start Activity=================     " + burnedCalories);
        return burnedCalories;
    }
}
