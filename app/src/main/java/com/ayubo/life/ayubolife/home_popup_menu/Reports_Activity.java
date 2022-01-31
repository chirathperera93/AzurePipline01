package com.ayubo.life.ayubolife.home_popup_menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.body.MyHealthDataActivity;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.health.Health_MainActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.health.RXViewActivity;
import com.ayubo.life.ayubolife.mind.Mind_MainActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;

public class Reports_Activity extends AppCompatActivity {
    String hasToken, loginType;
    SwipeRefreshLayout mySwipeRefreshLayout;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;
    ImageButton btn_signout;
    TextView btn_Appointment,header_menu_back;
    WebView webView;
    SharedPreferences prefs;
    String userid_ExistingUser,url;
    DatabaseHandler db;
    String encodedHashToken,statusFromServiceAPI_db;
    int result;
    private PrefManager pref;
    ProgressDialog webProgress_timeline;
    ImageButton btn_backImgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_);

        System.out.println(userid_ExistingUser + "============MedicalReports_Fragment=======onCreateView");
        prefs = getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        userid_ExistingUser= prefs.getString("uid", "0");
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        db = DatabaseHandler.getInstance(Reports_Activity.this);

        Ram.setIsOne_Medicine(true);
        Ram.setIsTwo_Medicine(false);
        inflatert = (LayoutInflater) Reports_Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(Reports_Activity.this);
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);

//        header_menu_back=(TextView) findViewById(R.id.header_menu_back);
//        header_menu_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                finish();
//            }
//        });


        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        prefs = getSharedPreferences("ayubolife", Context.MODE_PRIVATE);


        pref = new PrefManager(Reports_Activity.this);
        hasToken=pref.getLoginUser().get("hashkey");

        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Ram.setIsOne_Medicine(true);
        Ram.setIsTwo_Medicine(false);

       // url = ApiClient.BASE_URL_entypoint+"ayuboLifeTimelineLogin&type=mobileMedicalHistory&ref=";

        url = ApiClient.BASE_URL + "index.php?entryPoint=mobileMedicalHistory";
        String newUrl=Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"),url);


        if (Utility.isInternetAvailable(Reports_Activity.this)) {

            webProgress_timeline = new ProgressDialog(Reports_Activity.this);
            webProgress_timeline.show();
            webProgress_timeline.setMessage("Loading...");

            webView = (WebView) findViewById(R.id.webView_medicalreports);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setDomStorageEnabled(true);

            webView.setWebViewClient(new Callback());

            webView.requestFocus(View.FOCUS_DOWN);
            webView.loadUrl(newUrl);
            mySwipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                            mySwipeRefreshLayout.setRefreshing(false);

                        }
                    }
            );

        } else {
            textt.setText("Unable to detect an active internet connection");
            toastt.setView(layoutt);
            toastt.show();
        }

    }


    public class Callback extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val=false;

            System.out.println("===========MedicalReports_Fragment===Callback Redireted URL===================="+url);

            if(url.contains("index.php?entryPoint=ayuboLifeTimeline")){
                val=false;
                return val;
            }

            else if(url.contains(MAIN_URL_APPS)){
                if(url.contains("openInView=true")){
                    val=true;
                    Intent intent = new Intent(Reports_Activity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    return val;
                }
                else if(url.contains("openInBrowser=true")){
                    val=true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                }
                else{
                    val=false;
                    return val;
                }
            }

            else if(url.contains("subscriptionView")){
                val=true;
                Intent in = new Intent(Reports_Activity.this, RXViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains("medicineView")){
                val=true;
                Intent in = new Intent(Reports_Activity.this, Medicine_ViewActivity.class);
                startActivity(in);
                return val;
            }

            else if(url.contains("https://livehappy.ayubo.life")){
                 if(url.contains("openmedicaltrack")){
                    val=true;
                    Intent intent = new Intent(Reports_Activity.this, MyHealthDataActivity.class);
                    startActivity(intent);
                    return val;
                }
                 else if(url.contains("openInBrowser=true")){
                    val=true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                }
                else if(url.contains("openInSame=true")){
                    val=false;
                    return val;
                }
                else if(url.contains("index.php?entryPoint=mobile_mind_view")){
                    val=true;
                    //  Intent in = new Intent(getContext(), MindViewActivity.class);
                    Intent in = new Intent(Reports_Activity.this, Mind_MainActivity.class);
                    startActivity(in);
                    return val;
                }


                else if(url.contains("index.php?entryPoint=mobile_health_view")){
                    val=true;
                    Intent in = new Intent(Reports_Activity.this, Health_MainActivity.class);
                    //  Intent in = new Intent(getContext(), CommonTabActivity.class);

                    startActivity(in);
                    return val;
                }
                else if(url.contains("subscriptionView")){
                    val=true;
                    Intent in = new Intent(Reports_Activity.this, RXViewActivity.class);
                    startActivity(in);
                    return val;
                }
                else if(url.contains("medicineView")){
                    val=true;
                    Intent in = new Intent(Reports_Activity.this, Medicine_ViewActivity.class);
                    startActivity(in);
                    return val;
                }
                else if(url.contains("mobileMedicalHistory")){
//                    val=true;
//                    Intent intent = new Intent(Reports_Activity.this, CommonWebViewActivity.class);
//                    intent.putExtra("URL", url);
//                    startActivity(intent);
//                    return val;
                    System.out.println("==========mobileMedicalHistory=================="+url);
                    val=false;
                    return val;

                }
                else if(url.contains("index.php?entryPoint=ayuboLifeTimeline")){
                    val=false;
                    return val;
                }
                else if(url.contains("initChatMobile")){
                    val=true;
                    Intent intent = new Intent(Reports_Activity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                }
                else if(url.contains("water_intake_window_call")){
                    val=true;
                    Intent in = new Intent(Reports_Activity.this, WaterActivity.class);
                    startActivity(in);
                    return val;
                }
                else if(url.contains(ApiClient.BASE_URL)){
                    val=true;
                    System.out.println("=========iN======CommonWebViewActivity========="+url);
                    Intent intent = new Intent(Reports_Activity.this, CommonWebViewActivity.class);
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
            else if(url.contains("https://devo.ayubo.life")){
                if(url.contains("openmedicaltrack")){
                    val=true;
                    Intent intent = new Intent(Reports_Activity.this, MyHealthDataActivity.class);
                    startActivity(intent);
                    return val;
                }
                else if(url.contains("openInBrowser=true")){
                    val=true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                }
                else if(url.contains("openInSame=true")){
                    val=false;
                    return val;
                }
                else if(url.contains("index.php?entryPoint=mobile_mind_view")){
                    val=true;
                    //  Intent in = new Intent(getContext(), MindViewActivity.class);
                    Intent in = new Intent(Reports_Activity.this, Mind_MainActivity.class);
                    startActivity(in);
                    return val;
                }

                else if(url.contains("index.php?entryPoint=mobile_health_view")){
                    val=true;
                    Intent in = new Intent(Reports_Activity.this, Health_MainActivity.class);
                    //  Intent in = new Intent(getContext(), CommonTabActivity.class);

                    startActivity(in);
                    return val;
                }
                else if(url.contains("subscriptionView")){
                    val=true;
                    Intent in = new Intent(Reports_Activity.this, RXViewActivity.class);
                    startActivity(in);
                    return val;
                    //subscriptionView
//                    val=false;
//                    return val;
                }
                else if(url.contains("medicineView")){
                    val=true;
                    Intent in = new Intent(Reports_Activity.this, Medicine_ViewActivity.class);
                    startActivity(in);
                    return val;
                    //subscriptionView
//                    val=false;
//                    return val;
                }
//                else if(url.contains("subscriptionView")){
//                    val=false;
//                    return val;
//                }
                else if(url.contains("index.php?entryPoint=mobileMedicalHistory")){

                    if(url.contains("openInView=true")){
                        val=true;
                        Intent intent = new Intent(Reports_Activity.this, CommonWebViewActivity.class);
                        intent.putExtra("URL", url);
                        startActivity(intent);

                        return val;
                    }else{
                        val=false;
                        return val;
                    }
                }
                else if(url.contains("index.php?entryPoint=ayuboLifeTimeline")){
                    val=false;
                    return val;
                }
                else if(url.contains("initChatMobile")){
                    val=true;
                    Intent intent = new Intent(Reports_Activity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                }
                else if(url.contains("water_intake_window_call")){
                    val=true;
                    Intent in = new Intent(Reports_Activity.this, WaterActivity.class);
                    startActivity(in);
                    return val;
                }
                else if(url.contains(ApiClient.BASE_URL)){
                    val=true;
                    System.out.println("=========iN======CommonWebViewActivity========="+url);
                    Intent intent = new Intent(Reports_Activity.this, CommonWebViewActivity.class);
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

            else if(url.contains("disable_back")){
                val=true;
                return val;
            }

            else if(url.contains("subscriptionView")){
                val=false;
                return val;
            }
            else if(url.contains("index.php?entryPoint=mobileMedicalHistory")){

                if(url.contains("openInView=true")){
                    val=true;
                    Intent intent = new Intent(Reports_Activity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                }else{
                    val=false;
                    return val;
                }
            }


            else if(url.contains("index.php?entryPoint=ayuboLifeTimeline")){
                val=false;
                return val;
            }
            else if(url.contains("initChatMobile")){
                val=true;
                Intent intent = new Intent(Reports_Activity.this, CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);

                return val;
            }
            else if(url.contains(MAIN_URL_APPS)){
                val=true;
                System.out.println("==============Redireted URL===================="+url);
                //  url="https://www.w3schools.com/";
                webView.loadUrl(url);
                return val;
            }
            else if(url.contains("water_intake_window_call")){
                val=true;
                Intent in = new Intent(Reports_Activity.this, WaterActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains(ApiClient.BASE_URL)){
                val=true;
                System.out.println("=========iN======CommonWebViewActivity========="+url);
                Intent intent = new Intent(Reports_Activity.this, CommonWebViewActivity.class);
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
            if(webProgress_timeline!=null){
                webProgress_timeline.dismiss();
            }


        }
    }

}
