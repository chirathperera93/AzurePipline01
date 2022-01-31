package com.ayubo.life.ayubolife.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.health.ExpertViewActivity;
import com.ayubo.life.ayubolife.health.RXViewActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;

public class WorkoutActivity extends AppCompatActivity {
    String encodedHashToken;
    ProgressDialog web_prgDialog;
    String hasToken;
    SwipeRefreshLayout mySwipeRefreshLayout;
    WebView webView;
    HashMap<String, String> user = null;
    String url;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_workout);

        prefs = getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        hasToken = prefs.getString("hashToken", "0");

        webView = (WebView) findViewById(R.id.webView_workout_view);
        //=========================================================
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //  https://livehappy.ayubo.life/index.php?entryPoint=activity_tracker&id=1d29596b-9771-3c41-724e-5937c8878a2b&name=RUNNING
        url = MAIN_URL_LIVE_HAPPY + "index.php?entryPoint=activity_tracker&id=1d29596b-9771-3c41-724e-5937c8878a2b";
        //   https://livehappy.ayubo.life/index.php?entryPoint=activity_tracker&id=1d29596b-9771-3c41-724e-5937c8878a2b"
        try {

            if (Utility.isInternetAvailable(WorkoutActivity.this)) {

                web_prgDialog = new ProgressDialog(WorkoutActivity.this);
                web_prgDialog.setCancelable(false);
                web_prgDialog.show();
                web_prgDialog.setMessage("Loading...");

                WebSettings webSettings = webView.getSettings();
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);

                webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webView.setWebViewClient(new Callback());

                webView.loadUrl(url);
//                webView.setWebChromeClient(new WebChromeClient() {
//                });

                mySwipeRefreshLayout.setOnRefreshListener(
                        new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {

                                if (Utility.isInternetAvailable(WorkoutActivity.this)) {
                                    System.out.println("=====Webview onRefresh========");
                                    webView.reload();
                                } else {

                                }

                                webView.setWebViewClient(new WebViewClient() {
                                                             @Override
                                                             public void onPageFinished(WebView web, String url) {
                                                                 web_prgDialog.dismiss();
                                                                 System.out.println("=====Webview onPageFinished========");
                                                                 if (mySwipeRefreshLayout.isRefreshing()) {
                                                                     System.out.println("=====Webview onPageFinished 2========");
                                                                     mySwipeRefreshLayout.setRefreshing(false);
                                                                 }
                                                             }

                                                             @Override
                                                             public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                                                 System.out.println("====onReceivedError========1");

                                                                 webView.reload();

                                                             }

                                                             @Override
                                                             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                                                 //===============================
                                                                 System.out.println("====onPageStarted========");
                                                                 //     https://livehappy.ayubo.life/water_intake_window_call
                                                                 String cc = MAIN_URL_LIVE_HAPPY + "water_intake_window_call";
                                                                 if (url.equals(MAIN_URL_LIVE_HAPPY + "water_intake_window_call")) {
                                                                     Intent in = new Intent(WorkoutActivity.this, WaterActivity.class);
                                                                     startActivity(in);
                                                                 }

                                                             }


                                                         }
                                );

                            }
                        }
                );

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

            if (url.startsWith(MAIN_URL_LIVE_HAPPY + "workout_start")) {
                val = true;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return val;
            }
            if (url.startsWith(MAIN_URL_LIVE_HAPPY + "activity_tracker")) {
                val = true;
                Intent in = new Intent(WorkoutActivity.this, ActivityTrackerActivity.class);
                startActivity(in);
                return val;
            }
            if (url.startsWith(MAIN_URL_LIVE_HAPPY + "subscriptionView")) {
                val = true;
                Intent in = new Intent(WorkoutActivity.this, Medicine_ViewActivity.class);
                startActivity(in);
                return val;
            }
            if (url.startsWith(MAIN_URL_LIVE_HAPPY + "medicineView")) {
                val = true;
                Intent in = new Intent(WorkoutActivity.this, RXViewActivity.class);
                startActivity(in);
                return val;
            }
            if (url.startsWith(MAIN_URL_LIVE_HAPPY + "scheduleExpertView")) {
                val = true;
                Intent in = new Intent(WorkoutActivity.this, ExpertViewActivity.class);
                startActivity(in);
                return val;
            }

            if (url.startsWith(MAIN_URL_APPS)) {
                val = true;
                System.out.println("==============Redireted URL====================" + url);
                //  url="https://www.w3schools.com/";
                webView.loadUrl(url);
                return val;
            }
            if (url.startsWith(MAIN_URL_LIVE_HAPPY + "water_intake")) {
                val = true;
                Intent in = new Intent(WorkoutActivity.this, WaterActivity.class);
                startActivity(in);
                return val;
            }
            if (url.startsWith(MAIN_URL_LIVE_HAPPY + "water_intake_window_call")) {
                val = true;
                Intent in = new Intent(WorkoutActivity.this, WaterActivity.class);
                startActivity(in);
                return val;
            } else {
                val = true;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return val;
            }

        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            webView.reload();
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
                    Intent in = new Intent(WorkoutActivity.this, WaterActivity.class);
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
            webView.reload();
        }

    }


}
