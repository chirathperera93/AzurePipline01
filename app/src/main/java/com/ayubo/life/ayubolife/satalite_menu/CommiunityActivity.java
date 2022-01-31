package com.ayubo.life.ayubolife.satalite_menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.ActivityTrackerActivity;
import com.ayubo.life.ayubolife.activity.BodyViewActivity;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.QRWebViewActivity;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.activity.WorkoutActivity;
import com.ayubo.life.ayubolife.health.ExpertViewActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.health.RXViewActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class CommiunityActivity extends AppCompatActivity {
    private PrefManager pref;

    String encodedHashToken;
    ProgressDialog web_prgDialog;
    String hasToken;
    ImageButton btn_backImgBtn;
   // SwipeRefreshLayout mySwipeRefreshLayout;
    WebView webView;
    HashMap<String, String> user=null;
    String url;LinearLayout lay_btnBack;
    private void localConstructor(){
        pref = new PrefManager(CommiunityActivity.this);
        user=pref.getLoginUser();
        hasToken=user.get("hashkey");
    }
    public void goBackToHome(){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_commiunity);


        pref = new PrefManager(CommiunityActivity.this);

        hasToken=pref.getLoginUser().get("hashkey");

        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("=========go Back to Home===========");
//                Intent i = new Intent(v.getContext(), NewHomeWithSideMenuActivity.class);
//                startActivity(i);
                goBackToHome();
            }
        });


        lay_btnBack=(LinearLayout)findViewById(R.id.lay_btnBack);
        lay_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(v.getContext(), NewHomeWithSideMenuActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(i);
                goBackToHome();
                // do something when the corky is clicked
            }
        });


        localConstructor();

        if(hasToken==null){}else {
            try {
                encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            webView = (WebView) findViewById(R.id.webView_coporate);

            url = ApiClient.BASE_URL + "index.php?module=Accounts&action=findCorporate";

            String newUrl=Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"),url);
       //     BASE_URL
            if (Utility.isInternetAvailable(CommiunityActivity.this)) {

                web_prgDialog = new ProgressDialog(CommiunityActivity.this);
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
                webView.requestFocus(View.FOCUS_DOWN);
                webView.clearCache(true);
                webView.loadUrl(newUrl);
//                webView.setWebViewClient(new WebViewClient() {
//
//                    public void onPageFinished(WebView view, String url) {
//                        web_prgDialog.cancel();
//
//                    }
//                });


            }

        }}

    public class Callback extends WebViewClient{

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val=false;

            System.out.println("========Commiuinity===Redireted URL===================="+url);

            if(url.contains("/disable_back")){
                btn_backImgBtn.setVisibility(View.GONE);
                val=true;
                return val;
            }

            else if(url.contains("/enable_back")){
                btn_backImgBtn.setVisibility(View.VISIBLE);
                val=true;
                return val;
            }
            else if(url.contains("findCorporate")){
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
