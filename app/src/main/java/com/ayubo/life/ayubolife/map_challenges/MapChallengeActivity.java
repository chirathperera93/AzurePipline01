package com.ayubo.life.ayubolife.map_challenges;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.ContactDetailsActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.challenges.NewCHallengeActivity;
import com.ayubo.life.ayubolife.channeling.activity.DashboardActivity;
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity;
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters;
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.fragments.HomePage_Utility;
import com.ayubo.life.ayubolife.goals.AchivedGoal_Activity;
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity;
import com.ayubo.life.ayubolife.home_popup_menu.AskQuestion_Activity;
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity;
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.welcome.JanashakthiWelcomeActivity;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.ayubo.life.ayubolife.lifepoints.LifePointActivity;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.map_challenges.activity.NewLeaderBoardActivity;
import com.ayubo.life.ayubolife.map_challenges.treasureview.TreasureViewActivity;
import com.ayubo.life.ayubolife.model.CardsEntity;
import com.ayubo.life.ayubolife.model.RoadLocationObj;
import com.ayubo.life.ayubolife.notification.activity.SingleTimeline_Activity;
import com.ayubo.life.ayubolife.post.activity.NativePostActivity;
import com.ayubo.life.ayubolife.post.activity.NativePostJSONActivity;
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity;
import com.ayubo.life.ayubolife.prochat.data.model.Conversation;
import com.ayubo.life.ayubolife.programs.ProgramActivity;
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity;
import com.ayubo.life.ayubolife.timeline.OpenPostActivity;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.ayubo.life.ayubolife.payment.ConstantsKt.EXTRA_TREASURE_KEY;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MapChallengeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    com.google.android.gms.maps.MapView mapView;
    GoogleMap mGoogleMap;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 432;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION_MANAGER = 433;
    List<LatLng> points = null;
    List<LatLng> points2 = null;
    private Polyline mMutablePolyline;
    Marker gifMarker;
    Marker gifMarker2;
    ArrayList<RoadLocationObj> myTagList = null;
    ArrayList<RoadLocationObj> whitePath_myRoadLocationsList = null;
    ArrayList<RoadLocationObj> pendingRoadLocationsList = null;
    LatLng sydney = null;
    ApiInterface retrofitApiService;
    //  ArrayList<DB4String> datesListArray2=null;
    private Timer timerForGifDisplay, timerForGifDisplay2, timerForAnimation;
    ImageLoader imageLoader;
    //  int steps;
    public static final int PATTERN_DASH_LENGTH_PX = 20;
    public static final int PATTERN_GAP_LENGTH_PX = 20;
    public static final PatternItem DOT = new Dot();
    public static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    public static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    public static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DOT);
    private PrefManager pref;
    double current_lat, current_longi, current_distance;
    Context mContext;
    boolean isFirst = false;
    Marker mapMarker;
    String sss;
    RadioButton btn_appointment_left, btn_appointment_right;
    ProgressDialog prgDialog;
    String userid_ExistingUser, cityJsonString, setDeviceID_Success, today, serviceDataStatus, tip_icon, tip, tipheading;
    public static String chID;
    int total_steps;
    String strstep, noof_day, dsteps, weekSteps;
    ArrayList<RoadLocationObj> myTreasureList = null;

    int squareBitmapWidth;
    String nestCity, nestCitySteps;
    final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    boolean isPermissionGranted = false;
    String[] permissionsRequired = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    boolean isFirstLocation = false;
    boolean isFirstOffLocation = false;
    boolean isStartingLocation = false;
    String BaseUrl_Profile = null;
    String BaseUrl_Tresure = null;
    String BaseUrl_Advertiesments = null;
    ImageButton btn_leave_challenge;
    String zooml;
    String sDen = "";
    private SlidingUpPanelLayout mLayout;
    private static final String TAG = "DemoActivity";
    ListView CustomMapDetaisl_lv;
    CustomAdapterFresh customAdapter;
    ListViewItem[] items;
    String cards;
    ArrayList<CardsEntity> mapCardsList = null;
    LinearLayout dragView, top_bgnew;
    Timer timer_foutTimePerDay_Go = new Timer();
    TimerTask tTask;
    Timer timer_foutTimePerDay_Go2 = new Timer();
    TimerTask tTask2;
    private Handler handler = new Handler();
    private Handler handler2 = new Handler();
    LinearLayout top_bg;
    ImageView img_btn_down_arrow;
    private GestureDetector mDetector;
    String treatures, share_cityName, share_completed_steps, share_noofDays, share_image, status, showpopup;
    LinearLayout layout_popup_advers, layout_daily_tip;
    Book book;
    ArrayList<String> activeArray = null;
    ArrayList<RoadLocationObj> myTreasure = null;

    ProgressDialog proDialog;

    private void loadImage(String imageUrl, final com.android.volley.toolbox.NetworkImageView imageView, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        if (imageUrl != null) {
            imageLoader.get(imageUrl, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setImageBitmap(response.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    //	Log.e("onErrorResponse ", error.toString());
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    String service_checkpoints, enabled_checkpoints, white_lines;
    boolean gyroExists;
    String tip_header_1, tip_header_2, tip_type, tip_meta, objimg;
    Handler mHandler = new Handler();

    String getJSONTreasures(ArrayList<RoadLocationObj> myDataLists) {
        JSONArray ja = new JSONArray();
        String jsonString = null;
        for (int i = 0; i < myDataLists.size(); i++) {

            RoadLocationObj obj = myDataLists.get(i);

            JSONObject jo = new JSONObject();
            try {
                jo.put("latp", obj.getLat());
                jo.put("longp", obj.getLongitude());
                jo.put("objlink", obj.getMeta());
                jo.put("meta", obj.getMeta());
                jo.put("action", obj.getAction());
                jo.put("steps", obj.getDistance());
                jo.put("objimg", obj.getSteps());
                jo.put("status", obj.getFlag_act());

                jo.put("bubble_txt", obj.getBubble_txt());
                jo.put("bubble_link", obj.getBubble_link());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ja.put(jo);
        }
        return ja.toString();


    }

    ArrayList<RoadLocationObj> getTreasures(String data) {

        ArrayList<RoadLocationObj> myTreasur = null;
        myTreasur = new ArrayList<RoadLocationObj>();
        JSONArray myDataListsAll = null;
        try {
            myDataListsAll = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < myDataListsAll.length(); i++) {

            JSONObject childJson = null;
            try {
                childJson = (JSONObject) myDataListsAll.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String latp = childJson.optString("latp");
            String longp = childJson.optString("longp");
            String steps = childJson.optString("steps");
            String objimg = childJson.optString("objimg");
            String objlink = childJson.optString("objlink");
            String status = childJson.optString("status");
            String auto_hide = childJson.optString("auto_hide");

            String action = childJson.optString("action");
            String meta = childJson.optString("meta");

            double roadPath_lat = Double.parseDouble(latp);
            double roadPath_longitude = Double.parseDouble(longp);

            String bubble_text = null;
            String bubble_link = null;
            if (childJson.has("bubble_txt")) {
                try {
                    bubble_text = childJson.getString("bubble_txt");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                bubble_text = "";
            }
            if (childJson.has("bubble_link")) {
                try {
                    bubble_link = childJson.getString("bubble_link");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                bubble_link = "";
            }


            myTreasur.add(new RoadLocationObj(roadPath_lat, roadPath_longitude, steps, objimg, action, meta, meta, "", "", "",
                    "", "", "", "", "", "", bubble_text, bubble_link, auto_hide));

        }

        return myTreasur;

    }


    @Override
    public void onResume() {
        super.onResume();

        this.mapView.onResume();

        if (pref.isNeedMapReload()) {

            chID = getIntent().getStringExtra("challenge_id");
            pref.setChallangeID(chID);

            NService_getStepsDataAtResume task = new NService_getStepsDataAtResume();
            task.execute(new String[]{ApiClient.BASE_URL_live_v6});

        }


        if (mGoogleMap != null) {
            //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            if (pref.isNeedMapReload()) {
                mGoogleMap.clear();

                drawMapPath(mGoogleMap);
            } else {

                if (mGoogleMap != null) {
                    //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                    drawMapPath(mGoogleMap);
                    // add markers from database to the map
                }
            }

            // add markers from database to the map
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_challenge);

        mContext = MapChallengeActivity.this;
        points = new ArrayList<LatLng>();
        points2 = new ArrayList<LatLng>();
        myTreasureList = new ArrayList<RoadLocationObj>();
        mapCardsList = new ArrayList<CardsEntity>();
        proDialog = new ProgressDialog(MapChallengeActivity.this);
        proDialog.setCancelable(false);
        proDialog.setMessage("Loading...");
        PackageManager packageManager = getPackageManager();
        gyroExists = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);

        pref = new PrefManager(this);

        chID = getIntent().getStringExtra("challenge_id");
        pref.setChallangeID(chID);
        NService_getChallengeMapData_ServiceCall();

        //  FirebaseAnalytics Adding
        Bundle bChallenge_openedParams = new Bundle();
        bChallenge_openedParams.putString("challenge", chID);
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Challenge_opened", bChallenge_openedParams);
        }

        layout_daily_tip = (LinearLayout) findViewById(R.id.layout_daily_tip);
        layout_daily_tip.setVisibility(View.GONE);

        layout_popup_advers = (LinearLayout) findViewById(R.id.layout_popup_advers);
        layout_popup_advers.setVisibility(View.GONE);


        //=======   FINISED   CARD   VIEW    ================================================
        //=======   FINISED   CARD   VIEW    ================================================
        //=======   FINISED   CARD   VIEW    ================================================
        //=======   FINISED   CARD   VIEW    ================================================
        try {
            float density = Resources.getSystem().getDisplayMetrics().density * 160f;
            int den3 = (int) Math.round(density);
            // int den3 =0;

            if (den3 <= 120) {
                // 36x36 (0.75x) for low-density
                squareBitmapWidth = 40;
                sDen = "low";
                BaseUrl_Profile = MAIN_URL_LIVE_HAPPY + "ldpi/";
                BaseUrl_Tresure = MAIN_URL_LIVE_HAPPY + "ldpi/";
                BaseUrl_Advertiesments = MAIN_URL_LIVE_HAPPY + "ldpi/";
            } else if ((den3 < 120) && (den3 >= 160)) {
                // 48x48 (1.0x baseline) for medium-density
                squareBitmapWidth = 50;
                sDen = "medium";
                BaseUrl_Profile = MAIN_URL_LIVE_HAPPY + "mdpi/";
                BaseUrl_Tresure = MAIN_URL_LIVE_HAPPY + "mdpi/";
                BaseUrl_Advertiesments = MAIN_URL_LIVE_HAPPY + "mdpi/";
            } else if ((den3 > 160) && (den3 <= 240)) {
                // 72x72 (1.5x) for high-density
                squareBitmapWidth = 60;
                sDen = "high";
                BaseUrl_Profile = MAIN_URL_LIVE_HAPPY + "hdpi/";
                BaseUrl_Tresure = MAIN_URL_LIVE_HAPPY + "hdpi/";
                BaseUrl_Advertiesments = MAIN_URL_LIVE_HAPPY + "hdpi/";
            } else if ((den3 > 240) && (den3 <= 320)) {
                //96x96 (2.0x) for extra-high-density
                squareBitmapWidth = 80;
                sDen = "xdpi";
                BaseUrl_Profile = MAIN_URL_LIVE_HAPPY + "xdpi/";
                BaseUrl_Tresure = MAIN_URL_LIVE_HAPPY + "xdpi/";
                BaseUrl_Advertiesments = MAIN_URL_LIVE_HAPPY + "xdpi/";
            } else if ((den3 > 320) && (den3 <= 480)) {
                squareBitmapWidth = 120;
                sDen = "xxdpi";    // 96x96 (2.0x) for extra-high-density

                BaseUrl_Profile = MAIN_URL_LIVE_HAPPY + "hdpi/";
                BaseUrl_Tresure = MAIN_URL_LIVE_HAPPY + "hdpi/";
                BaseUrl_Advertiesments = MAIN_URL_LIVE_HAPPY + "hdpi/";
            } else if ((den3 > 480) && (den3 <= 600)) {
                squareBitmapWidth = 150;
                sDen = "xxxdpi";    //144x144 (3.0x) for extra-extra-high-density

                BaseUrl_Profile = MAIN_URL_LIVE_HAPPY + "hdpi/";
                BaseUrl_Tresure = MAIN_URL_LIVE_HAPPY + "hdpi/";
                BaseUrl_Advertiesments = MAIN_URL_LIVE_HAPPY + "hdpi/";
            } else if (den3 > 600) {
                squareBitmapWidth = 180;
                sDen = "xxxxx";    // 192x192 (4.0x) for extra-extra-extra-high-density

                BaseUrl_Profile = MAIN_URL_LIVE_HAPPY + "hdpi/";
                BaseUrl_Tresure = MAIN_URL_LIVE_HAPPY + "hdpi/";
                BaseUrl_Advertiesments = MAIN_URL_LIVE_HAPPY + "hdpi/";
            } else {
                squareBitmapWidth = 120;
                sDen = "medium";
            }

            sDen = Utility.getDeviceDensityName();
            if (book != null) {
                myTreasureList = book.myTagList;
            }

            System.out.println("======density====================" + sDen);
            if (strstep != null) {
                total_steps = Integer.parseInt(strstep);
                total_steps = 500;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


        try {
            checkFullPermission();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            pref = new PrefManager(this);
            userid_ExistingUser = pref.getLoginUser().get("uid");

            prgDialog = new ProgressDialog(MapChallengeActivity.this);
            btn_leave_challenge = (ImageButton) findViewById(R.id.btn_leave_challenge);
            top_bg = (LinearLayout) findViewById(R.id.top_bg);

            mapView = (com.google.android.gms.maps.MapView) findViewById(R.id.mapView);
            mapView.getMapAsync(this);
            mapView.onCreate(savedInstanceState);
            if (mapView != null) {
                mapView.getMapAsync(this);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window w = getWindow(); // in Activity's onCreate() for instance
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

//        try {
//            getLocationJson();
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }


        btn_appointment_left = findViewById(R.id.btn_appointment_left);

        btn_leave_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid_ExistingUser = pref.getLoginUser().get("uid");
                Intent intent = new Intent(MapChallengeActivity.this, QuitChallangeActivity.class);
                intent.putExtra("user", userid_ExistingUser);
                intent.putExtra("challenge_id", pref.getChallangeID());
                startActivity(intent);
            }
        });

        btn_appointment_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        btn_appointment_right = findViewById(R.id.btn_appointment_right);
        btn_appointment_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book(myTagList);
                Intent intent = new Intent(MapChallengeActivity.this, Journal_Activity.class);
                intent.putExtra("Book", book);
                intent.putExtra("total_steps", strstep);
                intent.putExtra("challenge_id", pref.getChallangeID());
                intent.putExtra("service_checkpoints", service_checkpoints);
                intent.putExtra("enabled_checkpoints", enabled_checkpoints);

                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        System.out.println("=============onTouchEvent=========");

        return false; // here it returns false so that another event's listener
        // should be called, in your case the MapFragment
        // listener
    }


    public class ListViewItem {
        private String text;
        private int type;

        public ListViewItem(String text, int type) {
            this.text = text;
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }

    public class CustomAdapterFresh extends ArrayAdapter<ListViewItem> {


        public static final int TYPE_LOCATION = 0;
        public static final int TYPE_BANNER = 1;
        public static final int TYPE_ADD_BOX = 2;
        public static final int TYPE_NEXT_BOX = 3;
        public static final int TYPE_PLAIN_BANNER = 4;
        public static final int TYPE_WORKOUT_SHARE = 5;
        public static final int TYPE_MAIN = 6;
        public static final int TYPE_WAITING_TO_START = 7;
        public static final int TYPE_COMPLETE = 8;
        public static final int TYPE_IN_COMPLETE = 9;
        public static final int TYPE_STATISTICS = 10;
        public static final int TYPE_HTML_VIEW = 11;
        public static final int TYPE_MAIN_CARD_TWO = 12;
        public static final int TYPE_LEADERBOARD = 13;
        private ListViewItem[] objects;

        @Override
        public int getViewTypeCount() {
            return 14;
        }

        //    Are you using getViewTypeCount() and getItemViewType() and returned an integer
        //   from getItemViewType equal or larger than you returned from getViewTypeCount() (or perhaps a negative number)?
//        //
        @Override
        public int getItemViewType(int position) {

            CardsEntity obj = mapCardsList.get(position);
            String typ = obj.getType();
            switch (typ) {
                case "location":
                    return 0;
                case "banner":
                    return 1;
                case "add_box":
                    return 2;
                case "next_box":
                    return 3;
                case "plain_banner":
                    return 4;
                case "share_box":
                    return 5;
                case "main_card":
                    return 6;
                case "waiting_to_start":
                    return 7;
                case "complete":
                    return 8;
                case "incomplete":
                    return 9;
                case "statistics":
                    return 10;
                case "html_card":
                    return 11;
                case "main_card_2":
                    return 12;
                case "native_leaderboard":
                    return 13;
                default:
                    return -1;
            }
        }

//        @Override
//        public int getItemViewType(int position) {
//             try {
//                 return position;
//             }catch(Exception e){
//                 e.printStackTrace();
//                 return 6;
//             }
////
//       }

        public CustomAdapterFresh(Context context, int resource, ListViewItem[] objects) {
            super(context, resource, objects);
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final CardsEntity obj = mapCardsList.get(position);
            String typ = obj.getType();
            // System.out.println("=======typ===================="+typ);
            int viewPos = 0;
            if (typ.equals("location")) {
                viewPos = 0;
            } else if (typ.equals("banner")) {
                viewPos = 1;
            } else if (typ.equals("add_box")) {
                viewPos = 2;
            } else if (typ.equals("next_box")) {
                viewPos = 3;
            } else if (typ.equals("plain_banner")) {
                viewPos = 4;
            } else if (typ.equals("share_box")) {
                viewPos = 5;
            } else if (typ.equals("main_card")) {
                viewPos = 6;
            } else if (typ.equals("waiting_to_start")) {
                viewPos = 7;
            } else if (typ.equals("complete")) {
                viewPos = 8;
            } else if (typ.equals("incomplete")) {
                viewPos = 9;
            } else if (typ.equals("statistics")) {
                viewPos = 10;
            } else if (typ.equals("html_card")) {
                viewPos = 11;
            } else if (typ.equals("main_card_2")) {
                viewPos = 12;
            }


            int listViewItemType = getItemViewType(position);
            // System.out.println("============viewPos==============================="+Integer.toString(listViewItemType));
            View viewHolder = convertView;

            if (viewHolder == null) {
                if (listViewItemType == TYPE_LEADERBOARD) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_leaderboard_layout, null);
                }
                if (listViewItemType == TYPE_MAIN) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem2_weather_layout, null);
                } else if (listViewItemType == TYPE_MAIN_CARD_TWO) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem12_current_location_new, null);
                } else if (listViewItemType == TYPE_WAITING_TO_START) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem7_waiting_to_start, null);
                } else if (listViewItemType == TYPE_COMPLETE) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem_challange_complete, null);
                } else if (listViewItemType == TYPE_IN_COMPLETE) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem9_challange_incomplete, null);
                } else if (listViewItemType == TYPE_STATISTICS) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem10_challange_statistics, null);
                } else if (listViewItemType == TYPE_LOCATION) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem5_location_layout, null);
                } else if (listViewItemType == TYPE_BANNER) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem4_banner_layout, null);
                } else if (listViewItemType == TYPE_NEXT_BOX) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem8_next_layout, null);
                } else if (listViewItemType == TYPE_ADD_BOX) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem6_addbox_layout, null);
                } else if (listViewItemType == TYPE_PLAIN_BANNER) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem7_plain_layout, null);
                } else if (listViewItemType == TYPE_WORKOUT_SHARE) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem1_share_layout, null);
                } else if (listViewItemType == TYPE_HTML_VIEW) {
                    viewHolder = LayoutInflater.from(getContext()).inflate(R.layout.map_dragable_tem11_html_view, null);
                }

            } else {
                // viewHolder = (ViewHolder) convertView.getTag();
            }

            if (listViewItemType == 0) {

                ImageView img_location_icon = (ImageView) viewHolder.findViewById(R.id.img_location_icon);
                TextView txt_steps = (TextView) viewHolder.findViewById(R.id.txt_steps_inlocation);
                TextView txt_current_locationcity = (TextView) viewHolder.findViewById(R.id.txt_current_locationcity);

                String image = obj.getImage();
                if (image.contains("http")) {
                    image = obj.getImage();
                } else {
                    image = ApiClient.BASE_URL + obj.getImage();
                }
                System.out.println("=========image==============" + image);

                int imageSize = Utility.getImageSizeFor_DeviceDensitySize(130);

                RequestOptions requestOptions1 = new RequestOptions()
                        .centerCrop()
                        .override(imageSize, imageSize)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(MapChallengeActivity.this).load(image)
                        .transition(withCrossFade())
                        .into(img_location_icon);

                txt_steps.setText("45");
                txt_current_locationcity.setText("Kelaniya");


            } else if (listViewItemType == TYPE_LEADERBOARD) {

                TextView txt_heading = (TextView) viewHolder.findViewById(R.id.txt_heading);
                TextView txt_description = (TextView) viewHolder.findViewById(R.id.txt_description);
                RelativeLayout main_bg = (RelativeLayout) viewHolder.findViewById(R.id.main_view_bg);

                txt_heading.setText(obj.getTitle());
                txt_description.setText(obj.getSubtitle());
                String image_Url = ApiClient.BASE_URL + "custom/include/images/challenge_images/leaderboard.png";
                main_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        processAction(obj.getAction(), obj.getMeta());

                    }
                });

                //   String image_Url=obj.getImage();
//                if(image_Url.contains("http")){
//                    image_Url=obj.getImage();
//                }else{
//                    image_Url=ApiClient.BASE_URL+obj.getImage();
//                }
                ProgressBar progressNewsList = (ProgressBar) viewHolder.findViewById(R.id.progressNewsList);
                NetworkImageView bg_image = (NetworkImageView) viewHolder.findViewById(R.id.main_bg_banner);

                if (imageLoader == null)
                    imageLoader = App.getInstance().getImageLoader();

                if (image_Url.length() > 10) {
                    bg_image.setImageUrl(image_Url, imageLoader);
                    loadImage(image_Url, bg_image, progressNewsList);
                }

                txt_heading.setText(obj.getHeading());
                txt_description.setText(obj.getText());

            } else if (listViewItemType == TYPE_MAIN) {
                //  TYPE_MAIN
                LinearLayout main_layout = (LinearLayout) viewHolder.findViewById(R.id.main_layout);

                TextView cityname_text = (TextView) viewHolder.findViewById(R.id.header_text);
                TextView txt_temperature = (TextView) viewHolder.findViewById(R.id.txt_value);
                TextView txt_description = (TextView) viewHolder.findViewById(R.id.txt_description);
                ImageView img_weather_icon = (ImageView) viewHolder.findViewById(R.id.img_weather_icon);

                TextView txt_completed_steps = (TextView) viewHolder.findViewById(R.id.txt_completed_steps);
                TextView txt_completed_inhowmany_days = (TextView) viewHolder.findViewById(R.id.txt_completed_inhowmany_days);
//                TextView txt_completed_inper_day = (TextView) viewHolder.findViewById(R.id.txt_completed_inper_day);


                TextView txt_waiting_days_in_com = (TextView) viewHolder.findViewById(R.id.txt_waiting_days_in_com);
                TextView txt_waiting_days_days_com = (TextView) viewHolder.findViewById(R.id.txt_waiting_days_days_com);

                TextView txt_remaining_steps = (TextView) viewHolder.findViewById(R.id.txt_remaining_steps);
                TextView txt_remaining_inhowmany_days = (TextView) viewHolder.findViewById(R.id.txt_remaining_inhowmany_days);
//                TextView txt_remaining_inper_day = (TextView) viewHolder.findViewById(R.id.txt_remaining_inper_day);

                main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("=============onClick=====7==============");
                        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        }
                    }
                });

                String path = obj.getImage();
                if (path.length() > 10) {
                    Picasso
                            .with(MapChallengeActivity.this)
                            .load(path)
                            .fit() // will explain later
                            .into(img_weather_icon);
                }


                String completedArray = obj.getMax_val();
                String remainingArray = obj.getMin_val();

                String cityName = obj.getHeading2();
                String desc = obj.getDesc();
                String value = obj.getValue();


                cityname_text.setText(cityName);

                if (value.equals("-")) {
                    txt_temperature.setText("");
                } else {
                    txt_temperature.setText((value) + " \u2103");
                }


                txt_description.setText(desc);
                //=============================================
                String[] completed_output = completedArray.split("_");
                int size = completed_output.length;

                String completed_steps = "";
                String completed_inhowmany_days = "";
                String completed_inper_day = "";

                if (size == 3) {
                    completed_steps = completed_output[0];
                    completed_inhowmany_days = completed_output[1];
                    completed_inper_day = completed_output[2];
                }

                txt_completed_steps.setText(completed_steps);
                //  txt_completed_inhowmany_days.setText(completed_inhowmany_days);
//                txt_completed_inper_day.setText(completed_inper_day);

                int intnoofdaysc = Integer.parseInt(completed_inhowmany_days);
                String daystext = "";
                if (intnoofdaysc == 0) {
                    txt_waiting_days_in_com.setText("In ");
                    txt_waiting_days_days_com.setText(" Days");
                    txt_completed_inhowmany_days.setText("0");
                }
                //txt_completed_inhowmany_days.setText(completed_inhowmany_days);
                else if (intnoofdaysc == 1) {
                    daystext = " Day";
                    txt_waiting_days_days_com.setText(daystext);
                    txt_completed_inhowmany_days.setText(completed_inhowmany_days);
                    txt_waiting_days_in_com.setText("In ");

                } else {
                    daystext = " Days";
                    txt_waiting_days_days_com.setText(daystext);
                    txt_completed_inhowmany_days.setText(completed_inhowmany_days);
                    txt_waiting_days_in_com.setText("In ");
                }
                //Remaining Values================================

                //=============================================
                String[] remaining_output = remainingArray.split("_");
                int size2 = remaining_output.length;

                String remaining_steps = "";
                String remaining_inhowmany_days = "";
                String remaining_inper_day = "";

                if (size2 == 3) {
                    remaining_steps = remaining_output[0];
                    remaining_inhowmany_days = remaining_output[1];
                    remaining_inper_day = remaining_output[2];
                }
                txt_remaining_steps.setText(remaining_steps);

                TextView txt_waiting_days_in = (TextView) viewHolder.findViewById(R.id.txt_waiting_days_in);
                TextView txt_waiting_days_text = (TextView) viewHolder.findViewById(R.id.txt_waiting_days_text);

//                txt_remaining_inper_day.setText(remaining_inper_day);

                int intnoofdays = Integer.parseInt(remaining_inhowmany_days);

                String daystextr = "";
                if (intnoofdays == 0) {
//                    txt_waiting_days_in.setText("In ");
//                    txt_waiting_days_text.setText(" Days");
//                    txt_remaining_inhowmany_days.setText("0");
                    txt_waiting_days_in.setText("");
                    txt_waiting_days_text.setText("");
                    txt_remaining_inhowmany_days.setText("Today");

                } else if (intnoofdays == 1) {
//                    daystextr = " Day";
//                    txt_waiting_days_text.setText(daystextr);
//                    txt_remaining_inhowmany_days.setText(remaining_inhowmany_days);
//                    txt_waiting_days_in.setText("In ");
                    txt_waiting_days_in.setText("");
                    txt_waiting_days_text.setText("");
                    txt_remaining_inhowmany_days.setText("Tomorrow");

                } else if (remaining_inhowmany_days.equals("0")) {
                    txt_waiting_days_text.setText("");
                    txt_remaining_inhowmany_days.setText("--");
                    txt_waiting_days_in.setText("");
//                    txt_remaining_inper_day.setText("");
                } else {
                    daystextr = " Days";
                    txt_waiting_days_text.setText(daystextr);
                    txt_remaining_inhowmany_days.setText(remaining_inhowmany_days);
                    txt_waiting_days_in.setText("In ");
                }


                //  txt_remaining_inhowmany_days.setText(remaining_inhowmany_days);

            } else if (listViewItemType == TYPE_MAIN_CARD_TWO) {
                //  TYPE_MAIN
                LinearLayout main_layout = (LinearLayout) viewHolder.findViewById(R.id.main_layout);

                TextView txt_week_details = (TextView) viewHolder.findViewById(R.id.txt_week_details);
                TextView cityname_text = (TextView) viewHolder.findViewById(R.id.header_text);
                TextView txt_temperature = (TextView) viewHolder.findViewById(R.id.txt_value);
                TextView txt_description = (TextView) viewHolder.findViewById(R.id.txt_description);
                ImageView img_weather_icon = (ImageView) viewHolder.findViewById(R.id.img_weather_icon);

                TextView txt_completed_steps = (TextView) viewHolder.findViewById(R.id.txt_completed_steps);
                TextView txt_completed_inhowmany_days = (TextView) viewHolder.findViewById(R.id.txt_completed_inhowmany_days);
//                TextView txt_completed_inper_day = (TextView) viewHolder.findViewById(R.id.txt_completed_inper_day);


                TextView txt_waiting_days_in_com = (TextView) viewHolder.findViewById(R.id.txt_waiting_days_in_com);
                TextView txt_waiting_days_days_com = (TextView) viewHolder.findViewById(R.id.txt_waiting_days_days_com);

                TextView txt_remaining_steps = (TextView) viewHolder.findViewById(R.id.txt_remaining_steps);
                TextView txt_remaining_inhowmany_days = (TextView) viewHolder.findViewById(R.id.txt_remaining_inhowmany_days);
//                TextView txt_remaining_inper_day = (TextView) viewHolder.findViewById(R.id.txt_remaining_inper_day);

                main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("=============onClick=====7==============");
                        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        }
                    }
                });

                String path = obj.getImage();
                if (path.length() > 10) {
                    Picasso
                            .with(MapChallengeActivity.this)
                            .load(path)
                            .fit() // will explain later
                            .into(img_weather_icon);
                }


                String completedArray = obj.getMax_val();
                String remainingArray = obj.getMin_val();

                String cityName = obj.getHeading2();
                String desc = obj.getDesc();
                String value = obj.getValue();

                txt_week_details.setText(obj.getHeading().toString());

                cityname_text.setText(cityName);

                if (value.equals("-")) {
                    txt_temperature.setText("");
                } else {
                    txt_temperature.setText((value) + " \u2103");
                }


                txt_description.setText(desc);
                //=============================================
                String[] completed_output = completedArray.split("_");
                int size = completed_output.length;

                String completed_steps = "";
                String completed_inhowmany_days = "";
                String completed_inper_day = "";

                if (size == 3) {
                    completed_steps = completed_output[0];
                    completed_inhowmany_days = completed_output[1];
                    completed_inper_day = completed_output[2];
                }

                txt_completed_steps.setText(completed_steps);
                //  txt_completed_inhowmany_days.setText(completed_inhowmany_days);
//                txt_completed_inper_day.setText(completed_inper_day);

                int intnoofdaysc = Integer.parseInt(completed_inhowmany_days);
                String daystext = "";
                if (intnoofdaysc == 0) {
                    txt_waiting_days_in_com.setText("In ");
                    txt_waiting_days_days_com.setText(" Days");
                    txt_completed_inhowmany_days.setText("0");
                }
                //txt_completed_inhowmany_days.setText(completed_inhowmany_days);
                else if (intnoofdaysc == 1) {
                    daystext = " Day";
                    txt_waiting_days_days_com.setText(daystext);
                    txt_completed_inhowmany_days.setText(completed_inhowmany_days);
                    txt_waiting_days_in_com.setText("In ");

                } else {
                    daystext = " Days";
                    txt_waiting_days_days_com.setText(daystext);
                    txt_completed_inhowmany_days.setText(completed_inhowmany_days);
                    txt_waiting_days_in_com.setText("In ");
                }

                String[] remaining_output = remainingArray.split("_");
                int size2 = remaining_output.length;

                String remaining_steps = "";
                String remaining_inhowmany_days = "";
                String remaining_inper_day = "";

                if (size2 == 3) {
                    remaining_steps = remaining_output[0];
                    remaining_inhowmany_days = remaining_output[1];
                    remaining_inper_day = remaining_output[2];
                }
                txt_remaining_steps.setText(remaining_steps);

                TextView txt_waiting_days_in = (TextView) viewHolder.findViewById(R.id.txt_waiting_days_in);
                TextView txt_waiting_days_text = (TextView) viewHolder.findViewById(R.id.txt_waiting_days_text);

//                txt_remaining_inper_day.setText(remaining_inper_day);

                int intnoofdays = Integer.parseInt(remaining_inhowmany_days);
                String daystextr = "";
                if (intnoofdays == 0) {
                    txt_waiting_days_in.setText("In ");
                    txt_waiting_days_text.setText(" Days");
                    txt_remaining_inhowmany_days.setText("0");
                } else if (intnoofdays == 1) {
                    daystextr = " Day";
                    txt_waiting_days_text.setText(daystextr);
                    txt_remaining_inhowmany_days.setText(remaining_inhowmany_days);
                    txt_waiting_days_in.setText("In ");
                } else if (remaining_inhowmany_days.equals("0")) {
                    txt_waiting_days_text.setText("");
                    txt_remaining_inhowmany_days.setText("--");
                    txt_waiting_days_in.setText("");
//                    txt_remaining_inper_day.setText("");
                } else {
                    daystextr = " Days";
                    txt_waiting_days_text.setText(daystextr);
                    txt_remaining_inhowmany_days.setText(remaining_inhowmany_days);
                    txt_waiting_days_in.setText("In ");
                }


                //  txt_remaining_inhowmany_days.setText(remaining_inhowmany_days);

            } else if (listViewItemType == TYPE_WAITING_TO_START) {
                //======TYPE_WAITING_TO_START===============

                TextView txt_waiting_days_in = (TextView) viewHolder.findViewById(R.id.txt_waiting_days_in);
                TextView txt_waiting_days = (TextView) viewHolder.findViewById(R.id.txt_waiting_days);
                TextView txt_waiting_days_text = (TextView) viewHolder.findViewById(R.id.txt_waiting_days_text);

                String noofdays = obj.getValue();
                int intnoofdays = Integer.parseInt(noofdays);
                String daystext = "";
                if (intnoofdays == 0) {
                    txt_waiting_days_in.setText("");
                    txt_waiting_days_text.setText("");
                    txt_waiting_days.setText("");
                } else if (intnoofdays == 1) {
                    daystext = "Day";
                    txt_waiting_days_in.setText("Tomorrow");
                    txt_waiting_days_in.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                    txt_waiting_days_in.setTextColor(Color.parseColor("#000000"));
                    txt_waiting_days_in.setTypeface(Typeface.DEFAULT_BOLD);
                    txt_waiting_days_text.setText("");
                    txt_waiting_days.setText("");
                } else {
                    txt_waiting_days_in.setText("In");
                    txt_waiting_days.setText(obj.getValue());
                    txt_waiting_days_text.setText("Days");
                }

            } else if (listViewItemType == TYPE_COMPLETE) {
                //======TYPE_COMPLETE_TO_START===============
                // GifDrawable gifFromResource = new GifDrawable(mContext.getResources(), R.drawable.original );
                TextView txt_reached_city = (TextView) viewHolder.findViewById(R.id.txt_reached_city);
                TextView txt_complete_heading = (TextView) viewHolder.findViewById(R.id.txt_complete_heading);


                TextView txt_completed_days = (TextView) viewHolder.findViewById(R.id.txt_completed_days);
                txt_completed_days.setText(obj.getValue());
                txt_reached_city.setText(obj.getText());

                TextView txt_comp_days_text = (TextView) viewHolder.findViewById(R.id.txt_comp_days_text);
                txt_complete_heading.setText(obj.getHeading());

                String noofdays = obj.getValue();
                int intnoofdays = Integer.parseInt(noofdays);
                String daystext = " Day";
                if (intnoofdays == 1) {
                    daystext = " Day";
                } else {
                    daystext = " Days";
                }
                txt_comp_days_text.setText(daystext);

            } else if (listViewItemType == TYPE_IN_COMPLETE) {
                //======TYPE_IN_COMPLETE==============
            } else if (listViewItemType == TYPE_STATISTICS) {

                //======TYPE_STATISTICS=============== 3000/Day
                TextView txt_avg_steps_sta = (TextView) viewHolder.findViewById(R.id.txt_avg_steps_sta);
                TextView txt_no_of_tresures = (TextView) viewHolder.findViewById(R.id.txt_no_of_tresures);
                TextView txt_raffle_draw_entries = (TextView) viewHolder.findViewById(R.id.txt_raffle_draw_entries);

                LinearLayout statis_main_view = (LinearLayout) viewHolder.findViewById(R.id.statis_main_view);

                String steps_with_comma = null;
                if (isStringInt(obj.getValue())) {
                    int steps = Integer.parseInt(obj.getValue());
                    steps_with_comma = NumberFormat.getIntegerInstance().format(steps);
                    txt_avg_steps_sta.setText(steps_with_comma);
                } else {
                    txt_avg_steps_sta.setText("0");
                }

                txt_no_of_tresures.setText(obj.getMin_val());
                txt_raffle_draw_entries.setText(obj.getMax_val());

                final String urls = obj.getLink();

                statis_main_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("=============onClick=====10==============" + urls);
                        if (urls.length() > 10) {
                            if (urls.contains("openbadgenative")) {
                                Intent intent = new Intent(getContext(), Badges_Activity.class);
                                startActivity(intent);
                            } else if (urls.contains("group_adventure_invite")) {
                                Intent intent = new Intent(getContext(), ContactDetailsActivity.class);
                                intent.putExtra("URL", urls);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", urls);
                                startActivity(intent);
                            }
                        }
                    }
                });

            } else if (listViewItemType == TYPE_HTML_VIEW) {

                String html = obj.getText();
                final String webLink = obj.getLink();
                WebView webView = (WebView) viewHolder.findViewById(R.id.webView_challange_htmlview);

                WebSettings webSettings = webView.getSettings();
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);
                webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webView.setLongClickable(true);
                webView.loadData(html, "text/html; charset=utf-8", "UTF-8");
//                webView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//
//                        Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
//                        intent.putExtra("URL", webLink);
//                        startActivity(intent);
//
//                        System.out.println("==========webView===onClick================");
////                        Intent intent = new Intent(getContext(), SimpleWebViewActivity.class);
////                        intent.putExtra("URL", webLink);
////                        startActivity(intent);
//                       /// finish();
//                        return true;
//                    }
//                });

            } else if (listViewItemType == TYPE_BANNER) {
                //Banner
                // TextView txt_heading = (TextView) viewHolder.findViewById(R.id.txt_heading);
                // TextView txt_description = (TextView) viewHolder.findViewById(R.id.txt_description);
                RelativeLayout main_bg = (RelativeLayout) viewHolder.findViewById(R.id.main_view_bg);
                final String url = obj.getLink();

                main_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        processAction(obj.getAction(), obj.getMeta());
                    }
                });

                String image_Url = obj.getImage();
                if (image_Url.contains("http")) {
                    image_Url = obj.getImage();
                } else {
                    image_Url = ApiClient.BASE_URL + obj.getImage();
                }
                System.out.println("=========image====1==========" + image_Url);

                ProgressBar progressNewsList = (ProgressBar) viewHolder.findViewById(R.id.progressNewsList);
                // NetworkImageView bg_image = (NetworkImageView) viewHolder.findViewById(R.id.main_bg_banner4);

                if (imageLoader == null)
                    imageLoader = App.getInstance().getImageLoader();

                if (image_Url.length() > 10) {
                    //   bg_image.setImageUrl(image_Url, imageLoader);
                    //  loadImage(image_Url, bg_image, progressNewsList);
                }

                //txt_heading.setText(obj.getHeading());
                //  txt_description.setText(obj.getText());
                // System.out.println("=========image====11=========="+image);
            } else if (listViewItemType == TYPE_ADD_BOX) {
                TextView txt_3_heading = (TextView) viewHolder.findViewById(R.id.txt_3_heading);
                TextView txt_3_text1 = (TextView) viewHolder.findViewById(R.id.txt_3_text1);
                TextView txt_3_text2 = (TextView) viewHolder.findViewById(R.id.txt_3_text2);

                ProgressBar progressNewsList = (ProgressBar) viewHolder.findViewById(R.id.progressNewsList);

                String image = obj.getImage();
                System.out.println("=========image====2==========" + image);
                if (image.contains("http")) {
                    image = obj.getImage();
                } else {
                    image = ApiClient.BASE_URL + obj.getImage();
                }
                final String url = obj.getLink();
                /// NetworkImageView main_bg_image = (NetworkImageView) viewHolder.findViewById(R.id.main_bg_image);
                if (imageLoader == null)
                    imageLoader = App.getInstance().getImageLoader();

                if (image.length() > 10) {
                    //  main_bg_image.setImageUrl(image, imageLoader);
                    //  loadImage(image, main_bg_image, progressNewsList);
                }


                txt_3_text2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("=============onClick=====2==============" + url);
                        if (url.length() > 10) {
                            if (url.contains("openbadgenative")) {
                                Intent intent = new Intent(getContext(), Badges_Activity.class);
                                startActivity(intent);
                            } else if (url.contains("group_adventure_invite")) {
                                Intent intent = new Intent(getContext(), ContactDetailsActivity.class);
                                intent.putExtra("URL", url);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", url);
                                startActivity(intent);
                            }
                        }
                    }
                });

                txt_3_heading.setText(obj.getHeading());
                txt_3_text1.setText(obj.getHeading2());
                txt_3_text2.setText(obj.getText());
            } else if (listViewItemType == TYPE_NEXT_BOX) {
                //     NEXT CITY CARD    =============================
                TextView txt_4_city = (TextView) viewHolder.findViewById(R.id.txt_4_city);
                TextView txt_4_steps = (TextView) viewHolder.findViewById(R.id.txt_4_steps);

                ImageView txt_4_image = (ImageView) viewHolder.findViewById(R.id.txt_4_image);
                final String url = obj.getLink();
                String image = obj.getImage();

                if (image.contains("http")) {
                    image = obj.getImage();
                } else {
                    //sDen="2x";
                    image = ApiClient.BASE_URL + obj.getImage();
                    image = image.replace("zoom_level", sDen);
                }
                System.out.println("=========3===sDen========" + sDen);
                int imageSize = Utility.getImageSizeFor_DeviceDensitySize(130);

                if (image.length() > 10) {
                    RequestOptions requestOptions1 = new RequestOptions().centerCrop()
                            .override(imageSize, imageSize)
                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(MapChallengeActivity.this).load(image)
                            .transition(withCrossFade())
                            .apply(requestOptions1)
                            .into(txt_4_image);
                }


                LinearLayout main_bg = (LinearLayout) viewHolder.findViewById(R.id.main_bg);
                main_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        }


                        if (url.length() > 10) {

                            if (url.contains("openbadgenative")) {
                                Intent intent = new Intent(getContext(), Badges_Activity.class);
                                startActivity(intent);
                            } else if (url.contains("group_adventure_invite")) {
                                Intent intent = new Intent(getContext(), ContactDetailsActivity.class);
                                intent.putExtra("URL", url);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", url);
                                startActivity(intent);
                            }
                        }


                    }
                });

                System.out.println("===9====" + obj.getHeading2());
                System.out.println("===99====" + obj.getText());

                txt_4_city.setText(obj.getHeading());


                if (isStringInt(obj.getValue())) {
                    int steps = Integer.parseInt(obj.getValue());
                    String steps_with_comma = NumberFormat.getIntegerInstance().format(steps);
                    txt_4_steps.setText(steps_with_comma);
                } else {
                    txt_4_steps.setText("0");
                }


            } else if (listViewItemType == TYPE_PLAIN_BANNER) {

                TextView txt_5_heading = (TextView) viewHolder.findViewById(R.id.txt_5_heading);
                TextView txt_5_desc = (TextView) viewHolder.findViewById(R.id.txt_5_desc);

                LinearLayout main_bg = (LinearLayout) viewHolder.findViewById(R.id.main_bg);
                String path;
                ImageView txt_5_imgage = (ImageView) viewHolder.findViewById(R.id.txt_5_imgage);
                String imageNew = obj.getImage();
                if (imageNew.contains("http")) {
                    imageNew = obj.getImage();
                } else {
                    imageNew = ApiClient.BASE_URL + obj.getImage();
                    imageNew = imageNew.replace("zoom_level", sDen);
                }
                final String url = obj.getMeta();

                System.out.println("=========4===sDen========" + sDen);
                System.out.println("=============onClick===image================" + imageNew);

                int imageSize = Utility.getImageSizeFor_DeviceDensitySize(130);

                System.out.println("=========4===imageSize========" + imageSize);
                if (imageNew.length() > 10) {
                    RequestOptions requestOptions1 = new RequestOptions().centerCrop()
                            .centerCrop()
                            .override(imageSize, imageSize)
                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(MapChallengeActivity.this).load(imageNew)
                            .transition(withCrossFade())
                            .apply(requestOptions1)
                            .into(txt_5_imgage);
                }

                txt_5_heading.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        processAction(obj.getAction(), obj.getMeta());
                    }
                });

                main_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        processAction(obj.getAction(), obj.getMeta());
                    }
                });

                txt_5_heading.setText(obj.getHeading());
                txt_5_desc.setText(obj.getText());
                txt_5_desc.setMaxLines(3);
            } else if (listViewItemType == TYPE_WORKOUT_SHARE) {
                //share
                ImageButton btn_6_share = (ImageButton) viewHolder.findViewById(R.id.btn_6_share);
                LinearLayout main_share_bg = (LinearLayout) viewHolder.findViewById(R.id.main_share_bg);

                String image_Url = obj.getImage();
                if (image_Url.contains("http")) {
                    image_Url = obj.getImage();
                } else {
                    image_Url = ApiClient.BASE_URL + obj.getImage();
                }
                ProgressBar progressNewsList = (ProgressBar) viewHolder.findViewById(R.id.progressNewsList);
                //  NetworkImageView bg_image = (NetworkImageView) viewHolder.findViewById(R.id.main_bg_banner);
                if (imageLoader == null)
                    imageLoader = App.getInstance().getImageLoader();
                if (image_Url.length() > 10) {
                    //   bg_image.setImageUrl(image_Url, imageLoader);
                    //    loadImage(image_Url, bg_image, progressNewsList);
                }

                share_image = obj.getLink();
                share_cityName = obj.getText();
                share_completed_steps = obj.getValue();
                share_noofDays = obj.getMin_val();
                status = obj.getStatus();


                main_share_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MapChallengeActivity.this, ShareMapPosition_Activity.class);
                        intent.putExtra("share_cityName", share_cityName);
                        intent.putExtra("share_completed_steps", share_completed_steps);
                        intent.putExtra("share_noofDays", share_noofDays);
                        intent.putExtra("status", status);
                        intent.putExtra("share_image", share_image);
                        startActivity(intent);
                    }
                });
                btn_6_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MapChallengeActivity.this, ShareMapPosition_Activity.class);
                        intent.putExtra("share_cityName", share_cityName);
                        intent.putExtra("share_completed_steps", share_completed_steps);
                        intent.putExtra("share_noofDays", share_noofDays);
                        intent.putExtra("share_image", share_image);
                        intent.putExtra("status", status);
                        startActivity(intent);
                    }
                });

            }

            return viewHolder;


        }


        private void loadImage(String imageUrl, final com.android.volley.toolbox.NetworkImageView imageView, final ProgressBar progressBar) {
            progressBar.setVisibility(View.VISIBLE);
            if (imageUrl != null) {
                imageLoader.get(imageUrl, new ImageLoader.ImageListener() {

                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        if (response.getBitmap() != null) {
                            progressBar.setVisibility(View.GONE);
                            imageView.setImageBitmap(response.getBitmap());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //	Log.e("onErrorResponse ", error.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }


    }


    public String loadJSONFromAssetNew(Context context) {
        String json = null;
        try {
            InputStream is = context.openFileInput(pref.getChallangeID() + ".json");

            int size = is.available();
            if (size > 0) {
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } else {
                return null;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    private TimerTask timerTask_timerForAnimation = new TimerTask() {
        @Override
        public void run() {

            MapChallengeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ViewAnimator
                            .animate(dragView)
                            .dp().translationY(-40, 0)
                            .duration(1200)
                            .start();
                }
            });
        }
    };


    public void startGif5() {
        timerForAnimation = new Timer();
        timerForAnimation.schedule(timerTask_timerForAnimation, 2000);

        System.out.println("========timerForAnimation=========");
    }


    class MarkerAdapter implements com.ayubo.life.ayubolife.map_challenges.MarkerAdapter, GoogleMap.InfoWindowAdapter {
        private final View mWindow;

        public MarkerAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.map_custom_info_contents,
                    null);
        }

        @Override
        public View getInfoContents(Marker mark) {

            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {

            render(marker, mWindow);
            return mWindow;
        }

        private void render(Marker marker, View view) {


        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("=========onStart=====0========");
        // startGif();
        System.out.println("=========onStart=====1========");
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        mGoogleMap.setInfoWindowAdapter(new MarkerAdapter());

        if (isPermissionGranted) {

            drawMapPath(map);
        } else {
            System.out.println("===================");
        }

    }

    private void checkFullPermission() {
        if (ContextCompat.checkSelfPermission(MapChallengeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) + ContextCompat
                .checkSelfPermission(MapChallengeActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (MapChallengeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (MapChallengeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder builder = new AlertDialog.Builder(MapChallengeActivity.this);
                builder.setTitle("Need Location Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MapChallengeActivity.this, permissionsRequired, PERMISSIONS_MULTIPLE_REQUEST);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                //   ===========================
//                Snackbar.make(MapChallengeActivity.this.findViewById(android.R.id.content),
//                        "Please Grant Permissions",
//                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (Build.VERSION.SDK_INT >= 23)
//                                    requestPermissions(
//                                            new String[]{Manifest.permission
//                                                    .ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                                            PERMISSIONS_MULTIPLE_REQUEST);
//                            }
//                        }).show();
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            drawMapPath(mGoogleMap);
            isPermissionGranted = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String ur = null;
        try {
            if (resultCode == Activity.RESULT_OK) {
                isPermissionGranted = true;
                if (requestCode == PERMISSIONS_MULTIPLE_REQUEST) {
                    drawMapPath(mGoogleMap);

                } else {
                    System.out.println("===================");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case PERMISSIONS_MULTIPLE_REQUEST: {
                isPermissionGranted = true;
                // If request is cancelled, the result arrays are empty.
                drawMapPath(mGoogleMap);
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;


    void drawMapPath(GoogleMap map) {

        loadLocalDataForMap();


        if (ActivityCompat.checkSelfPermission(MapChallengeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        } else {
            mGoogleMap.setMyLocationEnabled(true);

            //  getLocationJson();

            try {
                System.out.println("=====In drawMap========stated=========");
                List<LatLng> pointsNew = null;
                PolylineOptions optline = new PolylineOptions();
                PolylineOptions optline2 = new PolylineOptions();

                optline.geodesic(true);
                optline.width(15);

                optline2.geodesic(true);
                optline2.width(15);

                mGoogleMap.setMapType(mGoogleMap.MAP_TYPE_SATELLITE);
                mGoogleMap.setMyLocationEnabled(true);

                System.out.println("=====Is points added=========");
                PolylineOptions polylineOptions1 = new PolylineOptions();
                polylineOptions1.addAll(points);
                polylineOptions1.geodesic(true);
                polylineOptions1.width(10);
                polylineOptions1.pattern(PATTERN_POLYGON_ALPHA);


                // ADD CONDITION HERE FOR HIDING WHITE DOTTED LINE ...
                if (white_lines != null) {
                    if (white_lines.equals("true")) {
                        polylineOptions1.color(Color.parseColor("#ffffff"));
                        mGoogleMap.addPolyline(polylineOptions1);
                        System.out.println("====white_lines========Displayed");
                    }
                }

                // ADD CONDITION HERE FOR HIDING WHITE DOTTED LINE ...
                PolylineOptions polylineOptions2 = new PolylineOptions();
                polylineOptions2.addAll(points2);
                polylineOptions2.geodesic(true);
                polylineOptions2.width(10);
                polylineOptions2.color(Color.parseColor("#ed8414"));
                mGoogleMap.addPolyline(polylineOptions2);


                sydney = new LatLng(current_lat, current_longi);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

                int height = 69;
                int width = 59;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.prof);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                if ((myTagList != null) && (myTagList.size() > 0)) {
                    for (int i = 0; i < myTagList.size(); i++) {
                        System.out.println("====myTagList=============" + myTagList.get(i).getCity());
                        RoadLocationObj obj = myTagList.get(i);
                        int totalNum = myTagList.size();
                        totalNum = totalNum - 1;
                        LatLng tagLat = new LatLng(obj.getLat(), obj.getLongitude());

                        if (service_checkpoints.equals("true")) {
                            // int nowSteps = Integer.parseInt(obj.getSteps());
                            if (Utility.isContain(activeArray, Integer.toString(i))) {

                                String isFlagAct = obj.getFlag_act();
                                System.out.println("=============isHaveLink=========" + isFlagAct);
                                if (isFlagAct.equals("landmarke")) {
                                    BitmapDrawable bitmapdraw2 = (BitmapDrawable) getResources().getDrawable(R.drawable.landscape_blue);
                                    Bitmap b2 = bitmapdraw2.getBitmap();
                                    mapMarker = mGoogleMap.addMarker(new MarkerOptions()
                                            .position(tagLat)
                                            .icon(BitmapDescriptorFactory.fromBitmap(b2)));


                                } else {

                                    if (isFlagAct.length() > 5) {
                                        mapMarker = mGoogleMap.addMarker(new MarkerOptions().position(tagLat));
                                        mapMarker.setTag("3_" + Integer.toString(i));

                                        if ((isFlagAct.length() > 5) && (mapMarker != null)) {
                                            loadMarkerIconTreasure(mapMarker, isFlagAct);
                                        }


                                    }

                                }
                                mapMarker.setTag("9_" + Integer.toString(i));

                            } else {

                                String isFlagAct = obj.getFlag_deact();
                                if (isFlagAct.equals("landmarkd")) {
                                    BitmapDrawable bitmapdraw2 = (BitmapDrawable) getResources().getDrawable(R.drawable.landscape_grey);
                                    Bitmap b2 = bitmapdraw2.getBitmap();
                                    mapMarker = mGoogleMap.addMarker(new MarkerOptions()
                                            .position(tagLat)
                                            .icon(BitmapDescriptorFactory.fromBitmap(b2)));
                                } else {
                                    if (isFlagAct.length() > 5) {

                                        mapMarker = mGoogleMap.addMarker(new MarkerOptions().position(tagLat));
                                        mapMarker.setTag("3_" + Integer.toString(i));
                                        loadMarkerIconTreasure(mapMarker, isFlagAct);
                                    }

                                }
                                mapMarker.setTag("disabled");

                            }

                        }
                        //  OLD CITY COLORING WAY FINISH ======= ======== =======
                        else {

                            int nowSteps2 = Integer.parseInt(obj.getSteps());
                            if (total_steps > nowSteps2) {

                                String isFlagAct = obj.getFlag_act();
                                System.out.println("=============isHaveLink=========" + isFlagAct);
                                if (isFlagAct.equals("landmarke")) {
                                    BitmapDrawable bitmapdraw2 = (BitmapDrawable) getResources().getDrawable(R.drawable.landscape_blue);
                                    Bitmap b2 = bitmapdraw2.getBitmap();
                                    mapMarker = mGoogleMap.addMarker(new MarkerOptions()
                                            .position(tagLat)
                                            .icon(BitmapDescriptorFactory.fromBitmap(b2)));

                                } else {

                                    if (isFlagAct.length() > 5) {
                                        mapMarker = mGoogleMap.addMarker(new MarkerOptions().position(tagLat));
                                        mapMarker.setTag("3_" + Integer.toString(i));
                                        loadMarkerIconTreasure(mapMarker, isFlagAct);

                                    }

                                }
                                mapMarker.setTag("9_" + Integer.toString(i));

                            } else {

                                String isFlagAct = obj.getFlag_deact();
                                if (isFlagAct.equals("landmarkd")) {
                                    BitmapDrawable bitmapdraw2 = (BitmapDrawable) getResources().getDrawable(R.drawable.landscape_grey);
                                    Bitmap b2 = bitmapdraw2.getBitmap();
                                    mapMarker = mGoogleMap.addMarker(new MarkerOptions()
                                            .position(tagLat)
                                            .icon(BitmapDescriptorFactory.fromBitmap(b2)));
                                } else {
                                    if (isFlagAct.length() > 5) {

                                        mapMarker = mGoogleMap.addMarker(new MarkerOptions().position(tagLat));
                                        mapMarker.setTag("3_" + Integer.toString(i));
                                        loadMarkerIconTreasure(mapMarker, isFlagAct);
                                    }

                                }
                                mapMarker.setTag("disabled");

                            }

                        }

                    }
                }
//===========LOCATION ADDED==================================================

                System.out.println("===START==DISPLAY TREASURES========");

                try {

                    if ((myTreasureList != null) && (myTreasureList.size() > 0)) {

                        for (int n = 0; n < myTreasureList.size(); n++) {
                            RoadLocationObj obj = myTreasureList.get(n);
                            LatLng tagLat = new LatLng(obj.getLat(), obj.getLongitude());
                            String path = obj.getSteps().toString();
                            path = path.replace("zoom_level", sDen);
                            System.out.println("========myTreasureList=====path==================" + path);
                            mapMarker = mGoogleMap.addMarker(new MarkerOptions().position(tagLat));
                            String tag = "adds_" + Integer.toString(n);
                            mapMarker.setTag(tag);
                            loadMarkerIconTreasure(mapMarker, path);


                            if (obj.getBubble_txt() != null) {
                                if (obj.getBubble_txt().length() > 3) {
                                    showAnotationPopup(mapMarker, tagLat, obj.getBubble_txt(), obj.getBubble_link(), tag);
                                    System.out.println("==========getBubble_txt===Yes====" + obj.getBubble_txt());
                                } else {
                                    System.out.println("==========getBubble_txt=======" + obj.getBubble_txt());

                                }
                            }


                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("========In drawMap my Treasur List Added==================");
                // Creating a marker
                View markerView = ((LayoutInflater) getBaseContext()
                        .getSystemService(MapChallengeActivity.this.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.steps_in_map_layout, null);

                TextView txt_steps_in_map = (TextView) markerView.findViewById(R.id.txt_steps_in_map);


                if (isStringInt(weekSteps)) {
                    String competed_steps_with_comma = NumberFormat.getIntegerInstance().format(Integer.parseInt(weekSteps));
                    txt_steps_in_map.setText(competed_steps_with_comma);
                } else {
                    txt_steps_in_map.setText("0");
                }


                mapMarker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(current_lat, current_longi))
                        .title("")
                        .snippet("")
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(createDrawableFromView(
                                        MapChallengeActivity.this,
                                        markerView))));

                mapMarker.setTag("current_position0");


                mapMarker = mGoogleMap.addMarker(new MarkerOptions()
                        .title(weekSteps)
                        .snippet("Steps")
                        .position(new LatLng(current_lat, current_longi)));
                mapMarker.setTag("current_position");
                mapMarker.showInfoWindow();
                loadProfileIcon(mapMarker);


                mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {

                        if (marker.getTag() != null) {

                            sss = marker.getTag().toString();

                            System.out.println("=========sss=======================" + sss);

                            if (sss.equals("current_position")) {
                                // USER CLICK , NOTHING HAPPEN
                            } else if (sss.equals("current_position0")) {
                                // USER CLICK , NOTHING HAPPEN
                            } else if (sss.startsWith("disabled")) {

                            } else if (sss.startsWith("adds_")) {

                                String[] parts = sss.split("_");
                                String chalid = parts[1];

                                if (myTreasureList.size() >= Integer.parseInt(chalid)) {

                                }

                                RoadLocationObj obj = myTreasureList.get(Integer.parseInt(chalid));
                                String URL = obj.getMeta();

                                String imagePath = obj.getSteps();
                                if (obj.getAuto_hide().equals("true")) {

                                    System.out.println("=======getAuto_hide====true====" + URL);
                                    myTreasureList.remove(Integer.parseInt(chalid));
                                    marker.remove();
                                    marker.setVisible(false);
                                    pref.setMapReload(true);
                                    String dataJSONTreasure = getJSONTreasures(myTreasureList);
                                    pref.createChallangeData(pref.getChallangeID(), white_lines, noof_day, cards, showpopup, service_checkpoints,
                                            enabled_checkpoints, dataJSONTreasure, Integer.toString(total_steps), weekSteps, tip_icon, tip, tipheading, tip_header_1,
                                            tip_header_2, tip_type, tip_meta);
                                }

                                // Process click event ...

                                System.out.println("==========t=======" + obj.getAction());
                                System.out.println("==========t=======" + obj.getMeta());

                                processAction(obj.getAction(), obj.getMeta());

                            } else if (sss.startsWith("9")) {
                                String[] partss = sss.split("_");
                                String chalidd = partss[1];
                                RoadLocationObj obj = myTagList.get(Integer.parseInt(chalidd));
                                String URLj = obj.getMeta();

                                System.out.println("======9===========" + obj.getAction());
                                System.out.println("======9===========" + obj.getMeta());
                                // Process click event ...
                                processAction(obj.getAction(), obj.getMeta());

                            }

                        } else {
                            System.out.println("======Null=================");
                        }


                        return false;
                    }

                });


                //  sydney = new LatLng(6.919839, 79.879808);
                sydney = new LatLng(current_lat, current_longi);

                int zoomLevel = calculateZoomLevelNew2(getScreenWidth());


                int distance = (int) current_distance;

                System.out.println("===In drawMap====distance========" + distance);
                distance = distance * 2;
                //  distance=(distance/3)*2;
                int dens = getScreenWidth();
                // double val
                float fZoom = 8.3f;
                if (0.298 * dens > distance) {
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
                } else if (152 * dens > distance) {
                    fZoom = 10.00f;
                } else if (305 * dens > distance) {
                    fZoom = 9.00f;
                } else if (610 * dens > distance) {
                    fZoom = 8.00f;
                } else if (1222 * dens > distance) {
                    fZoom = 7.00f;
                } else if (2444 * dens > distance) {
                    fZoom = 6.00f;
                } else if (4888 * dens > distance) {
                    fZoom = 5.00f;
                } else {
                    fZoom = 12.00f;
                }
                System.out.println("=In drawMap====== fZoom finished==================");
                String s = Float.toString(fZoom);

//                if(!pref.isMapZoomLevel().equals("5")){
//                 //   setMapZoomLevel
//                  String soomLevel= pref.isMapZoomLevel();
//                    fZoom= Float.parseFloat(soomLevel);
//                    System.out.println("=======Zoooommmmmmmmmmmm========" + s);
//                }


                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, fZoom));
                //  callMet();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void removeDiamonds(final Marker marker) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                marker.remove();
                marker.setVisible(false);
                System.out.println("======removeDiamonds========");
            }
        }, 0);
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(deviceScreenDimension.getDisplayWidth(), deviceScreenDimension.getDisplayHeight());
        view.layout(0, 0, deviceScreenDimension.getDisplayWidth(),
                deviceScreenDimension.getDisplayHeight());
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());
        int height = deviceScreenDimension.getDisplayHeight();
        int width = deviceScreenDimension.getDisplayWidth();
        return width;
    }


    private int calculateZoomLevelNew2(int screenWidth) {
        double equatorLength = 40075004; // in meters
        double widthInPixels = screenWidth;
        double metersPerPixel = equatorLength / 256;
        int zoomLevel = 1;
        if (zooml == null || zooml.equals("") || zooml.equals("0")) {
            zoomLevel = 12;
        } else {

            double dis = 24000;

            while ((metersPerPixel * widthInPixels) > dis) {
                metersPerPixel /= 2;
                ++zoomLevel;
            }
        }
        // 5000   = 16
        //31000===............ 29384.536444415997
        Log.i("ADNAN", "zoom level = " + zoomLevel);
        return zoomLevel;
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


    @Override
    protected void onStop() {
        super.onStop();

    }


    public static class Book implements Parcelable {
        // book basics
        ArrayList<RoadLocationObj> myTagList = new ArrayList<RoadLocationObj>();

        private String title;
        private String author;

        // main constructor
        public Book(ArrayList<RoadLocationObj> myTagLis) {
            this.myTagList = myTagLis;

        }

        public Book(Parcel parcel) {

            myTagList = new ArrayList<RoadLocationObj>();
            this.myTagList = parcel.readArrayList(RoadLocationObj.class.getClassLoader());
        }

        // getters
        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {

            @Override
            public Book createFromParcel(Parcel parcel) {
                return new Book(parcel);
            }

            @Override
            public Book[] newArray(int size) {
                return new Book[size];
            }

//            @Override
//            public Book[] newArray(int size) {
//                return new Book[0];
//            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            try {
                parcel.writeList(myTagList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  dest.writeList(choices);
            //   parcel.writeString(title);
            //  parcel.writeString(author);
        }
    }

    private void loadMarkerIconTreasure(final Marker marker, final String path) {

        Log.d("map tres.", path);

        RequestOptions requestOptions1 = new RequestOptions().fitCenter();
        Glide
                .with(getApplicationContext())
                .asBitmap()
                .load(path)
                .apply(requestOptions1)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        assert e != null;
                        if (e != null) {
                            e.printStackTrace();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (bitmap != null) {
                            Bitmap mBitmap = getResizedBitmapImport(bitmap);
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(mBitmap);
                            Log.d("map tres..", path);
                            try {
                                if ((marker != null) && (icon != null)) {
                                    marker.setIcon(icon);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        return false;
                    }
                }).submit();

    }

    void showAnotationPopup(Marker mk, LatLng lat, String text, String link, String tag) {


        double latD = lat.latitude + 0.00025;
        double longD = lat.longitude + 0.00001;
        LatLng tagLat = new LatLng(latD, longD);

        Bitmap mBitmap = makeCustomBitMap(text);
        mapMarker.setTag(tag);
        mapMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(tagLat)
                .icon(BitmapDescriptorFactory.fromBitmap(mBitmap)));

    }

    public Bitmap createBitmapFromView(View v) {
        v.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return bitmap;
    }

    Bitmap makeCustomBitMap(String text) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout inflatedFrame = (LinearLayout) inflater.inflate(R.layout.map_anotation_bubble, null);
        TextView txt_ano_message = inflatedFrame.findViewById(R.id.txt_ano_message);

        txt_ano_message.setText(text);
        Bitmap bitmap = createBitmapFromView(inflatedFrame.findViewById(R.id.thumbnailbubble));


        return bitmap;
    }


    private void loadProfileIcon(final Marker marker) {
        String imagepath_db = pref.getLoginUser().get("image_path");
        Random rand = new Random();
        int n = rand.nextInt(50) + 1;
        String rannum = Integer.toString(n);
        String burlImg = MAIN_URL_LIVE_HAPPY + imagepath_db + "&cache=" + rannum;
        RequestOptions requestOptions1 = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(this)
                .asBitmap()
                .load(burlImg)
                .apply(requestOptions1)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (bitmap != null) {
                            Bitmap mBitmap = getResizedBitmapImage(bitmap);
                            mBitmap = getCircularBitmapWithWhiteBorder(mBitmap, 2);
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(mBitmap);

                            try {
                                if ((marker != null) && (icon != null)) {
                                    marker.setIcon(icon);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                        return false;
                    }
                }).submit();
    }


    public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap,
                                                          int borderWidth) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width = bitmap.getWidth() + borderWidth;
        final int height = bitmap.getHeight() + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return canvasBitmap;
    }


    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public Bitmap getResizedBitmapImport(Bitmap bitmap) {

        System.out.println("=============squareBitmapWidth=============" + squareBitmapWidth);

        Bitmap outpu = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        int w = bitmap.getWidth();
        float sss = squareBitmapWidth;
        float ratio = (sss / 100) + 1;
        float ratioWidth = ratio * bitmap.getWidth();
        float ratioHeight = ratio * bitmap.getHeight();

        int scWidth = (int) Math.round(ratioWidth);
        int scHeight = (int) Math.round(ratioHeight);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scWidth,
                scHeight, true);

        return scaledBitmap;
    }

    public Bitmap getResizedBitmapImage(Bitmap bitmap) {

        System.out.println("=============squareBitmapWidth=============" + squareBitmapWidth);

        Bitmap outpu = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        int w = bitmap.getWidth();
        float sss = squareBitmapWidth;
        float ratio = (sss / 100);
        float ratioWidth = ratio * bitmap.getWidth();
        float ratioHeight = ratio * bitmap.getHeight();

        int scWidth = (int) Math.round(ratioWidth);
        int scHeight = (int) Math.round(ratioHeight);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scWidth,
                scHeight, true);

        return scaledBitmap;
    }


    public Bitmap getCircularBitmapImport(Bitmap bitmap) {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int den3 = 0;
        try {
            double denDbl = dm.densityDpi;
            den3 = (int) denDbl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (den3 < 320) {
            squareBitmapWidth = 60;
        } else if ((den3 < 480) && (den3 >= 320)) {
            squareBitmapWidth = 120;
        } else if ((den3 < 600) && (den3 >= 480)) {
            squareBitmapWidth = 150;
        } else if ((den3 < 800) && (den3 >= 600)) {
            squareBitmapWidth = 150;
        } else {
            squareBitmapWidth = 120;
        }
        System.out.println("=============squareBitmapWidth=============" + squareBitmapWidth);


//=======================================================
        Bitmap output = Bitmap.createBitmap(squareBitmapWidth,
                squareBitmapWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int width = bitmap.getWidth();
        if (bitmap.getWidth() > bitmap.getHeight())
            width = bitmap.getHeight();
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);
        final RectF rectF = new RectF(rect);
        final float roundPx = width / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    //=============squareBitmapWidth=============
    protected Bitmap addBorderToCircularBitmap(Bitmap srcBitmap, int borderWidth, int borderColor) {
        // Calculate the circular bitmap width with border
        int w = srcBitmap.getWidth();
        int dstBitmapWidth = srcBitmap.getWidth();
        dstBitmapWidth = dstBitmapWidth - 2;
        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth, dstBitmapWidth, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(dstBitmap);
        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);
        canvas.drawCircle(
                canvas.getWidth() / 2, // cx
                canvas.getWidth() / 2, // cy
                canvas.getWidth() / 2 - borderWidth / 2, // Radius
                paint // Paint
        );
        srcBitmap.recycle();
        return dstBitmap;
    }

    private void requestCameraPermission() {

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(MapChallengeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MapChallengeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(MapChallengeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        // END_INCLUDE(camera_permission_request)
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        int position = (int) (marker.getTag());
        System.out.println("=======onMarkerClick========================" + position);
        if (position == 1) {
            System.out.println("===============================");
        }


        return true;
    }


    public boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }


    void processAction(String action, String meta) {


        if (action.equals("treasure")) {
            onTreasureClick(meta);
        }
        if (action.equals("openbadgenative")) {
            if (meta.length() > 0) {
                onOpenBadgersClick(meta);
            }
        }
        if (action.equals("programtimeline")) {
            if (meta.length() > 0) {
                onProgramNewDesignClick(meta);
            }
        }
        if (action.equals("leaderboard")) {
            onLeaderboardClick(meta);
        }
        if (action.equals("program")) {
            onProgramPostClick(meta);
        }
        if (action.equals("challenge")) {
            onMapChallangeClick(meta);
        }
        if (action.equals("chat")) {
            onAskClick(meta);
        }

        if (action.equals("commonview")) {
            onCommonViewClick(meta);
        }
        if (action.equals("web")) {
            onBowserClick(meta);
        }
        if (action.equals("help")) {
            onHelpClick(meta);
        }
        if (action.equals("reports")) {
            onReportsClick(meta);
        }
        if (action.equals("goal")) {
            onGoalClick(meta);
        }
        if (action.equals("videocall")) {
            onVideoCallClick(meta);
        }
        if (action.equals("channeling")) {
            onButtonChannelingClick(meta);
        }
        if (action.equals("janashakthiwelcome")) {
            onJanashakthiWelcomeClick(meta);
        }
        if (action.equals("janashakthireports")) {
            onJanashakthiReportsClick(meta);
        }
        if (action.equals("dynamicquestion")) {
            onDyanamicQuestionClick(meta);
        }
        if (action.equals("post")) {
            onPostClick(meta);
        }
        if (action.equals("native_post")) {
            onNativePostClick(meta);
        }
        if (action.equals("native_post_json")) {
            onJSONNativePostClick(meta);
        }
        if (action.equals("panaroma")) {
            onPanaromaClick(meta);
        }

    }

    void shareLink(Context context, String link) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "");
        i.putExtra(Intent.EXTRA_TEXT, link);
        context.startActivity(Intent.createChooser(i, "Share URL"));
    }

    void onShareClick(Conversation conversation, Integer position) {
        if (conversation.getMedia_type().isEmpty()) {
            shareLink(this, conversation.getMedia_url());
        } else {
            if (!conversation.getMedia_type().equals("link")) {
                //  downloadFile(conversation.media_url, true)
            } else {
                shareLink(this, conversation.getMedia_url());
            }
        }
    }

    void startDoctorsActivity(String doctorID) {
        DocSearchParameters parameters;
        parameters = new DocSearchParameters();
        PrefManager pref = new PrefManager(this);
        parameters.setUser_id(pref.getLoginUser().get("uid"));
        parameters.setDate("");
        parameters.setDoctorId(doctorID);
        parameters.setLocationId("");
        parameters.setSpecializationId("");

        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new SelectDoctorAction(parameters));
        intent.putExtra(SearchActivity.EXTRA_TO_DATE, "");
        startActivity(intent);
    }

    void onButtonChannelingClick(String meta) {
        if (meta.length() > 0) {
            startDoctorsActivity(meta);
        } else {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }
    }

    void onPanaromaClick(String meta) {
        if (gyroExists) {
            Intent intent = new Intent(MapChallengeActivity.this, PanaramaView_NewActivity.class);
            intent.putExtra("URL", meta);
            startActivity(intent);

        } else {
            Intent intent = new Intent(MapChallengeActivity.this, PanoramaView_LawDevices_Activity.class);
            intent.putExtra("URL", meta);
            startActivity(intent);
        }
    }


    void onVideoCallClick(String meta) {
        if (meta.length() > 0) {
            String activity = "my_doctor";
            Intent intent = new Intent(this, MyDoctorLocations_Activity.class);
            intent.putExtra("doctor_id", meta);
            intent.putExtra("activityName", activity);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MyDoctor_Activity.class);
            intent.putExtra("activityName", "myExperts");
            startActivity(intent);
        }
    }

    void onMapChallangeClick(String meta) {

        if (meta.length() > 0) {
            Intent intent = new Intent(this, MapChallengeActivity.class);
            intent.putExtra("challenge_id", meta);
            startActivity(intent);

//            MapChallangesServices serviceObj =new MapChallangesServices(this, meta);
//            serviceObj.Service_getChallengeMapData_ServiceCall();
        } else {
            Intent intent = new Intent(this, NewCHallengeActivity.class);
            startActivity(intent);
        }
    }

    void onAskClick(String meta) {
        if (meta.length() > 0) {
            Intent intent = new Intent(this, AyuboChatActivity.class);
            intent.putExtra("doctorId", meta);
            intent.putExtra("isAppointmentHistory", false);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, AskQuestion_Activity.class);
            startActivity(intent);
        }
    }

    void onTreasureClick(String meta) {
        //TreasureViewActivity.startActivity(this,meta);
        Intent intent = new Intent(this, TreasureViewActivity.class);
        intent.putExtra(EXTRA_TREASURE_KEY, meta);
        startActivity(intent);
    }

    void onOpenBadgersClick(String meta) {
        if (meta.length() > 0) {
            Intent intent = new Intent(this, Badges_Activity.class);
            startActivity(intent);
        }
    }


    void onProgramNewDesignClick(String meta) {
        if (meta.length() > 0) {
            Intent intent = new Intent(this, ProgramActivity.class);
            intent.putExtra("meta", meta);
            startActivity(intent);
        }
    }

    void onLeaderboardClick(String meta) {
        if (meta.length() > 0) {
            Intent intent = new Intent(this, NewLeaderBoardActivity.class);
            intent.putExtra("challange_id", meta);
            startActivity(intent);
        }
    }

    void onProgramPostClick(String meta) {
        if (meta.length() > 0) {
            Intent intent = new Intent(this, SingleTimeline_Activity.class);
            intent.putExtra("related_by_id", meta);
            intent.putExtra("type", "program");
            startActivity(intent);
        }
    }

    void onGoalClick(String meta) {

        if (meta.length() > 0) {
            PrefManager prefManager = new PrefManager(this);
            String status = prefManager.getMyGoalData().get("my_goal_status");

            if (status == "Pending") {
                Intent intent = new Intent(this, AchivedGoal_Activity.class);
                startActivity(intent);
            } else if (status == "Pick") {
                Intent intent = new Intent(this, PickAGoal_Activity.class);
                startActivity(intent);
            } else if (status == "Completed") {
                HomePage_Utility serviceObj = new HomePage_Utility(this);
                serviceObj.showAlert_Deleted(this, "This goal has been achieved for the day. Please pick another goal tomorrow");
            }
        }
    }

    void onJSONNativePostClick(String meta) {
        Intent intent = new Intent(this, NativePostJSONActivity.class);
        intent.putExtra("meta", meta);
        startActivity(intent);
    }

    void onNativePostClick(String meta) {
        Intent intent = new Intent(this, NativePostActivity.class);
        intent.putExtra("meta", meta);
        startActivity(intent);
    }

    void onPostClick(String meta) {
        Intent intent = new Intent(this, OpenPostActivity.class);
        intent.putExtra("postID", meta);
        startActivity(intent);
    }

    void onJanashakthiWelcomeClick(String meta) {
        PrefManager pref = new PrefManager(this);
        pref.setRelateID(meta);
        pref.setIsJanashakthiWelcomee(true);
        Intent intent = new Intent(this, JanashakthiWelcomeActivity.class);
        startActivity(intent);
    }

    void onDyanamicQuestionClick(String meta) {
        PrefManager pref = new PrefManager(this);
        pref.setIsJanashakthiDyanamic(true);
        pref.setRelateID(meta);
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    void onJanashakthiReportsClick(String meta) {
        MedicalUpdateActivity.startActivity(this);
    }

    void onReportsClick(String meta) {
        Intent intent = new Intent(this, ReportDetailsActivity.class);
        intent.putExtra("data", "all");
        Ram.setReportsType("fromHome");
        startActivity(intent);
    }

    void onLifePointsClick(String activityName, String meta) {
        Intent intent = new Intent(this, LifePointActivity.class);
        startActivity(intent);
    }

    void onHelpClick(String meta) {
        Intent intent = new Intent(this, HelpFeedbackActivity.class);
        intent.putExtra("activityName", "myExperts");
        startActivity(intent);
    }

    void onBowserClick(String meta) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(meta));
        startActivity(browserIntent);
    }

    void onCommonViewClick(String meta) {
        Intent intent = new Intent(this, CommonWebViewActivity.class);
        intent.putExtra("URL", meta);
        startActivity(intent);
    }

    public void NService_getChallengeMapData_ServiceCall() {
        if (pref.getIsFromPush()) {
            // Dont start activity. This is for Push...
        }
        if (pref.isFromJoinChallenge()) {
            // No need progress
        } else {
            proDialog.show();
        }
        System.out.println("=======challenge_id===0=====" + chID);

        if (Utility.isInternetAvailable(MapChallengeActivity.this)) {
            NService_getStepsData task = new NService_getStepsData();
            task.execute(new String[]{ApiClient.BASE_URL_live_v6});
        } else {
            Toast.makeText(MapChallengeActivity.this, "No active internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private class NService_getStepsDataAtResume extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            System.out.println("========Service Calling============0");
            NgetChallangeData();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (serviceDataStatus.equals("0")) {
                String cityFile = loadJSONFromAssetNew(com.ayubo.life.ayubolife.webrtc.App.getInstance());
                if (cityFile == null) {

                    Service_getChallengeJson_ServiceCall();
                } else {

                    if (mGoogleMap != null) {
                        //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                        if (pref.isNeedMapReload()) {
                            mGoogleMap.clear();
                        }
                        drawMapPath(mGoogleMap);
                        // add markers from database to the map
                    }

                    loadSpecialTip(tip);

                    loadCards();

                }
            }
            if (serviceDataStatus.equals("23")) {

                System.out.println("=======challenge_id====in MAp 23========" + pref.getChallangeID());

                Intent intent = new Intent(MapChallengeActivity.this, MapJoinChallenge_Activity.class);
                intent.putExtra("challenge_id", pref.getChallangeID());
                startActivity(intent);
            }

        }
    }

    private class NService_getStepsData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            System.out.println("========Service Calling============0");
            NgetChallangeData();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("========Service Calling============6");
            if (proDialog != null) {
                proDialog.dismiss();
            }

            if (serviceDataStatus.equals("0")) {
                String cityFile = loadJSONFromAssetNew(com.ayubo.life.ayubolife.webrtc.App.getInstance());
                if (cityFile == null) {

                    Service_getChallengeJson_ServiceCall();
                } else {

                    //   getLocationJson();
                    NprocessLocalJsonClass task = new NprocessLocalJsonClass();
                    task.execute(new String[]{ApiClient.BASE_URL_live_v6});

                }
            }
            if (serviceDataStatus.equals("23")) {

                System.out.println("=======challenge_id====in MAp 23========");

//                Intent intent = new Intent(MapChallengeActivity.this, MapJoinChallenge_Activity.class);
//                intent.putExtra("challenge_id",pref.getChallangeID());
//                startActivity(intent);
            }

        }
    }

    private void NgetChallangeData() {

        myTagList = new ArrayList<RoadLocationObj>();
        myTreasure = new ArrayList<RoadLocationObj>();

        serviceDataStatus = "99";
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(MAIN_URL_LIVE_HAPPY + "custom/service/v6/rest.php");
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        userid_ExistingUser = pref.getLoginUser().get("uid");


        String jsonStr =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"," +
                        "\"challenge_id\": \"" + pref.getChallangeID() + "\"" +
                        "}";

        System.out.println("=======challenge_id===getChallengeSteps======" + userid_ExistingUser);
        System.out.println("=======challenge_id===getChallengeSteps======" + pref.getChallangeID());


        nameValuePair.add(new BasicNameValuePair("method", "getChallengeSteps"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


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
            String res = null;
            try {
                if ((jsonObj != null) && ((jsonObj.toString().length() > 5))) {
                    res = jsonObj.optString("result");

                    serviceDataStatus = res;
                    int result = Integer.parseInt(res);

                    if (result == 0) {

                        try {

                            serviceDataStatus = "0";
                            if (myTreasure != null) {
                                myTreasure.clear();
                            }


                            String data = jsonObj.optString("data");
                            System.out.println("====New====Service Calling============" + data);
                            JSONObject jsonData = new JSONObject(data);
                            //Map Data====================================
                            service_checkpoints = jsonData.optString("service_checkpoints");
                            if (service_checkpoints.equals("true")) {
                                enabled_checkpoints = jsonData.optString("enabled_checkpoints");
                            } else {
                                service_checkpoints = "";
                                enabled_checkpoints = "";
                            }
                            tip_icon = jsonData.optString("tip_icon");
                            tip = jsonData.optString("tip");
                            tipheading = jsonData.optString("tipheading");

                            tip_header_1 = jsonData.optString("tip_header_1");
                            tip_header_2 = jsonData.optString("tip_header_2");
                            tip_type = jsonData.optString("tip_type");
                            tip_meta = jsonData.optString("tip_meta");
                            objimg = jsonData.optString("objimg");


                            white_lines = jsonData.optString("white_lines");
                            weekSteps = jsonData.optString("weekSteps");
                            String counter = jsonData.optString("counter");
                            noof_day = jsonData.optString("day");
                            treatures = jsonData.optString("treatures");
                            cards = jsonData.optString("cards");
                            total_steps = Integer.parseInt(counter);

                            pref.createChallangeData(pref.getChallangeID(), white_lines, noof_day, cards, showpopup, service_checkpoints,
                                    enabled_checkpoints, treatures, Integer.toString(total_steps), weekSteps, tip_icon, tip, tipheading, tip_header_1,
                                    tip_header_2, tip_type, tip_meta);


                            JSONArray myDataListsAll = null;
                            try {
                                myDataListsAll = new JSONArray(treatures);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < myDataListsAll.length(); i++) {

                                JSONObject childJson = null;
                                try {
                                    childJson = (JSONObject) myDataListsAll.get(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                String latp = childJson.optString("latp");
                                String longp = childJson.optString("longp");
                                String steps = childJson.optString("steps");
                                String objimg = childJson.optString("objimg");
                                String action = childJson.optString("action");
                                String meta = childJson.optString("meta");
                                String auto_hide = childJson.optString("auto_hide");

                                System.out.println("===ch=====action=====================" + action);
                                System.out.println("===ch=====meta======================" + meta);

                                String status = childJson.optString("status");

                                double roadPath_lat = Double.parseDouble(latp);
                                double roadPath_longitude = Double.parseDouble(longp);

                                String bubble_text = null;
                                String bubble_link = null;
                                if (childJson.has("bubble_txt")) {
                                    bubble_text = childJson.getString("bubble_txt");
                                } else {
                                    bubble_text = "";
                                }
                                if (childJson.has("bubble_link")) {
                                    bubble_link = childJson.getString("bubble_link");
                                } else {
                                    bubble_link = "";
                                }


                                myTreasure.add(new RoadLocationObj(roadPath_lat, roadPath_longitude, steps, objimg, action, meta, status, "", "", "",
                                        "", "", "", "", "", "", bubble_text, bubble_link, auto_hide));

                            }
                            System.out.println(myTreasure.size());

                        } catch (Exception e) {
                            serviceDataStatus = "99";
                            e.printStackTrace();
                        }


                    } else if (result == 23) {
                        String error = jsonObj.optString("error");

                        if (error.equals("User is not connected with challenge yet.")) {
                            Intent intent = new Intent(this, MapJoinChallenge_Activity.class);
                            intent.putExtra("challenge_id", chID);
                            intent.putExtra("steps", Integer.toString(total_steps));
                            startActivity(intent);
                            finish();

                        }
                    } else {
                        System.out.println("========Service Calling============10");
                        System.out.println("========Service Calling============result" + result);

                        serviceDataStatus = "23";
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                serviceDataStatus = "23";
            }


        } catch (IOException e) {
            serviceDataStatus = "23";
            System.out.println("========Service Calling============8");
            e.printStackTrace();
        }

    }

    private class NprocessLocalJsonClass extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            proDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            NgetLocationJson();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (proDialog != null) {
                proDialog.dismiss();
            }


            if (mGoogleMap != null) {
                //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                if (pref.isNeedMapReload()) {
                    mGoogleMap.clear();
                }
                drawMapPath(mGoogleMap);
                // add markers from database to the map
            }

            loadSpecialTip(tip);

            loadCards();
        }
    }

    void loadLocalDataForMap() {


        try {
            cards = pref.getChallangeData().get("cards");
            chID = pref.getChallangeData().get("challenge_id");
            System.out.println("...loadLocalDataForMap.....chID..........." + pref.getChallangeID());
            chID = pref.getChallangeID();
            strstep = pref.getChallangeData().get("steps");
            noof_day = pref.getChallangeData().get("noof_day");
            white_lines = pref.getChallangeData().get("white_lines");
            treatures = pref.getChallangeData().get("Treatures");
            showpopup = pref.getChallangeData().get("showpopup");
            service_checkpoints = pref.getChallangeData().get("service_checkpoints");
            enabled_checkpoints = pref.getChallangeData().get("enabled_checkpoints");
            weekSteps = pref.getChallangeData().get("weekSteps");
            tip_icon = pref.getChallangeData().get("tip_icon");
            tip = pref.getChallangeData().get("tip");
            tipheading = pref.getChallangeData().get("tipheading");
            tip_header_1 = pref.getChallangeData().get("tip_header_1");
            tip_header_2 = pref.getChallangeData().get("tip_header_2");
            tip_type = pref.getChallangeData().get("tip_type");
            tip_meta = pref.getChallangeData().get("tip_meta");


            System.out.println("Treatures.........in map challenge...@ Start......" + treatures);

        } catch (Exception e) {
            e.printStackTrace();

        }

        if ((treatures != null) && (treatures.length() > 0)) {
            myTreasure = getTreasures(treatures);
            myTreasureList = myTreasure;

        }

        activeArray = new ArrayList<String>();

        if (service_checkpoints != null) {
            if (service_checkpoints.equals("true")) {
                enabled_checkpoints = pref.getChallangeData().get("enabled_checkpoints");
                activeArray = Utility.getActiveArray(enabled_checkpoints);
                System.out.println("=========active Journal Array===============" + activeArray.toString());
            }
        }

    }


    void loadCards() {

        try {

            if (showpopup != null) {
                if (showpopup.length() > 4) {
                    layout_popup_advers.setVisibility(View.VISIBLE);

                    Random rand = new Random();
                    int n = rand.nextInt(50) + 1;
                    String rannum = Integer.toString(n);
                    String url = ApiClient.BASE_URL + "resize.php?img=/datadrive/wellness_upload/upload/" + pref.getChallangeID() + "_popup&" + rannum;

                    System.out.println("==================popupshow================" + url);
                    ProgressBar progressNewsList = (ProgressBar) findViewById(R.id.progressNewsList);
                    NetworkImageView main_bg_banner = (NetworkImageView) findViewById(R.id.main_bg_share_banner);
                    if (imageLoader == null)
                        imageLoader = App.getInstance().getImageLoader();
                    main_bg_banner.setImageUrl(url, imageLoader);
                    loadImage(url, main_bg_banner, progressNewsList);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layout_popup_advers.setVisibility(View.GONE);
                        }
                    }, 10000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        dragView = (LinearLayout) findViewById(R.id.dragView);

        //===============Card Next try=======================
        try {

            if (cards != null) {
                String completed_steps, completed_days, completed_avg, remaining_steps, remaining_days, remaining_avg;
                String action, related_id, name, type, text, image, heading, heading2, link, status, value, min_val, max_val, desc, title, subtitle, meta;
                ;
                title = "";
                related_id = "";
                subtitle = "";
                meta = "";
                name = "";
                type = "";
                text = "";
                image = "";
                heading = "";
                heading2 = "";
                link = "";
                status = "";
                value = "";
                min_val = "";
                max_val = "";
                desc = "";

                if (cards.length() > 10) {
                    JSONArray myCardsLists = null;
                    try {
                        myCardsLists = new JSONArray(cards);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    if (myCardsLists.length() > 0) {
                        dragView.setVisibility(View.VISIBLE);

                        for (int i = 0; i < myCardsLists.length(); i++) {

                            JSONObject childJson = null;
                            try {
                                childJson = (JSONObject) myCardsLists.get(i);
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }

                            type = childJson.optString("type").toString();

                            if (type.equals("main_card_2")) {
                                heading = childJson.optString("heading").toString();
                                name = childJson.optString("name").toString();
                                status = childJson.optString("weather_status").toString();
                                image = childJson.optString("weather_icon").toString();
                                value = childJson.optString("degree").toString();
                                desc = childJson.optString("description").toString();
                                max_val = childJson.optString("max_degree").toString();
                                min_val = childJson.optString("min_degree").toString();

                                completed_steps = childJson.optString("completed_steps").toString();
                                completed_days = childJson.optString("completed_days").toString();
                                completed_avg = childJson.optString("completed_avg").toString();

                                remaining_steps = childJson.optString("remaining_steps").toString();
                                remaining_days = childJson.optString("remaining_days").toString();
                                remaining_avg = childJson.optString("remaining_avg").toString();
                                link = childJson.optString("link").toString();

                                share_cityName = name;
                                share_completed_steps = completed_steps;
                                share_noofDays = completed_days;

                                if ((completed_steps.equals("")) || (completed_steps.equals("-"))) {
                                    completed_steps = "0";
                                }
                                if ((remaining_steps.equals("")) || (remaining_steps.equals("-"))) {
                                    remaining_steps = "0";
                                }
                                String completed_steps_with_comma = null;
                                if (isStringInt(completed_steps)) {
                                    int num = Integer.parseInt(completed_steps);
                                    completed_steps_with_comma = NumberFormat.getIntegerInstance().format(num);
                                } else {
                                    completed_steps_with_comma = "0";
                                }


                                String completed_avg_with_comma = null;
                                if (isStringInt(completed_avg)) {
                                    int num_completed_avg = Integer.parseInt(completed_avg);
                                    completed_avg_with_comma = NumberFormat.getIntegerInstance().format(num_completed_avg);
                                } else {
                                    completed_avg_with_comma = "0";
                                }

                                String remaining_steps_with_comma = null;
                                if (isStringInt(remaining_steps)) {
                                    int remaining_steps_in_int = Integer.parseInt(remaining_steps);
                                    remaining_steps_with_comma = NumberFormat.getIntegerInstance().format(remaining_steps_in_int);
                                } else {
                                    remaining_steps_with_comma = "0";
                                }

                                String remaining_avg_with_comma = null;
                                if (isStringInt(remaining_avg)) {
                                    int num_remaining_avg = Integer.parseInt(remaining_avg);
                                    remaining_avg_with_comma = NumberFormat.getIntegerInstance().format(num_remaining_avg);
                                } else {
                                    remaining_avg_with_comma = "0";
                                }

                                String completedTextArray = completed_steps_with_comma + "_" + completed_days + "_" + completed_avg_with_comma + "/Day";
                                String remainingTextArray = remaining_steps_with_comma + "_" + remaining_days + "_" + remaining_avg_with_comma + "/Day";

                                mapCardsList.add(new CardsEntity(type, text, image, heading, name, link, status, value, remainingTextArray, completedTextArray, desc, title, subtitle, "", meta, related_id));

                            } else if (type.equals("main_card")) {

                                name = childJson.optString("name").toString();
                                status = childJson.optString("weather_status").toString();
                                image = childJson.optString("weather_icon").toString();
                                value = childJson.optString("degree").toString();
                                desc = childJson.optString("description").toString();
                                max_val = childJson.optString("max_degree").toString();
                                min_val = childJson.optString("min_degree").toString();

                                completed_steps = childJson.optString("completed_steps").toString();
                                completed_days = childJson.optString("completed_days").toString();
                                completed_avg = childJson.optString("completed_avg").toString();

                                remaining_steps = childJson.optString("remaining_steps").toString();
                                remaining_days = childJson.optString("remaining_days").toString();
                                remaining_avg = childJson.optString("remaining_avg").toString();
                                link = childJson.optString("link").toString();

                                share_cityName = name;
                                share_completed_steps = completed_steps;
                                share_noofDays = completed_days;

                                if ((completed_steps.equals("")) || (completed_steps.equals("-"))) {
                                    completed_steps = "0";
                                }
                                if ((remaining_steps.equals("")) || (remaining_steps.equals("-"))) {
                                    remaining_steps = "0";
                                }

                                String completed_steps_with_comma = null;
                                if (isStringInt(completed_steps)) {
                                    int num = Integer.parseInt(completed_steps);
                                    completed_steps_with_comma = NumberFormat.getIntegerInstance().format(num);
                                } else {
                                    completed_steps_with_comma = "0";
                                }


                                String completed_avg_with_comma = null;
                                if (isStringInt(completed_avg)) {
                                    int num_completed_avg = Integer.parseInt(completed_avg);
                                    completed_avg_with_comma = NumberFormat.getIntegerInstance().format(num_completed_avg);
                                } else {
                                    completed_avg_with_comma = "0";
                                }

                                String remaining_steps_with_comma = null;
                                if (isStringInt(remaining_steps)) {
                                    int remaining_steps_in_int = Integer.parseInt(remaining_steps);
                                    remaining_steps_with_comma = NumberFormat.getIntegerInstance().format(remaining_steps_in_int);
                                } else {
                                    remaining_steps_with_comma = "0";
                                }

                                String remaining_avg_with_comma = null;
                                if (isStringInt(remaining_avg)) {
                                    int num_remaining_avg = Integer.parseInt(remaining_avg);
                                    remaining_avg_with_comma = NumberFormat.getIntegerInstance().format(num_remaining_avg);
                                } else {
                                    remaining_avg_with_comma = "0";
                                }

                                String completedTextArray = completed_steps_with_comma + "_" + completed_days + "_" + completed_avg_with_comma + "/Day";
                                String remainingTextArray = remaining_steps_with_comma + "_" + remaining_days + "_" + remaining_avg_with_comma + "/Day";

                                mapCardsList.add(new CardsEntity(type, text, image, heading, name, link, status, value, remainingTextArray, completedTextArray, desc, title, subtitle, "", meta, related_id));

                            } else if (type.equals("html_card")) {
                                String html = childJson.optString("html").toString();
                                link = childJson.optString("link").toString();
                                mapCardsList.add(new CardsEntity(type, html, "", "", "", link, "", "", "", "", "", title, subtitle, "", meta, related_id));
                            } else if (type.equals("waiting_to_start")) {
                                value = childJson.optString("remaining_days").toString();
                                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc, title, subtitle, "", meta, related_id));
                            } else if (type.equals("complete")) {

                                heading = childJson.optString("heading").toString();
                                value = childJson.optString("completed_days").toString();
                                text = childJson.optString("text").toString();
                                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc, title, subtitle, "", meta, related_id));
                            } else if (type.equals("incomplete")) {
                                value = childJson.optString("completed_days").toString();
                                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc, title, subtitle, "", meta, related_id));
                            } else if (type.equals("statistics")) {
                                value = childJson.optString("completed_avg").toString();
                                min_val = childJson.optString("collected_treatures").toString();
                                max_val = childJson.optString("collected_entries").toString();
                                link = childJson.optString("link").toString();

                                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc, title, subtitle, "", meta, related_id));
                            } else if (type.equals("weather")) {

                                name = childJson.optString("name").toString();
                                status = childJson.optString("weather_status").toString();
                                image = childJson.optString("weather_icon").toString();
                                value = childJson.optString("degree").toString();
                                desc = childJson.optString("description").toString();
                                max_val = childJson.optString("max_degree").toString();
                                min_val = childJson.optString("min_degree").toString();

                                link = childJson.optString("link").toString();

                                //  TextView header_text_indraggable = (TextView) findViewById(R.id.header_text_indraggable);
                                TextView txt_value = (TextView) findViewById(R.id.txt_value);
                                TextView txt_description = (TextView) findViewById(R.id.txt_description);
                                ImageView img_weather_icon = (ImageView) findViewById(R.id.img_weather_icon);

                                if (img_weather_icon != null) {
                                    Picasso
                                            .with(MapChallengeActivity.this)
                                            .load(image)
                                            .fit() // will explain later
                                            .into(img_weather_icon);
                                }


                                //    header_text_indraggable.setText(name);
                                txt_value.setText((value) + " \u2103");
                                txt_description.setText(desc);
                                //  String name,type,text,image,heading,heading2,link,status,value,min_val,max_val,desc;
                                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc, title, subtitle, "", meta, related_id));
                            } else if (type.equals("location")) {
                                image = childJson.optString("icon").toString();
                                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc, title, subtitle, "", meta, related_id));
                            } else if (type.equals("banner")) {
                                heading = childJson.optString("heading").toString();
                                text = childJson.optString("text").toString();
                                image = childJson.optString("image").toString();
                                action = childJson.optString("action").toString();
                                meta = childJson.optString("meta").toString();


                                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, action, status, value, min_val, max_val, desc, title, subtitle, action, meta, related_id));
                            } else if (type.equals("add_box")) {
                                heading = childJson.optString("heading").toString();
                                heading2 = childJson.optString("subheading").toString();
                                text = childJson.optString("text").toString();
                                image = childJson.optString("image").toString();
                                link = childJson.optString("link").toString();
                                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc, title, subtitle, "", meta, related_id));
                            } else if (type.equals("next_box")) {

                                heading = childJson.optString("name");
                                value = childJson.optString("stepstonext");

                                image = childJson.optString("icon").toString();
                                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc, title, subtitle, "", meta, related_id));
                            } else if (type.equals("plain_banner")) {
                                heading = childJson.optString("heading");
                                text = childJson.optString("text");
                                image = childJson.optString("icon");
                                action = childJson.optString("action");
                                meta = childJson.optString("meta");
                                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, action, status, value, min_val, max_val, desc, title, subtitle, action, meta, related_id));
                            } else if (type.equals("share_box")) {

                                text = childJson.optString("city").toString();
                                value = childJson.optString("steps").toString();
                                min_val = childJson.optString("days").toString();
                                status = childJson.optString("status").toString();
                                link = childJson.optString("share_image").toString();
                                image = childJson.optString("bg_img").toString();

                                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc, title, subtitle, "", meta, related_id));
                            } else if (type.equals("native_leaderboard")) {

                                text = childJson.optString("city").toString();
                                value = childJson.optString("steps").toString();
                                min_val = childJson.optString("days").toString();
                                status = childJson.optString("status").toString();
                                link = childJson.optString("share_image").toString();
                                image = childJson.optString("bg_img").toString();
                                title = childJson.optString("title").toString();
                                subtitle = childJson.optString("subtitle").toString();
                                meta = childJson.optString("meta").toString();
                                action = childJson.optString("action");

                                related_id = childJson.optString("related_id").toString();
                                mapCardsList.add(new CardsEntity(type, text, image, heading, heading2, link, status, value, min_val, max_val, desc, title, subtitle, action, meta, related_id));
                            } else {

                            }
                        }
                        // mapCardsList.add(new CardsEntity("a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a"));
                        CustomMapDetaisl_lv = (ListView) findViewById(R.id.list_new_design);
                        int f = 0;
                        // try {
                        items = new ListViewItem[mapCardsList.size()];
                        int size = mapCardsList.size();
                        //  size = size - 1;
                        for (int b = 0; b < size; b++) {
                            items[b] = new ListViewItem("firstraw", b);
                            f = b;
                        }
                        items[f] = new ListViewItem("firstraw", f);

                        customAdapter = new CustomAdapterFresh(MapChallengeActivity.this, R.id.text, items);
                        CustomMapDetaisl_lv.setAdapter(customAdapter);

                        CustomMapDetaisl_lv.setOnTouchListener(new View.OnTouchListener() {
                            public boolean onTouch(View v, MotionEvent event) {
                                System.out.println("........................" + event.getAction());
                                return true;
                            }
                        });

                        top_bgnew = (LinearLayout) findViewById(R.id.top_bgnew);
                        img_btn_down_arrow = (ImageView) findViewById(R.id.img_btn_down_arrow);
                        img_btn_down_arrow.setVisibility(View.GONE);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(20, 0, 20, 0);
                        top_bgnew.setLayoutParams(params);

                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) CustomMapDetaisl_lv
                                .getLayoutParams();
                        mlp.setMargins(20, 0, 20, 0);


                        ViewAnimator
                                .animate(dragView)
                                .dp().translationY(0, -40)
                                .duration(1200)
                                .start();

                        startGif5();

                        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                            @Override
                            public void onPanelSlide(View panel, float slideOffset) {
                                Log.i(TAG, "onPanelSlide, offset " + slideOffset);

                                float margin = slideOffset * 30;
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                int intRate = (int) margin;
                                intRate = 30 - intRate;
                                Log.i(TAG, "onPanelSlide, ...... " + intRate);
                                params.setMargins(intRate, 0, intRate, 0);
                                top_bgnew.setLayoutParams(params);

                                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) CustomMapDetaisl_lv
                                        .getLayoutParams();
                                mlp.setMargins(intRate, 0, intRate, 0);

                                if (0.89 < slideOffset) {
                                    mLayout.setDragView(top_bgnew);
                                    img_btn_down_arrow.setVisibility(View.VISIBLE);
                                    CustomMapDetaisl_lv.setOnTouchListener(new View.OnTouchListener() {
                                        public boolean onTouch(View v, MotionEvent event) {
                                            return false;
                                        }
                                    });
                                } else {
                                    CustomMapDetaisl_lv.setSelection(0);
                                    mLayout.setDragView(dragView);
                                    img_btn_down_arrow.setVisibility(View.GONE);
                                    CustomMapDetaisl_lv.setOnTouchListener(new View.OnTouchListener() {
                                        public boolean onTouch(View v, MotionEvent event) {
                                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                                            System.out.println("..................0......" + event.getAction());
                                            return true;
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                                Log.i(TAG, "onPanelStateChanged " + newState);

                                if (newState.equals("EXPANDED")) {

                                } else {
                                    //  mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

                                }

                            }
                        });

                        mLayout.setFadeOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            }
                        });

                    }

                }
            } else {
                dragView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    void loadSpecialTip(String tips) {

        if (tips != null) {
            if (tips.length() > 4) {
                layout_daily_tip.setVisibility(View.VISIBLE);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View myView = inflater.inflate(R.layout.popup_daily_tips, null);


                CardView card_one = (CardView) myView.findViewById(R.id.card_one);
                card_one.setRadius(15);

                LinearLayout layout_tip_close = myView.findViewById(R.id.layout_tip_close);
                ImageButton img_tip_close = (ImageButton) myView.findViewById(R.id.img_tip_close);
                ImageView img_tip_icon = (ImageView) myView.findViewById(R.id.img_tip_icon);
                TextView txt_tip_text = (TextView) myView.findViewById(R.id.txt_tip_text);
                TextView txt_heading_tip = (TextView) myView.findViewById(R.id.txt_heading_tip);
                TextView txt_tip_bottom_button_left = (TextView) myView.findViewById(R.id.txt_tip_bottom_button_left);

                RelativeLayout lay_tip_footer = myView.findViewById(R.id.lay_tip_footer);

                TextView txt_tip_bottom_button_right = (TextView) myView.findViewById(R.id.txt_tip_bottom_button_right);

                if (tip_type.length() == 0) {
                    lay_tip_footer.setVisibility(View.GONE);
                }


                txt_tip_bottom_button_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (tip_type.equals("chat")) {
                            AyuboChatActivity.Companion.startActivity(MapChallengeActivity.this, tip_meta, false, false, "");
                        }

                    }
                });

                img_tip_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layout_daily_tip.removeAllViews();
                        layout_daily_tip.setVisibility(View.GONE);
                    }
                });
                layout_tip_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layout_daily_tip.removeAllViews();
                        layout_daily_tip.setVisibility(View.GONE);
                    }
                });

                txt_tip_text.setText(tips);
                txt_heading_tip.setText(tipheading);

                int imageSize = Utility.getImageSizeFor_DeviceDensitySize(120);

//                RequestOptions myOptions = new RequestOptions()
//                        .centerCrop()
//                        .override(imageSize, imageSize);


                RequestOptions requestOptions = new RequestOptions().
                        transform(new CircleTransform(this)).
                        diskCacheStrategy(DiskCacheStrategy.NONE)
                        .override(imageSize, imageSize);


                String text = tip_header_1 + "\n" + tip_header_2;
                txt_tip_bottom_button_left.setText(text);

                Glide.with(MapChallengeActivity.this).load(tip_icon)
                        .apply(requestOptions)
                        .transition(withCrossFade())
                        .into(img_tip_icon);

                layout_daily_tip.addView(myView);

            }

        }
    }


    private void NgetLocationJson() {
        JSONObject obj = null;

        points = new ArrayList<LatLng>();
        points2 = new ArrayList<LatLng>();

        try {
            JSONArray m_jArry = null;
            whitePath_myRoadLocationsList = new ArrayList<RoadLocationObj>();
            pendingRoadLocationsList = new ArrayList<RoadLocationObj>();

            String cityFile = loadJSONFromAssetNew(com.ayubo.life.ayubolife.webrtc.App.getInstance());
            if (cityFile != null) {

                try {
                    obj = new JSONObject(loadJSONFromAssetNew(com.ayubo.life.ayubolife.webrtc.App.getInstance()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // JSONObject obj = new JSONObject(loadJSONFromAsset());
                try {
                    m_jArry = obj.getJSONArray("route");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> m_li;

                myTagList = new ArrayList<RoadLocationObj>();
                if (m_jArry != null) {


                    for (int i = 0; i < m_jArry.length(); i++) {

                        JSONObject jo_inside = m_jArry.getJSONObject(i);

                        String lat = jo_inside.getString("lat");
                        String longitude = jo_inside.getString("long");
                        String distance = jo_inside.getString("distance");
                        String ssteps = jo_inside.getString("steps");
                        String link = jo_inside.getString("link");

                        String flag_act = jo_inside.getString("flag_act");
                        String flag_deact = jo_inside.getString("flag_deact");
                        String stepstonext = jo_inside.getString("stepstonext");
                        String nextcity = jo_inside.getString("nextcity");
                        String city = jo_inside.getString("city");
                        String citymsg = jo_inside.getString("citymsg");
                        String cityimg = jo_inside.getString("cityimg");
                        String wc = jo_inside.getString("wc");
                        String zoom = jo_inside.getString("zooml");
                        // String auto_hide = jo_inside.getString("auto_hide");
                        String auto_hide = "";
                        String meta = "";
                        String disableimg = null;
                        if (jo_inside.has("disableimg")) {
                            disableimg = jo_inside.getString("disableimg");

                        } else {
                            disableimg = "";
                        }

                        String bubble_text = null;
                        String bubble_link = null;
                        if (jo_inside.has("bubble_txt")) {
                            bubble_text = jo_inside.getString("bubble_txt");
                        } else {
                            bubble_text = "";
                        }
                        if (jo_inside.has("bubble_link")) {
                            bubble_link = jo_inside.getString("bubble_link");
                        } else {
                            bubble_link = "";
                        }

                        System.out.println("===========bubble================" + bubble_text);
                        System.out.println("===========bubble================" + bubble_link);


                        double roadPath_lat = Double.parseDouble(lat);
                        double roadPath_longitude = Double.parseDouble(longitude);


                        if (link.length() > 2) {

                            myTagList.add(new RoadLocationObj(roadPath_lat, roadPath_longitude, distance, ssteps, link, meta, flag_act, flag_deact, stepstonext, nextcity,
                                    city, citymsg, cityimg, disableimg, wc, zoom, bubble_text, bubble_link, auto_hide));
                        }


                        if (total_steps <= Integer.parseInt(ssteps)) {
                            whitePath_myRoadLocationsList.add(new RoadLocationObj(roadPath_lat, roadPath_longitude, distance, ssteps, link, meta, flag_act, flag_deact, stepstonext, nextcity,
                                    city, citymsg, cityimg, disableimg, wc, zoom, bubble_text, bubble_link, auto_hide));

                            if (!isFirstLocation) {

                                pendingRoadLocationsList.add(new RoadLocationObj(roadPath_lat, roadPath_longitude, distance, ssteps, link, meta, flag_act, flag_deact, stepstonext, nextcity,
                                        city, citymsg, cityimg, disableimg, wc, zoom, bubble_text, bubble_link, auto_hide));

                                isFirstLocation = true;
                                current_lat = roadPath_lat;
                                current_longi = roadPath_longitude;
                                current_distance = Double.parseDouble(zoom);
                                nestCity = nextcity;
                                nestCitySteps = stepstonext;
                                zooml = zoom;

                            }


                        } else {

                            pendingRoadLocationsList.add(new RoadLocationObj(roadPath_lat, roadPath_longitude, distance, ssteps, link, meta, flag_act, flag_deact, stepstonext, nextcity,
                                    city, citymsg, cityimg, disableimg, wc, zoom, bubble_text, bubble_link, auto_hide));
                            current_lat = roadPath_lat;
                            current_longi = roadPath_longitude;
                            //  zooml = zoom;
                            nestCity = nextcity;
                            nestCitySteps = stepstonext;

                            if (noof_day.equals("0")) {

                            } else {
                                String dy = "";
                                if (noof_day.length() == 1) {
                                    dy = "Day-0" + noof_day;
                                } else {
                                    dy = "Day-" + noof_day;
                                }

                            }

                        }
                    }


                    if (points.size() > 0) {
                        points.clear();
                    }
                    if (points2.size() > 0) {
                        points2.clear();
                    }
                    System.out.println("===1====points=======" + points.size());
                    System.out.println("===1====points2=======" + points2.size());
                    for (int x = 0; x < whitePath_myRoadLocationsList.size(); x++) {
                        RoadLocationObj ob = whitePath_myRoadLocationsList.get(x);
                        LatLng roadPath = new LatLng(ob.getLat(), ob.getLongitude());
                        points.add(roadPath);
                    }
                    for (int y = 0; y < pendingRoadLocationsList.size(); y++) {
                        RoadLocationObj ob = pendingRoadLocationsList.get(y);
                        LatLng roadPath = new LatLng(ob.getLat(), ob.getLongitude());
                        points2.add(roadPath);
                    }
                    String dy = "";
                    System.out.println("===11====whitePath_myRoadLocationsList=======" + whitePath_myRoadLocationsList.size());
                    System.out.println("===11====pendingRoadLocationsList=======" + pendingRoadLocationsList.size());
                    System.out.println("===11====points=======" + points.size());
                    System.out.println("===11====points2=======" + points2.size());
                    System.out.println("===11====myTagList=======" + myTagList.size());

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void Service_getChallengeJson_ServiceCall() {

        if (Utility.isInternetAvailable(MapChallengeActivity.this)) {

            if (pref.getIsFromPush()) {
                // Dont start activity ...
            } else {

                proDialog.show();
            }

            NService_getChallengeJson task = new NService_getChallengeJson();
            task.execute(new String[]{ApiClient.BASE_URL_live_v6});
        } else {
        }
    }

    private class NService_getChallengeJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            getChallengeJson();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (proDialog != null) {
                proDialog.dismiss();
            }

            writeToFile(cityJsonString);

            String dataJSONTreasure = getJSONTreasures(myTreasure);

            pref.createChallangeData(pref.getChallangeID(), white_lines, noof_day, cards, showpopup, service_checkpoints,
                    enabled_checkpoints, dataJSONTreasure, Integer.toString(total_steps), weekSteps, tip_icon, tip, tipheading, tip_header_1,
                    tip_header_2, tip_type, tip_meta);


            //   getLocationJson();
            NprocessLocalJsonClass task = new NprocessLocalJsonClass();
            task.execute(new String[]{ApiClient.BASE_URL_live_v6});


        }
    }


    private void getChallengeJson() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live_v6);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        System.out.println("...getChallengeJson.....chID..........." + chID);

        userid_ExistingUser = pref.getLoginUser().get("uid");
        String jsonStr =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"," +
                        "\"challenge_id\": \"" + pref.getChallangeID() + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "join_adventure_challenge"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));

        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);


            String sc = String.valueOf(response.getStatusLine().getStatusCode());


            if (sc.equals("200")) {

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

                int result = Integer.parseInt(res);

                if (result == 0) {
                    cityJsonString = jsonObj.optString("json").toString();

                } else {

                    serviceDataStatus = "99";
                }


            }
        } catch (ClientProtocolException e) {
            System.out.println("========Service Calling============7");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("========Service Calling============8");
            e.printStackTrace();
        }

    }


    private void writeToFile(String result) {
        try {
            FileOutputStream stream = MapChallengeActivity.this.openFileOutput(pref.getChallangeID() + ".json", MapChallengeActivity.this.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(stream);
            try {
                if (result != null) {
                    outputStreamWriter.write(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


}
