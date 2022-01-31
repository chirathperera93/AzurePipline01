package com.ayubo.life.ayubolife.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.map_challenges.PanaramaView_NewActivity;
import com.ayubo.life.ayubolife.map_challenges.PanoramaView_LawDevices_Activity;
import com.ayubo.life.ayubolife.model.RoadLocationObj;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;

public class TemplateViewActivity extends AppCompatActivity {
    String noof_day, strstep;
    ArrayList<RoadLocationObj> myTagList = null;
    ArrayList<RoadLocationObj> myTreasure = null;
    Button btn_join_challenge;
    ImageButton btn_backImgBtn;
    ProgressDialog prgDialog;
    boolean gyroExists;
    String userid_ExistingUser, view_id, type, serviceDataStatus, challenge_id;
    String html, image;
    ImageLoader imageLoader;

    int total_steps_int;
    String setDeviceID_Success, today;
    String total_steps, cityJsonString, url;
    ArrayList<RoadLocationObj> myTreasureList = null;
    PrefManager pref;
    WebView webView;
    ProgressDialog web_prgDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_template_view);

        PackageManager packageManager = getPackageManager();
        gyroExists = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);

        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");

        prgDialog = new ProgressDialog(TemplateViewActivity.this);
        url = getIntent().getStringExtra("URL");

        challenge_id = getIntent().getStringExtra("challenge_id");
        total_steps = getIntent().getStringExtra("steps");
        url = getIntent().getStringExtra("URL");
        myTreasureList = new ArrayList<RoadLocationObj>();
        noof_day = getIntent().getStringExtra("noof_day");

        LinearLayout btn_backImgBtn_layout = findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageButton btn_back_Button = (ImageButton) findViewById(R.id.btn_back_Button);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String[] output = url.split("_");
        int size = output.length;
        if (size == 3) {

            view_id = output[1];
            type = output[2];

        } else if (size == 2) {

            view_id = output[1];
            type = "";

        }

        view_id = view_id.replaceAll("\\s+", "");

        // System.out.println("=========view_id=========="+view_id);

        Service_loadChallangeMap_ServiceCall();

        webView = (WebView) findViewById(R.id.webView_complete_challange);


    }

    private void Service_loadChallangeMap_ServiceCall() {

        if (Utility.isInternetAvailable(TemplateViewActivity.this)) {

            prgDialog.show();
            prgDialog.setMessage("Loading..");
            prgDialog.setCancelable(false);
            Service_loadChallangeMap task = new Service_loadChallangeMap();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
        }
    }

    private class Service_loadChallangeMap extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            System.out.println("========Service Calling============0");
            makeChallangeMap();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("========Service Calling============6");
            prgDialog.cancel();

            if (serviceDataStatus.equals("0")) {

                ProgressBar progressNewsList = (ProgressBar) findViewById(R.id.progressNewsList);
                NetworkImageView bg_image = (NetworkImageView) findViewById(R.id.main_bg_banner);

                if (imageLoader == null)
                    imageLoader = App.getInstance().getImageLoader();
                bg_image.setImageUrl(image, imageLoader);
                loadImage(image, bg_image, progressNewsList);


                try {

                    if (Utility.isInternetAvailable(TemplateViewActivity.this)) {

                        web_prgDialog = new ProgressDialog(TemplateViewActivity.this);
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
                        webView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                return true;
                            }
                        });
                        webView.loadData(html, "text/html; charset=utf-8", "UTF-8");


                    } else {

                    }
                } catch (Exception e) {
                    System.out.println("Timeline Webview....................." + e);

                }


            }

        }
    }

    private void makeChallangeMap() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        serviceDataStatus = "99";

        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"," +
                        "\"view_id\": \"" + view_id + "\"," +
                        "\"type\": \"" + type + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "htmlNativeView"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));

        System.out.println("..............htmlNativeView................." + nameValuePair.toString());
        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);


            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());

            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(responseString);
                String res = jsonObj.optString("result");

                int result = Integer.parseInt(res);

                if (result == 0) {
                    serviceDataStatus = "0";
                    html = jsonObj.optString("html");
                    image = jsonObj.optString("image");
                    System.out.println("========image===========" + image);
                } else {
                    System.out.println("========Service Calling============10");
                    serviceDataStatus = "99";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (ClientProtocolException e) {
            System.out.println("========Service Calling============7");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("========Service Calling============8");
            e.printStackTrace();
        }

    }


    public class Callback extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val = false;
            String rediredtedUrl = url;
            String Str = new String(url);
            System.out.println(Str.matches("activity_tracker"));
            System.out.println("==========Template View===Redireted URL====================" + url);

            //   https://livehappy.ayubo.life/index.php?entryPoint=activity_tracker&id=1d29596b-9771-3c41-724e-5937c8878a2b&name=RUNNING

            if (url.contains("panaroma")) {

                if (gyroExists) {
                    Intent intent = new Intent(TemplateViewActivity.this, PanaramaView_NewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(TemplateViewActivity.this, PanoramaView_LawDevices_Activity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                val = true;
                return val;
                // finish();
            } else if (url.contains("soloAdventureMap")) {

                String[] parts = url.split("_");
                challenge_id = parts[1];

                //    Service_loadChallangeMap_ServiceCall();
                val = true;
                return val;
            } else if (url.contains(ApiClient.BASE_URL_entypoint + "activity_tracker")) {
                val = true;
                System.out.println("========Inside Tracker=================" + url);
                String Str2;
                String[] parts = url.split("&");
                String idData = parts[1];
                String nameData = parts[2];
                System.out.println("======sss=====0==========" + idData);
                System.out.println("======sss=====00==========" + nameData);

                String[] idArrya = idData.split("=");
                String activityId = idArrya[1];

                String[] nameArrya = nameData.split("=");
                String activityName = nameArrya[1];

                System.out.println("============activityName========" + activityName);
                System.out.println("============activityId========" + activityId);
                Intent intent = new Intent(getBaseContext(), ActivityTrackerActivity.class);
                intent.putExtra("activityName", activityName);
                intent.putExtra("activityId", activityId);
                startActivity(intent);


                System.out.println("=========Activity Started with Live===================");
                return val;
            } else if (url.contains(MAIN_URL_LIVE_HAPPY + "workout_start")) {
                val = true;

                return val;
            }
            if (url.contains(MAIN_URL_LIVE_HAPPY + "activity_tracker")) {
                val = true;

                return val;
            } else if (url.contains("/subscriptionView")) {
                val = true;

                return val;
            } else if (url.contains("/medicineView")) {
                val = true;

                return val;
            } else if (url.contains("/scheduleExpertView")) {
                val = true;

                return val;
            } else if (url.contains(MAIN_URL_APPS)) {
                val = true;
                System.out.println("==============Redireted URL====================" + url);
                //  url="https://www.w3schools.com/";
                webView.loadUrl(url);
                return val;
            } else if (url.contains("/water_intake")) {
                val = true;

                return val;
            } else if (url.contains("/water_intake_window_call")) {
                val = true;

                return val;
            } else if (url.contains("/disable_back")) {
                val = true;
                return val;
            } else if (url.contains("/enable_back")) {
                val = true;
                return val;
            } else if (url.contains("https://livehappy.ayubo.life")) {
                val = true;
                Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);

                return val;
            } else {
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
        }
    }


    private void loadImage(String imageUrl, final com.android.volley.toolbox.NetworkImageView imageView, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        if (imageUrl != null) {
            imageLoader.get(imageUrl, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setImageBitmap(response.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    //	Log.e("onErrorResponse ", error.toString());
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }


}
