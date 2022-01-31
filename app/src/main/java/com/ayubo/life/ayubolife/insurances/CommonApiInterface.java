package com.ayubo.life.ayubolife.insurances;

import com.ayubo.life.ayubolife.insurances.Classes.InsuranceResponseDataArrayList;
import com.ayubo.life.ayubolife.insurances.Classes.InsuranceResponseDataObj;
import com.ayubo.life.ayubolife.insurances.Classes.RequestBankAccount;
import com.ayubo.life.ayubolife.insurances.Classes.RequestBenefactor;
import com.ayubo.life.ayubolife.insurances.Classes.RequestFileClaim;
import com.ayubo.life.ayubolife.insurances.Classes.RequestFileClaimImage;
import com.ayubo.life.ayubolife.insurances.Classes.RequestRemoveFileClaimImage;
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData;
import com.ayubo.life.ayubolife.lifeplus.SubmitFeedbackObj;
import com.ayubo.life.ayubolife.masterapiconfig.DevAPIConfig;
import com.ayubo.life.ayubolife.reports.activity.ReportByIdResponse;
import com.ayubo.life.ayubolife.reports.activity.ReportTypeMain;
import com.ayubo.life.ayubolife.reports.activity.ReportsResponse;
import com.ayubo.life.ayubolife.revamp.v1.model.UpdateCorporateEmailBody;
import com.ayubo.life.ayubolife.revamp.v1.model.UpdateCorporateEmailResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CommonApiInterface {
    @GET(DevAPIConfig.GET_ALL_POLICIES)
    Call<InsuranceResponseDataArrayList> getPolicies(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization
    );

    @GET(DevAPIConfig.GET_ALL_POLICIES_V2)
    Call<ProfileDashboardResponseData> getPoliciesV2(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization
    );

    @GET(DevAPIConfig.GET_ALL_POLICIES_FOR_HEMAS)
    Call<ProfileDashboardResponseData> getPoliciesForHemas(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization
    );

    @GET(DevAPIConfig.GET_SELECTED_POLICY)
    Call<InsuranceResponseDataObj> getSelectedPolicyData(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("policy_id") String policy_id
    );

    @GET(DevAPIConfig.GET_SELECTED_POLICY_HEMAS)
    Call<InsuranceResponseDataObj> getSelectedPolicyDataForHemas(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("policy_id") String policy_id
    );

    @GET(DevAPIConfig.GET_SELECTED_POLICY_PAYMENT_HISTORY)
    Call<InsuranceResponseDataArrayList> getSelectedPolicyPaymentHistory(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("policy_id") String policy_id
    );

    @GET(DevAPIConfig.GET_SELECTED_POLICY_PAYMENT_HISTORY_FOR_HEMAS)
    Call<InsuranceResponseDataArrayList> getSelectedPolicyPaymentHistoryForHemas(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("policy_id") String policy_id
    );

    @GET(DevAPIConfig.GET_SELECTED_POLICY_ENTITLEMENT)
    Call<InsuranceResponseDataArrayList> getSelectedPolicyEntitlement(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("policy_id") String policy_id
    );

    @GET(DevAPIConfig.GET_SELECTED_POLICY_ENTITLEMENT_HEMAS)
    Call<InsuranceResponseDataArrayList> getSelectedPolicyEntitlementForHemas(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("policy_id") String policy_id
    );

    @POST(DevAPIConfig.CREATE_INSURANCE_BANK_ACCOUNT)
    Call<InsuranceResponseDataObj> createInsuranceBankAccount(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body RequestBankAccount body);

    @POST(DevAPIConfig.CREATE_INSURANCE_BANK_ACCOUNT_FOR_HEMAS)
    Call<InsuranceResponseDataObj> createInsuranceBankAccountForHemas(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body RequestBankAccount body);

    @POST(DevAPIConfig.CREATE_FILE_CLAIM)
    Call<InsuranceResponseDataObj> createInsuranceFileClaim(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body RequestFileClaim body);

    @POST(DevAPIConfig.CREATE_FILE_CLAIM_FOR_HEMAS)
    Call<InsuranceResponseDataObj> createInsuranceFileClaimForHemas(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body RequestFileClaim body);


    @POST(DevAPIConfig.IMAGE_UPLOAD)
    Call<InsuranceResponseDataObj> uploadFileClaimImages(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body RequestFileClaimImage body);

    @POST(DevAPIConfig.IMAGE_UPLOAD_FOR_HEMAS)
    Call<InsuranceResponseDataObj> uploadFileClaimImagesForHemas(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body RequestFileClaimImage body);

    @GET(DevAPIConfig.GET_BENEFACTOR_BY_USER)
    Call<InsuranceResponseDataArrayList> getBenefactorByUser(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization
    );

    @GET(DevAPIConfig.GET_BENEFACTOR_BY_USER_FOR_HEMAS)
    Call<InsuranceResponseDataArrayList> getBenefactorByUserForHemas(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization
    );

    @POST(DevAPIConfig.CREATE_BENEFACTOR_BY_USER)
    Call<InsuranceResponseDataObj> createBenefactorByUser(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body RequestBenefactor body);

    @POST(DevAPIConfig.CREATE_BENEFACTOR_BY_USER_FOR_HEMAS)
    Call<InsuranceResponseDataObj> createBenefactorByUserForHemas(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body RequestBenefactor body);

    @GET(DevAPIConfig.GET_CLAIM_TYPES_BY_USER)
    Call<InsuranceResponseDataArrayList> getClaimsTypesByUser(@Header("app_id") String app_id, @Header("Authorization") String authorization);

    @GET(DevAPIConfig.GET_CLAIM_TYPES_BY_USER_FOR_HEMAS)
    Call<InsuranceResponseDataArrayList> getClaimsTypesByUserForHemas(@Header("app_id") String app_id, @Header("Authorization") String authorization);

    @POST(DevAPIConfig.REMOVE_IMAGE)
    Call<InsuranceResponseDataObj> removeFileClaimImages(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body RequestRemoveFileClaimImage body);

    @POST(DevAPIConfig.REMOVE_IMAGE_FOR_HEMAS)
    Call<InsuranceResponseDataObj> removeFileClaimImagesForHemas(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Body RequestRemoveFileClaimImage body
    );


    @GET(DevAPIConfig.GET_ALL_REPORT_TYPES)
    Call<ReportsResponse> getAllReportTypes(@Header("Authorization") String authorization, @Header("app_id") String app_id);

    @GET(DevAPIConfig.GET_ALL_REPORT_TYPES_FOR_HEMAS)
    Call<ReportsResponse> getAllReportTypesForHemas(@Header("Authorization") String authorization, @Header("app_id") String app_id);

    @GET(DevAPIConfig.GET_REPORT_BY_ID)
    Call<ReportByIdResponse> getReportFieldsById(@Header("Authorization") String authorization, @Header("app_id") String app_id, @Query("report_id") String report_id);

    @GET(DevAPIConfig.GET_REPORT_BY_ID_FOR_HEMAS)
    Call<ReportByIdResponse> getReportFieldsByIdForHemas(@Header("Authorization") String authorization, @Header("app_id") String app_id, @Query("report_id") String report_id);


    @POST(DevAPIConfig.CREATE_RECORD)
    Call<ReportByIdResponse> createRecord(@Header("Authorization") String authorization, @Header("app_id") String app_id, @Body ReportTypeMain body);

    @POST(DevAPIConfig.CREATE_RECORD_FOR_HEMAS)
    Call<ReportByIdResponse> createRecordForHemas(@Header("Authorization") String authorization, @Header("app_id") String app_id, @Body ReportTypeMain body);

    @POST(DevAPIConfig.CREATE_SUBMIT_FEEDBACK)
    Call<ReportByIdResponse> createSubmitFeedBack(@Header("Authorization") String authorization, @Header("app_id") String app_id, @Body SubmitFeedbackObj submitFeedbackObj);

    @PUT(DevAPIConfig.UPDATE_CORPORATE_EMAIL)
    Call<UpdateCorporateEmailResponse> updateCorporate(
            @Header("Authorization") String authorization,
            @Header("app_id") String app_id,
            @Body UpdateCorporateEmailBody updateCorporateEmailBody
    );

}
