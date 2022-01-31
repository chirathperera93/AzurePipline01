package com.flavor.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.utility.Ram;
import com.flavors.changes.Constants;

public class AboutActivity extends BaseActivity {
    TextView lbl_version, txt_privecy, terms_text, privacy_text;
    LinearLayout lay_btnBack;
    ImageButton btn_backImgBtn;
    LinearLayout terms_layout, privacy_layout;

    public void goBackToHome() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String noticolor = "#c49f12";
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
        setContentView(R.layout.activity_about);

        String versionName = BuildConfig.VERSION_NAME;


        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_about__mainbackImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        lbl_version = (TextView) findViewById(R.id.lbl_version);
        lbl_version.setText("Version " + versionName);
        Ram.setSideMenuItemNumber("35");

        terms_layout = (LinearLayout) findViewById(R.id.terms_layout);
        terms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processAction("native_post", "919");
            }
        });


        terms_text = (TextView) findViewById(R.id.terms_text);
        terms_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAction("native_post", "919");
            }
        });
        privacy_text = (TextView) findViewById(R.id.privacy_text);
        privacy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAction("native_post", "918");
            }
        });

    }


}
