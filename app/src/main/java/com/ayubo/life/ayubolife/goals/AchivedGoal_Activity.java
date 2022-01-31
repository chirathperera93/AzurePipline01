package com.ayubo.life.ayubolife.goals;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.goals_extention.ShareGoals_Activity;
import com.ayubo.life.ayubolife.pojo.goals.achieveGoal.Data;
import com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum;
import com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.MyProgressLoading;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AchivedGoal_Activity extends AppCompatActivity {
     PrefManager pref; String mygoalId;
     String userid_ExistingUser;Button btn_achieve_yes,btn_achieve_no;
     ImageButton btn_back_Button,btn_share;
     ImageView goalImage;
     TextView txt_goal_name; String goalName;String imageURL;String large_imageURL;
     String     my_goal_sponser_large_image;
     String my_goal_share_large_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achived_goal_);

        pref = new PrefManager(AchivedGoal_Activity.this);
        userid_ExistingUser=pref.getLoginUser().get("uid");

        large_imageURL=  pref.getMyGoalData().get("my_goal_large_image");
        imageURL=  pref.getMyGoalData().get("my_goal_image");
        goalName=  pref.getMyGoalData().get("my_goal_name");
        my_goal_sponser_large_image=  pref.getMyGoalData().get("my_goal_sponser_large_image");
        my_goal_share_large_image=  pref.getMyGoalData().get("my_goal_share_large_image");


        btn_share = (ImageButton) findViewById(R.id.btn_share);
        txt_goal_name = (TextView) findViewById(R.id.txt_goal_name);
        txt_goal_name.setText(goalName);


        int imageSize=Utility.getImageSizeFor_DeviceDensitySize(150);

        goalImage = (ImageView) findViewById(R.id.goalImage);
        System.out.println("=========4===imageSize========"+imageSize);
        if(imageURL.length() > 10) {
            RequestOptions requestOptions = new RequestOptions().override(imageSize, imageSize).diskCacheStrategy(DiskCacheStrategy.ALL).transforms(new CircleTransform(AchivedGoal_Activity.this));
            Glide.with(AchivedGoal_Activity.this).load(imageURL)
                    .transition(withCrossFade())
                    .thumbnail(0.5f)
                    .apply(requestOptions)
                    .into(goalImage);


        }
        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_back_Button = (ImageButton) findViewById(R.id.btn_back_Button);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AchivedGoal_Activity.this, ShareGoals_Activity.class);
                intent.putExtra("sponserURl", my_goal_sponser_large_image);
                intent.putExtra("shareImageURl", my_goal_share_large_image);
                intent.putExtra("goalName", goalName);
                intent.putExtra("from", "achived");
                startActivity(intent);

            }
        });


        btn_achieve_yes = (Button) findViewById(R.id.btn_achieve_yes);
        btn_achieve_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAchieveGoal("achieveGoal", "yes");
            }
        });

        btn_achieve_no = (Button) findViewById(R.id.btn_achieve_no);
        btn_achieve_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAchieveGoal("achieveGoal", "no");
              //  finish();
               // setAchieveGoal("achieveGoal", "no");

            }
        });

    }



    public void setAchieveGoal(String serviceName, final String state) {
        MyProgressLoading.showLoading(AchivedGoal_Activity.this,"Please wait...");

        userid_ExistingUser=pref.getLoginUser().get("uid");
         mygoalId= pref.getMyGoalData().get("my_goal_id");

        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"," +
                        "\"is_achieved\": \"" + state + "\"," +
                        "\"user_goal_id\": \"" + mygoalId + "\"" +
                        "}";

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.goals.achieveGoal.MainResponse> call = apiService.achieveGoal(AppConfig.APP_BRANDING_ID,serviceName, "JSON","JSON",jsonStr);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.goals.achieveGoal.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.goals.achieveGoal.MainResponse> call, Response<com.ayubo.life.ayubolife.pojo.goals.achieveGoal.MainResponse> response) {
                MyProgressLoading.dismissDialog();

                if(state.equals("no")){
                  finish();
                }else{
                    if(response.isSuccessful()) {
                        Data data= response.body().getData();

                        pref.setIsGoalAdded("yes");

                        if(data!=null) {

                            if (data.getStatus()) {
                                String title = "Completed";
                                String buttonText = "Done";
                                pref.setMyGoalData(mygoalId, goalName, imageURL, large_imageURL, title, buttonText, my_goal_share_large_image, my_goal_sponser_large_image,"");
                                finish();

                            }
                        }
                        else{
                            finish();
                        }
                    }
                }

            }
            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.goals.achieveGoal.MainResponse> call, Throwable t) {

                MyProgressLoading.dismissDialog();

                System.out.println("========t======"+t);
            }
        });
    }
}
