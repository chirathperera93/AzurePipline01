package com.ayubo.life.ayubolife.reports.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.reports.model.AssignUserMainResponse;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignReportActivity extends AppCompatActivity {
    PrefManager pref;
    String current_user_id;
    String patient_id;
    String hos_uid;

    ProgressDialog progressDialog;
    TextView txt_assign_to_me,txt_assign_to_member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_report);

        uiConstructorWithEvents();

        dataConstructor();

    }

    void dataConstructor(){
        pref = new PrefManager(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        current_user_id= getIntent().getStringExtra("current_user_id");
        patient_id= getIntent().getStringExtra("patient_id");
        hos_uid= getIntent().getStringExtra("hos_uid");


    }

    void uiConstructorWithEvents(){

        txt_assign_to_me=findViewById(R.id.txt_assign_to_me);
        txt_assign_to_member=findViewById(R.id.txt_assign_to_member);

        txt_assign_to_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assignAReports(current_user_id,hos_uid);
            }
        });


        txt_assign_to_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in=new Intent(AssignReportActivity.this,AssignUserActivity.class);
                in.putExtra("hos_uid", hos_uid);
                startActivity(in);
                finish();

            }
        });

    }

    public void assignAReports(String patient_id,String hos_uid) {
        String  appToken = pref.getUserToken();
        progressDialog.show();
        ApiInterface apiService = ApiClient.getReportsApiClient().create(ApiInterface.class);
        Call<AssignUserMainResponse> call = apiService.assignAReportsToUser(AppConfig.APP_BRANDING_ID,appToken,patient_id,hos_uid);

        call.enqueue(new Callback<AssignUserMainResponse>() {
            @Override
            public void onResponse(Call<AssignUserMainResponse> call, Response<AssignUserMainResponse> response) {
                if(progressDialog!=null){
                    progressDialog.cancel();
                }

                if(response.isSuccessful()) {
                   pref.setFamilyMemberId(current_user_id);

                    System.out.println("========t======"+response.body().getResult());
                    pref.setReportLoadingStatus("1");
                    Intent in=new Intent(AssignReportActivity.this,ReportDetailsActivity.class);
                    startActivity(in);
                   finish();

                }else{
                    System.out.println("========tt======"+response.body().getResult());
                }
            }
            @Override
            public void onFailure(Call<AssignUserMainResponse> call, Throwable t) {
                if(progressDialog!=null){
                    progressDialog.cancel();
                }
                Toast.makeText(AssignReportActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
            }
        });
    }

}
