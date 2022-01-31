package com.ayubo.life.ayubolife.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.common.SetDiscoverPage;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;


public class QRWebViewActivity extends AppCompatActivity {
    String encodedHashToken;
    ProgressDialog web_prgDialog;
    String hasToken;

    WebView webView;
    HashMap<String, String> user = null;
    String url;
    SharedPreferences prefs;

    public void goBackToHome(View v) {
//        Intent i = new Intent(this, NewHomeWithSideMenuActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(i);
        //==========================================
        Ram.setSideMenuItemNumber("25");
//        Intent tempIntent = new Intent(this, NewHomeWithSideMenuActivity.class);
//        Intent tempIntent = new Intent(this, LifePlusProgramActivity.class);
//        Intent tempIntent = new Intent(this, NewDiscoverActivity.class);
        Intent tempIntent = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
        startActivity(tempIntent);
        finish();
    }

    PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //  FirebaseAnalytics Adding
        Bundle bWorkout = new Bundle();
        bWorkout.putString("status", "successful");
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("QRCode_read", bWorkout);
        }


        setContentView(R.layout.activity_qrweb_view);
        url = getIntent().getStringExtra("URL");

        prefs = getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        hasToken = prefs.getString("hashToken", "0");

        pref = new PrefManager(QRWebViewActivity.this);

        hasToken = pref.getLoginUser().get("hashkey");
        webView = (WebView) findViewById(R.id.webView_rqcommonview);
        //=========================================================

        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {

            if (Utility.isInternetAvailable(QRWebViewActivity.this)) {

                web_prgDialog = new ProgressDialog(QRWebViewActivity.this);
                web_prgDialog.setCancelable(false);
                web_prgDialog.show();
                web_prgDialog.setMessage("Loading...");

                WebSettings webSettings = webView.getSettings();
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);

                webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webView.setWebViewClient(new Callback());


                String newUrl = Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"), url);
                webView.loadUrl(newUrl);


            } else {

            }
        } catch (Exception e) {
            System.out.println("Timeline Webview....................." + e);

        }

    }


    public class Callback extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val = false;
            System.out.println("==============Redireted URL====================" + url);


            if (url.startsWith("disable_back")) {
                val = true;

            } else if (url.startsWith("enable_back")) {
                val = true;

            }
//            else{
//                val=true;
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(browserIntent);
//                return val;
//            }
            return val;
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


    //===============================
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {

        Ram.setSideMenuItemNumber("25");
//        Intent tempIntent = new Intent(this, NewHomeWithSideMenuActivity.class);
//        Intent tempIntent = new Intent(this, LifePlusProgramActivity.class);
//        Intent tempIntent = new Intent(this, NewDiscoverActivity.class);
        Intent tempIntent = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
        startActivity(tempIntent);
        finish();
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
                    Intent in = new Intent(QRWebViewActivity.this, WaterActivity.class);
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


}

