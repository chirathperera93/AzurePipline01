package com.ayubo.life.ayubolife.walk_and_win;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.huawei_hms.GoogleSupportServices;
import com.ayubo.life.ayubolife.huawei_hms.HuaweiResult;
import com.ayubo.life.ayubolife.huawei_hms.HuaweiScopes;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.bumptech.glide.Glide;
import com.flavors.changes.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
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
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huawei.hms.hihealth.DataController;
import com.huawei.hms.hihealth.HiHealthOptions;
import com.huawei.hms.hihealth.HuaweiHiHealth;
import com.huawei.hms.hihealth.SettingController;
import com.huawei.hms.hihealth.data.SamplePoint;
import com.huawei.hms.hihealth.data.SampleSet;
import com.huawei.hms.support.hwid.HuaweiIdAuthAPIManager;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static java.text.DateFormat.getTimeInstance;

public class WalkWinMainActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mApiClient;

    private static final String TAG = "WalkWinMainActivity";

    ProgressBar circular_progress_bar_walk_win;

    Button walk_and_win_claim_reward_btn;

    private static List<StepObj> stepObjList;

    PrefManager prefManager;

    TextView text_view_progress, walk_and_win_steps, refreshNow, updateTime, walk_win_main_sub_title, header_text_walk_win;

    Integer walkAndWinChallengeSteps = 0;

    ProgressDialog progressDialog, progressDialogForLoading;


    ProgressDialog progressDialogForSubscribe;

    String subscribedTime = "";

    Long subscribedDateTime = 0L;

    Boolean isNowSubscribedChallenge = false;

    public Long challengeStartTime = 0L;
    public Long challengeEndTime = 0L;

    ImageView ad_info_icon, ad_close_icon;

    RelativeLayout claim_reward_ad_layout;

    ImageView walk_win_sponsor_ad;

    String sponsorLink;

    private boolean authInProgress = false;

    String challenge_id = "0";

    NotificationManagerCompat notificationManagerCompat;

    boolean isDisabledClaimRewardButton = true;

    private SettingController mSettingController;

    // Request code for displaying the authorization screen using the startActivityForResult method. The value can be customized.
    private static final int REQUEST_SIGN_IN_LOGIN = 1002;
    private static final int REQUEST_HEALTH_AUTH = 1003;
    private static final int REQUEST_LOGIN_CODE = 1102;
    private static final int ACTIVITY_RECOGNITION = 132;
    private static String HEALTH_SCHEME = "huaweischeme://healthapp/achievement?module=kit";

    private Boolean isLinkedHuawei = false;

    GoogleSignInAccount googleAccount;

    Drawable progressDrawable;
    Drawable backgroundDrawable;
    RelativeLayout tips;

    String gradientStartColor, gradientEndColor = "";

    Long sDateToGoogle, eDateToGoogle = 0L;
    String activityId, device_modal;
    float energDelta2 = 0;
    int userManualSteps = 0;
    int userSensorSteps = 0;

    String buttonTextInActive = "";
    String buttonTextActive = "";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_win_main);

        Intent intent = getIntent();
        challenge_id = intent.getStringExtra("challenge_id");
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());


        walk_and_win_claim_reward_btn = findViewById(R.id.walk_and_win_claim_reward_btn);
        tips = findViewById(R.id.tips);
        walk_and_win_claim_reward_btn.setEnabled(false);
        walk_and_win_claim_reward_btn.setBackgroundResource(R.drawable.rounded_coner_disabled_button_bg);

        circular_progress_bar_walk_win = findViewById(R.id.circular_progress_bar_walk_win);


//        circular_progress_bar_walk_win.getProgressDrawable().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_OUT);


        LayerDrawable progressBarDrawable = (LayerDrawable) circular_progress_bar_walk_win.getProgressDrawable();
        backgroundDrawable = progressBarDrawable.getDrawable(0);
        progressDrawable = progressBarDrawable.getDrawable(1);


        backgroundDrawable.setColorFilter(ContextCompat.getColor(WalkWinMainActivity.this, R.color.color_DDDDDD), PorterDuff.Mode.SRC_IN);


        text_view_progress = findViewById(R.id.text_view_progress);
        header_text_walk_win = findViewById(R.id.header_text_walk_win);
        walk_and_win_steps = findViewById(R.id.walk_and_win_steps);
        ad_info_icon = findViewById(R.id.ad_info_icon);
        ad_close_icon = findViewById(R.id.ad_close_icon);
        walk_win_sponsor_ad = findViewById(R.id.walk_win_sponsor_ad);
        claim_reward_ad_layout = findViewById(R.id.claim_reward_ad_layout);
        walk_win_main_sub_title = findViewById(R.id.walk_win_main_sub_title);
        refreshNow = findViewById(R.id.refreshNow);
        updateTime = findViewById(R.id.updateTime);
        prefManager = new PrefManager(this);
        walkAndWinChallengeSteps = 0;
        claim_reward_ad_layout.setVisibility(View.GONE);

        progressDialogForSubscribe = new ProgressDialog(WalkWinMainActivity.this);
        progressDialog = new ProgressDialog(WalkWinMainActivity.this);
        progressDialogForLoading = new ProgressDialog(WalkWinMainActivity.this);
        subscribedTime = "";
        sponsorLink = "";

        activityId = Ram.getActivityId();
        device_modal = Build.MODEL;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }


        mApiClient = new GoogleApiClient.Builder(getBaseContext())
                .addApi(ActivityRecognition.API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.SENSORS_API)
                .addScope(Fitness.SCOPE_ACTIVITY_READ)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(WalkWinMainActivity.this)
                .addOnConnectionFailedListener(this)
                .build();


//        final SwipeRefreshLayout pullToRefreshWalkWin = findViewById(R.id.pullToRefreshWalkWin);

//        pullToRefreshWalkWin.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshData(pullToRefreshWalkWin);
//            }
//        });

        refreshNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSelectedChallengeData(challenge_id);
//                refreshData(pullToRefreshWalkWin);
//                triggerNotificationWithBackStack(
//                        WalkWinNotifyDetailActivity.class,
//                        getApplicationContext().getString(R.string.default_notification_channel_id),
//                        "Sample Notification",
//                        "This is a sample notification app",
//                        PendingIntent.FLAG_UPDATE_CURRENT);
            }
        });

        ad_close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claim_reward_ad_layout.setVisibility(View.GONE);
            }
        });

        ad_info_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sponsorLink));
                startActivity(browserIntent);
            }
        });

        prefManager.setWalkWinChallengeId(challenge_id);


//        getSelectedChallengeData(challenge_id);


        walk_and_win_claim_reward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialogForLoading();
                WalkWinApiInterface walkWinApiInterface = ApiClient.getClient().create(WalkWinApiInterface.class);

                WalkWinJoinChallengeObj walkWinJoinChallengeObj = new WalkWinJoinChallengeObj();
                walkWinJoinChallengeObj.setChallenge_id(prefManager.getWalkWinChallengeId());

                Call<WalkWinRewardResponse> walkWinClaimRewardRequestCall = walkWinApiInterface.claimReward(AppConfig.APP_BRANDING_ID, prefManager.getUserToken(), walkWinJoinChallengeObj);


                if (Constants.type == Constants.Type.HEMAS) {

                    walkWinClaimRewardRequestCall = walkWinApiInterface.claimRewardHemas(AppConfig.APP_BRANDING_ID, prefManager.getUserToken(), walkWinJoinChallengeObj);
                }


                walkWinClaimRewardRequestCall.enqueue(new retrofit2.Callback<WalkWinRewardResponse>() {

                    @Override
                    public void onResponse(Call<WalkWinRewardResponse> call, Response<WalkWinRewardResponse> response) {
                        progressDialogForLoading.dismiss();


                        if (response.isSuccessful()) {
                            WalkWinRewardResponse walkWinRewardResponse = response.body();
                            JsonObject walkWinRewardDetailJsonObject = new Gson().toJsonTree(walkWinRewardResponse.getData()).getAsJsonObject();

                            Intent intent = new Intent(WalkWinMainActivity.this, WalkWinClaimAwardActivity.class);
                            intent.putExtra("claimRewardResponse", walkWinRewardDetailJsonObject.toString());
                            intent.putExtra("gradientStartColor", gradientStartColor);
                            intent.putExtra("gradientEndColor", gradientEndColor);
                            startActivity(intent);

                        } else {

                            ResponseBody responseBody = response.errorBody();
                            System.out.println(responseBody);


                            JSONObject jObjError = null;
                            try {
                                assert response.errorBody() != null;
                                jObjError = new JSONObject(response.errorBody().string());


                                Object b = jObjError.get("message");


                                System.out.println(b.toString());

                                Toast.makeText(WalkWinMainActivity.this, b.toString(), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


//                            Toast.makeText(WalkWinMainActivity.this, "", Toast.LENGTH_LONG).show();
//                            finish();
                        }


                    }

                    @Override
                    public void onFailure(Call<WalkWinRewardResponse> call, Throwable t) {
                        progressDialogForLoading.dismiss();
                        showToast("Claim reward API  request  failure...!");
                    }
                });


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getSelectedChallengeData(challenge_id);
    }

    //    private void refreshData(SwipeRefreshLayout swipeRefreshLayout) {
//        getSelectedChallengeData(challenge_id);
//        swipeRefreshLayout.setRefreshing(false);
//    }

    private void getSelectedChallengeData(String challengeId) {
        isNowSubscribedChallenge = false;
        subscribedTime = "";
        subscribedDateTime = 0L;
        walk_win_main_sub_title.setText("");
        challengeStartTime = 0L;
        challengeEndTime = 0L;
        walk_win_sponsor_ad.setBackgroundResource(0);
        claim_reward_ad_layout.setVisibility(View.GONE);
        sponsorLink = "";
        text_view_progress.setText("0");
        updateTime.setText("");
        gradientStartColor = "";
        gradientEndColor = "";

        circular_progress_bar_walk_win.setProgress(0);
        walk_and_win_claim_reward_btn = findViewById(R.id.walk_and_win_claim_reward_btn);
        walk_and_win_claim_reward_btn.setEnabled(false);
        walk_and_win_claim_reward_btn.setBackgroundResource(R.drawable.rounded_coner_disabled_button_bg);
        walk_and_win_claim_reward_btn.setTextColor(getResources().getColor(R.color.btn_disabled_font));
        walkAndWinChallengeSteps = 0;

        userManualSteps = 0;
        userSensorSteps = 0;


        switch (challengeId) {
            case "2":
                walk_win_main_sub_title.setText("Weekly Challenge");
                break;

            case "3":
                walk_win_main_sub_title.setText("Monthly Challenge");
                break;

            default:
                walk_win_main_sub_title.setText("Daily Challenge");
                break;
        }


        if (isInternetAvailable(WalkWinMainActivity.this)) {
            showProgressDialogForLoading();
            WalkWinApiInterface walkWinApiInterface = ApiClient.getClient().create(WalkWinApiInterface.class);
            Call<WalkWinStepsResponse> walkWinSaveStepsRequestCall = walkWinApiInterface.getChallengeById(AppConfig.APP_BRANDING_ID, prefManager.getUserToken(), challengeId);


            if (Constants.type == Constants.Type.HEMAS) {
                walkWinSaveStepsRequestCall = walkWinApiInterface.getChallengeByIdForHemas(AppConfig.APP_BRANDING_ID, prefManager.getUserToken(), challengeId);
            }


            walkWinSaveStepsRequestCall.enqueue(new retrofit2.Callback<WalkWinStepsResponse>() {
                @Override
                public void onResponse(Call<WalkWinStepsResponse> call, Response<WalkWinStepsResponse> response) {
                    progressDialogForLoading.dismiss();
                    if (response.isSuccessful()) {
                        WalkWinStepsResponse walkWinStepsResponse = response.body();
                        JsonObject challengeByIdJsonObject = new Gson().toJsonTree(walkWinStepsResponse.getData()).getAsJsonObject();

                        if (challengeByIdJsonObject.get("ch_config").getAsJsonObject().get("tips") != null
                                && !challengeByIdJsonObject.get("ch_config").getAsJsonObject().get("tips").getAsJsonObject().get("action").getAsString().equals("")
                                && !challengeByIdJsonObject.get("ch_config").getAsJsonObject().get("tips").getAsJsonObject().get("meta").getAsString().equals("")) {

                            tips.setVisibility(View.VISIBLE);
                            tips.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    processAction(
                                            challengeByIdJsonObject.get("ch_config").getAsJsonObject().get("tips").getAsJsonObject().get("action").getAsString(),
                                            challengeByIdJsonObject.get("ch_config").getAsJsonObject().get("tips").getAsJsonObject().get("meta").getAsString()
                                    );
                                }
                            });
                        }

                        String circleColor = challengeByIdJsonObject.get("ch_config").getAsJsonObject().get("dark_color").getAsString();


                        buttonTextInActive = challengeByIdJsonObject.get("ch_config").getAsJsonObject().get("button_text").getAsJsonObject().get("disabled").getAsString();
                        buttonTextActive = challengeByIdJsonObject.get("ch_config").getAsJsonObject().get("button_text").getAsJsonObject().get("enabled").getAsString();

                        walk_and_win_claim_reward_btn.setText(buttonTextInActive);


                        gradientStartColor = challengeByIdJsonObject.get("ch_config").getAsJsonObject().get("light_color").getAsString();
                        gradientEndColor = challengeByIdJsonObject.get("ch_config").getAsJsonObject().get("dark_color").getAsString();
                        header_text_walk_win.setText(challengeByIdJsonObject.get("ch_config").getAsJsonObject().get("header_text").getAsString());

                        progressDrawable.setColorFilter(Color.parseColor(circleColor), PorterDuff.Mode.SRC_IN);
                        refreshNow.setTextColor(Color.parseColor(circleColor));


                        challengeStartTime = (long) challengeByIdJsonObject.get("ch_startDateTime").getAsDouble();
                        challengeEndTime = (long) challengeByIdJsonObject.get("ch_endDateTime").getAsDouble();


                        sDateToGoogle = utcToLocal(new Date((long) challengeByIdJsonObject.get("ch_startDateTime").getAsDouble()));
                        eDateToGoogle = utcToLocal(new Date((long) challengeByIdJsonObject.get("ch_endDateTime").getAsDouble()));


                        if (challengeByIdJsonObject.get("sponsor_details") != null) {
                            prefManager.saveWnWSponsorDetail(challengeByIdJsonObject.get("sponsor_details").getAsJsonObject());
                        } else {
                            prefManager.saveWnWSponsorDetail(new JsonObject());
                        }


                        if (challengeByIdJsonObject.get("sponsor_details") != null && challengeByIdJsonObject.get("sponsor_details").getAsJsonObject().get("sp_creatives") != null) {
                            sponsorLink = challengeByIdJsonObject.get("sponsor_details").getAsJsonObject().get("sp_creatives").getAsJsonObject().get("click_url").getAsString();
                            Glide.with(getBaseContext()).load(challengeByIdJsonObject.get("sponsor_details").getAsJsonObject().get("sp_creatives").getAsJsonObject().get("image_url").getAsString()).centerCrop().into(walk_win_sponsor_ad);
                            claim_reward_ad_layout.setVisibility(View.VISIBLE);
//                            new WalkWinMainActivity.GetImageFromServer().execute(challengeByIdJsonObject.get("sponsor_details").getAsJsonObject().get("sp_creatives").getAsJsonObject().get("image_url").getAsString());

                        }

                        if (challengeByIdJsonObject.get("claim_details") != null) {
                            isDisabledClaimRewardButton = true;
                        } else {
                            isDisabledClaimRewardButton = false;
                        }


                        if (challengeByIdJsonObject.get("user_details") != null) {
                            subscribedDateTime = challengeByIdJsonObject.get("user_details").getAsJsonObject().get("chj_startDateTime").getAsLong();

                            if (subscribedDateTime > sDateToGoogle) {


//                                subscribedTime = convertTime(subscribedDateTime, true);
                                sDateToGoogle = subscribedDateTime;


                            } else if (subscribedDateTime < sDateToGoogle) {
                                sDateToGoogle = utcToLocal(new Date((long) challengeByIdJsonObject.get("ch_startDateTime").getAsDouble()));
                            }

                            getStepsWithGoogleClient(challengeByIdJsonObject);
                        } else {
                            showProgressDialogForSubscribe();
                            GradientDrawable gd = new GradientDrawable(
                                    GradientDrawable.Orientation.TOP_BOTTOM,
                                    new int[]{Color.parseColor(gradientStartColor), Color.parseColor(gradientEndColor)});
                            gd.setCornerRadius(25f);
                            switch (challengeId) {
                                case "2":
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.popup_header_liner_layout).setBackgroundDrawable(gd);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_and_win_subscribed_ok_btn).setBackgroundDrawable(gd);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_win_subscribe_title_icon).setBackgroundResource(R.drawable.weekly_reward_icon);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_win_daily_subscribe_title).setVisibility(View.GONE);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_win_weekly_subscribe_title).setVisibility(View.VISIBLE);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_win_monthly_subscribe_title).setVisibility(View.GONE);
                                    break;

                                case "3":
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.popup_header_liner_layout).setBackgroundDrawable(gd);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_and_win_subscribed_ok_btn).setBackgroundDrawable(gd);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_win_subscribe_title_icon).setBackgroundResource(R.drawable.monthly_reward_icon);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_win_daily_subscribe_title).setVisibility(View.GONE);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_win_weekly_subscribe_title).setVisibility(View.GONE);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_win_monthly_subscribe_title).setVisibility(View.VISIBLE);
                                    break;

                                default:
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.popup_header_liner_layout).setBackgroundDrawable(gd);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_and_win_subscribed_ok_btn).setBackgroundDrawable(gd);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_win_subscribe_title_icon).setBackgroundResource(R.drawable.daily_reward_icon);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_win_daily_subscribe_title).setVisibility(View.VISIBLE);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_win_weekly_subscribe_title).setVisibility(View.GONE);
                                    progressDialogForSubscribe.getWindow().findViewById(R.id.walk_win_monthly_subscribe_title).setVisibility(View.GONE);
                                    break;
                            }

                            progressDialogForSubscribe.getWindow().findViewById(R.id.walk_and_win_subscribed_ok_btn).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialogForSubscribe.dismiss();
//                                    getStepsWithGoogleClient(challengeByIdJsonObject);

                                    showProgressDialogForLoading();

                                    Long userJoinDate = new Date().getTime();
                                    Long userEndDate = 0L;


                                    if (challengeByIdJsonObject.get("ch_isRecurring").getAsBoolean()) {
                                        userEndDate = userJoinDate;
                                    } else {
                                        userEndDate = challengeEndTime;
                                    }


                                    WalkWinJoinChallengeObj walkWinJoinChallengeObj = new WalkWinJoinChallengeObj();
                                    walkWinJoinChallengeObj.setChallenge_id(challengeId.toString());
                                    walkWinJoinChallengeObj.setChj_startDateTime(userJoinDate);
                                    walkWinJoinChallengeObj.setChj_endDateTime(userEndDate);
                                    walkWinJoinChallengeObj.setUser_id(prefManager.getLoginUser().get("uid"));
                                    walkWinJoinChallengeObj.setChj_timezone("");

                                    WalkWinApiInterface walkWinApiInterface = ApiClient.getClient().create(WalkWinApiInterface.class);
                                    Call<WalkWinStepsResponse> joinChallengeRequestCall = walkWinApiInterface.joinChallenge(AppConfig.APP_BRANDING_ID, prefManager.getUserToken(), walkWinJoinChallengeObj);


                                    if (Constants.type == Constants.Type.HEMAS) {
                                        joinChallengeRequestCall = walkWinApiInterface.joinChallengeHemas(AppConfig.APP_BRANDING_ID, prefManager.getUserToken(), walkWinJoinChallengeObj);
                                    }


                                    joinChallengeRequestCall.enqueue(new retrofit2.Callback<WalkWinStepsResponse>() {

                                        @Override
                                        public void onResponse(Call<WalkWinStepsResponse> call, Response<WalkWinStepsResponse> response) {
                                            progressDialogForLoading.dismiss();
                                            if (response.isSuccessful()) {
                                                WalkWinStepsResponse joinChallengeResponse = response.body();
                                                JsonObject joinChallengeJsonObject = new Gson().toJsonTree(joinChallengeResponse.getData()).getAsJsonObject();
                                                subscribedTime = "";
                                                subscribedTime = convertTime((long) joinChallengeJsonObject.get("chj_startDateTime").getAsDouble(), true);
                                                isNowSubscribedChallenge = true;
                                                subscribedDateTime = joinChallengeJsonObject.get("chj_startDateTime").getAsLong();

                                                sDateToGoogle = subscribedDateTime;

                                                getStepsWithGoogleClient(challengeByIdJsonObject);

                                            } else {
                                                System.out.println(response.errorBody());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<WalkWinStepsResponse> call, Throwable t) {
                                            System.out.println(t.getMessage());
                                        }
                                    });
                                }
                            });

                            progressDialogForSubscribe.getWindow().findViewById(R.id.walk_and_win_subscribe_not_now_btn).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialogForSubscribe.dismiss();
//                                    finish();
                                }
                            });

                        }


                    } else {
                        showToast("Get challenge API response failure");
//                        finish();
                    }
                }

                @Override
                public void onFailure(Call<WalkWinStepsResponse> call, Throwable t) {
                    showToast("Get challenge API failure...!");
//                    finish();
                }
            });
        } else {
            showToast("Unable to detect an active internet connection");
        }


    }

    public int getDateComparingValue(long subscribedTime, long challengeStartTime) {

        Date subscribedTimeNew = new Date(subscribedTime);
        Date challengeStartTimeNew = new Date(challengeStartTime);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String subsDate = formatter.format(subscribedTimeNew);
        String challengeDate = formatter.format(challengeStartTimeNew);

        return subsDate.compareTo(challengeDate);
    }

    public static Long utcToLocal(Date utcDate) {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        return utcDate.getTime() - offsetInMillis;
    }

    public static Long LocalToUtc(Date utcDate) {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        return utcDate.getTime() + offsetInMillis;
    }

//    public class GetImageFromServer extends AsyncTask<String, Void, Bitmap> {
//
//        private Bitmap image;
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//
//            try {
//                URL url = new URL(params[0].trim());
//                URLConnection urlConnection = url.openConnection();
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 2;
//                image = BitmapFactory.decodeStream(urlConnection.getInputStream(), null, options);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return image;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//
//            if (result != null) {
//                Drawable drawable = new BitmapDrawable(result);
//                walk_win_sponsor_ad.setBackgroundDrawable(drawable);
//                claim_reward_ad_layout.setVisibility(View.VISIBLE);
//            } else {
//                claim_reward_ad_layout.setVisibility(View.GONE);
//            }
//
//        }
//
//
//    }

    private void showProgressDialogForSubscribe() {
        progressDialogForSubscribe.show();
        progressDialogForSubscribe.setCancelable(false);
        progressDialogForSubscribe.getWindow().setGravity(Gravity.CENTER);
        progressDialogForSubscribe.setContentView(R.layout.walk_win_subscribe_progress_dialog);
        progressDialogForSubscribe.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
    }

    private void showProgressDialogForLoading() {
        progressDialogForLoading.show();
        progressDialogForLoading.setCancelable(false);
        progressDialogForLoading.getWindow().setGravity(Gravity.CENTER);
        progressDialogForLoading.setContentView(R.layout.progress_dialog_for_loading);
        progressDialogForLoading.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
    }

    private void getStepsWithGoogleClient(JsonObject challengeByIdJsonObject) {
        showProgressDialog();
        walkAndWinChallengeSteps = challengeByIdJsonObject.get("ch_stepGoal").getAsInt();


        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(challengeByIdJsonObject.get("ch_stepGoal").getAsInt());

        walk_and_win_steps.setText("Goal " + yourFormattedString + " Steps");


        GoogleSupportServices googleSupportServices = new GoogleSupportServices(WalkWinMainActivity.this);


        if (googleSupportServices.isGooglePlayServicesAvailable()) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(WalkWinMainActivity.this);
            if (account == null) {
                signIn();
            } else {
                googleAccount = account;
                subscribeToFitnessData(account);
            }
        } else {
            // Huawei Sign In
            huaweiInitService();
            connectHuaweiSignIn();
        }


    }

    public void huaweiInitService() {
        HiHealthOptions fitnessOptions = HiHealthOptions.builder().build();
        AuthHuaweiId signInHuaweiId = HuaweiIdAuthManager.getExtendedAuthResult(fitnessOptions);
        mSettingController = HuaweiHiHealth.getSettingController(getApplicationContext(), signInHuaweiId);
    }

    public void connectHuaweiSignIn() {
/**
 * Sign-in and authorization method. The authorization screen will display if the current account has not granted authorization.
 */
        Log.i(TAG, "begin sign in");
        List<com.huawei.hms.support.api.entity.auth.Scope> scopeList = new ArrayList<>();

        HuaweiScopes huaweiScopes = new HuaweiScopes();

        // Add scopes to apply for. The following only shows an example. You need to add scopes according to your specific needs.
        scopeList.add(huaweiScopes.getHealthKitBoth()); // View and save step counts in HUAWEI Health Kit.
        scopeList.add(huaweiScopes.getHealthKitHeartRateBoth()); // View and save height and weight in HUAWEI Health Kit.
        scopeList.add(huaweiScopes.getHealthKitWeightBoth()); // View and save the heart rate data in HUAWEI Health Kit.
        scopeList.add(huaweiScopes.getHealthKitStepRealTime());

        // Configure authorization parameters.
        HuaweiIdAuthParamsHelper authParamsHelper = new HuaweiIdAuthParamsHelper(
                HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM);
        HuaweiIdAuthParams authParams = authParamsHelper.setIdToken()
                .setAccessToken()
                .setScopeList(scopeList)
                .createParams();

        // Initialize the HuaweiIdAuthService object.
        final HuaweiIdAuthService huaweiAuthService = HuaweiIdAuthManager.getService(getApplicationContext(), authParams);

        // Silent sign-in. If authorization has been granted by the current account, the authorization screen will not display. This is an asynchronous method.
        com.huawei.hmf.tasks.Task<AuthHuaweiId> authHuaweiIdTask = huaweiAuthService.silentSignIn();

        // Add the callback for the call result.
        authHuaweiIdTask.addOnSuccessListener(new com.huawei.hmf.tasks.OnSuccessListener<AuthHuaweiId>() {
            @Override
            public void onSuccess(AuthHuaweiId huaweiId) {

                // The silent sign-in is successful.
                Log.i(TAG, "silentSignIn success");
//                loadMainServiceForGoals();
                //=======Sending and afterthat Getting Latest Steps===========================
//                GetSteps task = new GetSteps();
//                task.execute();

                startActivityForResult(huaweiAuthService.getSignInIntent(), REQUEST_LOGIN_CODE);

            }
        }).addOnFailureListener(new com.huawei.hmf.tasks.OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // The silent sign-in fails. This indicates that the authorization has not been granted by the current account.
                if (exception instanceof com.huawei.hms.common.ApiException) {
                    com.huawei.hms.common.ApiException apiException = (com.huawei.hms.common.ApiException) exception;
                    Log.i(TAG, "sign failed status:" + apiException.getStatusCode());
                    Log.i(TAG, "begin sign in by intent");

                    // Call the sign-in API using the getSignInIntent() method.
                    Intent signInIntent = huaweiAuthService.getSignInIntent();

                    // Display the authorization screen by using the startActivityForResult() method of the activity.
                    // You can change HihealthKitMainActivity to the actual activity.
                    startActivityForResult(signInIntent, REQUEST_SIGN_IN_LOGIN);
                }
            }
        });

    }

    private void showProgressDialog() {
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.setContentView(R.layout.new_progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
    }

    public void showToast(String message) {
        progressDialog.dismiss();
        progressDialogForLoading.dismiss();
        progressDialogForSubscribe.dismiss();
        Toast.makeText(WalkWinMainActivity.this, message, Toast.LENGTH_SHORT).show();
//        final SwipeRefreshLayout pullToRefreshWalkWin = findViewById(R.id.pullToRefreshWalkWin);
//        pullToRefreshWalkWin.setRefreshing(false);
    }

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


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_OAUTH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        GoogleSupportServices googleSupportServices = new GoogleSupportServices(WalkWinMainActivity.this);

        if (googleSupportServices.isGooglePlayServicesAvailable()) {
            // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            if (requestCode == REQUEST_OAUTH) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                authInProgress = false;
                if (resultCode == RESULT_OK) {
                    if (!mApiClient.isConnecting() && !mApiClient.isConnected()) {
                        mApiClient.connect();
                    } else {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Log.e("GoogleFit", "RESULT_CANCELED");
                    showToast("RESULT_CANCELED");
                }

            } else {
                showToast("Google signing not available");
            }
        } else {
            if (requestCode == REQUEST_LOGIN_CODE) {
                com.huawei.hmf.tasks.Task<AuthHuaweiId> huaweiIdAuthManager = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
                if (huaweiIdAuthManager.isSuccessful()) {
                    HuaweiResult huaweiResult = new HuaweiResult();
                    huaweiResult.authResults = HuaweiIdAuthAPIManager.HuaweiIdAuthAPIService.parseHuaweiIdFromIntent(data);
                    if (!isLinkedHuawei) {
                        checkAndAuthorizeHealthApp();
                    }

                } else if (huaweiIdAuthManager.isCanceled()) {
                    Toast.makeText(WalkWinMainActivity.this, "Authorization was not granted", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_HEALTH_AUTH) {
                Log.i(TAG, "REQUEST_WAS_SUCCESS");
                GetHuaweiSteps task = new GetHuaweiSteps();
                task.execute();
            } else {
                System.out.println("else");
            }
        }

    }

    private void checkAndAuthorizeHealthApp() {
        Log.i(TAG, "Begin to checkOrAuthorizeHealthApp");
        mSettingController = HuaweiHiHealth.getSettingController(getApplicationContext(),
                HuaweiIdAuthManager.getExtendedAuthResult(
                        HiHealthOptions
                                .builder()
                                .build()
                ));
        com.huawei.hmf.tasks.Task<Boolean> mSettingControllerQueryTask = mSettingController.getHealthAppAuthorization();
        mSettingControllerQueryTask.addOnSuccessListener(new com.huawei.hmf.tasks.OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (Boolean.TRUE.equals(result)) {
                    Log.i(TAG, "queryHealthAuthorization get result is authorized");
                    Uri uri = Uri.parse(HEALTH_SCHEME);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(Objects.requireNonNull(Objects.requireNonNull(getApplicationContext())).getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_HEALTH_AUTH);

                    }
                } else {
                    Log.i(TAG, "queryHealthAuthorization get result is unauthorized");
                    Toast.makeText(getApplicationContext(), "Please link the app with Huawei Health", Toast.LENGTH_LONG).show();
                    Uri uri = Uri.parse(HEALTH_SCHEME);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(Objects.requireNonNull(Objects.requireNonNull(getApplicationContext())).getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_HEALTH_AUTH);

                    }
                }
            }
        }).addOnFailureListener(new com.huawei.hmf.tasks.OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                if (exception != null) {
                    Log.i(TAG, "queryHealthAuthorization has exception");
                    Toast.makeText(getApplicationContext(), "Health Authorization has exception", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private class GetHuaweiSteps extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            stepObjList = new ArrayList<StepObj>();

            HiHealthOptions hiHealthOptions = HiHealthOptions.builder()
                    .addDataType(com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA, HiHealthOptions.ACCESS_READ)
                    .addDataType(com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA, HiHealthOptions.ACCESS_WRITE)
                    .build();
            AuthHuaweiId signInHuaweiId = HuaweiIdAuthManager.getExtendedAuthResult(hiHealthOptions);

            DataController dataController = HuaweiHiHealth.getDataController(WalkWinMainActivity.this, signInHuaweiId);

            getHuaweiStepsWithDates(dataController);

            return null;
        }


//        @Override
//        protected void onPostExecute(String nowSteps) {
//
//            if (pref.getUserData().get("steps") != null) {
//                String lastStep = pref.getUserData().get("steps");
//
//                // if(Integer.parseInt(nowSteps)>Integer.parseInt(lastStep)){
//                sendLatestStepsToServer(nowSteps);
//                // }
//            }
//
//
//        }

    }

    public void getHuaweiStepsWithDates(DataController dataController) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long startTime = cal.getTimeInMillis();

        com.huawei.hmf.tasks.Task<SampleSet> daliySummationTask = dataController.readDailySummation(com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA, Integer.parseInt(dateFormat.format(challengeStartTime)), Integer.parseInt(dateFormat.format(challengeEndTime)));
        daliySummationTask.addOnSuccessListener(new com.huawei.hmf.tasks.OnSuccessListener<SampleSet>() {
            @Override
            public void onSuccess(SampleSet sampleSet) {
                logger("Success read daily summation from HMS core");
                if (sampleSet != null) {
                    showSampleSet(sampleSet);
                }
            }
        });
        daliySummationTask.addOnFailureListener(new com.huawei.hmf.tasks.OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                logger("readTodaySummation" + e.toString());
            }
        });

    }

    public Long getStartDateString(long startDate) {
        Date subscribedTimeNew = new Date(startDate);
        subscribedTimeNew.setHours(0);
        subscribedTimeNew.setMinutes(0);
        subscribedTimeNew.setSeconds(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(subscribedTimeNew);
        return calendar.getTimeInMillis();
    }

    public Long getEndDateString(long endDate) {
        Date subscribedTimeNew = new Date(endDate);
        subscribedTimeNew.setHours(23);
        subscribedTimeNew.setMinutes(59);
        subscribedTimeNew.setSeconds(59);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(subscribedTimeNew);
        return calendar.getTimeInMillis();
    }

    private void showSampleSet(SampleSet sampleSet) {


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (sampleSet.getSamplePoints().size() > 0) {
            for (SamplePoint samplePoint : sampleSet.getSamplePoints()) {
                logger("Sample point type: " + samplePoint.getDataType().getName());
                logger("Start: " + dateFormat.format(samplePoint.getStartTime(TimeUnit.MILLISECONDS)));
                logger("End: " + dateFormat.format(new Date(samplePoint.getEndTime(TimeUnit.MILLISECONDS))));
                for (com.huawei.hms.hihealth.data.Field field : samplePoint.getDataType().getFields()) {
                    String energy, calories, distance;
                    logger("Field: " + field.getName() + " Value: " + samplePoint.getFieldValue(field));
                    StepObj stepObj = new StepObj();
                    stepObj.setStepCount(samplePoint.getFieldValue(field).asIntValue());
                    stepObj.setStartDateTime(samplePoint.getStartTime(TimeUnit.MILLISECONDS));
                    stepObj.setEndDateTime(samplePoint.getEndTime(TimeUnit.MILLISECONDS));

                    String versionName = BuildConfig.VERSION_NAME;
                    int value = stepObj.getStepCount();
                    double energyVal = (value / 130) * 4.5;
                    energy = String.format("%.0f", energyVal);
                    double caloriesVal = (energyVal * 3.5 * 70) / 200;
                    calories = String.format("%.0f", caloriesVal);
                    double distanceVal = (value * 78) / (float) 100000;
                    distance = String.format("%.2f", distanceVal);


                    stepObj.setStartDateTime(getStartDateString(challengeStartTime));
                    stepObj.setEndDateTime(getEndDateString(challengeEndTime));
                    stepObj.setChallenge_id(challenge_id);
                    stepObj.setActivity("activity_AYUBO");
                    stepObj.setEnergy(energy);
                    stepObj.setCalorie(calories);
                    stepObj.setDuration("0");
                    stepObj.setDistance(distance);
                    stepObj.setWalk_count(stepObj.getStepCount().toString());
                    stepObj.setRun_count("11");
                    stepObj.setVersion(versionName);
                    stepObj.setOsType("android");
                    stepObj.setDevice_modal(device_modal);

                    stepObjList.add(stepObj);
                }
            }
        } else {
            String energy, calories, distance;
            StepObj stepObj = new StepObj();

            stepObj.setStepCount(0);

            String versionName = BuildConfig.VERSION_NAME;
            int value = stepObj.getStepCount();
            double energyVal = (value / 130) * 4.5;
            energy = String.format("%.0f", energyVal);
            double caloriesVal = (energyVal * 3.5 * 70) / 200;
            calories = String.format("%.0f", caloriesVal);
            double distanceVal = (value * 78) / (float) 100000;
            distance = String.format("%.2f", distanceVal);


            stepObj.setStartDateTime(getStartDateString(challengeStartTime));
            stepObj.setEndDateTime(getEndDateString(challengeEndTime));
            stepObj.setChallenge_id(challenge_id);
            stepObj.setActivity("activity_AYUBO");
            stepObj.setEnergy(energy);
            stepObj.setCalorie(calories);
            stepObj.setDuration("0");
            stepObj.setDistance(distance);
            stepObj.setWalk_count(stepObj.getStepCount().toString());
            stepObj.setRun_count("11");
            stepObj.setVersion(versionName);
            stepObj.setOsType("android");
            stepObj.setDevice_modal(device_modal);

            stepObjList.add(stepObj);
        }


        SaveGoogleStepsToServer task = new SaveGoogleStepsToServer();
        task.execute(stepObjList);
    }

    private void logger(String string) {
        Log.i("DataController", string);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            googleAccount = account;
            subscribeToFitnessData(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            showToast("signInResult:failed code=" + e.getStatusCode());
            showToast(e.getMessage());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTIVITY_RECOGNITION) {
            getFitnessData(googleAccount);
        }
    }

    public void subscribeToFitnessData(GoogleSignInAccount googleSignInAccount) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            System.out.println("Granted");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, ACTIVITY_RECOGNITION);
        } else {
            getFitnessData(googleSignInAccount);
        }


    }

    private void getFitnessData(GoogleSignInAccount googleSignInAccount) {
        Fitness.getRecordingClient(getApplicationContext(), googleSignInAccount)
                .subscribe(DataType.TYPE_ACTIVITY_SEGMENT)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully subscribed!");
                        createGoogleClient();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "There was a problem subscribing.");
                        showToast("There was a problem subscribing.");
                    }
                });
    }

    private void createGoogleClient() {
        if (!mApiClient.isConnecting() && !mApiClient.isConnected()) {
            mApiClient.connect();
        } else {
            GetGoogleFitSteps task = new GetGoogleFitSteps();
            task.execute();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        GetGoogleFitSteps task = new GetGoogleFitSteps();
        task.execute();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!authInProgress) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult(WalkWinMainActivity.this, REQUEST_OAUTH);
            } catch (IntentSender.SendIntentException e) {

            }
        } else {
            Log.e("GoogleFit", "authInProgress");
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private class GetGoogleFitSteps extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            String steps = "0";
            String energy, calories, distance;
            stepObjList = new ArrayList<StepObj>();
            DataReadRequest readRequest = queryFitnessDataForChallenge(sDateToGoogle, eDateToGoogle);
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mApiClient, readRequest).await(1, TimeUnit.SECONDS);
            steps = getReadResultData(dataReadResult);
            steps = String.valueOf(userSensorSteps);
            StepObj stepObj = new StepObj();


            stepObj.setStepCount(Integer.parseInt(steps));


            String versionName = BuildConfig.VERSION_NAME;
            int value = stepObj.getStepCount();
            double energyVal = (value / 130) * 4.5;
            energy = String.format("%.0f", energyVal);
            double caloriesVal = (energyVal * 3.5 * 70) / 200;
            calories = String.format("%.0f", caloriesVal);
            double distanceVal = (value * 78) / (float) 100000;
            distance = String.format("%.2f", distanceVal);


            stepObj.setStartDateTime(sDateToGoogle);
            stepObj.setEndDateTime(eDateToGoogle);
            stepObj.setChallenge_id(challenge_id);


            stepObj.setActivity("activity_AYUBO");
            stepObj.setEnergy(energy);
            stepObj.setCalorie(calories);
            stepObj.setDuration("0");
            stepObj.setDistance(distance);
            stepObj.setWalk_count(steps);
            stepObj.setRun_count("11");
            stepObj.setVersion(versionName);
            stepObj.setOsType("android");
            stepObj.setDevice_modal(device_modal);


            stepObjList.add(stepObj);

            return steps;
        }

        @Override
        protected void onPostExecute(String nowSteps) {
            SaveGoogleStepsToServer task = new SaveGoogleStepsToServer();
            task.execute(stepObjList);

        }


    }

    public DataReadRequest queryFitnessDataForChallenge(Long cStart, Long cEnd) {

        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(ESTIMATED_STEP_DELTAS, DataType.TYPE_STEP_COUNT_DELTA)
                .bucketByTime(24, TimeUnit.HOURS)
                .setTimeRange(sDateToGoogle, eDateToGoogle, TimeUnit.MILLISECONDS)
                .build();

        if (challenge_id.equals("1")) {
            readRequest = new DataReadRequest.Builder()
                    .aggregate(ESTIMATED_STEP_DELTAS, DataType.TYPE_STEP_COUNT_DELTA)
                    .bucketByTime(24, TimeUnit.HOURS)
                    .setTimeRange(sDateToGoogle, eDateToGoogle, TimeUnit.MILLISECONDS)
                    .build();
        }
        if (challenge_id.equals("3")) {
            readRequest = new DataReadRequest.Builder()
                    .aggregate(ESTIMATED_STEP_DELTAS, DataType.TYPE_STEP_COUNT_DELTA)
                    .bucketByTime(7, TimeUnit.DAYS)
                    .setTimeRange(sDateToGoogle, eDateToGoogle, TimeUnit.MILLISECONDS)
                    .build();
        }
        return readRequest;
    }

    public String getReadResultData(DataReadResult dataReadResult) {
        String step = "0";
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();

                for (DataSet dataSet : dataSets) {
                    System.out.println(dataSet);

                    step = dumpDataSet(dataSet);
                    System.out.println(step);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                step = dumpDataSet(dataSet);
                System.out.println(step);
            }

        }
        return step;
    }


    private String dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();
        String stepCount = null;
        if (dataSet.isEmpty()) {

            stepCount = "0";
        } else {
            System.out.println(dataSet.getDataPoints());
            System.out.println(dataSet.getDataPoints());
            for (DataPoint dp : dataSet.getDataPoints()) {
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());

                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));

                for (Field field : dp.getDataType().getFields()) {

                    System.out.println(dp.getOriginalDataSource().getStreamName().equals("user_input"));

                    if (dp.getOriginalDataSource().getStreamName().equals("user_input")) {


                        userManualSteps = userManualSteps + Integer.parseInt(dp.getValue(field).toString());


                    } else {

                        userSensorSteps = userSensorSteps + Integer.parseInt(dp.getValue(field).toString());


                    }

                    Log.i(TAG, "\tField: " + field.getName() + "GFit Steps Value: " + dp.getValue(field));
                    stepCount = "0";
                    stepCount = dp.getValue(field).toString();
                }
            }
        }


        return stepCount;
    }

    private class SaveGoogleStepsToServer extends AsyncTask<List<StepObj>, Void, Void> {

        @Override
        protected Void doInBackground(List<StepObj>... lists) {
            List<StepObj> stepObjectList = lists[0];
            if (stepObjectList.size() > 0) {
                saveWalkWinDailySteps(stepObjectList);
            } else {
                StepObj aaa = new StepObj();
                String energy, calories, distance;
                aaa.setStepCount(0);
                if (isNowSubscribedChallenge) {
                    aaa.setStartDateTime(LocalToUtc(new Date(sDateToGoogle)));
                } else {
                    aaa.setStartDateTime(getStartDateString(challengeStartTime));
                }

                aaa.setEndDateTime(getEndDateString(challengeEndTime));
                aaa.setChallenge_id(challenge_id);
                aaa.setActivity("activity_AYUBO");


                String versionName = BuildConfig.VERSION_NAME;
                int value = aaa.getStepCount();
                double energyVal = (value / 130) * 4.5;
                energy = String.format("%.0f", energyVal);
                double caloriesVal = (energyVal * 3.5 * 70) / 200;
                calories = String.format("%.0f", caloriesVal);
                double distanceVal = (value * 78) / (float) 100000;
                distance = String.format("%.2f", distanceVal);


                aaa.setEnergy(energy);
                aaa.setCalorie(calories);
                aaa.setDuration("0");
                aaa.setDistance(distance);
                aaa.setWalk_count(aaa.getStepCount().toString());
                aaa.setRun_count("11");
                aaa.setVersion(versionName);
                aaa.setOsType("android");
                aaa.setDevice_modal(device_modal);


                stepObjectList.add(aaa);

                saveWalkWinDailySteps(stepObjectList);
            }
            return null;
        }
    }

    private void saveWalkWinDailySteps(List<StepObj> obj) {
        WalkWinApiInterface walkWinApiInterface = ApiClient.getClient().create(WalkWinApiInterface.class);

        TimeZone timeZone = TimeZone.getDefault();
        String timeZoneId = timeZone.getID();


        Call<WalkWinStepsResponse> walkWinSaveStepsRequestCall = walkWinApiInterface.saveDailySteps(AppConfig.APP_BRANDING_ID, prefManager.getUserToken(), obj, timeZoneId);


        if (Constants.type == Constants.Type.HEMAS) {
            walkWinSaveStepsRequestCall = walkWinApiInterface.saveDailyStepsForHemas(AppConfig.APP_BRANDING_ID, prefManager.getUserToken(), obj);
        }


        walkWinSaveStepsRequestCall.enqueue(new retrofit2.Callback<WalkWinStepsResponse>() {

            @Override
            public void onResponse(Call<WalkWinStepsResponse> call, Response<WalkWinStepsResponse> response) {
                if (response.isSuccessful()) {
                    String timeUpdated = convertTime(0L, false);
                    updateTime.setText("Last updated " + timeUpdated + ". ");
                    getTotalStepsByDates(obj);

                } else {
                    showToast("Save daily step API response failure...");
                }
            }

            @Override
            public void onFailure(Call<WalkWinStepsResponse> call, Throwable t) {
                showToast("Save daily step API failure...");
            }
        });
    }

    public String convertTime(long time, boolean isTimeWithSecond) {
        String dateTimeValue = "";
        if (!isTimeWithSecond) {
            Calendar cal = Calendar.getInstance();
            Date now = new Date();
            cal.setTime(now);
            Date date = new Date(cal.getTimeInMillis());
            Format format = new SimpleDateFormat("hh:mm a");

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String strDate = formatter.format(date);

            Date todayDate = new Date();
            String strTodayDate = formatter.format(todayDate);

            int comparedValue = strTodayDate.compareTo(strDate);


            if (comparedValue == 0) {
                dateTimeValue = "today at " + format.format(date);
            } else if (comparedValue == -1) {
                dateTimeValue = "yesterday at " + format.format(date);
            } else {
                dateTimeValue = "at " + strDate + " " + format.format(date);
            }

            return dateTimeValue;
        } else {
            Date date = new Date(time);
            Format format = new SimpleDateFormat("HH:mm:ss");
            return format.format(date);
        }


    }

    private void getTotalStepsByDates(List<StepObj> stepObjList) {

        progressDialog.dismiss();
        int totalWalkedSteps = 0;

        for (int x = 0; x < stepObjList.size(); x++) {
            totalWalkedSteps = totalWalkedSteps + stepObjList.get(x).getStepCount();
        }

        float totalStepMultiplyValue = (totalWalkedSteps * 100);
        float totalStepsPercentage = totalStepMultiplyValue / walkAndWinChallengeSteps;

        startAnimationCounter(0, (int) totalStepsPercentage);
        startAnimationCounterForTextView(0, totalWalkedSteps);


        if (totalWalkedSteps >= walkAndWinChallengeSteps) {
            if (!isDisabledClaimRewardButton) {

                GradientDrawable gd = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{Color.parseColor(gradientStartColor), Color.parseColor(gradientEndColor)});
                gd.setCornerRadius(25f);

                walk_and_win_claim_reward_btn.setText(buttonTextActive);
                walk_and_win_claim_reward_btn.setEnabled(true);
                walk_and_win_claim_reward_btn.setBackgroundDrawable(gd);
                walk_and_win_claim_reward_btn.setTextColor(getResources().getColor(R.color.white));


            } else {
                Toast.makeText(WalkWinMainActivity.this, "You have already claimed your reward", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void startAnimationCounter(int start_no, int end_no) {
        ValueAnimator animator = ValueAnimator.ofInt(start_no, end_no);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circular_progress_bar_walk_win.setProgress(Integer.parseInt(animation.getAnimatedValue().toString()));
            }
        });
        animator.start();
    }

    public void startAnimationCounterForTextView(int start_no, int end_no) {
        ValueAnimator animator = ValueAnimator.ofInt(start_no, end_no);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                String yourFormattedString = formatter.format(animation.getAnimatedValue());
                text_view_progress.setText(yourFormattedString);
            }
        });
        animator.start();
    }


}