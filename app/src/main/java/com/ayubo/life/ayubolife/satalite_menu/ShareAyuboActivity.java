package com.ayubo.life.ayubolife.satalite_menu;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.ayubo.life.ayubolife.R;

public class ShareAyuboActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_ayubo);


        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Check out the all in one ayubo.life wellness app. https://www.ayubo.life/download");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } catch(Exception e) {
            e.toString();
        }

    }
}
