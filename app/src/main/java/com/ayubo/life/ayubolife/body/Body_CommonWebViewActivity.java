package com.ayubo.life.ayubolife.body;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonView_Second_WebviewActivity;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.health.ExpertViewActivity;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.health.RXViewActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;


public class Body_CommonWebViewActivity extends AppCompatActivity {
    private static final String TAG = Body_CommonWebViewActivity.class.getSimpleName();
    String encodedHashToken;
    ProgressDialog web_prgDialog;
    String hasToken;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;
    WebView webView;
    HashMap<String, String> user=null;
    String url;Vibrator v;
    //SharedPreferences prefs;
    PrefManager pref;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private int field = 0x00000020;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    ImageButton btn_backImgBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_body__common_web_view);

        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        url = getIntent().getStringExtra("URL");

        try {
            // Yeah, this is hidden field.
            field = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable ignored) {
        }

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(field, getLocalClassName());

        try {
            if (url.contains("activity_tracker")) {
                if(!wakeLock.isHeld()) {
                    wakeLock.acquire();
                }

                if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(Body_CommonWebViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )) {
                    ActivityCompat.requestPermissions(Body_CommonWebViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);



                }
            } else {

            }
        }catch(Exception e){

        }

        v = (Vibrator) Body_CommonWebViewActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        pref = new PrefManager(this);
        hasToken=pref.getLoginUser().get("hashkey");

        webView = (WebView) findViewById(R.id.webView_commonview);
        //=========================================================

        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try{

            if (Utility.isInternetAvailable(Body_CommonWebViewActivity.this)) {

                web_prgDialog = new ProgressDialog(Body_CommonWebViewActivity.this);
                web_prgDialog.setCancelable(false);
                web_prgDialog.show();
                web_prgDialog.setMessage("Loading...");

                WebSettings webSettings = webView.getSettings();
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);
                webView.addJavascriptInterface(new WebAppInterface(this), "Android");

                webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webView.setWebViewClient(new Callback());
                webView.setLongClickable(true);
                webView.requestFocus(View.FOCUS_DOWN);

                webView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
                System.out.println("url in.....Body_CommonWebViewActivity..................."+url+encodedHashToken);
                webView.loadUrl(url);
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

                        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(Body_CommonWebViewActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                                .checkSelfPermission(Body_CommonWebViewActivity.this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                    PERMISSIONS_MULTIPLE_REQUEST);

                        }

                        if(mUMA != null){
                            mUMA.onReceiveValue(null);
                        }
                        mUMA = filePathCallback;
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(takePictureIntent.resolveActivity(Body_CommonWebViewActivity.this.getPackageManager()) != null){
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
        }catch(Exception e){
            System.out.println("Timeline Webview....................."+e);

        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("================onPause=================");

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("================onStop=================");

    }

    private File createImageFile() throws IOException{
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }

    public class Callback extends WebViewClient{

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            String perURL=url;
            boolean perURL_flag=false;

            System.out.println("===========Body_CommonWebViewActivity===Redireted URL===================="+url);

            if(url.contains("disable_back")){
                btn_backImgBtn.setVisibility(View.GONE);
            }else{
                btn_backImgBtn.setVisibility(View.VISIBLE);
            }


            boolean val=false;


            if(url.contains("subscriptionView")){
                val=true;
                Intent in = new Intent(Body_CommonWebViewActivity.this, Medicine_ViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains("medicineView")){
                val=true;
                Intent in = new Intent(Body_CommonWebViewActivity.this, RXViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains("scheduleExpertView")){
                val=true;
                Intent in = new Intent(Body_CommonWebViewActivity.this, ExpertViewActivity.class);
                startActivity(in);
                return val;
            }
            else if(url.contains("vibrate")){

                v.vibrate(1500);
                return true;
            }
            else if(url.contains(MAIN_URL_APPS)){
                val=true;
                webView.loadUrl(url);
                return val;
            }
            else if(url.contains("/water_intake")){
                val=true;
                Intent in = new Intent(Body_CommonWebViewActivity.this, WaterActivity.class);
                startActivity(in);
                finish();
                return val;
            }
            else if(url.contains("/water_intake_window_call")){
                val=true;
                Intent in = new Intent(Body_CommonWebViewActivity.this, WaterActivity.class);
                startActivity(in);
                return val;
            }

            else if(url.contains("/disable_back")){
                btn_backImgBtn.setVisibility(View.GONE);
                val=true;
                return val;
            }

            else if(url.contains("/enable_back")){
                btn_backImgBtn.setVisibility(View.VISIBLE);
                val=true;
                return val;
            }
            else if(url.contains("sendToBaseView")){
                val=true;
                Intent intent = new Intent(getBaseContext(), NewHomeWithSideMenuActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
                return val;
            }
            else if(url.contains("PC_Hospitals&action=connect_user&type=mobile")){
                System.out.println("======Redireted URL===to CommonView_Second_WebviewActivity================="+url);
                val=true;
                Intent intent = new Intent(getBaseContext(), CommonView_Second_WebviewActivity.class);
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
            else if(url.contains(ApiClient.BASE_URL)){
                val=true;
                Intent intent = new Intent(getBaseContext(), Body_CommonWebViewActivity.class);
                intent.putExtra("URL", url);
                startActivity(intent);
                return val;
            }


            //     https://livehappy.ayubo.life/index.php?entryPoint=mobile_program_list
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
            mUMA = null;
        }else{
            if(requestCode == FCR){
                if(null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }





    //===============================




    public class WebAppInterface {
        Context mContext;
        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
        public void showDialog(String dialogMsg){
            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

            // Setting Dialog Title
            alertDialog.setTitle("JS triggered Dialog");

            // Setting Dialog Message
            alertDialog.setMessage(dialogMsg);

            // Setting alert dialog icon
            //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(mContext, "Dialog dismissed!", Toast.LENGTH_SHORT).show();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }

        @JavascriptInterface
        public void moveToNextScreen(String s){

            Intent chnIntent = new Intent(Body_CommonWebViewActivity.this, CommonWebViewActivity.class);
            chnIntent.putExtra("URL", s);
            startActivity(chnIntent);
        }
    }
}

