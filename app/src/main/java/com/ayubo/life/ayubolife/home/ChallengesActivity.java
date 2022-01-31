package com.ayubo.life.ayubolife.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.fragments.Challenges_Fragment;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.google.android.gms.common.api.Api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ChallengesActivity extends AppCompatActivity {
    private ImageView viewPager;
    String hasToken, loginType;
    // SwipeRefreshLayout mySwipeRefreshLayout;
    Toast toastt;  SwipeRefreshLayout mySwipeRefreshLayout;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;
    ImageButton btn_signout;
    TextView btn_Appointment;
    WebView webView;
    SharedPreferences prefs;
    String userid_ExistingUser;

    String encodedHashToken,statusFromServiceAPI_db;
    int result;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String url=null;ImageButton  boBack;
    PrefManager pref;
    ImageButton btn_backImgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);


        pref = new PrefManager(ChallengesActivity.this);
        prefs =getSharedPreferences("ayubolife", Context.MODE_PRIVATE);


        userid_ExistingUser=pref.getLoginUser().get("uid");
        hasToken=pref.getLoginUser().get("hashkey");


        inflatert = (LayoutInflater)ChallengesActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup)findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(ChallengesActivity.this);
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);
        Ram.setIsOne_Medicine(true);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Ram.setIsOne_Medicine(true);
        Ram.setIsTwo_Medicine(false);
      //  String url=ApiClient.BASE_URL_live;
        url = ApiClient.BASE_URL+ "index.php?entryPoint=joinChallenge#teamView";

        if (Utility.isInternetAvailable(ChallengesActivity.this)) {
//            progressBar = new ProgressDialog(getContext());
//            progressBar.show();
//            progressBar.setMessage("Loading Challeng data...");
            webView = (WebView) findViewById(R.id.webView_challenges);
            webView.getSettings().setJavaScriptEnabled(true);

            webView.getSettings().setDomStorageEnabled(true);
            webView.requestFocus(View.FOCUS_DOWN);
            webView.loadUrl(url + encodedHashToken);
            webView.setWebViewClient(new WebViewController());

            mySwipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                            mySwipeRefreshLayout.setRefreshing(false);

                        }
                    }
            );


            webView.setWebViewClient(new WebViewClient() {
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
                    //progressBar.dismiss();
                }
            });


        } else {
            textt.setText("Unable to detect an active internet connection");
            toastt.setView(layoutt);
            toastt.show();

        }
        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }

    public class WebViewController extends WebViewClient {
        @Override
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
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}
