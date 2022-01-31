package com.ayubo.life.ayubolife.revamp.v1.fragment;

import static com.ayubo.life.ayubolife.payment.ConstantsKt.EXTRA_PAYMENT_META;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.actions.ActionButtonEventsManager;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.ContactInfoActivity;
import com.ayubo.life.ayubolife.activity.HealthViewActivityNew;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.ask.AskActivity;
import com.ayubo.life.ayubolife.body.WorkoutActivity;
import com.ayubo.life.ayubolife.book_videocall.BookVideoCallActivity;
import com.ayubo.life.ayubolife.challenges.NewCHallengeActivity;
import com.ayubo.life.ayubolife.channeling.activity.DashboardActivity;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.fragments.HomePage_Utility;
import com.ayubo.life.ayubolife.goals.AchivedGoal_Activity;
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity;
import com.ayubo.life.ayubolife.goals_extention.DashBoard_Activity;
import com.ayubo.life.ayubolife.goals_extention.ShareGoals_Activity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.home_group_view.GroupViewActivity;
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity;
import com.ayubo.life.ayubolife.huawei_hms.GoogleSupportServices;
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.title_clicked.PendingAnalysisOkActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.title_clicked.TitleClickedActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.welcome.JanashakthiWelcomeActivity;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.ayubo.life.ayubolife.login.LoginActivity_First;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity;
import com.ayubo.life.ayubolife.map_challenges.MapJoinChallenge_Activity;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.NewHomeMenuOneObj;
import com.ayubo.life.ayubolife.model.RoadLocationObj;
import com.ayubo.life.ayubolife.notification.model.NotiCountMainData;
import com.ayubo.life.ayubolife.notification.model.NotiCountMainResponse;
import com.ayubo.life.ayubolife.pojo.TokenMainResponse;
import com.ayubo.life.ayubolife.pojo.timeline.Post;
import com.ayubo.life.ayubolife.pojo.timeline.main.Banner;
import com.ayubo.life.ayubolife.pojo.timeline.main.Goal;
import com.ayubo.life.ayubolife.pojo.timeline.main.QuickLink;
import com.ayubo.life.ayubolife.pojo.timeline.main.TopTile;
import com.ayubo.life.ayubolife.post.activity.NativePostActivity;
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo;
import com.ayubo.life.ayubolife.programs.ProgramActivity;
import com.ayubo.life.ayubolife.qrcode.DecoderActivity;
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity;
import com.ayubo.life.ayubolife.timeline.AddComments_Activity;
import com.ayubo.life.ayubolife.timeline.LikedUsers_Activity;
import com.ayubo.life.ayubolife.timeline.OpenPostActivity;
import com.ayubo.life.ayubolife.timeline.TimelineGif_Activity;
import com.ayubo.life.ayubolife.timeline.TimelineImage_Activity;
import com.ayubo.life.ayubolife.timeline.TimelineVideo_Activity;
import com.ayubo.life.ayubolife.timeline.Timeline_NewPost_Activity;
import com.ayubo.life.ayubolife.timeline.pagination.PaginationAdapterCallback;
import com.ayubo.life.ayubolife.timeline.pagination.PaginationScrollListener;
import com.ayubo.life.ayubolife.utility.CONSTANTS;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Util;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.flavors.changes.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class V1NewFeedActivity extends BaseActivity implements PaginationAdapterCallback {
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    public static PaginationAdapter adapter;
    public static ArrayList<Post> timelinePostlist;
    LinearLayoutManager linearLayoutManager;
    RecyclerView main_recycler;
    //    private ProgressDialog progressDialog;
    Button btnRetry;
    TextView txtError;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    Context contextt;
    int offset = 10;
    String maxPostId;

    float intWidth;
    float hi;
    int intHeight;
    boolean isNeedShowProgress = false;
    boolean isNeedCallService = false;

    String appToken;
    ArrayList<NewHomeMenuOneObj> dataList = null;
    ArrayList<NewHomeMenuOneObj> serviceList = null;
    ImageView mainBack;
    List<TopTile> serviceTopTileList = null;
    List<QuickLink> serviceBottomTileList = null;
    LayoutInflater inflater;
    String challenge_id;
    String sDen;
    PrefManager pref;
    String marketplace_Token_status, marketplace_Token, userid_ExistingUser, hasToken;
    TextView textt;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    ArrayList<RoadLocationObj> myTagList = null;
    ArrayList<RoadLocationObj> myTreasure = null;
    String showpopup;
    boolean isClicked = true;
    ProgressAyubo feedLoadingBar;
    ActionButtonEventsManager actionButtonEventsManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v1_new_feed);

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        pref = new PrefManager(getBaseContext());
        userid_ExistingUser = pref.getLoginUser().get("uid");
        pref.setIsRunFirstTime("false");

        feedLoadingBar = (ProgressAyubo) findViewById(R.id.feedLoadingBar);

//        progressDialog = new ProgressDialog(getBaseContext());
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Please wait...");
        feedLoadingBar.setVisibility(View.VISIBLE);


        String name = pref.getLoginUser().get("name");
        hasToken = pref.getLoginUser().get("hashkey");
        actionButtonEventsManager = new ActionButtonEventsManager(getBaseContext());

        pref.setGoalCategory("");

        sDen = Utility.getDeviceDensityName();
        dataList = new ArrayList<NewHomeMenuOneObj>();
        serviceList = new ArrayList<NewHomeMenuOneObj>();

        inflatert = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        layoutt = inflatert.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(getBaseContext());
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);
//===================================================


        //===============================
        pref.setFamilyMemberId("0");
        //================================

        //======RECYCLE VIEW SETTINGS===============================================
        main_recycler = (RecyclerView) findViewById(R.id.main_recycler);
        mainBack = (ImageView) findViewById(R.id.mainBack);
        mainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        main_recycler.setHasFixedSize(false);

        //  FirebaseAnalytics Adding
        Bundle bTimeline = new Bundle();
        bTimeline.putString("name", "Timeline");
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Tab_opened", bTimeline);
        }

        serviceTopTileList = new ArrayList<>();
        serviceBottomTileList = new ArrayList<>();

        myTagList = new ArrayList<RoadLocationObj>();
        myTreasure = new ArrayList<RoadLocationObj>();


        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new PaginationAdapter(contextt, userid_ExistingUser);
        main_recycler.setLayoutManager(linearLayoutManager);
        main_recycler.setItemAnimator(new DefaultItemAnimator());
        main_recycler.setAdapter(adapter);

        main_recycler.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;

                if (Ram.getCurrentPage() != null) {
                    currentPage = Ram.getCurrentPage();
                    currentPage += 1;
                    loadNextPage(appToken);

                    Ram.setCurrentPage(currentPage);
                }


            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        appToken = pref.getUserToken();
        System.out.println("======================Loomen=======================" + appToken);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());
        intWidth = deviceScreenDimension.getDisplayWidth();
        hi = (intWidth / 4) * 3;
        intHeight = (int) hi;

        System.out.println("===========intWidth==================" + intWidth);
        System.out.println("===========intHeight==================" + intHeight);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage_Posts(appToken);
            }
        });

        DBString jsonDoctrDataObj = getCashDataByType(getBaseContext(), "timeline_firstpage_data");
        if (jsonDoctrDataObj == null) {
            isNeedShowProgress = true;

        } else {

            if ((jsonDoctrDataObj.getId() != null) && (jsonDoctrDataObj.getId().length() > 10)) {
                loadOfflineDisplay();
            }

        }

    }

    void showWorkoutScreen() {
        //  FirebaseAnalytics Adding
        Bundle bWorkout = new Bundle();
        bWorkout.putString("name", "Workout");
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Tab_opened", bWorkout);
        }
        if (isClicked) {
            isClicked = false;
            Intent in = new Intent(getBaseContext(), WorkoutActivity.class);
            startActivity(in);

        }
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

    public void getNotificationsData() {

        String appToken = pref.getUserToken();

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<NotiCountMainResponse> call = apiService.getNotificationCount(appToken);

        call.enqueue(new Callback<NotiCountMainResponse>() {
            @Override
            public void onResponse(@NonNull Call<NotiCountMainResponse> call, @NonNull Response<NotiCountMainResponse> response) {

                if (response.isSuccessful()) {
                    NotiCountMainResponse res = response.body();
                    NotiCountMainData data = res.getData();

                    if (data != null) {


                        int count = data.getUnreadCount();

                        if (count > 0) {
                            final Animation myAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
                            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.5, 20);
                            myAnim.setInterpolator(interpolator);

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

    public void addAllToArray(ArrayList<Post> moveResults) {
        if (timelinePostlist == null) {
            timelinePostlist = new ArrayList<>();
        }
        for (Post result : moveResults) {
            addOne(result);
        }
    }

    public void addOne(Post r) {
        timelinePostlist.add(r);
        // notifyItemInserted(movieResults.size() - 1);
    }


    @Override
    public void onResume() {
        super.onResume();
        isClicked = true;

        System.out.println("====Path=====================" + NewHomeWithSideMenuActivity.isStepUpdated);
        if (!NewHomeWithSideMenuActivity.isStepUpdated) {
            currentPage = 1;
            if (pref.getUserToken().length() > 25) {
                if (Utility.isInternetAvailable(getBaseContext())) {
                    loadMainService();
                } else {

                    Toast.makeText(getBaseContext(), "No active internet connection", Toast.LENGTH_LONG).show();

                }

            } else {
                isNeedShowProgress = true;
                if (Utility.isInternetAvailable(getBaseContext())) {

                    if (pref.getUserToken().length() > 50) {
                        loadMainService();
                    } else {
                        //is this need
                        getUserTokenFromServer(userid_ExistingUser);
                    }

                } else {
                    Toast.makeText(getBaseContext(), "No active internet connection", Toast.LENGTH_LONG).show();
                }
            }
        } else if ((timelinePostlist != null) && (timelinePostlist.size() > 0)) {
            currentPage = 1;
            System.out.println("====Path n 2=====================");
            if (pref.getIsNewPostAdded().equals("yes")) {

                adapter.clear();
                adapter.addAll(timelinePostlist);
                adapter.notifyDataSetChanged();
                main_recycler.setAdapter(adapter);
                pref.setIsNewPostAdded("");

            } else if (pref.getIsNewCommentAdded().equals("yes")) {
                if (AddComments_Activity.parentPosition != null) {
                    adapter.notifyItemChanged(AddComments_Activity.parentPosition);
                    pref.setIsNewCommentAdded("");
                }
            } else if (pref.getIsGoalAdded().equals("yes")) {
                //Get and Check goals and refresh timeline...
                loadMainServiceForGoals();
                pref.setIsGoalAdded("");

            } else if (pref.isAfterGFitEnabled().equals("true")) {
                System.out.println("=====isAfterGFitEnabled=========true============");
                if (serviceTopTileList != null) {
                    if (serviceTopTileList.size() > 1) {
                        TopTile ob = serviceTopTileList.get(1);
                        if (ob.getTitle().equals("GoogleFit")) {
                            serviceTopTileList.remove(1);
                        }
                    }
                }

                loadMainServiceForGoals();
                pref.setAfterGFitEnabled("false");

            } else if (pref.getPreviousPosition() != 0) {
//               Integer parentPosit= pref.getPreviousPosition();
//                adapter.notifyItemChanged(parentPosit);
                pref.setPreviousPosition(0);

            } else {
                if (!isNeedCallService) {
                    isNeedCallService = false;
                    DBString jsonDoctrDataObj = DBRequest.getCashDataByType(getBaseContext(), "timeline_firstpage_data");
                    if (jsonDoctrDataObj != null) {
                        long minits = Util.getTimestampAsMinits(jsonDoctrDataObj.getName());
                        if (minits >= 1) {
                            isNeedShowProgress = false;
                            loadMainService();
                        }
                    }
                } else {
                    isNeedShowProgress = true;
                    loadMainService();
                }
                System.out.println("======loadOfflineDisplay=====1=======================");


            }
        }


        pref.setIsFromJoinChallenge(false);


    }


    void loadOfflineDisplay() {
        if ((serviceTopTileList != null) && (serviceTopTileList.size() > 0)) {
            serviceTopTileList.clear();
        }
        if ((serviceBottomTileList != null) && (serviceBottomTileList.size() > 0)) {
            serviceBottomTileList.clear();
        }

        DBString jsonStrinTop_Obj = DBRequest.getCashDataByType(getBaseContext(), "timeline_top_data");
        DBString jsonStringBottom_Obj = DBRequest.getCashDataByType(getBaseContext(), "timeline_bottom_data");
        //    DBString jsonStringMainResData111_Obj= DBRequest.getCashDataByType(getBaseContext(),"goal_data");
        String jsonStrinTop = null;
        String jsonStringBottom = null;
        if (jsonStrinTop_Obj != null) {
            jsonStrinTop = jsonStrinTop_Obj.getId();
            JSONArray bigTileList = null;
            try {
                bigTileList = new JSONArray(jsonStrinTop);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (bigTileList != null) {
                for (int i = 0; i < bigTileList.length(); i++) {
                    JSONObject jsonMainNode3 = null;
                    try {
                        jsonMainNode3 = (JSONObject) bigTileList.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String title = jsonMainNode3.optString("title");
                    String sub_title = jsonMainNode3.optString("subTitle");
                    String action_text = jsonMainNode3.optString("actionText");
                    String tile_status = jsonMainNode3.optString("titleStatus");
                    String relatedId = jsonMainNode3.optString("relatedId");

                    String image = jsonMainNode3.optString("image");
                    String type = jsonMainNode3.optString("type");
                    String meta = jsonMainNode3.optString("meta");
                    String icon = jsonMainNode3.optString("icon");
                    //     String bg_image = jsonMainNode3.optString("bg_image");

                    serviceTopTileList.add(new TopTile(title, sub_title, action_text, image, type, meta, icon, "", "", "", tile_status, relatedId));
                }
            }
        }
        if (jsonStrinTop_Obj != null) {
            jsonStringBottom = jsonStringBottom_Obj.getId();
            JSONArray myListsAll2 = null;

            try {
                myListsAll2 = new JSONArray(jsonStringBottom);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (myListsAll2 != null) {
                for (int i = 0; i < myListsAll2.length(); i++) {
                    JSONObject jsonMainNode3 = null;
                    try {
                        jsonMainNode3 = (JSONObject) myListsAll2.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String title = jsonMainNode3.optString("title");
                    String title2 = jsonMainNode3.optString("title2");
                    String type = jsonMainNode3.optString("type");
                    String icon = jsonMainNode3.optString("icon");
                    String meta = jsonMainNode3.optString("meta");

                    serviceBottomTileList.add(new QuickLink(title, title2, type, icon, meta));
                }
            }
            //========================================================

        }

        // String jsonStringMainResData111=  jsonStringMainResData111_Obj.getId();


        //===============================================


        JSONArray myListsAll3 = null;
        if (myListsAll3 != null) {
            try {
                myListsAll3 = new JSONArray(jsonStringBottom);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < myListsAll3.length(); i++) {
                JSONObject jsonMainNode3 = null;
                try {
                    jsonMainNode3 = (JSONObject) myListsAll3.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String title = jsonMainNode3.optString("title");
                String title2 = jsonMainNode3.optString("title2");
                String type = jsonMainNode3.optString("type");
                String icon = jsonMainNode3.optString("icon");
                String meta = jsonMainNode3.optString("meta");
                serviceBottomTileList.add(new QuickLink(title, title2, type, icon, meta));
            }
        }
        //==================loadOfflineDisplay======================================

        DBString jsonStringData_Obj = DBRequest.getCashDataByType(getBaseContext(), "timeline_firstpage_data");

        if (jsonStringData_Obj == null) {
        } else {
            String jsonStringData = jsonStringData_Obj.getId();
            Gson gson = new GsonBuilder().create();
            try {
                com.ayubo.life.ayubolife.pojo.timeline.Data dataObj = gson.fromJson(jsonStringData, com.ayubo.life.ayubolife.pojo.timeline.Data.class);
                if (dataObj != null) {
                    if ((timelinePostlist != null) && (timelinePostlist.size() > 0)) {
                        timelinePostlist.clear();
                    }

                    timelinePostlist = (ArrayList<Post>) dataObj.getPosts();


                    Post p = timelinePostlist.get(0);
                    if (p.getPostId() == null) {

                    } else {
                        Post post = new Post();
                        timelinePostlist.add(0, post);
                    }

                    if (isValidList(timelinePostlist)) {
                        loadTimelineOffline(timelinePostlist);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void retryPageLoad() {
        loadNextPage(appToken);

    }

    public void showAlert_Deleted(Context c, String msg) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_ok_and_share, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        //  TextView lbl_alert_title = (TextView) layoutView.findViewById(R.id.lbl_alert_title);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);

        TextView btn_share = (TextView) layoutView.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                pref.setIsGoalAdded("yes");

                String my_goal_sponser_large_image = pref.getMyGoalData().get("my_goal_sponser_large_image");
                String my_goal_share_large_image = pref.getMyGoalData().get("my_goal_share_large_image");
                String goalName = pref.getMyGoalData().get("my_goal_name");

                Intent intent = new Intent(getBaseContext(), ShareGoals_Activity.class);
                intent.putExtra("sponserURl", my_goal_sponser_large_image);
                intent.putExtra("shareImageURl", my_goal_share_large_image);
                intent.putExtra("goalName", goalName);
                intent.putExtra("from", "achived");
                startActivity(intent);

            }
        });


        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_ok);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                //   finish();

            }
        });

        dialog.show();
    }

    private static final String PACKAGE_NAME = "com.google.android.apps.fitness";

    @CheckResult
    public boolean fitInstalled() {
        try {
            getPackageManager().getPackageInfo(PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    private void openAppStore() {
        Service_getMarketPlace_ServiceCall();
    }

    private void Service_getMarketPlace_ServiceCall() {
        if (Utility.isInternetAvailable(getBaseContext())) {
            try {
                Service_getMarketPlace task = new Service_getMarketPlace();
                task.execute(new String[]{ApiClient.BASE_URL_live});
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    private class Service_getMarketPlace extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            getMarketPlaceToken();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {


            if (!(marketplace_Token_status == null)) {
                //  userIdFromServiceAPI
                if (marketplace_Token_status.equals("99")) {


                    return;
                }
                if (marketplace_Token_status.equals("11")) {

                    return;
                }
                if (marketplace_Token_status.equals("0")) {

                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Store_clicked", null);
                    }

                    try {
                        String url = "https://store.ayubo.life/index.php/ldap/index/token?token=";
                        System.out.println("===========Market Place Token============" + marketplace_Token);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url + marketplace_Token));
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    return;
                }
            }
        }
    }

    private void getMarketPlaceToken() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
        //Post Data
        //Post Data
        marketplace_Token_status = "99";
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String email, password;

        //  email = lusername;
        //  password = lpassword;

        String jsonStr =
                "{" +

                        "\"userid\": \"" + userid_ExistingUser + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "getEstoreToken"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............*-----..........................." + nameValuePair.toString());
        //Encoding POST data
        HttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }
        //making POST request.
        try {
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("========Service Calling============0");

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

            if (res.equals("0")) {
                marketplace_Token = jsonObj.optString("token").toString();
                marketplace_Token_status = "0";
            } else {
                marketplace_Token_status = "99";
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    String castPojoListToString(Object mainResponse) {
        String strData = null;
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            strData = ow.writeValueAsString(mainResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return strData;
    }

    void processMainResults(com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse mainResponse) {

        List<Goal> goalList = mainResponse.getGoal();

        if ((serviceTopTileList != null) && (serviceTopTileList.size() > 0)) {
            serviceTopTileList.clear();
        }
        if ((serviceBottomTileList != null) && (serviceBottomTileList.size() > 0)) {
            serviceBottomTileList.clear();
        }

        serviceBottomTileList = mainResponse.getQuickLinks();
        serviceTopTileList = mainResponse.getTopTiles();

        if (serviceTopTileList != null) {
            serviceTopTileList.add(0, new TopTile("", "", "", "", "first", "", "", "", "", "", "", ""));
        }

        String deviceName = pref.getDeviceData().get("stepdevice");
        System.out.println("====Path n 4=====================" + deviceName);
        Log.i("Activity type ---0 ", deviceName);
        if (deviceName != null) {
            Log.i("Activity type ---0 ", deviceName);
            if (deviceName.equals("activity_AYUBO")) {
                Log.i("Activity type --- 1", deviceName);

                GoogleSupportServices googleSupportServices = new GoogleSupportServices(getBaseContext());

                if (googleSupportServices.isGooglePlayServicesAvailable()) {
                    if (pref.isGoogleFitEnabled().equals("false")) {
                        Log.i("Activity type --- 2", deviceName);
                        if (serviceTopTileList != null) {
                            Log.i("Activity type --- 3", deviceName);
                            System.out.println("====Path n 5=====================" + deviceName);
                            serviceTopTileList.add(1, new TopTile("to view challenges", "Connect GoogleFit", "Connect", MAIN_URL_APPS + "static/hh/google_fit_bg.png", "googlefit", "", MAIN_URL_APPS + "static/hh/connect.png", "", "", "", "", ""));
                        }
                    }
                }

            }
        }


        if ((serviceTopTileList != null) && serviceTopTileList.size() > 0) {
            String stingData = castPojoListToString(serviceTopTileList);
            DBRequest.updateDoctorData(getBaseContext(), stingData, "timeline_top_data");
        }
        if ((serviceBottomTileList != null) && (serviceBottomTileList.size() > 0)) {
            String stingBottData = castPojoListToString(serviceBottomTileList);
            DBRequest.updateDoctorData(getBaseContext(), stingBottData, "timeline_bottom_data");
        }

        if ((goalList != null) && (goalList != null)) {
            String stingGoalData = castPojoListToString(goalList);
            DBRequest.updateDoctorData(getBaseContext(), stingGoalData, "goal_data");

//         .................GOALS DATA PRODESSING........................
            for (int i = 0; i < goalList.size(); i++) {
                Goal objc = goalList.get(i);
                String goalId = objc.getUserGoalId();
                String sgoalStatus = objc.getIsAchieved();
                String sgoalName = objc.getGoalName();
                String sgoalImage = objc.getTileImageUrl();
                String slargeImage = objc.getLargeImageUrl();
                String shareImage = objc.getShareImageUrl();
                String sponserLImage = objc.getSponserImageUrl();
                String bgImage = objc.getBgImage();

                if (sgoalImage.contains("zoom_level")) {
                    sgoalImage = sgoalImage.replace("zoom_level", "xxxhdpi");
                }

                if (sgoalStatus.equals("pick")) {
                    pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, "Pick", "Pick", shareImage, sponserLImage, bgImage);
                } else if (sgoalStatus.equals("pending")) {
                    pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, "Pending", "Let's Do It", shareImage, sponserLImage, bgImage);
                } else if (sgoalStatus.equals("yes")) {
                    pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, "Completed", "Done", shareImage, sponserLImage, bgImage);
                }
            }
        }


        if ((pref.getUserToken() != null) && (pref.getUserToken().length() > 10)) {
            loadFirstPage_Posts(pref.getUserToken());
        } else {
            Toast.makeText(getBaseContext(), "Service token unavailable", Toast.LENGTH_LONG).show();
        }


    }

    void processMainResultsForGoals(com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse mainResponse) {

        List<Goal> goalList = mainResponse.getGoal();

        if ((serviceTopTileList != null) && (serviceTopTileList.size() > 0)) {
            serviceTopTileList.clear();
        }
        if ((serviceBottomTileList != null) && (serviceBottomTileList.size() > 0)) {
            serviceBottomTileList.clear();
        }

        serviceBottomTileList = mainResponse.getQuickLinks();
        serviceTopTileList = mainResponse.getTopTiles();


        serviceTopTileList.add(0, new TopTile("", "", "", "", "first", "", "", "", "", "", "", ""));


        String stingData = castPojoListToString(serviceTopTileList);
        String stingBottData = castPojoListToString(serviceBottomTileList);
        String stingGoalData = castPojoListToString(goalList);

        DBRequest.updateDoctorData(getBaseContext(), stingData, "timeline_top_data");
        DBRequest.updateDoctorData(getBaseContext(), stingBottData, "timeline_bottom_data");
        DBRequest.updateDoctorData(getBaseContext(), stingGoalData, "goal_data");


//         .................GOALS DATA PRODESSING........................
        for (int i = 0; i < goalList.size(); i++) {
            Goal objc = goalList.get(i);
            String goalId = objc.getUserGoalId();
            String sgoalStatus = objc.getIsAchieved();
            String sgoalName = objc.getGoalName();
            String sgoalImage = objc.getTileImageUrl();
            String slargeImage = objc.getLargeImageUrl();
            String buttonText = "";
            String title = "";
            String shareImage = objc.getShareImageUrl();
            String sponserLImage = objc.getSponserImageUrl();
            String bgImage = objc.getBgImage();

            if (sgoalImage != null) {
                if (sgoalImage.contains("zoom_level")) {
                    sgoalImage = sgoalImage.replace("zoom_level", "xxxhdpi");
                }
            } else {
                sgoalImage = "";
            }


            if (sgoalStatus.equals("pick")) {
                title = "Pick";
                buttonText = "Pick";
                pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, title, buttonText, shareImage, sponserLImage, bgImage);
            } else if (sgoalStatus.equals("pending")) {
                title = "Pending";
                buttonText = "Let's Do It";
                pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, title, buttonText, shareImage, sponserLImage, bgImage);
            } else if (sgoalStatus.equals("yes")) {
                title = "Completed";
                buttonText = "Done";
                pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, title, buttonText, shareImage, sponserLImage, bgImage);
            } else {
                title = "";
                buttonText = "";
                pref.setMyGoalData(goalId, sgoalName, sgoalImage, slargeImage, title, buttonText, shareImage, sponserLImage, bgImage);
            }
        }

        //==================loadOfflineDisplay======================================

        DBString jsonStringData_Obj = DBRequest.getCashDataByType(getBaseContext(), "timeline_firstpage_data");
        if (jsonStringData_Obj == null) {

        } else {
            String jsonStringData = jsonStringData_Obj.getId();
            Gson gson = new GsonBuilder().create();
            try {
                com.ayubo.life.ayubolife.pojo.timeline.Data dataObj = gson.fromJson(jsonStringData, com.ayubo.life.ayubolife.pojo.timeline.Data.class);
                if (dataObj != null) {
                    if ((timelinePostlist != null) && (timelinePostlist.size() > 0)) {
                        timelinePostlist.clear();
                    }

                    timelinePostlist = (ArrayList<Post>) dataObj.getPosts();


                    Post p = timelinePostlist.get(0);
                    if (p.getPostId() == null) {

                    } else {
                        Post post = new Post();
                        timelinePostlist.add(0, post);
                    }

                    if (isValidList(timelinePostlist)) {
                        loadTimelineOffline(timelinePostlist);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    void loadMainServiceForGoals() {

        String serviceName = "homePageTiles";
        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"" +
                        "}";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> call = apiService.callMainService(AppConfig.APP_BRANDING_ID, serviceName, "JSON", "JSON", jsonStr);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> call, Response<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> response) {


                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResult() == 401) {
                            Intent in = new Intent(getBaseContext(), LoginActivity_First.class);
                            startActivity(in);
                        }
                        if (response.body().getResult() == 0) {
                            // Code here .............
                            com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse mainResponse = response.body();

                            String show = "false";
                            Banner benner = mainResponse.getBanner();
                            if (benner.getShow()) {
                                show = "true";
                            }
                            String heading = benner.getHeading();
                            String text = benner.getText();
                            String button_text = benner.getButtonText();
                            String image = benner.getButtonText();
                            String action = benner.getAction();
                            String meta = benner.getMeta();

                            pref.createBanner(show, heading, text, button_text, image, action, meta);


                            processMainResultsForGoals(mainResponse);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> call, Throwable t) {

                t.printStackTrace();

            }
        });
    }

    void loadMainService() {

        if (isNeedShowProgress) {
//            progressDialog.show();
//            progressDialog.setMessage("Loading...");
            feedLoadingBar.setVisibility(View.VISIBLE);
        }

        System.out.println("====homePageTiles===================");

        String serviceName = "homePageTiles";
        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"" +
                        "}";

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> call = apiService.callMainService(AppConfig.APP_BRANDING_ID, serviceName, "JSON", "JSON", jsonStr);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> call, Response<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> response) {
//                if (progressDialog != null) {
//                    progressDialog.dismiss();
//                }

                feedLoadingBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResult() == 401) {
                            Intent in = new Intent(getBaseContext(), LoginActivity_First.class);
                            startActivity(in);
                        }
                        if (response.body().getResult() == 0) {
                            // Code here .............
                            com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse mainResponse = response.body();
                            System.out.println("====Path n 2kkkk=====================");

                            processMainResults(mainResponse);
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> call, Throwable t) {

//                if (progressDialog != null) {
//                    progressDialog.dismiss();
//                }
                feedLoadingBar.setVisibility(View.GONE);

            }
        });
    }

    void getUserTokenFromServer(String user_id) {

        ApiInterface apiService = ApiClient.getNewApiClient_AUTH().create(ApiInterface.class);
        Call<TokenMainResponse> call = apiService.getToken(AppConfig.APP_BRANDING_ID, appToken, user_id);
        call.enqueue(new Callback<TokenMainResponse>() {
            @Override
            public void onResponse(Call<TokenMainResponse> call, Response<TokenMainResponse> response) {

                if (response.isSuccessful()) {

                    TokenMainResponse tokenObj = response.body();
                    String loomenToken = tokenObj.getUserToken();
                    pref.setUserToken("Bearer " + loomenToken);
                    loadMainService();

                }
            }

            @Override
            public void onFailure(Call<TokenMainResponse> call, Throwable t) {
                System.out.print("");
            }
        });
    }

    void loadNextPage(String appToke) {

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);

        Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call = apiService.getAllPost(AppConfig.APP_BRANDING_ID, appToke, currentPage, offset, true, maxPostId);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Response<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> response) {

                com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline obj = response.body();


                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getResult() == 401) {
                            Intent in = new Intent(getBaseContext(), LoginActivity_First.class);
                            startActivity(in);
                        }
                        if (response.body().getResult() == 0) {
                            // Code here .............
                            com.ayubo.life.ayubolife.pojo.timeline.Data data = obj.getData();

                            if (data != null) {
                                ArrayList<Post> timelinePostList = (ArrayList<Post>) data.getPosts();

                                if ((timelinePostList != null) && (timelinePostList.size() > 0)) {
                                    addAllToArray(timelinePostList);


                                    TOTAL_PAGES = data.getPagination().getPages();
                                    currentPage = data.getPagination().getPage();
                                    maxPostId = data.getPagination().getMaxPostId();

                                    adapter.removeLoadingFooter();
                                    isLoading = false;

                                    System.out.println("============currentPage======================" + currentPage);
                                    adapter.addAll(timelinePostList);
                                    adapter.notifyDataSetChanged();


                                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                                    else isLastPage = true;
                                } else {
                                    adapter.removeLoadingFooter();
                                    isLoading = false;
                                    isLastPage = true;
                                }

                            }
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Throwable t) {
                System.out.print(t.toString());

            }
        });
    }

    void loadFirstPage_Posts(String appToke) {

        Ram.setCurrentPage(1);

//        if (isNeedShowProgress) {
//            progressDialog.show();
//            progressDialog.setMessage("Please wait...");
//        }

        feedLoadingBar.setVisibility(View.VISIBLE);

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call = apiService.getAllPost(AppConfig.APP_BRANDING_ID, appToke, currentPage, offset, true, maxPostId);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Response<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> response) {
//                if (progressDialog != null) {
//                    progressDialog.dismiss();
//                }

                feedLoadingBar.setVisibility(View.GONE);
                com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline obj = response.body();

//                if(code==401)
                if (response.isSuccessful()) {

                    if (response.body().getResult() == 401) {
                        Intent in = new Intent(getBaseContext(), LoginActivity_First.class);
                        startActivity(in);
                    }
                    if (response.body().getResult() == 0) {
                        final com.ayubo.life.ayubolife.pojo.timeline.Data timeline_firstpage_data = obj.getData();
                        if (timeline_firstpage_data != null) {
                            NewHomeWithSideMenuActivity.isStepUpdated = true;

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    //TODO your background code
                                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                                    try {
                                        String json = ow.writeValueAsString(timeline_firstpage_data);
                                        DBRequest.updateDoctorData(getBaseContext(), json, "timeline_firstpage_data");

                                        //   pref.setTimeLineFirstPage("a");

                                    } catch (JsonProcessingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            ArrayList<Post> timelinePostLi = (ArrayList<Post>) timeline_firstpage_data.getPosts();
                            timeline_firstpage_data.getPagination().getCount();

                            TOTAL_PAGES = timeline_firstpage_data.getPagination().getPages();
                            currentPage = timeline_firstpage_data.getPagination().getPage();
                            maxPostId = timeline_firstpage_data.getPagination().getMaxPostId();
                            if ((timelinePostlist != null) && (timelinePostlist.size() > 0)) {
                                timelinePostlist.clear();
                            }
                            timelinePostlist = timelinePostLi;
                            loadTimeline(timelinePostlist);
                        }
                    }


                }
                getNotificationsData();
            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Throwable t) {
//                if (progressDialog != null) {
//                    progressDialog.dismiss();
//                }
                feedLoadingBar.setVisibility(View.GONE);
                getNotificationsData();
                t.printStackTrace();

            }
        });


    }

    void loadTimelineOffline(ArrayList<Post> data) {

//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
        feedLoadingBar.setVisibility(View.GONE);

        adapter.clear();
        adapter.addAll(data);
        main_recycler.setAdapter(adapter);

        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }

    boolean isValidList(ArrayList<Post> timelinePostList) {
        boolean isValid = false;

        if ((timelinePostList != null) && (timelinePostList.size() > 0)) {
            isValid = true;
        }

        return isValid;
    }

    void loadTimeline(ArrayList<Post> timelinePostList) {


        if ((timelinePostList != null) && (timelinePostList.size() > 0)) {
            Post p = timelinePostList.get(0);
            if (p.getPostId() != null) {
                Post post = new Post();
                timelinePostList.add(0, post);
            }
            adapter.clear();
            adapter.addAll(timelinePostList);
        }


        adapter.notifyDataSetChanged();
        main_recycler.setAdapter(adapter);


        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;

    }

    public static DBString getCashDataByType(Context c, String type) {
        DBString cashedData = null;
        if (c != null) {
            DatabaseHandler db = new DatabaseHandler(c);
            try {
                cashedData = db.getCashDataByTypeFromDB(type);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return cashedData;
    }

    void sendALike(Integer postid, int status) {

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        String post_id = Integer.toString(postid);

        Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call = apiService.LikeAPost(AppConfig.APP_BRANDING_ID, appToken, post_id, status);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call, Response<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getResult() == 401) {
                            Intent in = new Intent(getBaseContext(), LoginActivity_First.class);
                            startActivity(in);
                        }
                        if (response.body().getResult() == 0) {
                            // Code here .............
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call, Throwable t) {
                System.out.print("");
            }
        });
    }

    void deleteAPost(final Post postobj) {

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);


        Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call = apiService.DeleteAPost(AppConfig.APP_BRANDING_ID, appToken, Integer.toString(postobj.getPostId()));
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call, Response<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getResult() == 401) {
                            Intent in = new Intent(getBaseContext(), LoginActivity_First.class);
                            startActivity(in);
                        }
                        if (response.body().getResult() == 0) {
                            // Code here .............
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call, Throwable t) {
                System.out.print("");
            }
        });
    }

    void onCommonViewClick(String meta) {
        Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
        intent.putExtra("URL", meta);
        startActivity(intent);
    }

    void clickedQuickLink(QuickLink objS) {

        //  FirebaseAnalytics Adding
        String tileName = objS.getTitle().toString();


        AppEventsLogger loggerFB = AppEventsLogger.newLogger(getBaseContext());
        loggerFB = AppEventsLogger.newLogger(getBaseContext());

        Bundle params = new Bundle();
        params.putString(AppConfig.FACEBOOK_EVENT_ID_SCREEN_NAME, AppConfig.FACEBOOK_EVENT_ID_HOME);
        params.putString(AppConfig.FACEBOOK_EVENT_ID_ACTION_NAME, tileName);
        params.putString(AppConfig.FACEBOOK_EVENT_ID_META, objS.getType());
        loggerFB.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, params);


        Bundle bPromocode_used = new Bundle();
        bPromocode_used.putString("link", tileName);
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Quick_link_clicked", bPromocode_used);

            if (tileName.equals("COVID-19")) {
                SplashScreen.firebaseAnalytics.logEvent("QL_COVID", null);
            }

            if (tileName.equals("Doctor")) {
                SplashScreen.firebaseAnalytics.logEvent("QL_DocService", null);
            }

            if (tileName.equals("Nutrition")) {
                SplashScreen.firebaseAnalytics.logEvent("QL_NutriService", null);
            }

            if (tileName.equals("Fitness")) {
                SplashScreen.firebaseAnalytics.logEvent("QL_FitnessService", null);
            }

        }

        if (objS.getType().equals("chanelling")) {
            Intent intent = new Intent(getBaseContext(), DashboardActivity.class);
            Ram.setMapSreenshot(null);
            intent.putExtra("activityName", "chanelling");
            startActivity(intent);
        } else if (objS.getType().equals("store_group")) {
            Intent intent = new Intent(getBaseContext(), GroupViewActivity.class);
            intent.putExtra(EXTRA_PAYMENT_META, objS.getMeta());
            startActivity(intent);
        } else if (objS.getType().equals("openbadgenative")) {
            onOpenBadgersClick(objS.getMeta());
        } else if (objS.getType().equals("programtimeline")) {
            onProgramNewDesignClick(objS.getMeta());
        } else if (objS.getType().equals("program")) {
            onProgramPostClick(objS.getMeta());
        } else if (objS.getType().equals("challenge")) {
            onMapChallangeClick(objS.getMeta());
        } else if (objS.getType().equals("chat")) {
            onAskClick(objS.getMeta());
        } else if (objS.getType().equals("leaderboard")) {
            onLeaderboardClick(objS.getMeta());
        } else if (objS.getType().equals("commonview")) {
            onCommonViewClick(objS.getMeta());
        } else if (objS.getType().equals("common")) {
            onCommonViewClick(objS.getMeta());
        } else if (objS.getType().equals("web")) {
            onBowserClick(objS.getMeta());
        } else if (objS.getType().equals("help")) {
            onHelpClick(objS.getMeta());
        } else if (objS.getType().equals("reports")) {
            onReportsClick(objS.getMeta());
        } else if (objS.getType().equals("goal")) {
            onGoalClick(objS.getMeta());
        } else if (objS.getType().equals("videocall")) {
            onVideoCallClick(objS.getMeta());
        } else if (objS.getType().equals("exp_chanelling")) {
            startActivity(new Intent(getBaseContext(), BookVideoCallActivity.class));
        } else if (objS.getType().equals("channeling")) {
            onButtonChannelingClick(objS.getMeta());
        } else if (objS.getType().equals("janashakthiwelcome")) {
            onJanashakthiWelcomeClick(objS.getMeta());
        } else if (objS.getType().equals("janashakthireports")) {
            onJanashakthiReportsClick(objS.getMeta());
        } else if (objS.getType().equals("dynamicquestion")) {
            onDyanamicQuestionClick(objS.getMeta());
        } else if (objS.getType().equals("post")) {
            onPostClick(objS.getMeta());
        } else if (objS.getType().equals("native_post")) {
            onNativePostClick(objS.getMeta());
        } else if (objS.getType().equals("native_post_json")) {
            onJSONNativePostClick(objS.getMeta());
        } else if (objS.getType().equals("doc_chanelling")) {
            Intent intent = new Intent(getBaseContext(), MyDoctor_Activity.class);
            Ram.setMapSreenshot(null);
            intent.putExtra("activityName", "my_doctor");
            startActivity(intent);
        } else if (objS.getType().equals("contact_us")) {
            Intent intent = new Intent(getBaseContext(), ContactInfoActivity.class);
            Ram.setMapSreenshot(null);
            startActivity(intent);
        } else if (objS.getType().equals("vid_doctor_view")) {
            Intent intent = new Intent(this, MyDoctorLocations_Activity.class);
            intent.putExtra("doctor_id", objS.getMeta());
            startActivity(intent);
        } else if (objS.getType().equals("doc_chat")) {
            AyuboChatActivity.Companion.startActivity(this, objS.getMeta(), false, false, "");
        } else if (objS.getType().equals("doctor_chat")) {
            AyuboChatActivity.Companion.startActivity(this, objS.getMeta(), false, false, "");
        } else if (objS.getType().equals("phy_chanelling")) {
            Intent intent = new Intent(getBaseContext(), MyDoctor_Activity.class);
            Ram.setMapSreenshot(null);
            intent.putExtra("activityName", "phy_chanelling");
            startActivity(intent);
        } else if (objS.getType().equals("workout")) {
            showWorkoutScreen();
        } else if (objS.getType().equals("medicine")) {
            Intent intent = new Intent(getBaseContext(), Medicine_ViewActivity.class);
            startActivity(intent);
        } else if (objS.getType().equals("challenge")) {
            Intent intent = new Intent(getBaseContext(), NewCHallengeActivity.class);
            startActivity(intent);
        } else if (objS.getType().equals("reports")) {
            Intent intent = new Intent(getBaseContext(), ReportDetailsActivity.class);
            intent.putExtra("data", "all");
            Ram.setReportsType("fromHome");
            startActivity(intent);
        } else if (objS.getType().equals("healthview")) {
            Intent intent = new Intent(getBaseContext(), HealthViewActivityNew.class);
            startActivity(intent);
        } else if (objS.getType().equals("decoder")) {
            Intent intent = new Intent(getBaseContext(), DecoderActivity.class);
            startActivity(intent);
        } else if (objS.getType().equals("store")) {
            openAppStore();
        } else if (objS.getType().equals("prescription")) {
            Intent intent = new Intent(getBaseContext(), Medicine_ViewActivity.class);
            startActivity(intent);
        } else if (objS.getType().equals("commonview")) {
            Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
            intent.putExtra("URL", objS.getMeta());
            startActivity(intent);
        } else if (objS.getType().equals("common")) {
            Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
            intent.putExtra("URL", objS.getMeta());
            startActivity(intent);
        } else if (objS.getType().equals("native_post")) {
            onNativePostClick(objS.getMeta());
        } else if (objS.getType().equals("native_post_json")) {
            onJSONNativePostClick(objS.getMeta());
        } else if (objS.getType().equals("web")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(objS.getMeta()));
            startActivity(browserIntent);
        } else {
            processAction(objS.getType(), objS.getMeta());
        }
    }


    private void callOnPickAGoal(TopTile object) {

        if (object != null) {
            String type = object.getType();
            String tileName;

            if (type.equals("first")) {
                tileName = "Pick a Goal";
            } else if (type.equals("googlefit")) {
                tileName = "Connect GoogleFit";
            } else {
                tileName = type;
            }

            AppEventsLogger loggerFB = AppEventsLogger.newLogger(getBaseContext());
            loggerFB = AppEventsLogger.newLogger(getBaseContext());

            Bundle params = new Bundle();
            params.putString(AppConfig.FACEBOOK_EVENT_ID_SCREEN_NAME, AppConfig.FACEBOOK_EVENT_ID_HOME);
            params.putString(AppConfig.FACEBOOK_EVENT_ID_ACTION_NAME, tileName);
            params.putString(AppConfig.FACEBOOK_EVENT_ID_META, object.getMeta());

            loggerFB.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, params);

            System.out.println(object.getMeta());
            System.out.println(object.getMeta());

            //object.getMeta()
            pref.setRelateID(object.getMeta());

            //  FirebaseAnalytics Adding
            Bundle bTileParams = new Bundle();
            bTileParams.putString("tile", object.getTitle());
            if (SplashScreen.firebaseAnalytics != null) {
                SplashScreen.firebaseAnalytics.logEvent("Timeline_tile_clicked", bTileParams);

                if (tileName.equals("Pick a Goal")) {
                    SplashScreen.firebaseAnalytics.logEvent("Tdy_PickGoal", null);
                }

                if (tileName.equals("wellness_dashboard")) {
                    SplashScreen.firebaseAnalytics.logEvent("Tdy_WellnessDash", null);
                }
            }


            if (type.equals("first")) {

                String status = pref.getMyGoalData().get("my_goal_status");

                if (status.equals("Pending")) {
                    Intent intent = new Intent(getBaseContext(), AchivedGoal_Activity.class);
                    startActivity(intent);
                } else if (status.equals("Pick")) {

                    Intent intent = new Intent(getBaseContext(), PickAGoal_Activity.class);
                    startActivity(intent);
                } else if (status.equals("Completed")) {
                    showAlert_Deleted(getBaseContext(), "This goal has been achieved for the day. Please pick another goal tomorrow");
                }
            } else if (type.equals("googlefit")) {
            } else if (type.equals("wellness_dashboard")) {
                Intent intent = new Intent(getBaseContext(), DashBoard_Activity.class);
                System.out.println(object);
                System.out.println(object.getRelatedId());
                System.out.println(object.getRelatedId());
                intent.putExtra("meta", object.getRelatedId());
                startActivity(intent);
            } else if (type.equals("join_adv_challenge")) {
                Intent intent = new Intent(getBaseContext(), MapJoinChallenge_Activity.class);
                System.out.println("=============challenge_id====================" + object.getMeta());
                intent.putExtra("challenge_id", object.getMeta());
                startActivity(intent);
            } else if (type.equals("view_adv_challenge")) {
                pref.setIsFromPush(false);
                challenge_id = object.getMeta();
                System.out.println("=============challenge_id=====From HOME===============" + object.getMeta());

                onMapChallangeClick(challenge_id);
            } else if (type.equals("challenge")) {
                onMapChallangeClick(object.getMeta());
            } else if (type.equals("view_spon_adv_challenge")) {
                pref.setIsFromPush(false);
                challenge_id = object.getMeta();
                showpopup = "showpopup";

            } else if (type.equals("post")) {
                Intent intent = new Intent(getBaseContext(), OpenPostActivity.class);
                intent.putExtra("postID", object.getMeta());
                startActivity(intent);
            } else if (type.equals("doctor_chat")) {
                String docId = object.getMeta();
                AyuboChatActivity.Companion.startActivity(this, docId, false, false, "");
            } else if (type.equals("program_timeline")) {
                Intent in = new Intent(getBaseContext(), ProgramActivity.class);
                in.putExtra("meta", object.getRelatedId());
                startActivity(in);
            } else if (type.equals("dynamicquestion")) {
                pref.setRelateID(object.getRelatedId());
                pref.setIsJanashakthiDyanamic(true);
                Intent in = new Intent(getBaseContext(), IntroActivity.class);
                in.putExtra("meta", object.getRelatedId());
                startActivity(in);
            } else if (type.equals("janashakthi_policy")) {


                pref.setRelateID(object.getRelatedId());


                if (object.getTitleStatus().equals("welcome")) {
                    pref.setIsJanashakthiWelcomee(true);
                    Intent intent = new Intent(getBaseContext(), JanashakthiWelcomeActivity.class);
                    startActivity(intent);
                }
                if (object.getTitleStatus().equals("pending_analysis")) {
                    Intent intent = new Intent(getBaseContext(), PendingAnalysisOkActivity.class);
                    startActivity(intent);
                }
                if (object.getTitleStatus().equals("pending_follow_up")) {
                    Intent intent = new Intent(getBaseContext(), PendingAnalysisOkActivity.class);
                    startActivity(intent);
                }
                if (object.getTitleStatus().equals("active")) {
                    Intent in = new Intent(getBaseContext(), ProgramActivity.class);
                    in.putExtra("meta", object.getRelatedId());
                    startActivity(in);
                }
                if (object.getTitleStatus().equals("user_action")) {
                    Intent intent = new Intent(getBaseContext(), TitleClickedActivity.class);
                    intent.putExtra("meta", object.getMeta());
                    startActivity(intent);
                }
            } else if (type.equals("doc_chat")) {
                String docId = object.getMeta();
                AyuboChatActivity.Companion.startActivity(this, docId, false, false, "");
            } else if (type.equals("native_post_json")) {
                String meta = object.getMeta();
                onJSONNativePostClick(meta);
            } else if (type.equals("native_post")) {
                String meta = object.getMeta();
                onNativePostClick(meta);
            } else if (type.equals("common")) {
                Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", object.getMeta());
                startActivity(intent);
            } else if (type.equals("commonview")) {
                Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", object.getMeta());
                startActivity(intent);
            } else if (type.equals("web")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(object.getMeta()));
                startActivity(browserIntent);
            }

        }
    }


    void onNativePostClick(String meta) {
        Intent intent = new Intent(getBaseContext(), NativePostActivity.class);
        intent.putExtra("meta", meta);
        startActivity(intent);
    }

    void onOpenBadgersClick(String meta) {
        if (meta != null) {
            Intent intent = new Intent(getBaseContext(), Badges_Activity.class);
            startActivity(intent);
        }
    }

    void onGoalClick(String meta) {
        PrefManager prefManager = new PrefManager(getBaseContext());
        String status = prefManager.getMyGoalData().get("my_goal_status");

        if (status.equals("Pending")) {
            Intent intent = new Intent(getBaseContext(), AchivedGoal_Activity.class);
            startActivity(intent);
        }
        if (status.equals("Pick")) {


            Intent intent = new Intent(getBaseContext(), PickAGoal_Activity.class);
            startActivity(intent);
        }
        if (status.equals("Completed")) {
            HomePage_Utility serviceObj = new HomePage_Utility(getBaseContext());
            serviceObj.showAlert_Deleted(getBaseContext(), "This goal has been achieved for the day. Please pick another goal tomorrow");
        }

    }

    void onReportsClick(String meta) {
        Intent intent = new Intent(getBaseContext(), ReportDetailsActivity.class);
        intent.putExtra("data", "all");
        Ram.setReportsType("fromHome");
        startActivity(intent);
    }

    void onHelpClick(String meta) {
        Intent intent = new Intent(getBaseContext(), HelpFeedbackActivity.class);
        intent.putExtra("activityName", "myExperts");
        startActivity(intent);
    }

    void onBowserClick(String meta) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(meta));
        startActivity(browserIntent);
    }

    void onAskClick(String meta) {
        if (meta == null) {
            Intent intent = new Intent(getBaseContext(), AskActivity.class);
            startActivity(intent);
        } else if (meta.equals("")) {
            Intent intent = new Intent(getBaseContext(), AskActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getBaseContext(), AyuboChatActivity.class);
            intent.putExtra("doctorId", meta);
            intent.putExtra("isAppointmentHistory", false);
            startActivity(intent);
        }
    }

    public void showAlert_Add(Context c, String title, String msg, final Post post) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false);
        builder.setView(layoutView);
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);
        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                adapter.remove(post);
                //remove(position);

                deleteAPost(post);

            }
        });
        dialog.show();
    }

    class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        boolean shouldShow_TitlePanel = true;
        boolean shouldShow_ContentPanel = true;
        boolean shouldShow_UserInteractionPanel = true;

        boolean shouldShow_MediaPanel = false;
        boolean shouldShow_ProgramPanel = false;
        boolean shouldShow_OverlayImage = false;
        boolean shouldShow_LinkPreviewPanel = false;

        // View Types
        private static final int ITEM = 0;
        private static final int LOADING = 1;
        private static final int HEADER = 2;
        private static final int BANNER = 4;
        private List<Post> movieResults;
        private Context context;

        private boolean isLoadingAdded = false;
        private boolean retryPageLoad = false;
        String userid_ExistingUser, cellType;
        private PaginationAdapterCallback mCallback;

        private String errorMsg;

        public PaginationAdapter(Context context, String userid_ExistingUser) {
            this.context = context;
            this.mCallback = (PaginationAdapterCallback) context;
            movieResults = new ArrayList<>();
            this.userid_ExistingUser = userid_ExistingUser;

            // SETUP TIMELINE  e====================


        }

        public List<Post> getMovies() {
            return movieResults;
        }

        public void setMovies(List<Post> movieResults) {
            this.movieResults = movieResults;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());


            switch (viewType) {
                case ITEM:
                    View viewItem = inflater.inflate(R.layout.native_timeline_cell, parent, false);
                    viewHolder = new PostCell(viewItem);
                    break;
                case BANNER:
                    View viewBanner = inflater.inflate(R.layout.new_banner_cell, parent, false);
                    viewHolder = new BannerVH(viewBanner);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                    viewHolder = new LoadingVH(viewLoading);
                    break;
                case HEADER:

                    if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
                        View viewHero = inflater.inflate(R.layout.new_home_widgets_layout_lifeplus, parent, false);
                        viewHolder = new ServiceMenuView(viewHero);
                        break;
                    } else {
                        //  View viewHero = inflater.inflate(R.layout.new_home_widgets_layout_lifeplus, parent, false);

                        View viewHero = inflater.inflate(R.layout.new_home_widgets_layout, parent, false);
                        viewHolder = new ServiceMenuView(viewHero);
                        break;
                    }

            }
            return viewHolder;
        }

        public void remove(int position) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holde, @SuppressLint("RecyclerView") final int position) {
            switch (getItemViewType(position)) {

                case HEADER:
                    //loadDynamicMenus();
                    final ServiceMenuView heroVh = (ServiceMenuView) holde;

                    heroVh.txt_whats_on_mind.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getBaseContext(), Timeline_NewPost_Activity.class);
                            startActivity(intent);
                        }
                    });

                    heroVh.whatson_content_camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getBaseContext(), Timeline_NewPost_Activity.class);
                            startActivity(intent);
                        }
                    });

                    String imagepath_db = pref.getLoginUser().get("image_path");


                    String burlImg = "";
                    if (imagepath_db.contains(".jpg") || imagepath_db.contains(".jpeg") || imagepath_db.contains(".png")) {
                        burlImg = imagepath_db;
                    } else {


                        if (!imagepath_db.equals("") && !imagepath_db.contains(ApiClient.MAIN_URL_LIVE_HAPPY)) {
                            burlImg = MAIN_URL_LIVE_HAPPY + imagepath_db;

                        } else {
                            burlImg = imagepath_db;
                        }


                    }
                    if (burlImg == null) {
                        heroVh.profilePicture.setVisibility(View.GONE);
                    } else {
                        heroVh.profilePicture.setVisibility(View.VISIBLE);
                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).transforms(new CircleTransform(getBaseContext()));
                        Glide.with(getBaseContext()).load(burlImg)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(heroVh.profilePicture);
                    }

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());
                    int height = deviceScreenDimension.getDisplayHeight();
                    int width = deviceScreenDimension.getDisplayWidth();

                    int singleTileWidth = width / 4;
                    singleTileWidth = singleTileWidth + singleTileWidth / 4;
                    //==========================================================
                    // LOADING SERVICES MENU===================================
                    if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {


                        int numberDisplay = 3;
                        if (serviceTopTileList.size() < 3) {
                            numberDisplay = serviceTopTileList.size();
                        }

                        for (int num = 0; num < numberDisplay; num++) {

                            TopTile serviceObject = serviceTopTileList.get(num);
                            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                            // View mainSingleTilesView = inflater.inflate(R.layout.newdisign_menu_item, null);
                            View mainSingleTilesView = null;

                            if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
                                mainSingleTilesView = inflater.inflate(R.layout.home_toptiles_lifeplus, null);

                            } else {
                                mainSingleTilesView = inflater.inflate(R.layout.card_timeline_new_design_goals, null);
                            }


                            View space = inflater.inflate(R.layout.row_item, null);

                            //final CardView cardview_top_tiles =mainSingleTilesView.findViewById(R.id.cardview_top_tiles);
                            final LinearLayout cardview_top_tiles = mainSingleTilesView.findViewById(R.id.cardview_top_tiles);

                            RelativeLayout image_container = (RelativeLayout) mainSingleTilesView.findViewById(R.id.image_container);
                            TextView txt_desc1 = (TextView) mainSingleTilesView.findViewById(R.id.txt_desc2);
                            TextView txt_desc2 = (TextView) mainSingleTilesView.findViewById(R.id.txt_desc1);

                            TextView txt_stepcount = (TextView) mainSingleTilesView.findViewById(R.id.txt_stepcount);
                            TextView txt_steps = (TextView) mainSingleTilesView.findViewById(R.id.txt_steps);
                            //  TextView btn_action = (TextView) mainSingleTilesView.findViewById(R.id.btn_action);

                            // LinearLayout btn_action_lay = (LinearLayout) mainSingleTilesView.findViewById(R.id.btn_action_lay);
                            ImageView top_tile_bg = (ImageView) mainSingleTilesView.findViewById(R.id.img_quicl_link_bg);
                            ImageView img_goal_achive_icon = (ImageView) mainSingleTilesView.findViewById(R.id.img_goal_achive_icon);

                            cardview_top_tiles.setTag(serviceObject);
                            txt_desc1.setTag(serviceObject);
                            txt_desc2.setTag(serviceObject);


                            cardview_top_tiles.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    TopTile obj = (TopTile) v.getTag();
                                    callOnPickAGoal(obj);
                                }
                            });

                            txt_desc1.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    TopTile obj = (TopTile) v.getTag();
                                    callOnPickAGoal(obj);
                                }
                            });
                            txt_desc2.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    TopTile obj = (TopTile) v.getTag();
                                    callOnPickAGoal(obj);
                                }
                            });


                            String smallIcon = serviceObject.getIcon();
                            String mainImage = serviceObject.getImage();

                            if (serviceObject.getImage() != null) {
                                if (mainImage.contains("zoom_level")) {
                                    mainImage = mainImage.replace("zoom_level", "xxxhdpi");
                                } else if (mainImage.contains("zoom")) {
                                    mainImage = mainImage.replace("zoom", "xxxhdpi");
                                }
                            } else {
                                mainImage = "";
                            }

                            if (serviceObject.getIcon() != null) {
                                if (smallIcon.contains("zoom_level")) {
                                    smallIcon = smallIcon.replace("zoom_level", "xxxhdpi");
                                } else if (smallIcon.contains("zoom")) {
                                    smallIcon = smallIcon.replace("zoom", "xxxhdpi");
                                }
                            } else {
                                smallIcon = "";
                            }


                            int imageSize = Utility.getImageSizeFor_DeviceDensitySize(200);


                            intWidth = deviceScreenDimension.getDisplayWidth();

                            String text = serviceObject.getTitle();

                            System.out.println("==============text================" + text);
                            float tileWidthf = 0;
                            int tileWidth = 0;
                            float tileHeigthf = 0;
                            float tileHeigth = 0;
                            int myUnit = 0;
                            float myUnitf = intWidth / 720;

                            //  myUnit = (int) myUnitf;

                            tileHeigthf = myUnitf * 190;
                            tileHeigth = (int) tileHeigthf;

                            if (text.length() < 15) {
                                tileWidthf = myUnitf * 290;
                            } else if (text.length() < 20) {
                                tileWidthf = myUnitf * 390;
                            } else if (text.length() > 20) {
                                tileWidthf = myUnitf * 400;
                            }
                            tileWidth = (int) tileWidthf;


                            RequestOptions requestOptionsSamll = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(getBaseContext()).load(smallIcon)
                                    .apply(requestOptionsSamll)
                                    .into(img_goal_achive_icon);


                            String my_goal_name = null;
                            if (num != 0) {
                                my_goal_name = serviceObject.getTitle();
                            } else {
                                my_goal_name = pref.getMyGoalData().get("my_goal_name");
                                if (my_goal_name.equals("")) {
                                    my_goal_name = "Pick a goal";
                                }
                            }

                            if (my_goal_name.contains(" ")) {

                                String[] splited = my_goal_name.split("\\s+");

                                String secondName = null;
                                String ste1 = splited[0];

                                boolean isTwoWords = false;
                                if (splited.length > 2) {
                                    ste1 = splited[0] + " " + splited[1];
                                    if (ste1.length() < 18) {
                                        isTwoWords = true;
                                    } else {
                                        ste1 = splited[0];
                                    }
                                }
                                txt_desc1.setText(ste1);


                                if (splited.length == 2) {
                                    secondName = splited[1];
                                } else if (splited.length == 3) {
                                    if (isTwoWords) {
                                        secondName = splited[2];
                                    } else {
                                        secondName = splited[1] + " " + splited[2];
                                    }

                                } else if (splited.length == 4) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3];
                                    }

                                } else if (splited.length == 5) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4];
                                    }

                                } else if (splited.length == 6) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5];
                                    }

                                } else if (splited.length == 7) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6];
                                    }
                                } else if (splited.length == 8) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7];
                                    }
                                } else if (splited.length == 9) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8];
                                    }

                                } else if (splited.length == 10) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9];
                                    }

                                } else if (splited.length == 11) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }

                                } else if (splited.length == 12) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 13) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 14) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 15) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 16) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 17) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 18) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 19) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 20) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 21) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                }


                                //  System.out.println("==========secondName=============" + secondName);
                                if (secondName != null) {
                                    if (secondName.length() >= 20) {
                                        secondName = secondName.substring(0, Math.min(secondName.length(), 15));
                                        secondName = secondName + "...";
                                    }
                                    txt_desc2.setText(secondName);
                                }

                                txt_desc1.setText(serviceObject.getSubTitle());
                                txt_desc2.setText(serviceObject.getTitle());

                                if (serviceObject.getType().equals("wellness_dashboard")) {
                                    txt_stepcount.setVisibility(View.INVISIBLE);
                                    txt_steps.setVisibility(View.INVISIBLE);

                                    String step = pref.getUserData().get("steps");
                                    txt_stepcount.setText(step);
                                } else {
                                    txt_stepcount.setVisibility(View.INVISIBLE);
                                    txt_steps.setVisibility(View.INVISIBLE);
                                }


                            } else {
                                txt_desc1.setText(my_goal_name);
                                txt_desc2.setText("");
                            }
                            String te = serviceObject.getActionText();

                            if (num == 0) {

                                String goalStatus = pref.getMyGoalData().get("my_goal_status");
                                String goalImage = pref.getMyGoalData().get("my_goal_image");
                                String goalBgImage = pref.getMyGoalData().get("my_goal_bg");

                                my_goal_name = pref.getMyGoalData().get("my_goal_name");


                                txt_desc1.setText(serviceObject.getSubTitle());
                                txt_desc2.setText(serviceObject.getTitle());


                                RequestOptions requestOption = new RequestOptions().override(tileWidth, (int) tileHeigth).diskCacheStrategy(DiskCacheStrategy.ALL);
                                //  goalImage

                                if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
                                    Glide.with(getBaseContext()).load(goalBgImage)
                                            .apply(requestOptionsSamll)
                                            .into(img_goal_achive_icon);
                                } else {
                                    top_tile_bg.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            TopTile obj = (TopTile) v.getTag();
                                            callOnPickAGoal(obj);
                                        }
                                    });
                                    top_tile_bg.setTag(serviceObject);
                                    Glide.with(getBaseContext()).load(goalBgImage)
                                            .apply(requestOption)
                                            .into(top_tile_bg);

                                    Glide.with(getBaseContext()).load(goalImage)
                                            .apply(requestOptionsSamll)
                                            .into(img_goal_achive_icon);
                                }


                                if (goalStatus == null) {


                                    if (Constants.type == Constants.Type.LIFEPLUS) {
                                        Glide.with(getBaseContext()).load(goalImage)
                                                .apply(requestOptionsSamll)
                                                .into(img_goal_achive_icon);
                                    } else {
                                        Glide.with(getBaseContext()).load(goalBgImage)
                                                .apply(requestOption)
                                                .into(top_tile_bg);

                                        Glide.with(getBaseContext()).load(goalImage)
                                                .apply(requestOptionsSamll)
                                                .into(img_goal_achive_icon);
                                    }


                                    if (goalStatus.equals("Pending")) {
                                        //   btn_action.setTextColor(Color.parseColor("#ff860b"));
                                        txt_desc1.setText("Let's Do It");

                                        System.out.println("==============Pending===============" + te);
                                    } else if (goalStatus.equals("Pick")) {
                                        txt_desc2.setText("Pick a");
                                        txt_desc1.setText("goal");
                                    } else if (goalStatus.equals("Completed")) {
                                        System.out.println("==============Completed===============" + te);
                                        txt_desc1.setText("Done");
                                        //  txt_desc1.setTextColor(Color.parseColor("#45D100"));
                                    }
//
//                                }//
                                } else {

                                    if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
                                        Glide.with(getBaseContext()).load(goalBgImage)
                                                .apply(requestOptionsSamll)
                                                .into(img_goal_achive_icon);
                                    } else {
                                        Glide.with(getBaseContext()).load(goalBgImage)
                                                .apply(requestOption)
                                                .into(top_tile_bg);

                                        Glide.with(getBaseContext()).load(goalImage)
                                                .apply(requestOptionsSamll)
                                                .into(img_goal_achive_icon);
                                    }


                                    if (goalStatus.equals("Pending")) {
                                        txt_desc2.setText(my_goal_name);
                                        txt_desc1.setText("Let's Do It");
                                    } else if (goalStatus.equals("Pick")) {
                                        txt_desc2.setText("Pick a");
                                        txt_desc1.setText("goal");
                                    } else if (goalStatus.equals("Completed")) {
                                        txt_desc2.setText(my_goal_name);
                                        txt_desc1.setText("Done");
                                    }


                                }
                            } else {
                                txt_desc1.setText(serviceObject.getSubTitle());
                                txt_desc2.setText(serviceObject.getTitle());

                                if (serviceObject.getType().equals("wellness_dashboard")) {
                                    txt_stepcount.setVisibility(View.INVISIBLE);
                                    txt_steps.setVisibility(View.INVISIBLE);
                                    String ste = pref.getUserData().get("steps");
                                    txt_stepcount.setText(ste);
                                } else {
                                    txt_stepcount.setVisibility(View.INVISIBLE);
                                    txt_steps.setVisibility(View.INVISIBLE);
                                }

                                RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(tileWidth, (int) tileHeigth);

                                if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
                                    Glide.with(getBaseContext()).load(mainImage)
                                            .apply(requestOptions)
                                            .into(img_goal_achive_icon);
                                } else {
                                    Glide.with(getBaseContext()).load(mainImage)
                                            .apply(requestOptions)
                                            .into(top_tile_bg);
                                }


                            }


                        }

                    } else {

                        for (int num = 0; num < 3; num++) {
                            // SHESHELLS HOME WIDGET LOADING .....

                            TopTile serviceObject = serviceTopTileList.get(num);
                            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                            // View mainSingleTilesView = inflater.inflate(R.layout.newdisign_menu_item, null);
                            View mainSingleTilesView = null;

                            if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
                                mainSingleTilesView = inflater.inflate(R.layout.home_toptiles_lifeplus, null);

                            } else {
                                mainSingleTilesView = inflater.inflate(R.layout.card_timeline_new_design_goals, null);
                            }


                            View space = inflater.inflate(R.layout.row_item, null);

                            final CardView cardview_top_tiles = mainSingleTilesView.findViewById(R.id.cardview_top_tiles);
                            //final LinearLayout cardview_top_tiles = mainSingleTilesView.findViewById(R.id.cardview_top_tiles);

                            RelativeLayout image_container = (RelativeLayout) mainSingleTilesView.findViewById(R.id.image_container);
                            TextView txt_desc1 = (TextView) mainSingleTilesView.findViewById(R.id.txt_desc2);
                            TextView txt_desc2 = (TextView) mainSingleTilesView.findViewById(R.id.txt_desc1);

                            TextView txt_stepcount = (TextView) mainSingleTilesView.findViewById(R.id.txt_stepcount);
                            TextView txt_steps = (TextView) mainSingleTilesView.findViewById(R.id.txt_steps);
                            //  TextView btn_action = (TextView) mainSingleTilesView.findViewById(R.id.btn_action);

                            // LinearLayout btn_action_lay = (LinearLayout) mainSingleTilesView.findViewById(R.id.btn_action_lay);
                            ImageView top_tile_bg = (ImageView) mainSingleTilesView.findViewById(R.id.img_quicl_link_bg);
                            ImageView img_goal_achive_icon = (ImageView) mainSingleTilesView.findViewById(R.id.img_goal_achive_icon);

                            cardview_top_tiles.setTag(serviceObject);
                            txt_desc1.setTag(serviceObject);
                            txt_desc2.setTag(serviceObject);


                            cardview_top_tiles.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    TopTile obj = (TopTile) v.getTag();
                                    callOnPickAGoal(obj);
                                }
                            });

                            txt_desc1.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    TopTile obj = (TopTile) v.getTag();
                                    callOnPickAGoal(obj);
                                }
                            });
                            txt_desc2.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    TopTile obj = (TopTile) v.getTag();
                                    callOnPickAGoal(obj);
                                }
                            });


                            String smallIcon = serviceObject.getIcon();
                            String mainImage = serviceObject.getImage();

                            if (serviceObject.getImage() != null) {
                                if (mainImage.contains("zoom_level")) {
                                    mainImage = mainImage.replace("zoom_level", "xxxhdpi");
                                } else if (mainImage.contains("zoom")) {
                                    mainImage = mainImage.replace("zoom", "xxxhdpi");
                                }
                            } else {
                                mainImage = "";
                            }

                            if (serviceObject.getIcon() != null) {
                                if (smallIcon.contains("zoom_level")) {
                                    smallIcon = smallIcon.replace("zoom_level", "xxxhdpi");
                                } else if (smallIcon.contains("zoom")) {
                                    smallIcon = smallIcon.replace("zoom", "xxxhdpi");
                                }
                            } else {
                                smallIcon = "";
                            }


                            int imageSize = Utility.getImageSizeFor_DeviceDensitySize(200);

                            intWidth = deviceScreenDimension.getDisplayWidth();

                            String text = serviceObject.getTitle();

                            System.out.println("==============text================" + text);
                            float tileWidthf = 0;
                            int tileWidth = 0;
                            float tileHeigthf = 0;
                            float tileHeigth = 0;
                            int myUnit = 0;
                            float myUnitf = intWidth / 720;

                            //  myUnit = (int) myUnitf;

                            tileHeigthf = myUnitf * 190;
                            tileHeigth = (int) tileHeigthf;

                            if (text.length() < 15) {
                                tileWidthf = myUnitf * 290;
                            } else if (text.length() < 20) {
                                tileWidthf = myUnitf * 390;
                            } else if (text.length() > 20) {
                                tileWidthf = myUnitf * 400;
                            }
                            tileWidth = (int) tileWidthf;


                            RequestOptions requestOptionsSamll = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(getBaseContext()).load(smallIcon)
                                    .apply(requestOptionsSamll)
                                    .into(img_goal_achive_icon);


                            String my_goal_name = null;
                            if (num != 0) {
                                my_goal_name = serviceObject.getTitle();
                            } else {
                                my_goal_name = pref.getMyGoalData().get("my_goal_name");
                                if (my_goal_name.equals("")) {
                                    my_goal_name = "Pick a goal";
                                }
                            }

                            if (my_goal_name.contains(" ")) {

                                String[] splited = my_goal_name.split("\\s+");

                                String secondName = null;
                                String ste1 = splited[0];

                                boolean isTwoWords = false;
                                if (splited.length > 2) {
                                    ste1 = splited[0] + " " + splited[1];
                                    if (ste1.length() < 18) {
                                        isTwoWords = true;
                                    } else {
                                        ste1 = splited[0];
                                    }
                                }
                                txt_desc1.setText(ste1);


                                if (splited.length == 2) {
                                    secondName = splited[1];
                                } else if (splited.length == 3) {
                                    if (isTwoWords) {
                                        secondName = splited[2];
                                    } else {
                                        secondName = splited[1] + " " + splited[2];
                                    }

                                } else if (splited.length == 4) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3];
                                    }

                                } else if (splited.length == 5) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4];
                                    }

                                } else if (splited.length == 6) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5];
                                    }

                                } else if (splited.length == 7) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6];
                                    }
                                } else if (splited.length == 8) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7];
                                    }
                                } else if (splited.length == 9) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8];
                                    }

                                } else if (splited.length == 10) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9];
                                    }

                                } else if (splited.length == 11) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }

                                } else if (splited.length == 12) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 13) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 14) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 15) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 16) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 17) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 18) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 19) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 20) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                } else if (splited.length == 21) {
                                    if (isTwoWords) {
                                        secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    } else {
                                        secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
                                    }
                                }


                                //  System.out.println("==========secondName=============" + secondName);
                                if (secondName != null) {
                                    if (secondName.length() >= 20) {
                                        secondName = secondName.substring(0, Math.min(secondName.length(), 15));
                                        secondName = secondName + "...";
                                    }
                                    txt_desc2.setText(secondName);
                                }

                                txt_desc1.setText(serviceObject.getSubTitle());
                                txt_desc2.setText(serviceObject.getTitle());


                            } else {
                                txt_desc1.setText(my_goal_name);
                                txt_desc2.setText("");
                            }
                            String te = serviceObject.getActionText();

                            if (num == 0) {

                                String goalStatus = pref.getMyGoalData().get("my_goal_status");
                                String goalImage = pref.getMyGoalData().get("my_goal_image");
                                String goalBgImage = pref.getMyGoalData().get("my_goal_bg");

                                my_goal_name = pref.getMyGoalData().get("my_goal_name");


                                txt_desc1.setText(serviceObject.getSubTitle());
                                txt_desc2.setText(serviceObject.getTitle());


                                RequestOptions requestOption = new RequestOptions().override(tileWidth, (int) tileHeigth).diskCacheStrategy(DiskCacheStrategy.ALL);
                                //  goalImage

                                if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
                                    Glide.with(getBaseContext()).load(goalBgImage)
                                            .apply(requestOptionsSamll)
                                            .into(img_goal_achive_icon);
                                } else {
                                    top_tile_bg.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            TopTile obj = (TopTile) v.getTag();
                                            callOnPickAGoal(obj);
                                        }
                                    });
                                    top_tile_bg.setTag(serviceObject);
                                    Glide.with(getBaseContext()).load(goalBgImage)
                                            .apply(requestOption)
                                            .into(top_tile_bg);

                                    Glide.with(getBaseContext()).load(goalImage)
                                            .apply(requestOptionsSamll)
                                            .into(img_goal_achive_icon);
                                }


                                if (goalStatus == null) {


                                    if (Constants.type == Constants.Type.LIFEPLUS) {
                                        Glide.with(getBaseContext()).load(goalImage)
                                                .apply(requestOptionsSamll)
                                                .into(img_goal_achive_icon);
                                    } else {
                                        Glide.with(getBaseContext()).load(goalBgImage)
                                                .apply(requestOption)
                                                .into(top_tile_bg);

                                        Glide.with(getBaseContext()).load(goalImage)
                                                .apply(requestOptionsSamll)
                                                .into(img_goal_achive_icon);
                                    }


                                    if (goalStatus.equals("Pending")) {
                                        //   btn_action.setTextColor(Color.parseColor("#ff860b"));
                                        txt_desc1.setText("Let's Do It");

                                        System.out.println("==============Pending===============" + te);
                                    } else if (goalStatus.equals("Pick")) {
                                        txt_desc2.setText("Pick a");
                                        txt_desc1.setText("goal");
                                    } else if (goalStatus.equals("Completed")) {
                                        System.out.println("==============Completed===============" + te);
                                        txt_desc1.setText("Done");
                                        //  txt_desc1.setTextColor(Color.parseColor("#45D100"));
                                    }
//
//                                }//
                                } else {

                                    if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
                                        Glide.with(getBaseContext()).load(goalBgImage)
                                                .apply(requestOptionsSamll)
                                                .into(img_goal_achive_icon);
                                    } else {
                                        Glide.with(getBaseContext()).load(goalBgImage)
                                                .apply(requestOption)
                                                .into(top_tile_bg);

                                        Glide.with(getBaseContext()).load(goalImage)
                                                .apply(requestOptionsSamll)
                                                .into(img_goal_achive_icon);
                                    }


                                    if (goalStatus.equals("Pending")) {
                                        txt_desc2.setText(my_goal_name);
                                        txt_desc1.setText("Let's Do It");
                                    } else if (goalStatus.equals("Pick")) {
                                        txt_desc2.setText("Pick a");
                                        txt_desc1.setText("goal");
                                    } else if (goalStatus.equals("Completed")) {
                                        txt_desc2.setText(my_goal_name);
                                        txt_desc1.setText("Done");
                                    }


                                }
                            } else {
                                txt_desc1.setText(serviceObject.getSubTitle());
                                txt_desc2.setText(serviceObject.getTitle());


                                RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(tileWidth, (int) tileHeigth);

                                if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
                                    Glide.with(getBaseContext()).load(mainImage)
                                            .apply(requestOptions)
                                            .into(img_goal_achive_icon);
                                } else {
                                    Glide.with(getBaseContext()).load(mainImage)
                                            .apply(requestOptions)
                                            .into(top_tile_bg);
                                }


                            }


                        }
                    }


                    for (int i = 0; i < serviceBottomTileList.size(); i++) {
                        final QuickLink objSecond = serviceBottomTileList.get(i);
                        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        //  View myViewSec = inflater.inflate(R.layout.newdisign_menu_item_footer, null);
                        View myViewSec = null;
                        int tileHieght = 0;
                        int tileWidth = 0;

                        if (Constants.type == Constants.Type.LIFEPLUS) {
                            myViewSec = inflater.inflate(R.layout.homepage_quickslink_lifeplus, null);

                            float w = (intWidth * 20) / 100;
                            float h = (intWidth * 19) / 100;
                            int valueW = (int) w;
                            int valueH = (int) h;
                            myViewSec.setLayoutParams(new FrameLayout.LayoutParams(valueW, valueH));
                        } else {

                            myViewSec = inflater.inflate(R.layout.card_timeline_new_design_quicks, null);
                            intWidth = deviceScreenDimension.getDisplayWidth();

                            int imgHieght = 0;
                            tileWidth = getQWidth(context);
                            tileHieght = getQHidth(context);
                            imgHieght = getImageHidth(context);
                            myViewSec.setLayoutParams(new FrameLayout.LayoutParams(tileWidth, tileHieght));
                        }


                        View space = inflater.inflate(R.layout.row_item, null);

                        final TextView txt_firstline = (TextView) myViewSec.findViewById(R.id.txt_service_name);


                        LinearLayout lay_imgview = myViewSec.findViewById(R.id.lay_imgview);
                        final ImageView quicl_link_icon = (ImageView) myViewSec.findViewById(R.id.footer_remote_image_icon);
                        final ImageView remote_image_icon_quicklink = (ImageView) myViewSec.findViewById(R.id.remote_image_icon_quicklink);

                        //  lay_imgview.setLayoutParams(new LinearLayout.LayoutParams(imgHieght,imgHieght));
                        // cardview_top_quicklinks.setTag(objSecond);
                        quicl_link_icon.setTag(objSecond);
                        txt_firstline.setTag(objSecond);

//                        cardview_top_quicklinks.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                QuickLink objS = (QuickLink) txt_firstline.getTag();
//                                clickedQuickLink(objS);
//                            }
//                        });
                        txt_firstline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QuickLink objS = (QuickLink) txt_firstline.getTag();

                                clickedQuickLink(objS);

                            }
                        });

                        quicl_link_icon.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                // Perform action on click
                                QuickLink objS = (QuickLink) quicl_link_icon.getTag();

                                clickedQuickLink(objS);

                            }
                        });

                        if (objSecond.getIcon() != null) {

                            String img = objSecond.getIcon();

                            RequestOptions requestOption = new RequestOptions().fitCenter().override(tileWidth, tileHieght).diskCacheStrategy(DiskCacheStrategy.ALL);


                            if (Constants.type == Constants.Type.LIFEPLUS) {
//                                Glide.with(getBaseContext()).load("https://apps.ayubo.life/static/hh/quick_links_new.png")
//                                        .apply(requestOption)
//                                        .into(remote_image_icon_quicklink);
                            } else {
                                Glide.with(getBaseContext()).load(MAIN_URL_APPS + "static/hh/quick_links2.png")
                                        .apply(requestOption)
                                        .into(remote_image_icon_quicklink);
                            }


                            img = img.replace("zoom_level", "xxxhdpi");

                            int imageSize = Utility.getImageSizeFor_DeviceDensitySize(57);
                            if (img == null) {
                                quicl_link_icon.setVisibility(View.GONE);
                            } else {
                                quicl_link_icon.setVisibility(View.VISIBLE);
                                RequestOptions requestOptions = new RequestOptions().fitCenter().override(imageSize, imageSize).diskCacheStrategy(DiskCacheStrategy.ALL);
                                Glide.with(getBaseContext()).load(img)
                                        .apply(requestOptions)
                                        .into(quicl_link_icon);
                            }


                        }

                        if ((objSecond.getTitle() != null) && ((objSecond.getTitle2() != null) && (objSecond.getTitle().length() > 0) && (objSecond.getTitle2().length() > 0))) {
                            String name = null;
                            if ((objSecond.getTitle().length() > 0) && (objSecond.getTitle2().length() > 0)) {
                                name = objSecond.getTitle() + "\n" + objSecond.getTitle2();
                                txt_firstline.setText(name);
                            }

                        } else if ((objSecond.getTitle() != null) && (objSecond.getTitle().length() > 0)) {
                            txt_firstline.setText(objSecond.getTitle());
                        } else if ((objSecond.getTitle2() != null) && (objSecond.getTitle2().length() > 0)) {
                            txt_firstline.setText(objSecond.getTitle2());
                        }

//                        heroVh.layout_services_menu_horizontal.addView(myViewSec);
//                        heroVh.layout_services_menu_horizontal.addView(space);

                    }

                    break;


                case BANNER:
                    final Banner objb = null; // PostCell
                    final BannerVH holderBanner = (BannerVH) holde;

                    //if (obj.getPostId() == null) {
                    //   holder.user_panel.setVisibility(View.GONE);
                    //  holder.title_panel.setVisibility(View.GONE);
//                        img_banner_image = (ImageButton) itemView.findViewById(R.id.img_banner_image);
//                        txt_banner_header = (TextView) itemView.findViewById(R.id.txt_banner_header);
//                        txt_banner_desc = (TextView) itemView.findViewById(R.id.txt_banner_desc);
//                        txt_banner_button = (TextView) itemView.findViewById(R.id.txt_banner_button);
                    //  }
                    break;
                case ITEM:
                    final Post obj = movieResults.get(position); // PostCell
                    final PostCell holder = (PostCell) holde;

                    if (obj.getPostId() == null) {
                        //   holder.user_panel.setVisibility(View.GONE);
                        holder.title_panel.setVisibility(View.GONE);
                        holder.content_panel.setVisibility(View.GONE);
                        holder.media_panel.setVisibility(View.GONE);
                        holder.user_link_panel.setVisibility(View.GONE);
                        holder.user_reaction_panel.setVisibility(View.GONE);
                    } else {

                        cellType = obj.getType();
                        if (cellType == null) {
                            cellType = "0";
                        }

                        //======   SETUP CELL STATUS ============================================
                        setupCellStatus(obj);
                        //======   SETUP CELL VIEW ============================================
                        setupCellViewForPost(obj, holder);

                        //   SYSTEM_POST
                        try {

                            String postedUserId = null;

                            holder.img_btn_close_intop.setTag(obj);
                            holder.img_btn_close_bglayout.setTag(obj);
                            holder.user_reaction_panel_liked_user_list.setTag(obj);
                            holder.user_reaction_panel_like_count.setTag(obj);
                            holder.user_reaction_panel_like_text.setTag(obj);
                            holder.user_reaction_panel_comment_clicked.setTag(obj);
                            holder.user_reaction_panel_comment_second_text.setTag(obj);
                            holder.img_user_picture.setTag(obj);
                            holder.txt_panel_b_background_image.setTag(obj);
                            holder.txt_panel_b_view_button.setTag(obj);

                            holder.img_user_picture.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Post p = (Post) v.getTag();
                                    System.out.println("===============Post ID===================" + p.getPostId());
                                    System.out.println("======getType=============" + p.getType());
                                    System.out.println("======getRedirect_type=============" + p.getRedirect_type());
                                    System.out.println("======getRedirectUrl=============" + p.getRedirectUrl());
                                    System.out.println("======getBody=============" + p.getBody());
                                    System.out.println("======getTitle=============" + p.getTitle());
                                    System.out.println("======getVideoThumbnail=============" + p.getVideoThumbnail());
                                    System.out.println("======getButtonText=============" + p.getButtonText());
                                    System.out.println("======getSubTitle=============" + p.getSubTitle());
                                    System.out.println("======getPostThumbnailUrl=============" + p.getPostThumbnailUrl());
                                    System.out.println("============================================================");
                                }
                            });


                            //======iS Delete Enable =========================================
                            if (obj.getUser() != null) {
                                postedUserId = obj.getUser().getId().toString();
                                if (userid_ExistingUser.equals(postedUserId)) {
                                    holder.img_btn_close_intop.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Post obj = (Post) holder.img_btn_close_intop.getTag();
                                            showAlert_Add(V1NewFeedActivity.this, "", "Are you sure you want to delete post?", obj);
                                        }
                                    });
                                    holder.img_btn_close_bglayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Post obj = (Post) holder.img_btn_close_intop.getTag();
                                            showAlert_Add(V1NewFeedActivity.this, "", "Are you sure you want to delete post?", obj);
                                        }
                                    });
                                }
                            }


                            //======has Likes =========================================
                            if (obj.getIsLiked() != null) {

                                if (obj.getIsLiked()) {
                                    holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_blue_ic);
                                    holder.user_reaction_panel_liked_user_list.setText(obj.getLikedUsersText());
                                    holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#0662b8"));
                                } else {
                                    holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_ic);
                                    holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#000000"));
                                }
                            }

                            //======Like Text Clicked =========================================
                            holder.user_reaction_panel_like_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Post pobj = (Post) holder.user_reaction_panel_like_text.getTag();

                                    String likedUsersText = obj.getLikedUsersText();
                                    Integer likeCount = obj.getLikeCount();

                                    if (pobj.getIsLiked()) {
                                        String likeCounts = null;
                                        pobj.setIsLiked(false);
                                        sendALike(pobj.getPostId(), 0);

                                        Integer liked_count = obj.getLikeCount();

                                        String sss = null;
                                        if (likedUsersText.contains("You and ")) {
                                            sss = likedUsersText.substring(8);
                                        } else if (likedUsersText.contains("You, ")) {
                                            sss = likedUsersText.substring(5);
                                        } else if (likedUsersText.contains("You")) {
                                            sss = likedUsersText.substring(3);
                                        }

                                        if (liked_count > 0) {
                                            liked_count = liked_count - 1;
                                        }

                                        if (liked_count > 1) {
                                            likeCounts = Integer.toString(liked_count) + " Likes";
                                        } else if (liked_count == 1) {
                                            likeCounts = Integer.toString(liked_count) + " Like";
                                        } else if (liked_count == 0) {
                                            likeCounts = Integer.toString(liked_count) + " Like";
                                            holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);

                                        }

                                        pobj.setLikeCount(liked_count);
                                        pobj.setLikedUsersText(sss);
                                        holder.user_reaction_panel_liked_user_list.setText(sss);
                                        holder.user_reaction_panel_like_count.setText(likeCounts);
                                        holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#000000"));
                                        holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_ic);


                                    } else {
                                        holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);

                                        if (likeCount == 0) {
                                            likedUsersText = "You";
                                        } else if (likeCount == 1) {
                                            likedUsersText = "You and " + likedUsersText;
                                        } else {
                                            likedUsersText = "You, " + likedUsersText;
                                        }
                                        pobj.setLikedUsersText(likedUsersText);

                                        String likeCounts = null;
                                        pobj.setIsLiked(true);
                                        Integer liked_count = null;


                                        if (obj.getLikeCount() != null) {
                                            liked_count = obj.getLikeCount();
                                        }

                                        liked_count = liked_count + 1;

                                        if (liked_count > 1) {
                                            likeCounts = Integer.toString(liked_count) + " Likes";
                                        } else if (liked_count == 1) {
                                            likeCounts = Integer.toString(liked_count) + " Like";
                                        } else if (liked_count == 0) {
                                            likeCounts = Integer.toString(liked_count) + " Like";
                                        }

                                        pobj.setLikeCount(liked_count);

                                        holder.user_reaction_panel_like_count.setText(likeCounts);
                                        holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#0662b8"));
                                        holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_blue_ic);
                                        holder.user_reaction_panel_liked_user_list.setText(likedUsersText);

                                        sendALike(pobj.getPostId(), 1);
                                    }
                                }
                            });

                            //======Comments Clicked =========================================
                            holder.user_reaction_panel_comment_clicked.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getBaseContext(), AddComments_Activity.class);
                                    intent.putExtra("type", "timeline");
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                            });


                            //======Comments Clicked =========================================
                            holder.user_reaction_panel_comment_second_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getBaseContext(), AddComments_Activity.class);
                                    intent.putExtra("type", "timeline");
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                            });

                            //======Play Button Clicked =========================================
                            holder.txt_panel_b_play_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (obj.getType().equals("VIDEO_POST")) {
                                        Intent intent = new Intent(getBaseContext(), TimelineVideo_Activity.class);
                                        intent.putExtra("IURL", obj.getVideoThumbnail());
                                        intent.putExtra("URL", obj.getVideoUrl());
                                        startActivity(intent);
                                    } else if (obj.getType().equals("GIF_POST")) {
                                        Intent intent = new Intent(getBaseContext(), TimelineGif_Activity.class);
                                        intent.putExtra("URL", obj.getGifUrl());
                                        startActivity(intent);
                                    }
                                }
                            });

                            //======Liked Users Lists Text Clicked =========================================
                            holder.user_reaction_panel_liked_user_list.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Post obj = (Post) holder.user_reaction_panel_liked_user_list.getTag();

                                    Integer pID = obj.getPostId();
                                    Integer likeCount = obj.getLikeCount();

                                    Intent intent = new Intent(getBaseContext(), LikedUsers_Activity.class);
                                    intent.putExtra("postId", Integer.toString(pID));
                                    intent.putExtra("likeCount", Integer.toString(likeCount));
                                    startActivity(intent);
                                }
                            });

                            //======Liked Users Lists Clciked=========================================
                            holder.user_reaction_panel_like_count.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Post obj = (Post) holder.user_reaction_panel_like_count.getTag();
                                    Integer pID = obj.getPostId();
                                    Integer likeCount = obj.getLikeCount();

                                    Intent intent = new Intent(getBaseContext(), LikedUsers_Activity.class);
                                    intent.putExtra("postId", Integer.toString(pID));
                                    intent.putExtra("likeCount", Integer.toString(likeCount));
                                    startActivity(intent);
                                }
                            });


                            //======Setup Profile Picture=========================================
                            if (obj.getUser() == null) {
                                holder.img_user_picture.setVisibility(View.GONE);
                            } else {
                                holder.img_user_picture.setVisibility(View.VISIBLE);
                                RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).transforms(new CircleTransform(getBaseContext()));
                                Glide.with(getBaseContext()).load(obj.getUser().getProfilePicture())
                                        .thumbnail(0.5f)
                                        .transition(withCrossFade())
                                        .apply(requestOptions)
                                        .into(holder.img_user_picture);
                            }

                            //======Setup User Name =========================================
                            if (obj.getUser() != null) {
                                holder.txt_user_name.setText(obj.getUser().getName());
                            }

                            //======Set Post time =========================================
                            if (obj.getTimestamp() != null) {
                                holder.txt_time_ago.setText(getTimeStringFromInteger(obj.getTimestamp()));
                            }

                            //======Set Like text =========================================
                            if (obj.getLikeCount() != null) {
                                holder.user_reaction_panel_like_count.setText(getLikesStringFromCount(obj.getLikeCount()));
                            }

                            //======Set Comments count text =========================================
                            if (obj.getCommentCount() != null) {
                                holder.user_reaction_panel_comment_count.setText(Integer.toString(obj.getCommentCount()));
                            }

                            //======Set Likes Users List text =========================================
                            if (obj.getLikedUsersText() != null) {
                                holder.user_reaction_panel_liked_user_list.setText(obj.getLikedUsersText());
                            }

                            //======Set Post Title =========================================
                            if (obj.getTitle() != null) {
                                holder.title_panel_heading_text.setText(obj.getTitle());
                            }
                            //======Set Post description =========================================
                            if (obj.getBody() != null) {
                                holder.text_panel_text_message.setText(obj.getBody());
                            }


                            //============LINK VIEW===================================================
                            if (shouldShow_LinkPreviewPanel) {
                                if (obj.getLinkInfo() != null) {
                                    holder.link_panel_text_url.setText(obj.getLinkInfo().getLinkUrl());
                                    holder.link_panel_text_desc.setText(obj.getLinkInfo().getTitle());
                                    holder.link_panel_timeline_readme.setTag(obj.getLinkInfo().getLinkUrl());
                                }
                                holder.link_panel_timeline_readme.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String url = (String) holder.link_panel_timeline_readme.getTag();
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        startActivity(browserIntent);
                                    }
                                });
                            }


                            //============PROGRAM VIEW===================================================
                            if (shouldShow_ProgramPanel) {
                                if (obj.getSubTitle() != null) {
                                    holder.program_panel_text_normal.setText(obj.getSubTitle());
                                }
                                if (obj.getTitle() != null) {
                                    holder.program_panel_text_heading.setText(obj.getTitle());
                                }
                                if (obj.getButtonText() != null) {
                                    holder.txt_panel_b_view_button.setText(obj.getButtonText());
                                } else {
                                    holder.txt_panel_b_view_button.setText("View");
                                }

                                holder.txt_panel_b_view_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if ((obj.getRedirectUrl() != null) && (obj.getRedirectUrl().length() > 5)) {
                                            Post pot = (Post) holder.txt_panel_b_view_button.getTag();

                                            if (obj.isExternalLink()) {
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getRedirectUrl()));
                                                startActivity(browserIntent);
                                            } else {
                                                Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
                                                intent.putExtra("URL", pot.getRedirectUrl());
                                                startActivity(intent);
                                            }


                                        } else {
                                            Post objjj = (Post) holder.txt_panel_b_view_button.getTag();
                                            openZoomImage(position, objjj);

                                        }

                                    }
                                });
                            }


                            //======Main Image click event =========================================
                            holder.txt_panel_b_background_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if ((obj.getRedirectUrl() != null) && (obj.getRedirectUrl().length() > 5)) {

                                        if (obj.isExternalLink()) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getRedirectUrl()));
                                            startActivity(browserIntent);
                                        } else {
                                            Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
                                            intent.putExtra("URL", obj.getRedirectUrl());
                                            startActivity(intent);
                                        }


                                    } else {
                                        Post objjj = (Post) holder.txt_panel_b_background_image.getTag();
                                        openZoomImage(position, objjj);
                                    }
                                }
                            });


                            //======Main Image Setup =========================================
                            String imageURL = getImageUrlFromType(obj);
                            if ((imageURL == null) || (imageURL.equals(""))) {
                                holder.txt_panel_b_background_image.setVisibility(View.GONE);
                            } else {
                                holder.txt_panel_b_background_image.setVisibility(View.VISIBLE);

                                RequestOptions requestOpt = new RequestOptions().override((int) intWidth, intHeight);

                                Glide.with(getBaseContext()).load(imageURL)
                                        .apply(requestOpt)
                                        .into(holder.txt_panel_b_background_image);


                            }

                            holder.txt_panel_b_background_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (obj.getType().equals("SYSTEM_POST")) {

                                        if ((obj.getRedirectUrl() != null) && (obj.getRedirectUrl().length() > 5)) {

                                            String type = obj.getRedirect_type();

                                            if (type.equals(CONSTANTS.ACTION_KEY_PROGRAMTIMELINE)) {
                                                String meta = obj.getRedirectUrl();
                                                onProgramNewDesignClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_PROGRAM)) {
                                                String meta = obj.getRedirectUrl();
                                                onProgramPostClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_CHALLENGE)) {
                                                String meta = obj.getRedirectUrl();
                                                onMapChallangeClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_CHATPRO)) {
                                                String meta = obj.getRedirectUrl();
                                                onAskClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_COMMONVIEW)) {
                                                String meta = obj.getRedirectUrl();
                                                onCommonViewClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_COMMON)) {
                                                String meta = obj.getRedirectUrl();
                                                onCommonViewClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_WEBVIEW)) {
                                                String meta = obj.getRedirectUrl();
                                                onBowserClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_REPORTS)) {
                                                String meta = obj.getRedirectUrl();
                                                onReportsClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_GOAL)) {
                                                String meta = obj.getRedirectUrl();
                                                onGoalClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_GOAL)) {
                                                String meta = obj.getRedirectUrl();
                                                onVideoCallClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_CHANNELING)) {
                                                String meta = obj.getRedirectUrl();
                                                onButtonChannelingClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_JANASHAKTHI_WELCOME)) {
                                                String meta = obj.getRedirectUrl();
                                                onJanashakthiWelcomeClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_JANASHAKTHI_UPLOAD_REPORTS)) {
                                                String meta = obj.getRedirectUrl();
                                                onJanashakthiReportsClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_DYNAMICQUESTION)) {
                                                String meta = obj.getRedirectUrl();
                                                onDyanamicQuestionClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_POST)) {
                                                String meta = obj.getRedirectUrl();
                                                onPostClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_NATIVE_POST)) {
                                                String meta = obj.getRedirectUrl();
                                                onNativePostClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_JSON_NATIVE_POST)) {
                                                String meta = obj.getRedirectUrl();
                                                onJSONNativePostClick(meta);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_HELP)) {
                                                Intent intent = new Intent(getBaseContext(), HelpFeedbackActivity.class);
                                                startActivity(intent);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_CHALLENGE_OPEN)) {
                                                challenge_id = obj.getRedirectUrl();

//                                            Intent in = new Intent(getBaseContext(), MapChallangeActivity.class);
//                                            in.putExtra(EXTRA_CHALLANGE_ID, challenge_id);
//                                            startActivity(in);
                                                onMapChallangeClick(challenge_id);
//                                            Intent intent = new Intent(getBaseContext(), MapChallengeActivity.class);
//                                            intent.putExtra("challenge_id", challenge_id);
//                                            startActivity(intent);

                                            } else if (type.equals(CONSTANTS.ACTION_KEY_CHALLENGE_JOIN)) {
                                                String challengeID = obj.getRedirectUrl();
                                                Intent intent = new Intent(getBaseContext(), MapJoinChallenge_Activity.class);
                                                intent.putExtra("challenge_id", challengeID);
                                                startActivity(intent);
                                            } else if (obj.isExternalLink()) {

                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getRedirectUrl()));
                                                startActivity(browserIntent);


                                            } else {
                                                Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
                                                intent.putExtra("URL", obj.getRedirectUrl());
                                                startActivity(intent);
                                            }


                                        } else {

                                            Post objjj = (Post) holder.txt_panel_b_background_image.getTag();
                                            openZoomImage(position, objjj);

                                        }

                                    } else {
                                        Post objjj = (Post) holder.txt_panel_b_background_image.getTag();
                                        openZoomImage(position, objjj);

                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    break;

                case LOADING:
                    LoadingVH loadingVH = (LoadingVH) holde;

                    if (retryPageLoad) {
                        loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                        loadingVH.loadmore_progress.setVisibility(View.VISIBLE);

                        loadingVH.mErrorTxt.setText(
                                errorMsg != null ?
                                        errorMsg :
                                        getBaseContext().getString(R.string.error_msg_unknown));

                    } else {
                        loadingVH.mErrorLayout.setVisibility(View.GONE);
                        loadingVH.loadmore_progress.setVisibility(View.VISIBLE);
                    }
                    break;

            }
        }

        @Override
        public int getItemCount() {

            int finalCount;

            finalCount = movieResults == null ? 0 : movieResults.size();

            return finalCount;

        }

        void openZoomImage(Integer pos, Post pobj) {

            Intent intent = new Intent(getBaseContext(), TimelineImage_Activity.class);
            if (pobj.getPostThumbnailUrl() != null) {
                intent.putExtra("position", pos);
                intent.putExtra("TURL", pobj.getPostThumbnailUrl());
                intent.putExtra("URL", pobj.getPostImageUrl());
                startActivity(intent);
            }

        }

        String getImageUrlFromType(Post obj) {
            String imageURL = null;
            obj.getType();
            if (obj.getType().equals("IMAGE_POST")) {
                imageURL = obj.getPostImageUrl();
            }
            if (obj.getType().equals("VIDEO_POST")) {
                imageURL = obj.getVideoThumbnail();
            }
            if (obj.getType().equals("GIF_POST")) {
                imageURL = obj.getGifThumbnail();
            }
            if (obj.getType().equals("LINK_POST")) {
                imageURL = obj.getLinkInfo().getThumbnailUrl();
            }
            if (obj.getType().equals("SYSTEM_POST")) {
                imageURL = obj.getPostImageUrl();
            }
            if (obj.getType().equals("PROGRAM_POST")) {
                imageURL = obj.getPostImageUrl();
            }
            if (obj.getType().equals("IMAGE_POST")) {
                imageURL = obj.getPostImageUrl();
            }
            //  System.out.println("==============================================="+obj.getType());
            //   System.out.println("===u=="+imageURL);
            return imageURL;
        }

        private int getQWidth(Context context) {
            float density = getResources().getDisplayMetrics().density;
            int added = 0;
            if (density >= 5.0) {
                added = 300;
            } else if (density >= 4.0) {
                added = 300;
            } else if (density >= 3.0) {
                added = 250;
            } else if (density >= 2.0) {
                added = 200;
            } else if (density >= 1.5) {
                added = 180;
            } else if (density >= 1.0) {
                added = 150;
            }
            return added;
        }

        private int getImageHidth(Context context) {
            float density = getResources().getDisplayMetrics().density;
            int added = 0;
            if (density >= 5.0) {
                added = 125;
            } else if (density >= 4.0) {
                added = 102;
            } else if (density >= 3.0) {
                added = 82;
            } else if (density >= 2.0) {
                added = 60;
            } else if (density >= 1.5) {
                added = 52;
            } else if (density >= 1.0) {
                added = 38;
            }
            return added;
        }

        private int getQHidth(Context context) {
            float density = getResources().getDisplayMetrics().density;
            int added = 0;

            if (density >= 5.0) {
                added = 380;
            } else if (density >= 4.0) {
                added = 330;
            } else if (density >= 3.0) {
                added = 280;
            } else if (density >= 2.0) {
                added = 230;
            } else if (density >= 1.5) {
                added = 195;
            } else if (density >= 1.0) {
                added = 150;
            }
            return added;
        }

        String getLikesStringFromCount(Integer liked_count) {
            String likeCounts = null;
            if (liked_count > 1) {
                likeCounts = Integer.toString(liked_count) + " Likes";
            } else if (liked_count == 1) {
                likeCounts = Integer.toString(liked_count) + " Like";
            } else if (liked_count == 0) {
                likeCounts = Integer.toString(liked_count) + " Like";
            }

            return likeCounts;
        }

        String getTimeStringFromInteger(Integer timeInInteger) {

            long number = Long.valueOf(timeInInteger);
            number = number * 1000;
            // Date date=new Date(number);
            Calendar cal = Calendar.getInstance();
            Calendar todayCal = Calendar.getInstance();
            Date d = new Date(number);
            cal.setTime(d);

            int date = cal.get(Calendar.DATE);
            Date todayDate = new Date();

            todayCal.setTime(todayDate);
            int cameDate = cal.get(Calendar.DATE);
            int currDate = todayCal.get(Calendar.DATE);
            SimpleDateFormat formatter = null;
            if (cameDate == currDate) {
                formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss'X' z");
            } else {
                formatter = new SimpleDateFormat("E, dd MMM yyyy'X'HH:mm:ss z");
            }
            String dateFromDB_forADay = formatter.format(cal.getTime());
            String[] parts = dateFromDB_forADay.split("X");
            String part1 = parts[0];

            return part1;
        }

        private void setupCellStatus(Post postObj) {
            cellType = postObj.getType();

            if (postObj != null) {

                if (cellType.equals("NORMAL_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = false;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("IMAGE_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("VIDEO_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = true;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("GIF_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = true;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("LINK_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = true;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("SYSTEM_POST")) {
                    shouldShow_MediaPanel = postObj.hasImage();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_ContentPanel = postObj.hasBody();

                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;

                    if (postObj.isInteractionsEnabled()) {
                        shouldShow_UserInteractionPanel = true;   //E  Optional
                    } else {
                        shouldShow_UserInteractionPanel = false;  //E  Optional
                    }

                } else if (cellType.equals("PROGRAM_POST")) {
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = true;
                    shouldShow_TitlePanel = false;
                    shouldShow_ContentPanel = false;

                    shouldShow_UserInteractionPanel = false;   //E  Optional
                }
            }
        }

        private void setupCellViewForPost(Post postObj, PostCell holder) {

            // Title visibility ..........................
            if (shouldShow_TitlePanel) {
                holder.title_panel_heading_text.setVisibility(View.VISIBLE);
            } else {
                holder.title_panel_heading_text.setVisibility(View.GONE);
            }

            // Content visibility ..........................
            if (shouldShow_ContentPanel) {
                holder.content_panel.setVisibility(View.VISIBLE);
            } else {
                holder.content_panel.setVisibility(View.GONE);
            }

            // //Media panel visibility  ..........................
            if (shouldShow_MediaPanel) {
                holder.media_panel.setVisibility(View.VISIBLE);
            } else {
                holder.media_panel.setVisibility(View.GONE);
            }

            // Program panel visibility  ..........................
            if (shouldShow_ProgramPanel) {
                holder.program_panel_text_heading.setVisibility(View.VISIBLE);
                holder.txt_panel_b_view_button.setVisibility(View.VISIBLE);
                holder.program_panel_text_normal.setVisibility(View.VISIBLE);
            } else {
                holder.program_panel_text_heading.setVisibility(View.GONE);
                holder.txt_panel_b_view_button.setVisibility(View.GONE);
                holder.program_panel_text_normal.setVisibility(View.GONE);

            }

            //Overlay image visibility  ....................
            if (shouldShow_OverlayImage) {
                holder.txt_panel_b_play_button.setVisibility(View.VISIBLE);
            } else {
                holder.txt_panel_b_play_button.setVisibility(View.GONE);

            }

            //User Interaction Panel panel visibiity....................
            if (shouldShow_UserInteractionPanel) {
                holder.user_reaction_panel.setVisibility(View.VISIBLE);
                if ((postObj.getLikedUsersText() == null) || (postObj.getLikedUsersText().equals(""))) {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                } else {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);
                }


            } else {
                holder.user_reaction_panel.setVisibility(View.GONE);

            }

            //Link preview panel visibiity....................
            if (shouldShow_LinkPreviewPanel) {
                holder.user_link_panel.setVisibility(View.VISIBLE);
            } else {
                holder.user_link_panel.setVisibility(View.GONE);

            }

            // Close button visibility ....................
            if (postObj.getUser() == null) {
            } else {
                if (userid_ExistingUser.equals(postObj.getUser().getId())) {
                    holder.img_btn_close_intop.setVisibility(View.VISIBLE);
                    holder.img_btn_close_bglayout.setVisibility(View.VISIBLE);

                } else {
                    holder.img_btn_close_intop.setVisibility(View.GONE);
                    holder.img_btn_close_bglayout.setVisibility(View.GONE);
                }
            }

            // Liked users text list  visibility ....................
            if (postObj.getLikedUsersText() != null) {
                if (postObj.getLikedUsersText().length() > 1) {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                }

            }


        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        @Override
        public int getItemViewType(int position) {
            // if (isPositionHeader(position)) {
            if (isPositionHeader(position)) {
                return HEADER;
            } else {
                return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
            }
            //  return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }

        private String formatYearLabel(Post result) {
//        return result.getReleaseDate().substring(0, 4)  // we want the year only
//                + " | "
//                + result.getOriginalLanguage().toUpperCase();

            return "00000000";
        }

        public void addToTop(Post r) {
            movieResults.add(1, r);
            //  movieResults.add(r);
            notifyItemInserted(1);
        }

        public void add(Post r) {
            movieResults.add(r);
            notifyItemInserted(movieResults.size() - 1);
        }

        public void addAll(List<Post> moveResults) {
            for (Post result : moveResults) {
                add(result);
            }
        }

        public void remove(Post r) {
            int position = movieResults.indexOf(r);
            if (position > -1) {
                movieResults.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            isLoadingAdded = false;
            while (getItemCount() > 0) {
                remove(getItem(0));
            }
        }

        public boolean isEmpty() {
            return getItemCount() == 0;
        }

        public void addLoadingFooter() {
            isLoadingAdded = true;
            add(new Post());
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = movieResults.size() - 1;
            Post result = getItem(position);

            if (result != null) {
                movieResults.remove(position);
                notifyItemRemoved(position);
            }
        }

        public Post getItem(int position) {
            return movieResults.get(position);
        }

        public void showRetry(boolean show, @Nullable String errorMsg) {
            retryPageLoad = show;
            notifyItemChanged(movieResults.size() - 1);

            if (errorMsg != null) this.errorMsg = errorMsg;
        }

        /**
         * Header ViewHolder
         */
        protected class ServiceMenuView extends RecyclerView.ViewHolder {

            TextView txt_desc1;
            TextView txt_desc2;
            TextView btn_action;

            TextView txt_whats_on_mind;
            //            TextView txt_today_goals_viewmore;
            ImageView profilePicture, whatson_content_camera;
            LinearLayout btn_action_lay;
            ImageView main_services_image_icon;
//            LinearLayout layout_main_menu_horizontal;

//            LinearLayout layout_services_menu_horizontal;

            public ServiceMenuView(View itemView) {
                super(itemView);
//                layout_main_menu_horizontal = (LinearLayout) itemView.findViewById(R.id.layout_main_menu_horizontal);
//                layout_services_menu_horizontal = (LinearLayout) itemView.findViewById(R.id.layout_main_menu_horizontal_footer);

                txt_desc1 = (TextView) itemView.findViewById(R.id.txt_desc1);
                txt_desc2 = (TextView) itemView.findViewById(R.id.txt_desc2);
                btn_action = (TextView) itemView.findViewById(R.id.btn_action);
//                txt_today_goals_viewmore = itemView.findViewById(R.id.txt_today_goals_viewmore);
                main_services_image_icon = (ImageView) itemView.findViewById(R.id.remote_image_icon);

                whatson_content_camera = itemView.findViewById(R.id.whatson_content_camera);
                txt_whats_on_mind = (TextView) itemView.findViewById(R.id.lbl_whatsonyourmind);
                profilePicture = (ImageView) itemView.findViewById(R.id.img_timeline_post_click);

            }
        }

        /**
         * Main list's content ViewHolder
         */
        protected class PostCell extends RecyclerView.ViewHolder {
            private TextView mMovieTitle;
            private TextView mMovieDesc;
            private TextView mYear; // displays "year | language"
            private ImageView mPosterImg;
            private ProgressBar mProgress;

            LinearLayout img_btn_close_bglayout, user_reaction_panel_liked_user_list_layout, user_panel, title_panel, content_panel, user_link_panel, user_reaction_panel;
            ImageView user_reaction_panel_like_clicked_image, user_reaction_panel_liked_user_list_icon, img_user_picture, txt_panel_b_background_image, txt_panel_b_play_button;
            TextView txt_user_name, txt_time_ago, user_reaction_panel_like_text;
            ImageButton img_btn_close_intop, link_panel_timeline_readme;
            ConstraintLayout media_panel;
            Button txt_panel_b_view_button;
            TextView program_panel_text_heading, link_panel_text_url, link_panel_text_desc;
            TextView program_panel_text_normal, title_panel_heading_text, text_panel_text_message;
            TextView user_reaction_panel_liked_user_list, user_reaction_panel_like_count, user_reaction_panel_comment_count, user_reaction_panel_comment_clicked;
            TextView user_reaction_panel_comment_second_text;
            LinearLayout main_cell_view;

            public PostCell(View convertView) {
                super(convertView);
                main_cell_view = (LinearLayout) convertView.findViewById(R.id.main_cell_view);
                //  TYPE TIMELINE
                user_panel = (LinearLayout) convertView.findViewById(R.id.user_panel);
                title_panel = (LinearLayout) convertView.findViewById(R.id.title_panel);
                content_panel = (LinearLayout) convertView.findViewById(R.id.content_panel);
                media_panel = (ConstraintLayout) convertView.findViewById(R.id.media_panel);
                user_link_panel = (LinearLayout) convertView.findViewById(R.id.user_link_panel);
                user_reaction_panel = (LinearLayout) convertView.findViewById(R.id.user_reaction_panel);
                user_reaction_panel_liked_user_list_layout = (LinearLayout) convertView.findViewById(R.id.user_reaction_panel_liked_user_list_layout);

                // panel G
                txt_panel_b_view_button = (Button) convertView.findViewById(R.id.txt_panel_b_view_button);
                program_panel_text_heading = (TextView) convertView.findViewById(R.id.program_panel_text_heading);
                program_panel_text_normal = (TextView) convertView.findViewById(R.id.txt_panel_b_text_day);

                // button Play
                txt_panel_b_play_button = (ImageView) convertView.findViewById(R.id.txt_panel_b_play_button);

                user_reaction_panel_like_text = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_text);
                user_reaction_panel_like_clicked_image = (ImageView) convertView.findViewById(R.id.user_reaction_panel_like_clicked_image);
                user_reaction_panel_comment_second_text = (TextView) convertView.findViewById(R.id.user_reaction_panel_comment_second_text);

                txt_panel_b_background_image = (ImageView) convertView.findViewById(R.id.imageView2);

                // user_panel
                img_user_picture = (ImageView) convertView.findViewById(R.id.img_user_picture);
                txt_user_name = (TextView) convertView.findViewById(R.id.txt_user_name);
                txt_time_ago = (TextView) convertView.findViewById(R.id.txt_time_ago);
                img_btn_close_intop = (ImageButton) convertView.findViewById(R.id.img_btn_close_intop);
                img_btn_close_bglayout = (LinearLayout) convertView.findViewById(R.id.img_btn_close_bglayout);

                link_panel_timeline_readme = (ImageButton) convertView.findViewById(R.id.link_panel_timeline_readme);

                link_panel_text_url = (TextView) convertView.findViewById(R.id.link_panel_text_url);
                link_panel_text_desc = (TextView) convertView.findViewById(R.id.link_panel_text_desc);

                // viewHolder.user_reaction_panel_like_button = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_clicked);
                user_reaction_panel_like_count = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_count);
                user_reaction_panel_comment_count = (TextView) convertView.findViewById(R.id.user_reaction_panel_comment_count);
                user_reaction_panel_comment_clicked = (TextView) convertView.findViewById(R.id.user_reaction_panel_comment_clicked);
                user_reaction_panel_liked_user_list = (TextView) convertView.findViewById(R.id.user_reaction_panel_liked_user_list);
                user_reaction_panel_liked_user_list_icon = (ImageView) convertView.findViewById(R.id.user_reaction_panel_liked_user_list_icon);
                // title_panel
                title_panel_heading_text = (TextView) convertView.findViewById(R.id.title_panel_heading_text);

                // text_panel
                text_panel_text_message = (TextView) convertView.findViewById(R.id.text_panel_text_message);

            }
        }

        protected class BannerVH extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ProgressBar loadmore_progress;
            private ImageView img_banner_image;
            private TextView txt_banner_header, txt_banner_desc, txt_banner_button;
            private LinearLayout mErrorLayout;

            public BannerVH(View itemView) {
                super(itemView);


                img_banner_image = (ImageView) itemView.findViewById(R.id.img_banner_image);
                txt_banner_header = (TextView) itemView.findViewById(R.id.txt_banner_header);
                txt_banner_desc = (TextView) itemView.findViewById(R.id.txt_banner_desc);
                txt_banner_button = (TextView) itemView.findViewById(R.id.txt_banner_button);

                //  mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

                // mRetryBtn.setOnClickListener(this);
                //  mErrorLayout.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
//                    case R.id.loadmore_retry:
//                    case R.id.loadmore_errorlayout:
//
//                        showRetry(false, null);
//                        mCallback.retryPageLoad();

                    //break;
                }
            }
        }

        protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ProgressBar loadmore_progress;
            private ImageButton mRetryBtn;
            private TextView mErrorTxt;
            private LinearLayout mErrorLayout;

            public LoadingVH(View itemView) {
                super(itemView);

                loadmore_progress = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
                mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
                mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
                mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

                mRetryBtn.setOnClickListener(this);
                mErrorLayout.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.loadmore_retry:
                    case R.id.loadmore_errorlayout:

                        showRetry(false, null);
                        mCallback.retryPageLoad();

                        break;
                }
            }
        }

    }


}
