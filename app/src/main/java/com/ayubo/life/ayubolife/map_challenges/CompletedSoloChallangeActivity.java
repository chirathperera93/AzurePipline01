package com.ayubo.life.ayubolife.map_challenges;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.ActivityTrackerActivity;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.activity.WorkoutActivity;
import com.ayubo.life.ayubolife.challenges.NewCHallengeActivity;
import com.ayubo.life.ayubolife.fragments.Challenges_Fragment;
import com.ayubo.life.ayubolife.health.ExpertViewActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.health.RXViewActivity;
import com.ayubo.life.ayubolife.model.RoadLocationObj;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;


public class CompletedSoloChallangeActivity extends AppCompatActivity {
    ArrayList<RoadLocationObj> myTreasure=null;
    ProgressDialog prgDialog;
    int total_steps_int;String  service_checkpoints,enabled_checkpoints,weekSteps;
    String userid_ExistingUser,url,noof_day,treatures,serviceDataStatus,challenge_id,showpopup, tip_icon,tip,tipheading,tip_header_1,tip_header_2,tip_type,tip_meta;
    PrefManager pref; WebView webView; ProgressDialog web_prgDialog;



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                // do something here
                Intent intent = new Intent(this, MapChallengeActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MapChallengeActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_solo_challange);

        pref = new PrefManager(this);
        userid_ExistingUser=pref.getLoginUser().get("uid");
        prgDialog=new ProgressDialog(CompletedSoloChallangeActivity.this);
        webView = (WebView) findViewById(R.id.webView_complete_challange);

        challenge_id = getIntent().getStringExtra("challenge_id");
        url = getIntent().getStringExtra("URL");

        try{
            if (Utility.isInternetAvailable(CompletedSoloChallangeActivity.this)) {
                web_prgDialog = new ProgressDialog(CompletedSoloChallangeActivity.this);
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


                String newUrl=Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"),url);

                webView.loadUrl(newUrl);


            } else {

            }
        }catch(Exception e){
            System.out.println("Timeline Webview....................."+e);
        }
    }



    private void Service_loadChallangeMap_ServiceCall() {

        if (Utility.isInternetAvailable(CompletedSoloChallangeActivity.this)) {

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

            String cityFile= loadJSONFromAssetNew(com.ayubo.life.ayubolife.webrtc.App.getInstance());
            if(cityFile==null){

              //  Service_getChallengeJson_ServiceCall();
            }else {

                Intent intent = new Intent(CompletedSoloChallangeActivity.this, MapChallengeActivity.class);
                intent.putExtra("challenge_id", challenge_id);
                intent.putExtra("steps", Integer.toString(total_steps_int));


                startActivity(intent);
                finish();
            }
        }
    }
    private void makeChallangeMap() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

        String jsonStr =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"," +
                        "\"challenge_id\": \"" + challenge_id + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "getChallengeSteps"));
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

            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());

            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObj = null;

            try {
                jsonObj = new JSONObject(responseString);
                String res = jsonObj.optString("result").toString();

                int result = Integer.parseInt(res);

                if (result == 0) {
                    System.out.println("========Service Calling============4");
                    try {
                        myTreasure=new ArrayList<RoadLocationObj>();
                        serviceDataStatus = "0";
                        if(myTreasure!=null && myTreasure.size() > 0){
                            myTreasure.clear();
                        }
                        myTreasure=new ArrayList<RoadLocationObj>();

                        String data = jsonObj.optString("data").toString();
                        JSONObject jsonData = new JSONObject(data);
                        //Map Data====================================
                        service_checkpoints = jsonData.optString("service_checkpoints").toString();
                        if(service_checkpoints.equals("true")){
                            enabled_checkpoints = jsonData.optString("enabled_checkpoints").toString();
                        }else{
                            service_checkpoints="";
                            enabled_checkpoints="";
                        }
                        weekSteps = jsonData.optString("weekSteps").toString();


                        String counter = jsonData.optString("counter").toString();
                        noof_day = jsonData.optString("day").toString();
                        String treatures = jsonData.optString("treatures").toString();

                        total_steps_int=Integer.parseInt(counter);

                        JSONArray myDataListsAll= null;
                        try {
                            myDataListsAll = new JSONArray(treatures);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for(int i=0;i<myDataListsAll.length();i++) {

                            JSONObject childJson = null;
                            try {
                                childJson = (JSONObject) myDataListsAll.get(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String latp = childJson.optString("latp").toString();
                            String longp = childJson.optString("longp").toString();
                            String steps = childJson.optString("steps").toString();
                            String objimg = childJson.optString("objimg").toString();
                            String action = childJson.optString("action").toString();
                            String meta = childJson.optString("meta").toString();
                            String status = childJson.optString("status").toString();
                            //  System.out.println("========Service Calling============10");
                            double roadPath_lat = Double.parseDouble(latp);
                            double roadPath_longitude = Double.parseDouble(longp);
                            try {
                                myTreasure.add(new RoadLocationObj(roadPath_lat, roadPath_longitude, steps, objimg, action,meta, status, "", "", "",
                                        "", "", "", "", "","","","",""));
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            System.out.println("=======myTreasure=========="+myTreasure.size());
                        }


                    }catch(Exception e){
                        serviceDataStatus="99";
                        e.printStackTrace();
                    }


                }

                else{
                    System.out.println("========Service Calling============10");
                    serviceDataStatus="99";
                }

            }catch(Exception e){

            }


        } catch (ClientProtocolException e) {
            System.out.println("========Service Calling============7");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("========Service Calling============8");
            e.printStackTrace();
        }

    }



    public String loadJSONFromAssetNew(Context context) {
        String json = null;
        try {
            InputStream is = context.openFileInput(challenge_id+".json");
            int size = is.available();
            if(size > 0) {
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            }else{
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


public class Callback extends WebViewClient {

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        super.shouldOverrideUrlLoading(view, url);
        boolean val=false;
        String rediredtedUrl=url;
        String Str = new String(url);
        System.out.println(Str.matches("activity_tracker"));
        System.out.println("===========Completed Solo Challenge===Redireted URL===================="+url);

        //   https://livehappy.ayubo.life/index.php?entryPoint=activity_tracker&id=1d29596b-9771-3c41-724e-5937c8878a2b&name=RUNNING
          if(url.contains("soloAdventureMap")){

              String[] parts = url.split("_");
              challenge_id = parts[1];

              Service_loadChallangeMap_ServiceCall();
              val=true;
              return val;
          }


          else if(url.contains(ApiClient.BASE_URL_entypoint+"activity_tracker")){
            val=true;
            System.out.println("========Inside Tracker================="+url);
            String Str2;
            String[] parts = url.split("&");
            String idData = parts[1];
            String nameData = parts[2];
            System.out.println("======sss=====0=========="+idData);
            System.out.println("======sss=====00=========="+nameData);

            String[] idArrya = idData.split("=");
            String activityId = idArrya[1];

            String[] nameArrya = nameData.split("=");
            String activityName = nameArrya[1];

            System.out.println("============activityName========"+activityName);
            System.out.println("============activityId========"+activityId);
            Intent intent = new Intent(getBaseContext(), ActivityTrackerActivity.class);
            intent.putExtra("activityName", activityName);
            intent.putExtra("activityId", activityId);
            startActivity(intent);



            System.out.println("=========Activity Started with Live===================");
            return val;
        }

        else if(url.contains(MAIN_URL_LIVE_HAPPY + "workout_start")){
            val=true;
            Intent in = new Intent(CompletedSoloChallangeActivity.this, WorkoutActivity.class);
            startActivity(in);
            return val;
        }
        if(url.contains(MAIN_URL_LIVE_HAPPY + "activity_tracker")){
            val=true;
            Intent in = new Intent(CompletedSoloChallangeActivity.this, ActivityTrackerActivity.class);
            startActivity(in);
            return val;
        }
        else if(url.contains("/subscriptionView")){
            val=true;
            Intent in = new Intent(CompletedSoloChallangeActivity.this, Medicine_ViewActivity.class);
            startActivity(in);
            return val;
        }
        else if(url.contains("/medicineView")){
            val=true;
            Intent in = new Intent(CompletedSoloChallangeActivity.this, RXViewActivity.class);
            startActivity(in);
            return val;
        }
        else if(url.contains("/scheduleExpertView")){
            val=true;
            Intent in = new Intent(CompletedSoloChallangeActivity.this, ExpertViewActivity.class);
            startActivity(in);
            return val;
        }

        else if(url.contains(MAIN_URL_APPS )){
            val=true;
            System.out.println("==============Redireted URL===================="+url);
            //  url="https://www.w3schools.com/";
            webView.loadUrl(url);
            return val;
        }
        else if(url.contains("/water_intake")){
            val=true;
            Intent in = new Intent(CompletedSoloChallangeActivity.this, WaterActivity.class);
            startActivity(in);
            return val;
        }
        else if(url.contains("/water_intake_window_call")){
            val=true;
            Intent in = new Intent(CompletedSoloChallangeActivity.this, WaterActivity.class);
            startActivity(in);
            return val;
        }
        else if(url.contains("/disable_back")){
            val=true;
            return val;
        }
        else if(url.contains("/enable_back")){
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
