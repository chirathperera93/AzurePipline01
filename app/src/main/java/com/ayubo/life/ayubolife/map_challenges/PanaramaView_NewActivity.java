package com.ayubo.life.ayubolife.map_challenges;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ayubo.life.ayubolife.R;
import com.bumptech.glide.Glide;

public class PanaramaView_NewActivity extends AppCompatActivity {
//    GyroscopeObserver gyroscopeObserver;

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Register GyroscopeObserver.
//        gyroscopeObserver.register(this);
//    }

    //    @Override
//    protected void onPause() {
//        super.onPause();
//        // Unregister GyroscopeObserver.
//        gyroscopeObserver.unregister();
//    }
    ImageView panoramaImageView;
    //    com.gjiazhe.panoramaimageview.PanoramaImageView panoramaImageView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_panarama_view__new);

//        gyroscopeObserver = new GyroscopeObserver();
        // Set the maximum radian the device should rotate to show image's bounds.
        // It should be set between 0 and π/2.
        // The default value is π/9.
//        gyroscopeObserver.setMaxRotateRadian(Math.PI/9);

//        panoramaImageView = (com.gjiazhe.panoramaimageview.PanoramaImageView) findViewById(R.id.panorama_image_view);
        panoramaImageView = (ImageView) findViewById(R.id.panorama_image_view);
        // Set GyroscopeObserver for PanoramaImageView.
        String URL = getIntent().getStringExtra("URL");
        progressBar = (ProgressBar) findViewById(R.id.progress);
        getImage2(URL);

//        panoramaImageView.setGyroscopeObserver(gyroscopeObserver);
//        panoramaImageView.setEnablePanoramaMode(true);
//        panoramaImageView.setEnableScrollbar(true);
//        panoramaImageView.setInvertScrollDirection(true);

    }

    private void getImage2(final String URL) {
        progressBar.setVisibility(View.VISIBLE);
        try {
            Glide
                    .with(this)
                    .asBitmap()
                    .load(URL)
                    .into(panoramaImageView);
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
//                            progressBar.setVisibility(View.GONE);
//                            if (bitmap != null) {
//                                panoramaImageView.setImageBitmap(bitmap);
//                            }
//                        }
//
//                        @Override
//                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                            super.onLoadFailed(errorDrawable);
//                            // Do something.
//                            progressBar.setVisibility(View.GONE);
//                            getImage2(URL);
//                            Toast.makeText(getApplicationContext(), "Error ",
//                                    Toast.LENGTH_LONG).show();
//                        }
//
//                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
