package com.ayubo.life.ayubolife.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ContactDetailsActivity extends AppCompatActivity {
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
    // private PowerManager powerManager;
    // private PowerManager.WakeLock wakeLock;
    private int field = 0x00000020;
    private static final int READ_PHONE_CONTACTS = 101;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_contact_details);
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

        mWebView = (WebView) findViewById(R.id.webView_tracker);
        String URL = getIntent().getStringExtra("URL");

        // Brower niceties -- pinch / zoom, follow links in place
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new GeoWebViewClient());
        // Below required for geolocation
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.setWebChromeClient(new GeoWebChromeClient());


        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //  url=ApiClient.BASE_URL+"index.php?entryPoint=activity_tracker&id=";

        if (ContextCompat.checkSelfPermission(ContactDetailsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ContactDetailsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ContactDetailsActivity.this);
                builder.setTitle("Need Location Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ContactDetailsActivity.this, permissionsRequired, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
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

                ActivityCompat.requestPermissions(ContactDetailsActivity.this,
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

            if (Utility.isInternetAvailable(ContactDetailsActivity.this)) {

                web_prgDialog = new ProgressDialog(ContactDetailsActivity.this);
                web_prgDialog.setCancelable(false);
                web_prgDialog.show();
                web_prgDialog.setMessage("Loading...");

                mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                mWebView.setWebViewClient(new Callback());

                mWebView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
                mWebView.setLongClickable(false);

                String newUrl = Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"), URL);
                mWebView.loadUrl(newUrl);

                mySwipeRefreshLayout.setOnRefreshListener(
                        new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {

                                if (Utility.isInternetAvailable(ContactDetailsActivity.this)) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case READ_PHONE_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 1);

                    System.out.println("onRequestPermissionsResult===================PERMISSION_GRANTED==================");
                    //    newf=saveBitmap(b,"screnshot.png");

                } else {
                    System.out.println("onRequestPermissionsResult===================PERMISSION_GRANTED====no==============");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            Cursor c = getContentResolver().query(contactData, null, null, null, null);
            if (c.moveToFirst()) {

                String phoneNumber = "", emailAddress = "";
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                //http://stackoverflow.com/questions/866769/how-to-call-android-contacts-list   our upvoted answer

                String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1"))
                    hasPhone = "true";
                else
                    hasPhone = "false";

                if (Boolean.parseBoolean(hasPhone)) {
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                }

                // Find Email Addresses
                Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
                while (emails.moveToNext()) {
                    emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                emails.close();
                mWebView.loadUrl("javascript: (function(){document.getElementById('inviting_user_name').value ='" + name + "';})();");
                mWebView.loadUrl("javascript: (function(){document.getElementById('inviting_user').value ='" + phoneNumber + "';})();");
                //  dgfdfgdfg
                Log.d("curs", name + " num" + phoneNumber + " " + "mail" + emailAddress);

                System.out.println("=====================================");
                System.out.println("=====================================" + name + " " + phoneNumber + " " + emailAddress + " " + contactId);
                System.out.println("=====================================");


            }
            c.close();
        }
    }

    private void checkPermissionOpenSDCard() {
        if (ContextCompat
                .checkSelfPermission(ContactDetailsActivity.this,
                        Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (ContactDetailsActivity.this, Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(ContactDetailsActivity.this.findViewById(android.R.id.content),
                        "ayubo.life would like to access your contacts to add them on to a leaderboard with you.",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(
                                            new String[]{Manifest.permission.READ_CONTACTS},
                                            READ_PHONE_CONTACTS);
                            }
                        }).show();
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.READ_CONTACTS},
                            READ_PHONE_CONTACTS);
            }
        } else {
            // write your logic code if permission already granted
            //   System.out.println("checkPermissionOpenSDCard===================PERMISSION_GRANTED==================");
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 1);
        }
    }


    public class Callback extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            boolean val = false;

            System.out.println("======URL in======ActivityTrackerActivity=============" + url);

            if (url.contains("open_mobile_contact")) {

                checkPermissionOpenSDCard();


                val = true;
            } else if (url.contains("workout_stop")) {
                btn_backImgBtn.setVisibility(View.VISIBLE);
                String stes = Integer.toString(Ram.getCurrentWalkStepCount());
                System.out.println("============ActivityTrackerActivity==22=================" + stes);
                mWebView.loadUrl("javascript: (function(){document.getElementById('countLiveSteps').value ='" + stes + "';})();");
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


    //===============================
    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}

