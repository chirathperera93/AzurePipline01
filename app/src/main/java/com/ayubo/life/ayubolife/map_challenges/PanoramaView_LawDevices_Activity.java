package com.ayubo.life.ayubolife.map_challenges;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class PanoramaView_LawDevices_Activity extends AppCompatActivity {
    SensorManager mySensorManager;Sensor mSensor;HorizontalScrollView scrollView;
    ImageView btnimage;ProgressBar progressBar; String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_panorama_view__law_devices_);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        scrollView= (HorizontalScrollView)findViewById(R.id.hscrolview);
        btnimage= (ImageView)findViewById(R.id.btnimage);

        URL = getIntent().getStringExtra("URL");
     //    URL="http://everest.ayubo.life/images/sunset_at_pier.jpg";

      //  btnimage.setImageBitmap(bitmap);
       getImage2(URL);
    }

    private void getImage2(final String URL) {
        progressBar.setVisibility(View.VISIBLE);
        try {
            Glide
                    .with(this).asBitmap().load(URL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            progressBar.setVisibility(View.GONE);
                            if(bitmap!=null){
                                btnimage.setImageBitmap(bitmap);
                            }
                        }


                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            getImage2(URL);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Error loading",
                                    Toast.LENGTH_LONG).show();
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
