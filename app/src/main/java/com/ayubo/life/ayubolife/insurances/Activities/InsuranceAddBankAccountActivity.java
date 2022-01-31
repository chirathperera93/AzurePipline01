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
import com.ayubo.life.ayubolife.insurances.CommonApiInterface;
import com.ayubo.life.ayubolife.insurances.Classes.InsuranceResponseDataObj;
import com.ayubo.life.ayubolife.insurances.Classes.RequestBankAccount;
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.flavors.changes.Constants;


import retrofit2.Call;
import retrofit2.Response;

public class InsuranceAddBankAccountActivity extends AppCompatActivity {

    EditText activityInsuranceAddBankAccountHolderName,
            activityInsuranceAddBankAccountNickName,
            activityInsuranceAddBankAccountNumber,
            activityInsuranceAddBankAccountReNumber,
            activityInsuranceAddBankAccountBankName,
            activityInsuranceAddBankAccountBranchName;

    Button activityInsuranceAddBankAccountSaveBtn;

    LinearLayout activityInsuranceAddBankAccountBackBtnLayout;

    PrefManager prefManager;

    ProgressAyubo activity_insurance_add_bank_account_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_add_bank_account);

        prefManager = new PrefManager(this);

        activityInsuranceAddBankAccountHolderName = findViewById(R.id.activity_insurance_add_bank_account_holder_name);
        activityInsuranceAddBankAccountNickName = findViewById(R.id.activity_insurance_add_bank_account_nick_name);
        activityInsuranceAddBankAccountNumber = findViewById(R.id.activity_insurance_add_bank_account_number);
        activityInsuranceAddBankAccountReNumber = findViewById(R.id.activity_insurance_add_bank_account_re_number);
        activityInsuranceAddBankAccountBankName = findViewById(R.id.activity_insurance_add_bank_account_bank_name);
        activityInsuranceAddBankAccountBranchName = findViewById(R.id.activity_insurance_add_bank_account_branch_name);

        activityInsuranceAddBankAccountSaveBtn = findViewById(R.id.activity_insurance_add_bank_account_save_btn);

        activityInsuranceAddBankAccountBackBtnLayout = findViewById(R.id.activity_insurance_add_bank_account_back_btn_layout);

        activity_insurance_add_bank_account_progress_bar = findViewById(R.id.activity_insurance_add_bank_account_progress_bar);

        setButtonsClick();

    }

    private void setButtonsClick() {
        activityInsuranceAddBankAccountSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_insurance_add_bank_account_progress_bar.setVisibility(View.VISIBLE);

                if (activityInsuranceAddBankAccountHolderName.getText().toString().trim().length() > 0
                        && activityInsuranceAddBankAccountNickName.getText().toString().trim().length() > 0
                        && activityInsuranceAddBankAccountHolderName.getText().toString().trim().length() > 0
                        && activityInsuranceAddBankAccountNumber.getText().toString().trim().length() > 0
                        && activityInsuranceAddBankAccountReNumber.getText().toString().trim().length() > 0
                        && activityInsuranceAddBankAccountBankName.getText().toString().trim().length() > 0
                        && activityInsuranceAddBankAccountBranchName.getText().toString().trim().length() > 0) {
                    if (activityInsuranceAddBankAccountNumber.getText().toString().equals(activityInsuranceAddBankAccountReNumber.getText().toString())) {

                        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);
                        Call<InsuranceResponseDataObj> insuranceGetSelectedPolicyRequestCall = commonApiInterface.createInsuranceBankAccount(
                                AppConfig.APP_BRANDING_ID,
                                prefManager.getUserToken(),
                                new RequestBankAccount(
                                        activityInsuranceAddBankAccountHolderName.getText().toString(),
                                        activityInsuranceAddBankAccountNickName.getText().toString(),
                                        activityInsuranceAddBankAccountNumber.getText().toString(),
                                        activityInsuranceAddBankAccountBankName.getText().toString(),
                                        activityInsuranceAddBankAccountBranchName.getText().toString())
                        );

                        if (Constants.type == Constants.Type.HEMAS) {
                            insuranceGetSelectedPolicyRequestCall = commonApiInterface.createInsuranceBankAccountForHemas(
                                    AppConfig.APP_BRANDING_ID,
                                    prefManager.getUserToken(),
                                    new RequestBankAccount(
                                            activityInsuranceAddBankAccountHolderName.getText().toString(),
                                            activityInsuranceAddBankAccountNickName.getText().toString(),
                                            activityInsuranceAddBankAccountNumber.getText().toString(),
                                            activityInsuranceAddBankAccountBankName.getText().toString(),
                                            activityInsuranceAddBankAccountBranchName.getText().toString())
                            );
                        }


                        insuranceGetSelectedPolicyRequestCall.enqueue(new retrofit2.Callback<InsuranceResponseDataObj>() {

                            @Override
                            public void onResponse(Call<InsuranceResponseDataObj> call, Response<InsuranceResponseDataObj> response) {
                                activity_insurance_add_bank_account_progress_bar.setVisibility(View.GONE);
                                activityInsuranceAddBankAccountHolderName.getText().clear();
                                activityInsuranceAddBankAccountNickName.getText().clear();
                                activityInsuranceAddBankAccountNumber.getText().clear();
                                activityInsuranceAddBankAccountReNumber.getText().clear();
                                activityInsuranceAddBankAccountBankName.getText().clear();
                                activityInsuranceAddBankAccountBranchName.getText().clear();
                                if (response.isSuccessful()) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Bank account added successfully", Toast.LENGTH_SHORT);
                                    toast.show();
                                    finish();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong try again", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<InsuranceResponseDataObj> call, Throwable t) {
                                activity_insurance_add_bank_account_progress_bar.setVisibility(View.GONE);
                                Toast toast = Toast.makeText(getApplicationContext(), R.string.servcer_not_available, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    } else {
                        activity_insurance_add_bank_account_progress_bar.setVisibility(View.GONE);
                        activityInsuranceAddBankAccountReNumber.setError("Account number does not match");
                        Toast toast = Toast.makeText(getApplicationContext(), "Account numbers do not match", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    activity_insurance_add_bank_account_progress_bar.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(), "Please fill the all fields", Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });

        activityInsuranceAddBankAccountBackBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}