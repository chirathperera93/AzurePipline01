package com.ayubo.life.ayubolife.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class MindViewActivity extends AppCompatActivity {
    String encodedHashToken;
    ProgressDialog web_prgDialog;
    String hasToken;
   // SwipeRefreshLayout mySwipeRefreshLayout;
    WebView webView;
    HashMap<String, String> user=null;
    String url; SharedPreferences prefs;
    PrefManager pref;
    ImageButton btn_backImgBtn;
    private Button button1;
    private Button button2;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private int field = 0x00000020;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mind_view);
        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Vibrate for 500 milliseconds

        try {
            // Yeah, this is hidden field.
            field = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable ignored) {
        }
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(field, getLocalClassName());


        pref = new PrefManager(MindViewActivity.this);
        hasToken=pref.getLoginUser().get("hashkey");

        if(hasToken==null){}else {
            try {
                encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            url = ApiClient.BASE_URL_entypoint+ "mobile_mind_view&ref=";


            AudioManager am =(AudioManager) getSystemService(Context.AUDIO_SERVICE);
            am.setStreamVolume(AudioManager.STREAM_MUSIC,am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);

            if (Utility.isInternetAvailable(MindViewActivity.this)) {

                web_prgDialog = new ProgressDialog(MindViewActivity.this);
                web_prgDialog.setCancelable(false);
                web_prgDialog.show();
                web_prgDialog.setMessage("Loading...");

                webView = (WebView) findViewById(R.id.webView_mindview);
                webView.getSettings().setJavaScriptEnabled(true);

                webView.clearCache(true);
                webView.loadUrl(url + encodedHashToken);
                webView.setWebViewClient(new Callback());





            } else {

                Toast.makeText(MindViewActivity.this, R.string.toast_no_internet, Toast.LENGTH_LONG).show();

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!wakeLock.isHeld()) {
            wakeLock.acquire();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("================onStop=================");

    }
    @Override
    protected void onPause() {
        super.onPause();
       System.out.println("================onPause=================");

    }

    public class Callback extends WebViewClient{



        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val=false;
            System.out.println("==============Redireted URL====Mind================"+url);

            if(url.contains(MAIN_URL_LIVE_HAPPY + "activity_tracker")){
                val=true;
                Intent in = new Intent(MindViewActivity.this, ActivityTrackerActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains(MAIN_URL_LIVE_HAPPY + "subscriptionView")){
                val=true;
                Intent in = new Intent(MindViewActivity.this, RXViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains(MAIN_URL_LIVE_HAPPY + "medicineView")){
                val=true;
                Intent in = new Intent(MindViewActivity.this, Medicine_ViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains(MAIN_URL_LIVE_HAPPY + "scheduleExpertView")){
                val=true;
                Intent in = new Intent(MindViewActivity.this, ExpertViewActivity.class);
                startActivity(in);
                return val;
            }

            else if(url.contains(MAIN_URL_APPS)){
                val=true;
                webView.loadUrl(url);
                return val;
            }
            else if(url.contains(MAIN_URL_LIVE_HAPPY + "water_intake")){
                val=true;
                Intent in = new Intent(MindViewActivity.this, WaterActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains(MAIN_URL_LIVE_HAPPY + "water_intake_window_call")){
                val=true;
                Intent in = new Intent(MindViewActivity.this, WaterActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains(MAIN_URL_LIVE_HAPPY + "disable_back")){
                val=true;
                return val;
            }
            else if(url.contains("https://livehappy.ayubo.life")){
                val=true;
                Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
                return val;
            }
            //
            else{
                val=true;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return val;
            }

        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
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
