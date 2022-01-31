package com.ayubo.life.ayubolife.map_challenges;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.ayubo.life.ayubolife.R;

public class SimpleWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_web_view);
        String web_url = getIntent().getStringExtra("URL");
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl(web_url);



    }

    @Override
    public void onBackPressed() {

       System.out.println("==========================");

       finish();

        super.onBackPressed();
    }

}
