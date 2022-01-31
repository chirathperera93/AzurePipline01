package com.ayubo.life.ayubolife.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;

import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.flavors.changes.Constants;

public class LoginActivity_First extends AppCompatActivity {
    ApiInterface apiService;
    ProgressDialog prgDialog;
    private PrefManager pref;
    String user_id,Name,lName,email,gender,profile_pic,healthData,code,hashToken;

    AppCompatEditText edtPhoneNumber;
    int index;
    String loginStatus="";
    String SMSResult,SMSCode;
    String userRegisterStatus;
    TextView txt_moblie_number; String notiBarColor;
   // CountryCodePicker ccp;
    String mobile,country_code,userid_ExistingUser,checkAppVersion_status;
    Button next_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Constants.type == Constants.Type.LIFEPLUS) {
            notiBarColor = "#000000";
        }else{
            notiBarColor = "#c49f12";
        }


        int whiteInt = Color.parseColor(notiBarColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(whiteInt);
        }

        country_code="";

        setContentView(R.layout.activity_login__first);


        txt_moblie_number = findViewById(R.id.txt_moblie_number);
        txt_moblie_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity_First.this,   EnterMobileNumberActivity.class);
                startActivity(in);
                finish();
            }

        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        return super.onKeyDown(keyCode, event);
    }




}
