package com.ayubo.life.ayubolife.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.health.ExpertViewActivity;
import com.ayubo.life.ayubolife.health.RXViewActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;

public class BodyViewActivity extends AppCompatActivity {
    String encodedHashToken;
    ImageButton btn_backImgBtn;
    ProgressDialog web_prgDialog;
    String hasToken;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private int field = 0x00000020;
    WebView webView;
    HashMap<String, String> user = null;
    String url;
    SharedPreferences prefs;

    public void goBackToHome() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_body_view);

        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        try {
            // Yeah, this is hidden field.
            field = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);

            powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(field, getLocalClassName());

            if (!wakeLock.isHeld()) {
                wakeLock.acquire();
            }
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }
        } catch (Throwable ignored) {
        }


        prefs = getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        hasToken = prefs.getString("hashToken", "0");

        webView = (WebView) findViewById(R.id.webView_bodyview);
        //=========================================================

        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        url = ApiClient.BASE_URL_entypoint + "mobile_body_view&ref=";

        try {

            if (Utility.isInternetAvailable(BodyViewActivity.this)) {

                web_prgDialog = new ProgressDialog(BodyViewActivity.this);
                web_prgDialog.setCancelable(false);
                web_prgDialog.show();
                web_prgDialog.setMessage("Loading...");

                WebSettings webSettings = webView.getSettings();
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);

                webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webView.setWebViewClient(new Callback());
                webView.setLongClickable(true);
                webView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });

                webView.loadUrl(url + encodedHashToken);


            } else {

            }
        } catch (Exception e) {
            System.out.println("Timeline Webview....................." + e);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public class Callback extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val = false;
            String rediredtedUrl = url;
            String Str = new String(url);
            System.out.println(Str.matches("activity_tracker"));
            System.out.println("===========BodyView===Redireted URL====================" + url);

            //   https://livehappy.ayubo.life/index.php?entryPoint=activity_tracker&id=1d29596b-9771-3c41-724e-5937c8878a2b&name=RUNNING
            if (url.contains(ApiClient.BASE_URL_entypoint + "activity_tracker")) {
                val = true;
                System.out.println("========Inside Tracker=================" + url);
                String Str2;
                String[] parts = url.split("&");
                String idData = parts[1];
                String nameData = parts[2];
                System.out.println("======sss=====0==========" + idData);
                System.out.println("======sss=====00==========" + nameData);

                String[] idArrya = idData.split("=");
                String activityId = idArrya[1];

                String[] nameArrya = nameData.split("=");
                String activityName = nameArrya[1];

                System.out.println("============activityName========" + activityName);
                System.out.println("============activityId========" + activityId);
                Intent intent = new Intent(getBaseContext(), ActivityTrackerActivity.class);
                intent.putExtra("activityName", activityName);
                intent.putExtra("activityId", activityId);
                startActivity(intent);


                System.out.println("=========Activity Started with Live===================");
                return val;
            } else if (url.contains(MAIN_URL_LIVE_HAPPY + "workout_start")) {
                val = true;
                Intent in = new Intent(BodyViewActivity.this, WorkoutActivity.class);
                startActivity(in);
                return val;
            }
            if (url.contains(MAIN_URL_LIVE_HAPPY + "activity_tracker")) {
                val = true;
                Intent in = new Intent(BodyViewActivity.this, ActivityTrackerActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains("/subscriptionView")) {
                val = true;
                Intent in = new Intent(BodyViewActivity.this, Medicine_ViewActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains("/medicineView")) {
                val = true;
                Intent in = new Intent(BodyViewActivity.this, RXViewActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains("/scheduleExpertView")) {
                val = true;
                Intent in = new Intent(BodyViewActivity.this, ExpertViewActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains(MAIN_URL_APPS)) {
                val = true;
                System.out.println("==============Redireted URL====================" + url);
                //  url="https://www.w3schools.com/";
                webView.loadUrl(url);
                return val;
            } else if (url.contains("/water_intake")) {
                val = true;
                Intent in = new Intent(BodyViewActivity.this, WaterActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains("/water_intake_window_call")) {
                val = true;
                Intent in = new Intent(BodyViewActivity.this, WaterActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains("/disable_back")) {
                val = true;
                return val;
            } else if (url.contains("/enable_back")) {
                val = true;
                return val;
            } else if (url.contains("https://livehappy.ayubo.life")) {
                val = true;
                Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
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
            AssetManager mgr = getBaseContext().getAssets();
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

            web_prgDialog.dismiss();
        }
    }


    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val = false;
            String cc = MAIN_URL_LIVE_HAPPY + "water_intake_window_call";
            if (url.startsWith("https://livehappy.ayubo.life")) {
                if (cc.equals(url)) {
                    System.out.println("================");
                    System.out.println("======In Side App===========" + url);
                    System.out.println("=================");
                    val = true;
                    Intent in = new Intent(BodyViewActivity.this, WaterActivity.class);
                    startActivity(in);
                }
            } else {
                val = true;
                System.out.println("================");
                System.out.println("======Out Side App===========" + url);
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
            String htmlFilename = "error.html";
            AssetManager mgr = getBaseContext().getAssets();
            try {
                InputStream in = mgr.open(htmlFilename, AssetManager.ACCESS_BUFFER);
                String htmlContentInStringFormat = Utility.StreamToString(in);
                in.close();
                webView.loadDataWithBaseURL(null, htmlContentInStringFormat, "text/html", "utf-8", null);

            } catch (IOException e) {
                e.printStackTrace();
            }
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
