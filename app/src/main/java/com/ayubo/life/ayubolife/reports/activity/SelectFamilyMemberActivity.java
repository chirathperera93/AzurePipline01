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
import com.ayubo.life.ayubolife.channeling.activity.DetailActivity;
import com.ayubo.life.ayubolife.channeling.adapter.SearchAdapter;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.FamilyMember;
import com.ayubo.life.ayubolife.reports.adapters.FamilyMemberAdapter;
import com.ayubo.life.ayubolife.reports.model.DeleteMemberMainResponse;
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

public class SelectFamilyMemberActivity extends AppCompatActivity {
   ProgressDialog mProgressDialog;
    String userid_ExistingUser;
    private PrefManager pref;
    Gson gson =null;
    List<FamilyMemberData> dataList;
    private RecyclerView recyclerView;
    FamilyMemberAdapter adapter;

    void uiConstructorWithEvents(){

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
                Intent intent = new Intent(SelectFamilyMemberActivity.this, CreateFamilyMemberActivity.class);
                startActivity(intent);
            }
        });
        txt_add_member1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectFamilyMemberActivity.this, CreateFamilyMemberActivity.class);
                startActivity(intent);
            }
        });
        txt_add_member2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectFamilyMemberActivity.this, CreateFamilyMemberActivity.class);
                startActivity(intent);
            }
        });

    }
    void dataConstructor(){
        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        mProgressDialog=new ProgressDialog(SelectFamilyMemberActivity.this);
        gson = new Gson();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_family_member2);


        uiConstructorWithEvents();
        dataConstructor();



        DBString dataObj=  DBRequest.getCashDataByType(SelectFamilyMemberActivity.this,"reports_family_member");

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

    public void deleteMemberReport(String memberID) {

        String  appToken = pref.getUserToken();

        ApiInterface apiService = ApiClient.getReportsApiClient().create(ApiInterface.class);
        Call<DeleteMemberMainResponse> call = apiService.deleteFamilyMember(AppConfig.APP_BRANDING_ID,appToken,memberID);

        call.enqueue(new Callback<DeleteMemberMainResponse>() {
            @Override
            public void onResponse(Call<DeleteMemberMainResponse> call, Response<DeleteMemberMainResponse> response) {
                if(mProgressDialog!=null){
                    mProgressDialog.cancel();
                }
                if(response.isSuccessful()) {

                    //  pref.setReportLoadingStatus("1");
                    getAllFamilyMembers();
                 //   getAllReportsData(true,"all",true);
                    System.out.println("=======t======");

                }
            }
            @Override
            public void onFailure(Call<DeleteMemberMainResponse> call, Throwable t) {
                if(mProgressDialog!=null){
                    mProgressDialog.cancel();
                }

                Toast.makeText(SelectFamilyMemberActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
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

                    DBRequest.updateDoctorData(SelectFamilyMemberActivity.this,jsonCars,"reports_family_member");

                    dataList=  obj.getData();

                    displayView(dataList);

                }
            }
            @Override
            public void onFailure(Call<FamilyMemberMainResponse> call, Throwable t) {

                Toast.makeText(SelectFamilyMemberActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                System.out.println("========t======"+t);
            }
        });
    }

    void displayView( List<FamilyMemberData> familyData){

        recyclerView = findViewById(R.id.recycler_familymemeber);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new FamilyMemberAdapter(this, familyData);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnSelectAssignUserListener(new FamilyMemberAdapter.OnClickAssignUserImage() {
            @Override
            public void onSelectAssignUser(final FamilyMemberData objj,int pos) {


                showAlert_ConfirmDelete(SelectFamilyMemberActivity.this, objj,pos,"Are you sure ?");


                System.out.println("=====Delete===================onClick");
            }
        });
        adapter.setOnItemClickListener(new FamilyMemberAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object) {

                System.out.println("========================onClick");
                FamilyMemberData mem = (FamilyMemberData)object;

                pref.setFamilyMemberId(mem.getPatientId());

                Intent intent = new Intent(SelectFamilyMemberActivity.this, ReportDetailsActivity.class);
                startActivity(intent);

                finish();
            }
        } );
    }

    void showAlert_ConfirmDelete(Context c, final FamilyMemberData reportO,final int poss, String msg) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);


        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                dataList.remove(poss);
                adapter.notifyDataSetChanged();

                deleteMemberReport(reportO.getPatientId());

            }
        });
        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        dialog.show();
    }

}
