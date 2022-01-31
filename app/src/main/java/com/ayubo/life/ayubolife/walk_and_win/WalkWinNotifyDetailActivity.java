package com.ayubo.life.ayubolife.walk_and_win;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;

public class WalkWinNotifyDetailActivity extends AppCompatActivity {

    TextView textViewNotificationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_win_notify_detail);
        textViewNotificationDetails = (TextView) findViewById(R.id.textViewNotificationDetails);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        textViewNotificationDetails.setText(intent.getStringExtra("count"));
    }
}