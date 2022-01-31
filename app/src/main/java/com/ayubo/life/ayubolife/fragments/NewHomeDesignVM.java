package com.ayubo.life.ayubolife.fragments;

import android.content.Context;
import android.widget.Toast;

import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.MyProgressLoading;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewHomeDesignVM {

    PrefManager pref;
    Context context;
    String userid_ExistingUser;
    com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse mainResponse=null;
    public NewHomeDesignVM(Context conte){
        context=conte;
        pref = new PrefManager(context);
        userid_ExistingUser = pref.getLoginUser().get("uid");
    }

    com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse loadMainService(boolean isNeedShowProgress) {

        if (isNeedShowProgress) {
            MyProgressLoading.showLoading(context, "Please wait...");
        }

        System.out.println("====homePageTiles===================");

        String serviceName = "homePageTiles";
        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"" +
                        "}";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MainResponse> call = apiService.callMainService(AppConfig.APP_BRANDING_ID,serviceName, "JSON", "JSON", jsonStr);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> call, Response<MainResponse> response) {
                MyProgressLoading.dismissDialog();
                if (response.isSuccessful()) {

                    mainResponse = response.body();
                 //   NewHomeDesign.processMainResults(mainResponse);
                    System.out.println("====Path n 2kkkk=====================");


                }
            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> call, Throwable t) {

                MyProgressLoading.dismissDialog();

                Toast.makeText(context, "Server is temporarily unavailable", Toast.LENGTH_LONG).show();

            }
        });

        return mainResponse;
    }

}
