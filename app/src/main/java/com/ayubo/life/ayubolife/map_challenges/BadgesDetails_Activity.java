package com.ayubo.life.ayubolife.map_challenges;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.model.RoadLocationObj;
import com.ayubo.life.ayubolife.webrtc.App;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BadgesDetails_Activity extends AppCompatActivity {
    String noof_day,strstep;
    ArrayList<RoadLocationObj> myTagList=null;
    ArrayList<RoadLocationObj> myTreasure=null;
    TextView btn_share_button;
    Button btn_join_challenge;ImageButton btn_backImgBtn;
    ProgressDialog prgDialog;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 101;
    String userid_ExistingUser,name,last_date,desc,status,share_image;
    String html,image;
    ImageLoader imageLoader;
    Bitmap b;
    ProgressDialog mProgressDialog;
    int total_steps_int;
    String setDeviceID_Success,today;
    String total_steps,cityJsonString,url; ArrayList<RoadLocationObj> myTreasureList = null;
    PrefManager pref; WebView webView; ProgressDialog web_prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges_details_);
        mProgressDialog=new ProgressDialog(BadgesDetails_Activity.this);
        mProgressDialog.setMessage("Loading...");
        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");

        prgDialog = new ProgressDialog(BadgesDetails_Activity.this);

        desc = getIntent().getStringExtra("desc");
        image = getIntent().getStringExtra("image");
        name = getIntent().getStringExtra("name");
        last_date = getIntent().getStringExtra("last_date");
        status = getIntent().getStringExtra("status");
        share_image = getIntent().getStringExtra("share_image");

        btn_share_button = (TextView) findViewById(R.id.btn_share_button);

        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn_badges);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        btn_share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermissionOpenSDCard();


            }
        });

        TextView txt_name = (TextView) findViewById(R.id.txt_name);
        TextView txt_desc = (TextView) findViewById(R.id.txt_desc);
        TextView txt_date = (TextView) findViewById(R.id.txt_date);

        ProgressBar progressNewsList = (ProgressBar) findViewById(R.id.progressNewsList);
        NetworkImageView bg_image = (NetworkImageView) findViewById(R.id.main_bg_banner);

        if (imageLoader == null)
            imageLoader = App.getInstance().getImageLoader();
        bg_image.setImageUrl(image, imageLoader);
        loadImage(image, bg_image, progressNewsList);

        txt_name.setText(name);
        txt_desc.setText(desc);

        if (status.equals("lock")) {
            txt_date.setVisibility(View.GONE);
            btn_share_button.setVisibility(View.GONE);
        } else {
            txt_date.setVisibility(View.VISIBLE);
            btn_share_button.setVisibility(View.VISIBLE);

            Long timeInMillis = Long.valueOf(last_date);
            ;
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timeInMillis * 1000);

            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy");
            String sss = sdf.format(cal.getTime());
            txt_date.setText(sss);


        }
    }


    private void checkPermissionOpenSDCard() {
        if (ContextCompat
                .checkSelfPermission(BadgesDetails_Activity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (BadgesDetails_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(BadgesDetails_Activity.this.findViewById(android.R.id.content),
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
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case READ_EXTERNAL_STORAGE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
             //   int sds=grantResults.length;
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    sharePositiont();
                } else {

                  //  sharePositiont();

                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String ur = null;
        try{
            if (resultCode == Activity.RESULT_OK) {

                if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {

                    sharePositiont();
                }

            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    private class getPhoneContacts extends AsyncTask<String, String, String> {
        private String resp="";

        @Override
        protected String doInBackground(String... params) {


            URL url = null;
            try {
                url = new URL(share_image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                b = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();


            if(b!=null){
                try{
                    Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sendIntent.setAction(Intent.ACTION_SEND);
                    String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), b,"ayubolife", null);
                    Uri bmpUri = Uri.parse(pathofBmp);
                    //   Uri uri =  Uri.parse(share_image);
                    sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                    sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    sendIntent.setType("image/png");
                    startActivity(Intent.createChooser(sendIntent, "Share ayubo.life"));
                    finish();
                }catch(Exception e){
                    System.out.println("Error=================== "+e);
                }
            }else{
                System.out.println("=======Bitmap Null============ ");
            }
        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

    public void sharePositiont() {
        mProgressDialog.show();
        getPhoneContacts getContacts = new getPhoneContacts();
        getContacts.execute();
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
