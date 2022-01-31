package com.ayubo.life.ayubolife.map_challenges;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.home.PostDataActivity;
import com.ayubo.life.ayubolife.webrtc.App;


import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;

public class ShareMapPosition_Activity extends AppCompatActivity {
    String share_cityName,share_completed_steps,share_noofDays,share_image,status;
    TextView txt_cityName,txt_completed_steps,txt_noofDays,lbl_steps_lable,txt_txt_share_Days_text;
    NetworkImageView main_bg_banner;
    ImageLoader imageLoader;
    Bitmap b=null; File newf;
    LinearLayout share_screen;
    ImageView img_location_icon_on_map;


    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_share_map_position_);

        share_cityName = getIntent().getStringExtra("share_cityName");
        share_completed_steps = getIntent().getStringExtra("share_completed_steps");
        share_noofDays = getIntent().getStringExtra("share_noofDays");
        share_image = getIntent().getStringExtra("share_image");
        status = getIntent().getStringExtra("status");

        txt_cityName=(TextView)findViewById(R.id.txt_share_cityName);
        txt_completed_steps=(TextView)findViewById(R.id.share_share_completed_steps);
        txt_noofDays=(TextView)findViewById(R.id.txt_txt_share_noofDays);
        txt_txt_share_Days_text=(TextView)findViewById(R.id.txt_txt_share_Days_text);
        img_location_icon_on_map=(ImageView)findViewById(R.id.img_location_icon_on_map);

        lbl_steps_lable=(TextView)findViewById(R.id.lbl_steps_lable);
        txt_txt_share_Days_text=(TextView)findViewById(R.id.txt_txt_share_Days_text);


        main_bg_banner=(NetworkImageView)findViewById(R.id.main_bg_share_banner);
        share_screen = (LinearLayout) findViewById(R.id.cha_share_screen);


        if(status.equals("ongoing")){
            txt_cityName.setText(share_cityName);

            if(share_completed_steps==null){
                share_completed_steps="0";
            }
            int steps=Integer.parseInt(share_completed_steps);
            String steps_with_comma = NumberFormat.getIntegerInstance().format(steps);
            txt_completed_steps.setText(steps_with_comma);

            int intnoofdays=Integer.parseInt(share_noofDays);
            String daystext=" Day";
            if(intnoofdays==1){
                daystext=" Day";
            }else{
                daystext=" Days";
            }
            txt_txt_share_Days_text.setText(daystext);
            txt_noofDays.setText(share_noofDays);

            img_location_icon_on_map.setVisibility(View.VISIBLE);
            lbl_steps_lable.setVisibility(View.VISIBLE);
            txt_txt_share_Days_text.setVisibility(View.VISIBLE);
        }else{
            img_location_icon_on_map.setVisibility(View.GONE);
            lbl_steps_lable.setVisibility(View.GONE);
            txt_txt_share_Days_text.setVisibility(View.GONE);
        }

        ProgressBar progressNewsList = (ProgressBar) findViewById(R.id.progressNewsList);

        if (imageLoader == null)
            imageLoader= App.getInstance().getImageLoader();
        main_bg_banner.setImageUrl(share_image, imageLoader);
        loadImage(share_image,main_bg_banner,progressNewsList);

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
            sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share ayubo.life workout");
            sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
            sendIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            sendIntent.setType("image/png");
            startActivity(Intent.createChooser(sendIntent, "Share ayubo.life"));
        }catch(Exception e){
            System.out.println("Error "+e);
        }
    }

    public void sharePosition(View v) {
        checkPermissionOpenSDCard();
    }

    private void checkPermissionOpenSDCard() {
        if (ContextCompat
                .checkSelfPermission(ShareMapPosition_Activity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (ShareMapPosition_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(ShareMapPosition_Activity.this.findViewById(android.R.id.content),
                        "This app needs storage permission.",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                            }
                        }).show();
            }
            else
            {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        } else {
            // write your logic code if permission already granted
            sharePositiont();

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
