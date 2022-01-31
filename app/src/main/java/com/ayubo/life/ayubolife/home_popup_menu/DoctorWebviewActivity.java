package com.ayubo.life.ayubolife.home_popup_menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.LinearLayout;

import com.ayubo.life.ayubolife.fragments.Profile_Fragment;
import com.ayubo.life.ayubolife.gallery.Image;

public class DoctorWebviewActivity extends AppCompatActivity {
    private ArrayList<Image> images;
    private ImageView viewPager;
    String hasToken;
    SwipeRefreshLayout mySwipeRefreshLayout;
    private static final String TAG = com.ayubo.life.ayubolife.satalite_menu.ProfileActivity.class.getSimpleName();
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;
    static final int REQUEST_TAKE_PHOTO = 241;

    TextView btn_Appointment;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    SharedPreferences prefs;
    String userid_ExistingUser;
    private Uri fileUri;
    String encodedHashToken,statusFromServiceAPI_db;
    int result;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String url=null;String url_logout=null;
    WebView webView; LinearLayout lay_btnBack;  ImageButton btn_backImgBtn;
    // ImageButton  boBack;
    private Profile_Fragment.OnFragmentInteractionListener mListener;
    ProgressDialog prgDialog_pro;int SELECT_FILE = 1;
    PrefManager pref;
    public void goBackToHome(){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_profile);

        pref = new PrefManager(DoctorWebviewActivity.this);
        hasToken=pref.getLoginUser().get("hashkey");


        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToHome();
            }
        });

        lay_btnBack=(LinearLayout)findViewById(R.id.lay_btnBack);
        lay_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToHome();
            }
        });

        System.out.println("===========LOADING WEB VIEW============");
        System.out.println(userid_ExistingUser+"======================="+hasToken);
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        url = getIntent().getStringExtra("URL");

        String newUrl=Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"),url);


        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        if (Utility.isInternetAvailable(DoctorWebviewActivity.this)) {

            System.out.println(userid_ExistingUser+"======================="+encodedHashToken);
            prgDialog_pro = new ProgressDialog(DoctorWebviewActivity.this);
            prgDialog_pro.setCancelable(false);
            prgDialog_pro.show();
            prgDialog_pro.setMessage("Please wait...");

            webView.loadUrl(newUrl);
            webView.setWebViewClient(new DoctorWebviewActivity.WebViewController());

            webView.setWebChromeClient(new WebChromeClient(){

                public void onPageFinished(WebView view, String url) {
                    prgDialog_pro.dismiss();

                }

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


                    if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(DoctorWebviewActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                            .checkSelfPermission(DoctorWebviewActivity.this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                PERMISSIONS_MULTIPLE_REQUEST);

                    }

                    if(mUMA != null){
                      //  mUMA = null;
                      //  mUMA.onReceiveValue(null);
                    }
                    mUMA = filePathCallback;


                    selectImagePopup();




                  //  Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if(takePictureIntent.resolveActivity(DoctorWebviewActivity.this.getPackageManager()) != null){
//                        File photoFile = null;
//                        try{
//                            photoFile = createImageFile();
//                            takePictureIntent.putExtra("PhotoPath", mCM);
//                        }catch(IOException ex){
//                            Log.e(TAG, "Image file creation failed", ex);
//                        }
//                        if(photoFile != null){
//
//                            mCM = "file:" + photoFile.getAbsolutePath();
//                            System.out.println("==========UPLOAD==========");
//                            System.out.println("====================");
//                            System.out.println("=========pATH==========="+photoFile.getAbsolutePath());
//                            System.out.println("====================");
//                            System.out.println("===========UPLOAD=========");
//                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                        }else{
//                            takePictureIntent = null;
//                        }
//                    }
//                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                    contentSelectionIntent.setType("*/*");
//                    Intent[] intentArray;
//                    if(takePictureIntent != null){
//                        intentArray = new Intent[]{takePictureIntent};
//                    }else{
//                        intentArray = new Intent[0];
//                    }
//
//                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
//                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
//                    startActivityForResult(chooserIntent, FCR);
                    return true;
                }
            });

        } else {


        }


    }

    private void selectImagePopup() {
        try{
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(Build.VERSION.SDK_INT >= 21){
            Uri[] results = null;
            //Check if response is positive
            if(resultCode== RESULT_OK){
                if(requestCode == FCR){
                    if(null == mUMA){
                        return;
                    }
                    if(intent == null){
                        //Capture Photo if no image available
                        if(mCM != null){
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    }else{
                        String dataString = intent.getDataString();
                        if(dataString != null){
                            results = new Uri[]{Uri.parse(dataString)};

                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
         //   mUMA = null;
        }else{
            if(requestCode == FCR){
                if(null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }



    public class WebViewController extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            if(prgDialog_pro!=null){
                prgDialog_pro.dismiss();
            }


        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }







}
