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
import com.ayubo.life.ayubolife.activity.PrefManager;

public class Welcome_Animation_Activity extends AppCompatActivity {
    PrefManager pref;
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

        setContentView(R.layout.activity_welcome__animation_);
        pref=new PrefManager(Welcome_Animation_Activity.this);

//        Animation fadeIn = new AlphaAnimation(0, 1);
//        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
//        fadeIn.setDuration(1000);
//
//        Animation fadeOut = new AlphaAnimation(1, 0);
//        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
//        fadeOut.setStartOffset(1000);
//        fadeOut.setDuration(1000);
//
//        AnimationSet animation = new AnimationSet(false); //change to false
//        animation.addAnimation(fadeIn);
//        animation.addAnimation(fadeOut);
//        this.setAnimation(animation);

    }

    public void click_GoToFirstIntroView(View v){

        if(pref.isNeedLanguageSelection()){
            Intent in = new Intent(this, LanguageSelectionActivity.class);
            startActivity(in);
            finish();
        }else {
            Intent hhtpIntent = new Intent(Welcome_Animation_Activity.this, FirstInroViewActivity.class);
            startActivity(hhtpIntent);
            finish();
        }
    }
}
