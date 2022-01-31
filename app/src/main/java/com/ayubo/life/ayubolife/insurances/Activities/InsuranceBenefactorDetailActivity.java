package com.ayubo.life.ayubolife.insurances.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.insurances.Classes.InsuranceResponseDataObj;
import com.ayubo.life.ayubolife.insurances.Classes.RequestBenefactor;
import com.ayubo.life.ayubolife.insurances.CommonApiInterface;
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.flavors.changes.Constants;

import retrofit2.Call;
import retrofit2.Response;

public class InsuranceBenefactorDetailActivity extends AppCompatActivity {

    LinearLayout activity_insurance_benefactor_detail_back_btn_layout;
    EditText activity_insurance_benefactor_detail_first_name,
            activity_insurance_benefactor_detail_last_name,
            activity_insurance_benefactor_detail_identification_number,
            activity_insurance_benefactor_detail_contact_number,
            activity_insurance_benefactor_detail_relationship;

    Button activity_insurance_benefactor_detail_save_btn;

    PrefManager prefManager;

    ProgressAyubo activity_insurance_benefactor_detail_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_benefactor_detail);

        prefManager = new PrefManager(this);

        onInit();

        setButtonsClick();
    }


    private void onInit() {
        activity_insurance_benefactor_detail_first_name = findViewById(R.id.activity_insurance_benefactor_detail_first_name);
        activity_insurance_benefactor_detail_last_name = findViewById(R.id.activity_insurance_benefactor_detail_last_name);
        activity_insurance_benefactor_detail_identification_number = findViewById(R.id.activity_insurance_benefactor_detail_identification_number);
        activity_insurance_benefactor_detail_contact_number = findViewById(R.id.activity_insurance_benefactor_detail_contact_number);
        activity_insurance_benefactor_detail_relationship = findViewById(R.id.activity_insurance_benefactor_detail_relationship);
        activity_insurance_benefactor_detail_save_btn = findViewById(R.id.activity_insurance_benefactor_detail_save_btn);
        activity_insurance_benefactor_detail_back_btn_layout = findViewById(R.id.activity_insurance_benefactor_detail_back_btn_layout);
        activity_insurance_benefactor_detail_progress_bar = findViewById(R.id.activity_insurance_benefactor_detail_progress_bar);

    }

    private void setButtonsClick() {
        activity_insurance_benefactor_detail_back_btn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activity_insurance_benefactor_detail_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_insurance_benefactor_detail_progress_bar.setVisibility(View.VISIBLE);
                String bFirstName = activity_insurance_benefactor_detail_first_name.getText().toString();
                String bLastName = activity_insurance_benefactor_detail_last_name.getText().toString();
                String bIdentificationNumber = activity_insurance_benefactor_detail_identification_number.getText().toString();
                String bContactNumber = activity_insurance_benefactor_detail_contact_number.getText().toString();
                String bRelationship = activity_insurance_benefactor_detail_relationship.getText().toString();
                try {
                    if (
                            activity_insurance_benefactor_detail_first_name.getText().toString().trim().length() > 0
                                    && activity_insurance_benefactor_detail_last_name.getText().toString().trim().length() > 0
                                    && activity_insurance_benefactor_detail_identification_number.getText().toString().trim().length() > 0
                                    && activity_insurance_benefactor_detail_contact_number.getText().toString().trim().length() > 0
                                    && activity_insurance_benefactor_detail_relationship.getText().toString().trim().length() > 0) {

                        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);
                        Call<InsuranceResponseDataObj> insuranceGetSelectedPolicyRequestCall = commonApiInterface.createBenefactorByUser(
                                AppConfig.APP_BRANDING_ID,
                                prefManager.getUserToken(),
                                new RequestBenefactor(
                                        bFirstName,
                                        bLastName,
                                        bIdentificationNumber,
                                        bContactNumber,
                                        "Approved",
                                        bRelationship)
                        );


                        if (Constants.type == Constants.Type.HEMAS) {
                            insuranceGetSelectedPolicyRequestCall = commonApiInterface.createBenefactorByUserForHemas(
                                    AppConfig.APP_BRANDING_ID,
                                    prefManager.getUserToken(),
                                    new RequestBenefactor(
                                            bFirstName,
                                            bLastName,
                                            bIdentificationNumber,
                                            bContactNumber,
                                            "Approved",
                                            bRelationship)
                            );
                        }


                        insuranceGetSelectedPolicyRequestCall.enqueue(new retrofit2.Callback<InsuranceResponseDataObj>() {

                            @Override
                            public void onResponse(Call<InsuranceResponseDataObj> call, Response<InsuranceResponseDataObj> response) {
                                activity_insurance_benefactor_detail_progress_bar.setVisibility(View.GONE);
                                activity_insurance_benefactor_detail_first_name.getText().clear();
                                activity_insurance_benefactor_detail_last_name.getText().clear();
                                activity_insurance_benefactor_detail_identification_number.getText().clear();
                                activity_insurance_benefactor_detail_contact_number.getText().clear();
                                activity_insurance_benefactor_detail_relationship.getText().clear();
                                if (response.isSuccessful()) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Benefactor added successfully", Toast.LENGTH_SHORT);
                                    toast.show();
                                    finish();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong try again", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<InsuranceResponseDataObj> call, Throwable t) {
                                activity_insurance_benefactor_detail_progress_bar.setVisibility(View.GONE);
                                Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong try again", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    } else {
                        activity_insurance_benefactor_detail_progress_bar.setVisibility(View.GONE);
                        Toast toast = Toast.makeText(getApplicationContext(), "Please fill the all fields", Toast.LENGTH_SHORT);
                        toast.show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }
}