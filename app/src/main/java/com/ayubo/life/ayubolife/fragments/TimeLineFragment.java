package com.ayubo.life.ayubolife.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.ContactDetailsActivity;
import com.ayubo.life.ayubolife.activity.DayActivitiesActivity;
import com.ayubo.life.ayubolife.activity.HealthViewActivityNew;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.body.WorkoutActivity;
import com.ayubo.life.ayubolife.challenges.NewCHallengeActivity;
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity;
import com.ayubo.life.ayubolife.health.Health_MainActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.health.RXViewActivity;
import com.ayubo.life.ayubolife.home.EatViewActivity;
import com.ayubo.life.ayubolife.home.PostDataActivity;
import com.ayubo.life.ayubolife.home.RelaxViewActivity;
import com.ayubo.life.ayubolife.home_popup_menu.AskQuestion_Activity;
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity;
import com.ayubo.life.ayubolife.map_challenges.ChallengeCompletedView_Activity;
import com.ayubo.life.ayubolife.map_challenges.Dataset;
import com.ayubo.life.ayubolife.map_challenges.MapChallengeActivity;
import com.ayubo.life.ayubolife.map_challenges.MapJoinChallenge_Activity;
import com.ayubo.life.ayubolife.map_challenges.PanaramaView_NewActivity;
import com.ayubo.life.ayubolife.map_challenges.PanoramaView_LawDevices_Activity;
import com.ayubo.life.ayubolife.mind.Mind_MainActivity;
import com.ayubo.life.ayubolife.model.Achievement;
import com.ayubo.life.ayubolife.model.AchievementObj;
import com.ayubo.life.ayubolife.model.MenuObj;
import com.ayubo.life.ayubolife.model.RoadLocationObj;
import com.ayubo.life.ayubolife.qrcode.DecoderActivity;
import com.ayubo.life.ayubolife.reports.Reports_MainActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flavors.changes.Constants;
import com.google.firebase.crash.FirebaseCrash;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeLineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeLineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeLineFragment extends Fragment {
    private static final String TAG = NewHomeWithSideMenuActivity.class.getSimpleName();
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;
    boolean permissionGrantedStatus = false;
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    String currentUrl;
    PopupWindow mPopupWindow;
    LinearLayout mRelativeLayout;
    ImageButton mButton;


    ArrayList<AchievementObj> achievementObj = null;
    boolean isPopupClicked = false;

    private final static int PICKFILE_REQUEST_CODE = 2;

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;


    // String url=null;

    String loadingStatus;

    boolean isLoaded = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    boolean timeout;
    String hasToken, loginType;
    SwipeRefreshLayout mySwipeRefreshLayout;

    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;

    ImageButton btn_signout;
    TextView btn_Appointment;
    WebView webView;
    SharedPreferences prefs;
    String userid_ExistingUser;
    String link;
    String menuLink;
    String encodedHashToken;
    private boolean isCurrentCameraFront;
    private boolean isLocalVideoFullScreen;
    // String url=null;
    String url_logout = null;
    protected Handler mainHandler;
    boolean st = false;
    private TextView connectionStatusLocal;
    private PrefManager pref;
    WebView browser = null;
    //  final String TAG = "WebviewActivity";
    String requestedURL = null;
    // private ConnectionTimeoutHandler timeoutHandler = null;
    int PAGE_LOAD_PROGRESS = 0;
    final String KEY_REQUESTED_URL = "requested_url";
    final String CALLBACK_URL = "success";
    String url;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int[] ITEM_DRAWABLES = {
            R.drawable.sidemenu_ic_helpw,
            R.drawable.user_activew,
            R.drawable.sidemenu_ic_corporatew,
            R.drawable.sidemenu_ic_qrw,
            R.drawable.sidemenu_ic_connectw,
            R.drawable.sidemenu_ic_sharew};
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2, marketplace_Token_status, marketplace_Token;

    String challenge_id, serviceDataStatus, badgesJsonData;
    String cityJsonString, noof_day;
    ArrayList<RoadLocationObj> myTagList = null;
    ArrayList<RoadLocationObj> myTreasure = null;
    int total_steps;
    String cards, weekSteps;
    List<String> listDataHeader;
    HashMap<String, List<Achievement>> listDataChild;
    //  ArrayList<Achievement>
    List<String> mListHeaders;
    List<String> headerNamesList;
    Map<String, List<Object>> map;
    List<Object> listMap;
    private Dataset dataSet;
    String showpopup;
    ImageButton btn_img_workout, btn_img_body, btn_img_food, btn_img_mind, btn_img_relax, btn_img_eat;
    TextView btn_txt_workout, btn_txt_body, btn_txt_food, btn_txt_mind, btn_txt_relax, btn_txt_eat;
    LinearLayout btn_health, btn_body, btn_workout, btn_mind, main_dark_view, btn_relax, btn_eat;

    String service_checkpoints, enabled_checkpoints;

    private String[] str = {"Feedback", "Profile", "Join Communities", "Scan QR Code", "Connect Devices", "Invite Friends"};
    private OnFragmentInteractionListener mListener;
    //  FrameLayout frameLayout;
    //ArcMenu arcMenu=null;
    boolean ArcMenuStatus = false;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public TimeLineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeLineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeLineFragment newInstance(String param1, String param2) {
        TimeLineFragment fragment = new TimeLineFragment();
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


    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.8f;
        wm.updateViewLayout(container, p);
    }


    private AlertDialog getForgotPassword() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = this.getLayoutInflater();
        final View layoutView = inflater.inflate(R.layout.no_internet_alert, null, false);
        builder2.setView(layoutView);
        builder2.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(getContext(), NewHomeWithSideMenuActivity.class);
                startActivity(intent);

            }
        });
        return builder2.create();
    }

    boolean gyroExists;
    ProgressDialog progressTimeline;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_line,
                container, false);

        if (progressTimeline == null) {
            progressTimeline = Utility.createProgressDialog(getContext());
        }

//        progressTimeline=new ProgressDialog(getActivity());
//
//        progressTimeline.setCancelable(true);
//
//        Window window = progressTimeline.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//
//        wlp.gravity = Gravity.CENTER_HORIZONTAL;
//
//        window.setAttributes(wlp);
        //    progressTimeline.getWindow().setGravity(Gravity.CENTER);
        //    progressTimeline.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        prefs = getContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);


        pref = new PrefManager(getContext());

        userid_ExistingUser = pref.getLoginUser().get("uid");
        hasToken = pref.getLoginUser().get("hashkey");
        loadingStatus = prefs.getString("TimeLine_isLoaded", "false");

        //ADD MENU CODE HERE FROM GMAIL===========================================
        achievementObj = new ArrayList<AchievementObj>();


        PackageManager packageManager = getActivity().getPackageManager();
        gyroExists = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);

        setRetainInstance(true);

        myTreasure = new ArrayList<RoadLocationObj>();

        main_dark_view = (LinearLayout) view.findViewById(R.id.main_dark_view);
        //   main_dark_view.setVisibility(View.GONE);

        btn_workout = (LinearLayout) view.findViewById(R.id.btn_workout);
        btn_body = (LinearLayout) view.findViewById(R.id.btn_body);
        btn_mind = (LinearLayout) view.findViewById(R.id.btn_workout);

        btn_relax = (LinearLayout) view.findViewById(R.id.btn_relax);
        btn_img_relax = (ImageButton) view.findViewById(R.id.btn_img_relax);
        btn_txt_relax = (TextView) view.findViewById(R.id.btn_txt_relax);

        btn_eat = (LinearLayout) view.findViewById(R.id.btn_eat);
        btn_img_eat = (ImageButton) view.findViewById(R.id.btn_img_eat);
        btn_txt_eat = (TextView) view.findViewById(R.id.btn_txt_eat);

        btn_mind = (LinearLayout) view.findViewById(R.id.btn_mind);

        btn_img_workout = (ImageButton) view.findViewById(R.id.btn_img_workout);

        // btn_img_health=(ImageButton)view.findViewById(R.id.btn_img_health);
        btn_img_body = (ImageButton) view.findViewById(R.id.btn_img_body);
        //  btn_img_challenge=(ImageButton)view.findViewById(R.id.btn_img_challenge);
        btn_img_mind = (ImageButton) view.findViewById(R.id.btn_img_mind);

        btn_txt_workout = (TextView) view.findViewById(R.id.btn_txt_workout);
        //  btn_txt_health=(TextView)view.findViewById(R.id.btn_txt_health);
        btn_txt_body = (TextView) view.findViewById(R.id.btn_txt_body);

        btn_txt_mind = (TextView) view.findViewById(R.id.btn_txt_mind);


        btn_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), WorkoutActivity.class);
                startActivity(in);

            }
        });
        btn_img_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), WorkoutActivity.class);
                //  Service_getAchievementBadges_ServiceCall();

                startActivity(in);
            }
        });
        btn_txt_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), WorkoutActivity.class);
                startActivity(in);
            }
        });
//====================

        btn_eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
//                intent.putExtra("URL", ApiClient.BASE_URL_entypoint+"mobile_food_view");
//                startActivity(intent);
                Intent intent = new Intent(getContext(), EatViewActivity.class);
                intent.putExtra("URL", ApiClient.BASE_URL_entypoint + "mobile_food_view");
                startActivity(intent);
            }
        });
        btn_img_eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), EatViewActivity.class);
                intent.putExtra("URL", ApiClient.BASE_URL_entypoint + "mobile_food_view");
                startActivity(intent);


            }
        });

        btn_txt_eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EatViewActivity.class);
                intent.putExtra("URL", ApiClient.BASE_URL_entypoint + "mobile_food_view");
                startActivity(intent);
//                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
//                intent.putExtra("URL", ApiClient.BASE_URL_entypoint+"mobile_food_view");
//                startActivity(intent);
            }
        });
//=======================
        btn_relax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
//                intent.putExtra("URL", ApiClient.BASE_URL_entypoint+"mobile_mind_view");
//                startActivity(intent);
                Intent intent = new Intent(getContext(), RelaxViewActivity.class);
                intent.putExtra("URL", ApiClient.BASE_URL_entypoint + "mobile_mind_view");
                startActivity(intent);
//                Intent intent = new Intent(getContext(), AudioPlayerActivity.class);

//                startActivity(intent);
            }
        });
        btn_img_relax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext(), RelaxViewActivity.class);
                intent.putExtra("URL", ApiClient.BASE_URL_entypoint + "mobile_mind_view");
                startActivity(intent);

            }
        });
        btn_txt_relax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RelaxViewActivity.class);
                intent.putExtra("URL", ApiClient.BASE_URL_entypoint + "mobile_mind_view");
                startActivity(intent);

            }
        });


        inflatert = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) view.findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(getContext());
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);


        webView = (WebView) view.findViewById(R.id.webView_timeline);
        //=========================================================
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // ========================================================
        System.out.print("=======hasToken=============" + hasToken);

        url = ApiClient.BASE_URL_entypoint + "ayuboLifeTimelineLogin&ref=";

        try {

            if (Utility.isInternetAvailable(getContext())) {


                WebSettings webSettings = webView.getSettings();
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);

                if (Build.VERSION.SDK_INT >= 21) {
                    webSettings.setMixedContentMode(0);
                    webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                } else if (Build.VERSION.SDK_INT >= 19) {
                    webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                } else if (Build.VERSION.SDK_INT < 19) {
                    webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }

                webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webView.setWebViewClient(new Callback());
                webView.addJavascriptInterface(new WebAppInterface(getActivity()), "Android");
                System.out.print("====================");
                System.out.print("========final URL Timeline=========" + url + encodedHashToken);
                webView.getSettings().setLoadsImagesAutomatically(true);
                //  System.out.print(url+encodedHashToken);
                System.out.print("====================");
                webView.requestFocus(View.FOCUS_DOWN);
                webView.loadUrl(url + encodedHashToken);


                // url="https://www.w3schools.com/";
                //    webView.loadUrl(url);
                timeout = true;
                webView.setDownloadListener(new DownloadListener() {
                    public void onDownloadStart(String url, String userAgent,
                                                String contentDisposition, String mimetype,
                                                long contentLength) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
                webView.setWebChromeClient(new WebChromeClient() {


                    //For Android 3.0+
                    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                        mUM = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
                    }

                    // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
                    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                        mUM = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        startActivityForResult(
                                Intent.createChooser(i, "File Browser"),
                                FCR);
                    }

                    //For Android 4.1+
                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                        mUM = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
                    }

                    //For Android 5.0+
                    public boolean onShowFileChooser(
                            WebView webView, ValueCallback<Uri[]> filePathCallback,
                            WebChromeClient.FileChooserParams fileChooserParams) {

                        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                                .checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                    PERMISSIONS_MULTIPLE_REQUEST);

                        }
//                        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED )) {
//                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 2);
//                        }
                        // write your logic here

                        if (mUMA != null) {
                            mUMA.onReceiveValue(null);
                        }
                        mUMA = filePathCallback;
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                                takePictureIntent.putExtra("PhotoPath", mCM);
                            } catch (IOException ex) {
                                Log.e(TAG, "Image file creation failed", ex);
                            }
                            if (photoFile != null) {

                                mCM = "file:" + photoFile.getAbsolutePath();
                                System.out.println("==========UPLOAD==========");
                                System.out.println("====================");
                                System.out.println("=========pATH===========" + photoFile.getAbsolutePath());
                                System.out.println("====================");
                                System.out.println("===========UPLOAD=========");
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            } else {
                                takePictureIntent = null;
                            }
                        }
                        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        contentSelectionIntent.setType("*/*");
                        Intent[] intentArray;
                        if (takePictureIntent != null) {
                            intentArray = new Intent[]{takePictureIntent};
                        } else {
                            intentArray = new Intent[0];
                        }

                        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                        startActivityForResult(chooserIntent, FCR);
                        return true;
                    }

                });

                mySwipeRefreshLayout.setOnRefreshListener(
                        new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {

                                Intent intent = new Intent(getContext(), NewHomeWithSideMenuActivity.class);
                                startActivity(intent);


                            }
                        }
                );

            } else {
                // showAlertDialog("No internet connection");
//                textt.setText("Unable to detect an active internet connection");
//                toastt.setView(layoutt);
//                toastt.show();

            }
        } catch (Exception e) {
            System.out.println("Timeline Webview....................." + e);
            FirebaseCrash.log("Timeline Webview....................." + e);
            FirebaseCrash.report(e);
        }
        //===============================================================
        //
// Get the widgets reference from XML layout
        mRelativeLayout = (LinearLayout) view.findViewById(R.id.btn_mind);
        mButton = (ImageButton) view.findViewById(R.id.btn_img_love);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MenuObj> menuIconList;
                menuIconList = new ArrayList<MenuObj>();
                int noofExtraMenus = 0;
                isPopupClicked = true;
                ArrayList<MenuObj> datesListArray2 = Ram.getMenuIconList();
                if (datesListArray2 == null) {
                    noofExtraMenus = 0;
                } else {
                    noofExtraMenus = datesListArray2.size();
                }


                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = null;
                System.out.println("=======ttt=========" + noofExtraMenus);
                if (noofExtraMenus == 0) {
                    customView = inflater.inflate(R.layout.main_menu_popup_layout, null);
                } else if (noofExtraMenus <= 4) {
                    customView = inflater.inflate(R.layout.main_menu_popup_layout_level_two, null);


                    if (noofExtraMenus == 1) {

                        MenuObj menuOb1 = datesListArray2.get(0);
                        final String menuLink1 = menuOb1.getLink();
                        String imgPath = menuOb1.getImage();
                        String imgTile = menuOb1.getLable();

                        TextView btn_txt_level_1_M1 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M1);
                        ImageView btn_img_level_1_M1 = (ImageView) customView.findViewById(R.id.im_level_1_M1);
                        btn_txt_level_1_M1.setText(imgTile);
                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M1);
                        btn_img_level_1_M1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                System.out.println("===================================" + menuLink);
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink1);
                                startActivity(intent);
                            }
                        });
                    }
                    //===============================
                    if (noofExtraMenus == 2) {

                        MenuObj menuOb1 = datesListArray2.get(0);
                        final String menuLink1 = menuOb1.getLink();
                        String imgPath = menuOb1.getImage();
                        String imgTile = menuOb1.getLable();

                        TextView btn_txt_level_1_M1 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M1);
                        ImageView btn_img_level_1_M1 = (ImageView) customView.findViewById(R.id.im_level_1_M1);
                        btn_txt_level_1_M1.setText(imgTile);
                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M1);
                        btn_img_level_1_M1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                System.out.println("===================================" + menuLink);
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink1);
                                startActivity(intent);
                            }
                        });
                        //=============================
                        MenuObj menuOb2 = datesListArray2.get(1);
                        final String menuLink2 = menuOb2.getLink();
                        String imgPath2 = menuOb2.getImage();

                        String imgTile2 = menuOb2.getLable();
                        TextView btn_txt_level_1_M2 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M2);
                        btn_txt_level_1_M2.setText(imgTile2);

                        ImageView btn_img_level_1_M2 = (ImageView) customView.findViewById(R.id.im_level_1_M2);
                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M2);


                        btn_img_level_1_M2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink2);
                                startActivity(intent);
                            }
                        });

                    }
                    //===============3====================
                    if (noofExtraMenus == 3) {

                        MenuObj menuOb1 = datesListArray2.get(0);
                        final String menuLink1 = menuOb1.getLink();
                        String imgPath = menuOb1.getImage();
                        String imgTile = menuOb1.getLable();

                        TextView btn_txt_level_1_M1 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M1);
                        ImageView btn_img_level_1_M1 = (ImageView) customView.findViewById(R.id.im_level_1_M1);
                        btn_txt_level_1_M1.setText(imgTile);

                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M1);
                        btn_img_level_1_M1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                System.out.println("===================================" + menuLink);
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink1);
                                startActivity(intent);
                            }
                        });
                        //=============================
                        MenuObj menuOb2 = datesListArray2.get(1);
                        final String menuLink2 = menuOb2.getLink();
                        String imgPath2 = menuOb2.getImage();

                        String imgTile2 = menuOb2.getLable();
                        TextView btn_txt_level_1_M2 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M2);
                        btn_txt_level_1_M2.setText(imgTile2);

                        ImageView btn_img_level_1_M2 = (ImageView) customView.findViewById(R.id.im_level_1_M2);
                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath2)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M2);


                        btn_img_level_1_M2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink2);
                                startActivity(intent);
                            }
                        });

                        //=============================
                        MenuObj menuOb3 = datesListArray2.get(2);
                        final String menuLink3 = menuOb3.getLink();
                        String imgPath3 = menuOb3.getImage();

                        String imgTile3 = menuOb3.getLable();
                        TextView btn_txt_level_1_M3 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M3);
                        btn_txt_level_1_M3.setText(imgTile3);

                        ImageView btn_img_level_1_M3 = (ImageView) customView.findViewById(R.id.im_level_1_M3);
                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath3)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M3);


                        btn_img_level_1_M3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink3);
                                startActivity(intent);
                            }
                        });
                    }
                    //===================4================
                    if (noofExtraMenus == 4) {
                        MenuObj menuOb1 = datesListArray2.get(0);
                        final String menuLink1 = menuOb1.getLink();
                        String imgPath = menuOb1.getImage();
                        String imgTile = menuOb1.getLable();

                        TextView btn_txt_level_1_M1 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M1);
                        ImageView btn_img_level_1_M1 = (ImageView) customView.findViewById(R.id.im_level_1_M1);
                        btn_txt_level_1_M1.setText(imgTile);
                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M1);
                        btn_img_level_1_M1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                System.out.println("===================================" + menuLink);
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink1);
                                startActivity(intent);
                            }
                        });
                        //=============================
                        MenuObj menuOb2 = datesListArray2.get(1);
                        final String menuLink2 = menuOb2.getLink();
                        String imgPath2 = menuOb2.getImage();

                        String imgTile2 = menuOb2.getLable();
                        TextView btn_txt_level_1_M2 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M2);
                        btn_txt_level_1_M2.setText(imgTile2);

                        ImageView btn_img_level_1_M2 = (ImageView) customView.findViewById(R.id.im_level_1_M2);
                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath2)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M2);


                        btn_img_level_1_M2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink2);
                                startActivity(intent);
                            }
                        });

                        //=============================
                        MenuObj menuOb3 = datesListArray2.get(2);
                        final String menuLink3 = menuOb3.getLink();
                        String imgPath3 = menuOb3.getImage();

                        String imgTile3 = menuOb3.getLable();
                        TextView btn_txt_level_1_M3 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M3);
                        btn_txt_level_1_M3.setText(imgTile3);

                        ImageView btn_img_level_1_M3 = (ImageView) customView.findViewById(R.id.im_level_1_M3);
                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath3)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M3);


                        btn_img_level_1_M3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink3);
                                startActivity(intent);
                            }
                        });
                        //=============================
                        MenuObj menuOb4 = datesListArray2.get(3);
                        final String menuLink4 = menuOb4.getLink();
                        String imgPath4 = menuOb4.getImage();

                        String imgTile4 = menuOb4.getLable();
                        TextView btn_txt_level_1_M4 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M4);
                        btn_txt_level_1_M4.setText(imgTile4);

                        ImageView btn_img_level_1_M4 = (ImageView) customView.findViewById(R.id.im_level_1_M4);

                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath3)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M4);

                        btn_img_level_1_M4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink4);
                                startActivity(intent);
                            }
                        });
                    }

                    //===================FIRST LEVEL FINISHED===============================================
                    //===================FIRST LEVEL FINISHED===============================================
                    //===================FIRST LEVEL FINISHED===============================================
                    //===================FIRST LEVEL FINISHED===============================================
                    //===================FIRST LEVEL FINISHED===============================================

                } else if (noofExtraMenus <= 8) {
                    customView = inflater.inflate(R.layout.main_menu_popup_layout_level_three, null);

                    //===================4================
                    if (noofExtraMenus == 5) {
                        MenuObj menuOb1 = datesListArray2.get(0);
                        final String menuLink1 = menuOb1.getLink();
                        String imgPath = menuOb1.getImage();
                        String imgTile = menuOb1.getLable();

                        TextView btn_txt_level_1_M1 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M1);
                        ImageView btn_img_level_1_M1 = (ImageView) customView.findViewById(R.id.im_level_1_M1);
                        btn_txt_level_1_M1.setText(imgTile);
                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M1);
                        btn_img_level_1_M1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                System.out.println("===================================" + menuLink);
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink1);
                                startActivity(intent);
                            }
                        });
                        //=============================
                        MenuObj menuOb2 = datesListArray2.get(1);
                        final String menuLink2 = menuOb2.getLink();
                        String imgPath2 = menuOb2.getImage();

                        String imgTile2 = menuOb2.getLable();
                        TextView btn_txt_level_1_M2 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M2);
                        btn_txt_level_1_M2.setText(imgTile2);

                        ImageView btn_img_level_1_M2 = (ImageView) customView.findViewById(R.id.im_level_1_M2);
                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath2)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M2);


                        btn_img_level_1_M2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink2);
                                startActivity(intent);
                            }
                        });

                        //=============================
                        MenuObj menuOb3 = datesListArray2.get(2);
                        final String menuLink3 = menuOb3.getLink();
                        String imgPath3 = menuOb3.getImage();

                        String imgTile3 = menuOb3.getLable();
                        TextView btn_txt_level_1_M3 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M3);
                        btn_txt_level_1_M3.setText(imgTile3);

                        ImageView btn_img_level_1_M3 = (ImageView) customView.findViewById(R.id.im_level_1_M3);
                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath3)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M3);


                        btn_img_level_1_M3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink3);
                                startActivity(intent);
                            }
                        });
                        //=============================
                        MenuObj menuOb4 = datesListArray2.get(3);
                        final String menuLink4 = menuOb4.getLink();
                        String imgPath4 = menuOb4.getImage();

                        String imgTile4 = menuOb4.getLable();
                        TextView btn_txt_level_1_M4 = (TextView) customView.findViewById(R.id.btn_txt_level_1_M4);
                        btn_txt_level_1_M4.setText(imgTile4);

                        ImageView btn_img_level_1_M4 = (ImageView) customView.findViewById(R.id.im_level_1_M4);

                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath4)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_1_M4);

                        btn_img_level_1_M4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink4);
                                startActivity(intent);
                            }
                        });

                        //==============5===============
                        MenuObj menuOb5 = datesListArray2.get(4);
                        final String menuLink5 = menuOb5.getLink();
                        String imgPath5 = menuOb5.getImage();

                        String imgTile5 = menuOb5.getLable();
                        TextView btn_txt_level_2_M1 = (TextView) customView.findViewById(R.id.btn_txt_level_2_M1);
                        btn_txt_level_2_M1.setText(imgTile5);

                        ImageView btn_img_level_2_M1 = (ImageView) customView.findViewById(R.id.im_level_2_M1);
                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath5)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_2_M1);

                        btn_img_level_2_M1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink5);
                                startActivity(intent);
                            }
                        });
                        //==============6===============
                        MenuObj menuOb6 = datesListArray2.get(5);
                        final String menuLink6 = menuOb6.getLink();
                        String imgPath6 = menuOb6.getImage();

                        String imgTile6 = menuOb6.getLable();
                        TextView btn_txt_level_2_M2 = (TextView) customView.findViewById(R.id.btn_txt_level_2_M2);
                        btn_txt_level_2_M2.setText(imgTile6);

                        ImageView btn_img_level_2_M2 = (ImageView) customView.findViewById(R.id.im_level_2_M2);
                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath6)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_2_M2);

                        btn_img_level_2_M2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink6);
                                startActivity(intent);
                            }
                        });
                        //====================
                        //==============7===============
                        MenuObj menuOb7 = datesListArray2.get(6);
                        final String menuLink7 = menuOb7.getLink();
                        String imgPath7 = menuOb7.getImage();

                        String imgTile7 = menuOb7.getLable();
                        TextView btn_txt_level_2_M3 = (TextView) customView.findViewById(R.id.btn_txt_level_2_M3);
                        btn_txt_level_2_M3.setText(imgTile7);

                        ImageView btn_img_level_2_M3 = (ImageView) customView.findViewById(R.id.im_level_2_M3);
                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath7)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_2_M3);

                        btn_img_level_2_M3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink7);
                                startActivity(intent);
                            }
                        });
                        //====================
                        //==============8===============
                        MenuObj menuOb8 = datesListArray2.get(7);
                        final String menuLink8 = menuOb8.getLink();
                        String imgPath8 = menuOb8.getImage();

                        String imgTile8 = menuOb8.getLable();
                        TextView btn_txt_level_2_M4 = (TextView) customView.findViewById(R.id.btn_txt_level_2_M4);
                        btn_txt_level_2_M4.setText(imgTile8);

                        ImageView btn_img_level_2_M4 = (ImageView) customView.findViewById(R.id.im_level_2_M4);

                        requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(getContext()));
                        Glide.with(getContext()).load(imgPath8)
                                .transition(withCrossFade())
                                .thumbnail(0.5f)
                                .apply(requestOptions)
                                .into(btn_img_level_2_M4);

                        btn_img_level_2_M4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                                intent.putExtra("URL", menuLink8);
                                startActivity(intent);
                            }
                        });
                        //====================
                    }


                }


                mPopupWindow = new PopupWindow(
                        customView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                mPopupWindow.setOutsideTouchable(true);

                if (Build.VERSION.SDK_INT >= 21) {
                    mPopupWindow.setElevation(25.0f);
                }
                ImageButton btn_img_my_doctor = (ImageButton) customView.findViewById(R.id.btn_img_my_doctor);
                btn_img_my_doctor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                        Intent intent = new Intent(getContext(), MyDoctor_Activity.class);
                        Ram.setMapSreenshot(null);
                        intent.putExtra("activityName", "my_doctor");
                        startActivity(intent);
                    }
                });

                ImageButton btn_img_mywellnessexpert = (ImageButton) customView.findViewById(R.id.btn_img_mywellnessexpert);
                btn_img_mywellnessexpert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                        Intent intent = new Intent(getContext(), MyDoctor_Activity.class);
                        Ram.setMapSreenshot(null);
                        intent.putExtra("activityName", "myExperts");
                        startActivity(intent);
                    }
                });
                ImageButton btn_img_myfamilty = (ImageButton) customView.findViewById(R.id.btn_img_myfamilty);
                btn_img_myfamilty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();

                        Intent intent = new Intent(getContext(), HealthViewActivityNew.class);
                        startActivity(intent);

                    }
                });

                ImageButton btn_img_myreports = (ImageButton) customView.findViewById(R.id.btn_img_myreports);
                btn_img_myreports.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();

                        Intent in = new Intent(getContext(), Reports_MainActivity.class);
                        //  in.putExtra("type","fromHome" );
                        Ram.setReportsType("fromHome");
                        startActivity(in);

                        // Intent intent = new Intent(getContext(), Reports_Activity.class);
                        // startActivity(intent);
                    }
                });
                ImageButton btn_img_qrcode = (ImageButton) customView.findViewById(R.id.btn_img_qrcode);
                btn_img_qrcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                        Intent intent = new Intent(getContext(), DecoderActivity.class);
                        startActivity(intent);
                    }
                });

                ImageButton btn_img_ordermedicine = (ImageButton) customView.findViewById(R.id.btn_img_ordermedicine);
                btn_img_ordermedicine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                        Intent intent = new Intent(getContext(), Medicine_ViewActivity.class);
                        startActivity(intent);
                    }
                });
                ImageButton btn_img_askquestion = (ImageButton) customView.findViewById(R.id.btn_img_askquestion);
                btn_img_askquestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                        Intent intent = new Intent(getContext(), AskQuestion_Activity.class);
                        startActivity(intent);


                    }
                });
                ImageButton btn_apps_store = (ImageButton) customView.findViewById(R.id.btn_apps_store);
                btn_apps_store.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();

                        openAppStore();

                    }
                });


                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.BOTTOM, 0, 140);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dimBehind(mPopupWindow);
                }

            }
        });

        return view;

    }

    private void Service_getAchievementBadges_ServiceCall() {

        if (Utility.isInternetAvailable(getContext())) {

            progressTimeline.show();

            Service_getBadgesData task = new Service_getBadgesData();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
        }
    }

    private class Service_getBadgesData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            getBadgesData();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //
            if (progressTimeline != null) {
                progressTimeline.dismiss();
            }

            if (serviceDataStatus != null) {
                if (serviceDataStatus.equals("0")) {
                    Intent in = new Intent(getContext(), Badges_Activity.class);
                    in.putExtra("badgesJsonData", badgesJsonData);
                    startActivity(in);
                } else {

                }
            }

            //    Intent in = new Intent(getContext(), WorkoutActivity.class);
            // Intent in = new Intent(getContext(), ChallengeCompletedView_Activity.class);


        }
    }

    private void getBadgesData() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String jsonStr =
                "{" +

                        "\"user_id\": \"" + userid_ExistingUser + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "getAchievementBadges"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............getActivityDetails..................." + nameValuePair.toString());
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
            System.out.println("........getActivityDetails..response..........." + response);
            System.out.println("========Service Calling============0");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                System.out.println("========Service Calling============1");
                if (!(builder == null && builder.toString().equals(""))) {
                    System.out.println("========Service Calling============2");
                    for (String line = null; (line = reader.readLine()) != null; ) {
                        System.out.println("========Service Calling============3");
                        builder.append(line).append("\n");
                        System.out.println("..............=........................." + line);
                        JSONObject jsonObj = new JSONObject(line);

                        String data = jsonObj.optString("data").toString();
                        System.out.println("========Service Calling============" + data);
                        achievementObj = new ArrayList<AchievementObj>();
                        if (data.length() > 5) {
                            serviceDataStatus = "0";
                            badgesJsonData = data;


                        } else {

                            serviceDataStatus = "99";
                        }

                    }
                } else {

                }
            } catch (JSONException e) {

                e.printStackTrace();
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            System.out.println("========Service Calling============7");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("========Service Calling============8");
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utility.isInternetAvailable(getContext())) {
        } else {
            getForgotPassword().show();

        }
    }


    private void openAppStore() {
        Service_getMarketPlace_ServiceCall();
    }

    private void Service_getMarketPlace_ServiceCall() {
        if (Utility.isInternetAvailable(getContext())) {
            try {
                Service_getMarketPlace task = new Service_getMarketPlace();
                task.execute(new String[]{ApiClient.BASE_URL_live});
            } catch (Exception e) {
                FirebaseCrash.log("..................................");
                FirebaseCrash.report(e);
            }
        } else {
            textt.setText("Unable to detect an active internet connection");
            toastt.setView(layoutt);
            toastt.show();
        }
    }


    public class Callback extends WebViewClient {


        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val = false;

            System.out.println("===========TimeLine===Callback Redireted URL====================" + url);

            String appsUrl = Constants.type == Constants.Type.HEMAS ? "apps.hemashealth.com" : "apps.ayubo.life";

            if (url.contains("index.php?entryPoint=ayuboLifeTimeline")) {
                val = false;
                return val;
            } else if (url.contains(appsUrl)) {

                if (url.contains("panaroma")) {
                    val = true;
                    if (gyroExists) {
                        Intent intent = new Intent(getContext(), PanaramaView_NewActivity.class);
                        intent.putExtra("URL", url);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(getContext(), PanoramaView_LawDevices_Activity.class);
                        intent.putExtra("URL", url);
                        startActivity(intent);
                    }

                    return val;
                } else if (url.contains("openInView=true")) {
                    val = true;
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    return val;
                } else if (url.contains("group_adventure_invite")) {
                    val = true;
                    Intent intent = new Intent(getContext(), ContactDetailsActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    return val;
                } else if (url.contains("openInBrowser=true")) {
                    val = true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                } else {
                    val = false;
                    return val;
                }
            } else if (url.contains("gotonativechallengeactivity")) {

                Intent intent = new Intent(getContext(), NewCHallengeActivity.class);
                startActivity(intent);
                val = true;
                return val;
            } else if (url.contains("openbadgenative")) {
                val = true;
                Intent intent = new Intent(getContext(), Badges_Activity.class);
                startActivity(intent);
                return val;
            } else if (url.contains("group_adventure_invite")) {
                val = true;
                Intent intent = new Intent(getContext(), ContactDetailsActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
                return val;
            }
            // startActivity(new Intent(NewHomeWithSideMenuActivity.this, Badges_Activity.class));
            else if (url.contains("soloAdventureMap")) {

                String[] parts = url.split("_");
                challenge_id = parts[1];
                if (parts.length == 3) {
                    showpopup = parts[2];
                }
                val = true;
                Service_getChallengeMapData_ServiceCall();

                return val;
            } else if (url.contains("soloAdventureCompleted")) {
                val = true;
                Intent intent = new Intent(getContext(), ChallengeCompletedView_Activity.class);
                startActivity(intent);
                return val;
            } else if (url.contains("panaroma")) {
                val = true;
                if (gyroExists) {
                    Intent intent = new Intent(getContext(), PanaramaView_NewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(getContext(), PanoramaView_LawDevices_Activity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                return val;
            } else if (url.contains("soloAdventureJoin")) {
                val = true;

                String[] parts = url.split("_");
                challenge_id = parts[1];
                //MapChallengeActivity.Book treaturesList = new MapChallengeActivity.Book(myTreasure);

                Intent intent = new Intent(getContext(), MapJoinChallenge_Activity.class);
                intent.putExtra("challenge_id", challenge_id);
                intent.putExtra("Treatures", "");
                intent.putExtra("steps", "0");
                startActivity(intent);
                return val;
            } else if (url.contains("subscriptionView")) {
                val = true;
                Intent in = new Intent(getContext(), RXViewActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains("medicineView")) {
                val = true;
                Intent in = new Intent(getContext(), Medicine_ViewActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains("livehappy.ayubo.life")) {
                if (url.contains("openInView=true")) {
                    val = true;
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    return val;
                } else if (url.contains("group_adventure_invite")) {
                    val = true;
                    Intent intent = new Intent(getContext(), ContactDetailsActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    return val;
                } else if (url.contains("openInBrowser=true")) {
                    val = true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                } else if (url.contains("timelinepost")) {
                    val = true;
                    Intent in = new Intent(getContext(), PostDataActivity.class);
                    in.putExtra("cId", "aa");
                    in.putExtra("cName", "aa");
                    in.putExtra("postType", "aa");
                    startActivity(in);
                    return val;
                } else if (url.contains("openInSame=true")) {
                    val = false;
                    return val;
                } else if (url.contains("index.php?entryPoint=mobile_mind_view")) {
                    val = true;
                    //  Intent in = new Intent(getContext(), MindViewActivity.class);
                    Intent in = new Intent(getContext(), Mind_MainActivity.class);
                    startActivity(in);
                    return val;
                } else if (url.contains("index.php?entryPoint=mobile_health_view")) {
                    val = true;
                    Intent in = new Intent(getContext(), Health_MainActivity.class);
                    startActivity(in);
                    return val;
                } else if (url.contains("subscriptionView")) {
                    val = true;
                    Intent in = new Intent(getContext(), RXViewActivity.class);
                    startActivity(in);
                    return val;
                } else if (url.contains("medicineView")) {
                    val = true;
                    Intent in = new Intent(getContext(), Medicine_ViewActivity.class);
                    startActivity(in);
                    return val;
                } else if (url.contains("mobileMedicalHistory")) {
                    val = true;
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    return val;

                } else if (url.contains("index.php?entryPoint=ayuboLifeTimeline")) {
                    val = false;
                    return val;
                } else if (url.contains("initChatMobile")) {
                    val = true;
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                } else if (url.contains("water_intake_window_call")) {
                    val = true;
                    Intent in = new Intent(getContext(), WaterActivity.class);
                    startActivity(in);
                    return val;
                } else if (url.contains(ApiClient.BASE_URL)) {
                    val = true;
                    System.out.println("=========iN======CommonWebViewActivity=========" + url);
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                } else {
                    val = true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                }

            } else if (url.contains("devo.ayubo.life")) {
                if (url.contains("openInBrowser=true")) {
                    val = true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                } else if (url.contains("timelinepost")) {
                    val = true;
                    Intent intent = new Intent(getContext(), PostDataActivity.class);
                    intent.putExtra("cId", "aa");
                    intent.putExtra("cName", "aa");
                    intent.putExtra("postType", "aa");
                    startActivity(intent);
                    return val;
                } else if (url.contains("group_adventure_invite")) {
                    val = true;
                    Intent intent = new Intent(getContext(), ContactDetailsActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    return val;
                } else if (url.contains("openInSame=true")) {
                    val = false;
                    return val;
                } else if (url.contains("index.php?entryPoint=mobile_mind_view")) {
                    val = true;
                    Intent in = new Intent(getContext(), Mind_MainActivity.class);
                    startActivity(in);
                    return val;
                } else if (url.contains("index.php?entryPoint=mobile_health_view")) {
                    val = true;
                    Intent in = new Intent(getContext(), Health_MainActivity.class);
                    startActivity(in);
                    return val;
                } else if (url.contains("subscriptionView")) {
                    val = true;
                    Intent in = new Intent(getContext(), RXViewActivity.class);
                    startActivity(in);
                    return val;
                } else if (url.contains("medicineView")) {
                    val = true;
                    Intent in = new Intent(getContext(), Medicine_ViewActivity.class);
                    startActivity(in);
                    return val;
                } else if (url.contains("index.php?entryPoint=mobileMedicalHistory")) {

                    if (url.contains("openInView=true")) {
                        val = true;
                        Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                        intent.putExtra("URL", url);
                        startActivity(intent);

                        return val;
                    } else {
                        val = false;
                        return val;
                    }
                } else if (url.contains("index.php?entryPoint=ayuboLifeTimeline")) {
                    val = false;
                    return val;
                } else if (url.contains("initChatMobile")) {
                    val = true;
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                } else if (url.contains("water_intake_window_call")) {
                    val = true;
                    Intent in = new Intent(getContext(), WaterActivity.class);
                    startActivity(in);
                    return val;
                } else if (url.contains(ApiClient.BASE_URL)) {
                    val = true;
                    System.out.println("=========iN======CommonWebViewActivity=========" + url);
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                } else {
                    val = true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                }
            } else if (url.contains("disable_back")) {
                val = true;
                return val;
            } else if (url.contains("subscriptionView")) {
                val = false;
                return val;
            } else if (url.contains("index.php?entryPoint=mobileMedicalHistory")) {

                if (url.contains("openInView=true")) {
                    val = true;
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                } else {
                    val = false;
                    return val;
                }
            } else if (url.contains("index.php?entryPoint=ayuboLifeTimeline")) {
                val = false;
                return val;
            } else if (url.contains("initChatMobile")) {
                val = true;
                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);

                return val;
            } else if (url.contains(MAIN_URL_APPS)) {
                val = true;
                System.out.println("==============Redireted URL====================" + url);
                //  url="https://www.w3schools.com/";
                webView.loadUrl(url);
                return val;
            } else if (url.contains("water_intake_window_call")) {
                val = true;
                Intent in = new Intent(getContext(), WaterActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains(ApiClient.BASE_URL)) {
                val = true;
                System.out.println("=========iN======CommonWebViewActivity=========" + url);
                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);

                return val;
            } else {
                val = true;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return val;
            }

        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            String htmlFilename = "error.html";
            AssetManager mgr = getActivity().getBaseContext().getAssets();
            try {
                InputStream in = mgr.open(htmlFilename, AssetManager.ACCESS_BUFFER);
                String htmlContentInStringFormat = Utility.StreamToString(in);
                in.close();
                webView.loadDataWithBaseURL(null, htmlContentInStringFormat, "text/html", "utf-8", null);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            System.out.println("=========onPageStarted================");
            progressTimeline.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if ((progressTimeline != null) && (progressTimeline.isShowing())) {
                System.out.println("=========dismiss================");
                progressTimeline.dismiss();
            }
        }
    }
    //=================

    private void Service_getChallengeMapData_ServiceCall() {

        if (Utility.isInternetAvailable(getContext())) {

            progressTimeline.show();

            Service_getStepsData task = new Service_getStepsData();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
        }
    }

    private class Service_getStepsData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            System.out.println("========Service Calling============0");
            makeSetDefaultDevice();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("========Service Calling============6");
            if (progressTimeline != null) {
                progressTimeline.dismiss();
            }

            String cityFile = loadJSONFromAssetNew(com.ayubo.life.ayubolife.webrtc.App.getInstance());
            if (cityFile == null) {

                Service_getChallengeJson_ServiceCall();
            } else {

                MapChallengeActivity.Book treaturesList = new MapChallengeActivity.Book(myTreasure);

                Intent intent = new Intent(getContext(), MapChallengeActivity.class);
                intent.putExtra("challenge_id", challenge_id);
                intent.putExtra("noof_day", noof_day);
                intent.putExtra("cards", cards);
                intent.putExtra("showpopup", showpopup);
                intent.putExtra("service_checkpoints", service_checkpoints);
                intent.putExtra("enabled_checkpoints", enabled_checkpoints);
                intent.putExtra("Treatures", treaturesList);
                intent.putExtra("steps", Integer.toString(total_steps));
                intent.putExtra("weekSteps", weekSteps);
                System.out.println("=========from result Service_getStepsData=====================");
                startActivity(intent);
            }
        }
    }

    private void makeSetDefaultDevice() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String jsonStr =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"," +
                        "\"challenge_id\": \"" + challenge_id + "\"" +
                        "}";


        nameValuePair.add(new BasicNameValuePair("method", "getChallengeSteps"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............getActivityDetails..................." + nameValuePair.toString());
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
            System.out.println("........getActivityDetails..response..........." + response);
            System.out.println("========Service Calling============0");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                System.out.println("========Service Calling============1");
                if (!(builder == null && builder.toString().equals(""))) {
                    System.out.println("========Service Calling============2");
                    for (String line = null; (line = reader.readLine()) != null; ) {
                        System.out.println("========Service Calling============3");
                        builder.append(line).append("\n");
                        System.out.println("..............=........................." + line);
                        JSONObject jsonObj = new JSONObject(line);

                        String res = jsonObj.optString("result").toString();

                        int result = Integer.parseInt(res);

                        if (result == 0) {
                            System.out.println("========Service Calling============4");
                            try {

                                serviceDataStatus = "0";
                                if (myTreasure != null) {
                                    myTreasure.clear();
                                }


                                String data = jsonObj.optString("data").toString();
                                JSONObject jsonData = new JSONObject(data);
                                //Map Data====================================
                                service_checkpoints = jsonData.optString("service_checkpoints").toString();
                                if (service_checkpoints.equals("true")) {
                                    enabled_checkpoints = jsonData.optString("enabled_checkpoints").toString();
                                } else {
                                    service_checkpoints = "";
                                    enabled_checkpoints = "";
                                }
                                weekSteps = jsonData.optString("weekSteps").toString();
                                String counter = jsonData.optString("counter").toString();
                                noof_day = jsonData.optString("day").toString();
                                String treatures = jsonData.optString("treatures").toString();
                                cards = jsonData.optString("cards").toString();
                                total_steps = Integer.parseInt(counter);

                                JSONArray myDataListsAll = null;
                                try {
                                    myDataListsAll = new JSONArray(treatures);
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

                                    String latp = childJson.optString("latp").toString();
                                    String longp = childJson.optString("longp").toString();
                                    String steps = childJson.optString("steps").toString();
                                    String objimg = childJson.optString("objimg").toString();
                                    String objlink = childJson.optString("objlink").toString();
                                    String status = childJson.optString("status").toString();
                                    //  System.out.println("========Service Calling============10");
                                    double roadPath_lat = Double.parseDouble(latp);
                                    double roadPath_longitude = Double.parseDouble(longp);

//                                    myTreasure.add(new RoadLocationObj(roadPath_lat,roadPath_longitude,steps,objimg,objlink,status,"","","",
//                                            "","","","","",""));

                                }


                            } catch (Exception e) {
                                serviceDataStatus = "99";
                                e.printStackTrace();
                            }


                        } else {
                            System.out.println("========Service Calling============10");
                            serviceDataStatus = "99";
                        }

                    }
                } else {

                }
            } catch (JSONException e) {
                System.out.println("========Service Calling============9");
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            System.out.println("========Service Calling============7");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("========Service Calling============8");
            e.printStackTrace();
        }

    }

    public String loadJSONFromAssetNew(Context context) {
        String json = null;
        try {
            InputStream is = context.openFileInput(challenge_id + ".json");
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

    //=====GET DATA FOR JSON=============================
    private void Service_getChallengeJson_ServiceCall() {

        if (Utility.isInternetAvailable(getContext())) {

            progressTimeline.show();

            Service_getChallengeJson task = new Service_getChallengeJson();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
        }
    }

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

    //===========================================


    private class Service_getChallengeJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            System.out.println("========Service Calling============0");
            getChallengeJson();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {


            if (progressTimeline != null) {
                progressTimeline.dismiss();
            }

            writeToFile(cityJsonString);
            MapChallengeActivity.Book treaturesList = new MapChallengeActivity.Book(myTreasure);

            Intent intent = new Intent(getContext(), MapChallengeActivity.class);
//            intent.putExtra("challenge_id", challenge_id);
//            intent.putExtra("noof_day", noof_day);
//            intent.putExtra("Treatures", treaturesList);
//            intent.putExtra("cards", cards);
//            intent.putExtra("steps", Integer.toString(total_steps));


            intent.putExtra("challenge_id", challenge_id);
            intent.putExtra("noof_day", noof_day);
            intent.putExtra("cards", cards);
            intent.putExtra("showpopup", showpopup);
            intent.putExtra("service_checkpoints", service_checkpoints);
            intent.putExtra("enabled_checkpoints", enabled_checkpoints);
            intent.putExtra("Treatures", treaturesList);
            intent.putExtra("steps", Integer.toString(total_steps));
            intent.putExtra("weekSteps", weekSteps);
//From Exception Json ===============
            System.out.println("=========from result Service_getChallengeJson=====================");
            startActivity(intent);

        }
    }

    private void getChallengeJson() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String email, password;

        //  email = lusername;
        //  password = lpassword;


        String jsonStr =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"," +
                        "\"challenge_id\": \"" + challenge_id + "\"" +
                        "}";

        //   challenge_id

        nameValuePair.add(new BasicNameValuePair("method", "join_adventure_challenge"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............getActivityDetails..................." + nameValuePair.toString());
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


            String sc = String.valueOf(response.getStatusLine().getStatusCode());


            if (sc.equals("200")) {

                System.out.println("........getActivityDetails..response..........." + response);
                System.out.println("========Service Calling============0");
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    System.out.println("========Service Calling============1");
                    if (!(builder == null && builder.toString().equals(""))) {
                        System.out.println("========Service Calling============2");
                        for (String line = null; (line = reader.readLine()) != null; ) {
                            System.out.println("========Service Calling============3");
                            builder.append(line).append("\n");
                            System.out.println("..............=........................." + line);
                            JSONObject jsonObj = new JSONObject(line);

                            String res = jsonObj.optString("result").toString();

                            int result = Integer.parseInt(res);

                            if (result == 0) {
                                cityJsonString = jsonObj.optString("json").toString();

                            } else {

                                serviceDataStatus = "99";
                            }

                        }
                    } else {

                    }
                } catch (JSONException e) {
                    System.out.println("========Service Calling============9");
                    e.printStackTrace();
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

    //===========================================


    //================
    // Create an image file
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {
            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }


    //===============================
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraPermission && readExternalFile) {
                        permissionGrantedStatus = true;
                        System.out.println("=======================");
                        System.out.println("=======================");
                        System.out.println("=======================");
                        System.out.println("=======================");
                        System.out.println("=======================");
                        System.out.println("==========onRequestPermissionsResult=============");
                        System.out.println("==========permission already granted=============");
                        System.out.println("=======================");
                        System.out.println("=======================");
                        System.out.println("=======================");

                        // write your logic here
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Permission required");
                        builder.setMessage("This app needs storage and camera permission.");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }
                break;
        }
    }


    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        public void showDialog(String dialogMsg) {
            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

            // Setting Dialog Title
            alertDialog.setTitle("JS triggered Dialog");

            // Setting Dialog Message
            alertDialog.setMessage(dialogMsg);

            // Setting alert dialog icon
            //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(mContext, "Dialog dismissed!", Toast.LENGTH_SHORT).show();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }

        @JavascriptInterface
        public void moveToNextScreen(String s) {

            Intent chnIntent = new Intent(getContext(), DayActivitiesActivity.class);
            chnIntent.putExtra("URL", s);
            startActivity(chnIntent);
        }
    }

    private class Service_getMarketPlace extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressTimeline.show();


        }

        @Override
        protected String doInBackground(String... urls) {
            getMarketPlaceToken();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (progressTimeline != null) {
                progressTimeline.dismiss();
            }
            if (!(marketplace_Token_status == null)) {
                //  userIdFromServiceAPI
                if (marketplace_Token_status.equals("99")) {
                    textt.setText("Error");
                    toastt.setView(layoutt);
                    toastt.show();

                    return;
                }
                if (marketplace_Token_status.equals("11")) {

                    textt.setText("No access");
                    toastt.setView(layoutt);
                    toastt.show();

                    return;
                }
                if (marketplace_Token_status.equals("0")) {
                    try {
                        String url = "https://store.ayubo.life/index.php/ldap/index/token?token=";
                        System.out.println("===========Market Place Token============" + marketplace_Token);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url + marketplace_Token));
                        startActivity(i);
                    } catch (Exception e) {
                        FirebaseCrash.log("..................................");
                        FirebaseCrash.report(e);
                    }

                } else {
                    //  Toast.makeText(getApplicationContext(), "Login Failed !",
                    //        Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    private void getMarketPlaceToken() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data

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
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println("..........response..........." + response);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();

                if (!(builder == null && builder.toString().equals(""))) {

                    for (String line = null; (line = reader.readLine()) != null; ) {

                        builder.append(line).append("\n");
                        System.out.println("..............=........................." + line);
                        JSONObject jsonObj = new JSONObject(line);


                        String number = jsonObj.optString("number").toString();

                        if (number.isEmpty()) {

                            String res = jsonObj.optString("result").toString();

                            int result = Integer.parseInt(res);

                            System.out.println("==========================Result===============>" + result);
                            if (result == 11) {
                                marketplace_Token_status = "11";
                            }
                            if (result == 0) {

                                marketplace_Token = jsonObj.optString("token").toString();
                                marketplace_Token_status = "0";


                            }

                        } else {

                            marketplace_Token_status = "99";
                            System.out.println("=========================LOGIN FAIL=========>" + 3);
                        }

                    }
                } else {
                    Toast.makeText(getContext(), "Server Error !",
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }

    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Permission required");
                builder.setMessage("This app needs storage and camera permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                PERMISSIONS_MULTIPLE_REQUEST);
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
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {

            permissionGrantedStatus = true;
            System.out.println("=======================");
            System.out.println("==========permission already granted=============");
            System.out.println("=======================");
            // write your logic code if permission already granted
            // write your logic code if permission already granted
            // write your logic code if permission already granted
            // write your logic code if permission already granted
            // write your logic code if permission already granted
        }
    }


}
