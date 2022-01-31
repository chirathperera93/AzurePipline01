package com.ayubo.life.ayubolife.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;

public class TermsDiliveMedicinActivity extends AppCompatActivity {
    ImageButton btn_backImgBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_dilive_medicin);

        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Intent i = new Intent(TermsDiliveMedicinActivity.this, Medicine_ViewActivity.class);
               // startActivity(i);
                finish();

            }
        });

    }
}
