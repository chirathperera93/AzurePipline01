package com.flavor.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrivacyActivity;
import com.ayubo.life.ayubolife.post.activity.NativePostActivity;
import com.ayubo.life.ayubolife.utility.Ram;

public class AboutActivity extends AppCompatActivity {
    TextView lbl_version, txt_privecy, txt_terms;
    LinearLayout lay_btnBack;
    ImageButton btn_backImgBtn;
    LinearLayout txt_termsl;

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

        txt_termsl = (LinearLayout) findViewById(R.id.txt_termsl);
        txt_termsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, NativePostActivity.class);
                intent.putExtra("meta", "919");
                startActivity(intent);
//                Intent i = new Intent(AboutActivity.this, TermsActivity.class);
//                startActivity(i);
                finish();

            }
        });


        txt_terms = (TextView) findViewById(R.id.txt_terms);
        txt_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, NativePostActivity.class);
                intent.putExtra("meta", "918");
                startActivity(intent);
//                Intent i = new Intent(AboutActivity.this, TermsActivity.class);
//                startActivity(i);
                finish();
            }
        });
        txt_privecy = (TextView) findViewById(R.id.txt_privecy);
        txt_privecy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AboutActivity.this, PrivacyActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
