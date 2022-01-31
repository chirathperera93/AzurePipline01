package com.ayubo.life.ayubolife.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.fragments.TimeLineFragment;
import com.ayubo.life.ayubolife.health.ExpertViewActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.health.RXViewActivity;
import com.ayubo.life.ayubolife.model.NotificationObj;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.IOException;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
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
import android.widget.EditText;
import android.widget.ImageButton;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.health.ExpertViewActivity;
import com.ayubo.life.ayubolife.health.RXViewActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.utility.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;


public class NotificationDetailsViewActivity extends AppCompatActivity {
    String encodedHashToken;
    ProgressDialog web_prgDialog;
    String hasToken;

    WebView webView;
    HashMap<String, String> user = null;
    String url;
    PrefManager prefs;

    ProgressDialog progressDialog;
    // Listview Adapter

    // ArrayAdapter<String> adapter;
    ArrayList<NotificationObj> dataList = null;
    private PrefManager pref;
    // Search EditText
    Context globalContext;
    EditText inputSearch;
    String userid_ExistingUser, statusFromServiceAPI_db;
    ImageLoader imageLoader;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    ImageButton btn_backImgBtn;
    private List<NotificationObj> filteredDataM = null;
    ArrayList<HashMap<String, String>> productList;
    String notification_id, web_link, readStatus;
    String webURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_notification_details_view);


        pref = new PrefManager(NotificationDetailsViewActivity.this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        url = getIntent().getStringExtra("URL");
        notification_id = getIntent().getStringExtra("notification_id");
        readStatus = getIntent().getStringExtra("readStatus");

        webURL = url + notification_id;


        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        prefs = new PrefManager(NotificationDetailsViewActivity.this);
        hasToken = prefs.getLoginUser().get("hashkey");

        webView = (WebView) findViewById(R.id.webView_noti_details);
        //=========================================================

        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {

            if (Utility.isInternetAvailable(NotificationDetailsViewActivity.this)) {

                web_prgDialog = new ProgressDialog(NotificationDetailsViewActivity.this);
                web_prgDialog.setCancelable(false);
                web_prgDialog.show();
                web_prgDialog.setMessage("Loading...");

                WebSettings webSettings = webView.getSettings();
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);

                webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                //  webView.setWebViewClient(new Callback());

                webView.loadUrl(url);
                webView.setWebViewClient(new WebViewClient() {

                    public void onPageFinished(WebView view, String url) {
                        web_prgDialog.dismiss();

                        if (readStatus.equals("false")) {
                            clearNoti();
                        }
                    }
                });

            }
        } catch (Exception e) {
        }

    }


    public class Callback extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val = false;
            System.out.println("==============Redireted URL====================" + url);

            if (url.contains(MAIN_URL_LIVE_HAPPY + "activity_tracker")) {
                val = true;
                Intent in = new Intent(NotificationDetailsViewActivity.this, ActivityTrackerActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains(MAIN_URL_LIVE_HAPPY + "subscriptionView")) {
                val = true;
                Intent in = new Intent(NotificationDetailsViewActivity.this, RXViewActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains(MAIN_URL_LIVE_HAPPY + "medicineView")) {
                val = true;
                Intent in = new Intent(NotificationDetailsViewActivity.this, Medicine_ViewActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains(MAIN_URL_LIVE_HAPPY + "scheduleExpertView")) {
                val = true;
                Intent in = new Intent(NotificationDetailsViewActivity.this, ExpertViewActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains(MAIN_URL_APPS)) {
                val = true;
                webView.loadUrl(url);
                return val;
            } else if (url.contains(MAIN_URL_LIVE_HAPPY + "water_intake")) {
                val = true;
                Intent in = new Intent(NotificationDetailsViewActivity.this, WaterActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains(MAIN_URL_LIVE_HAPPY + "water_intake_window_call")) {
                val = true;
                Intent in = new Intent(NotificationDetailsViewActivity.this, WaterActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains(MAIN_URL_LIVE_HAPPY + "disable_back")) {
                val = true;
                return val;
            } else if (url.contains("https://livehappy.ayubo.life")) {
                val = true;
                Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
                return val;
            } else if (url.contains("https://devo.ayubo.life")) {
                val = true;
                Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
                return val;
            }
            //
            else {
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

            if (readStatus.equals("false")) {
                clearNoti();
            }

        }
    }

    private void clearNoti() {
        if (Utility.isInternetAvailable(this)) {
            updateClearNotifications task = new updateClearNotifications();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        }
    }

    private class updateClearNotifications extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            makePostRequest_updateClearNotifications();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        private void makePostRequest_updateClearNotifications() {
            //  prgDialog.show();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;

            String jsonStr =
                    "{" +
                            "\"userid\": \"" + userid_ExistingUser + "\"," +
                            "\"notification_id\": \"" + notification_id + "\"" +
                            "}";


            nameValuePair.add(new BasicNameValuePair("method", "set_user_notification_read"));
            nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));

            System.out.println("...........getCommunityListByUser............." + nameValuePair.toString());

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // log exception
                e.printStackTrace();
            }
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int r = 0;

            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) {
                r = response.getStatusLine().getStatusCode();
                if (r == 200) {
                    try {

                        JSONObject jsonObj = new JSONObject(responseString);
                        statusFromServiceAPI_db = jsonObj.optString("result").toString();


                        if (statusFromServiceAPI_db.equals("0")) {
                            statusFromServiceAPI_db = "0";
                            System.out.println(".........Notification  Read Marked............." + statusFromServiceAPI_db);

                        } else {
                            statusFromServiceAPI_db = "55";
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(".........Notification  Read Marked  Error............." + statusFromServiceAPI_db);

                    statusFromServiceAPI_db = "999";
                }
            }

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
                    Intent in = new Intent(NotificationDetailsViewActivity.this, WaterActivity.class);
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
