package com.ayubo.life.ayubolife.reports.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.FamilyMember;
import com.ayubo.life.ayubolife.reports.adapters.FamilyMemberAdapter;
import com.ayubo.life.ayubolife.reports.model.AssignUserMainResponse;
import com.ayubo.life.ayubolife.reports.model.FamilyMemberData;
import com.ayubo.life.ayubolife.reports.model.FamilyMemberMainResponse;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignUserActivity extends AppCompatActivity {

    String userid_ExistingUser;
    private PrefManager pref;
    Gson gson =null;
    List<FamilyMemberData> dataList;
    String hos_uid;
    ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_user);

        dataConstructor();

        uiEventLister();


        DBString dataObj=  DBRequest.getCashDataByType(AssignUserActivity.this,"reports_family_member");

        if(dataObj!=null){
            String  products_list = dataObj.getId();
            if ((products_list != null) && (products_list.length() > 5)) {
                Type type = new TypeToken<FamilyMemberMainResponse>() {
                }.getType();
                FamilyMemberMainResponse resp = gson.fromJson(products_list, type);
                List<FamilyMemberData>  data = (ArrayList<FamilyMemberData>) resp.getData();
                displayView(data);
            }
        }


        getAllFamilyMembers();

    }

    void dataConstructor(){

        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        gson = new Gson();
        hos_uid= getIntent().getStringExtra("hos_uid");
        progress=new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Please wait...");
    }

    void uiEventLister(){

        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btn_back_Button = (ImageButton) findViewById(R.id.btn_back_Button);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView txt_add_member1 = (TextView) findViewById(R.id.txt_add_member1);
        TextView txt_add_member2 = (TextView) findViewById(R.id.txt_add_member2);
        LinearLayout layout_add_member= findViewById(R.id.layout_add_member);

        layout_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssignUserActivity.this, CreateFamilyMemberActivity.class);
                startActivity(intent);
            }
        });
        txt_add_member1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssignUserActivity.this, CreateFamilyMemberActivity.class);
                startActivity(intent);
            }
        });
        txt_add_member2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssignUserActivity.this, CreateFamilyMemberActivity.class);
                startActivity(intent);
            }
        });


    }


    public void assignAReports(String patient_id,String hos_uid) {
        String  appToken = pref.getUserToken();
        progress.show();
        ApiInterface apiService = ApiClient.getReportsApiClient().create(ApiInterface.class);
        Call<AssignUserMainResponse> call = apiService.assignAReportsToUser(AppConfig.APP_BRANDING_ID,appToken,patient_id,hos_uid);

        call.enqueue(new Callback<AssignUserMainResponse>() {
            @Override
            public void onResponse(Call<AssignUserMainResponse> call, Response<AssignUserMainResponse> response) {

                if(progress!=null){
                    progress.cancel();
                }
                if(response.isSuccessful()) {
                    System.out.println("========t======"+response.body().getResult());
                    pref.setReportLoadingStatus("1");
                 //   Intent in=new Intent(AssignUserActivity.this,ReportDetailsActivity.class);
                 //   startActivity(in);
                    finish();
                }else{
                    System.out.println("========tt======"+response.body().getResult());
                }
            }
            @Override
            public void onFailure(Call<AssignUserMainResponse> call, Throwable t) {
                if(progress!=null){
                    progress.cancel();
                }
                  Toast.makeText(AssignUserActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                System.out.println("========t======"+t);
            }
        });
    }


    public void getAllFamilyMembers() {
        String  appToken = pref.getUserToken();

        ApiInterface apiService = ApiClient.getReportsApiClient().create(ApiInterface.class);
        Call<FamilyMemberMainResponse> call = apiService.getAllFamilyMembersOld(AppConfig.APP_BRANDING_ID,appToken);

        call.enqueue(new Callback<FamilyMemberMainResponse>() {
            @Override
            public void onResponse(Call<FamilyMemberMainResponse> call, Response<FamilyMemberMainResponse> response) {

                if(response.isSuccessful()) {

                    Gson gson1 = new Gson();
                    FamilyMemberMainResponse obj=   response.body();

                    String jsonCars = gson1.toJson(response.body());

                    DBRequest.updateDoctorData(AssignUserActivity.this,jsonCars,"reports_family_member");

                    dataList=  obj.getData();

                    displayView(dataList);

                }
            }
            @Override
            public void onFailure(Call<FamilyMemberMainResponse> call, Throwable t) {

                Toast.makeText(AssignUserActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                System.out.println("========t======"+t);
            }
        });
    }

    void displayView(List<FamilyMemberData> familyData){
        RecyclerView recyclerView = findViewById(R.id.recycler_familymemeber);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        FamilyMemberAdapter adapter = new FamilyMemberAdapter(this, familyData);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new FamilyMemberAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object) {

               FamilyMemberData mem =(FamilyMemberData) object;
               pref.setFamilyMemberId(mem.getPatientId());
               assignAReports(mem.getPatientId(),hos_uid);

            }
        } );
    }


}

