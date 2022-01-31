package com.ayubo.life.ayubolife.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
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
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;

import com.ayubo.life.ayubolife.common.SetDiscoverPage;
import com.ayubo.life.ayubolife.lifeplus.LifePlusProgramActivity;
import com.ayubo.life.ayubolife.map_challenges.MapJoinChallenge_Activity;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.body.WorkoutActivity;
import com.ayubo.life.ayubolife.health.Health_MainActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.health.RXViewActivity;
import com.ayubo.life.ayubolife.home.EatViewActivity;
import com.ayubo.life.ayubolife.home.PostDataActivity;
import com.ayubo.life.ayubolife.home.RelaxViewActivity;
import com.ayubo.life.ayubolife.home_popup_menu.AskQuestion_Activity;
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity;
import com.ayubo.life.ayubolife.home_popup_menu.Reports_Activity;
import com.ayubo.life.ayubolife.map_challenges.MapChallengeActivity;
import com.ayubo.life.ayubolife.mind.Mind_MainActivity;
import com.ayubo.life.ayubolife.model.RoadLocationObj;
import com.ayubo.life.ayubolife.qrcode.DecoderActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;


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
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;


public class Challenges_Fragment extends Fragment {
    private static final String TAG = NewHomeWithSideMenuActivity.class.getSimpleName();
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;
    boolean permissionGrantedStatus=false;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    String currentUrl;
    PopupWindow mPopupWindow;
    LinearLayout mRelativeLayout;
    ImageButton mButton;
    ProgressDialog webProgress_timeline;

    boolean isPopupClicked=false;

    private final static int PICKFILE_REQUEST_CODE=2;

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE=1;


    // String url=null;

    String loadingStatus;

    boolean isLoaded=false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    boolean timeout;
    String hasToken, loginType;
    SwipeRefreshLayout mySwipeRefreshLayout;

    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;

    String setDeviceID_Success,today,serviceDataStatus;
    int total_steps;
    String noof_day;
    ImageButton btn_signout;
    TextView btn_Appointment;
    WebView webView;
    SharedPreferences prefs;
    String userid_ExistingUser;
    String cityJsonString;
    String encodedHashToken;
    private boolean isCurrentCameraFront;
    private boolean isLocalVideoFullScreen;
    // String url=null;
    String url_logout=null;
    protected Handler mainHandler;
    boolean st=false;
    private TextView connectionStatusLocal;
    String challenge_id;


    WebView browser = null;
    //  final String TAG = "WebviewActivity";
    String requestedURL = null;
    // private ConnectionTimeoutHandler timeoutHandler = null;
    int PAGE_LOAD_PROGRESS = 0;
    final String KEY_REQUESTED_URL = "requested_url";
    final String CALLBACK_URL = "success";
    ProgressDialog prgDialog,web_prgDialog;  String url;
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
            R.drawable.sidemenu_ic_sharew };
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,marketplace_Token_status,marketplace_Token;

    ImageButton btn_img_workout,btn_img_body,btn_img_food,btn_img_mind,btn_img_relax,btn_img_eat;
    TextView btn_txt_workout,btn_txt_body,btn_txt_food,btn_txt_mind,btn_txt_relax,btn_txt_eat;
    LinearLayout btn_health,btn_body,btn_workout,btn_mind,main_dark_view,btn_relax,btn_eat;
    private OnFragmentInteractionListener mListener;
    // private ProgressDialog prgDialog_ch,progressBar;
    ArrayList<RoadLocationObj> myTagList=null;
    ArrayList<RoadLocationObj> myTreasure=null;
    PrefManager pref;
    public Challenges_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Prescription3_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Challenges_Fragment newInstance(String param1, String param2) {
        Challenges_Fragment fragment = new Challenges_Fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_prescription3_, container, false);

        View view = inflater.inflate(R.layout.fragment_challenges_, container, false);
        prefs = getContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        challenge_id="";
        pref = new PrefManager(getContext());
        prefs = getContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        myTagList=new ArrayList<RoadLocationObj>();
        myTreasure=new ArrayList<RoadLocationObj>();

        userid_ExistingUser=pref.getLoginUser().get("uid");
        hasToken=pref.getLoginUser().get("hashkey");
        prgDialog=new ProgressDialog(getContext());



        inflatert = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) view.findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(getContext());
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);
        Ram.setIsOne_Medicine(true);
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Ram.setIsOne_Medicine(true);
        Ram.setIsTwo_Medicine(false);

        url =ApiClient.BASE_URL_entypoint+ "ayuboLifeTimelineLogin&type=joinChallenge&ref=";

        webView = (WebView) view.findViewById(R.id.webView_challenges);
        //=========================================================
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.print("=======hasToken============="+hasToken);


        try{

            if (Utility.isInternetAvailable(getContext())) {

                webProgress_timeline = new ProgressDialog(getActivity());
                webProgress_timeline.show();
                webProgress_timeline.setMessage("Loading...");


                WebSettings webSettings = webView.getSettings();
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);

                if(Build.VERSION.SDK_INT >= 21){
                    webSettings.setMixedContentMode(0);
                    webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }else if(Build.VERSION.SDK_INT >= 19){
                    webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }else if(Build.VERSION.SDK_INT < 19){
                    webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }

                webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webView.setWebViewClient(new Callback());

                System.out.print("====================");
                System.out.print("========final URL Timeline=========" +url+encodedHashToken);
                webView.getSettings().setLoadsImagesAutomatically(true);
                //  System.out.print(url+encodedHashToken);
                System.out.print("====================");
                webView.requestFocus(View.FOCUS_DOWN);
                webView.loadUrl(url+encodedHashToken);
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
                webView.setWebChromeClient(new WebChromeClient(){



                    //For Android 3.0+
                    public void openFileChooser(ValueCallback<Uri> uploadMsg){
                        mUM = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        startActivityForResult(Intent.createChooser(i,"File Chooser"), FCR);
                    }
                    // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
                    public void openFileChooser(ValueCallback uploadMsg, String acceptType){
                        mUM = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        startActivityForResult(
                                Intent.createChooser(i, "File Browser"),
                                FCR);
                    }
                    //For Android 4.1+
                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                        mUM = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
                    }
                    //For Android 5.0+
                    public boolean onShowFileChooser(
                            WebView webView, ValueCallback<Uri[]> filePathCallback,
                            WebChromeClient.FileChooserParams fileChooserParams){

                        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                                .checkSelfPermission(getActivity(),Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                    PERMISSIONS_MULTIPLE_REQUEST);

                        }
//                        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED )) {
//                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 2);
//                        }
                        // write your logic here

                        if(mUMA != null){
                            mUMA.onReceiveValue(null);
                        }
                        mUMA = filePathCallback;
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
                            File photoFile = null;
                            try{
                                photoFile = createImageFile();
                                takePictureIntent.putExtra("PhotoPath", mCM);
                            }catch(IOException ex){
                                Log.e(TAG, "Image file creation failed", ex);
                            }
                            if(photoFile != null){

                                mCM = "file:" + photoFile.getAbsolutePath();
                                System.out.println("==========UPLOAD==========");
                                System.out.println("====================");
                                System.out.println("=========pATH==========="+photoFile.getAbsolutePath());
                                System.out.println("====================");
                                System.out.println("===========UPLOAD=========");
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            }else{
                                takePictureIntent = null;
                            }
                        }
                        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        contentSelectionIntent.setType("*/*");
                        Intent[] intentArray;
                        if(takePictureIntent != null){
                            intentArray = new Intent[]{takePictureIntent};
                        }else{
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


//                                Intent intent = new Intent(getContext(), NewHomeWithSideMenuActivity.class);
//                                Intent intent = new Intent(getContext(), LifePlusProgramActivity.class);
                                Intent intent = new SetDiscoverPage().getDiscoverIntent(getContext());
                                startActivity(intent);

                                //=======================================================


                            }
                        }
                );

            } else {
                textt.setText("Unable to detect an active internet connection");
                toastt.setView(layoutt);
                toastt.show();

            }
        }catch(Exception e){
            e.printStackTrace();
        }

//===============================================
        main_dark_view=(LinearLayout)view.findViewById(R.id.main_dark_view);
        //   main_dark_view.setVisibility(View.GONE);

        btn_workout=(LinearLayout)view.findViewById(R.id.btn_workout);
        btn_body=(LinearLayout)view.findViewById(R.id.btn_body);
        btn_mind=(LinearLayout)view.findViewById(R.id.btn_workout);

        btn_relax=(LinearLayout)view.findViewById(R.id.btn_relax);
        btn_img_relax=(ImageButton)view.findViewById(R.id.btn_img_relax);
        btn_txt_relax=(TextView)view.findViewById(R.id.btn_txt_relax);

        btn_eat=(LinearLayout)view.findViewById(R.id.btn_eat);
        btn_img_eat=(ImageButton)view.findViewById(R.id.btn_img_eat);
        btn_txt_eat=(TextView)view.findViewById(R.id.btn_txt_eat);

        btn_mind=(LinearLayout)view.findViewById(R.id.btn_mind);

        btn_img_workout=(ImageButton)view.findViewById(R.id.btn_img_workout);

        // btn_img_health=(ImageButton)view.findViewById(R.id.btn_img_health);
        btn_img_body=(ImageButton)view.findViewById(R.id.btn_img_body);
        //  btn_img_challenge=(ImageButton)view.findViewById(R.id.btn_img_challenge);
        btn_img_mind=(ImageButton)view.findViewById(R.id.btn_img_mind);

        btn_txt_workout=(TextView)view.findViewById(R.id.btn_txt_workout);
        //  btn_txt_health=(TextView)view.findViewById(R.id.btn_txt_health);
        btn_txt_body=(TextView)view.findViewById(R.id.btn_txt_body);

        btn_txt_mind=(TextView)view.findViewById(R.id.btn_txt_mind);



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
                Intent intent = new Intent(getContext(), EatViewActivity.class);
                intent.putExtra("URL", ApiClient.BASE_URL_entypoint+"mobile_food_view");
                startActivity(intent);
            }
        });
        btn_img_eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
//                intent.putExtra("URL", ApiClient.BASE_URL_entypoint+"mobile_food_view");
//                startActivity(intent);
                Intent intent = new Intent(getContext(), EatViewActivity.class);
                intent.putExtra("URL", ApiClient.BASE_URL_entypoint+"mobile_food_view");
                startActivity(intent);
            }
        });
        btn_txt_eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EatViewActivity.class);
                intent.putExtra("URL", ApiClient.BASE_URL_entypoint+"mobile_food_view");
                startActivity(intent);
            }
        });
//=======================
        btn_relax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RelaxViewActivity.class);
                intent.putExtra("URL", ApiClient.BASE_URL_entypoint+"mobile_mind_view");
                startActivity(intent);
            }
        });
        btn_img_relax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RelaxViewActivity.class);
                intent.putExtra("URL", ApiClient.BASE_URL_entypoint+"mobile_mind_view");
                startActivity(intent);
            }
        });
        btn_txt_relax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(getContext(), Mind_MainActivity.class);
//                startActivity(in);
                Intent intent = new Intent(getContext(), RelaxViewActivity.class);
                intent.putExtra("URL", ApiClient.BASE_URL_entypoint+"mobile_mind_view");
                startActivity(intent);
            }
        });

        //==============================================

        //===============================================================
        //
// Get the widgets reference from XML layout
        mRelativeLayout = (LinearLayout) view.findViewById(R.id.btn_mind);
        mButton = (ImageButton) view.findViewById(R.id.btn_img_love);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isPopupClicked=true;

                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.main_menu_popup_layout, null);

                mPopupWindow = new PopupWindow(
                        customView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
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
                        intent.putExtra("activityName", "mydiatition");
                        startActivity(intent);
                    }
                });
                ImageButton btn_img_myfamilty = (ImageButton) customView.findViewById(R.id.btn_img_myfamilty);
                btn_img_myfamilty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                        Intent intent = new Intent(getContext(), MyDoctor_Activity.class);
                        intent.putExtra("activityName", "mytrainer");
                        startActivity(intent);
                    }
                });

                ImageButton btn_img_myreports = (ImageButton) customView.findViewById(R.id.btn_img_myreports);
                btn_img_myreports.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                        Intent intent = new Intent(getContext(), Reports_Activity.class);
                        startActivity(intent);
                    }
                });
                ImageButton btn_img_qrcode = (ImageButton) customView.findViewById(R.id.btn_img_qrcode);
                btn_img_qrcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                        Intent intent = new Intent(getContext(), DecoderActivity.class);
                        // Intent intent = new Intent(getContext(), MyHealthDataActivity.class);
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

                dimBehind(mPopupWindow);

            }
        });

        return view;


    }

    private void Service_Service_getStepsData_ServiceCall() {

        if (Utility.isInternetAvailable(getContext())) {

            prgDialog.show();
            prgDialog.setMessage("Loading..");
            prgDialog.setCancelable(false);
            Service_getStepsData task = new Service_getStepsData();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
        }
    }
    private void writeToFile(String result) {
        try {
            FileOutputStream fos =getActivity().openFileOutput(challenge_id+".json", getActivity().MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(result);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String loadJSONFromAssetNew(Context context) {
        String json = null;
        try {
            InputStream is = context.openFileInput(challenge_id+".json");
            int size = is.available();
            if(size > 0) {
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            }else{
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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
    private void Service_getChallengeJson_ServiceCall() {

        if (Utility.isInternetAvailable(getContext())) {

            prgDialog.show();
            prgDialog.setMessage("Loading challenge data..");
            prgDialog.setCancelable(false);
            Service_getChallengeJson task = new Service_getChallengeJson();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
        }
    }
    private class Service_getChallengeJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            System.out.println("========Service Calling============0");
            getChallengeJson();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            prgDialog.cancel();

            writeToFile(cityJsonString);
            MapChallengeActivity.Book treaturesList = new MapChallengeActivity.Book(myTreasure);

            Intent intent = new Intent(getContext(), MapChallengeActivity.class);
            intent.putExtra("challenge_id", challenge_id);
            intent.putExtra("noof_day", noof_day);
            intent.putExtra("Treatures", treaturesList);
            intent.putExtra("steps", Integer.toString(total_steps));
            startActivity(intent);

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
            prgDialog.cancel();

            String cityFile= loadJSONFromAssetNew(com.ayubo.life.ayubolife.webrtc.App.getInstance());
             if(cityFile==null){

                 Service_getChallengeJson_ServiceCall();
             }else {

                 MapChallengeActivity.Book treaturesList = new MapChallengeActivity.Book(myTreasure);

                 Intent intent = new Intent(getContext(), MapChallengeActivity.class);
                 intent.putExtra("challenge_id", challenge_id);
                 intent.putExtra("noof_day", noof_day);
                 intent.putExtra("Treatures", treaturesList);
                 intent.putExtra("steps", Integer.toString(total_steps));
                 startActivity(intent);
             }
        }
    }
    private void makeSetDefaultDevice() {

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
                                myTreasure.clear();

                                String data = jsonObj.optString("data").toString();
                                JSONObject jsonData = new JSONObject(data);

                                String counter = jsonData.optString("counter").toString();
                                noof_day = jsonData.optString("day").toString();
                                String treatures = jsonData.optString("treatures").toString();

                                total_steps=Integer.parseInt(counter);

                                JSONArray myDataListsAll= null;
                                try {
                                    myDataListsAll = new JSONArray(treatures);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                for(int i=0;i<myDataListsAll.length();i++) {

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


                            }catch(Exception e){
                                serviceDataStatus="99";
                                e.printStackTrace();
                            }


                        }

                        else{
                            System.out.println("========Service Calling============10");
                            serviceDataStatus="99";
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
    public static class Treatures implements Parcelable {
        // book basics
        ArrayList<RoadLocationObj> myTreaturesList=  new ArrayList<RoadLocationObj>();

        private String title;
        private String author;
        // main constructor
        public Treatures(ArrayList<RoadLocationObj> myTagLis) {
            this.myTreaturesList = myTagLis;

        }
        public Treatures(Parcel parcel) {

            myTreaturesList = new ArrayList<RoadLocationObj>();
            this.myTreaturesList = parcel.readArrayList(RoadLocationObj.class.getClassLoader());

        }

        // getters
        public String getTitle() {
            return title;
        }
        public String getAuthor() {
            return author;
        }

        public static final Parcelable.Creator<Treatures> CREATOR = new Parcelable.Creator<Treatures>() {

            @Override
            public Treatures createFromParcel(Parcel parcel) {
                return new Treatures(parcel);
            }

            @Override
            public Treatures[] newArray(int size) {
                return new Treatures[size];
            }

        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            try{
                parcel.writeList(myTreaturesList);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
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

    private void openAppStore(){
        Service_getMarketPlace_ServiceCall();
    }

    private void Service_getMarketPlace_ServiceCall() {
        if (Utility.isInternetAvailable(getContext())) {
            try {
                Service_getMarketPlace task = new Service_getMarketPlace();
                task.execute(new String[]{ApiClient.BASE_URL_live});
            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
            textt.setText("Unable to detect an active internet connection");
            toastt.setView(layoutt);
            toastt.show();
        }
    }

    private class Service_getMarketPlace extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(getContext());
            prgDialog.show();
            prgDialog.setMessage("Loading..");
        }

        @Override
        protected String doInBackground(String... urls) {
            getMarketPlaceToken();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            prgDialog.cancel();
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
                    try{
                        String url="https://store.ayubo.life/index.php/ldap/index/token?token=";
                        System.out.println("===========Market Place Token============"+marketplace_Token);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url+marketplace_Token));
                        startActivity(i);
                    }catch(Exception e){
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

                        if(number.isEmpty()){

                            String res = jsonObj.optString("result").toString();

                            int result = Integer.parseInt(res);

                            System.out.println("==========================Result===============>" + result);
                            if (result == 11) {
                                marketplace_Token_status="11";
                            }
                            if (result == 0) {

                                marketplace_Token = jsonObj.optString("token").toString();
                                marketplace_Token_status="0";


                            }

                        }else{

                            marketplace_Token_status="99";
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private File createImageFile() throws IOException{
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }

    public class Callback extends WebViewClient{

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val=false;

            System.out.println("===========TimeLine===Callback Redireted URL===================="+url);

            if(url.contains("/index.php?entryPoint=joinChallenge")){
                val=false;
                return val;
            }


            else if(url.contains("soloAdventureMap")){

                String[] parts = url.split("_");
                challenge_id = parts[1];
                val=true;
                Service_Service_getStepsData_ServiceCall();

                return val;
            }
             else if(url.contains("soloAdventureCompleted")){

                String[] parts = url.split("_");
                challenge_id = parts[1];
                String  chUrl= ApiClient.BASE_URL+"index.php?module=HAL_challenges&action=adventureSolo_complete&record="+challenge_id;

                val=true;
                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", chUrl);
                startActivity(intent);
                return val;


            }

            else if(url.contains("soloAdventureJoin")){
                val=true;

                String[] parts = url.split("_");
                challenge_id = parts[1];
                MapChallengeActivity.Book treaturesList = new MapChallengeActivity.Book(myTreasure);

                Intent intent = new Intent(getContext(), MapJoinChallenge_Activity.class);
                intent.putExtra("challenge_id",challenge_id);
                intent.putExtra("Treatures", treaturesList);
                intent.putExtra("steps",Integer.toString(total_steps));
                startActivity(intent);
                return val;
            }
            else if(url.contains(MAIN_URL_APPS)){
                if(url.contains("openInView=true")){
                    val=true;
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    return val;
                }
                else if(url.contains("openInBrowser=true")){
                    val=true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                }
                else{
                    val=false;
                    return val;
                }
            }

            else if(url.contains("subscriptionView")){
                val=true;
                Intent in = new Intent(getContext(), RXViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains("medicineView")){
                val=true;
                Intent in = new Intent(getContext(), Medicine_ViewActivity.class);
                startActivity(in);
                return val;
            }

            else if(url.contains("https://livehappy.ayubo.life")){
                if(url.contains("openInBrowser=true")){
                    val=true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                }
                else if(url.contains("timelinepost")){
                    val=true;
                    Intent in = new Intent(getContext(), PostDataActivity.class);
                    in.putExtra("cId", "aa");
                    in.putExtra("cName", "aa");
                    in.putExtra("postType", "aa");
                    startActivity(in);
                    return val;
                }
                else if(url.contains("openInSame=true")){
                    val=false;
                    return val;
                }
                else if(url.contains("index.php?entryPoint=mobile_mind_view")){
                    val=true;
                    //  Intent in = new Intent(getContext(), MindViewActivity.class);
                    Intent in = new Intent(getContext(), Mind_MainActivity.class);
                    startActivity(in);
                    return val;
                }

                else if(url.contains("index.php?entryPoint=mobile_health_view")){
                    val=true;
                    Intent in = new Intent(getContext(), Health_MainActivity.class);
                    //  Intent in = new Intent(getContext(), CommonTabActivity.class);

                    startActivity(in);
                    return val;
                }
                else if(url.contains("subscriptionView")){
                    val=true;
                    Intent in = new Intent(getContext(), RXViewActivity.class);
                    startActivity(in);
                    return val;
                }
                else if(url.contains("medicineView")){
                    val=true;
                    Intent in = new Intent(getContext(), Medicine_ViewActivity.class);
                    startActivity(in);
                    return val;
                }
                else if(url.contains("mobileMedicalHistory")){
                    val=true;
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    return val;

                }
                else if(url.contains("index.php?entryPoint=ayuboLifeTimeline")){
                    val=false;
                    return val;
                }
                else if(url.contains("initChatMobile")){
                    val=true;
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                }
                else if(url.contains("water_intake_window_call")){
                    val=true;
                    Intent in = new Intent(getContext(), WaterActivity.class);
                    startActivity(in);
                    return val;
                }
                else if(url.contains(ApiClient.BASE_URL)){
                    val=true;
                    System.out.println("=========iN======CommonWebViewActivity========="+url);
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                }
                else{
                    val=true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                }

            }
            else if(url.contains("https://devo.ayubo.life")){
                if(url.contains("openInBrowser=true")){
                    val=true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                }

                else if(url.contains("timelinepost")){
                    val=true;
                    Intent intent = new Intent(getContext(), PostDataActivity.class);
                    intent.putExtra("cId", "aa");
                    intent.putExtra("cName", "aa");
                    intent.putExtra("postType", "aa");
                    startActivity(intent);
                    return val;
                }
                else if(url.contains("openInSame=true")){
                    val=false;
                    return val;
                }
                else if(url.contains("index.php?entryPoint=mobile_mind_view")){
                    val=true;
                    //  Intent in = new Intent(getContext(), MindViewActivity.class);
                    Intent in = new Intent(getContext(), Mind_MainActivity.class);
                    startActivity(in);
                    return val;
                }

                else if(url.contains("index.php?entryPoint=mobile_health_view")){
                    val=true;
                    Intent in = new Intent(getContext(), Health_MainActivity.class);
                    //  Intent in = new Intent(getContext(), CommonTabActivity.class);

                    startActivity(in);
                    return val;
                }
                else if(url.contains("subscriptionView")){
                    val=true;
                    Intent in = new Intent(getContext(), RXViewActivity.class);
                    startActivity(in);
                    return val;
                    //subscriptionView
//                    val=false;
//                    return val;
                }
                else if(url.contains("medicineView")){
                    val=true;
                    Intent in = new Intent(getContext(), Medicine_ViewActivity.class);
                    startActivity(in);
                    return val;
                    //subscriptionView
//                    val=false;
//                    return val;
                }
//                else if(url.contains("subscriptionView")){
//                    val=false;
//                    return val;
//                }
                else if(url.contains("index.php?entryPoint=mobileMedicalHistory")){

                    if(url.contains("openInView=true")){
                        val=true;
                        Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                        intent.putExtra("URL", url);
                        startActivity(intent);

                        return val;
                    }else{
                        val=false;
                        return val;
                    }
                }
                else if(url.contains("index.php?entryPoint=ayuboLifeTimeline")){
                    val=false;
                    return val;
                }
                else if(url.contains("initChatMobile")){
                    val=true;
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                }
                else if(url.contains("water_intake_window_call")){
                    val=true;
                    Intent in = new Intent(getContext(), WaterActivity.class);
                    startActivity(in);
                    return val;
                }
                else if(url.contains(ApiClient.BASE_URL)){
                    val=true;
                    System.out.println("=========iN======CommonWebViewActivity========="+url);
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                }
                else{
                    val=true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                }
            }

            else if(url.contains("disable_back")){
                val=true;
                return val;
            }

            else if(url.contains("subscriptionView")){
                val=false;
                return val;
            }
            else if(url.contains("index.php?entryPoint=mobileMedicalHistory")){

                if(url.contains("openInView=true")){
                    val=true;
                    Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                }else{
                    val=false;
                    return val;
                }
            }


            else if(url.contains("index.php?entryPoint=ayuboLifeTimeline")){
                val=false;
                return val;
            }
            else if(url.contains("initChatMobile")){
                val=true;
                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);

                return val;
            }
            else if(url.contains(MAIN_URL_APPS)){
                val=true;
                System.out.println("==============Redireted URL===================="+url);
                //  url="https://www.w3schools.com/";
                webView.loadUrl(url);
                return val;
            }
            else if(url.contains("water_intake_window_call")){
                val=true;
                Intent in = new Intent(getContext(), WaterActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains(ApiClient.BASE_URL)){
                val=true;
                System.out.println("=========iN======CommonWebViewActivity========="+url);
                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);

                return val;
            }
            else{
                val=true;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return val;
            }

        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
            String htmlFilename = "error.html";
            AssetManager mgr =getActivity().getBaseContext().getAssets();
            try {
                InputStream in = mgr.open(htmlFilename, AssetManager.ACCESS_BUFFER);
                String htmlContentInStringFormat = Utility.StreamToString(in);
                in.close();
                webView.loadDataWithBaseURL(null, htmlContentInStringFormat, "text/html", "utf-8", null);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(webProgress_timeline!=null){
                webProgress_timeline.dismiss();
            }
            //   Intent intent = new Intent(getContext(), SetStepsCalibilityActivity.class);
            //   startActivity(intent);



        }
    }

    public class WebViewController extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
            String htmlFilename = "error.html";
            AssetManager mgr =getActivity().getBaseContext().getAssets();
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
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
