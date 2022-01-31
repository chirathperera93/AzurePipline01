package com.ayubo.life.ayubolife.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;

public class PrivacyActivity extends AppCompatActivity {
    String hasToken, loginType;

    SharedPreferences prefs;
    String userid_ExistingUser;
    LinearLayout lay_btnBack;  ImageButton btn_backImgBtn;
    String encodedHashToken,statusFromServiceAPI_db;
    int result;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;

    public void goBackToHome(){
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);



        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToHome();
            }
        });

        lay_btnBack=(LinearLayout)findViewById(R.id.lay_btnBack);
        lay_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToHome();
            }
        });



    }
}
