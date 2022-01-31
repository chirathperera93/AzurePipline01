package com.ayubo.life.ayubolife.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class WaterActivity extends AppCompatActivity {

    String encodedHashToken;
    ProgressDialog web_prgDialog;
    String hasToken;
    SwipeRefreshLayout mySwipeRefreshLayout;
    WebView webView;
    HashMap<String, String> user=null;
    String url; PrefManager prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_water);

        System.out.println("======== In clicked on Bubble=========");
        System.out.println("======== In clicked on Bubble=========");
        System.out.println("======== In  clicked on Bubble=========");

        prefs = new PrefManager(WaterActivity.this);
        hasToken=prefs.getLoginUser().get("hashkey");

        if(hasToken==null){}else {
            try {
                encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

            url= ApiClient.BASE_URL+"index.php?module=MA_Health_Data&action=waterIntake";

            String newUrl=Utility.appendAyuboLoginInfo(prefs.getLoginUser().get("hashkey"),url);

            if (Utility.isInternetAvailable(WaterActivity.this)) {

                web_prgDialog = new ProgressDialog(WaterActivity.this);
                web_prgDialog.setCancelable(false);
                web_prgDialog.show();
                web_prgDialog.setMessage("Loading...");

                webView = (WebView) findViewById(R.id.webView_water);
                webView.getSettings().setJavaScriptEnabled(true);

                webView.clearCache(true);
                webView.loadUrl(newUrl);
              //  webView.setWebViewClient(new QRWebViewActivity.WebViewController());
                webView.setWebViewClient(new WebViewClient() {

                    public void onPageFinished(WebView view, String url) {
                        web_prgDialog.cancel();

                    }
                });

                mySwipeRefreshLayout.setOnRefreshListener(
                        new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {

                                if (Utility.isInternetAvailable(WaterActivity.this)) {
                                    webView.reload();
                                } else {
                                    mySwipeRefreshLayout.setRefreshing(false);

                                    Toast.makeText(WaterActivity.this, R.string.toast_no_internet, Toast.LENGTH_LONG).show();

                                }

                                webView.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public void onPageFinished(WebView web, String url) {
                                        if (mySwipeRefreshLayout.isRefreshing()) {
                                            mySwipeRefreshLayout.setRefreshing(false);
                                        }
                                    }
                                });
                            }
                        }
                );

            } else {

                Toast.makeText(WaterActivity.this, R.string.toast_no_internet, Toast.LENGTH_LONG).show();

            }
        }
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent in = new Intent(this, WelcomeHome.class);
//            startActivity(in);
//            finish();
//        }
//
//
//        return super.onKeyDown(keyCode, event);
//    }

    public class Callback extends WebViewClient{

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val=false;
            System.out.println("==============Redireted URL===================="+url);


            if(url.startsWith("disable_back")){
                val=true;
                return val;
            }
            else if(url.startsWith("enable_back")){
                val=true;
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
            AssetManager mgr =getBaseContext().getAssets();
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

}
