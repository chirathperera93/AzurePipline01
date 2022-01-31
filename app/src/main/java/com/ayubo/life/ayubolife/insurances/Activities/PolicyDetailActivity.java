package com.ayubo.life.ayubolife.insurances.Activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.insurances.Adapters.ClaimHistoryAdapter;
import com.ayubo.life.ayubolife.insurances.Adapters.PolicyDetailCoversClaimAdapter;
import com.ayubo.life.ayubolife.insurances.Adapters.PolicyDetailEntitleAdapter;
import com.ayubo.life.ayubolife.insurances.Classes.InsuranceResponseDataArrayList;
import com.ayubo.life.ayubolife.insurances.Classes.PaymentHistoryData;
import com.ayubo.life.ayubolife.insurances.Classes.PolicyPayments;
import com.ayubo.life.ayubolife.insurances.GenericItems.ClaimHistoryItem;
import com.ayubo.life.ayubolife.insurances.GenericItems.PolicyDetailCoversClaimItem;
import com.ayubo.life.ayubolife.insurances.GenericItems.PolicyDetailEntitleItem;
import com.ayubo.life.ayubolife.insurances.CommonApiInterface;
import com.ayubo.life.ayubolife.insurances.InsurancePrefManager;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.flavors.changes.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

public class PolicyDetailActivity extends BaseActivity implements
        PolicyDetailCoversClaimAdapter.OnItemClickListener,
        ClaimHistoryAdapter.OnItemClickListener,
        PolicyDetailEntitleAdapter.OnItemClickListener {

    LinearLayout policyDetailLayoutBackBtn, policy_detail_payment_history_no_data_view, policy_detail_claim_history_no_data_view;
    TableLayout mTableLayout;
    ProgressAyubo insurancesPolicyDetailProgressBar;

    ScrollView policy_detail_payment_history_scroll_view;

    ImageView viewPolicyTermsConditionIcon, policyDetailClaimHistoryShowMoreIcon;

    LinearLayout policyDetailClaimHistoryShowMoreMainLayout, policyDetailClaimHistoryShowMoreLayout, policy_detail_entitle_no_data_view;

    TextView policyDetailHeader, policyDetailNumber, policyDetailStatus, policyDetailClaimHistoryShowMoreTxt;

    private RecyclerView cRecyclerView;
    private PolicyDetailCoversClaimAdapter cPolicyCoverClaimAdapter;
    private ArrayList<PolicyDetailCoversClaimItem> cPolicyCoverClaimList;

    private RecyclerView cbRecyclerView;
    private PolicyDetailCoversClaimAdapter cbPolicyCoverClaimAdapter;
    private ArrayList<PolicyDetailCoversClaimItem> cbPolicyCoverClaimList;

    private RecyclerView pchPolicyClaimHistoryRecyclerView;
    private ClaimHistoryAdapter pchPolicyClaimHistoryAdapter;
    private ArrayList<ClaimHistoryItem> pchPolicyClaimHistoryList;

    private RecyclerView policyDetailEntitleRecyclerView;
    private PolicyDetailEntitleAdapter policyDetailEntitleAdapter;
    private ArrayList<PolicyDetailEntitleItem> policyDetailEntitleList;

    boolean isPolicyDetailClaimHistoryExpandable = false;

    InsurancePrefManager insurancePrefManager;
    PrefManager prefManager;

    Map<String, String> policyTeamsConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_policy_detail);

        insurancePrefManager = new InsurancePrefManager(this);
        prefManager = new PrefManager(this);

        insurancesPolicyDetailProgressBar = findViewById(R.id.insurances_policy_detail_progress_bar);

        policyDetailClaimHistoryShowMoreMainLayout = findViewById(R.id.policy_detail_claim_history_show_more_main_layout);
        policyDetailClaimHistoryShowMoreLayout = findViewById(R.id.policy_detail_claim_history_show_more_layout);
        policyDetailClaimHistoryShowMoreIcon = findViewById(R.id.policy_detail_claim_history_show_more_icon);
        policyDetailClaimHistoryShowMoreTxt = findViewById(R.id.policy_detail_claim_history_show_more_txt);

        isPolicyDetailClaimHistoryExpandable = false;

        policy_detail_entitle_no_data_view = findViewById(R.id.policy_detail_entitle_no_data_view);
        policy_detail_entitle_no_data_view.setVisibility(View.GONE);

        policyDetailHeader = findViewById(R.id.policy_detail_header);
        policyDetailNumber = findViewById(R.id.policy_detail_number);
        policyDetailStatus = findViewById(R.id.policy_detail_status);

        cRecyclerView = findViewById(R.id.policy_detail_recycler_view);
        cRecyclerView.setHasFixedSize(true);
        cRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cPolicyCoverClaimList = new ArrayList<>();

        cbRecyclerView = findViewById(R.id.policy_detail_claim_balance_recycler_view);
        cbRecyclerView.setHasFixedSize(true);
        cbRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cbPolicyCoverClaimList = new ArrayList<>();

        pchPolicyClaimHistoryRecyclerView = findViewById(R.id.policy_detail_claim_history_recycler_view);
        pchPolicyClaimHistoryRecyclerView.setHasFixedSize(true);
        pchPolicyClaimHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pchPolicyClaimHistoryList = new ArrayList<>();


        policyDetailEntitleRecyclerView = findViewById(R.id.policy_detail_entitle_recycler_view);
        policyDetailEntitleRecyclerView.setHasFixedSize(true);
        policyDetailEntitleRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        policyDetailEntitleList = new ArrayList<>();


        policyDetailLayoutBackBtn = findViewById(R.id.policy_detail_layout_back_btn);
        viewPolicyTermsConditionIcon = findViewById(R.id.view_policy_terms_condition_icon);
        policy_detail_payment_history_no_data_view = findViewById(R.id.policy_detail_payment_history_no_data_view);
        policy_detail_payment_history_scroll_view = findViewById(R.id.policy_detail_payment_history_scroll_view);
        policy_detail_claim_history_no_data_view = findViewById(R.id.policy_detail_claim_history_no_data_view);


        policyDetailLayoutBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPolicyTermsConditionIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAction(policyTeamsConditions.get("action"), policyTeamsConditions.get("meta"));

            }
        });

        ViewGroup.LayoutParams params = pchPolicyClaimHistoryRecyclerView.getLayoutParams();
        policyDetailClaimHistoryShowMoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPolicyDetailClaimHistoryExpandable) {
                    params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    pchPolicyClaimHistoryRecyclerView.setLayoutParams(params);
                    policyDetailClaimHistoryShowMoreIcon.setRotation(180);
                    policyDetailClaimHistoryShowMoreTxt.setText("Show less");
                    isPolicyDetailClaimHistoryExpandable = false;
                } else {
                    params.height = 500;
                    pchPolicyClaimHistoryRecyclerView.setLayoutParams(params);
                    policyDetailClaimHistoryShowMoreIcon.setRotation(360);
                    policyDetailClaimHistoryShowMoreTxt.setText("Show more");
                    isPolicyDetailClaimHistoryExpandable = true;
                }
            }
        });

        // setup the table
        mTableLayout = (TableLayout) findViewById(R.id.policy_detail_payment_history_table);
        mTableLayout.setStretchAllColumns(true);


        setPolicyDetailCardDetail();
        setPolicyDetailPaymentHistoryData();
        setClaimHistoryData();
        setPolicyEntitlements();

    }


    private void setPolicyDetailCardDetail() {

        JSONObject obj = new JSONObject(insurancePrefManager.getSelectedPolicyOverviewPolicyDetail());

        try {
            JSONObject obj1 = new JSONObject((String) obj.get("policy_detail_policy_data_object"));
            policyDetailHeader.setText(obj1.get("pl_heading").toString());
            policyDetailNumber.setText(obj1.get("pl_policy_no").toString());
            policyDetailStatus.setText(obj1.get("pl_status").toString());

            if (obj1.get("pl_status").toString().equals("Active") || obj1.get("pl_status").toString().equals("Confirm")) {
                policyDetailStatus.setTextColor(getResources().getColor(R.color.green));
            } else {
                policyDetailStatus.setTextColor(getResources().getColor(R.color.orange));
            }


            JsonObject pl_terms_conditions = new Gson().toJsonTree(obj1.get("pl_terms_conditions")).getAsJsonObject();
            policyTeamsConditions = new HashMap<String, String>();
            policyTeamsConditions.put("action", pl_terms_conditions.get("nameValuePairs").getAsJsonObject().get("action").getAsString());
            policyTeamsConditions.put("meta", pl_terms_conditions.get("nameValuePairs").getAsJsonObject().get("meta").getAsString());


            setCoversClaims(obj1.get("pl_covers"));
            setClaimBalance(obj1.get("pl_claim_balance"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setCoversClaims(Object coversClaims) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(coversClaims.toString());
            Iterator keys = jsonObject.keys();

            while (keys.hasNext()) {
                String currentDynamicKey = (String) keys.next();

                switch (currentDynamicKey) {
                    case "life_cover":
                        cPolicyCoverClaimList.add(new PolicyDetailCoversClaimItem("Life cover", jsonObject.getString(currentDynamicKey)));
                        break;

                    case "hospitalization_cover":
                        cPolicyCoverClaimList.add(new PolicyDetailCoversClaimItem("Hospitalization cover", jsonObject.getString(currentDynamicKey)));
                        break;

                    case "critical_illness_cover":
                        cPolicyCoverClaimList.add(new PolicyDetailCoversClaimItem("Critical Illness cover", jsonObject.getString(currentDynamicKey)));
                        break;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cPolicyCoverClaimAdapter = new PolicyDetailCoversClaimAdapter(PolicyDetailActivity.this, cPolicyCoverClaimList);
        cRecyclerView.setAdapter(cPolicyCoverClaimAdapter);
        cPolicyCoverClaimAdapter.setOnItemClickListener(PolicyDetailActivity.this);
    }

    private void setClaimBalance(Object claimBalance) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(claimBalance.toString());
            Iterator keys = jsonObject.keys();

            while (keys.hasNext()) {
                String currentDynamicKey = (String) keys.next();
                switch (currentDynamicKey) {
                    case "critical_illness_cover":
                        cbPolicyCoverClaimList.add(new PolicyDetailCoversClaimItem("Critical Illness cover", jsonObject.getString(currentDynamicKey)));
                        break;

                    case "hospitalization_cover":
                        cbPolicyCoverClaimList.add(new PolicyDetailCoversClaimItem("Hospitalization cover", jsonObject.getString(currentDynamicKey)));
                        break;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cbPolicyCoverClaimAdapter = new PolicyDetailCoversClaimAdapter(PolicyDetailActivity.this, cbPolicyCoverClaimList);
        cbRecyclerView.setAdapter(cbPolicyCoverClaimAdapter);
        cbPolicyCoverClaimAdapter.setOnItemClickListener(PolicyDetailActivity.this);
    }

    private void setClaimHistoryData() {
        JSONObject obj = new JSONObject(insurancePrefManager.getSelectedPolicyOverviewClaimsHistory());
        try {
            JSONArray jsonArray = new JSONArray((String) obj.get("policy_detail_claim_history_object"));

            if (jsonArray.length() == 0) {
                policy_detail_claim_history_no_data_view.setVisibility(View.VISIBLE);
                pchPolicyClaimHistoryRecyclerView.setVisibility(View.GONE);

            } else {
                policy_detail_claim_history_no_data_view.setVisibility(View.GONE);
                pchPolicyClaimHistoryRecyclerView.setVisibility(View.VISIBLE);
                if (jsonArray.length() > 2) {
                    policyDetailClaimHistoryShowMoreMainLayout.setVisibility(View.VISIBLE);
                    policyDetailClaimHistoryShowMoreIcon.setBackgroundResource(R.drawable.show_more);
                    policyDetailClaimHistoryShowMoreTxt.setText("Show more");
                    isPolicyDetailClaimHistoryExpandable = true;
                    ViewGroup.LayoutParams params = pchPolicyClaimHistoryRecyclerView.getLayoutParams();
                    params.height = 500;
                    pchPolicyClaimHistoryRecyclerView.setLayoutParams(params);
                } else if (jsonArray.length() == 2) {
                    ViewGroup.LayoutParams params = pchPolicyClaimHistoryRecyclerView.getLayoutParams();
                    params.height = 500;
                    pchPolicyClaimHistoryRecyclerView.setLayoutParams(params);
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonArrayJSONObject = jsonArray.getJSONObject(i);

                    try {
                        String title = jsonArrayJSONObject.getString("cl_title");
                        String amount = jsonArrayJSONObject.getString("cl_currency") + " " + jsonArrayJSONObject.getString("cl_amount");

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                        format.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date dateNw = format.parse(jsonArrayJSONObject.getString("cl_createdatetime"));
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

                        String date = formatter.format(dateNw);
                        String status = jsonArrayJSONObject.getString("cl_status");
                        pchPolicyClaimHistoryList.add(new ClaimHistoryItem(title, amount, date, status));
                    } catch (Exception e) {
                        e.getStackTrace();
                    }

                }

                pchPolicyClaimHistoryAdapter = new ClaimHistoryAdapter(PolicyDetailActivity.this, pchPolicyClaimHistoryList);
                pchPolicyClaimHistoryRecyclerView.setAdapter(pchPolicyClaimHistoryAdapter);
                pchPolicyClaimHistoryAdapter.setOnItemClickListener(PolicyDetailActivity.this);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setPolicyEntitlements() {
        insurancesPolicyDetailProgressBar.setVisibility(View.VISIBLE);
        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);
        Call<InsuranceResponseDataArrayList> insuranceGetSelectedPolicyEntitlementRequestCall = commonApiInterface.getSelectedPolicyEntitlement(
                AppConfig.APP_BRANDING_ID,
                prefManager.getUserToken(),
                insurancePrefManager.getSelectedPolicyId()
        );


        if (Constants.type == Constants.Type.HEMAS) {
            insuranceGetSelectedPolicyEntitlementRequestCall = commonApiInterface.getSelectedPolicyEntitlementForHemas(
                    AppConfig.APP_BRANDING_ID,
                    prefManager.getUserToken(),
                    insurancePrefManager.getSelectedPolicyId()
            );
        }


        insuranceGetSelectedPolicyEntitlementRequestCall.enqueue(new retrofit2.Callback<InsuranceResponseDataArrayList>() {
            @Override
            public void onResponse(Call<InsuranceResponseDataArrayList> call, Response<InsuranceResponseDataArrayList> response) {
                insurancesPolicyDetailProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    InsuranceResponseDataArrayList mainResponse = response.body();
                    if (mainResponse.getData().size() == 0) {
                        policy_detail_entitle_no_data_view.setVisibility(View.VISIBLE);
                        policyDetailEntitleRecyclerView.setVisibility(View.GONE);
                        Toast toast = Toast.makeText(getApplicationContext(), "No policy entitlement data", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {

                        policy_detail_entitle_no_data_view.setVisibility(View.GONE);
                        policyDetailEntitleRecyclerView.setVisibility(View.VISIBLE);
                        JsonArray policyEntitlementJsonArray = new Gson().toJsonTree(mainResponse.getData()).getAsJsonArray();
                        for (int i = 0; i < policyEntitlementJsonArray.size(); i++) {
                            JsonElement jsonElement = policyEntitlementJsonArray.get(i);
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            String title = jsonObject.get("pe_title").getAsString();


                            String action = "";
                            if (jsonObject.get("pe_action") != null && jsonObject.get("pe_action").getAsString() != "")
                                action = jsonObject.get("pe_action").getAsString();

                            String meta = "";

                            if (jsonObject.get("pe_meta") != null && jsonObject.get("pe_meta").getAsString() != "") {
                                meta = jsonObject.get("pe_meta").getAsString();
                            }


                            policyDetailEntitleList.add(new PolicyDetailEntitleItem(title, action, meta));
                        }
                        policyDetailEntitleAdapter = new PolicyDetailEntitleAdapter(PolicyDetailActivity.this, policyDetailEntitleList);
                        policyDetailEntitleRecyclerView.setAdapter(policyDetailEntitleAdapter);
                        policyDetailEntitleAdapter.setOnItemClickListener(PolicyDetailActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<InsuranceResponseDataArrayList> call, Throwable t) {
                policy_detail_entitle_no_data_view.setVisibility(View.VISIBLE);
                policyDetailEntitleRecyclerView.setVisibility(View.GONE);
            }
        });

    }

    private void setPolicyDetailPaymentHistoryData() {
        insurancesPolicyDetailProgressBar.setVisibility(View.VISIBLE);
        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);
        Call<InsuranceResponseDataArrayList> insuranceGetSelectedPolicyPaymentHistoryRequestCall = commonApiInterface.getSelectedPolicyPaymentHistory(
                AppConfig.APP_BRANDING_ID,
                prefManager.getUserToken(),
                insurancePrefManager.getSelectedPolicyId()
        );


        if (Constants.type == Constants.Type.HEMAS) {
            insuranceGetSelectedPolicyPaymentHistoryRequestCall = commonApiInterface.getSelectedPolicyPaymentHistoryForHemas(
                    AppConfig.APP_BRANDING_ID,
                    prefManager.getUserToken(),
                    insurancePrefManager.getSelectedPolicyId()
            );
        }


        insuranceGetSelectedPolicyPaymentHistoryRequestCall.enqueue(new retrofit2.Callback<InsuranceResponseDataArrayList>() {

            @Override
            public void onResponse(Call<InsuranceResponseDataArrayList> call, Response<InsuranceResponseDataArrayList> response) {
                if (response.isSuccessful()) {
                    InsuranceResponseDataArrayList mainResponse = response.body();

                    if (mainResponse.getData().size() == 0) {
                        policy_detail_payment_history_no_data_view.setVisibility(View.VISIBLE);
                        policy_detail_payment_history_scroll_view.setVisibility(View.GONE);
                        Toast toast = Toast.makeText(getApplicationContext(), "No payment data", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        policy_detail_payment_history_no_data_view.setVisibility(View.GONE);
                        policy_detail_payment_history_scroll_view.setVisibility(View.VISIBLE);
                        JsonArray policyPaymentHistoryJsonArray = new Gson().toJsonTree(mainResponse.getData()).getAsJsonArray();
                        new LoadDataTask(policyPaymentHistoryJsonArray).execute(0);
                    }
                } else {
                    policy_detail_payment_history_no_data_view.setVisibility(View.VISIBLE);
                    policy_detail_payment_history_scroll_view.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(), "Error loading payment history data", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<InsuranceResponseDataArrayList> call, Throwable t) {
                policy_detail_payment_history_no_data_view.setVisibility(View.VISIBLE);
                policy_detail_payment_history_scroll_view.setVisibility(View.GONE);
                Toast toast = Toast.makeText(getApplicationContext(), "Error loading payment history data", Toast.LENGTH_SHORT);
                toast.show();
            }
        });


    }

    @Override
    public void onItemClick(String action, String meta) {
        processAction(action, meta);
    }

    class LoadDataTask extends AsyncTask<Integer, Integer, String> {

        JsonArray policyPaymentHistory;

        public LoadDataTask(JsonArray policyPaymentHistoryJsonArray) {
            policyPaymentHistory = policyPaymentHistoryJsonArray;
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Task Completed.";
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String result) {
            loadData(policyPaymentHistory);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadData(JsonArray policyPaymentHistory) {

        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;

        PolicyPayments policyPayments = new PolicyPayments();
        PaymentHistoryData[] data = policyPayments.getPolicyPaymentData(policyPaymentHistory);

        Typeface typeface = getResources().getFont(R.font.roboto_light);

        int rows = data.length;
        TextView textSpacer = null;

//        mTableLayout.removeAllViews();

        if (rows == 0) {
            policy_detail_payment_history_no_data_view.setVisibility(View.VISIBLE);
            policy_detail_payment_history_scroll_view.setVisibility(View.GONE);

        } else {
            policy_detail_payment_history_no_data_view.setVisibility(View.GONE);
            policy_detail_payment_history_scroll_view.setVisibility(View.VISIBLE);
            // -1 means heading row
            for (int i = -1; i < rows; i++) {
                PaymentHistoryData row = null;
                if (i > -1)
                    row = data[i];
                else {
                    textSpacer = new TextView(this);
                    textSpacer.setText("");

                }


                // data columns
                final TextView textView = new TextView(this);
                textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                textView.setGravity(Gravity.CENTER);
//            textView.setPadding(5, 15, 0, 15);
                textView.setTypeface(typeface);
                textView.setTextSize(12);

                if (i == -1) {
//                textView.setText("Date");
//                textView.setTextColor(Color.parseColor("#ff8000"));
                } else {
                    textView.setText(row.policyPaymentDate);
                    textView.setTextColor(Color.parseColor("#000000"));
                    textView.setPadding(0, 8, 0, 8);

                }


                final TextView textView2 = new TextView(this);
                if (i == -1) {
//                textView2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                } else {
                    textView2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

                }

                textView2.setGravity(Gravity.CENTER);
//            textView2.setPadding(5, 15, 0, 15);
                textView2.setTypeface(typeface);
                textView2.setTextSize(12);

                if (i == -1) {
//                textView2.setText("Amount");
//                textView2.setTextColor(Color.parseColor("#ff8000"));
                } else {
                    textView2.setText(row.policyPaymentAmount);
                    textView2.setTextColor(Color.parseColor("#000000"));
                    textView2.setPadding(0, 8, 0, 8);
                }


                final TextView textView3 = new TextView(this);
                if (i == -1) {
//                textView3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
//                textView3.setPadding(5, 5, 0, 5);
                } else {
                    textView3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

                }

                textView3.setGravity(Gravity.CENTER);
                textView3.setTypeface(typeface);
                textView3.setTextSize(12);


                if (i == -1) {
//                textView3.setText("Status");
//                textView3.setTextColor(Color.parseColor("#ff8000"));
                } else {
                    textView3.setText(row.policyPaymentStatus);
                    textView3.setTextColor(Color.parseColor("#000000"));
                    textView3.setPadding(0, 8, 0, 8);
                }


                // add table row
                final TableRow tr = new TableRow(this);
                tr.setId(i + 1);
                TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                tr.setPadding(0, 0, 0, 0);
                tr.setLayoutParams(trParams);


                tr.addView(textView);
                tr.addView(textView2);
                tr.addView(textView3);

                if (i > -1) {
                    tr.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            TableRow tr = (TableRow) v;
                            //do whatever action is needed

                        }
                    });


                }
                mTableLayout.addView(tr, trParams);

                if (i > -1) {

                    // add separator row
                    final TableRow trSep = new TableRow(this);
                    TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                    trSep.setLayoutParams(trParamsSep);

                    TextView tvSep = new TextView(this);
                    TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    tvSepLay.span = 3;
                    tvSep.setLayoutParams(tvSepLay);
                    tvSep.setHeight(2);

                    trSep.addView(tvSep);
                    mTableLayout.addView(trSep, trParamsSep);
                }


            }
        }


        insurancesPolicyDetailProgressBar.setVisibility(View.GONE);
    }


    @Override
    public void onItemClick(int position) {

    }
}