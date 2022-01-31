package com.ayubo.life.ayubolife.body;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.model.workoutEntity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
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
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ShareWorkoutActivity extends AppCompatActivity {
   // txt_heading_text_mapsc im;
    LinearLayout lll;
    TextView txt_mets,txt_dis,txt_ckal,txt_dura,btn_share,txt_heading_text_mapsc;
    String sMets_ToIntent,sCals_ToIntent,sDis_ToIntent,sDur_ToIntent,statusFromServiceAPI_db,userid_ExistingUser,activityName;
    ImageButton btn_backImgBtn;
    private ProgressDialog progressDialog;
    private PrefManager pref;
    EditText txt_share_message;
    LinearLayout screenShotView;
    ImageView workout_share_map_image;

    @Override
    protected void onResume() {
        super.onResume();
        btn_backImgBtn.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_workout);

        sMets_ToIntent = getIntent().getStringExtra("mets");
        sCals_ToIntent = getIntent().getStringExtra("cals");
        sDis_ToIntent = getIntent().getStringExtra("dis");
        sDur_ToIntent = getIntent().getStringExtra("durWithDetails");
        activityName= getIntent().getStringExtra("activityName");

        lll = (LinearLayout) findViewById(R.id.lll);
        Bitmap b= Ram.getMapSreenshot();

        BitmapDrawable background = new BitmapDrawable(b);
        lll.setBackgroundDrawable(background);
        screenShotView = (LinearLayout) findViewById(R.id.mapView_lay);
        workout_share_map_image= (ImageView) findViewById(R.id.workout_share_map_image);

        txt_mets = (TextView) findViewById(R.id.txt_dmets);
        txt_dis = (TextView) findViewById(R.id.txt_dis);
        txt_ckal = (TextView) findViewById(R.id.txt_dckal);
        txt_dura = (TextView) findViewById(R.id.txt_dura);

        txt_heading_text_mapsc = (TextView) findViewById(R.id.txt_heading_text_mapsc);

        btn_share = (TextView) findViewById(R.id.btn_share);
        txt_share_message = (EditText) findViewById(R.id.txt_share_message);

        txt_mets.setText(sMets_ToIntent);
        txt_ckal.setText(sCals_ToIntent);
        txt_dis.setText(sDis_ToIntent);
        txt_dura.setText(sDur_ToIntent);
        Bitmap bitmap1=null;

        txt_heading_text_mapsc.setText(activityName+" WITH");

        if(activityName.equals("RUNNING")){
            bitmap1= BitmapFactory.decodeResource(ShareWorkoutActivity.this.getResources(), R.drawable.running_w_black);
        }
        else if(activityName.equals("HIKING")){
            bitmap1= BitmapFactory.decodeResource(ShareWorkoutActivity.this.getResources(), R.drawable.hicking_map_banner);
        }
        else if(activityName.equals("CYCLING")){
            bitmap1= BitmapFactory.decodeResource(ShareWorkoutActivity.this.getResources(), R.drawable.map_top_workout);
        }

        workout_share_map_image.setImageBitmap(bitmap1);




        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShareWorkoutActivity.this, StartWorkoutActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String msg=txt_share_message.getText().toString();

                btn_backImgBtn.setVisibility(View.GONE);
                Bitmap snapshot= takeScreenshotNew(screenShotView);
                Ram.setMapSreenshot(snapshot);
                Ram.setShareMessage(msg);
                Intent intent = new Intent(ShareWorkoutActivity.this, WorkoutShareListActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }




    public static Bitmap takeScreenshotNew(View v){
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b=Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ShareWorkoutActivity.this, StartWorkoutActivity.class);
        startActivity(intent);
        finish();
    }




    public void clickOnShare(View v){

        String msg=txt_share_message.getText().toString();

        btn_backImgBtn.setVisibility(View.GONE);
        Bitmap snapshot= takeScreenshotNew(screenShotView);
        Ram.setMapSreenshot(snapshot);
        Ram.setShareMessage(msg);
        Intent intent = new Intent(ShareWorkoutActivity.this, WorkoutShareListActivity.class);
        startActivity(intent);
       // finish();
    }
}
