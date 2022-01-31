package com.ayubo.life.ayubolife.challenges;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;

import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.health.Health_MainActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.health.RXViewActivity;
import com.ayubo.life.ayubolife.home.PostDataActivity;
import com.ayubo.life.ayubolife.map_challenges.ChallengeCompletedView_Activity;
import com.ayubo.life.ayubolife.map_challenges.MapChallengeActivity;
import com.ayubo.life.ayubolife.map_challenges.MapJoinChallenge_Activity;
import com.ayubo.life.ayubolife.mind.Mind_MainActivity;
import com.ayubo.life.ayubolife.model.RoadLocationObj;
import com.ayubo.life.ayubolife.rest.ApiClient;
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


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;

public class NewCHallengeActivity extends AppCompatActivity {
    PrefManager pref;
    WebView webView;
    String tip_header_1,tip_header_2,tip_type,tip_meta;
    ProgressDialog progressTimeline;
    String url,userid_ExistingUser,hasToken,encodedHashToken,white_lines;
    private static final String TAG = NewHomeWithSideMenuActivity.class.getSimpleName();
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;
    boolean permissionGrantedStatus=false;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    String currentUrl;
    PopupWindow mPopupWindow;
    LinearLayout mRelativeLayout;
    ImageButton mButton;
    String challenge_id,serviceDataStatus,showpopup;
    String cityJsonString,noof_day;
    ArrayList<RoadLocationObj> myTagList=null;
    ArrayList<RoadLocationObj> myTreasure=null;
    int total_steps;String weekSteps;
    ImageButton btn_backImgBtn;
    String cards;
    String service_checkpoints,enabled_checkpoints;

    String tip_icon,tip,tipheading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_challenge);

        webView = (WebView) findViewById(R.id.webView_bodyview);

        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (progressTimeline == null) {
            progressTimeline = Utility.createProgressDialog(NewCHallengeActivity.this);
        }

        pref = new PrefManager(NewCHallengeActivity.this);
        myTagList=new ArrayList<RoadLocationObj>();
        myTreasure=new ArrayList<RoadLocationObj>();

        userid_ExistingUser=pref.getLoginUser().get("uid");
        hasToken=pref.getLoginUser().get("hashkey");
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        url = ApiClient.BASE_URL + "index.php?entryPoint=joinChallenge&app_id"+ AppConfig.APP_BRANDING_ID;
        String newUrl=Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"),url);


        try{

            if (Utility.isInternetAvailable(NewCHallengeActivity.this)) {



                WebSettings webSettings = webView.getSettings();
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);

                if(Build.VERSION.SDK_INT >= 21){
                    webSettings.setMixedContentMode(0);
                    webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }else if(Build.VERSION.SDK_INT >= 19){
                    webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }else if(Build.VERSION.SDK_INT < 19){
                    webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }

                webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webView.setWebViewClient(new Callback());

                webView.getSettings().setLoadsImagesAutomatically(true);

                webView.requestFocus(View.FOCUS_DOWN);

                webView.loadUrl(newUrl);

                System.out.print("========newUrl=========" +newUrl);

                webView.setDownloadListener(new DownloadListener() {
                    public void onDownloadStart(String url, String userAgent,
                                                String contentDisposition, String mimetype,
                                                long contentLength) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
                webView.setWebChromeClient(new WebChromeClient(){



                    //For Android 3.0+
                    public void openFileChooser(ValueCallback<Uri> uploadMsg){
                        mUM = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        startActivityForResult(Intent.createChooser(i,"File Chooser"), FCR);
                    }
                    // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
                    public void openFileChooser(ValueCallback uploadMsg, String acceptType){
                        mUM = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        startActivityForResult(
                                Intent.createChooser(i, "File Browser"),
                                FCR);
                    }
                    //For Android 4.1+
                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                        mUM = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
                    }
                    //For Android 5.0+
                    public boolean onShowFileChooser(
                            WebView webView, ValueCallback<Uri[]> filePathCallback,
                            WebChromeClient.FileChooserParams fileChooserParams){

                        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(NewCHallengeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                                .checkSelfPermission(NewCHallengeActivity.this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                    PERMISSIONS_MULTIPLE_REQUEST);

                        }
//                        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED )) {
//                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 2);
//                        }
                        // write your logic here

                        if(mUMA != null){
                            mUMA.onReceiveValue(null);
                        }
                        mUMA = filePathCallback;
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
                            File photoFile = null;
                            try{
                                photoFile = createImageFile();
                                takePictureIntent.putExtra("PhotoPath", mCM);
                            }catch(IOException ex){
                                Log.e(TAG, "Image file creation failed", ex);
                            }
                            if(photoFile != null){

                                mCM = "file:" + photoFile.getAbsolutePath();
                                System.out.println("==========UPLOAD==========");
                                System.out.println("====================");
                                System.out.println("=========pATH==========="+photoFile.getAbsolutePath());
                                System.out.println("====================");
                                System.out.println("===========UPLOAD=========");
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            }else{
                                takePictureIntent = null;
                            }
                        }
                        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        contentSelectionIntent.setType("*/*");
                        Intent[] intentArray;
                        if(takePictureIntent != null){
                            intentArray = new Intent[]{takePictureIntent};
                        }else{
                            intentArray = new Intent[0];
                        }

                        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                        startActivityForResult(chooserIntent, FCR);
                        return true;
                    }

                });


            } else {

            }
        }

        catch(Exception e){
            //System.out.println("Timeline Webview....................."+e);

        }


    }
    private void Service_Service_getStepsData_ServiceCall() {

        if (Utility.isInternetAvailable(NewCHallengeActivity.this)) {
            progressTimeline.show();
            Service_getStepsData task = new Service_getStepsData();
            task.execute(new String[]{ApiClient.BASE_URL_live});
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
            System.out.println("========Service Calling============6");
            if(progressTimeline!=null){
                progressTimeline.dismiss();
            }

            String cityFile= loadJSONFromAssetNew(com.ayubo.life.ayubolife.webrtc.App.getInstance());
            if(cityFile==null){

                Service_getChallengeJson_ServiceCall();
            }else {

                MapChallengeActivity.Book treaturesList = new MapChallengeActivity.Book(myTreasure);

                Intent intent = new Intent(NewCHallengeActivity.this, MapChallengeActivity.class);
                intent.putExtra("challenge_id", challenge_id);
                intent.putExtra("noof_day", noof_day);
                intent.putExtra("cards", cards);
                intent.putExtra("showpopup", showpopup);
                intent.putExtra("white_lines", white_lines);

                intent.putExtra("tip_icon", tip_icon);
                intent.putExtra("tip", tip);
                intent.putExtra("tipheading", tipheading);

                intent.putExtra("tip_header_1", tip_header_1);
                intent.putExtra("tip_header_2", tip_header_2);
                intent.putExtra("tip_type", tip_type);
                intent.putExtra("tip_meta", tip_meta);

                intent.putExtra("service_checkpoints", service_checkpoints);
                intent.putExtra("enabled_checkpoints", enabled_checkpoints);
                intent.putExtra("Treatures", treaturesList);
                intent.putExtra("steps", Integer.toString(total_steps));
                intent.putExtra("weekSteps", weekSteps);


                startActivity(intent);
            }
        }
    }
    private void makeSetDefaultDevice() {

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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String res = jsonObj.optString("result").toString();


                        int result = Integer.parseInt(res);

                        if (result == 0) {
                            System.out.println("========Service Calling============4");
                            try {

                                serviceDataStatus = "0";

                                if(myTreasure!=null){
                                    myTreasure.clear();
                                }


                                String data = jsonObj.optString("data").toString();
                                JSONObject jsonData = new JSONObject(data);

                                //==================================
                                 service_checkpoints = jsonData.optString("service_checkpoints").toString();
                                if(service_checkpoints.equals("true")){
                                     enabled_checkpoints = jsonData.optString("enabled_checkpoints").toString();
                                }else{
                                    service_checkpoints="";
                                }
                                //=================================
                                tip_icon = jsonData.optString("tip_icon").toString();
                                tip = jsonData.optString("tip").toString();
                                tipheading = jsonData.optString("tipheading").toString();

                                tip_header_1 = jsonData.optString("tip_header_1");
                                tip_header_2 = jsonData.optString("tip_header_2");
                                tip_type = jsonData.optString("tip_type");
                                tip_meta = jsonData.optString("tip_meta");

                                white_lines = jsonData.optString("white_lines").toString();
                                weekSteps = jsonData.optString("weekSteps").toString();
                                String counter = jsonData.optString("counter").toString();
                                noof_day = jsonData.optString("day").toString();
                                String treatures = jsonData.optString("treatures").toString();
                                cards = jsonData.optString("cards").toString();
                                total_steps=Integer.parseInt(counter);

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
                                    String auto_hide = childJson.optString("auto_hide").toString();

                                    String bubble_text =null;
                                    String bubble_link =null;
                                    if (childJson.has("bubble_txt")) {
                                        bubble_text =childJson.getString("bubble_txt");
                                    }else{bubble_text="";}
                                    if (childJson.has("bubble_link")) {
                                        bubble_link =childJson.getString("bubble_link");
                                    }else{bubble_link="";}

                                    System.out.println("==========================="+bubble_text);
                                    System.out.println("==========================="+bubble_link);

                                    double roadPath_lat = Double.parseDouble(latp);
                                    double roadPath_longitude = Double.parseDouble(longp);

                                    myTreasure.add(new RoadLocationObj(roadPath_lat,roadPath_longitude,steps,objimg,action,meta,status,"","","",
                                            "","","","","","",bubble_text,bubble_link,auto_hide));

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




        } catch (IOException e) {
            System.out.println("========Service Calling============8");
            e.printStackTrace();
        }

    }


    private File createImageFile() throws IOException{
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
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
    private void getChallengeJson() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
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


            String sc = String.valueOf(response.getStatusLine().getStatusCode());


            if (sc.equals("200")) {

                String responseString = null;
                try {
                    responseString = EntityUtils.toString(response.getEntity());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(responseString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String res = jsonObj.optString("result").toString();

                            int result = Integer.parseInt(res);

                            if (result == 0) {
                                cityJsonString = jsonObj.optString("json").toString();

                            } else {

                                serviceDataStatus = "99";
                            }


            }

        } catch (IOException e) {
            System.out.println("========Service Calling============8");
            e.printStackTrace();
        }

    }

    private void Service_getChallengeJson_ServiceCall() {

        if (Utility.isInternetAvailable(NewCHallengeActivity.this)) {


            Service_getChallengeJson task = new Service_getChallengeJson();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
        }
    }
    private class Service_getChallengeJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            System.out.println("========Service Calling============0");
            getChallengeJson();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if(progressTimeline!=null){
                progressTimeline.dismiss();
            }

            writeToFile(cityJsonString);
            MapChallengeActivity.Book treaturesList = new MapChallengeActivity.Book(myTreasure);

            Intent intent = new Intent(NewCHallengeActivity.this, MapChallengeActivity.class);
            intent.putExtra("challenge_id", challenge_id);
            intent.putExtra("noof_day", noof_day);
            intent.putExtra("service_checkpoints", service_checkpoints);
            intent.putExtra("enabled_checkpoints", enabled_checkpoints);
            intent.putExtra("Treatures", treaturesList);
            intent.putExtra("cards", cards);
            intent.putExtra("steps", Integer.toString(total_steps));
            startActivity(intent);

        }
    }

    private void writeToFile(String result) {
        if(result!=null){
        try {
            FileOutputStream fos =openFileOutput(challenge_id+".json",NewCHallengeActivity.this.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(result);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        }
    }




    public class Callback extends WebViewClient{

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val=false;

            System.out.println("===========TimeLine===Callback Redireted URL===================="+url);

            if(url.contains("/index.php?entryPoint=joinChallenge")){
                val=false;
                return val;
            }


            else if(url.contains("soloAdventureMap")){
                System.out.println("===========soloAdventureMap URL===================="+url);
                String[] parts = url.split("_");
                challenge_id = parts[1];
                if(parts.length==3){
                 showpopup = parts[2];
                }
                val=true;
                Service_Service_getStepsData_ServiceCall();


                return val;
            }

            else if(url.contains("soloAdventureCompleted")) {
                val=true;
                Intent intent = new Intent(NewCHallengeActivity.this, ChallengeCompletedView_Activity.class);
                startActivity(intent);
                return val;
            }
            else if(url.contains("soloAdventureJoin")){
                val=true;

                String[] parts = url.split("_");
                challenge_id = parts[1];
                MapChallengeActivity.Book treaturesList = new MapChallengeActivity.Book(myTreasure);

                Intent intent = new Intent(NewCHallengeActivity.this, MapJoinChallenge_Activity.class);
                intent.putExtra("challenge_id",challenge_id);
                intent.putExtra("Treatures", treaturesList);
                intent.putExtra("steps",Integer.toString(total_steps));
                startActivity(intent);
                return val;
            }
            else if(url.contains(MAIN_URL_APPS)){
                if(url.contains("openInView=true")){
                    val=true;
                    Intent intent = new Intent(NewCHallengeActivity.this, CommonWebViewActivity.class);
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
                Intent in = new Intent(NewCHallengeActivity.this, RXViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains("medicineView")){
                val=true;
                Intent in = new Intent(NewCHallengeActivity.this, Medicine_ViewActivity.class);
                startActivity(in);
                return val;
            }

            else if(url.contains("https://livehappy.ayubo.life")){
                if(url.contains("openInBrowser=true")){
                    val=true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                }
                else if(url.contains("timelinepost")){
                    val=true;
                    Intent in = new Intent(NewCHallengeActivity.this, PostDataActivity.class);
                    in.putExtra("cId", "aa");
                    in.putExtra("cName", "aa");
                    in.putExtra("postType", "aa");
                    startActivity(in);
                    return val;
                }
                else if(url.contains("openInSame=true")){
                    val=false;
                    return val;
                }
                else if(url.contains("index.php?entryPoint=mobile_mind_view")){
                    val=true;
                    //  Intent in = new Intent(getContext(), MindViewActivity.class);
                    Intent in = new Intent(NewCHallengeActivity.this, Mind_MainActivity.class);
                    startActivity(in);
                    return val;
                }

                else if(url.contains("index.php?entryPoint=mobile_health_view")){
                    val=true;
                    Intent in = new Intent(NewCHallengeActivity.this, Health_MainActivity.class);
                    //  Intent in = new Intent(getContext(), CommonTabActivity.class);

                    startActivity(in);
                    return val;
                }
                else if(url.contains("subscriptionView")){
                    val=true;
                    Intent in = new Intent(NewCHallengeActivity.this, RXViewActivity.class);
                    startActivity(in);
                    return val;
                }
                else if(url.contains("medicineView")){
                    val=true;
                    Intent in = new Intent(NewCHallengeActivity.this, Medicine_ViewActivity.class);
                    startActivity(in);
                    return val;
                }
                else if(url.contains("mobileMedicalHistory")){
                    val=true;
                    Intent intent = new Intent(NewCHallengeActivity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                    return val;

                }
                else if(url.contains("index.php?entryPoint=ayuboLifeTimeline")){
                    val=false;
                    return val;
                }
                else if(url.contains("initChatMobile")){
                    val=true;
                    Intent intent = new Intent(NewCHallengeActivity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                }
                else if(url.contains("water_intake_window_call")){
                    val=true;
                    Intent in = new Intent(NewCHallengeActivity.this, WaterActivity.class);
                    startActivity(in);
                    return val;
                }
                else if(url.contains(ApiClient.BASE_URL)){
                    val=true;
                    System.out.println("=========iN======CommonWebViewActivity========="+url);
                    Intent intent = new Intent(NewCHallengeActivity.this, CommonWebViewActivity.class);
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
                if(url.contains("openInBrowser=true")){
                    val=true;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    return val;
                }

                else if(url.contains("timelinepost")){
                    val=true;
                    Intent intent = new Intent(NewCHallengeActivity.this, PostDataActivity.class);
                    intent.putExtra("cId", "aa");
                    intent.putExtra("cName", "aa");
                    intent.putExtra("postType", "aa");
                    startActivity(intent);
                    return val;
                }
                else if(url.contains("openInSame=true")){
                    val=false;
                    return val;
                }
                else if(url.contains("index.php?entryPoint=mobile_mind_view")){
                    val=true;
                    //  Intent in = new Intent(getContext(), MindViewActivity.class);
                    Intent in = new Intent(NewCHallengeActivity.this, Mind_MainActivity.class);
                    startActivity(in);
                    return val;
                }

                else if(url.contains("index.php?entryPoint=mobile_health_view")){
                    val=true;
                    Intent in = new Intent(NewCHallengeActivity.this, Health_MainActivity.class);
                    //  Intent in = new Intent(getContext(), CommonTabActivity.class);

                    startActivity(in);
                    return val;
                }
                else if(url.contains("subscriptionView")){
                    val=true;
                    Intent in = new Intent(NewCHallengeActivity.this, RXViewActivity.class);
                    startActivity(in);
                    return val;
                    //subscriptionView
//                    val=false;
//                    return val;
                }
                else if(url.contains("medicineView")){
                    val=true;
                    Intent in = new Intent(NewCHallengeActivity.this, Medicine_ViewActivity.class);
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
                        Intent intent = new Intent(NewCHallengeActivity.this, CommonWebViewActivity.class);
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
                    Intent intent = new Intent(NewCHallengeActivity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                    return val;
                }
                else if(url.contains("water_intake_window_call")){
                    val=true;
                    Intent in = new Intent(NewCHallengeActivity.this, WaterActivity.class);
                    startActivity(in);
                    return val;
                }
                else if(url.contains(ApiClient.BASE_URL)){
                    val=true;
                    System.out.println("=========iN======CommonWebViewActivity========="+url);
                    Intent intent = new Intent(NewCHallengeActivity.this, CommonWebViewActivity.class);
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
                    Intent intent = new Intent(NewCHallengeActivity.this, CommonWebViewActivity.class);
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
                Intent intent = new Intent(NewCHallengeActivity.this, CommonWebViewActivity.class);
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
                Intent in = new Intent(NewCHallengeActivity.this, WaterActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains(ApiClient.BASE_URL)){
                val=true;
                System.out.println("=========iN======CommonWebViewActivity========="+url);
                Intent intent = new Intent(NewCHallengeActivity.this, CommonWebViewActivity.class);
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


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            System.out.println("=========onPageStarted================");
            progressTimeline.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if(progressTimeline!=null){
                progressTimeline.dismiss();
            }
        }
    }
}
