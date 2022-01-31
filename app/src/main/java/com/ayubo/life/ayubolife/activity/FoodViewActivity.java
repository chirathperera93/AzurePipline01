package com.ayubo.life.ayubolife.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;

public class FoodViewActivity extends AppCompatActivity {
    String encodedHashToken;
    ProgressDialog web_prgDialog;
    String hasToken;
    ImageButton btn_backImgBtn;
    WebView webView;
    HashMap<String, String> user=null;
    String url; PrefManager prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_food_view);
        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        prefs = new PrefManager(FoodViewActivity.this);
        hasToken=prefs.getLoginUser().get("hashkey");

        webView = (WebView) findViewById(R.id.webView_foodview);
        //=========================================================

        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        url=MAIN_URL_LIVE_HAPPY + "index.php?entryPoint=mobile_food_view&ref=";

        try{

            if (Utility.isInternetAvailable(FoodViewActivity.this)) {

                web_prgDialog = new ProgressDialog(FoodViewActivity.this);
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


            } else {

            }
        }catch(Exception e){
            System.out.println("Timeline Webview....................."+e);

        }

    }


    public class Callback extends WebViewClient{

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val=false;
            System.out.println("=============FoodView Callback===================="+url);

            if(url.contains(MAIN_URL_LIVE_HAPPY + "today_recipe")){
                val=true;
                System.out.println("==============Redireted URL===================="+url);
                webView.loadUrl(url+encodedHashToken);
                return val;
            }
            else if(url.contains(MAIN_URL_LIVE_HAPPY + "activity_tracker")){
                val=true;
                Intent in = new Intent(FoodViewActivity.this, ActivityTrackerActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains(MAIN_URL_LIVE_HAPPY + "subscriptionView")){
                val=true;
                Intent in = new Intent(FoodViewActivity.this, Medicine_ViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains(MAIN_URL_LIVE_HAPPY + "medicineView")){
                val=true;
                Intent in = new Intent(FoodViewActivity.this, RXViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains(MAIN_URL_LIVE_HAPPY + "scheduleExpertView")){

                val=true;
                Intent in = new Intent(FoodViewActivity.this, ExpertViewActivity.class);
                startActivity(in);
                return val;
            }

            else if(url.contains(MAIN_URL_APPS)){
                val=true;
                System.out.println("==============Redireted URL===================="+url);
                //  url="https://www.w3schools.com/";
                webView.loadUrl(url);
                return val;
            }
            else if(url.contains(MAIN_URL_LIVE_HAPPY + "water_intake_window_call")){
                val=true;
                Intent in = new Intent(FoodViewActivity.this, WaterActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains("initChatMobile")){
                val=true;
                Intent intent = new Intent(FoodViewActivity.this, CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);

                return val;
            }
            else if(url.contains("https://livehappy.ayubo.life")){
                val=true;
                Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
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
            boolean val=false;
            String cc=MAIN_URL_LIVE_HAPPY + "water_intake_window_call";
            if(url.startsWith("https://livehappy.ayubo.life")){
                if(cc.equals(url)){
                    System.out.println("================");
                    System.out.println("======In Side App==========="+url);
                    System.out.println("=================");
                    val=true;
                    Intent in = new Intent(FoodViewActivity.this, WaterActivity.class);
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
