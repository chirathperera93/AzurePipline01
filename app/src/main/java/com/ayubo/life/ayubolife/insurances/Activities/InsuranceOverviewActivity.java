package com.ayubo.life.ayubolife.insurances.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.insurances.Adapters.BankAccountAdapter;
import com.ayubo.life.ayubolife.insurances.Adapters.ClaimAdapter;
import com.ayubo.life.ayubolife.insurances.Adapters.PolicyDetailCoversClaimAdapter;
import com.ayubo.life.ayubolife.insurances.Classes.InsuranceResponseDataArrayList;
import com.ayubo.life.ayubolife.insurances.GenericItems.BankAccountItem;
import com.ayubo.life.ayubolife.insurances.GenericItems.ClaimItem;
import com.ayubo.life.ayubolife.insurances.GenericItems.PolicyDetailCoversClaimItem;
import com.ayubo.life.ayubolife.insurances.CommonApiInterface;
import com.ayubo.life.ayubolife.insurances.InsurancePrefManager;
import com.ayubo.life.ayubolife.insurances.Classes.InsuranceResponseDataObj;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.flavors.changes.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;


import retrofit2.Call;
import retrofit2.Response;

public class InsuranceOverviewActivity extends BaseActivity implements ClaimAdapter.OnItemClickListener, BankAccountAdapter.OnItemClickListener {
    InsurancePrefManager insurancePrefManager;
    PrefManager prefManager;

    ProgressAyubo insuranceOverviewActivityProgressBar;

    RecyclerView cRecyclerView;
    ArrayList<ClaimItem> cPolicyClaimList;

    RecyclerView bRecyclerView;
    ArrayList<BankAccountItem> bPolicyBankAccountList;

    LinearLayout
            policyOverviewLayoutBackBtn,
            fileClaimIconLayout,
            viewPolicyOverviewIconLayout,
            editBankAccountLayout,
            policy_claim_no_data_view,
            policy_bank_account_no_data_view,
            insuranceOverviewShowMoreMainLayout,
            insuranceOverviewBankAccShowMoreMainLayout,
            policyOverviewImage,
            policy_overview_pdf_linear_layout,
            insuranceOverviewShowMorLayout,
            insuranceOverviewBankAccShowMoreLayout,
            policy_overview_claims_no_data_view,
            policy_card_view_no_data_view,
            policy_benefactor_card_view_no_data_view,
            policy_benefactor_detail_view_layout,
            edit_benefactor_layout,
            policy_overview_claims_view_policy_details_main_layout,
            policy_claim_file_claim_main_layout,
            policy_bank_account_edit_main_layout,
            create_nominee_main_layout;

    TextView policyViewId,
            policyDetailsHeader,
            policyDetailsPdfSubHeader,
            policyDetailStatus,
            policyCardCardValidity,
            policyCardPolicyNo,
            insuranceOverviewShowMoreTxt,
            insuranceOverviewBankAccShowMorTxt,
            policyOverviewImageNameOfInsured,
            policy_benefactor_detail_name,
            policy_benefactor_detail_nic,
            policy_benefactor_detail_mobile,
            policy_number_label,
            valid_thru;


    ImageView insuranceOverviewShowMoreIcon,
            insuranceOverviewBankAccShowMoreIcon,
            editBenefactorIcon,
            policyOverviewPdfDownloader,
            insurance_chat_btn;

    boolean isClaimExpandable, isBankAccExpandable = false;

    NestedScrollView InsuranceOverviewActivityNestedScrollView;

    CardView policyCardView;

    RecyclerView claimsRecyclerView;
    ArrayList<PolicyDetailCoversClaimItem> cPolicyCoverClaimList;

    RelativeLayout insurance_chat_btn_relative_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_overview);
        initializeData();
        setButtonClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSelectedPolicyData();
    }

    private void initializeData() {
        insurancePrefManager = new InsurancePrefManager(this);
        prefManager = new PrefManager(this);

        claimsRecyclerView = findViewById(R.id.policy_overview_claims_recycler_view);
        policy_number_label = findViewById(R.id.policy_number_label);
        valid_thru = findViewById(R.id.valid_thru);
        policy_number_label.setVisibility(View.GONE);
        valid_thru.setVisibility(View.GONE);
        claimsRecyclerView.setHasFixedSize(true);
        claimsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        policyOverviewLayoutBackBtn = findViewById(R.id.policy_overview_layout_back_btn);
        insuranceOverviewActivityProgressBar = findViewById(R.id.insuranceOverviewActivityProgressBar);

        fileClaimIconLayout = findViewById(R.id.file_claim_icon_layout);

        InsuranceOverviewActivityNestedScrollView = findViewById(R.id.InsuranceOverviewActivityNestedScrollView);
        policy_overview_pdf_linear_layout = findViewById(R.id.policy_overview_pdf_linear_layout);
        policy_overview_pdf_linear_layout.setVisibility(View.GONE);

        policy_overview_claims_no_data_view = findViewById(R.id.policy_overview_claims_no_data_view);
        policyOverviewPdfDownloader = findViewById(R.id.policy_overview_pdf_downloader);
        insurance_chat_btn = findViewById(R.id.insurance_chat_btn);
        insurance_chat_btn_relative_layout = findViewById(R.id.insurance_chat_btn_relative_layout);

        editBankAccountLayout = findViewById(R.id.edit_bank_account_layout);
        policyCardView = findViewById(R.id.policy_card_view);

        policyOverviewImageNameOfInsured = findViewById(R.id.policy_overview_image_name_of_insured);
        policyCardCardValidity = findViewById(R.id.policy_card_card_validity);
        policyCardPolicyNo = findViewById(R.id.policy_card_policy_no);

        policy_claim_no_data_view = findViewById(R.id.policy_claim_no_data_view);
        policy_bank_account_no_data_view = findViewById(R.id.policy_bank_account_no_data_view);

        policyViewId = findViewById(R.id.policy_card_nic_no);
        policyDetailsHeader = findViewById(R.id.policy_overview_header);
        policyDetailsPdfSubHeader = findViewById(R.id.policy_overview_pdf_sub_header);
        policyDetailStatus = findViewById(R.id.policy_overview_status);
        viewPolicyOverviewIconLayout = findViewById(R.id.view_policy_overview_icon_layout);

        insuranceOverviewShowMoreMainLayout = findViewById(R.id.insurance_overview_show_more_main_layout);
        insuranceOverviewShowMorLayout = findViewById(R.id.insurance_overview_show_more_layout);
        insuranceOverviewShowMoreIcon = findViewById(R.id.insurance_overview_show_more_icon);
        insuranceOverviewShowMoreTxt = findViewById(R.id.insurance_overview_show_more_txt);

        insuranceOverviewBankAccShowMoreMainLayout = findViewById(R.id.insurance_overview_bankAcc_show_more_main_layout);
        insuranceOverviewBankAccShowMoreLayout = findViewById(R.id.insurance_overview_bankAcc_show_more_layout);
        insuranceOverviewBankAccShowMoreIcon = findViewById(R.id.insurance_overview_bankAcc_show_more_icon);
        insuranceOverviewBankAccShowMorTxt = findViewById(R.id.insurance_overview_bankAcc_show_more_txt);

        policyOverviewImage = findViewById(R.id.policy_overview_image);

        editBenefactorIcon = findViewById(R.id.edit_benefactor_icon);

        edit_benefactor_layout = findViewById(R.id.edit_benefactor_layout);

        isClaimExpandable = false;
        isBankAccExpandable = false;

        cRecyclerView = findViewById(R.id.policy_claim_recycler_view);
        cRecyclerView.setHasFixedSize(true);
        cRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bRecyclerView = findViewById(R.id.policy_bank_account_recycler_view);
        bRecyclerView.setHasFixedSize(true);
        bRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        policy_benefactor_detail_name = findViewById(R.id.policy_benefactor_detail_name);
        policy_benefactor_detail_nic = findViewById(R.id.policy_benefactor_detail_nic);
        policy_benefactor_detail_mobile = findViewById(R.id.policy_benefactor_detail_mobile);

        policy_overview_claims_view_policy_details_main_layout = findViewById(R.id.policy_overview_claims_view_policy_details_main_layout);
        policy_claim_file_claim_main_layout = findViewById(R.id.policy_claim_file_claim_main_layout);
        policy_bank_account_edit_main_layout = findViewById(R.id.policy_bank_account_edit_main_layout);
        create_nominee_main_layout = findViewById(R.id.create_nominee_main_layout);

        policy_card_view_no_data_view = findViewById(R.id.policy_card_view_no_data_view);
        policy_card_view_no_data_view.setVisibility(View.GONE);

        policy_benefactor_card_view_no_data_view = findViewById(R.id.policy_benefactor_card_view_no_data_view);
        policy_benefactor_card_view_no_data_view.setVisibility(View.GONE);

        policy_benefactor_detail_view_layout = findViewById(R.id.policy_benefactor_detail_view_layout);
        policy_benefactor_detail_view_layout.setVisibility(View.VISIBLE);
        insurance_chat_btn_relative_layout.setVisibility(View.GONE);


    }

    private void setSelectedPolicyData() {
        cPolicyCoverClaimList = new ArrayList<>();
        cPolicyClaimList = new ArrayList<>();
        bPolicyBankAccountList = new ArrayList<>();
        insuranceOverviewActivityProgressBar.setVisibility(View.VISIBLE);
        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);


        Call<InsuranceResponseDataObj> insuranceGetSelectedPolicyRequestCall = commonApiInterface.getSelectedPolicyData(AppConfig.APP_BRANDING_ID, prefManager.getUserToken(), insurancePrefManager.getSelectedPolicyId());


        if (Constants.type == Constants.Type.HEMAS) {
            insuranceGetSelectedPolicyRequestCall = commonApiInterface.getSelectedPolicyDataForHemas(AppConfig.APP_BRANDING_ID, prefManager.getUserToken(), insurancePrefManager.getSelectedPolicyId());

        }


        insuranceGetSelectedPolicyRequestCall.enqueue(new retrofit2.Callback<InsuranceResponseDataObj>() {

            @Override
            public void onResponse(Call<InsuranceResponseDataObj> call, Response<InsuranceResponseDataObj> response) {
                getBenefactor();
                if (response.isSuccessful()) {
                    InsuranceResponseDataObj mainResponse = response.body();
                    if (mainResponse.getData() == "") {
                        insuranceOverviewActivityProgressBar.setVisibility(View.GONE);
                        InsuranceOverviewActivityNestedScrollView.setVisibility(View.GONE);
                        Toast toast = Toast.makeText(getApplicationContext(), "There are no data", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {


                        JsonObject policyDetailJsonObject = new Gson().toJsonTree(mainResponse.getData()).getAsJsonObject();
                        if (policyDetailJsonObject.get("card") != null && !policyDetailJsonObject.get("card").getAsJsonObject().equals("")) {
                            policy_card_view_no_data_view.setVisibility(View.GONE);
                            policyOverviewImageNameOfInsured.setVisibility(View.VISIBLE);
                            policy_number_label.setVisibility(View.VISIBLE);
                            valid_thru.setVisibility(View.VISIBLE);
                            JsonObject policyDetailCardDataJsonObject = policyDetailJsonObject.get("card").getAsJsonObject();
                            policyOverviewImageNameOfInsured.setText(policyDetailCardDataJsonObject.get("name_of_insured").getAsString());
                            policyViewId.setText(policyDetailCardDataJsonObject.get("nic_no").getAsString());
                            policyCardCardValidity.setText(policyDetailCardDataJsonObject.get("card_validity").getAsString());
                            policyCardPolicyNo.setText(policyDetailCardDataJsonObject.get("policy_no").getAsString());
                            policyDetailsPdfSubHeader.setText(policyDetailCardDataJsonObject.get("policy_no").getAsString());

                            prefManager.setInsuranceChat(policyDetailCardDataJsonObject.get("insurance_chat").getAsJsonObject().toString());

                            insurance_chat_btn_relative_layout.setVisibility(View.VISIBLE);
                            policy_overview_pdf_linear_layout.setVisibility(View.VISIBLE);
                            insuranceOverviewActivityProgressBar.setVisibility(View.VISIBLE);

                            if (policyDetailCardDataJsonObject.get("bg_image") != null && !policyDetailCardDataJsonObject.get("bg_image").getAsString().equals("")) {
                                new GetImageFromServer().execute(policyDetailCardDataJsonObject.get("bg_image").getAsString());
                            } else {
                                policyOverviewImage.setBackgroundColor(getResources().getColor(R.color.white));
                            }

                        } else {
                            policy_card_view_no_data_view.setVisibility(View.VISIBLE);
                            policyOverviewImageNameOfInsured.setVisibility(View.GONE);
                        }


                        if (policyDetailJsonObject.get("policy") != null && !policyDetailJsonObject.get("policy").getAsJsonObject().equals("")) {
                            Gson policyDetailPolicyDataGSON = new Gson();
                            policy_overview_claims_no_data_view.setVisibility(View.GONE);
                            claimsRecyclerView.setVisibility(View.VISIBLE);
                            JsonObject policyDetailPolicyDataJsonObject = policyDetailJsonObject.get("policy").getAsJsonObject();
                            insurancePrefManager.setSelectedPolicyOverviewPolicyDetail(policyDetailPolicyDataGSON.toJson(policyDetailPolicyDataJsonObject));
                            policyDetailsHeader.setText(policyDetailPolicyDataJsonObject.get("pl_heading").getAsString());
                            policyDetailStatus.setText(policyDetailPolicyDataJsonObject.get("pl_status").getAsString());

                            if (policyDetailPolicyDataJsonObject.get("pl_status").getAsString().equals("Active")) {
                                policyDetailStatus.setTextColor(getResources().getColor(R.color.green));
                            } else {
                                policyDetailStatus.setTextColor(getResources().getColor(R.color.orange));
                            }

                            if (policyDetailPolicyDataJsonObject.get("pl_covers") != null && !policyDetailPolicyDataJsonObject.get("pl_covers").equals("")) {
                                setPolicyClaimCover(policyDetailPolicyDataJsonObject.get("pl_covers"));
                            }
                        } else {
                            policy_overview_claims_no_data_view.setVisibility(View.VISIBLE);
                            claimsRecyclerView.setVisibility(View.GONE);
                        }


                        Gson policyClaimHistoryDataGSON = new Gson();

                        if (policyDetailJsonObject.get("claims") != null) {
                            policy_claim_no_data_view.setVisibility(View.GONE);
                            cRecyclerView.setVisibility(View.VISIBLE);
                            JsonArray policyDetailClaimsDataJsonArray = policyDetailJsonObject.get("claims").getAsJsonArray();
                            insurancePrefManager.setSelectedPolicyOverviewClaimsHistory(policyClaimHistoryDataGSON.toJson(policyDetailClaimsDataJsonArray));
                            setPolicyClaimData(policyDetailClaimsDataJsonArray);
                        } else {
                            policy_claim_no_data_view.setVisibility(View.VISIBLE);
                            cRecyclerView.setVisibility(View.GONE);
                        }


                        if (policyDetailJsonObject.get("bank_accounts") != null) {
                            policy_bank_account_no_data_view.setVisibility(View.GONE);
                            bRecyclerView.setVisibility(View.VISIBLE);
                            JsonArray policyDetailBankAccountsJsonArray = policyDetailJsonObject.get("bank_accounts").getAsJsonArray();
                            setPolicyBankData(policyDetailBankAccountsJsonArray);
                        } else {
                            policy_bank_account_no_data_view.setVisibility(View.VISIBLE);
                            bRecyclerView.setVisibility(View.GONE);
                        }

                        if (policyDetailJsonObject.get("policy_settings") != null) {

                            if (policyDetailJsonObject.get("policy_settings").getAsJsonObject().get("claiming_possible") != null &&
                                    policyDetailJsonObject.get("policy_settings").getAsJsonObject().get("claiming_possible").getAsBoolean()) {
                                policy_claim_file_claim_main_layout.setVisibility(View.VISIBLE);
                            } else {
                                policy_claim_file_claim_main_layout.setVisibility(View.GONE);
                            }


                            if (policyDetailJsonObject.get("policy_settings").getAsJsonObject().get("edit_bank_account") != null &&
                                    policyDetailJsonObject.get("policy_settings").getAsJsonObject().get("edit_bank_account").getAsBoolean()) {
                                policy_bank_account_edit_main_layout.setVisibility(View.VISIBLE);
                            } else {
                                policy_bank_account_edit_main_layout.setVisibility(View.GONE);
                            }


                            if (policyDetailJsonObject.get("policy_settings").getAsJsonObject().get("edit_benefactor") != null &&
                                    policyDetailJsonObject.get("policy_settings").getAsJsonObject().get("edit_benefactor").getAsBoolean()) {
                                create_nominee_main_layout.setVisibility(View.VISIBLE);
                            } else {
                                create_nominee_main_layout.setVisibility(View.GONE);
                            }
                        }


                    }
                } else {
                    insuranceOverviewActivityProgressBar.setVisibility(View.GONE);
                    InsuranceOverviewActivityNestedScrollView.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<InsuranceResponseDataObj> call, Throwable t) {
                insuranceOverviewActivityProgressBar.setVisibility(View.GONE);
                InsuranceOverviewActivityNestedScrollView.setVisibility(View.GONE);
                Toast toast = Toast.makeText(getApplicationContext(), R.string.servcer_not_available, Toast.LENGTH_SHORT);
                toast.show();
                getBenefactor();
            }
        });

    }

    private void setPolicyClaimCover(Object coversClaims) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(coversClaims.toString());
            if (jsonObject.length() > 0) {
                policy_overview_claims_no_data_view.setVisibility(View.GONE);
                claimsRecyclerView.setVisibility(View.VISIBLE);
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

            } else {
                policy_overview_claims_no_data_view.setVisibility(View.VISIBLE);
                claimsRecyclerView.setVisibility(View.GONE);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        PolicyDetailCoversClaimAdapter cPolicyCoverClaimAdapter = new PolicyDetailCoversClaimAdapter(InsuranceOverviewActivity.this, cPolicyCoverClaimList);
        claimsRecyclerView.setAdapter(cPolicyCoverClaimAdapter);
    }

    private void setButtonClick() {

        ViewGroup.LayoutParams params = cRecyclerView.getLayoutParams();
        insuranceOverviewShowMorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClaimExpandable) {
                    params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    cRecyclerView.setLayoutParams(params);
                    insuranceOverviewShowMoreIcon.setRotation(180);
                    insuranceOverviewShowMoreTxt.setText("Show less");
                    isClaimExpandable = false;
                } else {
                    params.height = 400;
                    cRecyclerView.setLayoutParams(params);
                    insuranceOverviewShowMoreIcon.setRotation(360);
                    insuranceOverviewShowMoreTxt.setText("Show more");
                    isClaimExpandable = true;
                }

            }
        });

        ViewGroup.LayoutParams bankAccParams = bRecyclerView.getLayoutParams();
        insuranceOverviewBankAccShowMoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBankAccExpandable) {
                    bankAccParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                    bRecyclerView.setLayoutParams(bankAccParams);
                    insuranceOverviewBankAccShowMoreIcon.setRotation(180);
                    insuranceOverviewBankAccShowMorTxt.setText("Show less");
                    isBankAccExpandable = false;
                } else {
                    bankAccParams.height = 500;
                    bRecyclerView.setLayoutParams(bankAccParams);
                    insuranceOverviewBankAccShowMoreIcon.setRotation(360);
                    insuranceOverviewBankAccShowMorTxt.setText("Show more");
                    isBankAccExpandable = true;
                }
            }
        });

//        editBankAccountLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(InsuranceOverviewActivity.this, InsuranceAddBankAccountActivity.class);
//                startActivity(intent);
//
//            }
//        });

        edit_benefactor_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsuranceOverviewActivity.this, InsuranceBenefactorDetailActivity.class);
                startActivity(intent);
            }
        });

        policyOverviewLayoutBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(InsuranceOverviewActivity.this, InsurancePolicesActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

//        viewPolicyOverviewIconLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(InsuranceOverviewActivity.this, PolicyDetailActivity.class);
//                startActivity(intent);
//            }
//        });

//        fileClaimIconLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(InsuranceOverviewActivity.this, FileClaimActivity.class);
//                startActivity(intent);
//            }
//        });

        policyOverviewPdfDownloader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);
                } else {
                    JSONObject obj = new JSONObject(insurancePrefManager.getSelectedPolicyOverviewPolicyDetail());
                    try {
                        JSONObject obj1 = new JSONObject((String) obj.get("policy_detail_policy_data_object"));
                        String media_url = obj1.get("pl_policy_doc").toString();
                        if (media_url.contains("pdf")) {
                            Uri myUri = Uri.parse(media_url);
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            target.setDataAndType(myUri, "application/pdf");
                            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            Intent intent = Intent.createChooser(target, "Open File");
                            startActivity(intent);
                        } else {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(media_url));
                            startActivity(browserIntent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        policy_overview_claims_view_policy_details_main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsuranceOverviewActivity.this, PolicyDetailActivity.class);
                startActivity(intent);
            }
        });

        policy_claim_file_claim_main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsuranceOverviewActivity.this, FileClaimActivity.class);
                startActivity(intent);
            }
        });

        policy_bank_account_edit_main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsuranceOverviewActivity.this, InsuranceAddBankAccountActivity.class);
                startActivity(intent);

            }
        });

        insurance_chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object obj = prefManager.getInsuranceChat();
                try {
                    JSONObject jsonObject = new JSONObject(obj.toString());
                    processAction(jsonObject.get("action").toString(), jsonObject.get("meta").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setPolicyClaimData(JsonArray policyDetailClaimsDataJsonArray) {

        if (policyDetailClaimsDataJsonArray.size() == 0) {
            policy_claim_no_data_view.setVisibility(View.VISIBLE);
            cRecyclerView.setVisibility(View.GONE);

        } else {
            policy_claim_no_data_view.setVisibility(View.GONE);
            cRecyclerView.setVisibility(View.VISIBLE);
            if (policyDetailClaimsDataJsonArray.size() > 2) {
                insuranceOverviewShowMoreMainLayout.setVisibility(View.VISIBLE);
                insuranceOverviewShowMoreIcon.setBackgroundResource(R.drawable.show_more);
                insuranceOverviewShowMoreTxt.setText("Show more");
                isClaimExpandable = true;
                ViewGroup.LayoutParams params = cRecyclerView.getLayoutParams();
                params.height = 400;
                cRecyclerView.setLayoutParams(params);
            } else if (policyDetailClaimsDataJsonArray.size() == 2) {
                ViewGroup.LayoutParams params = cRecyclerView.getLayoutParams();
                params.height = 400;
                cRecyclerView.setLayoutParams(params);
            }


            for (int i = 0; i < policyDetailClaimsDataJsonArray.size(); i++) {
                JsonElement jsonElement = policyDetailClaimsDataJsonArray.get(i);
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                    format.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date dateNw = format.parse(jsonObject.get("cl_createdatetime").getAsString());
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

                    String type = jsonObject.get("cl_title").getAsString();
                    String createDate = formatter.format(dateNw);
                    String status = jsonObject.get("cl_status").getAsString();
                    cPolicyClaimList.add(new ClaimItem(type, createDate, status));
                } catch (Exception e) {
                    e.getStackTrace();
                }


            }

            ClaimAdapter cPolicyClaimAdapter = new ClaimAdapter(InsuranceOverviewActivity.this, cPolicyClaimList);
            cRecyclerView.setAdapter(cPolicyClaimAdapter);
            cPolicyClaimAdapter.setOnItemClickListener(InsuranceOverviewActivity.this);
        }


    }

    private void setPolicyBankData(JsonArray policyDetailBankAccountsJsonArray) {

        if (policyDetailBankAccountsJsonArray.size() == 0) {
            policy_bank_account_no_data_view.setVisibility(View.VISIBLE);
            bRecyclerView.setVisibility(View.GONE);
        } else {
            bRecyclerView.setVisibility(View.VISIBLE);
            policy_bank_account_no_data_view.setVisibility(View.GONE);
            if (policyDetailBankAccountsJsonArray.size() > 2) {
                insuranceOverviewBankAccShowMoreMainLayout.setVisibility(View.VISIBLE);
                insuranceOverviewBankAccShowMoreIcon.setBackgroundResource(R.drawable.show_more);
                insuranceOverviewBankAccShowMorTxt.setText("Show more");
                isBankAccExpandable = true;
                ViewGroup.LayoutParams bankAccParams = bRecyclerView.getLayoutParams();
                bankAccParams.height = 500;
                bRecyclerView.setLayoutParams(bankAccParams);
            } else if (policyDetailBankAccountsJsonArray.size() == 2) {
                ViewGroup.LayoutParams bankAccParams = bRecyclerView.getLayoutParams();
                bankAccParams.height = 500;
                bRecyclerView.setLayoutParams(bankAccParams);
            }


            for (int i = 0; i < policyDetailBankAccountsJsonArray.size(); i++) {

                JsonElement jsonElement = policyDetailBankAccountsJsonArray.get(i);
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                String accHolderName = jsonObject.get("acc_holder_name") == null ? "" : jsonObject.get("acc_holder_name").getAsString();
                String bankName = jsonObject.get("acc_bank_name") == null ? "" : jsonObject.get("acc_bank_name").getAsString();
                String accNo = jsonObject.get("acc_number") == null ? "" : jsonObject.get("acc_number").getAsString();
                bPolicyBankAccountList.add(new BankAccountItem(accHolderName, bankName, accNo));
            }

            BankAccountAdapter bPolicyBankAccountAdapter = new BankAccountAdapter(InsuranceOverviewActivity.this, bPolicyBankAccountList);
            bRecyclerView.setAdapter(bPolicyBankAccountAdapter);
            bPolicyBankAccountAdapter.setOnItemClickListener(InsuranceOverviewActivity.this);
        }


    }

    @Override
    public void onItemClick(int position) {
    }

    public class GetImageFromServer extends AsyncTask<String, Void, Bitmap> {

        private Bitmap image;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(params[0].trim());
                URLConnection urlConnection = url.openConnection();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                image = BitmapFactory.decodeStream(urlConnection.getInputStream(), null, options);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Drawable drawable = new BitmapDrawable(result);
            policyCardView.setBackgroundDrawable(drawable);
//            insuranceOverviewActivityProgressBar.setVisibility(View.GONE);
        }


    }

    private void getBenefactor() {
        insuranceOverviewActivityProgressBar.setVisibility(View.VISIBLE);
        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);
        Call<InsuranceResponseDataArrayList> insuranceGetBenefactorByUserRequestCall = commonApiInterface.getBenefactorByUser(AppConfig.APP_BRANDING_ID, prefManager.getUserToken());


        if (Constants.type == Constants.Type.HEMAS) {
            insuranceGetBenefactorByUserRequestCall = commonApiInterface.getBenefactorByUserForHemas(AppConfig.APP_BRANDING_ID, prefManager.getUserToken());
        }


        insuranceGetBenefactorByUserRequestCall.enqueue(new retrofit2.Callback<InsuranceResponseDataArrayList>() {

            @Override
            public void onResponse(Call<InsuranceResponseDataArrayList> call, Response<InsuranceResponseDataArrayList> response) {
                insuranceOverviewActivityProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    InsuranceResponseDataArrayList mainResponse = response.body();
                    if (mainResponse.getData().size() == 0) {
                        policy_benefactor_card_view_no_data_view.setVisibility(View.VISIBLE);
                        policy_benefactor_detail_view_layout.setVisibility(View.GONE);
                        Toast toast = Toast.makeText(getApplicationContext(), "No benefactor data", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        policy_benefactor_card_view_no_data_view.setVisibility(View.GONE);
                        policy_benefactor_detail_view_layout.setVisibility(View.VISIBLE);
                        JsonArray policyJsonArray = new Gson().toJsonTree(mainResponse.getData()).getAsJsonArray();
                        JsonElement jsonElement = policyJsonArray.get(policyJsonArray.size() - 1);
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        policy_benefactor_detail_name.setText(jsonObject.get("bf_firstname").getAsString() + " " + jsonObject.get("bf_lastname").getAsString());
                        policy_benefactor_detail_nic.setText(jsonObject.get("bf_identification").getAsString());
                        policy_benefactor_detail_mobile.setText(jsonObject.get("bf_contactnumber").getAsString());

                    }
                } else {
                    policy_benefactor_card_view_no_data_view.setVisibility(View.VISIBLE);
                    policy_benefactor_detail_view_layout.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<InsuranceResponseDataArrayList> call, Throwable t) {
                insuranceOverviewActivityProgressBar.setVisibility(View.GONE);
                Toast toast = Toast.makeText(getApplicationContext(), R.string.servcer_not_available, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}