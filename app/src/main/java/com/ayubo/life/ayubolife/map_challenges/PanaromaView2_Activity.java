package com.ayubo.life.ayubolife.map_challenges;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class PanaromaView2_Activity extends AppCompatActivity {
    SensorManager mySensorManager;
    Sensor mSensor;
    int width;
    HorizontalScrollView scrollView;
    ImageView btnimage;
    boolean isTouch = false;
    ///TextView txt_name1,txt_name2;
    String URL;
    ProgressBar progressBar;
    boolean isImageLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String noticolor = "#95000000";
        int whiteInt = Color.parseColor(noticolor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(whiteInt);
        }

        setContentView(R.layout.activity_panaroma_view2_);

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // (1)

        mSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // (2)

        mySensorManager.registerListener(mySensorEventListener, mySensorManager
                        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                5); // (3)

        scrollView = (HorizontalScrollView) findViewById(R.id.hscrolview);
        //txt_name2= (TextView)findViewById(R.id.txt_name2);
        // txt_name1= (TextView)findViewById(R.id.txt_name1);
        //txt_name1.setVisibility(View.GONE);  txt_name2.setVisibility(View.GONE);
        btnimage = (ImageView) findViewById(R.id.btnimage);

        URL = getIntent().getStringExtra("URL");
        System.out.println("========URL==================" + URL);
        String url2 = "https://devo.ayubo.life/custom/include/images/everest/everest.jpg";

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar = new ProgressBar(PanaromaView2_Activity.this);

        getImage2(url2);

    }

    private void getImage2(String URL) {
        progressBar.setVisibility(View.VISIBLE);
        try {
            RequestOptions myOptions = new RequestOptions()
                    .fitCenter();
            Glide
                    .with(this).asBitmap().load(URL)
                    .apply(myOptions)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            progressBar.setVisibility(View.GONE);
                            if (bitmap != null) {

                                btnimage.setImageBitmap(bitmap);
                                isImageLoaded = true;
                                //sphericalView.setPanorama(bitmap, true);
                            }

                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Error loading",
                                    Toast.LENGTH_LONG).show();
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final SensorEventListener mySensorEventListener = new SensorEventListener() {
        boolean isFirst = true;
        private float[] gravity = new float[3];
        float[] history = new float[2];
        String[] direction = {"NONE", "NONE"};

        public void onSensorChanged(SensorEvent event) {

            Sensor portrait = event.sensor;

            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)

                if (isImageLoaded) {
                    System.out.println("================isImageLoaded=================" + isImageLoaded);
                    focusOnView2(scrollView, btnimage, x, y, z);
                }


        }

        public void onAccuracyChanged(Sensor portrait, int accuracy) {
            /* can be ignored in this example */
        }


    };

    private final void focusOnView2(final HorizontalScrollView scroll, final View view, final int x, final int y, final int z) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int vLeft = view.getLeft();
                int vRight = view.getRight();
                int sWidth = scroll.getWidth();
                // ===================720   0     2686
                // System.out.println("==================="+sWidth+"   "+vLeft+"     "+vRight);
                int sss = (vLeft + vRight - sWidth) / 2;
                // System.out.println("===========2========"+sss);


                //   if(isTouch) {
                // System.out.println("========isTouch======");

                if (y < 0) {
                    // negative
                    scroll.smoothScrollTo(sss, 0);
//                    txt_name1.setVisibility(View.VISIBLE);
//                    txt_name2.setVisibility(View.VISIBLE);
                    // scroll.smoothScrollBy(2 * 1, 0);
                } else if (y == 0 || y == 9) {
//                    txt_name1.setVisibility(View.VISIBLE);
//                    txt_name2.setVisibility(View.VISIBLE);
                    scroll.smoothScrollTo(sss, 0);
                } else {
                    int w = 0;
//                    txt_name1.setVisibility(View.GONE);
//                    txt_name2.setVisibility(View.GONE);
                    if (y == 10) {
                        w = 1;
                    } else if (y == 9) {
                        w = 1;
                    } else if (y == 8) {
                        w = 1;
                    } else if (y == 7) {
                        w = 1;
                    } else if (y == 6) {
                        w = 1;
                    } else if (y == 5) {
                        w = 1;
                    } else if (y == 4) {
                        w = 1;
                    } else if (y == 3) {
                        w = 1;
                    } else if (y == 2) {
                        w = 1;
                    } else if (y == 1) {
                        w = 1;
                    } else if (y == 0) {
                        w = 1;
                    }

                    //    scroll.smoothScrollBy( -sss, 0 );
                    scroll.smoothScrollBy(1, 0);
                    // scroll.smoothScrollBy(2*sss , 0);
                    // scroll.smoothScrollTo(-sss, 0);
                    //  scroll.smoothScrollBy(2*sss, 0);
                }
                //  }


//                scroll.smoothScrollTo(((vLeft + vRight) / 2) - (sWidth / 2), 0);
//                float sss2=((vLeft + vRight) / 2) - (sWidth / 2), 0);
//                System.out.println("===========2========"+sss2);
            }
        });
    }
}
