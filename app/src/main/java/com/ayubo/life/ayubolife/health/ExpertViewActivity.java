package com.ayubo.life.ayubolife.health;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.ActivityTrackerActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.db.QbUsersDbManager;

import com.ayubo.life.ayubolife.webrtc.utils.Consts;
import com.ayubo.life.ayubolife.webrtc.utils.PermissionsChecker;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import android.view.View;
import android.widget.ListView;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;

public class ExpertViewActivity extends BaseActivity {
    String encodedHashToken;
    ProgressDialog web_prgDialog;
    String hasToken;

    HashMap<String, String> user=null;
    String url; SharedPreferences prefs;
    private PermissionsChecker checker;

    private ListView opponentsListView;

    private QbUsersDbManager dbManager;
    private boolean isRunForCall;

    WebView webView;
    String perviousLink;
    PrefManager pref;
    ImageButton btn_backImgBtn;
    String userid_ExistingUser, fullName, email,QB,imagepath_db,displayNotiVal;
    final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_expert_view);

        perviousLink="";

        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(ExpertViewActivity.this, Health_MainActivity.class);
//                startActivity(in);
                finish();
            }
        });

        pref = new PrefManager(ExpertViewActivity.this);
        userid_ExistingUser=pref.getLoginUser().get("uid");
        prefs = getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        hasToken= prefs.getString("hashToken", "0");

        webView = (WebView) findViewById(R.id.webView_search_consultant_view);
        //=========================================================  mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url=ApiClient.BASE_URL+"index.php?module=PC_Appointments&action=searchConsultant_Mobile&ref=";



            if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(ExpertViewActivity.this, Manifest.permission.RECORD_AUDIO) + ContextCompat
                .checkSelfPermission(ExpertViewActivity.this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.CAMERA},
                    PERMISSIONS_MULTIPLE_REQUEST);

        }


        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("is_video_call_enable", "true");
        editor.commit();

     //   activateVideoCall();

        try{

            if (Utility.isInternetAvailable(ExpertViewActivity.this)) {

                web_prgDialog = new ProgressDialog(ExpertViewActivity.this);
                web_prgDialog.setCancelable(false);
                web_prgDialog.show();
                web_prgDialog.setMessage("Loading...");

                WebSettings webSettings = webView.getSettings();
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);

                webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webView.setWebViewClient(new Callback());

                webView.loadUrl(url+encodedHashToken);
                System.out.println("ExpertViewActivity Webview....................."+url);

            } else {

            }
        }catch(Exception e){
            System.out.println("Timeline Webview....................."+e);

        }

    }


//1.61.205

    public class Callback extends WebViewClient{

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val=false;
            System.out.println("URL in ExpertViewActivity..................."+url);



            if(url.contains("activity_tracker")){
                val=true;
                Intent in = new Intent(ExpertViewActivity.this, ActivityTrackerActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains("subscriptionView")){
                val=true;
                Intent in = new Intent(ExpertViewActivity.this, RXViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains("medicineView")){
                val=true;
                Intent in = new Intent(ExpertViewActivity.this, Medicine_ViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains("scheduleExpertView")){
                val=true;
                Intent in = new Intent(ExpertViewActivity.this, ExpertViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains("disable_back")){
                btn_backImgBtn.setVisibility(View.GONE);
                val=true;

                return val;
            }
            else if(url.contains("enable_back")){
                btn_backImgBtn.setVisibility(View.VISIBLE);
                val=true;

                return val;
            }
            else if(url.startsWith(MAIN_URL_APPS)){
                val=true;
                webView.loadUrl(url);
                return val;
            }
            else if(url.contains("water_intake_window_call")){
                val=true;
                Intent in = new Intent(ExpertViewActivity.this, WaterActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains(ApiClient.BASE_URL)){

                val=false;
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
            webView.reload();
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            web_prgDialog.dismiss();
        }
    }


    public void activateVideoCall(){
        System.out.println("Video Call Initialising.......................");

       start(true);
//        if (QB.equals("QB")) {
//            initVedioCall();
//            start(true);
//        } else {
//            initVedioCall();
//        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Consts.EXTRA_LOGIN_RESULT_CODE) {
            hideProgressDialog();
            boolean isLoginSuccess = data.getBooleanExtra(Consts.EXTRA_LOGIN_RESULT, false);
            String errorMessage = data.getStringExtra(Consts.EXTRA_LOGIN_ERROR_MESSAGE);

            if (isLoginSuccess) {
              //  saveUserData(userForSave);

              //  signInCreatedUser(userForSave, false);
            } else {
               // Toaster.longToast(getString(R.string.login_chat_login_error) + errorMessage);
                //  userNameEditText.setText(userForSave.getFullName());
                //  chatRoomNameEditText.setText(userForSave.getTags().get(0));
            }
        }
    }
    void hideProgressDialog() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
    }

    public void start(boolean isRun) {

        //initFields(isRun);
    }






    //===============================
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val=false;
            String cc=MAIN_URL_LIVE_HAPPY + "water_intake_window_call";
            if(url.startsWith("https://livehappy.ayubo.life")){
                if(cc.equals(url)){
                    System.out.println("================");
                    System.out.println("======In Side App==========="+url);
                    System.out.println("=================");
                    val=true;
                    Intent in = new Intent(ExpertViewActivity.this, WaterActivity.class);
                    startActivity(in);
                }
            }else{
                val=true;
                System.out.println("================");
                System.out.println("======Out Side App==========="+url);
                System.out.println("=================");

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }

            return val;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            web_prgDialog.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            webView.reload();
        }

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent = new Intent(this,WelcomeHome.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
