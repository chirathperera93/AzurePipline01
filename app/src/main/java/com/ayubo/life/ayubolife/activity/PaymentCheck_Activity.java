package com.ayubo.life.ayubolife.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.home_popup_menu.AskQuestion_Activity;
import com.ayubo.life.ayubolife.mobile_login.Config;
import com.ayubo.life.ayubolife.model.PayData;
import com.ayubo.life.ayubolife.model.PayHeraMainResponse;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentCheck_Activity extends AppCompatActivity {
    ImageButton btn_back_Button;
    PrefManager pref;
    String status;
    Button btn_use_trial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_check_);

        btn_back_Button = (ImageButton) findViewById(R.id.btn_back_Button);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pref = new PrefManager(PaymentCheck_Activity.this);
        status = getIntent().getStringExtra("status");
        btn_use_trial=findViewById(R.id.btn_use_trial);
        if(status!=null){

            if(status.equals("hide")){
                btn_use_trial.setVisibility(View.INVISIBLE);
            }else{
                btn_use_trial.setVisibility(View.VISIBLE);
            }
        }else{
            btn_use_trial.setVisibility(View.VISIBLE);
        }
    }

    public void getTrailRun() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String appToken = pref.getUserToken();

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<PayHeraMainResponse> call = apiService.getTrailRun(AppConfig.APP_BRANDING_ID,appToken,"global_chat");

        call.enqueue(new Callback<PayHeraMainResponse>() {
            @Override
            public void onResponse(Call<PayHeraMainResponse> call, Response<PayHeraMainResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {

                    Intent intent = new Intent(PaymentCheck_Activity.this, AskQuestion_Activity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PayHeraMainResponse> call, Throwable t) {
                progressDialog.cancel();
                System.out.println(t);
            }
        });
    }

    public void getPayLink() {
      final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String appToken = pref.getUserToken();

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<PayHeraMainResponse> call = apiService.setPayLink(AppConfig.APP_BRANDING_ID,appToken,"global_chat");

        call.enqueue(new Callback<PayHeraMainResponse>() {
            @Override
            public void onResponse(Call<PayHeraMainResponse> call, Response<PayHeraMainResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {

                    PayData data=  response.body().getData();

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getPaymentLink()));
                    startActivity(browserIntent);
                }
            }

            @Override
            public void onFailure(Call<PayHeraMainResponse> call, Throwable t) {
                progressDialog.cancel();
                System.out.println(t);
            }
        });
    }

    public void clickPay(View view) {

        getPayLink();
    }


    public void clickTrial(View view) {

        getTrailRun();
    }
}
