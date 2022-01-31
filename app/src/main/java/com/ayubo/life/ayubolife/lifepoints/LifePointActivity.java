package com.ayubo.life.ayubolife.lifepoints;

import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;

import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.flavors.changes.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LifePointActivity extends AppCompatActivity {

    String userid_ExistingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String noticolor = "#c49527";
        int whiteInt = Color.parseColor(noticolor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(whiteInt);
        }


         if (Constants.type == Constants.Type.LIFEPLUS) {
            String noticolr = "#000000";
            int whiteInta = Color.parseColor(noticolr);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(whiteInta);

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = getWindow().getDecorView();

                //    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                decor.setSystemUiVisibility(0);
                //   decor.setSystemUiVisibility(0);
                // }
            }
        }

        setContentView(R.layout.activity_life_point);


        callViewGoals("getLifePoints");
    }




    public void callViewGoals(String serviceName) {
       // MyProgressLoading.showLoading(getApplicationContext(),"Please wait...");

        PrefManager pref;

        pref=new PrefManager(LifePointActivity.this);

        userid_ExistingUser=pref.getLoginUser().get("uid");

        String jsonStr =
                "{" +

                        "\"user_id\": \"" + userid_ExistingUser + "\"" +
                        "}";

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.lifepoints.MainResponse> call = apiService.getLifePoints(AppConfig.APP_BRANDING_ID,serviceName, "JSON","JSON",jsonStr);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.lifepoints.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.lifepoints.MainResponse> call, Response<com.ayubo.life.ayubolife.lifepoints.MainResponse> response) {
               // MyProgressLoading.dismissDialog();

                if(response.body().getResult()==0){
                    String points=  response.body().getPoints();
                    TextView txt_stpes=findViewById(R.id.txt_stpes);
                    txt_stpes.setText(points);
                }

                System.out.println("========t======");

            }
            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.lifepoints.MainResponse> call, Throwable t) {

            //    MyProgressLoading.dismissDialog();

                System.out.println("========t======"+t);
            }
        });
    }





}
