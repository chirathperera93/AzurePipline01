package com.ayubo.life.ayubolife.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.fragments.TermasandConditionFragment;
import com.ayubo.life.ayubolife.utility.Utility;

import java.io.IOException;
import java.io.InputStream;

public class TermsActivity extends AppCompatActivity {
    String hasToken, loginType;

    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;
    ImageButton btn_signout;
    TextView btn_Appointment;
    WebView webView;
    SharedPreferences prefs;
    String userid_ExistingUser;
    LinearLayout lay_btnBack;  ImageButton btn_backImgBtn;
    String encodedHashToken,statusFromServiceAPI_db;
    int result;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    // TODO: Rename and change types of parameters

    String url=null;ImageButton  boBack;

    private ProgressDialog prgDialog_ch,progressBar;
    public void goBackToHome(){
        finish();
    }

    // TODO: Rename and change types of parameters
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        webView = (WebView) findViewById(R.id.webView_ter);


        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });

        lay_btnBack=(LinearLayout)findViewById(R.id.lay_btnBack);
        lay_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToHome();
            }
        });
        prgDialog_ch = new ProgressDialog(TermsActivity.this);


        String htmlFilename = "tos.htm";
        AssetManager mgr = getBaseContext().getAssets();
        try {
            InputStream in = mgr.open(htmlFilename, AssetManager.ACCESS_BUFFER);
            String htmlContentInStringFormat = Utility.StreamToString(in);
            in.close();
            webView.loadDataWithBaseURL(null, htmlContentInStringFormat, "text/html", "utf-8", null);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
