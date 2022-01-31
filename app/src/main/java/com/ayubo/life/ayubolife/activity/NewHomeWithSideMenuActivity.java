package com.ayubo.life.ayubolife.activity;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Ringtone;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.fragments.Challenges_Fragment;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.fragments.HomeDesign;
import com.ayubo.life.ayubolife.fragments.HomeFragment;
import com.ayubo.life.ayubolife.fragments.MedicalReports_Fragment;
import com.ayubo.life.ayubolife.fragments.NewHome;
import com.ayubo.life.ayubolife.fragments.NewHomeDesign;
import com.ayubo.life.ayubolife.fragments.TimeLineFragment;
import com.ayubo.life.ayubolife.fragments.VedioGallery_Fragment;
import com.ayubo.life.ayubolife.insurances.Activities.InsurancePolicesActivity;
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData;
import com.ayubo.life.ayubolife.lifepoints.LifePointActivity;
import com.ayubo.life.ayubolife.login.EnterPromoCodeActivity;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.login.UserProfileActivity;
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity;
import com.ayubo.life.ayubolife.model.MenuObj;
import com.ayubo.life.ayubolife.notification.activity.NotificationsListActivity;
import com.ayubo.life.ayubolife.notification.model.NotiCountMainData;
import com.ayubo.life.ayubolife.notification.model.NotiCountMainResponse;
import com.ayubo.life.ayubolife.payment.activity.MainPaymentHistoryActivity;
import com.ayubo.life.ayubolife.payment.activity.PaymentSettingsActivity;
import com.ayubo.life.ayubolife.payment.activity.PurchaseHistory;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.qrcode.DecoderActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.satalite_menu.CommiunityActivity;
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity;
import com.ayubo.life.ayubolife.timeline.pagination.PaginationAdapterCallback;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.walk_and_win.StepObj;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flavor.activity.AboutActivity;
import com.flavors.changes.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huawei.hms.api.HuaweiApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewHomeWithSideMenuActivity extends BaseActivity implements NewHome.OnFragmentInteractionListener, TimeLineFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener, VedioGallery_Fragment.OnFragmentInteractionListener,
        HomeDesign.OnFragmentInteractionListener,
        com.ayubo.life.ayubolife.fragments.NewHomeDesign.OnFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Challenges_Fragment.OnFragmentInteractionListener, MedicalReports_Fragment.OnFragmentInteractionListener, PaginationAdapterCallback {
    //main calss

    String notificationCount = "0";
    private FloatingActionButton fab;
    long totalSize = 0;
    private PendingIntent pendingIntent;
    AlarmManager alarmManager;
    String gfit_steps_0, gfit_steps_1, gfit_steps_2, gfit_steps_3, gfit_steps_4, gfit_steps_5, gfit_steps_6, gfit_steps_7, gfit_steps_8, gfit_steps_9;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE_DEVICE = 123;
    // urls to load navigation header background image
    // and profile image
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    String append_menu;
    boolean isBackFirsttime;
    boolean isFirsttime = false;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler = new Handler();
    int serviceDone = 0;
//    ImageView btn_notifications;
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 888;
    public static final int MY_PERMISSIONS_REQUEST_AUDIO_RECODING = 345;
    String total_steps;
    String total_calories;
    String total_distance;
    String total_energy;
    String total_duration, serviceDataStatus;
    File newf;
    String notification_count;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    String user_last_name, user_first_name, show_lang_screen;

    private static final String TAG_HOME = "home";
    public static String CURRENT_TAG = TAG_HOME;

    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static String userid_ExistingUser_static, versionName, device_modal;

    String userid_ExistingUser, errorData, userStatus, fullName, email, QB, imagepath_db, displayNotiVal, stepsStatus, str_steps;
    DatabaseHandler db;

    String encodedHashToken, stepsSentToServerStatusAtPause;
    String stepsSentToDBStatus;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;
    Toast toastt;
    String regId = null;
    String url = null;
    String hasToken, videoCallEnable;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    SimpleDateFormat sdf;
    String stepsFromDB_forADay0, dateFromDB_forADay0;
    String stepsFromDB_forADay1, dateFromDB_forADay1;
    String stepsFromDB_forADay2, dateFromDB_forADay2;
    String stepsFromDB_forADay3, dateFromDB_forADay3;
    String stepsFromDB_forADay4, dateFromDB_forADay4;
    String stepsFromDB_forADay5, dateFromDB_forADay5;
    String stepsFromDB_forADay6, dateFromDB_forADay6;
    String stepsFromDB_forADay7, dateFromDB_forADay7;
    String stepsFromDB_forADay8, dateFromDB_forADay8;
    String stepsFromDB_forADay9, dateFromDB_forADay9;
    String stepsFromDB_forADay10, dateFromDB_forADay10;
    int senserSteps, dbSteps;

    String image_absolute_path;
    boolean isProfileLoaded = false;
    public static JSONArray jsonStepsForWeekArray;
    public static List<StepObj> jsonStepsForWeekArrayForNewSave;
    String notiCount;
    private PowerManager mPowerManager;
    private WindowManager mWindowManager;
    private PowerManager.WakeLock mWakeLock;
    public static boolean isStepUpdated = false;

    private int NFINAL_FULL_RUN_COUNT = 0;
    private int NFINAL_FULL_JUMP_COUNT = 0;
    private int NFINAL_FULL_WALK_COUNT = 0;
    private int FINAL_FULL_STEP_COUNT = 0;

    public static Bundle loaded_savedInstanceState;

    private float NFINAL_FULL_MET_COUNT = 0;
    private float NFINAL_ENERGY_PERSENTAGE = 0;
    private float NFINAL_ENERGY_PERSENTAGE_CIRCLE = 0;
    private float NWFINAL_FULL_MET_COUNT = 0;
    private float NFINAL_FULL_CAL_COUNT = 0;
    private float NWFINAL_FULL_CAL_COUNT = 0;
    private int NWFINAL_FULL_STEP_COUNT = 0;
    private float NFINAL_FULL_DISTANCE_COUNT = 0;
    private int NWFINAL_FULL_RUN_COUNT = 0;
    String deviceIdPush = null;
    SharedPreferences prefs;

    private String statusFromServiceAPI_db, setDeviceID_Success;
    int result;
    int REQUEST_CAMERA = 0, SELECT_FILE = 125;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg;
    CircleImageView imgProfile;
    private TextView txtName, txtWebsite, txtStepDisplay;
    private Toolbar toolbar;
    //   private FloatingActionButton fab;
    static Button notifCount;
    static int mNotifCount = 0;
    // urls to load navigation header background image
    // and profile image
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private PrefManager pref;
    boolean isLoaded = false;
    static NewHomeWithSideMenuActivity activityA;
    public static GoogleApiClient mApiClient;

    public static HuaweiApiClient huaweiApiClient;

    private boolean isGoogleFitConnected = false;
    private boolean isFirstLoading = false;
    private boolean isUpdated_Server = false;
    String googlefitenabled;
    private static final int REQUEST_OAUTH = 1;
    private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
    private static String stepCount;
    ArrayList<String> tenDaysDataList = new ArrayList<String>();
//    TextView txt_noti_count;
    LinearLayout notibg;
    String isUserGoogleFit;
    private static final String TAG = NewHomeWithSideMenuActivity.class.getSimpleName();
    String deviceStatus;
    private static final long ON_ITEM_CLICK_DELAY = TimeUnit.SECONDS.toMillis(10);
    Ringtone myDoctorCallRing;

    ImageView ic_call_image;
    TextView navi_header_mobile_no;

    public static String stepStatus;
    LinearLayout view_container;
    LinearLayout layout_for_link_to_profile;


    public static NewHomeWithSideMenuActivity getInstance() {
        return activityA;
    }


    public static Bundle firebaseAnalyticsBundle;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constants.type == Constants.Type.AYUBO) {
            setContentView(R.layout.activity_new_home_with_side_menu);
        } else if (Constants.type == Constants.Type.SHESHELLS) {
            setContentView(R.layout.activity_new_home_with_side_menu);
        } else if (Constants.type == Constants.Type.LIFEPLUS) {

            String noticolor = "#000000";
            int whiteInt = Color.parseColor(noticolor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(whiteInt);

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = getWindow().getDecorView();

                //    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                decor.setSystemUiVisibility(0);
                //   decor.setSystemUiVisibility(0);
                // }
            }

            setContentView(R.layout.activity_new_home_with_side_menu_lipfeplus);
        } else {
            Log.i("App Name : ", "Hemas App ");
            setContentView(R.layout.activity_new_home_with_side_menu_hemas);
        }

        jsonStepsForWeekArray = new JSONArray();
        jsonStepsForWeekArrayForNewSave = new ArrayList<StepObj>();
        pref = new PrefManager(this);
        pref.setNewServiceLastRunningTime("0");

        prefs = this.getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        pref.setMapReload(false);

        try {
            db = new DatabaseHandler(NewHomeWithSideMenuActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        isBackFirsttime = false;
        try {
            // FirebaseAnalytics Configurations...................
            SplashScreen.firebaseAnalytics = FirebaseAnalytics.getInstance(this);
            //Sets whether analytics collection is enabled for this app on this device.
            SplashScreen.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
            //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
//            SplashScreen.firebaseAnalytics.setMinimumSessionDuration(10000);
            //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
            SplashScreen.firebaseAnalytics.setSessionTimeoutDuration(500);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            userid_ExistingUser = pref.getLoginUser().get("uid");
            userid_ExistingUser_static = pref.getLoginUser().get("uid");
            hasToken = pref.getLoginUser().get("hashkey");
            imagepath_db = pref.getLoginUser().get("image_path");
            fullName = pref.getLoginUser().get("name");


            email = pref.getLoginUser().get("email");
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            QB = prefs.getString("QB", "");
            device_modal = Build.MODEL;
            videoCallEnable = "";

            versionName = BuildConfig.VERSION_NAME;

            System.out.println("==========onCreate================" + userid_ExistingUser);
            isStepUpdated = false;
            toolbar = findViewById(R.id.toolbar_for_newsidemenu);
            //  toolbar_for_newsidemenu

            if (Build.VERSION.SDK_INT >= 21) {
                //   toolbar.setElevation(0);
            } else {
                // Implement this feature without material design
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        RelativeLayout lay_notifications = toolbar.findViewById(R.id.lay_notifications);
        TextView txt_expert_heading = toolbar.findViewById(R.id.txt_expert_heading);
//        btn_notifications = toolbar.findViewById(R.id.btn_notifications);
//        txt_noti_count = (TextView) toolbar.findViewById(R.id.txt_noti_count);

        txt_expert_heading.setText("Feed");
        txt_expert_heading.setTextColor(Color.parseColor("#3B3B3B"));

//        btn_notifications.setVisibility(View.VISIBLE);
//        txt_noti_count.setVisibility(View.VISIBLE);


//        lay_notifications.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //   Intent intent = new Intent(NewHomeWithSideMenuActivity.this, NotificationsListActivity.class);
//                //  startActivity(intent);
//            }
//        });
//        btn_notifications.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(NewHomeWithSideMenuActivity.this, NotificationsListActivity.class);
//                startActivity(intent);
////                JanashakthiWelcomeActivity.startActivity(NewHomeWithSideMenuActivity.this);
//
//            }
//        });
//        txt_noti_count.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(NewHomeWithSideMenuActivity.this, NotificationsListActivity.class);
//                startActivity(intent);
//            }
//        });

        deviceStatus = pref.getDeviceData().get("stepdevice");
        mHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (Constants.type == Constants.Type.AYUBO) {
            navigationView = (NavigationView) findViewById(R.id.nav_view_newSidemenu_with_activity);
        } else if (Constants.type == Constants.Type.SHESHELLS) {
            navigationView = (NavigationView) findViewById(R.id.nav_view_newSidemenu_with_activity);
        } else if (Constants.type == Constants.Type.LIFEPLUS) {
            navigationView = (NavigationView) findViewById(R.id.nav_view_newSidemenu_with_activity);
        } else {
            Log.i("App Name : ", "Hemas App ");
            navigationView = (NavigationView) findViewById(R.id.nav_view_newSidemenu_with_activity_hemas);
        }


        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.navi_header_name);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        imgProfile = (CircleImageView) navHeader.findViewById(R.id.img_profile_new);
        navi_header_mobile_no = navHeader.findViewById(R.id.navi_header_mobile_no);
        ic_call_image = navHeader.findViewById(R.id.ic_call_image);
        view_container = navHeader.findViewById(R.id.view_container);
        layout_for_link_to_profile = navHeader.findViewById(R.id.layout_for_link_to_profile);
//        imgProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                if (ActivityCompat.checkSelfPermission(NewHomeWithSideMenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(NewHomeWithSideMenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                        //Show Information about why you need the permission
//                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NewHomeWithSideMenuActivity.this);
//                        builder.setTitle("Need Storage Permission");
//                        builder.setMessage("This app needs storage permission.");
//                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                                ActivityCompat.requestPermissions(NewHomeWithSideMenuActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
//                            }
//                        });
//                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                        builder.show();
//                    } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
//                        //Previously Permission Request was cancelled with 'Dont Ask Again',
//                        // Redirect to Settings after showing Information about why you need the permission
//                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NewHomeWithSideMenuActivity.this);
//                        builder.setTitle("Need Storage Permission");
//                        builder.setMessage("This app needs storage permission.");
//                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                                sentToSettings = true;
//                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                                intent.setData(uri);
//                                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
//                                Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
//                            }
//                        });
//                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                        builder.show();
//                    } else {
//                        //just request the permission
//                        ActivityCompat.requestPermissions(NewHomeWithSideMenuActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
//                    }
//
//                    SharedPreferences.Editor editor = permissionStatus.edit();
//                    editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
//                    editor.commit();
//
//
//                } else {
//                    //You already have the permission, just go ahead.
//                    selectImagePopup();
//                }
//            }
//        });

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        // initializing navigation menu

        if (Constants.type == Constants.Type.AYUBO) {
            setUpNavigationView();
        } else if (Constants.type == Constants.Type.LIFEPLUS) {
            setUpNavigationView_LifePlus();
        } else if (Constants.type == Constants.Type.SHESHELLS) {
            setUpNavigationView();
        } else {
            Log.i("App Name : ", "Hemas App ");
            setUpNavigationView_Hemas();
        }


        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

//        try {
//            displayFirebaseRegId();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            Service_getSetDefaultDevice_ServiceCall();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        try {
            callAppOnLoadDataService();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        new_home_loading = (ProgressAyubo)  findViewById(R.id.new_home_loading);


        System.out.println("==========OnCreate==========");
    }

    private void loadNavHeader() {
        try {
            if (Constants.type == Constants.Type.LIFEPLUS) {
                view_container.setBackgroundResource(R.drawable.lifeplus_nav_header_main_bg);
            }

            // txtName.setText(fullName);
            String sName = pref.getUserProfile().get("name");
            // userEmail=pref.getLoginUser().get("email");
            txtName.setText(sName);


            if (!pref.getLoginUser().get("mobile").equals("")) {
                ic_call_image.setVisibility(View.VISIBLE);
                navi_header_mobile_no.setText(pref.getLoginUser().get("mobile"));
            }

            layout_for_link_to_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Profile_clicked", null);
                    }
                    startActivity(new Intent(NewHomeWithSideMenuActivity.this, UserProfileActivity.class));
                    drawer.closeDrawers();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  FINISH ON CREATE==========================================
    @Override
    protected void onResume() {
        super.onResume();
        // load nav menu header data
        loadNavHeader();
        //  System.out.println("==========onResume==========");
//        txt_noti_count.setVisibility(View.INVISIBLE);


        String action = "";
        String meta = "";
        action = pref.getAllFireBaseNotificationAction();
        meta = pref.getAllFireBaseNotificationMeta();


        if (!action.equals("") && !meta.equals("")) {
            processAction(action, meta);
            pref.setAllFireBaseNotificationData("", "");
        }

//        if (pref.isHomeFirsttime().equals("false")) {
//            // System.out.println("==========isHomeFirsttime==========false");
//            String deviceName = pref.getDeviceData().get("stepdevice");
//
//            if (deviceName != null) {
//                //    System.out.println("========deviceName=========not null");
//                if (deviceName.equals("activity_AYUBO")) {
//
//                    HuaweiServices huaweiServices = new HuaweiServices(getApplicationContext());
//
//                    if (huaweiServices.isGooglePlayServicesAvailable()) {
//                        //      System.out.println("========deviceName=========activity_AYUBO");
//                        if (pref.isGoogleFitEnabled().equals("true")) {
//                            //  System.out.println("========isGoogleFitEnabled=========true");
//                            stepStatus = "good";
//                            check_buildFitnessClient();
//                        } else {
//                            //   System.out.println("========isGoogleFitEnabled=========true");
//                        }
//                    }
//                }
//            } else {
//                //  System.out.println("========deviceName=========null");
//            }
//        } else {
//            //System.out.println("==========isHomeFirsttime=======not===false"+pref.isHomeFirsttime());
//        }
        pref.setMapZoomLevel("5");


        if (pref.getUserToken().length() > 50) {
            getNotificationsData();
        }


//        Intent launchIntent = new Intent(getBaseContext(), VideoCallLoginActivity.class);
//        launchIntent.setClass(getBaseContext(), VideoCallLoginActivity.class);
//        pref.setPrefVdioCallerName("video_call");
//        pref.setVideocall("yes");
//        if (launchIntent != null) {
//            startActivity(launchIntent);
//        }


//        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
//        if (sharedPrefsHelper.hasQbUser()) {
//            Log.d(TAG, "----onMessageReceived---------App have logined user");
//            QBUser qbUser = sharedPrefsHelper.getQbUser();
//            CallService.start(this, qbUser);
//        }


        // String currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;


    }

    public void getNotificationsData() {

        String appToken = pref.getUserToken();

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<NotiCountMainResponse> call = apiService.getNotificationCount(appToken);
        // Call<NotiCountMainResponse> call = apiService.getNotificationCount(AppConfig.APP_BRANDING_ID,AppConfig.SUGER_API_TOKEN);
        call.enqueue(new Callback<NotiCountMainResponse>() {
            @Override
            public void onResponse(Call<NotiCountMainResponse> call, Response<NotiCountMainResponse> response) {

                if (response.isSuccessful()) {
                    NotiCountMainResponse res = response.body();
                    NotiCountMainData data = res.getData();

                    if (data != null) {


                        int count = data.getUnreadCount();

                        if (count > 0) {
                            notificationCount = Integer.toString(count);
//                            txt_noti_count.setText(notificationCount);

                            final Animation myAnim = AnimationUtils.loadAnimation(NewHomeWithSideMenuActivity.this, R.anim.bounce);
                            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.5, 20);
                            myAnim.setInterpolator(interpolator);


//                            if (!isFirsttime) {
//                                isFirsttime = true;
//                                MediaPlayer mp = MediaPlayer.create(NewHomeWithSideMenuActivity.this, Settings.System.DEFAULT_NOTIFICATION_URI);
//                                if (mp != null) {
//                                    mp.start();
//                                }
//
//                            }


//                            btn_notifications.startAnimation(myAnim);
//                            txt_noti_count.setVisibility(View.VISIBLE);

                        } else {
//                            txt_noti_count.setVisibility(View.INVISIBLE);
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<NotiCountMainResponse> call, Throwable t) {

                System.out.println(t);
            }
        });
    }

    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }

//    private void check_buildFitnessClient() {
//        // Create the Google API Client
//        if (mApiClient != null) {
//            System.out.println("==========mClient not null=====================");
//            if (mApiClient.isConnected()) {
//
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(new Date());
//                dateFromDB_forADay0 = sdf.format(cal.getTime());
//
//
//                try {
//                    System.out.println("=========send10Days_GFITSteps====================");
//                    send10Days_GFITSteps();
//
//                    // new InsertAndVerifyDataTask00().execute();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            } else {
//                mApiClient.connect();
//                if (mApiClient.isConnected()) {
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(new Date());
//                    dateFromDB_forADay0 = sdf.format(cal.getTime());
//
//
//                    try {
//                        send10Days_GFITSteps();
//                        // new InsertAndVerifyDataTask00().execute();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                } else {
//
//                }
//
//            }
//
//        } else {
//            System.out.println("========== FIT Not Connected=====================");
//            mApiClient = new GoogleApiClient.Builder(NewHomeWithSideMenuActivity.this)
//                    .addApi(Fitness.RECORDING_API)
//                    .addApi(Fitness.HISTORY_API)
//                    .addApi(Fitness.SENSORS_API)
//                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
//                    .addConnectionCallbacks(
//                            new GoogleApiClient.ConnectionCallbacks() {
//                                @Override
//                                public void onConnected(Bundle bundle) {
//                                    Log.i(TAG, "Connected!!!");
//                                    Calendar cal = Calendar.getInstance();
//                                    cal.setTime(new Date());
//                                    dateFromDB_forADay0 = sdf.format(cal.getTime());
//                                    //  Toast.makeText(NewHomeWithSideMenuActivity.this, "Getting GFIT Steps 0", Toast.LENGTH_LONG).show();
//                                    //   Toast.makeText(NewHomeWithSideMenuActivity.this, "GoogleFit 0", Toast.LENGTH_LONG).show();
//
//                                    try {
//                                        send10Days_GFITSteps();
//                                        //  new InsertAndVerifyDataTask00().execute();
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//
//                                @Override
//                                public void onConnectionSuspended(int i) {
//                                    // If your connection to the portrait gets lost at some point,
//                                    // you'll be able to determine the reason and react to it here.
//                                    pref.setGoogleFitEnabled("false");
//                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
//                                        Log.i(TAG, "Connection lost.  Cause: Network Lost.");
//                                    } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
//                                        Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
//                                    }
//                                }
//                            }
//                    )
//                    .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
//                        @Override
//                        public void onConnectionFailed(ConnectionResult result) {
//                            Log.i(TAG, "Google Play services connection failed. Cause: " +
//                                    result.toString());
//                            pref.setGoogleFitEnabled("false");
//                            Snackbar.make(NewHomeWithSideMenuActivity.this.findViewById(android.R.id.content),
//                                    "Unable to connect GoogleFit",
//                                    Snackbar.LENGTH_INDEFINITE).setAction("DISMISS",
//                                    new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//
//                                        }
//                                    }).show();
//
//                        }
//                    })
//                    .build();
//        }
//
//
//    }

//    private void send10Days_GFITSteps() {
//        GFitGetSteps_LastWeek task = new GFitGetSteps_LastWeek();
//        task.execute();
//    }

//    private class GFitGetSteps_LastWeek extends AsyncTask<Void, Void, String> {
//        @Override
//        protected String doInBackground(Void... arg0) {
//            String ste = "0";
//
//            DataReadRequest readRequest = queryFitnessDataLastWeek();
//            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mApiClient, readRequest).await(1, TimeUnit.MINUTES);
//
//            //This is new Testing one
//            printDataNewForLast(dataReadResult);
//            return ste;
//        }
//
//        @Override
//        protected void onPostExecute(String steps) {
//
//            send10Days_GFIT();
//
//        }
//
//    }

//    public static void showDataSetForLastWeek(DataSet dataSet) {
//        Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
//        DateFormat dateFormat = DateFormat.getDateInstance();
//        DateFormat timeFormat = DateFormat.getTimeInstance();
//
//
//        String strSteps = null;
//        String strDate = null;
//        for (DataPoint dp : dataSet.getDataPoints()) {
//
//            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//            strDate = format1.format(dp.getStartTime(TimeUnit.MILLISECONDS));
//            System.out.println("================Date============================" + strDate);
//
//            String si[] = dp.getOriginalDataSource().getStreamIdentifier().toLowerCase().split(":");
//
//            //     System.out.println("===******AYUBO*********=="+si[si.length - 1]);
//
//            for (Field field : dp.getDataType().getFields()) {
//                System.out.println("===dataSet====Not=Empty==========2===");
//
//                strSteps = dp.getValue(field).toString();
//
//                if ((((si[si.length - 1].contains("soft")) || (si[si.length - 1].contains("step"))) && si[si.length - 1].contains("counter"))) {
//                    stepStatus = "good";
//                    System.out.println("=====GFIT=======Good Steps================");
//                } else {
//                    if (dp.getOriginalDataSource().getStreamName().equals("user_input")) {
//                        stepStatus = "bad";
//                        System.out.println("======BAD STEPS ===========");
//                    }
//                }
//            }
//
//
//            jsonStepsForWeekArray.put(createGFitStepsDataArray(strSteps, strDate, stepStatus));
//            jsonStepsForWeekArrayForNewSave.add(createGFitStepsDataArrayForNewSave(strSteps, dp.getStartTime(TimeUnit.MILLISECONDS), dp.getEndTime(TimeUnit.MILLISECONDS)));
//        }
//
//
//    }

//    public static void printDataNewForLast(DataReadResult dataReadResult) {
//        //Used for aggregated data
//        if (dataReadResult.getBuckets().size() > 0) {
//            Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
//            for (Bucket bucket : dataReadResult.getBuckets()) {
//                List<DataSet> dataSets = bucket.getDataSets();
//                for (DataSet dataSet : dataSets) {
//                    showDataSetForLastWeek(dataSet);
//                }
//            }
//        }
////Used for non-aggregated data
//        else if (dataReadResult.getDataSets().size() > 0) {
//            Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
//            for (DataSet dataSet : dataReadResult.getDataSets()) {
//                showDataSetForLastWeek(dataSet);
//            }
//        }
//    }

//    public static DataReadRequest queryFitnessDataLastWeek() {
//
//
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
//        Date now = new Date();
//        cal.setTime(now);
//        long endTime = cal.getTimeInMillis();
//        cal.add(Calendar.WEEK_OF_YEAR, -1);
//
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//
//        long startTime = cal.getTimeInMillis();
//
//
//        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
//        Log.i(TAG, "Range End: " + dateFormat.format(endTime));
//
//        DataReadRequest readRequest = new DataReadRequest.Builder()
//                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
//                .bucketByTime(1, TimeUnit.DAYS)
//                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
//                .build();
//
//        return readRequest;
//    }

//    public static JSONObject createGFitStepsDataArray(String steps, String date, String isStepsGood) {
//
//        float value = Float.parseFloat(steps);
//        String sMets, sCals, sDis;
//        double metsVal = (value / 130) * 4.5;
//        sMets = String.format("%.0f", metsVal);
//        double calories = (metsVal * 3.5 * 70) / 200;
//        sCals = String.format("%.0f", calories);
//        double distance = (value * 78) / (float) 100000;
//        sDis = String.format("%.2f", distance);
//
//        JSONObject list1 = new JSONObject();
//
//        String run_count = "11";
//
//
//        if (isStepsGood.equals("bad")) {
//            run_count = "22";
//        } else {
//            run_count = "11";
//        }
//
//        try {
//            list1.put("userid", userid_ExistingUser_static);
//            list1.put("activity", "activity_AYUBO");
//            list1.put("energy", sMets);
//            list1.put("steps", steps);
//            list1.put("calorie", sCals);
//            list1.put("duration", "0");
//            list1.put("distance", sDis);
//            list1.put("date", date);
//            list1.put("walk_count", steps);
//            list1.put("run_count", run_count);
//            list1.put("version", versionName);
//            list1.put("osType", "android");
//            list1.put("device_modal", device_modal);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return list1;
//    }

//    public static StepObj createGFitStepsDataArrayForNewSave(String steps, Long startTime, Long endTime) {
//        StepObj stepObj = new StepObj();
//        stepObj.setStepCount(Integer.parseInt(steps));
//        stepObj.setStartDateTime(startTime);
//        stepObj.setEndDateTime(endTime);
//        return stepObj;
//    }


    @Override
    public void retryPageLoad() {
        //  loadNextPage(appToken);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//
//    private void Service_sendAyuboStepsToServer_ForTenDays0() {
//        sendAyuboStepsToServer_ForTenDays0 task = new sendAyuboStepsToServer_ForTenDays0();
//        task.execute(new String[]{ApiClient.BASE_URL_live});
//    }
//
//    private class sendAyuboStepsToServer_ForTenDays0 extends AsyncTask<String, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... urls) {
//            if (stepCount != null) {
//                sendAyuboSteps_ForTenDays0();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//        }
//    }
//
//
//    private void sendAyuboSteps_ForTenDays0() {
//
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
//        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
//        //Post Data
//        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        dateFromDB_forADay0 = sdf.format(cal.getTime());
//
//        float value = Float.parseFloat(stepCount);
//        String sMets, sCals, sDis;
//        double metsVal = (value / 130) * 4.5;
//        sMets = String.format("%.0f", metsVal);
//        double calories = (metsVal * 3.5 * 70) / 200;
//        sCals = String.format("%.0f", calories);
//        double distance = (value * 78) / (float) 100000;
//        sDis = String.format("%.2f", distance);
//
//        String run_count = "11";
//
//        if (stepStatus.equals("bad")) {
//            run_count = "22";
//        } else {
//            run_count = "11";
//        }
//
//        String jsonStr =
//                "{" +
//                        "\"userid\": \"" + userid_ExistingUser + "\"," +
//                        "\"activity\": \"" + "activity_AYUBO" + "\"," +
//                        "\"energy\": \"" + sMets + "\"," +
//                        "\"steps\": \"" + stepCount + "\"," +
//                        "\"calorie\": \"" + sCals + "\", " +
//                        "\"duration\": \"" + "0" + "\"," +
//                        "\"distance\": \"" + sDis + "\"," +
//                        "\"date\": \"" + dateFromDB_forADay0 + "\"," +
//                        "\"walk_count\": \"" + stepCount + "\"," +
//                        "\"run_count\": \"" + run_count + "\"," +
//                        "\"version\": \"" + versionName + "\"," +
//                        "\"osType\": \"" + "android" + "\"," +
//                        "\"device_modal\": \"" + device_modal
//                        + "\"" +
//                        "}";
//
//
//        nameValuePair.add(new BasicNameValuePair("method", "addDailyActivitySummary"));
//        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));
//
//
//        System.out.println("....Today......addDailyActivitySummary....." + nameValuePair.toString());
//        //Encoding POST data
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
//        } catch (UnsupportedEncodingException e) {
//            // log exception
//            e.printStackTrace();
//        }
//
//        //making POST request.
//        try {
//            HttpResponse response = httpClient.execute(httpPost);
//            System.out.println("..........response..........." + response);
//
//            String responseString = null;
//            try {
//                responseString = EntityUtils.toString(response.getEntity());
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            JSONObject jsonObj = null;
//            try {
//                jsonObj = new JSONObject(responseString);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            String number = jsonObj.optString("number");
//
//            if (number.isEmpty()) {
//
//                String res = jsonObj.optString("result");
//
//                int result = Integer.parseInt(res);
//                if (result == 11) {
//                    stepsSentToDBStatus = "11";
//                }
//                if (result == 0) {
//                    pref.setHomeFirsttime("true");
//                    stepsSentToDBStatus = "0";
//                }
//
//            } else {
//                stepsSentToDBStatus = "99";
//            }
//
//
//        } catch (ClientProtocolException e) {
//            // Log exception
//            e.printStackTrace();
//        } catch (IOException e) {
//            // Log exception
//            e.printStackTrace();
//        }
//
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        isProfileLoaded = false;


    }

    @Override
    protected void onStop() {
        super.onStop();

        isGoogleFitConnected = false;

        isLoaded = false;
        // System.out.println("======onStop======================");

        if (myDoctorCallRing != null) {
            if (myDoctorCallRing.isPlaying()) {
                myDoctorCallRing.stop();
            }
        }
    }


    private void callAppOnLoadDataService() {

        if (Utility.isInternetAvailable(NewHomeWithSideMenuActivity.this)) {
            Service_getAppOnLoadData task = new Service_getAppOnLoadData();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {

        }
    }


    private class Service_getAppOnLoadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            userStatus = "999";
        }

        @Override
        protected String doInBackground(String... urls) {
            getAppOnLoadData();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            String sName = pref.getUserProfile().get("name");
            // String sName=pref.getUserProfile().get("name");
            txtName.setText(sName);


            ArrayList<MenuObj> datesListArray2 = null;
            JSONArray myDataListsAll = null;
            datesListArray2 = new ArrayList<MenuObj>();

            if (!isProfileLoaded) {
                //Add Popup Menu ...................................
                isProfileLoaded = true;
                if (append_menu != null) {
//                    int noticount = Integer.parseInt(notification_count);
//                    if (noticount > 0) {
//                        notibg.setVisibility(View.VISIBLE);
//                        txt_noti_count.setVisibility(View.VISIBLE);
//                        txt_noti_count.setText(notification_count);
//                    } else {
//                        txt_noti_count.setVisibility(View.GONE);
//                        notibg.setVisibility(View.GONE);
//                    }

                    try {
                        myDataListsAll = new JSONArray(append_menu);
                        for (int i = 0; i < myDataListsAll.length(); i++) {
                            JSONObject jsonMainNode3 = null;
                            try {
                                jsonMainNode3 = (JSONObject) myDataListsAll.get(i);
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                            String image = jsonMainNode3.optString("image");
                            String link = jsonMainNode3.optString("link");
                            String lable = jsonMainNode3.optString("lable");
                            datesListArray2.add(new MenuObj(image, link, lable));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                    Ram.setMenuIconList(datesListArray2);
                }

                //Set Profile Image ...................................
                boolean isImageSet = Ram.isProfileImageStatus();
                if (!isImageSet) {
                    //   System.out.println("====IS Profile Picture set=======" + isImageSet);
                    imagepath_db = pref.getLoginUser().get("image_path");
                    if (imagepath_db != null) {
                        Random rand = new Random();
                        int n = rand.nextInt(50) + 1;
                        String rannum = Integer.toString(n);
                        try {

                            //    System.out.println("=====Profile URL====0======" + imagepath_db);


                            if (imagepath_db.contains(".jpg") || imagepath_db.contains(".jpeg") || imagepath_db.contains(".png")) {
                                Glide.with(getBaseContext()).load(imagepath_db).into(imgProfile);
                            } else {
                                RequestOptions requestOptions = new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .transforms(new CircleTransform(NewHomeWithSideMenuActivity.this));
                                Glide.with(NewHomeWithSideMenuActivity.this).load(imagepath_db)
                                        .thumbnail(0.5f)
                                        .apply(requestOptions)
                                        .into(imgProfile);

                                Ram.setProfileImageStatus(true);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Random rand = new Random();
                        int n = rand.nextInt(50) + 1;
                        String rannum = Integer.toString(n);
                        imagepath_db = pref.getLoginUser().get("image_path");

                        if (imagepath_db.contains(".jpg") || imagepath_db.contains(".jpeg") || imagepath_db.contains(".png")) {
                            Glide.with(getBaseContext()).load(imagepath_db).into(imgProfile);
                        } else {
                            String burlImg = MAIN_URL_LIVE_HAPPY + imagepath_db + "&cache=" + rannum;

                            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).transforms(new CircleTransform(NewHomeWithSideMenuActivity.this));
                            Glide.with(NewHomeWithSideMenuActivity.this).load(burlImg)
                                    .thumbnail(0.5f)
                                    .transition(withCrossFade())
                                    .apply(requestOptions)
                                    .into(imgProfile);
                        }


                        Ram.setProfileImageStatus(true);
                    }
                }
            }
//======================================================

            String deviceStatus = pref.getDeviceData().get("stepdevice");

            googlefitenabled = pref.isGoogleFitEnabled();

            String times = pref.isGoogleFitDisabledTimes();


//            if(deviceStatus.equals("activity_AYUBO")) {
//                if (googlefitenabled.equals("false")) {
//                    int nooftimes = Integer.parseInt(times);
//                    if (nooftimes < 2) {
//
//                        if (nooftimes == 0) {
//                            pref.setGoogleFitDisabledTimes("1");
//                        } else if (nooftimes == 1) {
//                            pref.setGoogleFitDisabledTimes("2");
//                        }
//                        else if(nooftimes==2){
//                            pref.setGoogleFitDisabledTimes("3");
//                        }
//
//                        System.out.println("===========nooftimes=============activity_AYUBO=====" + times);
//
//                        final AlertDialog.Builder builder = new AlertDialog.Builder(NewHomeWithSideMenuActivity.this);
//                        LayoutInflater inflater = (LayoutInflater) NewHomeWithSideMenuActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                        final View layoutView = inflater.inflate(R.layout.alert_ask_googlefit_connect_desc, null, false);
//                        builder.setView(layoutView);
//
//                        final AlertDialog dialog = builder.create();
//                        dialog.setCancelable(false);
//                        Button dialogButton = (Button) layoutView.findViewById(R.id.btn_Alert_Cancel);
//                        dialogButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.cancel();
//                               // send10Days_Native();
//
//                            }
//                        });
//                        Button btn_Alert_Connect = (Button) layoutView.findViewById(R.id.btn_Alert_Connect);
//                        btn_Alert_Connect.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.cancel();
//
//                            }
//                        });
//
//                        dialog.show();
//
//                    }else{
//
//                    }
//
//                } else {
//
//                }
//            }

            //    txt_noti_count.setText(notification_count);

        }

    }


    private void getAppOnLoadData() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live_v6);
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));

        String jsonStree =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "app_on_load"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStree));
        System.out.println(".............app_on_load................" + jsonStree);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }
        //making POST request.
        try {
            int r = 0;
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
                //  System.out.println("......getUserProfilePicture..response..........." + response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) {
                r = response.getStatusLine().getStatusCode();
                if (r == 200) {

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

                    String res = jsonObj.optString("result").toString();
                    System.out.println(".............app_on_load......res.........." + res);
                    if (res.equals("0")) {
                        userStatus = "0";
                        try {
                            //  imagepath_db
                            String path_db = jsonObj.optString("data").toString();
                            System.out.println("=====dddd=====" + path_db);
                            JSONObject jsonObj2 = new JSONObject(path_db);
                            notification_count = jsonObj2.optString("notification_count").toString();
                            //    System.out.println("============notification_count====in Service=====================" + notification_count);
                            imagepath_db = jsonObj2.optString("profile_pic").toString();
                            String default_device_info = jsonObj2.optString("default_device_info").toString();
                            append_menu = jsonObj2.optString("append_menu").toString();
                            String activity_detailsArray = jsonObj2.optString("activity_details").toString();

                            JSONObject json_Default_device_info = new JSONObject(default_device_info);
                            user_first_name = jsonObj2.optString("user_first_name").toString();
                            user_last_name = jsonObj2.optString("user_last_name").toString();
                            // String activity_details = jsonObj2.optString("activity_details");

//                            String steps="0";
//                            JSONObject jsonObjActivityDetails = new JSONObject(activity_details);
//                            String result = jsonObjActivityDetails.optString("result");
//                            if(result.equals("0")){
//                                String data = jsonObjActivityDetails.optString("data");
//                                JSONObject stepsDataJSON = new JSONObject(data);
//                                steps = stepsDataJSON.optString("total_steps");
//                            }

                            show_lang_screen = jsonObj2.optString("show_lang_screen");
                            if (show_lang_screen.equals("true")) {
                                pref.setLanguageSelection(true);
                            }


                            fullName = user_first_name + " " + user_last_name;


                            String dName = json_Default_device_info.optString("name").toString();
                            String dIcon = json_Default_device_info.optString("icon").toString();
                            pref.createDeviceData(dIcon, dName);

                            JSONObject jsonActivityDetails = new JSONObject(activity_detailsArray);
                            String activity_details = jsonActivityDetails.optString("data").toString();
                            JSONObject jsonData = new JSONObject(activity_details);

                            total_steps = jsonData.optString("total_steps").toString();
                            total_calories = jsonData.optString("total_calories").toString();
                            total_distance = jsonData.optString("total_distance").toString();
                            total_duration = jsonData.optString("total_duration").toString();
                            total_energy = jsonData.optString("total_energy").toString();

                            if (dName != null) {
                                deviceStatus = dName;
                            }

                            pref.createUserData(total_steps, total_calories, total_energy, total_distance, total_duration);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        userStatus = "17";
                        errorData = jsonObj.optString("error").toString();
                    }


                }

            }

        } catch (Exception e) {
            // Log exception
            e.printStackTrace();
        }
    }

//    public static String printData(DataReadResult dataReadResult) {
//        String step = "0";
//        if (dataReadResult.getBuckets().size() > 0) {
//            Log.i(TAG, "Number of returned buckets of DataSets is: "
//                    + dataReadResult.getBuckets().size());
//            for (Bucket bucket : dataReadResult.getBuckets()) {
//                List<DataSet> dataSets = bucket.getDataSets();
//                for (DataSet dataSet : dataSets) {
//                    step = dumpDataSet(dataSet);
//                }
//            }
//        } else if (dataReadResult.getDataSets().size() > 0) {
//            Log.i(TAG, "Number of returned DataSets is: "
//                    + dataReadResult.getDataSets().size());
//            for (DataSet dataSet : dataReadResult.getDataSets()) {
//                step = dumpDataSet(dataSet);
//            }
//
//        }
//        return step;
//        // [END parse_read_data_result]
//    }

//    private static String dumpDataSet(DataSet dataSet) {
//        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
//        DateFormat dateFormat = getTimeInstance();
//
//        if (dataSet.isEmpty()) {
//            //  System.out.println("========Empty======GFit Steps============");
//            stepCount = "0";
//        } else {
//            for (DataPoint dp : dataSet.getDataPoints()) {
//                Log.i(TAG, "Data point:");
//                Log.i(TAG, "\tType: " + dp.getDataType().getName());
//
//                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
//                for (Field field : dp.getDataType().getFields()) {
//                    Log.i(TAG, "\tField: " + field.getName() +
//                            "GFit Steps Value: " + dp.getValue(field));
//                    stepCount = "0";
//                    stepCount = dp.getValue(field).toString();
//                    //   Ram.setGoogleFitSteps(stepCount);
//                    // System.out.println("==============GFit Steps Value:==================" + stepCount);
//                }
//            }
//        }
//
//        return stepCount;
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                selectImagePopup();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(NewHomeWithSideMenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewHomeWithSideMenuActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            ActivityCompat.requestPermissions(NewHomeWithSideMenuActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

//        if (resultCode == Activity.RESULT_OK) {
//
//
//            if (requestCode == SELECT_FILE)
//
//                onSelectFromGalleryResult(data);
//            else if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);
//        }
        System.out.println("============+++++++++++++++++++++++++++++++++++++=============================");
    }


//    @Override
//    protected View getSnackbarAnchorView() {
//        return null;
//    }


//    private void displayFirebaseRegId() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        String regId = pref.getString("regId", null);
//
//        if (!TextUtils.isEmpty(regId)) {
//            deviceIdPush = regId;
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putString("push_id", deviceIdPush);
//            editor.commit();
//            Log.e(TAG, "====Notification=push_id=======" + deviceIdPush);
//
//        } else {
//            deviceIdPush = FirebaseInstanceId.getInstance().getToken();
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putString("push_id", deviceIdPush);
//            editor.commit();
//
//            Log.e(TAG, "========Notification registration faild!=========" + deviceIdPush);
//        }
//    }

//    private void Service_getSetDefaultDevice_ServiceCall() {
//
//        try {
//            if (Utility.isInternetAvailable(NewHomeWithSideMenuActivity.this)) {
//                Service_SetDefaultDevice task = new Service_SetDefaultDevice();
//                task.execute(new String[]{ApiClient.BASE_URL_live});
//            } else {
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private class Service_SetDefaultDevice extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            makeSetDefaultDevice();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//        }
//    }

//    private void makeSetDefaultDevice() {
//
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
//        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
//        //Post Data
//        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
//
//        String versionName = BuildConfig.VERSION_NAME;
//
//
//        String jsonStr =
//                "{" +
//                        "\"userID\": \"" + userid_ExistingUser + "\"," +
//                        "\"deviceID\": \"" + deviceIdPush + "\"," +
//                        "\"voipId\": \"" + "" + "\"," +
//                        "\"version\": \"" + versionName + "\"," +
//                        "\"os\": \"" + "android"
//                        + "\"" +
//                        "}";
//
//
//        nameValuePair.add(new BasicNameValuePair("method", "setMyNewDeviceID"));
//        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));
//
//
//        System.out.println(".......send Device id with....setMyNewDeviceID..............." + nameValuePair.toString());
//
//        //Encoding POST data
//        System.out.println("............App steps Upldated..................." + nameValuePair.toString());
//        //Encoding POST data
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
//        } catch (UnsupportedEncodingException e) {
//            // log exception
//            e.printStackTrace();
//        }
//
//        //making POST request.
//        try {
//            HttpResponse response = httpClient.execute(httpPost);
//            System.out.println("==========deviceIdPush===Sent=======" + deviceIdPush);
//            int r = 0;
//
//        } catch (ClientProtocolException e) {
//            // Log exception
//            e.printStackTrace();
//        } catch (IOException e) {
//            // Log exception
//            e.printStackTrace();
//        }
//
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        boolean retunStatus;

        if (isBackFirsttime) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                isStepUpdated = false;
                pref.setHomeFirsttime("false");
                isFirsttime = false;
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
            isBackFirsttime = false;
            retunStatus = super.onKeyDown(keyCode, event);
        } else {
            pref.setHomeFirsttime("false");
            Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_LONG).show();
            isBackFirsttime = true;

            retunStatus = false;
        }


        return retunStatus;
    }

    private void loadHomeFragment() {
        // Ram.setTopMenuTabName("home");
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        //   setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            //   toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        //  toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        //  invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                // Ram.setTopMenuTabName("home");
                //   TimeLineFragment NewHome
                com.ayubo.life.ayubolife.fragments.NewHomeDesign homeFragment = new NewHomeDesign();
                // TimeLineFragment homeFragment = new TimeLineFragment();
                return homeFragment;
            case 1:
                // photos
                TimeLineFragment homeFragment2 = new TimeLineFragment();
                return homeFragment2;
            case 2:
                // movies fragment
                TimeLineFragment homeFragment3 = new TimeLineFragment();
                return homeFragment3;
            case 3:
                // notifications fragment
                TimeLineFragment homeFragment4 = new TimeLineFragment();
                return homeFragment4;

            case 4:
                // settings fragment
                TimeLineFragment homeFragment5 = new TimeLineFragment();
                return homeFragment5;
            default:
                return new TimeLineFragment();
        }
    }

    private void selectNavMenu() {


//        String tabName = Ram.getTopMenuTabName();
//        if(tabName.equals("history")){
//            navItemIndex=3;
//        }

        try {
            navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setUpNavigationView_LifePlus() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
//                    case R.id.nav_home:
//                        //  Ram.setTopMenuTabName("home");
//                        drawer.closeDrawers();
//                        navItemIndex = 0;
//                        CURRENT_TAG = TAG_HOME;
//                        break;

//                    case R.id.nav_language:
//                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, LanguageSelectionActivity.class));
//                        drawer.closeDrawers();
//                        return true;

                    case R.id.nav_share:
                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Share_ayubo_life_clicked", null);
                        }


                        try {
                            Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
                            //  Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            // String shareBody = "Check out the all in one ayubo.life wellness app. http://apps.ayubo.life/download";
                            // sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share ayubo.life");
                            String shareBody = "Check out the all in one lifeplus wellness app. http://bit.ly/lifeplusbd";
                            sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share lifeplus");
                            sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            sendIntent.setType("text/plain");
                            // startActivity(Intent.createChooser(sendIntent, "Share ayubo.life"));
                            startActivity(Intent.createChooser(sendIntent, "Share lifeplus"));
                            drawer.closeDrawers();
                        } catch (Exception e) {
                            e.toString();
                        }
                        return true;

                    case R.id.nav_subscriptions:

                        drawer.closeDrawers();
                        ApiInterface apiService =
                                ApiClient.getBaseApiClientNew().create(ApiInterface.class);


                        Call<ProfileDashboardResponseData> getWnWValidateCall = apiService.getWnWValidate(
                                AppConfig.APP_BRANDING_ID,
                                pref.getUserToken()
                        );

                        getWnWValidateCall.enqueue(new Callback<ProfileDashboardResponseData>() {

                            @Override
                            public void onResponse(Call<ProfileDashboardResponseData> call, Response<ProfileDashboardResponseData> response) {

                                if (response.isSuccessful()) {


                                    JsonObject wnwValidateMainData =
                                            new Gson().toJsonTree(response.body().getData()).getAsJsonObject();

//                                    Intent intent2 = new Intent(NewHomeWithSideMenuActivity.this, GroupViewActivity.class);
//                                    intent2.putExtra(EXTRA_PAYMENT_META, wnwValidateMainData.get("meta").getAsString());
//                                    startActivity(intent2);
                                    processAction(wnwValidateMainData.get("action").getAsString(), wnwValidateMainData.get("meta").getAsString());
                                } else {
                                    Toast.makeText(getBaseContext(), "No data found", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ProfileDashboardResponseData> call, Throwable throwable) {
                                throwable.printStackTrace();
                                Toast.makeText(getBaseContext(), "Server error", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });


//                        Intent intent2 = new Intent(NewHomeWithSideMenuActivity.this, GroupViewActivity.class);
//                        intent2.putExtra(EXTRA_PAYMENT_META, "100");
//                        startActivity(intent2);
//                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_promo:

                        //  FirebaseAnalytics Added Inside
                        Intent intentPromo = new Intent(NewHomeWithSideMenuActivity.this, EnterPromoCodeActivity.class);
                        intentPromo.putExtra("fromClass", "no");
                        startActivity(intentPromo);

                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_history:

                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("History_clicked", null);
                        }
                        Intent intent = new Intent(NewHomeWithSideMenuActivity.this, PurchaseHistory.class);

                        startActivity(intent);

                        drawer.closeDrawers();
                        return true;


                    case R.id.nav_payment_history:
                        //  FirebaseAnalytics Adding

                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Payment_History_Clicked", null);
                        }
                        Intent mainPaymentIntent = new Intent(NewHomeWithSideMenuActivity.this, MainPaymentHistoryActivity.class);

                        startActivity(mainPaymentIntent);

                        drawer.closeDrawers();
                        return true;


                    case R.id.nav_insurance:
                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Clicked_insurances", null);
                        }
                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, InsurancePolicesActivity.class));
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_lifepoints:

                        //  FirebaseAnalytics Added Inside
                        Intent inl = new Intent(NewHomeWithSideMenuActivity.this, LifePointActivity.class);
                        startActivity(inl);

                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_connect:

                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("ConnectDevice_clicked", null);
                        }
                        try {
                            try {
                                encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            url = ApiClient.BASE_URL + "index.php?module=MA_Health_Data&action=connectDevices";
                            String newUrl = Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"), url);

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(newUrl));
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_qrcode:

                        //  FirebaseAnalytics Added inside
                        Intent intentqrcode = new Intent(NewHomeWithSideMenuActivity.this, DecoderActivity.class);
                        intentqrcode.putExtra("error", "old");
                        startActivity(intentqrcode);

                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_profile:

                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Profile_clicked", null);
                        }
                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, UserProfileActivity.class));
                        //startActivity(new Intent(NewHomeWithSideMenuActivity.this, ProfileActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_about:

                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("About_clicked", null);
                        }


                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, AboutActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_help:

                        //  FirebaseAnalytics Added Inside
                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, HelpFeedbackActivity.class));
                        drawer.closeDrawers();
                        return true;

//                    case R.id.nav_badges:
//
//                        //  FirebaseAnalytics Adding
//                        if (SplashScreen.firebaseAnalytics != null) {
//                            SplashScreen.firebaseAnalytics.logEvent("Badges_clicked", null);
//                        }
//
//                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, Badges_Activity.class));
//                        drawer.closeDrawers();
//                        return true;


                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                //  loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
//                    case R.id.nav_home:
//                        //  Ram.setTopMenuTabName("home");
//                        drawer.closeDrawers();
//                        navItemIndex = 0;
//                        CURRENT_TAG = TAG_HOME;
//                        break;

//                    case R.id.nav_language:
//                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, LanguageSelectionActivity.class));
//                        drawer.closeDrawers();
//                        return true;

                    case R.id.nav_share:
                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Share_ayubo_life_clicked", null);
                        }


                        try {
                            Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
                            //  Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);

                            String shareBody = "";
                            String title = "";
                            if (Constants.type == Constants.Type.AYUBO) {
                                shareBody = "Check out the all in one ayubo.life wellness app. " + MAIN_URL_APPS + "download";
                                sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share ayubo.life");
                                title = "Share ayubo.life";
                            } else if (Constants.type == Constants.Type.LIFEPLUS) {
                                shareBody = "Check out the all in one lifeplus wellness app." + MAIN_URL_APPS + "downloadlifeplus";
                                sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share lifeplus");
                                title = "Share lifeplus";
                            } else {
                                shareBody = "Check out the all in one ayubo.life wellness app." + MAIN_URL_APPS + "download";
                                sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share ayubo.life");
                                title = "Share ayubo.life";
                            }

                            sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            sendIntent.setType("text/plain");
                            // startActivity(Intent.createChooser(sendIntent, "Share ayubo.life"));
                            startActivity(Intent.createChooser(sendIntent, title));
                            drawer.closeDrawers();
                        } catch (Exception e) {
                            e.toString();
                        }
                        return true;

                    case R.id.nav_insurance:
                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Clicked_insurances", null);
                        }
                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, InsurancePolicesActivity.class));
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_coporate:

                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Join_community_clicked", null);
                        }

                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, CommiunityActivity.class));
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_history:

                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("History_clicked", null);
                        }
                        Intent intent = new Intent(NewHomeWithSideMenuActivity.this, PurchaseHistory.class);

                        startActivity(intent);

                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_payment_history:
                        //  FirebaseAnalytics Adding

                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Payment_History_Clicked", null);
                        }
                        Intent mainPaymentIntent = new Intent(NewHomeWithSideMenuActivity.this, MainPaymentHistoryActivity.class);

                        startActivity(mainPaymentIntent);

                        drawer.closeDrawers();
                        return true;

//                    case R.id.nav_payments:
//
//                        Intent intentPay = new Intent(NewHomeWithSideMenuActivity.this, PaymentSettingsActivity.class);
//                        startActivity(intentPay);
//
//                        drawer.closeDrawers();
//                        return true;
                    case R.id.nav_promo:

                        //  FirebaseAnalytics Added Inside
                        Intent intentPromo = new Intent(NewHomeWithSideMenuActivity.this, EnterPromoCodeActivity.class);
                        intentPromo.putExtra("fromClass", "no");
                        startActivity(intentPromo);

                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_lifepoints:

                        //  FirebaseAnalytics Added Inside
                        Intent inl = new Intent(NewHomeWithSideMenuActivity.this, LifePointActivity.class);
                        startActivity(inl);

                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_connect:

                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("ConnectDevice_clicked", null);
                        }
                        try {
                            try {
                                encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            url = ApiClient.BASE_URL + "index.php?module=MA_Health_Data&action=connectDevices";
                            String newUrl = Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"), url);

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(newUrl));
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_qrcode:

                        //  FirebaseAnalytics Added inside
                        Intent intentQrcode = new Intent(NewHomeWithSideMenuActivity.this, DecoderActivity.class);
                        intentQrcode.putExtra("error", "old");
                        startActivity(intentQrcode);

                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_profile:

                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Profile_clicked", null);
                        }
                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, UserProfileActivity.class));
                        //   startActivity(new Intent(NewHomeWithSideMenuActivity.this, ProfileActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_about:

                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("About_clicked", null);
                        }

                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, AboutActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_help:

                        //  FirebaseAnalytics Added Inside
                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, HelpFeedbackActivity.class));
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_badges:

                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Badges_clicked", null);
                        }

                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, Badges_Activity.class));
                        drawer.closeDrawers();
                        return true;


                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                //  loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void setUpNavigationView_Hemas() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        //  Ram.setTopMenuTabName("home");
                        drawer.closeDrawers();
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;

                        break;
//                    case R.id.nav_share:
//                        System.out.println("======Share Ayubo life==============");
//
//                        //  FirebaseAnalytics Adding
//                        if (SplashScreen.firebaseAnalytics != null) {
//                            SplashScreen.firebaseAnalytics.logEvent("Share_ayubo_life_clicked", null);
//                        }
//
//
//                        try {
//                            Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
//                            //  Intent sendIntent = new Intent();
//                            sendIntent.setAction(Intent.ACTION_SEND);
//                            String shareBody = "Check out the all in one ayubo.life wellness app. http://apps.ayubo.life/download";
//
//                            String shareBody = "Check out the all in one ayubo.life wellness app. http://apps.ayubo.life/download";
//
//
//                            //                        sendIntent.putExtra(Intent.EXTRA_TEXT,
////                                "Check out the all in one ayubo.life wellness app. Https://www.ayubo.life/download");
//                            sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share ayubo.life");
//                            sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                            sendIntent.setType("text/plain");
//                            startActivity(Intent.createChooser(sendIntent, "Share ayubo.life"));
//                            //   startActivity(sendIntent);
//                            //   frameLayout.getBackground().setAlpha(0);
//                            drawer.closeDrawers();
//                        } catch (Exception e) {
//                            e.toString();
//                        }
//                        return true;
//                    case R.id.nav_coporate:
//
//                        //  FirebaseAnalytics Adding
//                        if (SplashScreen.firebaseAnalytics != null) {
//                            SplashScreen.firebaseAnalytics.logEvent("Join_community_clicked", null);
//                        }
//
//                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, CommiunityActivity.class));
//                        drawer.closeDrawers();
//                        return true;
                    case R.id.nav_promo:

                        //  FirebaseAnalytics Added Inside
                        Intent in = new Intent(NewHomeWithSideMenuActivity.this, EnterPromoCodeActivity.class);
                        in.putExtra("fromClass", "no");
                        startActivity(in);

                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_history:

                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("History_clicked", null);
                        }
                        Intent intent = new Intent(NewHomeWithSideMenuActivity.this, PurchaseHistory.class);

                        startActivity(intent);

                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_payment_history:
                        //  FirebaseAnalytics Adding

                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Payment_History_Clicked", null);
                        }
                        Intent mainPaymentIntent = new Intent(NewHomeWithSideMenuActivity.this, MainPaymentHistoryActivity.class);

                        startActivity(mainPaymentIntent);

                        drawer.closeDrawers();
                        return true;

//                    case R.id.nav_lifepoints:
//
//                        //  FirebaseAnalytics Added Inside
//                        Intent inl = new Intent(NewHomeWithSideMenuActivity.this, LifePointActivity.class);
//                        startActivity(inl);
//
//                        drawer.closeDrawers();
//                        return true;
                    case R.id.nav_connect:

                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("ConnectDevice_clicked", null);
                        }
                        try {
                            try {
                                encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            url = ApiClient.BASE_URL + "index.php?module=MA_Health_Data&action=connectDevices";
                            String newUrl = Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"), url);

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(newUrl));
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_qrcode:

                        //  FirebaseAnalytics Added inside
                        Intent intentqrcode = new Intent(NewHomeWithSideMenuActivity.this, DecoderActivity.class);
                        intentqrcode.putExtra("error", "old");
                        startActivity(intentqrcode);

                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_profile:

                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Profile_clicked", null);
                        }
                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, UserProfileActivity.class));
                        // startActivity(new Intent(NewHomeWithSideMenuActivity.this, ProfileActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_about:

                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("About_clicked", null);
                        }

                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, AboutActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_help:

                        //  FirebaseAnalytics Added Inside
                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, HelpFeedbackActivity.class));
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_badges:
                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("Badges_clicked", null);
                        }

                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, Badges_Activity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_contact:
                        //  FirebaseAnalytics Added Inside
                        //  FirebaseAnalytics Adding
                        if (SplashScreen.firebaseAnalytics != null) {
                            SplashScreen.firebaseAnalytics.logEvent("ContactInfo_clicked", null);
                        }
                        startActivity(new Intent(NewHomeWithSideMenuActivity.this, ContactInfoActivity.class));
                        drawer.closeDrawers();
                        return true;

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                //  loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {


        isStepUpdated = false;
        pref.setHomeFirsttime("false");

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }


        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

//    private String getStepsDataFromDatabase(String date) {
//        todaysummery data = null;
//        int step_from_db = 0;
//        try {
//            data = db.getSelectedDaySummeryFromDB(date);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            if (data == null) {
//                step_from_db = 0;
//            } else {
//                step_from_db = data.getStep_tot();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("==========date=========" + date);
//        System.out.println("==========steps=========" + step_from_db);
//        String steps = Integer.toString(step_from_db);
//
//        return steps;
//    }
//
//    private String getDateForHistory(int days) {
//        String day = null;
//        Calendar calObj = Calendar.getInstance();
//        calObj.setTime(new Date());
//        calObj.add(Calendar.DATE, -days);
//        day = sdf.format(calObj.getTime());
//        System.out.println("=========day==========" + day);
//        return day;
//    }

//    private JSONArray collectNativeStepsFor10Days() {
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(0)), getDateForHistory(0)));
//        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(1)), getDateForHistory(1)));
//        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(2)), getDateForHistory(2)));
//        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(3)), getDateForHistory(3)));
//        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(4)), getDateForHistory(4)));
//        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(5)), getDateForHistory(5)));
//        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(6)), getDateForHistory(6)));
//        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(7)), getDateForHistory(7)));
//        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(8)), getDateForHistory(8)));
//        jsonArray.put(createStepsDataArray(getStepsDataFromDatabase(getDateForHistory(9)), getDateForHistory(9)));
//        return jsonArray;
//    }

//    private JSONArray collectGFITStepsDatass() {
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put(createStepsDataArray(gfit_steps_0, getDateForHistory(0)));
//        jsonArray.put(createStepsDataArray(gfit_steps_1, getDateForHistory(1)));
//        jsonArray.put(createStepsDataArray(gfit_steps_2, getDateForHistory(2)));
//        jsonArray.put(createStepsDataArray(gfit_steps_3, getDateForHistory(3)));
//        jsonArray.put(createStepsDataArray(gfit_steps_4, getDateForHistory(4)));
//        jsonArray.put(createStepsDataArray(gfit_steps_5, getDateForHistory(5)));
//        jsonArray.put(createStepsDataArray(gfit_steps_6, getDateForHistory(6)));
//        jsonArray.put(createStepsDataArray(gfit_steps_7, getDateForHistory(7)));
//        jsonArray.put(createStepsDataArray(gfit_steps_8, getDateForHistory(8)));
//        jsonArray.put(createStepsDataArray(gfit_steps_9, getDateForHistory(9)));
//        return jsonArray;
//    }


//    public static DataReadRequest queryFitnessData(int days) {
//
//        Calendar secondDay = Calendar.getInstance();
//        secondDay.add(Calendar.DATE, -days);
//
//        SimpleDateFormat format1 = new SimpleDateFormat("dd-M-yyyy");
//        String dateString = format1.format(secondDay.getTime());
//
//        long endTime;
//        long midNight;
//
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//
//        String startDateString = dateString + " 00:00:01";
//        String endDateString = dateString + " 23:59:59";
//
//        Calendar calendarStart = null;
//        Calendar calendarEnd = null;
//        try {
//            //formatting the dateString to convert it into a Date
//            Date sdate = sdf.parse(startDateString);
//            Date edate = sdf.parse(endDateString);
//
//            calendarStart = Calendar.getInstance();
//            calendarEnd = Calendar.getInstance();
//            //Setting the Calendar date and time to the given date and time
//            calendarStart.setTime(sdate);
//            //  System.out.println("Given Time in milliseconds Staart: "+calendars.getTimeInMillis());
//            calendarEnd.setTime(edate);
//            //  System.out.println("Given Time in milliseconds End: "+calendare.getTimeInMillis());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        midNight = calendarStart.getTimeInMillis();
//        endTime = calendarEnd.getTimeInMillis();
//        //===================================================
//
//        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
//                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
//                .setType(DataSource.TYPE_DERIVED)
//                .setStreamName("estimated_steps")
//                .setAppPackageName("com.google.android.gms")
//                .build();
//
//        DataReadRequest readRequest = new DataReadRequest.Builder()
//                .aggregate(ESTIMATED_STEP_DELTAS, DataType.AGGREGATE_STEP_COUNT_DELTA)
//                .bucketByTime(1, TimeUnit.DAYS)
//                .setTimeRange(midNight, endTime, TimeUnit.MILLISECONDS)
//                .build();
//
//        return readRequest;
//    }

//    JSONObject createStepsDataArray(String steps, String date) {
//
//        float value = Float.parseFloat(steps);
//        String sMets, sCals, sDis;
//        double metsVal = (value / 130) * 4.5;
//        sMets = String.format("%.0f", metsVal);
//        double calories = (metsVal * 3.5 * 70) / 200;
//        sCals = String.format("%.0f", calories);
//        double distance = (value * 78) / (float) 100000;
//        sDis = String.format("%.2f", distance);
//
//        JSONObject list1 = new JSONObject();
//        try {
//            list1.put("userid", userid_ExistingUser);
//            list1.put("activity", "activity_AYUBO");
//            list1.put("energy", sMets);
//            list1.put("steps", steps);
//            list1.put("calorie", sCals);
//            list1.put("duration", "0");
//            list1.put("distance", sDis);
//            list1.put("date", date);
//            list1.put("walk_count", steps);
//            list1.put("run_count", "11");
//            list1.put("version", versionName);
//            list1.put("osType", "android");
//            list1.put("device_modal", device_modal);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return list1;
//    }


    //==================GFIT===========================
//    private void send10Days_GFIT() {
//        //
//        newService_For10DaysGFIT task = new newService_For10DaysGFIT();
//        task.execute(new String[]{ApiClient.BASE_URL_live});
//    }

//    private class newService_For10DaysGFIT extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            newService_sendFor10DaysGFIT();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Date date = new Date();
//            long currentTime = date.getTime();
//            String str_currentTime = String.valueOf(currentTime);
//            pref.setLastDataUpdateTimestamp(str_currentTime);
//        }
//    }

//    private void newService_sendFor10DaysGFIT() {
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
//        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
//
//
//        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
//        ArrayList<String> ar = new ArrayList<String>();
//        JSONArray data = null;
//
//        //  data = collectGFITStepsDatass();
//        data = jsonStepsForWeekArray;
//
//        //  String summeryData=data.toString().replace("\"","\\\"");
//        System.out.println("========alldata==============" + data);
//
//
////        newServiceAPIFor10DaysGFIT task = new newServiceAPIFor10DaysGFIT();
////        task.execute(jsonStepsForWeekArrayForNewSave);
//
//        String jsonStr =
//                "{" +
//                        "\"userid\": \"" + userid_ExistingUser + "\"," +
//                        "\"alldata\": " + data + "," +
//                        "\"version\": \"" + versionName + "\"," +
//                        "\"osType\": \"" + "android" + "\"," +
//                        "\"device_modal\": \"" + device_modal
//                        + "\"" +
//                        "}";
//
//        nameValuePair.add(new BasicNameValuePair("method", "addBulkDailyActivitySummary"));
//        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));
//        System.out.println("======data=======" + nameValuePair.toString());
//        stepsSentToDBStatus = "11";
//
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
//        } catch (UnsupportedEncodingException e) {
//            // log exception
//            e.printStackTrace();
//        }
//        HttpResponse response = null;
//        try {
//            response = httpClient.execute(httpPost);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }

//    private class newServiceAPIFor10DaysGFIT extends AsyncTask<List<StepObj>, Void, String> {
//
//        @Override
//        protected String doInBackground(List<StepObj>... arrayLists) {
//
//            List<StepObj> stepDetail = arrayLists[0];
//            saveWalkWinDailySteps(stepDetail);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Date date = new Date();
//            long currentTime = date.getTime();
//            String str_currentTime = String.valueOf(currentTime);
//            pref.setLastDataUpdateTimestamp(str_currentTime);
//        }
//    }

//    private void saveWalkWinDailySteps(List<StepObj> obj) {
//        WalkWinApiInterface walkWinApiInterface = ApiClient.getClient().create(WalkWinApiInterface.class);
//        Call<WalkWinStepsResponse> walkWinSaveStepsRequestCall = walkWinApiInterface.saveDailySteps(AppConfig.APP_BRANDING_ID, pref.getUserToken(), obj);
//        walkWinSaveStepsRequestCall.enqueue(new retrofit2.Callback<WalkWinStepsResponse>() {
//
//            @Override
//            public void onResponse(Call<WalkWinStepsResponse> call, Response<WalkWinStepsResponse> response) {
//                if (response.isSuccessful()) {
//
//                    // successfully save
//                    System.out.println(response);
//                } else {
//                    // no result
//                    System.out.println(response);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WalkWinStepsResponse> call, Throwable t) {
////                showToast("Save daily step API failure...");
//            }
//        });
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


//    private void uploadProfileImageAsFile() {
//
//        if (Utility.isInternetAvailable(this)) {
//
//            uploadProfileImage();
//
//        } else {
//            Toast.makeText(getApplicationContext(), "No internet connection. Cannot upload profile image. ",
//                    Toast.LENGTH_LONG).show();
//            // finish();
//        }
//
//    }

//    private void uploadProfileImage() {
//        new UploadFileToServer().execute();
//    }

    public static File resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName) {
        final int MAX_IMAGE_SIZE = 200 * 200; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 100, 100);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath, options);

        Bitmap bmpPicNew = null;

        int compressQuality = 5; // quality decreasing by 5 every loop.
        int streamLength;
        do {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb");
        } while (streamLength >= MAX_IMAGE_SIZE);

        try {

            //save the resized and compressed file to disk cache
            Log.d("compressBitmap", "cacheDir: " + context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir() + fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmpPic.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sp = context.getCacheDir() + "/" + fileName;
        //return the path of resized and compressed file
        return destination;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

//    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
//        @Override
//        protected void onPreExecute() {
//
//            MyProgressLoading.showLoading(NewHomeWithSideMenuActivity.this, "Profile image uploading...");
//            newf = resizeAndCompressImageBeforeSend(NewHomeWithSideMenuActivity.this, image_absolute_path, "profile.png");
//
//            super.onPreExecute();
//        }
//
////        @Override
////        protected void onProgressUpdate(Integer... progress) {
////            // Making progress bar visible
////            //  progressBar.setVisibility(View.VISIBLE);
////
////            // updating progress bar value
////            // progressBar.setProgress(progress[0]);
////            MyProgressLoading.showLoading(NewHomeWithSideMenuActivity.this, "Profile image uploading...");
////
////            // updating percentage value
////            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
////        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            return uploadFile();
//        }
//
//        @SuppressWarnings("deprecation")
//        private String uploadFile() {
//            String responseString = null;
//
//            HttpClient httpclient = new DefaultHttpClient();
//
//            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_entypoint + "uploadUserPicture");
//            httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
//
//            try {
//                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
//                        new AndroidMultiPartEntity.ProgressListener() {
//
//                            @Override
//                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
//                            }
//                        });
//                //  MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,);
//                File sourceFile = new File(image_absolute_path);
//
//                // Adding file data to http body
//                entity.addPart("imageParamName", new FileBody(newf));
//                entity.addPart("userid", new StringBody(userid_ExistingUser));
//                entity.addPart("extention", new StringBody("png"));
//
//                totalSize = entity.getContentLength();
//                httpPost.setEntity(entity);
//
//                // Making server call
//                HttpResponse response = httpclient.execute(httpPost);
//                HttpEntity r_entity = response.getEntity();
//                r_entity.toString();
//                int statusCode = response.getStatusLine().getStatusCode();
//                if (statusCode == 200) {
//                    // Server response
//                    responseString = org.apache.http.util.EntityUtils.toString((HttpEntity) r_entity);
//                    System.out.println("============================" + responseString);
//                } else {
//                    responseString = "Error occurred! Http Status Code: "
//                            + statusCode;
//                }
//
//            } catch (ClientProtocolException e) {
//                responseString = e.toString();
//            } catch (IOException e) {
//                responseString = e.toString();
//            }
//
//            return responseString;
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // showing the server response in an alert dialog
//            //  showAlert(result);
//            super.onPostExecute(result);
//
//            MyProgressLoading.dismissDialog();
//
//            JSONObject jsonObj = null;
//            String res = null;
//            try {
//                jsonObj = new JSONObject(result);
//                res = jsonObj.optString("result").toString();
//                if (res != null) {
//                    if (res.equals("0")) {
//                        Toast.makeText(NewHomeWithSideMenuActivity.this, "Successfully uploaded", Toast.LENGTH_LONG).show();
//
//
//                    } else {
//                        System.out.println("Profile Image upload error " + result);
//                        Toast.makeText(NewHomeWithSideMenuActivity.this, "Uploading error", Toast.LENGTH_LONG).show();
//                    }
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//
//    }

//    private void requestCameraPermission() {
//
//        // BEGIN_INCLUDE(camera_permission_request)
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            // Provide an additional rationale to the user if the permission was not granted
//            // and the user would benefit from additional context for the use of the permission.
//            // For example if the user has previously denied the permission.
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_STORAGE_DEVICE);
//
//
//        } else {
//
//            // Camera permission has not been granted yet. Request it directly.
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_STORAGE_DEVICE);
//        }
//        // END_INCLUDE(camera_permission_request)
//    }

    private void selectImagePopup() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

    }

//    public int getOrientation(Context context, Uri photoUri, String path) {
//        int rotate = 0;
//        try {
//            //getContentResolver().notifyChange(photoUri, null);
//            //File imageFile = new File(photoUri.getAbsolutePath());
//            File imageFile = new File(path);
//
//            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    rotate = 270;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    rotate = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    rotate = 90;
//                    break;
//            }
//            //Log.v(Common.TAG, "Exif orientation: " + orientation);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return rotate;
//        /****** Image rotation ****/
//
//    }

    //======================================
    @SuppressWarnings("deprecation")
//    private void onSelectFromGalleryResult(Intent data) {
//        //  Ram.setHomeFragmentNumber("1");
//        Uri selectedImageUri = data.getData();
//        System.out.println("============+++++++++++++++++++++++++++++++++++++======================2=======");
//        System.out.println("==================selectedImageUri Gallery================================" + selectedImageUri.toString());
//        Cursor cursor;
//
//        String[] projection = {MediaStore.MediaColumns.DATA};
//        String selectedImagePath = null;
//
//        cursor = NewHomeWithSideMenuActivity.this.getContentResolver().query(selectedImageUri, projection, null, null, null);
//
//        if (cursor != null && cursor.getCount() > 0) {
//
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            cursor.moveToFirst();
//
//            selectedImagePath = cursor.getString(column_index);
//            image_absolute_path = selectedImagePath;
//            System.out.println("=============================" + image_absolute_path);
//
//            Bitmap bm;
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(selectedImagePath, options);
//            final int REQUIRED_SIZE = 50;
//            int scale = 1;
//            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
//                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//                scale *= 2;
//            options.inSampleSize = scale;
//            options.inJustDecodeBounds = false;
//            //bm = BitmapFactory.decodeFile(selectedImagePath, options);
//
//            //=========================
//            int photoW = options.outWidth;
//            int photoH = options.outHeight;
//            int targetW = imgProfile.getWidth();
//            int targetH = imgProfile.getHeight();
//
//            /* Figure out which way needs to be reduced less */
//            int scaleFactor = 1;
//            if ((targetW > 0) || (targetH > 0)) {
//                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//            }
//
//            /* Set bitmap options to scale the image decode target */
//            options.inJustDecodeBounds = false;
//            options.inSampleSize = scaleFactor;
//            options.inPurgeable = true;
//            File imgFile = new File(selectedImagePath);
//            /* Decode the JPEG file into a Bitmap */
//            bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
//
//            //     Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, 100, 100, true);
//
//
//            db.addProfileImageToDB(userid_ExistingUser, selectedImageUri.toString(), selectedImagePath);
//            System.out.println("=============IMAGE 0==2=======================" + selectedImageUri.toString() + "   " + selectedImagePath);
//
//            imgProfile.setImageBitmap(bm);
//
////           int orientation = getOrientation(NewHomeWithSideMenuActivity.this, selectedImageUri, selectedImagePath);
////            //=======================
////
////            if (orientation == 90) {
////                System.out.println("==========================Rotsted=========================================" + 90);
////                Matrix matrix = new Matrix();
////                matrix.postRotate(90);
////                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
////                matrix.postRotate(90);
////                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, 100, 100, true);
////                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
////                imgProfile.setImageBitmap(bm);
////            }
////            if (orientation == 270) {
////                Matrix matrix = new Matrix();
////                matrix.postRotate(270);
////                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
////                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, 100, 100, true);
////                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
////                imgProfile.setImageBitmap(bm);
////            }
////            else {
////                imgProfile.setImageBitmap(bm);
////                System.out.println("============+++++++++++++++++++++++++++++++++++++=======================E======");
////            }
//
//            uploadProfileImageAsFile();
//        } else {
//            Bitmap myBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.aaaaa);
//            Bitmap circularBitmap = getRoundedCornerBitmap(myBitmap, 150);
//            imgProfile.setImageBitmap(circularBitmap);
//        }
//        //========================
//
//    }


//    private void onCaptureImageResult(Intent data) {
//        //    Ram.setHomeFragmentNumber("1");
//        int currentAPIVersion = Build.VERSION.SDK_INT;
//        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
//
//            if (ContextCompat.checkSelfPermission(NewHomeWithSideMenuActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//
//                Uri selectedImageUri = data.getData();
//
//                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                thumbnail.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
//
//                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
//
//                db.addProfileImageToDB(userid_ExistingUser, selectedImageUri.toString(), destination.toString());
//                //   Ram.setProfileImageUri(selectedImageUri.toString());
//                System.out.println("======================destination===================================" + destination.toString());
//                FileOutputStream fo;
//                try {
//                    destination.createNewFile();
//                    fo = new FileOutputStream(destination);
//                    fo.write(bytes.toByteArray());
//                    fo.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                imgProfile.setImageBitmap(thumbnail);
//
//
//            } else if (ActivityCompat.shouldShowRequestPermissionRationale(NewHomeWithSideMenuActivity.this, Manifest.permission.CAMERA)) {
//                // Toast.makeText(getContext(), "Permission Issue 02..",Toast.LENGTH_SHORT).show();
//                // We've been denied once before. Explain why we need the permission, then ask again.
//                // requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
//
//
//            } else {
//                //  Toast.makeText(getContext(), "Permission Issue 03..",Toast.LENGTH_SHORT).show();
//                // We've never asked. Just do it.
//                // requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
//            }
//        }
//
//        else {
//            Uri selectedImageUri = data.getData();
//
//            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//            File destination = new File(Environment.getExternalStorageDirectory(),
//                    System.currentTimeMillis() + ".jpg");
//
//            db.addProfileImageToDB(userid_ExistingUser, selectedImageUri.toString(), destination.toString());
//            // Ram.setProfileImageUri(selectedImageUri.toString());
//            System.out.println("======================destination===================================" + destination.toString());
//            FileOutputStream fo;
//            try {
//                destination.createNewFile();
//                fo = new FileOutputStream(destination);
//                fo.write(bytes.toByteArray());
//                fo.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            imgProfile.setImageBitmap(thumbnail);
//        }
//
//
//    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        //final float densityMultiplier = Welcome.getContext().getResources().getDisplayMetrics().density;
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        //final float roundPx = 30;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


}
