package com.ayubo.life.ayubolife.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ayubo.life.ayubolife.R;

public class ThirdIntroViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String noticolor = "#242424";
        int whiteInt = Color.parseColor(noticolor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(whiteInt);
        }

        setContentView(R.layout.activity_third_intro_view);
    }

    public void click_GoFourthIntroView(View v){
        Intent in = new Intent(ThirdIntroViewActivity.this,   FourthIntroViewActivity.class);
        startActivity(in);
        finish();
    }


}
