package com.ayubo.life.ayubolife.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.common.SetDiscoverPage;

public class FourthIntroViewActivity extends AppCompatActivity {

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

        setContentView(R.layout.activity_fourth_intro_view);
    }

    public void click_GoWelcomeHome(View v) {
//        Intent in = new Intent(FourthIntroViewActivity.this,   NewHomeWithSideMenuActivity.class);
//        Intent in = new Intent(FourthIntroViewActivity.this,   LifePlusProgramActivity.class);
//        Intent in = new Intent(FourthIntroViewActivity.this, NewDiscoverActivity.class);
        Intent in = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
        startActivity(in);
        finish();
    }


}
