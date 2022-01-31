package com.ayubo.life.ayubolife.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;


public class ActivityTrackerActivity extends AppCompatActivity {
    String encodedHashToken;
    ProgressDialog web_prgDialog;
    String hasToken;
    TextView txt_activity_name;
    SwipeRefreshLayout mySwipeRefreshLayout;
    WebView mWebView;
    private PrefManager pref;
    HashMap<String, String> user = null;
    String url;
    SharedPreferences prefs;
    String activityId, activityName;
    SimpleDateFormat sdf;
    String[] permissionsRequired = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;
    ImageButton btn_backImgBtn;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private int field = 0x00000020;

    public class GeoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // When user clicks a hyperlink, load in the existing WebView
            view.loadUrl(url);
            return true;
        }
    }

    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            // Always grant permission since the app itself requires location
            // permission and the user has therefore already granted it
            callback.invoke(origin, true, false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mWebView.loadUrl(url + activityId);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            // Yeah, this is hidden field.
            field = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable ignored) {
        }

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(field, getLocalClassName());

        if (!wakeLock.isHeld()) {
            wakeLock.acquire();
        }

        setContentView(R.layout.activity_tracker);
        pref = new PrefManager(this);
        prefs = getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        hasToken = prefs.getString("hashToken", "0");
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        activityName = getIntent().getStringExtra("activityName");
        activityId = getIntent().getStringExtra("activityId");


        txt_activity_name = (TextView) findViewById(R.id.txt_activity_name);
        txt_activity_name.setText(activityName);

        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);


        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        System.out.println("============ActivityTrackerActivity========" + activityName);
        System.out.println("============ActivityTrackerActivity========" + activityId);

        mWebView = (WebView) findViewById(R.id.webView_tracker);

        // Brower niceties -- pinch / zoom, follow links in place
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new GeoWebViewClient());
        // Below required for geolocation
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.setWebChromeClient(new GeoWebChromeClient());


        //===============IF RUNNING BELLOW WILL WOEK==========================================
        if (activityName.equals("RUNNING")) {

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_location = false;

            try {
                gps_location = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

            } catch (Exception ex) {
            }

            if (gps_location) {
                System.out.println("=============Location Enabled===================");
            } else {
                //===========================================================
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityTrackerActivity.this);
                alertDialogBuilder.setTitle("Permission required");
                alertDialogBuilder
                        .setMessage("Need Location Permission to run activity")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ActivityTrackerActivity.this.startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        }
        //===============IF RUNNING ABOVE WILL WOEK==========================================

        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        url = ApiClient.BASE_URL + "index.php?entryPoint=activity_tracker&id=";


        //PERMISSION SETTING STARTED================================
        //PERMISSION SETTING STARTED================================
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ActivityTrackerActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityTrackerActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTrackerActivity.this);
                builder.setTitle("Need Location Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ActivityTrackerActivity.this, permissionsRequired, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(ActivityTrackerActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        //PERMISSION SETTING END================================
        //PERMISSION SETTING END================================


        try {

            if (Utility.isInternetAvailable(ActivityTrackerActivity.this)) {

                web_prgDialog = new ProgressDialog(ActivityTrackerActivity.this);
                web_prgDialog.setCancelable(false);
                web_prgDialog.show();
                web_prgDialog.setMessage("Loading...");

//                WebSettings webSettings = webView.getSettings();
//                webSettings.setDomStorageEnabled(true);
//                webSettings.setJavaScriptEnabled(true);
//                webSettings.setAllowFileAccess(true);

                mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                mWebView.setWebViewClient(new Callback());

                mWebView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
                mWebView.setLongClickable(false);

                mWebView.loadUrl(url + activityId);

                mySwipeRefreshLayout.setOnRefreshListener(
                        new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {

                                if (Utility.isInternetAvailable(ActivityTrackerActivity.this)) {
                                    System.out.println("=====Webview onRefresh========");
                                    mWebView.loadUrl(url + activityId);
                                } else {

                                }

                                mWebView.setWebViewClient(new WebViewClient() {
                                                              @Override
                                                              public void onPageFinished(WebView web, String url) {
                                                                  web_prgDialog.dismiss();
                                                                  System.out.println("=====Webview onPageFinished========");
                                                                  if (mySwipeRefreshLayout.isRefreshing()) {
                                                                      System.out.println("=====Webview onPageFinished 2========");
                                                                      mySwipeRefreshLayout.setRefreshing(false);
                                                                  }
                                                              }

                                                              @Override
                                                              public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                                                  System.out.println("====onReceivedError========1");

                                                                  String htmlFilename = "error.html";
                                                                  AssetManager mgr = getBaseContext().getAssets();
                                                                  try {
                                                                      InputStream in = mgr.open(htmlFilename, AssetManager.ACCESS_BUFFER);
                                                                      String htmlContentInStringFormat = Utility.StreamToString(in);
                                                                      in.close();
                                                                      mWebView.loadDataWithBaseURL(null, htmlContentInStringFormat, "text/html", "utf-8", null);

                                                                  } catch (IOException e) {
                                                                      e.printStackTrace();
                                                                  }

                                                              }

                                                              @Override
                                                              public void onPageStarted(WebView view, String url, Bitmap favicon) {


                                                              }


                                                          }
                                );

                            }
                        }
                );

            } else {

            }
        } catch (Exception e) {
            System.out.println("Timeline Webview....................." + e);

        }

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public class Callback extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val = false;

            System.out.println("======URL in======ActivityTrackerActivity=============" + url);

            if (url.contains("workout_start")) {
                btn_backImgBtn.setVisibility(View.GONE);
                String stes = Integer.toString(Ram.getCurrentWalkStepCount());
                System.out.println("============ActivityTrackerActivity==21=================" + stes);
                mWebView.loadUrl("javascript: (function(){document.getElementById('countLiveSteps').value ='" + stes + "';})();");
                // Service_sendAyuboStepsToServer_ForTenDays0();
                val = true;
            } else if (url.contains("workout_stop")) {
                btn_backImgBtn.setVisibility(View.VISIBLE);

                String stes = Integer.toString(Ram.getCurrentWalkStepCount());
                System.out.println("============ActivityTrackerActivity==22=================" + stes);
                mWebView.loadUrl("javascript: (function(){document.getElementById('countLiveSteps').value ='" + stes + "';})();");
                // Service_sendAyuboStepsToServer_ForTenDays0();
                val = true;
            } else {
                val = false;
            }
            return val;


        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            String htmlFilename = "error.html";
            AssetManager mgr = getBaseContext().getAssets();
            try {
                InputStream in = mgr.open(htmlFilename, AssetManager.ACCESS_BUFFER);
                String htmlContentInStringFormat = Utility.StreamToString(in);
                in.close();
                mWebView.loadDataWithBaseURL(null, htmlContentInStringFormat, "text/html", "utf-8", null);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            web_prgDialog.dismiss();
        }
    }


//    private void Service_sendAyuboStepsToServer_ForTenDays0() {
//        sendAyuboStepsToServer_ForTenDays0 task = new sendAyuboStepsToServer_ForTenDays0();
//        task.execute(new String[]{ApiClient.BASE_URL_live});
//    }

//    private class sendAyuboStepsToServer_ForTenDays0 extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            sendAyuboSteps_ForTenDays0();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//        }
//    }

//    private void sendAyuboSteps_ForTenDays0() {
//
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
//        //Post Data
//        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
//        String userid_ExistingUser = pref.getLoginUser().get("uid");
//        String stes = Integer.toString(Ram.getCurrentWalkStepCount());
//
//        Date dae = new Date();
//        String sToday = sdf.format(dae);
//
//        String jsonStr =
//                "{" +
//                        "\"userid\": \"" + userid_ExistingUser + "\"," +
//                        "\"activity\": \"" + "activity_AYUBO" + "\"," +
//                        "\"energy\": \"" + "0" + "\"," +
//                        "\"steps\": \"" + stes + "\"," +
//                        "\"calorie\": \"" + "0" + "\", " +
//                        "\"duration\": \"" + "0" + "\"," +
//                        "\"distance\": \"" + "0" + "\"," +
//                        "\"date\": \"" + sToday + "\"," +
//                        "\"walk_count\": \"" + stes + "\"," +
//                        "\"run_count\": \"" + "0"
//                        + "\"" +
//                        "}";
//
//        //  addWorkoutSummary
//        nameValuePair.add(new BasicNameValuePair("method", "addDailyActivitySummary"));
//        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
//        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));
//
//
//        System.out.println("...........addDailyActivitySummary...in Activity Tracker..." + nameValuePair.toString());
//        //Encoding POST data
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
//        } catch (UnsupportedEncodingException e) {
//            // log exception
//            e.printStackTrace();
//        }
//
//        //making POST request.
//        try {
//            HttpResponse response = httpClient.execute(httpPost);
//            System.out.println("..........response..........." + response);
//
//            String responseString = null;
//            try {
//                responseString = EntityUtils.toString(response.getEntity());
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            JSONObject jsonObj = null;
//            try {
//                jsonObj = new JSONObject(responseString);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            String number = jsonObj.optString("number").toString();
//
//            if (number.isEmpty()) {
//
//                String res = jsonObj.optString("result").toString();
//
//                int result = Integer.parseInt(res);
//
//
//                if (result == 0) {
//                    System.out.println("==========Result==============" + result);
//
//
//                }
//
//            } else {
//
//                // stepsSentToDBStatus="99";
//
//            }
//
//
////                        if (number.equals("10")) {
////                            statusFromServiceAPI_db = "11";
////                            System.out.println("=========================LOGIN FAIL=========>" + number);
////                        } else {
//
//
//        } catch (ClientProtocolException e) {
//            // Log exception
//            e.printStackTrace();
//        } catch (IOException e) {
//            // Log exception
//            e.printStackTrace();
//        }
//
//    }


    //===============================
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (wakeLock.isHeld()) {
            wakeLock.release();
        }

    }


}
