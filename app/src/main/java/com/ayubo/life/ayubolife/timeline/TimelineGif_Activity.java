package com.ayubo.life.ayubolife.timeline;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ayubo.life.ayubolife.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import javax.sql.DataSource;

public class TimelineGif_Activity extends AppCompatActivity {
    private ImageView mImageView;
    ImageButton img_btn_close;
    RelativeLayout main;
    String gifUrl = "";
    LinearLayout lay_btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_gif_);

        gifUrl = getIntent().getStringExtra("URL");

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);

        System.out.println("=========GIF URL========"+gifUrl);

        mImageView=(ImageView)findViewById(R.id.imageView);
        img_btn_close=(ImageButton)findViewById(R.id.img_btn_close);

        lay_btn_close=(LinearLayout)findViewById(R.id.lay_btn_close);

        Glide
                .with(TimelineGif_Activity.this)
                .load(gifUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }
                }).into(mImageView);

        lay_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
