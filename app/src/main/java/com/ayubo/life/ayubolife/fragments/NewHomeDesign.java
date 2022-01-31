package com.ayubo.life.ayubolife.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.BuildConfig;
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
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity;
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters;
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction;
import com.ayubo.life.ayubolife.common.SetDiscoverPage;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity;
import com.ayubo.life.ayubolife.goals.AchivedGoal_Activity;
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity;
import com.ayubo.life.ayubolife.goals_extention.DashBoard_Activity;
import com.ayubo.life.ayubolife.goals_extention.ShareGoals_Activity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.home.EatViewActivity;
import com.ayubo.life.ayubolife.home.RelaxViewActivity;
import com.ayubo.life.ayubolife.home_group_view.GroupViewActivity;
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity;
import com.ayubo.life.ayubolife.huawei_hms.GoogleSupportServices;
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.title_clicked.PendingAnalysisOkActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.title_clicked.TitleClickedActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.welcome.JanashakthiWelcomeActivity;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.ayubo.life.ayubolife.lifeplus.ProfileNew;
import com.ayubo.life.ayubolife.lifepoints.LifePointActivity;
import com.ayubo.life.ayubolife.login.LoginActivity_First;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.map_challange.MapChallangeKActivity;
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity;
import com.ayubo.life.ayubolife.map_challenges.MapJoinChallenge_Activity;
import com.ayubo.life.ayubolife.map_challenges.activity.NewLeaderBoardActivity;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.NewHomeMenuOneObj;
import com.ayubo.life.ayubolife.model.RoadLocationObj;
import com.ayubo.life.ayubolife.notification.activity.SingleTimeline_Activity;
import com.ayubo.life.ayubolife.notification.model.NotiCountMainData;
import com.ayubo.life.ayubolife.notification.model.NotiCountMainResponse;
import com.ayubo.life.ayubolife.payment.activity.PaymentActivity;
import com.ayubo.life.ayubolife.pojo.TokenMainResponse;
import com.ayubo.life.ayubolife.pojo.timeline.Post;
import com.ayubo.life.ayubolife.pojo.timeline.main.Banner;
import com.ayubo.life.ayubolife.pojo.timeline.main.Goal;
import com.ayubo.life.ayubolife.pojo.timeline.main.QuickLink;
import com.ayubo.life.ayubolife.pojo.timeline.main.TopTile;
import com.ayubo.life.ayubolife.post.activity.NativePostActivity;
import com.ayubo.life.ayubolife.post.activity.NativePostJSONActivity;
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity;
import com.ayubo.life.ayubolife.prochat.data.model.Conversation;
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
import com.ayubo.life.ayubolife.timeline.bottummenu.HealthViewActivity;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huawei.hms.hihealth.SettingController;

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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.ayubo.life.ayubolife.payment.ConstantsKt.EXTRA_CHALLANGE_ID;
import static com.ayubo.life.ayubolife.payment.ConstantsKt.EXTRA_PAYMENT_META;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewHomeDesign.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewHomeDesign#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewHomeDesign extends Fragment implements PaginationAdapterCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    String todayStepsToSendToServer;
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
//    private GoogleApiClient mApiClient;

    boolean isTmelineDataAvailable = true;
    public static PaginationAdapter adapter;
    public static ArrayList<Post> timelinePostlist;

    LinearLayoutManager linearLayoutManager;
    private static final String TAG = "MainActivity";
    RecyclerView rv;
    private ProgressDialog progressDialog;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private ApiClient movieService;
    Context contextt;
    int offset = 10;


    Integer totalPages;
    Integer pageNumber;
    String maxPostId;

    float intWidth;
    float hi;
    int intHeight;

    String timelineData;
    String serverResponse;
    ListView mainListview;
    View header;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    String goalName = null;
    String goalImage = null;
    boolean isNeedShowProgress = false;
    boolean isNeedCallService = false;

    String appToken;
    String service_checkpoints, enabled_checkpoints;

    // Class variable declaretions =====================
    ArrayList<NewHomeMenuOneObj> dataList = null;
    ArrayList<NewHomeMenuOneObj> serviceList = null;

    List<TopTile> serviceTopTileList = null;
    List<QuickLink> serviceBottomTileList = null;

    LinearLayout bottom_menu_view_lifeplus;
    LinearLayout bottom_menu_view;
    LayoutInflater inflater;

    String sDen;
    PrefManager pref;
    String marketplace_Token_status, marketplace_Token, userid_ExistingUser, hasToken;
    ImageView btn_me_img, img_btn_notifications;
    ImageButton menu_workout_img, menu_diet_img, menu_relax_img, menu_health_img, btn_programs_img;
    TextView btn_health_tv, btn_diet_tv, btn_relax_tv, btn_workout_tv, btn_programs_tv, btn_me_tv;

    LinearLayout menu_workout_layout, menu_diet_layout, menu_relax_layout, menu_health_layout, btn_me, btn_programs;


    Boolean isExpandedTodayTiles = false;
    TextView textt;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    boolean isGoogleFitConnected = false;
    String challenge_id, serviceDataStatus, badgesJsonData;
    String cityJsonString, noof_day;
    ArrayList<RoadLocationObj> myTagList = null;
    ArrayList<RoadLocationObj> myTreasure = null;
    int total_steps;
    String tip_icon, tip, tipheading;
    String cards, weekSteps, showpopup, white_lines;
    boolean isClicked = true;
    String tip_header_1, tip_header_2, tip_type, tip_meta;

    ActionButtonEventsManager actionButtonEventsManager;

//    public static List<StepObj> jsonStepsForDailyArrayForNewSave;
//    public static List<StepObj> jsonStepsForWeekArrayForNewSave;

    // The scheme to display the Health authorization screen.
    private static final String HEALTH_APP_SETTING_DATA_SHARE_HEALTHKIT_ACTIVITY_SCHEME = "huaweischeme://healthapp/achievement?module=kit";
    // Request code for displaying the Health authorization screen using the startActivityForResult method. The value can be customized.
    private static final int REQUEST_HEALTH_AUTH = 1003;
    private SettingController mSettingController;


    // Request code for displaying the authorization screen using the startActivityForResult method. The value can be customized.
    private static final int REQUEST_SIGN_IN_LOGIN = 1002;

    private static final int REQUEST_LOGIN_CODE = 1102;

    private static String HEALTH_SCHEME = "huaweischeme://healthapp/achievement?module=kit";

    public static JSONArray jsonHuaweiStepsForWeekArray;

    private static String userid_ExistingUser_static, versionName, device_modal;

    private Boolean isLinkedHuawei = false;

    public NewHomeDesign() {
        // Required empty public constructor
    }

    public static NewHomeDesign newInstance(String param1, String param2) {
        NewHomeDesign fragment = new NewHomeDesign();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
            //  AskActivity.startActivity(getContext()); WorkoutActivity
            //  Intent in = new Intent(getContext(), AddReportForReviewActivity.class);
            //  Intent in = new Intent(getContext(), GetAReviewActivity.class);
            // startActivity
            // startActivity
            // startActivity
            Intent in = new Intent(getContext(), WorkoutActivity.class);
            //   Intent in = new Intent(getContext(), TwillioCallInit.class);

            // Intent in = new Intent(getContext(), TwilioHomeActivity.class);
            //    Intent in = new Intent(getContext(), ProgramSettingsActivity.class);
            // Intent in = new Intent(getContext(), AddReportReviewActivity.class);
            //  in.putExtra("challange_id", "f7178c00-db85-11e9-bec6-000d3aa00fd6");
            startActivity(in);

        }
    }

    void showDietScreen() {
        //  FirebaseAnalytics Adding
        Bundle bEat = new Bundle();
        bEat.putString("name", "Eat");
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Tab_opened", bEat);
        }
        if (isClicked) {
            isClicked = false;
            //  Intent intent = new Intent(getContext(), TwilioHomeActivity.class);
            Intent intent = new Intent(getContext(), EatViewActivity.class);
            intent.putExtra("URL", ApiClient.BASE_URL_entypoint + "mobile_food_view");
            startActivity(intent);
        }
    }

    void showRelaxScreen() {
        //  FirebaseAnalytics Adding
        Bundle bRelax = new Bundle();
        bRelax.putString("name", "Relax");
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Tab_opened", bRelax);
        }
        if (isClicked) {
            isClicked = false;
            Intent intent = new Intent(getContext(), RelaxViewActivity.class);
            intent.putExtra("URL", ApiClient.BASE_URL_entypoint + "mobile_mind_view");
            startActivity(intent);
        }
    }

    void showHealthScreen() {
        //  FirebaseAnalytics Adding
        Bundle bHealth = new Bundle();
        bHealth.putString("name", "Health");
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Tab_opened", bHealth);
        }
        if (isClicked) {
            isClicked = false;
            Intent intent = new Intent(getContext(), HealthViewActivity.class);
            startActivity(intent);
        }

    }

//    public void getPopupData() {
//        // pref=new PrefManager(getContext());
//        appToken = pref.getUserToken();
//        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
//        Call<PopupMainResponse> call = apiService.getPopupData(appToken);
//        call.enqueue(new Callback<PopupMainResponse>() {
//            @Override
//            public void onResponse(Call<PopupMainResponse> call, Response<PopupMainResponse> response) {
//
//                boolean status = response.isSuccessful();
//                if (status) {
//                    if (response.body() != null) {
//
//                        PopupMainData dataObj = response.body().getData();
//                        Integer reslt = response.body().getResult();
//
//                        if (dataObj != null) {
//                            showAlert_Add(getContext(), dataObj);
//                        }
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PopupMainResponse> call, Throwable t) {
//
//                System.out.println("========t======" + t);
//            }
//        });
//    }

//    public void setReadPopup(String id) {
//
//        appToken = pref.getUserToken();
//        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
//        Call<Object> call = apiService.setReadPopup(appToken, id);
//        call.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//
//                boolean status = response.isSuccessful();
//                if (status) {
//                    if (response.body() != null) {
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                System.out.println("========t======" + t);
//            }
//        });
//    }

//    public void showAlert_Add(Context c, PopupMainData dataObj) {
//        final android.app.AlertDialog dialogView;
//
//        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
//        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layoutView = inflater.inflate(R.layout.alert_question_views, null, false);
//
//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        int width = display.getWidth();
//        double ratio = ((float) (width)) / 300.0;
//        int height = (int) (ratio * 50);
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, 300);
//        layoutView.setLayoutParams(layoutParams);
//
//        builder.setView(layoutView);
//        dialogView = builder.create();
//        dialogView.setCancelable(false);
//
//        PopupMainData obj = dataObj;
//        String imageURL = obj.getImageUrl();
//        String headerText = obj.getHeader();
//        String headerBody = obj.getBody();
//        String buttonAction = obj.getButton().getAction();
//        String buttonMeta = obj.getButton().getMeta();
//        String buttonText = obj.getButton().getText();
//
//        RequestOptions requestOptionsSamll = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
//        ImageView img_bg_image_bg = layoutView.findViewById(R.id.img_bg_image_bg);
//
//        Glide.with(getContext()).load(imageURL)
//                .apply(requestOptionsSamll)
//                .into(img_bg_image_bg);
//
//        Button btn_view_report = layoutView.findViewById(R.id.btn_view_report_action);
//        btn_view_report.setText(buttonText);
//        LinearLayout lay_close_pop = layoutView.findViewById(R.id.lay_close_pop);
//        TextView txt_heading = layoutView.findViewById(R.id.txt_heading);
//        TextView txt_sub_heading = layoutView.findViewById(R.id.txt_sub_heading);
//        txt_heading.setText(headerText);
//        txt_sub_heading.setText(headerBody);
//
//        ImageButton btn_close_pop = layoutView.findViewById(R.id.btn_close_pop);
//
//        btn_close_pop.setTag(obj);
//        btn_close_pop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogView.cancel();
//                PopupMainData obj = (PopupMainData) v.getTag();
//                setReadPopup(obj.getId().toString());
//            }
//        });
//
//
//        lay_close_pop.setTag(obj);
//        lay_close_pop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogView.cancel();
//                PopupMainData obj = (PopupMainData) v.getTag();
//                setReadPopup(obj.getId().toString());
//            }
//        });
//
//        btn_view_report.setTag(obj);
//        btn_view_report.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialogView.cancel();
//                PopupMainData obj = (PopupMainData) v.getTag();
//                setReadPopup(obj.getId().toString());
//                processAction(obj.getButton().getAction(), obj.getButton().getMeta());
//
//            }
//        });
//
//        dialogView.show();
//    }

    void processAction(String action, String meta) {

        if (action.equals("openbadgenative")) {
            onOpenBadgersClick(meta);
        }
        if (action.equals("programtimeline")) {
            onProgramNewDesignClick(meta);
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
        if (action.equals("leaderboard")) {
            onLeaderboardClick(meta);
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
        if (action.equals("paynow")) {
            onPayNowClick(meta);
        }
        if (action.equals("discover")) {

//            Intent intent = new Intent(getContext(), LifePlusProgramActivity.class);
            Intent intent = new SetDiscoverPage().getDiscoverIntent(getContext());
            intent.putExtra("isFromSearchResults", false);
            startActivity(intent);
        }


    }

    void onOpenBadgersClick(String meta) {
        if (meta != null) {
            Intent intent = new Intent(getContext(), Badges_Activity.class);
            startActivity(intent);
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
            shareLink(getContext(), conversation.getMedia_url());
        } else {
            if (!conversation.getMedia_type().equals("link")) {
                //  downloadFile(conversation.media_url, true)
            } else {
                shareLink(getContext(), conversation.getMedia_url());
            }
        }
    }

    void startDoctorsActivity(String doctorID) {
        DocSearchParameters parameters;
        parameters = new DocSearchParameters();
        PrefManager pref = new PrefManager(getContext());
        parameters.setUser_id(pref.getLoginUser().get("uid"));
        parameters.setDate("");
        parameters.setDoctorId(doctorID);
        parameters.setLocationId("");
        parameters.setSpecializationId("");

        Intent intent = new Intent(getContext(), SearchActivity.class);
        intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new SelectDoctorAction(parameters));
        intent.putExtra(SearchActivity.EXTRA_TO_DATE, "");
        startActivity(intent);
    }

    void onButtonChannelingClick(String meta) {
        if (meta != null) {
            startDoctorsActivity(meta);
        } else {
            Intent intent = new Intent(getContext(), DashboardActivity.class);
            startActivity(intent);
        }
    }

    void onVideoCallClick(String meta) {
        if (meta != null) {
            String activity = "my_doctor";
            Intent intent = new Intent(getContext(), MyDoctorLocations_Activity.class);
            intent.putExtra("doctor_id", meta);
            intent.putExtra("activityName", activity);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), MyDoctor_Activity.class);
            intent.putExtra("activityName", "myExperts");
            startActivity(intent);
        }


    }

    void onMapChallangeClick(String meta) {

        Intent in = new Intent(getContext(), MapChallangeKActivity.class);
        in.putExtra(EXTRA_CHALLANGE_ID, meta);
        startActivity(in);


        //Old code
//            Intent intent = new Intent(getContext(), MapChallengeActivity.class);
//            intent.putExtra("challenge_id", meta);
//            startActivity(intent);
    }

    void onAskClick(String meta) {
        if (meta == null) {
            Intent intent = new Intent(getContext(), AskActivity.class);
            startActivity(intent);
        } else if (meta.equals("")) {
            Intent intent = new Intent(getContext(), AskActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), AyuboChatActivity.class);
            intent.putExtra("doctorId", meta);
            intent.putExtra("isAppointmentHistory", false);
            startActivity(intent);
        }
    }

    void onProgramNewDesignClick(String meta) {
        if (meta != null) {
            Intent intent = new Intent(getContext(), ProgramActivity.class);
            intent.putExtra("meta", meta);
            startActivity(intent);
        }
    }

    void onProgramPostClick(String meta) {
        if (meta != null) {
            Intent intent = new Intent(getContext(), SingleTimeline_Activity.class);
            intent.putExtra("related_by_id", meta);
            intent.putExtra("type", "program");
            startActivity(intent);
        }
    }

    void onGoalClick(String meta) {
        PrefManager prefManager = new PrefManager(getContext());
        String status = prefManager.getMyGoalData().get("my_goal_status");

        if (status.equals("Pending")) {
            Intent intent = new Intent(getContext(), AchivedGoal_Activity.class);
            startActivity(intent);
        }
        if (status.equals("Pick")) {


            Intent intent = new Intent(getContext(), PickAGoal_Activity.class);
            startActivity(intent);
        }
        if (status.equals("Completed")) {
            HomePage_Utility serviceObj = new HomePage_Utility(getContext());
            serviceObj.showAlert_Deleted(getContext(), "This goal has been achieved for the day. Please pick another goal tomorrow");
        }

    }

    void onPayNowClick(String meta) {
        Intent intent = new Intent(getContext(), PaymentActivity.class);
        intent.putExtra("paymentmeta", meta);
        startActivity(intent);
    }

    void onJSONNativePostClick(String meta) {
        Intent intent = new Intent(getContext(), NativePostJSONActivity.class);
        intent.putExtra("meta", meta);
        startActivity(intent);
    }

    void onNativePostClick(String meta) {
        Intent intent = new Intent(getContext(), NativePostActivity.class);
        intent.putExtra("meta", meta);
        startActivity(intent);
    }

    void onPostClick(String meta) {
        Intent intent = new Intent(getContext(), OpenPostActivity.class);
        intent.putExtra("postID", meta);
        startActivity(intent);
    }

    void onJanashakthiWelcomeClick(String meta) {
        PrefManager pref = new PrefManager(getContext());
        pref.setRelateID(meta);
        pref.setIsJanashakthiWelcomee(true);
        Intent intent = new Intent(getContext(), JanashakthiWelcomeActivity.class);
        startActivity(intent);
    }

    void onDyanamicQuestionClick(String meta) {
        PrefManager pref = new PrefManager(getContext());
        pref.setIsJanashakthiDyanamic(true);
        pref.setRelateID(meta);
        Intent intent = new Intent(getContext(), IntroActivity.class);
        startActivity(intent);
    }

    void onJanashakthiReportsClick(String meta) {
        MedicalUpdateActivity.startActivity(getContext());
    }

    void onReportsClick(String meta) {
        Intent intent = new Intent(getContext(), ReportDetailsActivity.class);
        intent.putExtra("data", "all");
        Ram.setReportsType("fromHome");
        startActivity(intent);
    }

    void onLifePointsClick(String activityName, String meta) {
        Intent intent = new Intent(getContext(), LifePointActivity.class);
        startActivity(intent);
    }

    void onHelpClick(String meta) {
        Intent intent = new Intent(getContext(), HelpFeedbackActivity.class);
        intent.putExtra("activityName", "myExperts");
        startActivity(intent);
    }

    void onLeaderboardClick(String meta) {
        if (meta.length() > 0) {
            Intent intent = new Intent(getContext(), NewLeaderBoardActivity.class);
            startActivity(intent);
        }
    }

    void onBowserClick(String meta) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(meta));
        startActivity(browserIntent);
    }

    void onCommonViewClick(String meta) {
        Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
        intent.putExtra("URL", meta);
        startActivity(intent);
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
                            String notificationCount = Integer.toString(count);
                            // txt_noti_count.setText(notificationCount);
                            // txt_noti_count.setVisibility(View.VISIBLE);
                            final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
                            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.5, 20);
                            myAnim.setInterpolator(interpolator);


//                            if (!isFirsttime) {
//                                isFirsttime = true;
                            //    MediaPlayer mp = MediaPlayer.create(getContext(), Settings.System.DEFAULT_NOTIFICATION_URI);
                            //  if (mp != null) {
                            //        mp.start();
                            // }


                            // img_btn_notifications.startAnimation(myAnim);
                            // txt_noti_count.setVisibility(View.VISIBLE);

                        } else {
                            //   txt_noti_count.setText("0");
                            //  txt_noti_count.setVisibility(View.INVISIBLE);
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

    void setupBottomMenuLifePlus(View view) {

        btn_programs = view.findViewById(R.id.btn_programs);
        btn_programs_img = view.findViewById(R.id.btn_programs_img);
        btn_programs_tv = view.findViewById(R.id.btn_programs_tv);

        btn_me = (LinearLayout) view.findViewById(R.id.btn_me);
        //btn_me_img = view.findViewById(R.id.btn_notifications);

        img_btn_notifications = view.findViewById(R.id.img_btn_notifications);
//        txt_noti_count = view.findViewById(R.id.txt_noti_count);

        btn_me_tv = view.findViewById(R.id.btn_me_tv);

        if (Constants.type == Constants.Type.AYUBO) {
            btn_me_tv.setText("Home");
        }
        if (Constants.type == Constants.Type.LIFEPLUS) {
//            btn_me_tv.setText("Messages");
            btn_me_tv.setText("Home");
        }
        if (Constants.type == Constants.Type.HEMAS) {
            btn_me_tv.setText("Home");
        }


        btn_programs_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProgramView();
            }
        });
        btn_programs_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProgramView();
            }
        });

        btn_programs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProgramView();
            }
        });
        btn_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMeView();
            }
        });
//        btn_me_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadMeView();
//            }
//        });

        btn_me_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMeView();
            }
        });

    }

    void loadMeView() {

        // ProfileActivity.startActivity(getContext());
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3333333");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "JustPay Fail");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "JustPay error is this is the one");
            SplashScreen.firebaseAnalytics.logEvent("JUSTPAY", bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Constants.type == Constants.Type.LIFEPLUS) {
//            Intent intent = new Intent(getContext(), NotificationsListActivity.class);
//            startActivity(intent);
            Intent intent = new Intent(getContext(), ProfileNew.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), ProfileNew.class);
            startActivity(intent);
//            ProfileActivity.startActivity(getContext());
        }

    }

    void loadProgramView() {
//        Intent in = new Intent(getContext(), LifePlusProgramActivity.class);
        Intent in = new SetDiscoverPage().getDiscoverIntent(getContext());
        in.putExtra("isFromSearchResults", false);
        startActivity(in);
    }

    void setupBottomMenu(View view) {

        menu_workout_layout = view.findViewById(R.id.btn_workout_layout);
        menu_diet_layout = view.findViewById(R.id.btn_diet_layout);
        menu_relax_layout = view.findViewById(R.id.btn_relax_layout);
        menu_health_layout = view.findViewById(R.id.btn_health_layout);

        menu_workout_img = view.findViewById(R.id.btn_workout_img);
        menu_diet_img = view.findViewById(R.id.btn_diet_img);
        menu_relax_img = view.findViewById(R.id.btn_relax_img);
        menu_health_img = view.findViewById(R.id.btn_health_img);

        btn_workout_tv = view.findViewById(R.id.btn_workout_tv);
        btn_diet_tv = view.findViewById(R.id.btn_diet_tv);
        btn_relax_tv = view.findViewById(R.id.btn_relax_tv);
        btn_health_tv = view.findViewById(R.id.btn_health_tv);

        menu_workout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWorkoutScreen();
            }
        });
        menu_workout_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWorkoutScreen();
            }
        });
        btn_workout_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWorkoutScreen();
            }
        });

        menu_diet_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDietScreen();
            }
        });
        menu_diet_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDietScreen();
            }
        });
        btn_diet_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDietScreen();
            }
        });

        menu_relax_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRelaxScreen();
            }
        });
        menu_relax_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRelaxScreen();
            }
        });
        btn_relax_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRelaxScreen();
            }
        });


        menu_health_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHealthScreen();
            }
        });
        menu_health_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHealthScreen();
            }
        });
        btn_health_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHealthScreen();
            }
        });

    }


    ScrollView layout_srcollview;
    boolean isFirst = false;
    int up = 0;
    int dwn = 0;
    int upStart = 0;
    int upEnd = 0;
    int dwnStart = 0;
    int dwnupEnd = 0;


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
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_home_design, container, false);

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        pref = new PrefManager(getContext());
        jsonHuaweiStepsForWeekArray = new JSONArray();
        userid_ExistingUser = pref.getLoginUser().get("uid");
        pref.setIsRunFirstTime("false");
        versionName = BuildConfig.VERSION_NAME;
        userid_ExistingUser_static = pref.getLoginUser().get("uid");
        device_modal = Build.MODEL;

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");


        String name = pref.getLoginUser().get("name");
        hasToken = pref.getLoginUser().get("hashkey");
        actionButtonEventsManager = new ActionButtonEventsManager(getContext());

        pref.setGoalCategory("");

        sDen = Utility.getDeviceDensityName();
        dataList = new ArrayList<NewHomeMenuOneObj>();
        serviceList = new ArrayList<NewHomeMenuOneObj>();

        inflatert = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        layoutt = inflatert.inflate(R.layout.custom_toast, (ViewGroup) getActivity().findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(getContext());
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);
//===================================================


        //===============================
        pref.setFamilyMemberId("0");
        //================================

        //======RECYCLE VIEW SETTINGS===============================================
        rv = (RecyclerView) view.findViewById(R.id.main_recycler);
        rv.setHasFixedSize(false);

        //  FirebaseAnalytics Adding
        Bundle bTimeline = new Bundle();
        bTimeline.putString("name", "Timeline");
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Tab_opened", bTimeline);
        }

        serviceTopTileList = new ArrayList<>();
        serviceBottomTileList = new ArrayList<>();

//        jsonStepsForDailyArrayForNewSave = new ArrayList<StepObj>();
//        jsonStepsForWeekArrayForNewSave = new ArrayList<StepObj>();

        myTagList = new ArrayList<RoadLocationObj>();
        myTreasure = new ArrayList<RoadLocationObj>();


        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new PaginationAdapter(contextt, userid_ExistingUser);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;

                if (Ram.getCurrentPage() != null) {
                    currentPage = Ram.getCurrentPage();
                    currentPage += 1;
                    bottom_menu_view.setVisibility(View.GONE);
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

        bottom_menu_view = (LinearLayout) view.findViewById(R.id.bottom_menu_view);
        bottom_menu_view_lifeplus = (LinearLayout) view.findViewById(R.id.bottom_menu_view_lifeplus);

        if (Constants.type == Constants.Type.AYUBO) {
            bottom_menu_view.setVisibility(View.GONE);
            bottom_menu_view_lifeplus.setVisibility(View.VISIBLE);
            setupBottomMenuLifePlus(bottom_menu_view_lifeplus);
        }
        if (Constants.type == Constants.Type.HEMAS) {
            // bottom_menu_view.setVisibility(View.VISIBLE);
            //  bottom_menu_view_lifeplus.setVisibility(View.GONE);
            bottom_menu_view.setVisibility(View.GONE);
            bottom_menu_view_lifeplus.setVisibility(View.VISIBLE);
            setupBottomMenuLifePlus(bottom_menu_view_lifeplus);
            // setupBottomMenu(bottom_menu_view);
        }
        if (Constants.type == Constants.Type.SHESHELLS) {
            bottom_menu_view.setVisibility(View.GONE);
            bottom_menu_view_lifeplus.setVisibility(View.GONE);
            // setupBottomMenu(bottom_menu_view);
        }
        if (Constants.type == Constants.Type.LIFEPLUS) {
            bottom_menu_view.setVisibility(View.GONE);
            bottom_menu_view_lifeplus.setVisibility(View.VISIBLE);
            ImageButton new_feed_active_icon = view.findViewById(R.id.new_feed_active_icon);
            TextView new_feed_active_icon_text = view.findViewById(R.id.new_feed_active_icon_text);
            new_feed_active_icon.setBackground(getResources().getDrawable(R.drawable.new_feed_active_life_plus_icon));
            new_feed_active_icon_text.setTextColor(getResources().getColor(R.color.lifeplus_green));
            setupBottomMenuLifePlus(bottom_menu_view_lifeplus);
        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getContext());
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

        DBString jsonDoctrDataObj = getCashDataByType(getContext(), "timeline_firstpage_data");
        if (jsonDoctrDataObj == null) {
            isNeedShowProgress = true;

        } else {

            if ((jsonDoctrDataObj.getId() != null) && (jsonDoctrDataObj.getId().length() > 10)) {
                loadOfflineDisplay();
            }

        }

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        isClicked = true;

//        HuaweiServices huaweiServices = new HuaweiServices(getActivity());
//
//        if (huaweiServices.isGooglePlayServicesAvailable()) {
//            AppUpdateChecker appUpdateChecker = new AppUpdateChecker(getActivity());  //pass the activity in constructure
//            appUpdateChecker.checkForUpdate(false); //mannual check false here
//        }

        System.out.println("====Path=====================" + NewHomeWithSideMenuActivity.isStepUpdated);
        if (!NewHomeWithSideMenuActivity.isStepUpdated) {
            currentPage = 1;
            if (pref.getUserToken().length() > 25) {
                if (Utility.isInternetAvailable(getContext())) {
                    loadMainService();
                } else {

                    Toast.makeText(getContext(), "No active internet connection", Toast.LENGTH_LONG).show();

                }

            } else {
                isNeedShowProgress = true;
                if (Utility.isInternetAvailable(getContext())) {

                    if (pref.getUserToken().length() > 50) {
                        loadMainService();
                    } else {
                        //is this need
                        getUserTokenFromServer(userid_ExistingUser);
                    }

                } else {
                    Toast.makeText(getContext(), "No active internet connection", Toast.LENGTH_LONG).show();
                }
            }
        } else if ((timelinePostlist != null) && (timelinePostlist.size() > 0)) {
            currentPage = 1;
            System.out.println("====Path n 2=====================");
            if (pref.getIsNewPostAdded().equals("yes")) {

                adapter.clear();
                adapter.addAll(timelinePostlist);
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
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
                    DBString jsonDoctrDataObj = DBRequest.getCashDataByType(getContext(), "timeline_firstpage_data");
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
        } else {

        }

//        getPopupData();


        pref.setIsFromJoinChallenge(false);

//        if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//
//            if (huaweiServices.isGooglePlayServicesAvailable()) {
//                //START In Resume, every time checking that IS GOOGLEFIT CONNECTED
//                if (pref.isGoogleFitEnabled().equals("false")) {
//                    boolean bb = fitInstalled();
//                    if (bb) {
//                        createGoogleClient();
//                    } else {
//                        openGoogleFitInPlayStore();
//                    }
//                }
//                //END In Resume, every time checking that IS GOOGLEFIT CONNECTED
//
//            } else {
//                // Initialize SettingController.
//                if (pref.getIsRunFirstTime().equals("false")) {
//                    huaweiInitService();
//                    connectHuaweiSignIn();
//                }
//
//            }
//
//
//        }


    }


//    public void huaweiInitService() {
//        contextt = getContext();
//        HiHealthOptions fitnessOptions = HiHealthOptions.builder().build();
//        AuthHuaweiId signInHuaweiId = HuaweiIdAuthManager.getExtendedAuthResult(fitnessOptions);
//        mSettingController = HuaweiHiHealth.getSettingController(contextt, signInHuaweiId);
//    }
//
//    public void connectHuaweiSignIn() {
///**
// * Sign-in and authorization method. The authorization screen will display if the current account has not granted authorization.
// */
//        Log.i(TAG, "begin sign in");
//        List<com.huawei.hms.support.api.entity.auth.Scope> scopeList = new ArrayList<>();
//
//        HuaweiScopes huaweiScopes = new HuaweiScopes();
//
//        // Add scopes to apply for. The following only shows an example. You need to add scopes according to your specific needs.
//        scopeList.add(huaweiScopes.getHealthKitBoth()); // View and save step counts in HUAWEI Health Kit.
//        scopeList.add(huaweiScopes.getHealthKitHeartRateBoth()); // View and save height and weight in HUAWEI Health Kit.
//        scopeList.add(huaweiScopes.getHealthKitWeightBoth()); // View and save the heart rate data in HUAWEI Health Kit.
//        scopeList.add(huaweiScopes.getHealthKitStepRealTime());
//
//        // Configure authorization parameters.
//        HuaweiIdAuthParamsHelper authParamsHelper = new HuaweiIdAuthParamsHelper(
//                HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM);
//        HuaweiIdAuthParams authParams = authParamsHelper.setIdToken()
//                .setAccessToken()
//                .setScopeList(scopeList)
//                .createParams();
//
//        // Initialize the HuaweiIdAuthService object.
//        final HuaweiIdAuthService huaweiAuthService = HuaweiIdAuthManager.getService(getContext(), authParams);
//
//        // Silent sign-in. If authorization has been granted by the current account, the authorization screen will not display. This is an asynchronous method.
//        Task<AuthHuaweiId> authHuaweiIdTask = huaweiAuthService.silentSignIn();
//
//        // Add the callback for the call result.
//        authHuaweiIdTask.addOnSuccessListener(new OnSuccessListener<AuthHuaweiId>() {
//            @Override
//            public void onSuccess(AuthHuaweiId huaweiId) {
//
//                // The silent sign-in is successful.
//                Log.i(TAG, "silentSignIn success");
////                loadMainServiceForGoals();
//                //=======Sending and afterthat Getting Latest Steps===========================
////                GetSteps task = new GetSteps();
////                task.execute();
//
//                getActivity().startActivityForResult(huaweiAuthService.getSignInIntent(), REQUEST_LOGIN_CODE);
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception exception) {
//                // The silent sign-in fails. This indicates that the authorization has not been granted by the current account.
//                if (exception instanceof ApiException) {
//                    ApiException apiException = (ApiException) exception;
//                    Log.i(TAG, "sign failed status:" + apiException.getStatusCode());
//                    Log.i(TAG, "begin sign in by intent");
//
//                    // Call the sign-in API using the getSignInIntent() method.
//                    Intent signInIntent = huaweiAuthService.getSignInIntent();
//
//                    // Display the authorization screen by using the startActivityForResult() method of the activity.
//                    // You can change HihealthKitMainActivity to the actual activity.
//                    getActivity().startActivityForResult(signInIntent, REQUEST_SIGN_IN_LOGIN);
//                }
//            }
//        });
//
//    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

//        Toast.makeText(getContext(), "GoogleFit connected", Toast.LENGTH_LONG).show();

//        pref.setGoogleFitEnabled("true");

        loadMainServiceForGoals();
//
//        //=======Sending and afterthat Getting Latest Steps===========================
//        GetSteps task = new GetSteps();
//        task.execute();

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!authInProgress) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult(getActivity(), REQUEST_OAUTH);

            } catch (IntentSender.SendIntentException e) {

                e.printStackTrace();
            }
        } else {
            Log.e("GoogleFit", "authInProgress");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        GoogleSupportServices googleSupportServices = new GoogleSupportServices(getActivity());

//        if (huaweiServices.isGooglePlayServicesAvailable()) {
//            if (requestCode == REQUEST_OAUTH) {
//                authInProgress = false;
//                if (resultCode == RESULT_OK) {
//                    if (!mApiClient.isConnecting() && !mApiClient.isConnected()) {
//                        mApiClient.connect();
//                        Log.e("GoogleFit", "connected-------------");
//                    }
//                } else if (resultCode == RESULT_CANCELED) {
//                    Log.e("GoogleFit", "RESULT_CANCELED");
//                }
//            } else {
//                Log.e("GoogleFit", "requestCode NOT request_oauth");
//            }
//
//        }

//        else {
//
//            pref.setIsRunFirstTime("true");
//            if (requestCode == REQUEST_LOGIN_CODE) {
//                Task<AuthHuaweiId> huaweiIdAuthManager = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
//                if (huaweiIdAuthManager.isSuccessful()) {
//                    HuaweiResult huaweiResult = new HuaweiResult();
//                    huaweiResult.authResults = HuaweiIdAuthAPIManager.HuaweiIdAuthAPIService.parseHuaweiIdFromIntent(data);
//                    if (!isLinkedHuawei) {
//                        checkAndAuthorizeHealthApp();
//                    }
//
//                } else if (huaweiIdAuthManager.isCanceled()) {
//                    Toast.makeText(getContext(), "Authorization was not granted", Toast.LENGTH_SHORT).show();
//                }
//            } else if (requestCode == REQUEST_HEALTH_AUTH) {
//                Log.i(TAG, "REQUEST_WAS_SUCCESS");
//                loadMainServiceForGoals();
//                GetSteps task = new GetSteps();
//                task.execute();
//            } else {
//                System.out.println("else");
//            }
//        }


    }

//    private void checkAndAuthorizeHealthApp() {
//        Log.i(TAG, "Begin to checkOrAuthorizeHealthApp");
//        mSettingController = HuaweiHiHealth.getSettingController(contextt,
//                HuaweiIdAuthManager.getExtendedAuthResult(
//                        HiHealthOptions
//                                .builder()
//                                .build()
//                ));
//        Task<Boolean> mSettingControllerQueryTask = mSettingController.getHealthAppAuthorization();
//        mSettingControllerQueryTask.addOnSuccessListener(new OnSuccessListener<Boolean>() {
//            @Override
//            public void onSuccess(Boolean result) {
//                if (Boolean.TRUE.equals(result)) {
//                    Log.i(TAG, "queryHealthAuthorization get result is authorized");
//                    Uri uri = Uri.parse(HEALTH_SCHEME);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    if (intent.resolveActivity(Objects.requireNonNull(Objects.requireNonNull(getContext())).getPackageManager()) != null) {
//                        getActivity().startActivityForResult(intent, REQUEST_HEALTH_AUTH);
//
//                    }
//                } else {
//                    Log.i(TAG, "queryHealthAuthorization get result is unauthorized");
//                    Toast.makeText(getContext(), "Please link the app with Huawei Health", Toast.LENGTH_LONG).show();
//                    Uri uri = Uri.parse(HEALTH_SCHEME);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    if (intent.resolveActivity(Objects.requireNonNull(Objects.requireNonNull(getContext())).getPackageManager()) != null) {
//                        getActivity().startActivityForResult(intent, REQUEST_HEALTH_AUTH);
//
//                    }
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception exception) {
//                if (exception != null) {
//                    Log.i(TAG, "queryHealthAuthorization has exception");
//                    Toast.makeText(getContext(), "Health Authorization has exception", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//
//    }


//    private void handleHealthAuthResult(int requestCode) {
//        if (requestCode != REQUEST_HEALTH_AUTH) {
//            return;
//        }
//        queryHealthAuthorization();
//    }


    /**
     * Check whether the authorization is successful.
     */
//    private void queryHealthAuthorization() {
//        Log.d(TAG, "begint to queryHealthAuthorization");
//
//        Task<Boolean> queryTask = mSettingController.getHealthAppAuthorization();
//        queryTask.addOnSuccessListener(new OnSuccessListener<Boolean>() {
//            @Override
//            public void onSuccess(Boolean result) {
//                if (Boolean.TRUE.equals(result)) {
//                    Log.i(TAG, "queryHealthAuthorization get result is authorized");
//                } else {
//                    Log.i(TAG, "queryHealthAuthorization get result is unauthorized");
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception exception) {
//                if (exception != null) {
//                    Log.i(TAG, "queryHealthAuthorization has exception");
//                }
//            }
//        });
//    }

    /**
     * Method of handling authorization result responses
     *
     * @param requestCode Request code for displaying the authorization screen.
     * @param data        Authorization result response.
     */
//    private void handleSignInResult(int requestCode, Intent data) {
//        // Handle only the authorized responses
//        if (requestCode != REQUEST_SIGN_IN_LOGIN) {
//            return;
//        }
//
//        // Obtain the authorization response from the intent.
//        HuaweiIdAuthResult result = HuaweiIdAuthAPIManager.HuaweiIdAuthAPIService.parseHuaweiIdFromIntent(data);
//        Log.d(TAG, "handleSignInResult status = " + result.getStatus() + ", result = " + result.isSuccess());
//        if (result.isSuccess()) {
//            Log.d(TAG, "sign in is success");
//
//            // Obtain the authorization result.
//            HuaweiIdAuthResult authResult = HuaweiIdAuthAPIManager.HuaweiIdAuthAPIService.parseHuaweiIdFromIntent(data);
//            // Check whether the HUAWEI Health app has been authorized to open data to Health Kit.
//            checkOrAuthorizeHealth();
//        } else {
//            Log.d(TAG, "sign in is failed");
//        }
//    }

    /**
     * Query the Health authorization and display the authorization screen when necessary.
     */
//    private void checkOrAuthorizeHealth() {
//        Log.d(TAG, "begint to checkOrAuthorizeHealth");
//
//        // Check the Health authorization status. If the authorization has not been granted, display the authorization screen in the HUAWEI Health app.
//        Task<Boolean> authTask = mSettingController.getHealthAppAuthorization();
//        authTask.addOnSuccessListener(new OnSuccessListener<Boolean>() {
//            @Override
//            public void onSuccess(Boolean result) {
//                if (Boolean.TRUE.equals(result)) {
//                    Log.i(TAG, "checkOrAuthorizeHealth get result success");
//                } else {
//                    // If the authorization has not been granted, display the authorization screen in the HUAWEI Health app.
//                    Uri healthKitSchemaUri = Uri.parse(HEALTH_APP_SETTING_DATA_SHARE_HEALTHKIT_ACTIVITY_SCHEME);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, healthKitSchemaUri);
//                    // Check whether the authorization screen of the Health app can be displayed.
//                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
//                        // Display the authorization screen by using the startActivityForResult() method of the activity.
//                        // You can change HihealthKitMainActivity to the actual activity.
//                        getActivity().startActivityForResult(intent, REQUEST_HEALTH_AUTH);
//                    } else {
//                        Log.w(TAG, "can not resolve HUAWEI Health Auth Activity");
//                    }
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception exception) {
//                if (exception != null) {
//                    Log.i(TAG, "checkOrAuthorizeHealth has exception");
//                }
//            }
//        });
//    }


    void loadOfflineDisplay() {
        if ((serviceTopTileList != null) && (serviceTopTileList.size() > 0)) {
            serviceTopTileList.clear();
        }
        if ((serviceBottomTileList != null) && (serviceBottomTileList.size() > 0)) {
            serviceBottomTileList.clear();
        }

        DBString jsonStrinTop_Obj = DBRequest.getCashDataByType(getContext(), "timeline_top_data");
        DBString jsonStringBottom_Obj = DBRequest.getCashDataByType(getContext(), "timeline_bottom_data");
        //    DBString jsonStringMainResData111_Obj= DBRequest.getCashDataByType(getContext(),"goal_data");
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

        DBString jsonStringData_Obj = DBRequest.getCashDataByType(getContext(), "timeline_firstpage_data");

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

    /**
     * @param throwable required for {@link #fetchErrorMessage(Throwable)}
     * @return
     */


    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
//    private String fetchErrorMessage(Throwable throwable) {
//        String errorMsg = getResources().getString(R.string.error_msg_unknown);
//
//        if (!isNetworkConnected()) {
//            errorMsg = getResources().getString(R.string.no_internet_connection);
//        } else if (throwable instanceof TimeoutException) {
//            errorMsg = getResources().getString(R.string.error_msg_timeout);
//        }
//
//        return errorMsg;
//    }

    // Helpers -------------------------------------------------------------------------------------


    /**
     * Remember to add android.permission.ACCESS_NETWORK_STATE permission.
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    //CODING FOR PAGINATION END======

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

                Intent intent = new Intent(getContext(), ShareGoals_Activity.class);
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

//    ArrayList<NewHomeMenuOneObj> getServicesMenuData(String service) {
//        ArrayList<NewHomeMenuOneObj> serviceDat = new ArrayList<NewHomeMenuOneObj>();
//        JSONArray myServiceLists = null;
//        try {
//            myServiceLists = new JSONArray(service);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i < myServiceLists.length(); i++) {
//
//            JSONObject jsonMainNode3 = null;
//            try {
//                jsonMainNode3 = (JSONObject) myServiceLists.get(i);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            String title = jsonMainNode3.optString("title");
//            String image = jsonMainNode3.optString("image");
//            String link = jsonMainNode3.optString("link");
//            serviceDat.add(new NewHomeMenuOneObj(title, image, "", "", link));
//        }
//        return serviceDat;
//    }

//    ArrayList<NewHomeMenuOneObj> getMainMenuData(String main) {
//
//        ArrayList<NewHomeMenuOneObj> data = new ArrayList<NewHomeMenuOneObj>();
//
//        String gStatus = pref.getMyGoalData().get("my_goal_status");
//        String defaultGoalImage = pref.getMyGoalData().get("my_goal_image");
//
//        if (defaultGoalImage.equals("")) {
//            defaultGoalImage = "http://apps.ayubo.life/ayubo-app-ui/xhdpi/pic-a-goal.png";
//        }
//        if (gStatus.equals("Pick")) {
//            if (defaultGoalImage.contains("zoom")) {
//                defaultGoalImage = defaultGoalImage.replace("zoom", "xhdpi");
//            }
//            data.add(new NewHomeMenuOneObj("Pick a goal", defaultGoalImage, "yes", gStatus, "activity"));
//        } else {
//            String gId = pref.getMyGoalData().get("my_goal_id");
//            String gName = pref.getMyGoalData().get("my_goal_name");
//            String gImage = pref.getMyGoalData().get("my_goal_image");
//            data.add(new NewHomeMenuOneObj(gName, gImage, "yes", gStatus, "activity"));
//        }
//
//
//        JSONArray myListsAll = null;
//        try {
//            myListsAll = new JSONArray(main);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i < myListsAll.length(); i++) {
//
//            JSONObject jsonMainNode3 = null;
//            try {
//                jsonMainNode3 = (JSONObject) myListsAll.get(i);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            String title = jsonMainNode3.optString("title");
//            String image = jsonMainNode3.optString("image");
//            String button = jsonMainNode3.optString("button");
//            String button_text = jsonMainNode3.optString("button_text");
//            String link = jsonMainNode3.optString("link");
//            data.add(new NewHomeMenuOneObj(title, image, button, button_text, link));
//        }
//
//        return data;
//    }

//    private void check_buildFitnessClientAndSendSteps() {
//        // Create the Google API Client
//
//        if (mApiClient != null) {
//            if (mApiClient.isConnected()) {
//                // Here Means GoogleFit connected ....
//                //Then send today GFit steps to server
//                GetSteps task = new GetSteps();
//                task.execute();
//
//            }
//
//        } else {
//            //Still not GFit connected...So we do nothing..................
//            Toast.makeText(getContext(), "Still not GFit connected...So we do nothing..................", Toast.LENGTH_SHORT);
//        }
//    }
//
//    private class GetSteps extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... arg0) {
//
//            String ste = "0";
//            jsonStepsForDailyArrayForNewSave = new ArrayList<StepObj>();
//            jsonStepsForWeekArrayForNewSave = new ArrayList<StepObj>();
//            HuaweiServices huaweiServices = new HuaweiServices(getActivity());
//            if (huaweiServices.isGooglePlayServicesAvailable()) {
//                DataReadRequest readRequest = queryFitnessData(0);
//                DataReadResult dataReadResult = Fitness.HistoryApi.readData(mApiClient, readRequest).await(1, TimeUnit.MINUTES);
//                ste = printData(dataReadResult);
//                sendLatestStepsToServer(ste);
//            } else {
//                HiHealthOptions hiHealthOptions = HiHealthOptions.builder()
//                        .addDataType(com.huawei.hms.hihealth.data.DataType.DT_INSTANTANEOUS_HEIGHT, HiHealthOptions.ACCESS_READ)
//                        .addDataType(com.huawei.hms.hihealth.data.DataType.DT_INSTANTANEOUS_HEIGHT, HiHealthOptions.ACCESS_WRITE)
//                        .addDataType(com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA, HiHealthOptions.ACCESS_READ)
//                        .addDataType(com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA, HiHealthOptions.ACCESS_WRITE)
//                        .build();
//                AuthHuaweiId signInHuaweiId = HuaweiIdAuthManager.getExtendedAuthResult(hiHealthOptions);
//
//                DataController dataController = HuaweiHiHealth.getDataController(getActivity(), signInHuaweiId);
//
//
//                Task<SampleSet> todaySummationTask = dataController.readTodaySummation(com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA);
//                todaySummationTask.addOnSuccessListener(new OnSuccessListener<SampleSet>() {
//                    @Override
//                    public void onSuccess(SampleSet sampleSet) {
//                        logger("Success read today summation from HMS core");
//                        isLinkedHuawei = true;
//                        if (sampleSet != null) {
//                            String steps = showSampleSet(sampleSet);
//                            sendLatestStepsToServer(steps);
//                        }
//                    }
//                });
//                todaySummationTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(Exception e) {
//                        isLinkedHuawei = true;
//                        String errorCode = e.getMessage();
//                        String errorMsg = HiHealthStatusCodes.getStatusCodeMessage(Integer.parseInt(errorCode));
//                        logger(errorCode + ": " + errorMsg);
//                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//                getHueaweiWeeklyStepsAndSave task = new getHueaweiWeeklyStepsAndSave();
//                task.execute(dataController);
//
//            }
//            return null;
//        }
//
//
////        @Override
////        protected void onPostExecute(String nowSteps) {
////
////            if (pref.getUserData().get("steps") != null) {
////                String lastStep = pref.getUserData().get("steps");
////
////                // if(Integer.parseInt(nowSteps)>Integer.parseInt(lastStep)){
////                sendLatestStepsToServer(nowSteps);
////                // }
////            }
////
////
////        }
//
//    }

//    private class getHueaweiWeeklyStepsAndSave extends AsyncTask<DataController, Void, String> {
//
//        String step = "0";
//
//        @Override
//        protected String doInBackground(DataController... dataController) {
//            getHuaweiStepsForWeek(dataController[0]);
//            return step;
//        }
//
//    }

//
//    private void send10DaysHuaweiSteps() {
//        //
//        newServiceFor10DaysHuawei task = new newServiceFor10DaysHuawei();
//        task.execute();
//    }

//    private class newServiceFor10DaysHuawei extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            newServiceSendFor10DaysHuawei();
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

//    private void newServiceSendFor10DaysHuawei() {
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
//        data = jsonHuaweiStepsForWeekArray;
//
//        newServiceAPIFor10DaysGFIT task = new newServiceAPIFor10DaysGFIT();
//        task.execute(jsonStepsForWeekArrayForNewSave);
//
//
//        //  String summeryData=data.toString().replace("\"","\\\"");
//        System.out.println("========alldata==============" + data);
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


    /**
     * Print the SamplePoint in the SampleSet object as an output.
     *
     * @param sampleSet Sampling dataset.
     */
//    private String showSampleSet(SampleSet sampleSet) {
//        String stepCount = "0";
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        for (SamplePoint dp : sampleSet.getSamplePoints()) {
//            logger("Sample point type: " + dp.getDataType().getName());
//            logger("Start: " + dateFormat.format(new Date(dp.getStartTime(TimeUnit.MILLISECONDS))));
//            logger("End: " + dateFormat.format(new Date(dp.getEndTime(TimeUnit.MILLISECONDS))));
//            for (com.huawei.hms.hihealth.data.Field field : dp.getDataType().getFields()) {
//                logger("Field: " + field.getName() + " Value: " + dp.getFieldValue(field));
//                stepCount = dp.getFieldValue(field).toString();
//            }
//
//            jsonStepsForDailyArrayForNewSave.add(createGFitStepsDataArrayForNewSave(stepCount, dp.getStartTime(TimeUnit.MILLISECONDS), dp.getEndTime(TimeUnit.MILLISECONDS)));
//
//        }
//
//        return stepCount;
//    }

//    public void getHuaweiStepsForWeek(DataController dataController) {
//        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
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
//        Task<SampleSet> daliySummationTask = dataController.readDailySummation(com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA, Integer.parseInt(dateFormat.format(startTime)), Integer.parseInt(dateFormat.format(endTime)));
//        daliySummationTask.addOnSuccessListener(new OnSuccessListener<SampleSet>() {
//            @Override
//            public void onSuccess(SampleSet sampleSet) {
//                logger("Success read daily summation from HMS core");
//                if (sampleSet != null) {
//                    showWeeklySampleSet(sampleSet);
//                }
//            }
//        });
//        daliySummationTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception e) {
//                logger("readTodaySummation" + e.toString());
//            }
//        });
//
//    }

//    private void showWeeklySampleSet(SampleSet sampleSet) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//
//        for (SamplePoint dp : sampleSet.getSamplePoints()) {
//            logger("Sample point type: " + dp.getDataType().getName());
//            logger("Start: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//            logger("End: " + dateFormat.format(new Date(dp.getEndTime(TimeUnit.MILLISECONDS))));
//            String strSteps = null;
//
//            for (com.huawei.hms.hihealth.data.Field field : dp.getDataType().getFields()) {
//
//                strSteps = dp.getFieldValue(field).toString();
//                logger("Field: " + field.getName() + " Value: " + dp.getFieldValue(field));
//                jsonHuaweiStepsForWeekArray.put(createGFitStepsDataArray(
//                        dp.getFieldValue(field).toString(),
//                        dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)),
//                        "good"));
//
//
//            }
//
//            jsonStepsForWeekArrayForNewSave.add(createGFitStepsDataArrayForNewSave(strSteps, dp.getStartTime(TimeUnit.MILLISECONDS), dp.getEndTime(TimeUnit.MILLISECONDS)));
//
//        }
//
//
//        send10DaysHuaweiSteps();
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


//    private void printDataHuawei(ReadReply readReply) {
//        String steps = "0";
//        for (SampleSet sampleSet : readReply.getSampleSets()) {
//            steps = showSampleSetHuawei(sampleSet);
//        }
//        sendLatestStepsToServer(steps);
//    }


//    /**
//     * Print the SamplePoint in the SampleSet object as an output.
//     *
//     * @param sampleSet Sampling dataset.
//     */
//    private String showSampleSetHuawei(SampleSet sampleSet) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String stepCount = "0";
//        for (SamplePoint samplePoint : sampleSet.getSamplePoints()) {
//            logger("Sample point type: " + samplePoint.getDataType().getName());
//            logger("Start: " + dateFormat.format(new Date(samplePoint.getStartTime(TimeUnit.MILLISECONDS))));
//            logger("End: " + dateFormat.format(new Date(samplePoint.getEndTime(TimeUnit.MILLISECONDS))));
//            for (com.huawei.hms.hihealth.data.Field field : samplePoint.getDataType().getFields()) {
//                logger("Field: " + field.getName() + " Value: " + samplePoint.getFieldValue(field));
//                stepCount = samplePoint.getFieldValue(field).toString();
//            }
//        }
//        return stepCount;
//    }

    /**
     * TextView to send the operation result logs to the logcat and to the UI
     *
     * @param string Log string.
     */
    private void logger(String string) {
        Log.i("DataController", string);
    }


//    void sendLatestStepsToServer(String stps) {
//        todayStepsToSendToServer = stps;
//        sendAyuboStepsToServer_ForTenDays0 task = new sendAyuboStepsToServer_ForTenDays0();
//        task.execute(new String[]{ApiClient.BASE_URL_live});
//    }

//    private class sendAyuboStepsToServer_ForTenDays0 extends AsyncTask<String, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... urls) {
//            sendAyuboSteps_ForTenDays0();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//
//        }
//    }

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
//        SimpleDateFormat database_date_format_sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//        String dateFromDB_forADay0 = database_date_format_sdf.format(cal.getTime());
//
//
//        newServiceAPIFor0DaysGFIT task = new newServiceAPIFor0DaysGFIT();
//        task.execute(jsonStepsForDailyArrayForNewSave);
//
//        float value = Float.parseFloat(todayStepsToSendToServer);
//        String sMets, sCals, sDis;
//        double metsVal = (value / 130) * 4.5;
//        sMets = String.format("%.0f", metsVal);
//        double calories = (metsVal * 3.5 * 70) / 200;
//        sCals = String.format("%.0f", calories);
//        double distance = (value * 78) / (float) 100000;
//        sDis = String.format("%.2f", distance);
//        String versionName = BuildConfig.VERSION_NAME;
//        String device_modal = Build.MODEL;
//
//        String jsonStr =
//                "{" +
//                        "\"userid\": \"" + userid_ExistingUser + "\"," +
//                        "\"activity\": \"" + "activity_AYUBO" + "\"," +
//                        "\"energy\": \"" + sMets + "\"," +
//                        "\"steps\": \"" + todayStepsToSendToServer + "\"," +
//                        "\"calorie\": \"" + sCals + "\", " +
//                        "\"duration\": \"" + "0" + "\"," +
//                        "\"distance\": \"" + sDis + "\"," +
//                        "\"date\": \"" + dateFromDB_forADay0 + "\"," +
//                        "\"walk_count\": \"" + todayStepsToSendToServer + "\"," +
//                        "\"run_count\": \"" + "15" + "\"," +
//                        "\"version\": \"" + versionName + "\"," +
//                        "\"osType\": \"" + "android" + "\"," +
//                        "\"device_modal\": \"" + device_modal
//                        + "\"" +
//                        "}";
//
//        nameValuePair.add(new BasicNameValuePair("method", "addDailyActivitySummary"));
//        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));
//
//
//        //  System.out.println("....Today......addDailyActivitySummary....." + nameValuePair.toString());
//        //Encoding POST data
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
//
//        } catch (UnsupportedEncodingException e) {
//            // log exception
//            e.printStackTrace();
//        }
//
//
//        HttpResponse response = null;
//        try {
//            response = httpClient.execute(httpPost);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String sc = String.valueOf(response.getStatusLine().getStatusCode());
//
//
//        if (sc.equals("200")) {
//
//            String responseString = null;
//            try {
//                responseString = EntityUtils.toString(response.getEntity());
//                System.out.println("=====responseString====addDailyActivitySummary===" + responseString);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//
//    }

//    private class newServiceAPIFor0DaysGFIT extends AsyncTask<List<StepObj>, Void, String> {
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

//    public static ReadOptions queryFitnessDataForHuawei(int days) {
//
//        Calendar secondDay = Calendar.getInstance();
//        secondDay.add(Calendar.DATE, -days);
//
//        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//        String dateString = format1.format(secondDay.getTime());
//
//        long endTime;
//        long midNight;
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//        String startDateString = dateString + " 00:00:01";
//        String endDateString = dateString + " 23:59:59";
//
//        Calendar calendarStart = null;
//        Calendar calendarEnd = null;
//        Date sdate = null;
//        Date edate = null;
//
//        try {
//            //formatting the dateString to convert it into a Date
//            sdate = sdf.parse(startDateString);
//            edate = sdf.parse(endDateString);
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
//        // The DataType.DT_CONTINUOUS_STEPS_DELTA specified here can be used to query the user's step count.
//        ReadOptions readOptions = new ReadOptions.Builder().read(com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_TOTAL)
//                .setTimeRange(midNight, endTime, TimeUnit.MILLISECONDS)
//                .build();
//
//        return readOptions;
//    }

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
//        String stepCount = null;
//        if (dataSet.isEmpty()) {
//
//            stepCount = "0";
//        } else {
//            for (DataPoint dp : dataSet.getDataPoints()) {
//                Log.i(TAG, "Data point:");
//                Log.i(TAG, "\tType: " + dp.getDataType().getName());
//
//                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
//
//                for (Field field : dp.getDataType().getFields()) {
//                    Log.i(TAG, "\tField: " + field.getName() +
//                            "GFit Steps Value: " + dp.getValue(field));
//                    stepCount = "0";
//                    stepCount = dp.getValue(field).toString();
//
//                    System.out.println("==============GFit Steps Value:==================" + stepCount);
//
//
//                }
//
//                jsonStepsForDailyArrayForNewSave.add(createGFitStepsDataArrayForNewSave(stepCount, dp.getStartTime(TimeUnit.MILLISECONDS), dp.getEndTime(TimeUnit.MILLISECONDS)));
//            }
//        }
//
//        return stepCount;
//    }

//    public static StepObj createGFitStepsDataArrayForNewSave(String steps, Long startTime, Long endTime) {
//        StepObj stepObj = new StepObj();
//        stepObj.setStepCount(Integer.parseInt(steps));
//        stepObj.setStartDateTime(startTime);
//        stepObj.setEndDateTime(endTime);
//        return stepObj;
//    }

    private static final String PACKAGE_NAME = "com.google.android.apps.fitness";

    @CheckResult
    public boolean fitInstalled() {
        try {
            getActivity().getPackageManager().getPackageInfo(PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

//    private void createGoogleClient() {
//
//        mApiClient = new GoogleApiClient.Builder(getContext())
//                .addApi(ActivityRecognition.API)
//                .addApi(Fitness.RECORDING_API)
//                .addApi(Fitness.HISTORY_API)
//                .addApi(Fitness.SENSORS_API)
//                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//        mApiClient.connect();
//    }

//    private void openGoogleFitInPlayStore() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layoutView = inflater.inflate(R.layout.alert_install_googlefit, null, false);
//        builder.setView(layoutView);
//        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.fitness")));
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

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

            AppEventsLogger loggerFB = AppEventsLogger.newLogger(getContext());
            loggerFB = AppEventsLogger.newLogger(getContext());

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
                    Intent intent = new Intent(getContext(), AchivedGoal_Activity.class);
                    startActivity(intent);
                } else if (status.equals("Pick")) {

                    Intent intent = new Intent(getContext(), PickAGoal_Activity.class);
                    startActivity(intent);
                } else if (status.equals("Completed")) {
                    showAlert_Deleted(getContext(), "This goal has been achieved for the day. Please pick another goal tomorrow");
                }
            } else if (type.equals("googlefit")) {

//                boolean bb = fitInstalled();
//                if (bb) {
//                    createGoogleClient();
//                } else {
//                    openGoogleFitInPlayStore();
//                }
            } else if (type.equals("wellness_dashboard")) {
                Intent intent = new Intent(getContext(), DashBoard_Activity.class);
                System.out.println(object);
                System.out.println(object.getRelatedId());
                System.out.println(object.getRelatedId());
                intent.putExtra("meta", object.getRelatedId());
                startActivity(intent);
            } else if (type.equals("join_adv_challenge")) {
                Intent intent = new Intent(getContext(), MapJoinChallenge_Activity.class);
                System.out.println("=============challenge_id====================" + object.getMeta());
                intent.putExtra("challenge_id", object.getMeta());
                startActivity(intent);
            } else if (type.equals("view_adv_challenge")) {
                pref.setIsFromPush(false);
                challenge_id = object.getMeta();
                System.out.println("=============challenge_id=====From HOME===============" + object.getMeta());

                onMapChallangeClick(challenge_id);
//                Intent in = new Intent(getContext(), MapChallangeActivity.class);
//                in.putExtra(EXTRA_CHALLANGE_ID, challenge_id);
//                startActivity(in);
                //    Intent intent = new Intent(getContext(), MapChallengeActivity.class);
//                intent.putExtra("challenge_id", challenge_id);
//                startActivity(intent);
            } else if (type.equals("challenge")) {
                onMapChallangeClick(object.getMeta());
            } else if (type.equals("view_spon_adv_challenge")) {
                pref.setIsFromPush(false);
                challenge_id = object.getMeta();
                showpopup = "showpopup";

//                Intent in = new Intent(getContext(), MapChallangeActivity.class);
//                in.putExtra(EXTRA_CHALLANGE_ID, challenge_id);
//                startActivity(in);
                onMapChallangeClick(challenge_id);
//                Intent intent = new Intent(getContext(), MapChallengeActivity.class);
//                intent.putExtra("challenge_id", challenge_id);
//                startActivity(intent);

            } else if (type.equals("post")) {
                Intent intent = new Intent(getContext(), OpenPostActivity.class);
                intent.putExtra("postID", object.getMeta());
                startActivity(intent);
            } else if (type.equals("doctor_chat")) {
                String docId = object.getMeta();
                AyuboChatActivity.Companion.startActivity(getActivity(), docId, false, false, "");
            } else if (type.equals("program_timeline")) {
                Intent in = new Intent(getContext(), ProgramActivity.class);
                in.putExtra("meta", object.getRelatedId());
                startActivity(in);
            } else if (type.equals("dynamicquestion")) {
                pref.setRelateID(object.getRelatedId());
                pref.setIsJanashakthiDyanamic(true);
                Intent in = new Intent(getContext(), IntroActivity.class);
                in.putExtra("meta", object.getRelatedId());
                startActivity(in);
            } else if (type.equals("janashakthi_policy")) {


                pref.setRelateID(object.getRelatedId());


                if (object.getTitleStatus().equals("welcome")) {
                    pref.setIsJanashakthiWelcomee(true);
                    Intent intent = new Intent(getContext(), JanashakthiWelcomeActivity.class);
                    startActivity(intent);
                }
                if (object.getTitleStatus().equals("pending_analysis")) {
                    Intent intent = new Intent(getContext(), PendingAnalysisOkActivity.class);
                    startActivity(intent);
                }
                if (object.getTitleStatus().equals("pending_follow_up")) {
                    Intent intent = new Intent(getContext(), PendingAnalysisOkActivity.class);
                    startActivity(intent);
                }
                if (object.getTitleStatus().equals("active")) {
                    Intent in = new Intent(getContext(), ProgramActivity.class);
                    in.putExtra("meta", object.getRelatedId());
                    startActivity(in);
                }
                if (object.getTitleStatus().equals("user_action")) {
                    Intent intent = new Intent(getContext(), TitleClickedActivity.class);
                    intent.putExtra("meta", object.getMeta());
                    startActivity(intent);
                }
            } else if (type.equals("doc_chat")) {
                String docId = object.getMeta();
                AyuboChatActivity.Companion.startActivity(getActivity(), docId, false, false, "");
            } else if (type.equals("native_post_json")) {
                String meta = object.getMeta();
                onJSONNativePostClick(meta);
            } else if (type.equals("native_post")) {
                String meta = object.getMeta();
                onNativePostClick(meta);
            } else if (type.equals("common")) {
                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", object.getMeta());
                startActivity(intent);
            } else if (type.equals("commonview")) {
                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", object.getMeta());
                startActivity(intent);
            } else if (type.equals("web")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(object.getMeta()));
                startActivity(browserIntent);
            }

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            contextt = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void openAppStore() {
        Service_getMarketPlace_ServiceCall();
    }

    //=============================================

    private void writeToFile(String result) {
        try {
            FileOutputStream stream = getActivity().openFileOutput(challenge_id + ".json", getContext().MODE_PRIVATE);
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

    //=============================================

    private void Service_getMarketPlace_ServiceCall() {
        if (Utility.isInternetAvailable(getContext())) {
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

                GoogleSupportServices googleSupportServices = new GoogleSupportServices(getContext());

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
            DBRequest.updateDoctorData(getContext(), stingData, "timeline_top_data");
        }
        if ((serviceBottomTileList != null) && (serviceBottomTileList.size() > 0)) {
            String stingBottData = castPojoListToString(serviceBottomTileList);
            DBRequest.updateDoctorData(getContext(), stingBottData, "timeline_bottom_data");
        }

        if ((goalList != null) && (goalList != null)) {
            String stingGoalData = castPojoListToString(goalList);
            DBRequest.updateDoctorData(getContext(), stingGoalData, "goal_data");

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
            Toast.makeText(getContext(), "Service token unavailable", Toast.LENGTH_LONG).show();
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

        DBRequest.updateDoctorData(getContext(), stingData, "timeline_top_data");
        DBRequest.updateDoctorData(getContext(), stingBottData, "timeline_bottom_data");
        DBRequest.updateDoctorData(getContext(), stingGoalData, "goal_data");


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

        DBString jsonStringData_Obj = DBRequest.getCashDataByType(getContext(), "timeline_firstpage_data");
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
                            Intent in = new Intent(getContext(), LoginActivity_First.class);
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
            progressDialog.show();
            progressDialog.setMessage("Loading...");
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
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResult() == 401) {
                            Intent in = new Intent(getContext(), LoginActivity_First.class);
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

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

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

                if (Constants.type == Constants.Type.AYUBO) {
                    bottom_menu_view.setVisibility(View.VISIBLE);
                }
                if (Constants.type == Constants.Type.HEMAS) {
                    bottom_menu_view.setVisibility(View.VISIBLE);
                }
                if (Constants.type == Constants.Type.SHESHELLS) {
                    bottom_menu_view.setVisibility(View.INVISIBLE);
                }

                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getResult() == 401) {
                            Intent in = new Intent(getContext(), LoginActivity_First.class);
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

        if (isNeedShowProgress) {
            progressDialog.show();
            progressDialog.setMessage("Please wait...");
        }

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call = apiService.getAllPost(AppConfig.APP_BRANDING_ID, appToke, currentPage, offset, true, maxPostId);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Response<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline obj = response.body();

//                if(code==401)
                if (response.isSuccessful()) {

                    if (response.body().getResult() == 401) {
                        Intent in = new Intent(getContext(), LoginActivity_First.class);
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
                                        DBRequest.updateDoctorData(getContext(), json, "timeline_firstpage_data");

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
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                getNotificationsData();
                t.printStackTrace();

            }
        });


    }

    void loadTimelineOffline(ArrayList<Post> data) {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        adapter.clear();
        adapter.addAll(data);
        rv.setAdapter(adapter);

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
        rv.setAdapter(adapter);

        String deviceName = pref.getDeviceData().get("stepdevice");
//        if (deviceName != null) {
//            if (deviceName.equals("activity_AYUBO")) {
//                if (pref.isGoogleFitEnabled().equals("true")) {
//                    //Send steps to server
//                    check_buildFitnessClientAndSendSteps();
//                }
//            }
//        }


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
                            Intent in = new Intent(getContext(), LoginActivity_First.class);
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
                            Intent in = new Intent(getContext(), LoginActivity_First.class);
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

    void clickedQuickLink(QuickLink objS) {

        //  FirebaseAnalytics Adding
        String tileName = objS.getTitle().toString();


        AppEventsLogger loggerFB = AppEventsLogger.newLogger(getContext());
        loggerFB = AppEventsLogger.newLogger(getContext());

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
            Intent intent = new Intent(getContext(), DashboardActivity.class);
            Ram.setMapSreenshot(null);
            intent.putExtra("activityName", "chanelling");
            startActivity(intent);
        } else if (objS.getType().equals("store_group")) {
            Intent intent = new Intent(getContext(), GroupViewActivity.class);
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
            startActivity(new Intent(getContext(), BookVideoCallActivity.class));
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
            Intent intent = new Intent(getContext(), MyDoctor_Activity.class);
            Ram.setMapSreenshot(null);
            intent.putExtra("activityName", "my_doctor");
            startActivity(intent);
        } else if (objS.getType().equals("contact_us")) {
            Intent intent = new Intent(getContext(), ContactInfoActivity.class);
            Ram.setMapSreenshot(null);
            startActivity(intent);
        } else if (objS.getType().equals("vid_doctor_view")) {
            Intent intent = new Intent(getActivity(), MyDoctorLocations_Activity.class);
            intent.putExtra("doctor_id", objS.getMeta());
            startActivity(intent);
        } else if (objS.getType().equals("doc_chat")) {
            AyuboChatActivity.Companion.startActivity(getActivity(), objS.getMeta(), false, false, "");
        } else if (objS.getType().equals("doctor_chat")) {
            AyuboChatActivity.Companion.startActivity(getActivity(), objS.getMeta(), false, false, "");
        } else if (objS.getType().equals("phy_chanelling")) {
            Intent intent = new Intent(getContext(), MyDoctor_Activity.class);
            Ram.setMapSreenshot(null);
            intent.putExtra("activityName", "phy_chanelling");
            startActivity(intent);
        } else if (objS.getType().equals("workout")) {
            showWorkoutScreen();
        } else if (objS.getType().equals("medicine")) {
            Intent intent = new Intent(getContext(), Medicine_ViewActivity.class);
            startActivity(intent);
        } else if (objS.getType().equals("challenge")) {
            Intent intent = new Intent(getContext(), NewCHallengeActivity.class);
            startActivity(intent);
        } else if (objS.getType().equals("reports")) {
            Intent intent = new Intent(getContext(), ReportDetailsActivity.class);
            intent.putExtra("data", "all");
            Ram.setReportsType("fromHome");
            startActivity(intent);
        } else if (objS.getType().equals("healthview")) {
            Intent intent = new Intent(getContext(), HealthViewActivityNew.class);
            startActivity(intent);
        } else if (objS.getType().equals("decoder")) {
            Intent intent = new Intent(getContext(), DecoderActivity.class);
            startActivity(intent);
        } else if (objS.getType().equals("store")) {
            openAppStore();
        } else if (objS.getType().equals("prescription")) {
            Intent intent = new Intent(getContext(), Medicine_ViewActivity.class);
            startActivity(intent);
        } else if (objS.getType().equals("commonview")) {
            Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
            intent.putExtra("URL", objS.getMeta());
            startActivity(intent);
        } else if (objS.getType().equals("common")) {
            Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
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
        public void onBindViewHolder(RecyclerView.ViewHolder holde, final int position) {
            switch (getItemViewType(position)) {

                case HEADER:
                    //loadDynamicMenus();
                    final ServiceMenuView heroVh = (ServiceMenuView) holde;

//                    heroVh.layout_services_menu_horizontal.removeAllViews();
//                    heroVh.layout_main_menu_horizontal.removeAllViews();

                    heroVh.txt_whats_on_mind.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), Timeline_NewPost_Activity.class);
                            startActivity(intent);
                        }
                    });

                    heroVh.whatson_content_camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), Timeline_NewPost_Activity.class);
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
                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(burlImg)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(heroVh.profilePicture);
                    }

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getContext());
                    int height = deviceScreenDimension.getDisplayHeight();
                    int width = deviceScreenDimension.getDisplayWidth();

                    int singleTileWidth = width / 4;
                    singleTileWidth = singleTileWidth + singleTileWidth / 4;
                    //==========================================================
                    // LOADING SERVICES MENU===================================
                    if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {


//                        heroVh.txt_today_goals_viewmore.setOnClickListener(new View.OnClickListener() {
//                            public void onClick(View v) {
//                                heroVh.layout_main_menu_horizontal.removeAllViews();
//                                if (isExpandedTodayTiles) {
//                                    isExpandedTodayTiles = false;
//
//                                    for (int num = 0; num < 3; num++) {
//
//                                        TopTile serviceObject = serviceTopTileList.get(num);
//                                        inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
//                                        // View mainSingleTilesView = inflater.inflate(R.layout.newdisign_menu_item, null);
//                                        View mainSingleTilesView = null;
//
//                                        if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                            mainSingleTilesView = inflater.inflate(R.layout.home_toptiles_lifeplus, null);
//
//                                        } else {
//                                            mainSingleTilesView = inflater.inflate(R.layout.card_timeline_new_design_goals, null);
//                                        }
//
//
//                                        View space = inflater.inflate(R.layout.row_item, null);
//
//                                        //final CardView cardview_top_tiles =mainSingleTilesView.findViewById(R.id.cardview_top_tiles);
//                                        final LinearLayout cardview_top_tiles = mainSingleTilesView.findViewById(R.id.cardview_top_tiles);
//
//                                        RelativeLayout image_container = (RelativeLayout) mainSingleTilesView.findViewById(R.id.image_container);
//                                        TextView txt_desc1 = (TextView) mainSingleTilesView.findViewById(R.id.txt_desc2);
//                                        TextView txt_desc2 = (TextView) mainSingleTilesView.findViewById(R.id.txt_desc1);
//
//                                        TextView txt_stepcount = (TextView) mainSingleTilesView.findViewById(R.id.txt_stepcount);
//                                        TextView txt_steps = (TextView) mainSingleTilesView.findViewById(R.id.txt_steps);
//                                        //  TextView btn_action = (TextView) mainSingleTilesView.findViewById(R.id.btn_action);
//
//                                        // LinearLayout btn_action_lay = (LinearLayout) mainSingleTilesView.findViewById(R.id.btn_action_lay);
//                                        ImageView top_tile_bg = (ImageView) mainSingleTilesView.findViewById(R.id.img_quicl_link_bg);
//                                        ImageView img_goal_achive_icon = (ImageView) mainSingleTilesView.findViewById(R.id.img_goal_achive_icon);
//
//                                        cardview_top_tiles.setTag(serviceObject);
//                                        txt_desc1.setTag(serviceObject);
//                                        txt_desc2.setTag(serviceObject);
//
//
//                                        cardview_top_tiles.setOnClickListener(new View.OnClickListener() {
//                                            public void onClick(View v) {
//                                                TopTile obj = (TopTile) v.getTag();
//                                                callOnPickAGoal(obj);
//                                            }
//                                        });
//
//                                        txt_desc1.setOnClickListener(new View.OnClickListener() {
//                                            public void onClick(View v) {
//                                                TopTile obj = (TopTile) v.getTag();
//                                                callOnPickAGoal(obj);
//                                            }
//                                        });
//                                        txt_desc2.setOnClickListener(new View.OnClickListener() {
//                                            public void onClick(View v) {
//                                                TopTile obj = (TopTile) v.getTag();
//                                                callOnPickAGoal(obj);
//                                            }
//                                        });
//
//
//                                        String smallIcon = serviceObject.getIcon();
//                                        String mainImage = serviceObject.getImage();
//
//                                        if (serviceObject.getImage() != null) {
//                                            if (mainImage.contains("zoom_level")) {
//                                                mainImage = mainImage.replace("zoom_level", "xxxhdpi");
//                                            } else if (mainImage.contains("zoom")) {
//                                                mainImage = mainImage.replace("zoom", "xxxhdpi");
//                                            }
//                                        } else {
//                                            mainImage = "";
//                                        }
//
//                                        if (serviceObject.getIcon() != null) {
//                                            if (smallIcon.contains("zoom_level")) {
//                                                smallIcon = smallIcon.replace("zoom_level", "xxxhdpi");
//                                            } else if (smallIcon.contains("zoom")) {
//                                                smallIcon = smallIcon.replace("zoom", "xxxhdpi");
//                                            }
//                                        } else {
//                                            smallIcon = "";
//                                        }
//
//
//                                        int imageSize = Utility.getImageSizeFor_DeviceDensitySize(200);
//
//                                        intWidth = displayMetrics.widthPixels;
//
//                                        String text = serviceObject.getTitle();
//
//                                        System.out.println("==============text================" + text);
//                                        float tileWidthf = 0;
//                                        int tileWidth = 0;
//                                        float tileHeigthf = 0;
//                                        float tileHeigth = 0;
//                                        int myUnit = 0;
//                                        float myUnitf = intWidth / 720;
//
//                                        //  myUnit = (int) myUnitf;
//
//                                        tileHeigthf = myUnitf * 190;
//                                        tileHeigth = (int) tileHeigthf;
//
//                                        if (text.length() < 15) {
//                                            tileWidthf = myUnitf * 290;
//                                        } else if (text.length() < 20) {
//                                            tileWidthf = myUnitf * 390;
//                                        } else if (text.length() > 20) {
//                                            tileWidthf = myUnitf * 400;
//                                        }
//                                        tileWidth = (int) tileWidthf;
//
//
//                                        RequestOptions requestOptionsSamll = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
//                                        Glide.with(getContext()).load(smallIcon)
//                                                .apply(requestOptionsSamll)
//                                                .into(img_goal_achive_icon);
//
//
//                                        String my_goal_name = null;
//                                        if (num != 0) {
//                                            my_goal_name = serviceObject.getTitle();
//                                        } else {
//                                            my_goal_name = pref.getMyGoalData().get("my_goal_name");
//                                            if (my_goal_name.equals("")) {
//                                                my_goal_name = "Pick a goal";
//                                            }
//                                        }
//
//                                        if (my_goal_name.contains(" ")) {
//
//                                            String[] splited = my_goal_name.split("\\s+");
//
//                                            String secondName = null;
//                                            String ste1 = splited[0];
//
//                                            boolean isTwoWords = false;
//                                            if (splited.length > 2) {
//                                                ste1 = splited[0] + " " + splited[1];
//                                                if (ste1.length() < 18) {
//                                                    isTwoWords = true;
//                                                } else {
//                                                    ste1 = splited[0];
//                                                }
//                                            }
//                                            txt_desc1.setText(ste1);
//
//
//                                            if (splited.length == 2) {
//                                                secondName = splited[1];
//                                            } else if (splited.length == 3) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2];
//                                                }
//
//                                            } else if (splited.length == 4) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3];
//                                                }
//
//                                            } else if (splited.length == 5) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4];
//                                                }
//
//                                            } else if (splited.length == 6) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5];
//                                                }
//
//                                            } else if (splited.length == 7) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6];
//                                                }
//                                            } else if (splited.length == 8) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7];
//                                                }
//                                            } else if (splited.length == 9) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8];
//                                                }
//
//                                            } else if (splited.length == 10) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9];
//                                                }
//
//                                            } else if (splited.length == 11) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//
//                                            } else if (splited.length == 12) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 13) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 14) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 15) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 16) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 17) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 18) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 19) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 20) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 21) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            }
//
//
//                                            //  System.out.println("==========secondName=============" + secondName);
//                                            if (secondName != null) {
//                                                if (secondName.length() >= 20) {
//                                                    secondName = secondName.substring(0, Math.min(secondName.length(), 15));
//                                                    secondName = secondName + "...";
//                                                }
//                                                txt_desc2.setText(secondName);
//                                            }
//
//                                            txt_desc1.setText(serviceObject.getSubTitle());
//                                            txt_desc2.setText(serviceObject.getTitle());
//
//                                            if (serviceObject.getType().equals("wellness_dashboard")) {
//                                                txt_stepcount.setVisibility(View.INVISIBLE);
//                                                txt_steps.setVisibility(View.INVISIBLE);
//
//                                                String step = pref.getUserData().get("steps");
//                                                txt_stepcount.setText(step);
//                                            } else {
//                                                txt_stepcount.setVisibility(View.INVISIBLE);
//                                                txt_steps.setVisibility(View.INVISIBLE);
//                                            }
//
//
//                                        } else {
//                                            txt_desc1.setText(my_goal_name);
//                                            txt_desc2.setText("");
//                                        }
//                                        String te = serviceObject.getActionText();
//
//                                        if (num == 0) {
//
//                                            String goalStatus = pref.getMyGoalData().get("my_goal_status");
//                                            String goalImage = pref.getMyGoalData().get("my_goal_image");
//                                            String goalBgImage = pref.getMyGoalData().get("my_goal_bg");
//
//                                            my_goal_name = pref.getMyGoalData().get("my_goal_name");
//
//
//                                            txt_desc1.setText(serviceObject.getSubTitle());
//                                            txt_desc2.setText(serviceObject.getTitle());
//
//
//                                            RequestOptions requestOption = new RequestOptions().override(tileWidth, (int) tileHeigth).diskCacheStrategy(DiskCacheStrategy.ALL);
//                                            //  goalImage
//
//                                            if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                                Glide.with(getContext()).load(goalBgImage)
//                                                        .apply(requestOptionsSamll)
//                                                        .into(img_goal_achive_icon);
//                                            } else {
//                                                top_tile_bg.setOnClickListener(new View.OnClickListener() {
//                                                    public void onClick(View v) {
//                                                        TopTile obj = (TopTile) v.getTag();
//                                                        callOnPickAGoal(obj);
//                                                    }
//                                                });
//                                                top_tile_bg.setTag(serviceObject);
//                                                Glide.with(getContext()).load(goalBgImage)
//                                                        .apply(requestOption)
//                                                        .into(top_tile_bg);
//
//                                                Glide.with(getContext()).load(goalImage)
//                                                        .apply(requestOptionsSamll)
//                                                        .into(img_goal_achive_icon);
//                                            }
//
//
//                                            if (goalStatus == null) {
//
//
//                                                if (Constants.type == Constants.Type.LIFEPLUS) {
//                                                    Glide.with(getContext()).load(goalImage)
//                                                            .apply(requestOptionsSamll)
//                                                            .into(img_goal_achive_icon);
//                                                } else {
//                                                    Glide.with(getContext()).load(goalBgImage)
//                                                            .apply(requestOption)
//                                                            .into(top_tile_bg);
//
//                                                    Glide.with(getContext()).load(goalImage)
//                                                            .apply(requestOptionsSamll)
//                                                            .into(img_goal_achive_icon);
//                                                }
//
//
//                                                if (goalStatus.equals("Pending")) {
//                                                    //   btn_action.setTextColor(Color.parseColor("#ff860b"));
//                                                    txt_desc1.setText("Let's Do It");
//
//                                                    System.out.println("==============Pending===============" + te);
//                                                } else if (goalStatus.equals("Pick")) {
//                                                    txt_desc2.setText("Pick a");
//                                                    txt_desc1.setText("goal");
//                                                } else if (goalStatus.equals("Completed")) {
//                                                    System.out.println("==============Completed===============" + te);
//                                                    txt_desc1.setText("Done");
//                                                    //  txt_desc1.setTextColor(Color.parseColor("#45D100"));
//                                                }
////
////                                }//
//                                            } else {
//
//                                                if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                                    Glide.with(getContext()).load(goalBgImage)
//                                                            .apply(requestOptionsSamll)
//                                                            .into(img_goal_achive_icon);
//                                                } else {
//                                                    Glide.with(getContext()).load(goalBgImage)
//                                                            .apply(requestOption)
//                                                            .into(top_tile_bg);
//
//                                                    Glide.with(getContext()).load(goalImage)
//                                                            .apply(requestOptionsSamll)
//                                                            .into(img_goal_achive_icon);
//                                                }
//
//
//                                                if (goalStatus.equals("Pending")) {
//                                                    txt_desc2.setText(my_goal_name);
//                                                    txt_desc1.setText("Let's Do It");
//                                                } else if (goalStatus.equals("Pick")) {
//                                                    txt_desc2.setText("Pick a");
//                                                    txt_desc1.setText("goal");
//                                                } else if (goalStatus.equals("Completed")) {
//                                                    txt_desc2.setText(my_goal_name);
//                                                    txt_desc1.setText("Done");
//                                                }
//
//
//                                            }
//                                        } else {
//                                            txt_desc1.setText(serviceObject.getSubTitle());
//                                            txt_desc2.setText(serviceObject.getTitle());
//
//                                            if (serviceObject.getType().equals("wellness_dashboard")) {
//                                                txt_stepcount.setVisibility(View.INVISIBLE);
//                                                txt_steps.setVisibility(View.INVISIBLE);
//                                                String ste = pref.getUserData().get("steps");
//                                                txt_stepcount.setText(ste);
//                                            } else {
//                                                txt_stepcount.setVisibility(View.INVISIBLE);
//                                                txt_steps.setVisibility(View.INVISIBLE);
//                                            }
//
//                                            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(tileWidth, (int) tileHeigth);
//
//                                            if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                                Glide.with(getContext()).load(mainImage)
//                                                        .apply(requestOptions)
//                                                        .into(img_goal_achive_icon);
//                                            } else {
//                                                Glide.with(getContext()).load(mainImage)
//                                                        .apply(requestOptions)
//                                                        .into(top_tile_bg);
//                                            }
//
//
//                                        }
//
//
//                                        String deviceName = pref.getDeviceData().get("stepdevice");
//
//
//                                        if ((!deviceName.equals("activity_AYUBO"))) {
//                                            //Hide challenges Titles .................................
//                                            heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
//
//                                            if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                                View spacetendp = inflater.inflate(R.layout.row_item_ten_dp, null);
//                                                heroVh.layout_main_menu_horizontal.addView(spacetendp);
//                                            } else {
//                                                heroVh.layout_main_menu_horizontal.addView(space);
//                                            }
//
//                                            heroVh.layout_main_menu_horizontal.setBackgroundColor(Color.TRANSPARENT);
//                                        } else if ((pref.isGoogleFitEnabled().equals("false") && (serviceObject.getType().equals("join_adv_challenge"))) || (pref.isGoogleFitEnabled().equals("false") && (serviceObject.getType().equals("view_adv_challenge")))) {
//                                            //Hide challenges Titles .................................
//                                            Log.d("NewHomeDesign", "Hiding Tiles bcos not GFit Connected============");
//                                            heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
//                                            if (Constants.type == Constants.Type.LIFEPLUS) {
//                                                View spacetendp = inflater.inflate(R.layout.row_item_ten_dp, null);
//                                                heroVh.layout_main_menu_horizontal.addView(spacetendp);
//                                            } else {
//                                                heroVh.layout_main_menu_horizontal.addView(space);
//                                            }
//                                        } else {
//                                            //Show other  Titles .................................
//                                            Log.d("NewHomeDesign", "Showing other Tiles============");
//                                            heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
//                                            if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                                View spacetendp = inflater.inflate(R.layout.row_item_ten_dp, null);
//                                                heroVh.layout_main_menu_horizontal.addView(spacetendp);
//                                            } else {
//                                                heroVh.layout_main_menu_horizontal.addView(space);
//                                            }
//
//                                            heroVh.layout_main_menu_horizontal.setBackgroundColor(Color.TRANSPARENT);
//                                        }
//
//                                    }
////                                    if (serviceTopTileList.size() > 3) {
////                                        heroVh.txt_today_goals_viewmore.setText("More");
////                                        heroVh.txt_today_goals_viewmore.setVisibility(View.VISIBLE);
////                                    } else {
////                                        heroVh.txt_today_goals_viewmore.setVisibility(View.GONE);
////                                    }
//
//
//                                } else {
//                                    isExpandedTodayTiles = true;
//
//                                    for (int num = 0; num < serviceTopTileList.size(); num++) {
//
//                                        TopTile serviceObject = serviceTopTileList.get(num);
//                                        inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
//                                        // View mainSingleTilesView = inflater.inflate(R.layout.newdisign_menu_item, null);
//                                        View mainSingleTilesView = null;
//
//                                        if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                            mainSingleTilesView = inflater.inflate(R.layout.home_toptiles_lifeplus, null);
//
//                                        } else {
//                                            mainSingleTilesView = inflater.inflate(R.layout.card_timeline_new_design_goals, null);
//                                        }
//
//
//                                        View space = inflater.inflate(R.layout.row_item, null);
//
//                                        //final CardView cardview_top_tiles =mainSingleTilesView.findViewById(R.id.cardview_top_tiles);
//                                        final LinearLayout cardview_top_tiles = mainSingleTilesView.findViewById(R.id.cardview_top_tiles);
//
//                                        RelativeLayout image_container = (RelativeLayout) mainSingleTilesView.findViewById(R.id.image_container);
//                                        TextView txt_desc1 = (TextView) mainSingleTilesView.findViewById(R.id.txt_desc2);
//                                        TextView txt_desc2 = (TextView) mainSingleTilesView.findViewById(R.id.txt_desc1);
//
//                                        TextView txt_stepcount = (TextView) mainSingleTilesView.findViewById(R.id.txt_stepcount);
//                                        TextView txt_steps = (TextView) mainSingleTilesView.findViewById(R.id.txt_steps);
//                                        //  TextView btn_action = (TextView) mainSingleTilesView.findViewById(R.id.btn_action);
//
//                                        // LinearLayout btn_action_lay = (LinearLayout) mainSingleTilesView.findViewById(R.id.btn_action_lay);
//                                        ImageView top_tile_bg = (ImageView) mainSingleTilesView.findViewById(R.id.img_quicl_link_bg);
//                                        ImageView img_goal_achive_icon = (ImageView) mainSingleTilesView.findViewById(R.id.img_goal_achive_icon);
//
//                                        cardview_top_tiles.setTag(serviceObject);
//                                        txt_desc1.setTag(serviceObject);
//                                        txt_desc2.setTag(serviceObject);
//
//
//                                        cardview_top_tiles.setOnClickListener(new View.OnClickListener() {
//                                            public void onClick(View v) {
//                                                TopTile obj = (TopTile) v.getTag();
//                                                callOnPickAGoal(obj);
//                                            }
//                                        });
//
//                                        txt_desc1.setOnClickListener(new View.OnClickListener() {
//                                            public void onClick(View v) {
//                                                TopTile obj = (TopTile) v.getTag();
//                                                callOnPickAGoal(obj);
//                                            }
//                                        });
//                                        txt_desc2.setOnClickListener(new View.OnClickListener() {
//                                            public void onClick(View v) {
//                                                TopTile obj = (TopTile) v.getTag();
//                                                callOnPickAGoal(obj);
//                                            }
//                                        });
//
//
//                                        String smallIcon = serviceObject.getIcon();
//                                        String mainImage = serviceObject.getImage();
//
//                                        if (serviceObject.getImage() != null) {
//                                            if (mainImage.contains("zoom_level")) {
//                                                mainImage = mainImage.replace("zoom_level", "xxxhdpi");
//                                            } else if (mainImage.contains("zoom")) {
//                                                mainImage = mainImage.replace("zoom", "xxxhdpi");
//                                            }
//                                        } else {
//                                            mainImage = "";
//                                        }
//
//                                        if (serviceObject.getIcon() != null) {
//                                            if (smallIcon.contains("zoom_level")) {
//                                                smallIcon = smallIcon.replace("zoom_level", "xxxhdpi");
//                                            } else if (smallIcon.contains("zoom")) {
//                                                smallIcon = smallIcon.replace("zoom", "xxxhdpi");
//                                            }
//                                        } else {
//                                            smallIcon = "";
//                                        }
//
//
//                                        int imageSize = Utility.getImageSizeFor_DeviceDensitySize(200);
//
//                                        intWidth = displayMetrics.widthPixels;
//
//                                        String text = serviceObject.getTitle();
//
//                                        System.out.println("==============text================" + text);
//                                        float tileWidthf = 0;
//                                        int tileWidth = 0;
//                                        float tileHeigthf = 0;
//                                        float tileHeigth = 0;
//                                        int myUnit = 0;
//                                        float myUnitf = intWidth / 720;
//
//                                        //  myUnit = (int) myUnitf;
//
//                                        tileHeigthf = myUnitf * 190;
//                                        tileHeigth = (int) tileHeigthf;
//
//                                        if (text.length() < 15) {
//                                            tileWidthf = myUnitf * 290;
//                                        } else if (text.length() < 20) {
//                                            tileWidthf = myUnitf * 390;
//                                        } else if (text.length() > 20) {
//                                            tileWidthf = myUnitf * 400;
//                                        }
//                                        tileWidth = (int) tileWidthf;
//
//
//                                        RequestOptions requestOptionsSamll = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
//                                        Glide.with(getContext()).load(smallIcon)
//                                                .apply(requestOptionsSamll)
//                                                .into(img_goal_achive_icon);
//
//
//                                        String my_goal_name = null;
//                                        if (num != 0) {
//                                            my_goal_name = serviceObject.getTitle();
//                                        } else {
//                                            my_goal_name = pref.getMyGoalData().get("my_goal_name");
//                                            if (my_goal_name.equals("")) {
//                                                my_goal_name = "Pick a goal";
//                                            }
//                                        }
//
//                                        if (my_goal_name.contains(" ")) {
//
//                                            String[] splited = my_goal_name.split("\\s+");
//
//                                            String secondName = null;
//                                            String ste1 = splited[0];
//
//                                            boolean isTwoWords = false;
//                                            if (splited.length > 2) {
//                                                ste1 = splited[0] + " " + splited[1];
//                                                if (ste1.length() < 18) {
//                                                    isTwoWords = true;
//                                                } else {
//                                                    ste1 = splited[0];
//                                                }
//                                            }
//                                            txt_desc1.setText(ste1);
//
//
//                                            if (splited.length == 2) {
//                                                secondName = splited[1];
//                                            } else if (splited.length == 3) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2];
//                                                }
//
//                                            } else if (splited.length == 4) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3];
//                                                }
//
//                                            } else if (splited.length == 5) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4];
//                                                }
//
//                                            } else if (splited.length == 6) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5];
//                                                }
//
//                                            } else if (splited.length == 7) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6];
//                                                }
//                                            } else if (splited.length == 8) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7];
//                                                }
//                                            } else if (splited.length == 9) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8];
//                                                }
//
//                                            } else if (splited.length == 10) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9];
//                                                }
//
//                                            } else if (splited.length == 11) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//
//                                            } else if (splited.length == 12) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 13) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 14) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 15) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 16) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 17) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 18) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 19) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 20) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            } else if (splited.length == 21) {
//                                                if (isTwoWords) {
//                                                    secondName = splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                } else {
//                                                    secondName = splited[1] + " " + splited[2] + " " + splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7] + " " + splited[8] + " " + splited[9] + " " + splited[10];
//                                                }
//                                            }
//
//
//                                            //  System.out.println("==========secondName=============" + secondName);
//                                            if (secondName != null) {
//                                                if (secondName.length() >= 20) {
//                                                    secondName = secondName.substring(0, Math.min(secondName.length(), 15));
//                                                    secondName = secondName + "...";
//                                                }
//                                                txt_desc2.setText(secondName);
//                                            }
//
//                                            txt_desc1.setText(serviceObject.getSubTitle());
//                                            txt_desc2.setText(serviceObject.getTitle());
//
//                                            if (serviceObject.getType().equals("wellness_dashboard")) {
//                                                txt_stepcount.setVisibility(View.INVISIBLE);
//                                                txt_steps.setVisibility(View.INVISIBLE);
//
//                                                String step = pref.getUserData().get("steps");
//                                                txt_stepcount.setText(step);
//                                            } else {
//                                                txt_stepcount.setVisibility(View.INVISIBLE);
//                                                txt_steps.setVisibility(View.INVISIBLE);
//                                            }
//
//
//                                        } else {
//                                            txt_desc1.setText(my_goal_name);
//                                            txt_desc2.setText("");
//                                        }
//                                        String te = serviceObject.getActionText();
//
//                                        if (num == 0) {
//
//                                            String goalStatus = pref.getMyGoalData().get("my_goal_status");
//                                            String goalImage = pref.getMyGoalData().get("my_goal_image");
//                                            String goalBgImage = pref.getMyGoalData().get("my_goal_bg");
//
//                                            my_goal_name = pref.getMyGoalData().get("my_goal_name");
//
//
//                                            txt_desc1.setText(serviceObject.getSubTitle());
//                                            txt_desc2.setText(serviceObject.getTitle());
//
//
//                                            RequestOptions requestOption = new RequestOptions().override(tileWidth, (int) tileHeigth).diskCacheStrategy(DiskCacheStrategy.ALL);
//                                            //  goalImage
//
//                                            if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                                Glide.with(getContext()).load(goalBgImage)
//                                                        .apply(requestOptionsSamll)
//                                                        .into(img_goal_achive_icon);
//                                            } else {
//                                                top_tile_bg.setOnClickListener(new View.OnClickListener() {
//                                                    public void onClick(View v) {
//                                                        TopTile obj = (TopTile) v.getTag();
//                                                        callOnPickAGoal(obj);
//                                                    }
//                                                });
//                                                top_tile_bg.setTag(serviceObject);
//                                                Glide.with(getContext()).load(goalBgImage)
//                                                        .apply(requestOption)
//                                                        .into(top_tile_bg);
//
//                                                Glide.with(getContext()).load(goalImage)
//                                                        .apply(requestOptionsSamll)
//                                                        .into(img_goal_achive_icon);
//                                            }
//
//
//                                            if (goalStatus == null) {
//
//
//                                                if (Constants.type == Constants.Type.LIFEPLUS) {
//                                                    Glide.with(getContext()).load(goalImage)
//                                                            .apply(requestOptionsSamll)
//                                                            .into(img_goal_achive_icon);
//                                                } else {
//                                                    Glide.with(getContext()).load(goalBgImage)
//                                                            .apply(requestOption)
//                                                            .into(top_tile_bg);
//
//                                                    Glide.with(getContext()).load(goalImage)
//                                                            .apply(requestOptionsSamll)
//                                                            .into(img_goal_achive_icon);
//                                                }
//
//
//                                                if (goalStatus.equals("Pending")) {
//                                                    //   btn_action.setTextColor(Color.parseColor("#ff860b"));
//                                                    txt_desc1.setText("Let's Do It");
//
//                                                    System.out.println("==============Pending===============" + te);
//                                                } else if (goalStatus.equals("Pick")) {
//                                                    txt_desc2.setText("Pick a");
//                                                    txt_desc1.setText("goal");
//                                                } else if (goalStatus.equals("Completed")) {
//                                                    System.out.println("==============Completed===============" + te);
//                                                    txt_desc1.setText("Done");
//                                                    //  txt_desc1.setTextColor(Color.parseColor("#45D100"));
//                                                }
////
////                                }//
//                                            } else {
//
//                                                if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                                    Glide.with(getContext()).load(goalBgImage)
//                                                            .apply(requestOptionsSamll)
//                                                            .into(img_goal_achive_icon);
//                                                } else {
//                                                    Glide.with(getContext()).load(goalBgImage)
//                                                            .apply(requestOption)
//                                                            .into(top_tile_bg);
//
//                                                    Glide.with(getContext()).load(goalImage)
//                                                            .apply(requestOptionsSamll)
//                                                            .into(img_goal_achive_icon);
//                                                }
//
//
//                                                if (goalStatus.equals("Pending")) {
//                                                    txt_desc2.setText(my_goal_name);
//                                                    txt_desc1.setText("Let's Do It");
//                                                } else if (goalStatus.equals("Pick")) {
//                                                    txt_desc2.setText("Pick a");
//                                                    txt_desc1.setText("goal");
//                                                } else if (goalStatus.equals("Completed")) {
//                                                    txt_desc2.setText(my_goal_name);
//                                                    txt_desc1.setText("Done");
//                                                }
//
//
//                                            }
//                                        } else {
//                                            txt_desc1.setText(serviceObject.getSubTitle());
//                                            txt_desc2.setText(serviceObject.getTitle());
//
//                                            if (serviceObject.getType().equals("wellness_dashboard")) {
//                                                txt_stepcount.setVisibility(View.INVISIBLE);
//                                                txt_steps.setVisibility(View.INVISIBLE);
//                                                String ste = pref.getUserData().get("steps");
//                                                txt_stepcount.setText(ste);
//                                            } else {
//                                                txt_stepcount.setVisibility(View.INVISIBLE);
//                                                txt_steps.setVisibility(View.INVISIBLE);
//                                            }
//
//                                            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(tileWidth, (int) tileHeigth);
//
//                                            if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                                Glide.with(getContext()).load(mainImage)
//                                                        .apply(requestOptions)
//                                                        .into(img_goal_achive_icon);
//                                            } else {
//                                                Glide.with(getContext()).load(mainImage)
//                                                        .apply(requestOptions)
//                                                        .into(top_tile_bg);
//                                            }
//
//
//                                        }
//
//
//                                        String deviceName = pref.getDeviceData().get("stepdevice");
//
//
//                                        if ((!deviceName.equals("activity_AYUBO"))) {
//                                            //Hide challenges Titles .................................
//                                            heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
//
//                                            if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                                View spacetendp = inflater.inflate(R.layout.row_item_ten_dp, null);
//                                                heroVh.layout_main_menu_horizontal.addView(spacetendp);
//                                            } else {
//                                                heroVh.layout_main_menu_horizontal.addView(space);
//                                            }
//
//                                            heroVh.layout_main_menu_horizontal.setBackgroundColor(Color.TRANSPARENT);
//                                        } else if ((pref.isGoogleFitEnabled().equals("false") && (serviceObject.getType().equals("join_adv_challenge"))) || (pref.isGoogleFitEnabled().equals("false") && (serviceObject.getType().equals("view_adv_challenge")))) {
//                                            //Hide challenges Titles .................................
//                                            Log.d("NewHomeDesign", "Hiding Tiles bcos not GFit Connected============");
//                                            heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
//                                            if (Constants.type == Constants.Type.LIFEPLUS) {
//                                                View spacetendp = inflater.inflate(R.layout.row_item_ten_dp, null);
//                                                heroVh.layout_main_menu_horizontal.addView(spacetendp);
//                                            } else {
//                                                heroVh.layout_main_menu_horizontal.addView(space);
//                                            }
//                                        } else {
//                                            //Show other  Titles .................................
//                                            Log.d("NewHomeDesign", "Showing other Tiles============");
//                                            heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
//                                            if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                                View spacetendp = inflater.inflate(R.layout.row_item_ten_dp, null);
//                                                heroVh.layout_main_menu_horizontal.addView(spacetendp);
//                                            } else {
//                                                heroVh.layout_main_menu_horizontal.addView(space);
//                                            }
//
//                                            heroVh.layout_main_menu_horizontal.setBackgroundColor(Color.TRANSPARENT);
//                                        }
//
//                                    }
//                                    heroVh.txt_today_goals_viewmore.setText("Less");
//                                }
//
//
//                            }
//                        });

                        int numberDisplay = 3;
                        if (serviceTopTileList.size() < 3) {
                            numberDisplay = serviceTopTileList.size();
                        }

                        for (int num = 0; num < numberDisplay; num++) {

                            TopTile serviceObject = serviceTopTileList.get(num);
                            inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
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
                            Glide.with(getContext()).load(smallIcon)
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
                                    Glide.with(getContext()).load(goalBgImage)
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
                                    Glide.with(getContext()).load(goalBgImage)
                                            .apply(requestOption)
                                            .into(top_tile_bg);

                                    Glide.with(getContext()).load(goalImage)
                                            .apply(requestOptionsSamll)
                                            .into(img_goal_achive_icon);
                                }


                                if (goalStatus == null) {


                                    if (Constants.type == Constants.Type.LIFEPLUS) {
                                        Glide.with(getContext()).load(goalImage)
                                                .apply(requestOptionsSamll)
                                                .into(img_goal_achive_icon);
                                    } else {
                                        Glide.with(getContext()).load(goalBgImage)
                                                .apply(requestOption)
                                                .into(top_tile_bg);

                                        Glide.with(getContext()).load(goalImage)
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
                                        Glide.with(getContext()).load(goalBgImage)
                                                .apply(requestOptionsSamll)
                                                .into(img_goal_achive_icon);
                                    } else {
                                        Glide.with(getContext()).load(goalBgImage)
                                                .apply(requestOption)
                                                .into(top_tile_bg);

                                        Glide.with(getContext()).load(goalImage)
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
                                    Glide.with(getContext()).load(mainImage)
                                            .apply(requestOptions)
                                            .into(img_goal_achive_icon);
                                } else {
                                    Glide.with(getContext()).load(mainImage)
                                            .apply(requestOptions)
                                            .into(top_tile_bg);
                                }


                            }


                            String deviceName = pref.getDeviceData().get("stepdevice");


//                            if ((!deviceName.equals("activity_AYUBO"))) {
//                                //Hide challenges Titles .................................
//                                heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
//
//                                if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                    View spacetendp = inflater.inflate(R.layout.row_item_ten_dp, null);
//                                    heroVh.layout_main_menu_horizontal.addView(spacetendp);
//                                } else {
//                                    heroVh.layout_main_menu_horizontal.addView(space);
//                                }
//
//                                heroVh.layout_main_menu_horizontal.setBackgroundColor(Color.TRANSPARENT);
//                            }
//
//                            else if (
//                                    (pref.isGoogleFitEnabled().equals("false") && (serviceObject.getType().equals("join_adv_challenge"))) ||
//                                            (pref.isGoogleFitEnabled().equals("false") && (serviceObject.getType().equals("view_adv_challenge")))) {
//                                //Hide challenges Titles .................................
//                                Log.d("NewHomeDesign", "Hiding Tiles bcos not GFit Connected============");
//                                heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
//                                if (Constants.type == Constants.Type.LIFEPLUS) {
//                                    View spacetendp = inflater.inflate(R.layout.row_item_ten_dp, null);
//                                    heroVh.layout_main_menu_horizontal.addView(spacetendp);
//                                } else {
//                                    heroVh.layout_main_menu_horizontal.addView(space);
//                                }
//                            }
//
//                            else {
//                                //Show other  Titles .................................
//                                Log.d("NewHomeDesign", "Showing other Tiles============");
//                                heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
//                                if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                    View spacetendp = inflater.inflate(R.layout.row_item_ten_dp, null);
//                                    heroVh.layout_main_menu_horizontal.addView(spacetendp);
//                                } else {
//                                    heroVh.layout_main_menu_horizontal.addView(space);
//                                }
//
//                                heroVh.layout_main_menu_horizontal.setBackgroundColor(Color.TRANSPARENT);
//                            }

                        }

                    } else {

                        for (int num = 0; num < 3; num++) {
                            // SHESHELLS HOME WIDGET LOADING .....

                            TopTile serviceObject = serviceTopTileList.get(num);
                            inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
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
                            Glide.with(getContext()).load(smallIcon)
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
                                    Glide.with(getContext()).load(goalBgImage)
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
                                    Glide.with(getContext()).load(goalBgImage)
                                            .apply(requestOption)
                                            .into(top_tile_bg);

                                    Glide.with(getContext()).load(goalImage)
                                            .apply(requestOptionsSamll)
                                            .into(img_goal_achive_icon);
                                }


                                if (goalStatus == null) {


                                    if (Constants.type == Constants.Type.LIFEPLUS) {
                                        Glide.with(getContext()).load(goalImage)
                                                .apply(requestOptionsSamll)
                                                .into(img_goal_achive_icon);
                                    } else {
                                        Glide.with(getContext()).load(goalBgImage)
                                                .apply(requestOption)
                                                .into(top_tile_bg);

                                        Glide.with(getContext()).load(goalImage)
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
                                        Glide.with(getContext()).load(goalBgImage)
                                                .apply(requestOptionsSamll)
                                                .into(img_goal_achive_icon);
                                    } else {
                                        Glide.with(getContext()).load(goalBgImage)
                                                .apply(requestOption)
                                                .into(top_tile_bg);

                                        Glide.with(getContext()).load(goalImage)
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
                                    Glide.with(getContext()).load(mainImage)
                                            .apply(requestOptions)
                                            .into(img_goal_achive_icon);
                                } else {
                                    Glide.with(getContext()).load(mainImage)
                                            .apply(requestOptions)
                                            .into(top_tile_bg);
                                }


                            }


                            String deviceName = pref.getDeviceData().get("stepdevice");


//                            if ((!deviceName.equals("activity_AYUBO"))) {
//                                //Hide challenges Titles .................................
//                                heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
//
//                                if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                    View spacetendp = inflater.inflate(R.layout.row_item_ten_dp, null);
//                                    heroVh.layout_main_menu_horizontal.addView(spacetendp);
//                                } else {
//                                    heroVh.layout_main_menu_horizontal.addView(space);
//                                }
//
//                                heroVh.layout_main_menu_horizontal.setBackgroundColor(Color.TRANSPARENT);
//                            }
//
//                            else if ((pref.isGoogleFitEnabled().equals("false") && (serviceObject.getType().equals("join_adv_challenge"))) ||
//                                    (pref.isGoogleFitEnabled().equals("false") && (serviceObject.getType().equals("view_adv_challenge")))) {
//                                //Hide challenges Titles .................................
//                                Log.d("NewHomeDesign", "Hiding Tiles bcos not GFit Connected============");
//                                heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
//                                if (Constants.type == Constants.Type.LIFEPLUS) {
//                                    View spacetendp = inflater.inflate(R.layout.row_item_ten_dp, null);
//                                    heroVh.layout_main_menu_horizontal.addView(spacetendp);
//                                } else {
//                                    heroVh.layout_main_menu_horizontal.addView(space);
//                                }
//                            }
//
//                            else {
//                                //Show other  Titles .................................
//                                Log.d("NewHomeDesign", "Showing other Tiles============");
//                                heroVh.layout_main_menu_horizontal.addView(mainSingleTilesView);
//                                if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {
//                                    View spacetendp = inflater.inflate(R.layout.row_item_ten_dp, null);
//                                    heroVh.layout_main_menu_horizontal.addView(spacetendp);
//                                } else {
//                                    heroVh.layout_main_menu_horizontal.addView(space);
//                                }
//
//                                heroVh.layout_main_menu_horizontal.setBackgroundColor(Color.TRANSPARENT);
//                            }

                        }
                    }

                    //==========================================================
                    // LOADING SERVICES MENU===================================

                    //==================Hiding Tiles menu =======================================
                    // LOADING SERVICES MENU===================================


                    //==========================================================
                    //==============================================================
                    String heading = pref.getBannerData().get("bheading");
//                    if(pref.getBannerData().get("bheading")!=null) {
//
//                        if (pref.getBannerData().get("bheading").equals("")) {
//
//                        } else {
//                            inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
//                            View myViewSec = null;
//                            myViewSec = inflater.inflate(R.layout.new_banner_cell, null);
//
//                           int intWi = displayMetrics.widthPixels;
//                           int heigh=intWi/2;
//
//                            TextView txt_banner_header = myViewSec.findViewById(R.id.txt_banner_header);
//                            TextView txt_banner_desc = myViewSec.findViewById(R.id.txt_banner_desc);
//                            TextView txt_banner_button = myViewSec.findViewById(R.id.txt_banner_button);
//                            String bheading = pref.getBannerData().get("bheading");
//                            String desc = pref.getBannerData().get("text");
//
//                            txt_banner_header.setText(pref.getBannerData().get("bheading"));
//                            txt_banner_desc.setText(pref.getBannerData().get("text"));
//                            txt_banner_button.setText(pref.getBannerData().get("bbtext"));
//                            String bannerImage = pref.getBannerData().get("bimage");
//
//                            myViewSec.setLayoutParams(new LinearLayout.LayoutParams(intWi,heigh));
//
//                            heroVh.layout_main_menu_horizontal.addView(myViewSec);
//                        }
//                    }

                    for (int i = 0; i < serviceBottomTileList.size(); i++) {
                        final QuickLink objSecond = serviceBottomTileList.get(i);
                        inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
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
//                                Glide.with(getContext()).load("https://apps.ayubo.life/static/hh/quick_links_new.png")
//                                        .apply(requestOption)
//                                        .into(remote_image_icon_quicklink);
                            } else {
                                Glide.with(getContext()).load(MAIN_URL_APPS + "static/hh/quick_links2.png")
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
                                Glide.with(context).load(img)
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
                                            showAlert_Add(getContext(), "", "Are you sure you want to delete post?", obj);
                                        }
                                    });
                                    holder.img_btn_close_bglayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Post obj = (Post) holder.img_btn_close_intop.getTag();
                                            showAlert_Add(getContext(), "", "Are you sure you want to delete post?", obj);
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
                                    Intent intent = new Intent(getContext(), AddComments_Activity.class);
                                    intent.putExtra("type", "timeline");
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                            });


                            //======Comments Clicked =========================================
                            holder.user_reaction_panel_comment_second_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), AddComments_Activity.class);
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
                                        Intent intent = new Intent(getContext(), TimelineVideo_Activity.class);
                                        intent.putExtra("IURL", obj.getVideoThumbnail());
                                        intent.putExtra("URL", obj.getVideoUrl());
                                        startActivity(intent);
                                    } else if (obj.getType().equals("GIF_POST")) {
                                        Intent intent = new Intent(getContext(), TimelineGif_Activity.class);
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

                                    Intent intent = new Intent(getContext(), LikedUsers_Activity.class);
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

                                    Intent intent = new Intent(getContext(), LikedUsers_Activity.class);
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
                                RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).transforms(new CircleTransform(context));
                                Glide.with(context).load(obj.getUser().getProfilePicture())
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
                                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
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
                                            Intent intent = new Intent(context, CommonWebViewActivity.class);
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

                                Glide.with(context).load(imageURL)
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
                                                Intent intent = new Intent(getContext(), HelpFeedbackActivity.class);
                                                startActivity(intent);
                                            }
                                            if (type.equals(CONSTANTS.ACTION_KEY_CHALLENGE_OPEN)) {
                                                challenge_id = obj.getRedirectUrl();

//                                            Intent in = new Intent(getContext(), MapChallangeActivity.class);
//                                            in.putExtra(EXTRA_CHALLANGE_ID, challenge_id);
//                                            startActivity(in);
                                                onMapChallangeClick(challenge_id);
//                                            Intent intent = new Intent(getContext(), MapChallengeActivity.class);
//                                            intent.putExtra("challenge_id", challenge_id);
//                                            startActivity(intent);

                                            } else if (type.equals(CONSTANTS.ACTION_KEY_CHALLENGE_JOIN)) {
                                                String challengeID = obj.getRedirectUrl();
                                                Intent intent = new Intent(getContext(), MapJoinChallenge_Activity.class);
                                                intent.putExtra("challenge_id", challengeID);
                                                startActivity(intent);
                                            } else if (obj.isExternalLink()) {

                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getRedirectUrl()));
                                                startActivity(browserIntent);


                                            } else {
                                                Intent intent = new Intent(context, CommonWebViewActivity.class);
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
                                        context.getString(R.string.error_msg_unknown));

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

            Intent intent = new Intent(getContext(), TimelineImage_Activity.class);
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
            float density = context.getResources().getDisplayMetrics().density;
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
            float density = context.getResources().getDisplayMetrics().density;
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
            float density = context.getResources().getDisplayMetrics().density;
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
