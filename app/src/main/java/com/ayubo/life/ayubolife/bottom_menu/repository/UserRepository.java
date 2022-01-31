package com.ayubo.life.ayubolife.bottom_menu.repository;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ayubo.life.ayubolife.bottom_menu.models.User;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.pojo.timeline.Post;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.MyProgressLoading;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private String TAG = "UserRepository";
    private static UserRepository instanceU;
    List<Post> timelinePostLi = null;
    //  final MutableLiveData<Post> timelinePostData = new MutableLiveData<>();
    private MutableLiveData<List<Post>> mutableLiveData = new MutableLiveData<>();

    public static UserRepository getInstance() {
        if (instanceU == null) {
            instanceU = new UserRepository();
        }
        return instanceU;
    }


//    public MutableLiveData<List<Post>> getUser(String userId) {
//
//        final MutableLiveData<List<Post>> userData = new MutableLiveData<>();
//
//        userData.setValue(loadFirstPage_Posts(userId));
//
//        return userData;
//    }

    public User getCurrentUser(String serviceName) {
        User user = null;
        Log.d(TAG, ".................. getCurrentUser method called");
        user = new User("url", "Kamal Perera", "Engineer");

        return user;
    }


    public MutableLiveData<List<Post>> loadFirstPage_Posts(String appToke) {

        appToke = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvbGl2ZWhhcHB5LmF5dWJvLmxpZmVcL2FwaS5heXViby5saWZlXC9wdWJsaWNcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNTY3MzkxMzE0LCJleHAiOjE2MzA0NjMzMTQsIm5iZiI6MTU2NzM5MTMxNCwianRpIjoiMm5VR013blJ4aW5OU2dNbyIsInN1YiI6IjE2MDcwYmI1LTAyODUtMzhmMC1hOTBlLTU5YjdiZThhYzRjMiIsInBydiI6Ijg3ZTBhZjFlZjlmZDE1ODEyZmRlYzk3MTUzYTE0ZTBiMDQ3NTQ2YWEiLCJpZCI6IjE2MDcwYmI1LTAyODUtMzhmMC1hOTBlLTU5YjdiZThhYzRjMiJ9.EbtNUbav3iRJIFpZ4B9lhvkLYED4qV-HMU6HMoydAtU";
        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call = apiService.getAllPost(AppConfig.APP_BRANDING_ID, appToke, 1, 10, true, null);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Response<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> response) {
                MyProgressLoading.dismissDialog();
                com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline obj = response.body();

//                if(code==401)
                if (response.isSuccessful()) {

                    if (response.body().getResult() == 0) {
                        final com.ayubo.life.ayubolife.pojo.timeline.Data timeline_firstpage_data = obj.getData();
                        if (timeline_firstpage_data != null) {

                            timelinePostLi = (List<Post>) timeline_firstpage_data.getPosts();
                            mutableLiveData.setValue(timelinePostLi);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Throwable t) {
                MyProgressLoading.dismissDialog();
                t.printStackTrace();

            }
        });

        return mutableLiveData;
    }


}