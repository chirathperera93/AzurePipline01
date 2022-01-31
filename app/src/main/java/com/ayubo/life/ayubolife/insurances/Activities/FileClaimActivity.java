package com.ayubo.life.ayubolife.insurances.Activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.insurances.Adapters.FileClaimAdapter;
import com.ayubo.life.ayubolife.insurances.Classes.InsuranceResponseDataArrayList;
import com.ayubo.life.ayubolife.insurances.GenericItems.FileClaimItem;
import com.ayubo.life.ayubolife.insurances.CommonApiInterface;
import com.ayubo.life.ayubolife.insurances.InsurancePrefManager;
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.flavors.changes.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;

public class FileClaimActivity extends AppCompatActivity implements FileClaimAdapter.OnItemClickListener {

    public static final String SELECTED_CLAIM_FILE_ID = "id";
    public static final String SELECTED_CLAIM_FILE_HEADER = "header";

    private RecyclerView mRecyclerView;
    private FileClaimAdapter mFileClaimAdapter;
    private ArrayList<FileClaimItem> mFileClaimList;

    private RequestQueue mRequestQueue;

    ProgressAyubo fileClaimProgressBar;

    LinearLayout fileClaimBackButtonLayout, fileClaimListEmptyMessageLayout;

    PrefManager prefManager;

    InsurancePrefManager insurancePrefManager;

    ScrollView file_claim_scroll_view_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_claim);

        prefManager = new PrefManager(this);
        insurancePrefManager = new InsurancePrefManager(this);

        fileClaimProgressBar = findViewById(R.id.fileClaimProgressBar);
        fileClaimBackButtonLayout = findViewById(R.id.file_claim_back_button_layout);
        file_claim_scroll_view_list = findViewById(R.id.file_claim_scroll_view_list);
        fileClaimListEmptyMessageLayout = findViewById(R.id.fileClaimListEmptyMessageLayout);
        fileClaimListEmptyMessageLayout.setVisibility(View.GONE);

        mRecyclerView = findViewById(R.id.file_claim_data_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFileClaimList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);

        getFileDataClaims();

        setButtonClick();
    }

    private void setButtonClick() {
        fileClaimBackButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getFileDataClaims() {
        fileClaimProgressBar.setVisibility(View.VISIBLE);


        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);


        Call<InsuranceResponseDataArrayList> insuranceGetClaimsTypesByUserRequestCall = commonApiInterface.getClaimsTypesByUser(
                AppConfig.APP_BRANDING_ID,
                prefManager.getUserToken()
        );

        if (Constants.type == Constants.Type.HEMAS) {
            insuranceGetClaimsTypesByUserRequestCall = commonApiInterface.getClaimsTypesByUserForHemas(
                    AppConfig.APP_BRANDING_ID,
                    prefManager.getUserToken()
            );
        }


        insuranceGetClaimsTypesByUserRequestCall.enqueue(new retrofit2.Callback<InsuranceResponseDataArrayList>() {

            @Override
            public void onResponse(Call<InsuranceResponseDataArrayList> call, retrofit2.Response<InsuranceResponseDataArrayList> response) {
                fileClaimProgressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    InsuranceResponseDataArrayList mainResponse = response.body();
                    if (mainResponse.getData().size() == 0) {
                        fileClaimListEmptyMessageLayout.setVisibility(View.VISIBLE);
                        file_claim_scroll_view_list.setVisibility(View.GONE);
                        Toast toast = Toast.makeText(getApplicationContext(), "There are no data", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        file_claim_scroll_view_list.setVisibility(View.VISIBLE);
                        fileClaimListEmptyMessageLayout.setVisibility(View.GONE);
                        JsonArray policyJsonArray = new Gson().toJsonTree(mainResponse.getData()).getAsJsonArray();

                        for (int i = 0; i < policyJsonArray.size(); i++) {
                            JsonElement jsonElement = policyJsonArray.get(i);
                            JsonObject jsonObject = jsonElement.getAsJsonObject();

                            String heading = jsonObject.get("ct_name").getAsString();
                            mFileClaimList.add(new FileClaimItem(heading, jsonObject));

                        }

                        mFileClaimAdapter = new FileClaimAdapter(FileClaimActivity.this, mFileClaimList);
                        mRecyclerView.setAdapter(mFileClaimAdapter);
                        mFileClaimAdapter.setOnItemClickListener(FileClaimActivity.this);
                    }
                } else {
                    file_claim_scroll_view_list.setVisibility(View.GONE);
                    fileClaimListEmptyMessageLayout.setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<InsuranceResponseDataArrayList> call, Throwable t) {
                fileClaimProgressBar.setVisibility(View.GONE);
                file_claim_scroll_view_list.setVisibility(View.GONE);
                fileClaimListEmptyMessageLayout.setVisibility(View.VISIBLE);
                Toast toast = Toast.makeText(getApplicationContext(), R.string.servcer_not_available, Toast.LENGTH_SHORT);
                toast.show();
            }
        });


    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, ClaimUploadDocumentActivity.class);
        FileClaimItem clickedItem = mFileClaimList.get(position);
        insurancePrefManager.setSelectedClaimTypeDetail(clickedItem.getJsonObject());
        startActivity(detailIntent);

    }
}