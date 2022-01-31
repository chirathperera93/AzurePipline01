package com.ayubo.life.ayubolife.map_challenges;

import static com.ayubo.life.ayubolife.payment.ConstantsKt.EXTRA_CHALLANGE_ID;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonView_Second_WebviewActivity;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.ContactDetailsActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.body.MyHealthDataActivity;
import com.ayubo.life.ayubolife.common.SetDiscoverPage;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.health.ExpertViewActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.health.RXViewActivity;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.map_challange.MapChallangeKActivity;
import com.ayubo.life.ayubolife.model.JoinChallengeDataObj;
import com.ayubo.life.ayubolife.model.JoinChallengeObj;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class MapJoinChallenge_Activity extends AppCompatActivity {
    Button btn_join_challenge;
    ImageButton btn_backImgBtn;
    ProgressDialog prgDialog;

    String total_steps, cityJsonString;
    PrefManager pref;
    WebView webView;
    String userid_ExistingUser, setDeviceID_Success, today, serviceDataStatus, challenge_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_join_challenge_);


        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");

        prgDialog = new ProgressDialog(MapJoinChallenge_Activity.this);
        prgDialog.setCancelable(false);
        challenge_id = getIntent().getStringExtra("challenge_id");
        total_steps = getIntent().getStringExtra("steps");


        System.out.println("=======challenge_id============" + challenge_id);


        webView = (WebView) findViewById(R.id.webView_join_challange);

        try {

            if (Utility.isInternetAvailable(MapJoinChallenge_Activity.this)) {

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


            } else {

            }
        } catch (Exception e) {
            System.out.println("Timeline Webview....................." + e);

        }


        btn_join_challenge = (Button) findViewById(R.id.btn_join_challenge);
        btn_join_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Service_Service_getStepsData_ServiceCall();
            }
        });


        // Main  Service Calling ...
        loadJoinChallengeDataFromServer();
        // Main  Service Calling ...

    }


    void loadJoinChallengeDataFromServer() {
        prgDialog.show();
        String serviceName = "adventure_challenge_info";
        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"," +
                        "\"challenge_id\": \"" + challenge_id + "\"" +
                        "}";


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JoinChallengeObj> call = apiService.getJoinChallenge(AppConfig.APP_BRANDING_ID, serviceName, "JSON", "JSON", jsonStr);
        call.enqueue(new retrofit2.Callback<JoinChallengeObj>() {
            @Override
            public void onResponse(Call<JoinChallengeObj> call, Response<JoinChallengeObj> response) {
                prgDialog.cancel();
                if (response.isSuccessful()) {

                    JoinChallengeObj mainResponse = response.body();
                    JoinChallengeDataObj obj = mainResponse.getData();

                    if (obj != null) {
                        displayView(obj);
                    }
                }
            }

            @Override
            public void onFailure(Call<JoinChallengeObj> call, Throwable t) {
                prgDialog.cancel();


                Toast.makeText(MapJoinChallenge_Activity.this, "Server is temporarily unavailable", Toast.LENGTH_LONG).show();

            }
        });
    }

    void displayView(JoinChallengeDataObj obj) {

        String imageURL = obj.getBannerImg();
        String title = obj.getTitle();
        String html = obj.getDescription();

        final String mimeType = "text/html";
        final String encoding = "UTF-8";


        webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
        TextView txt_challenge_heading = findViewById(R.id.txt_challenge_heading);
        txt_challenge_heading.setText(title);

        ImageView challenge_image = findViewById(R.id.challenge_image);


        Glide.with(getApplicationContext()).load(imageURL)
                .into(challenge_image);


    }


    public class Callback extends WebViewClient {
        boolean timeout = true;

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            String perURL = url;
            boolean perURL_flag = false;
            boolean val = false;

            System.out.println("=============================");
            System.out.println("=========start of comon web view Callback====================");
            System.out.println("=============================");


            System.out.println("===========CommonView===Redireted URL====================" + url);
            if (url.contains(MAIN_URL_APPS)) {
                if (url.contains("openInView=true")) {
                    val = true;
                    Intent intent = new Intent(MapJoinChallenge_Activity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    return val;
                } else if (url.contains("openInBrowser=true")) {
                    val = true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                } else if (url.contains("group_adventure_invite")) {
                    val = true;
                    Intent intent = new Intent(MapJoinChallenge_Activity.this, ContactDetailsActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    return val;
                } else if (url.contains("enable_proximity")) {
                    //   v.vibrate(1500);
                    return true;
                } else {
                    val = false;
                    return val;
                }
            }

            if (url.contains("/disable_back")) {
                btn_backImgBtn.setVisibility(View.GONE);
                val = true;
                return val;
            } else if (url.contains("/enable_back")) {
                btn_backImgBtn.setVisibility(View.VISIBLE);
                val = true;
                return val;
            } else if (url.contains("openInBrowser=true")) {
                val = true;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return val;
            } else if (url.contains("openmedicaltrack")) {
                val = true;
                Intent intent = new Intent(MapJoinChallenge_Activity.this, MyHealthDataActivity.class);
                startActivity(intent);
                return val;
            } else if (url.contains("group_adventure_invite")) {
                val = true;
                Intent intent = new Intent(MapJoinChallenge_Activity.this, ContactDetailsActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
                return val;
            } else if (url.contains("openInSame=true")) {
                val = false;
                return val;
            } else if (url.contains("soloAdventureMap")) {


            } else if (url.contains("PC_Hospitals&action=connect_user&type=mobile")) {
                System.out.println("========CommonView_Second_WebviewActivity===Redireted URL====================" + url);
                val = true;
                Intent intent = new Intent(getBaseContext(), CommonView_Second_WebviewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
                return val;
            } else if (url.contains("action=setFamilyMember")) {
                val = true;
                Intent intent = new Intent(getBaseContext(), CommonView_Second_WebviewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
                return val;
            } else if (url.contains("sendToBaseView")) {
                val = true;
                pref.setFromSendToBaseView("yes");
//                Intent intent = new Intent(getBaseContext(), NewHomeWithSideMenuActivity.class);
//                Intent intent = new Intent(getBaseContext(), LifePlusProgramActivity.class);
//                Intent intent = new Intent(getBaseContext(), NewDiscoverActivity.class);
                Intent intent = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
                startActivity(intent);

                return val;
            } else if (url.contains("water_intake")) {
                val = true;
                Intent in = new Intent(MapJoinChallenge_Activity.this, WaterActivity.class);
                startActivity(in);
                return val;
            } else {
                val = true;
                Intent intent = new Intent(MapJoinChallenge_Activity.this, CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
                return val;
            }


            if (url.contains("disable_back")) {
                btn_backImgBtn.setVisibility(View.GONE);
            } else {
                btn_backImgBtn.setVisibility(View.VISIBLE);
            }


            if (url.contains("subscriptionView")) {
                val = true;
                Intent in = new Intent(MapJoinChallenge_Activity.this, Medicine_ViewActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains("medicineView")) {
                val = true;
                Intent in = new Intent(MapJoinChallenge_Activity.this, RXViewActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains("scheduleExpertView")) {
                val = true;
                Intent in = new Intent(MapJoinChallenge_Activity.this, ExpertViewActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains(MAIN_URL_APPS)) {
                val = true;
                webView.loadUrl(url);
                return val;
            } else if (url.contains("/water_intake")) {
                val = true;
                Intent in = new Intent(MapJoinChallenge_Activity.this, WaterActivity.class);
                startActivity(in);
                finish();
                return val;
            } else if (url.contains("/water_intake_window_call")) {
                val = true;
                Intent in = new Intent(MapJoinChallenge_Activity.this, WaterActivity.class);
                startActivity(in);
                return val;
            } else if (url.contains("/disable_back")) {
                btn_backImgBtn.setVisibility(View.GONE);
                val = true;
                return val;
            } else if (url.contains("/enable_back")) {
                btn_backImgBtn.setVisibility(View.VISIBLE);
                val = true;
                return val;
            } else if (url.contains("openInBrowser=true")) {
                val = true;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return val;
            } else if (url.contains(ApiClient.BASE_URL)) {
                val = true;
                Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
                return val;
            }


            //     https://livehappy.ayubo.life/index.php?entryPoint=mobile_program_list
            else {
                val = true;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return val;
            }

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Runnable run = new Runnable() {
                public void run() {
                    if (timeout) {
                        // do what you want
                        System.out.println("============timeout================");
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
            };
            Handler myHandler = new Handler(Looper.myLooper());
            myHandler.postDelayed(run, 30000);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            System.out.println("============onPageFinished================");
            timeout = false;

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

    }


    private void Service_Service_getStepsData_ServiceCall() {

        if (Utility.isInternetAvailable(MapJoinChallenge_Activity.this)) {

            prgDialog.show();
            prgDialog.setMessage("Joining challenge..");
            prgDialog.setCancelable(false);
            Service_getStepsData task = new Service_getStepsData();
            task.execute(new String[]{ApiClient.BASE_URL_live_v6});
        } else {
        }
    }

    private class Service_getStepsData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            System.out.println("========Service Calling============0");
            makeSetDefaultDevice();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (prgDialog != null) {
                prgDialog.cancel();
            }


            if (cityJsonString != null) {
                writeToFile(cityJsonString);
            }


            //  FirebaseAnalytics Adding
            Bundle bPromocode_used = new Bundle();
            bPromocode_used.putString("challenge", challenge_id);
            if (SplashScreen.firebaseAnalytics != null) {
                SplashScreen.firebaseAnalytics.logEvent("Challenge_joined", bPromocode_used);
            }

            Intent intent = new Intent(MapJoinChallenge_Activity.this, MapChallangeKActivity.class);
            intent.putExtra(EXTRA_CHALLANGE_ID, challenge_id);
            startActivity(intent);

            pref.setIsFromJoinChallenge(true);
//            MapChallangesServices serviceObj = new MapChallangesServices(MapJoinChallenge_Activity.this, challenge_id);
//            serviceObj.Service_getChallengeMapData_ServiceCall();
            finish();

        }
    }

    private void writeToFile(String result) {
        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(challenge_id + ".json", Context.MODE_PRIVATE));
            outputStreamWriter.write(result);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    JSONArray roadsArry = null;

    private void makeSetDefaultDevice() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live_v6);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String email, password;

        //  email = lusername;
        //  password = lpassword;


        String jsonStr =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"," +
                        "\"challenge_id\": \"" + challenge_id + "\"" +
                        "}";

        //   challenge_id

        nameValuePair.add(new BasicNameValuePair("method", "join_adventure_challenge"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............getActivityDetails..................." + nameValuePair.toString());
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
            System.out.println("........getActivityDetails..response..........." + response);
            System.out.println("========Service Calling============0");

            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());

            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject jsonObj = new JSONObject(responseString);

            String res = jsonObj.optString("result").toString();

            int result = Integer.parseInt(res);

            if (result == 0) {
                cityJsonString = jsonObj.optString("json").toString();

                try {

                    JsonObject jsonElement = new JsonParser().parse(cityJsonString).getAsJsonObject();

                    JSONArray jsArray = new JSONArray();
                    for (int i = 0; i < jsonElement.get("route").getAsJsonArray().size(); i++) {
                        jsArray.put(jsonElement.get("route").getAsJsonArray().get(i));
                    }

                    roadsArry = jsArray;


                    writeToFile(roadsArry.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {

                serviceDataStatus = "99";
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void writeJSONtoFile(String fileName, String strData) {
        try {
            Charset charset = StandardCharsets.UTF_8;
            byte[] byteArrray = strData.getBytes(charset);
            MapJoinChallenge_Activity.this.openFileOutput(fileName, Context.MODE_PRIVATE).write(byteArrray);

//            this@MapChallangeKActivity.openFileOutput(fileName, Context.MODE_PRIVATE).use {
//                it.write(strData.toByteArray())
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
