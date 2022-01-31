package com.ayubo.life.ayubolife.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;



public class QRScannerActivity extends AppCompatActivity {


    Button ScanButton;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.main);
        // Set the scanner view as the content view
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        ScanButton = (Button) findViewById(R.id.ScanButton);

        ScanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BarcodeScanner.class);
                startActivity(intent);
            }
        });
    }

    public void onPause() {
        super.onPause();

    }

    /**
     * A safe way to get an instance of the Camera object.
     */




}
