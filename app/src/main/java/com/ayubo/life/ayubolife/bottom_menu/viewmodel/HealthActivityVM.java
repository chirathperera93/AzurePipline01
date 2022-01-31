package com.ayubo.life.ayubolife.bottom_menu.viewmodel;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.ayubo.life.ayubolife.bottom_menu.repository.UserRepository;
import com.ayubo.life.ayubolife.pojo.timeline.Post;

import java.util.List;

public class HealthActivityVM extends ViewModel {
   private String TAG="HealthActivityVM";
    private UserRepository userRepo;
    private MutableLiveData<List<Post>> user;

    public LiveData<List<Post>> getUser() {
        return user;
    }

    public void queryRepo(String userId) {
        Log.d(TAG,".............. queryRepo method called");

        userRepo = UserRepository.getInstance();
        user = userRepo.loadFirstPage_Posts(userId);

        Log.d(TAG,".............. queryRepo method called");

    }

//    public void updateUser(User updatedUser) {
//        user.postValue(updatedUser);
//    }
}