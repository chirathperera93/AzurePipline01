package com.ayubo.life.ayubolife.insurances.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.insurances.Adapters.PolicyAdapter;
import com.ayubo.life.ayubolife.insurances.GenericItems.PolicyItem;
import com.ayubo.life.ayubolife.insurances.CommonApiInterface;
import com.ayubo.life.ayubolife.insurances.InsurancePrefManager;
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.flavors.changes.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;

public class InsurancePolicesActivity extends BaseActivity implements PolicyAdapter.OnItemClickListener {

    RecyclerView mRecyclerView;
    PolicyAdapter mPolicyAdapter;
    ArrayList<PolicyItem> mPolicyList;


    ImageView btn_backImgBtn;
    TextView policy_layout_back_btnText, myPoliciesEmptyMessageLayout_heading, myPoliciesEmptyMessageLayout_sub_heading, button1, button2, button3;

    ProgressAyubo insurances_progress_bar;

    LinearLayout myPoliciesEmptyMessageLayout;

    ScrollView scrollViewList;

    JsonObject insuranceInfoJsonObject;

    InsurancePrefManager insurancePrefManager;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_polices);

        insurancePrefManager = new InsurancePrefManager(this);
        prefManager = new PrefManager(this);

        myPoliciesEmptyMessageLayout_heading = findViewById(R.id.myPoliciesEmptyMessageLayout_heading);
        myPoliciesEmptyMessageLayout_sub_heading = findViewById(R.id.myPoliciesEmptyMessageLayout_sub_heading);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPolicyList = new ArrayList<>();
                setPolicyData();
                pullToRefresh.setRefreshing(false);
            }
        });

        btn_backImgBtn = findViewById(R.id.policy_layout_back_btn);
        myPoliciesEmptyMessageLayout = findViewById(R.id.myPoliciesEmptyMessageLayout);
        policy_layout_back_btnText = findViewById(R.id.policy_layout_back_btnText);
        scrollViewList = findViewById(R.id.scrollViewList);

        insurances_progress_bar = findViewById(R.id.insurancesProgressBar);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        mRecyclerView = findViewById(R.id.policy_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPolicyList = new ArrayList<>();


        setButtonClick();
        setPolicyData();
    }

    private void setButtonClick() {
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(InsurancePolicesActivity.this, NewHomeWithSideMenuActivity.class));
//                Intent intent = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
//                startActivity(intent);
//                startActivity(new Intent(InsurancePolicesActivity.this, NewHomeWithSideMenuActivity.class));
                finish();
            }
        });

        policy_layout_back_btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(InsurancePolicesActivity.this, NewHomeWithSideMenuActivity.class));
//                Intent intent = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
//                startActivity(intent);
                finish();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processAction(
                        insuranceInfoJsonObject.get("button_1").getAsJsonObject().get("action").getAsString(),
                        insuranceInfoJsonObject.get("button_1").getAsJsonObject().get("meta").getAsString());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                processAction(
                        insuranceInfoJsonObject.get("button_2").getAsJsonObject().get("action").getAsString(),
                        insuranceInfoJsonObject.get("button_2").getAsJsonObject().get("meta").getAsString());

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processAction(
                        insuranceInfoJsonObject.get("button_3").getAsJsonObject().get("action").getAsString(),
                        insuranceInfoJsonObject.get("button_3").getAsJsonObject().get("meta").getAsString());

            }
        });
    }

    private void setPolicyData() {
        insurances_progress_bar.setVisibility(View.VISIBLE);
        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);
//        Call<InsuranceResponseDataArrayList> insuranceGetAllPoliciesRequestCall = insuranceApiInterface.getPolicies(AppConfig.APP_BRANDING_ID, prefManager.getUserToken());
        Call<ProfileDashboardResponseData> insuranceGetAllPoliciesRequestCall = commonApiInterface.getPoliciesV2(AppConfig.APP_BRANDING_ID, prefManager.getUserToken());

        if (Constants.type == Constants.Type.HEMAS) {
            insuranceGetAllPoliciesRequestCall = commonApiInterface.getPoliciesForHemas(AppConfig.APP_BRANDING_ID, prefManager.getUserToken());
        }

        insuranceGetAllPoliciesRequestCall.enqueue(new retrofit2.Callback<ProfileDashboardResponseData>() {
            @Override
            public void onResponse(Call<ProfileDashboardResponseData> call, retrofit2.Response<ProfileDashboardResponseData> response) {
                if (response.isSuccessful()) {
                    insurances_progress_bar.setVisibility(View.GONE);

                    JsonObject insurancePoliciesMainData = new Gson().toJsonTree(response.body().getData()).getAsJsonObject();

                    insuranceInfoJsonObject = insurancePoliciesMainData.get("insurance_info").getAsJsonObject();

                    myPoliciesEmptyMessageLayout_heading.setText(insuranceInfoJsonObject.get("title").getAsString());
                    myPoliciesEmptyMessageLayout_sub_heading.setText(insuranceInfoJsonObject.get("description").getAsString());

                    if (insuranceInfoJsonObject.get("button_1") != null) {
                        button1.setVisibility(View.VISIBLE);
                        button1.setText(insuranceInfoJsonObject.get("button_1").getAsJsonObject().get("title").getAsString());
                    }


                    if (insuranceInfoJsonObject.get("button_2") != null) {
                        button2.setVisibility(View.VISIBLE);
                        button2.setText(insuranceInfoJsonObject.get("button_2").getAsJsonObject().get("title").getAsString());
                    }

                    if (insuranceInfoJsonObject.get("button_3") != null) {
                        button3.setVisibility(View.VISIBLE);
                        button3.setText(insuranceInfoJsonObject.get("button_3").getAsJsonObject().get("title").getAsString());
                    }


                    JsonArray policyJsonArray = insurancePoliciesMainData.get("policies").getAsJsonArray();

                    if (policyJsonArray.size() == 0) {
                        myPoliciesEmptyMessageLayout.setVisibility(View.VISIBLE);
                        scrollViewList.setVisibility(View.GONE);
//                        Toast toast = Toast.makeText(getApplicationContext(), "No policies in your account yet!", Toast.LENGTH_SHORT);
//                        toast.show();
                    } else {
                        scrollViewList.setVisibility(View.VISIBLE);
                        myPoliciesEmptyMessageLayout.setVisibility(View.GONE);


                        for (int i = 0; i < policyJsonArray.size(); i++) {
                            JsonElement jsonElement = policyJsonArray.get(i);
                            JsonObject jsonObject = jsonElement.getAsJsonObject();

                            String id = jsonObject.get("id").getAsString();
                            String policyNo = jsonObject.get("pl_policy_no").getAsString();
                            String heading = jsonObject.get("pl_heading").getAsString();
                            String subHeading = jsonObject.get("pl_type").getAsString();
                            String status = jsonObject.get("pl_status").getAsString();
                            int clickable = jsonObject.get("pl_clickable").getAsInt();
                            mPolicyList.add(new PolicyItem(id, heading, subHeading, status, clickable, policyNo));

                        }

                        mPolicyAdapter = new PolicyAdapter(InsurancePolicesActivity.this, mPolicyList);
                        mRecyclerView.setAdapter(mPolicyAdapter);
                        mPolicyAdapter.setOnItemClickListener(InsurancePolicesActivity.this);
                    }


                } else {
                    insurances_progress_bar.setVisibility(View.GONE);
                    scrollViewList.setVisibility(View.GONE);
                    myPoliciesEmptyMessageLayout.setVisibility(View.VISIBLE);
                    myPoliciesEmptyMessageLayout_heading.setText("Oops something went wrong");
                    myPoliciesEmptyMessageLayout_sub_heading.setText("Server error");
                    Toast toast = Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<ProfileDashboardResponseData> call, Throwable t) {
                insurances_progress_bar.setVisibility(View.GONE);
                scrollViewList.setVisibility(View.GONE);
                myPoliciesEmptyMessageLayout.setVisibility(View.VISIBLE);
                myPoliciesEmptyMessageLayout_heading.setText("Oops something went wrong");
                myPoliciesEmptyMessageLayout_sub_heading.setText("Server error");
                Toast toast = Toast.makeText(getApplicationContext(), R.string.servcer_not_available, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, InsuranceOverviewActivity.class);
        PolicyItem clickedItem = mPolicyList.get(position);

        int clickable = clickedItem.getClickable();
        if (clickable != 0) {
            insurancePrefManager.setSelectedPolicyDetail(clickedItem.getId(), clickedItem.getHeading(), clickedItem.getStatus());
            startActivity(detailIntent);
        }
    }
}