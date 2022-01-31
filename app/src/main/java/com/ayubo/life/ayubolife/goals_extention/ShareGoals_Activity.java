package com.ayubo.life.ayubolife.goals_extention;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.map_challenges.ShareMapPosition_Activity;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;

public class ShareGoals_Activity extends AppCompatActivity {
    int intWidth;
    float hi;
    int intHeight;
    PrefManager pref;
    String sponserURl,shareImageURl,goalName;
    TextView txt_share_cityNamessss,txt_completed_steps,txt_noofDays,lbl_steps_lable,txt_txt_share_Days_text;
    NetworkImageView main_bg_banner;
    ImageLoader imageLoader;
    Bitmap b=null; File newf;
    LinearLayout share_screen;
    ImageView img_location_icon_on_map,img_sponsor_logo;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 901;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_goals_);



       pref=new PrefManager(ShareGoals_Activity.this);

       String from = getIntent().getStringExtra("from");

       if(from.equals("achived")){
           sponserURl=  pref.getMyGoalData().get("my_goal_sponser_large_image");
           shareImageURl=  pref.getMyGoalData().get("my_goal_share_large_image");
           goalName=  pref.getMyGoalData().get("my_goal_name");
       }else{
           sponserURl = getIntent().getStringExtra("sponserURl");
           shareImageURl = getIntent().getStringExtra("shareImageURl");
           goalName = getIntent().getStringExtra("goalName");
       }

            System.out.println(sponserURl);
            System.out.println(goalName);
            System.out.println(shareImageURl);

           img_sponsor_logo= findViewById(R.id.img_sponsor_logo);

           txt_share_cityNamessss= findViewById(R.id.txt_share_cityNamessss);
           txt_completed_steps= findViewById(R.id.share_share_completed_steps);
           txt_noofDays= findViewById(R.id.txt_txt_share_noofDays);
           txt_txt_share_Days_text= findViewById(R.id.txt_txt_share_Days_text);
           img_location_icon_on_map= findViewById(R.id.img_location_icon_on_map);

           lbl_steps_lable= findViewById(R.id.lbl_steps_lable);
           txt_txt_share_Days_text= findViewById(R.id.txt_txt_share_Days_text);


           main_bg_banner= findViewById(R.id.main_bg_share_banner);
           share_screen = findViewById(R.id.cha_share_screen);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());
        intWidth = deviceScreenDimension.getDisplayWidth();
        hi = (intWidth / 4) * 3;
        intHeight = (int) hi;

        ViewGroup.LayoutParams params = share_screen.getLayoutParams();

        params.height = intHeight;
        params.width = intWidth;
        share_screen.setLayoutParams(params);


            txt_share_cityNamessss.setText(goalName);

        int imageSize = Utility.getImageSizeFor_DeviceDensitySize(120);

        sponserURl = sponserURl.replace("zoom_level", "xxxhdpi");

        if((sponserURl!=null)&&(sponserURl.length()>10)){

            RequestOptions requestOptions1 = new RequestOptions()
                    .transform(new CircleTransform(ShareGoals_Activity.this))
                    .fitCenter()
                    .override(imageSize, imageSize)
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(ShareGoals_Activity.this).load(sponserURl)
                    .apply(requestOptions1)
                    .into(img_sponsor_logo);
           }


      //  https://livehappy.ayubo.life/custom/include/images/goal_images/zoom_level/sponsor-logo-ayubo.png
           System.out.println("---------------------------------"+sponserURl);

           ProgressBar progressNewsList = findViewById(R.id.progressNewsList);

           if (imageLoader == null)
           imageLoader= App.getInstance().getImageLoader();
           main_bg_banner.setImageUrl(shareImageURl, imageLoader);
           loadImage(shareImageURl,main_bg_banner,progressNewsList);

           }
       public static Bitmap takeScreenshotNew(View v){
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b=Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;


        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String ur = null;
        try{

        if (resultCode == Activity.RESULT_OK) {
           sharePositiont();
        }

        }catch (Exception e){

        e.printStackTrace();
        }
        }


@Override
public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        switch (requestCode) {

        case READ_EXTERNAL_STORAGE_REQUEST_CODE: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        sharePositiont();

        } else {
        System.out.println("===========================");
        // permission denied, boo! Disable the
        // functionality that depends on this permission.
        }
        return;
        }
        // other 'case' lines to check for other
        // permissions this app might request
        }
        }

        public void sharePositiont() {

        Bitmap snapshot= takeScreenshotNew(share_screen);

        try{
        Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
        sendIntent.setAction(Intent.ACTION_SEND);
        String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), snapshot,"ayubolife", null);
        Uri bmpUri = Uri.parse(pathofBmp);
        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My wellness goal for today");
        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        sendIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        sendIntent.setType("image/png");
        startActivity(Intent.createChooser(sendIntent, "Share ayubo.life"));

        finish();

        }catch(Exception e){
        System.out.println("Error "+e);
        }
        }

public void sharePosition(View v) {
        checkPermissionOpenSDCard();
        }

private void checkPermissionOpenSDCard() {
        if (ContextCompat.checkSelfPermission(ShareGoals_Activity.this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

        if (ActivityCompat.shouldShowRequestPermissionRationale
        (ShareGoals_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        Snackbar.make(ShareGoals_Activity.this.findViewById(android.R.id.content),
        "This app needs storage permission.",
        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
        new View.OnClickListener() {
@Override
public void onClick(View v) {
        if (Build.VERSION.SDK_INT >= 23)
        requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},READ_EXTERNAL_STORAGE_REQUEST_CODE); } }).show();
        }
        else
        {
        // No explanation needed, we can request the permission.
        if (Build.VERSION.SDK_INT >= 23) requestPermissions(
        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        } else {
        // write your logic code if permission already granted
        sharePositiont();

        }
        }

private static File saveBitmap(Bitmap bm, String fileName){
        String path =null;
        File dir =null;
        try {
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        dir = new File(path);
        }catch(Exception e){
        e.printStackTrace();
        }
        if(!dir.exists())
        dir.mkdirs();
        File file = new File(dir, fileName);
        try {
        FileOutputStream fOut = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
        fOut.flush();
        fOut.close();
        } catch (Exception e) {
        e.printStackTrace();
        }
        return file;
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
