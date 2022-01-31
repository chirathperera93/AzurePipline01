package com.ayubo.life.ayubolife.timeline;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


public class TimelineImage_Activity extends AppCompatActivity  {
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;
    RelativeLayout main;
    String TURL;
    String URL;
    PrefManager pref=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //BigImageViewer.initialize(FrescoImageLoader.with(getApplicationContext()));

        setContentView(R.layout.activity_timeline_image_);
        pref = new PrefManager(TimelineImage_Activity.this);
        main = (RelativeLayout)findViewById(R.id.main);

        pref=new PrefManager(TimelineImage_Activity.this);

        Integer parentPosition= getIntent().getIntExtra("position",0);
        URL = getIntent().getStringExtra("URL");
        TURL = getIntent().getStringExtra("TURL");

        pref.setPreviousPosition(parentPosition);

        if((URL.equals("") &&(URL==null))){
            URL=TURL;
        }

      //  btn_layout_close
        LinearLayout btn_layout_close=(LinearLayout)findViewById(R.id.btn_layout_close) ;

        ImageButton btn_close=(ImageButton)findViewById(R.id.btn_close) ;
        //BigImageView bigImageView = (BigImageView) findViewById(R.id.mBigImage);
        ImageView bigImageView = (ImageView) findViewById(R.id.mBigImage);
       // bigImageView.setProgressIndicator(new ProgressPieIndicator());

        System.out.println("=================TURL======================="+TURL);
        System.out.println("=================URL======================="+URL);

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(TimelineImage_Activity.this).load(URL)
                .apply(requestOptions)
                .into(bigImageView);
//        bigImageView.showImage(
//                Uri.parse(URL)
//        );


        btn_layout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    public boolean onTouchEvent(MotionEvent motionEvent) {
//        mScaleGestureDetector.onTouchEvent(motionEvent);
//        return true;
//    }
//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//        @Override
//        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
//
//            float scaleFactor = scaleGestureDetector.getScaleFactor();
//
//            if (scaleFactor >= 1) {
//               System.out.println("===============Zoom Out============="+scaleFactor);
//                mScaleFactor *= scaleGestureDetector.getScaleFactor();
//                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
//                mImageView.setScaleX(mScaleFactor);
//                mImageView.setScaleY(mScaleFactor);
//            } else {
//                System.out.println("===============Zoom In ============="+scaleFactor);
//                mScaleFactor *= scaleGestureDetector.getScaleFactor();
//                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
//                mImageView.setScaleX(mScaleFactor);
//                mImageView.setScaleY(mScaleFactor);
//            }
//
//
//            return true;
//        }
//    }
}
