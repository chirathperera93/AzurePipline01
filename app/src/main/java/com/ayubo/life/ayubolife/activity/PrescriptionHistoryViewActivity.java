package com.ayubo.life.ayubolife.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.IOException;
import java.io.InputStream;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.satalite_menu.ProfileActivity;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;

public class PrescriptionHistoryViewActivity extends AppCompatActivity {
private static final String TAG = CommonWebViewActivity.class.getSimpleName();
        String encodedHashToken;
        ProgressDialog web_prgDialog;
        String hasToken;
private String mCM;
private ValueCallback<Uri> mUM;
private ValueCallback<Uri[]> mUMA;
private final static int FCR=1;
        WebView webView;
        HashMap<String, String> user=null;
        String url;
        //SharedPreferences prefs;
        PrefManager pref;
       ImageButton btn_backImgBtn;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_prescription_history_view);
    btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
    btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
//        try {
//            url = URLEncoder.encode(url, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        //prefs = getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        // hasToken= prefs.getString("hashToken", "0");
        pref = new PrefManager(this);
        hasToken=pref.getLoginUser().get("hashkey");

        webView = (WebView) findViewById(R.id.webView_pres_history_view);
        //=========================================================
        url = ApiClient.BASE_URL_entypoint+"index.php?module=HAL_Timeline&action=gallery";
    String newUrl=Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"),url);

        try {
        encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        }
        System.out.println("url in......Pres Histroy Webview ..................."+url);
        System.out.println("key in......Pres Histroy Webview ..................."+encodedHashToken);
        //  url="https://livehappy.ayubo.life/index.php?entryPoint=mobile_body_view&ref=";

        try{

        if (Utility.isInternetAvailable(PrescriptionHistoryViewActivity.this)) {

        web_prgDialog = new ProgressDialog(PrescriptionHistoryViewActivity.this);
        web_prgDialog.setCancelable(false);
        web_prgDialog.show();
        web_prgDialog.setMessage("Loading...");

        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setWebViewClient(new Callback());

        System.out.println("url in......Pres Histroy Webview ..................."+url+encodedHashToken);
        webView.loadUrl(newUrl);


        } else {

        }
        }catch(Exception e){
        System.out.println("Timeline Webview....................."+e);

        }

        }


    public class Callback extends WebViewClient {

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        super.shouldOverrideUrlLoading(view, url);
        boolean val=false;
        System.out.println("======pres=====CommonView===Redireted URL===================="+url);

        if(url.startsWith(MAIN_URL_LIVE_HAPPY + "disable_back")){
            val=true;
            return val;
        }
        else if(url.startsWith(MAIN_URL_LIVE_HAPPY + "enable_back")){
            val=true;
            return val;
        }
        else{
            val=false;
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
                Intent in = new Intent(PrescriptionHistoryViewActivity.this, WaterActivity.class);
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

