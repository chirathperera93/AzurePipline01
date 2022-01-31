package com.ayubo.life.ayubolife.bottom_menu.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.bottom_menu.viewmodel.HealthActivityVM;
import com.ayubo.life.ayubolife.pojo.timeline.Post;

import java.util.List;

public class HealthActivity extends AppCompatActivity {

    private HealthActivityVM mMainActivityViewModel;
    private ImageView mProfileImage;
    private TextView mName;
    private TextView mOccupation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);


        mProfileImage = findViewById(R.id.img_picture);
        mName = findViewById(R.id.txt_name);
        mOccupation = findViewById(R.id.txt_occupation);

        mMainActivityViewModel = ViewModelProviders.of(this)
                .get(HealthActivityVM.class);

        mMainActivityViewModel.queryRepo("userID");

        mMainActivityViewModel.getUser()
                .observe(this, new Observer<List<Post>>() {
                    @Override
                    public void onChanged(List<Post> user) {
                        // Set profile image

                        List<Post> timelinePostLi = user;
//                        mName.setText(user.getName());
//                        mOccupation.setText(user.getOccupation());
                    }
                });
    }


//    public void update(View view) {
//        User user=null;
//
//        user=new User("url","Sunil Mendia","Artist");
//       // updatedUser.set(newName);
//        mMainActivityViewModel.updateUser(user);
//    }
}
