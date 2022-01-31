package com.ayubo.life.ayubolife.masterapiconfig;

import com.ayubo.life.ayubolife.rest.ApiClient;
import com.flavors.changes.Constants;

public class DevAPIConfig {

    // Insurance APIs
    public static final String GET_ALL_POLICIES = "https://dev-ayubolife-insurance-policy.azurewebsites.net/dev/v1/get-all";
    public static final String GET_ALL_POLICIES_V2 = "https://dev-ayubolife-insurance-policy.azurewebsites.net/dev/v2/get-all";
    public static final String GET_ALL_POLICIES_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-insurance-policies/v1/get-all";

    public static final String GET_SELECTED_POLICY = "https://dev-ayubolife-insurance-policy.azurewebsites.net/dev/v1/getpolicybyid";
    public static final String GET_SELECTED_POLICY_HEMAS = "https://dev-ayubolife-insurance-policy.azurewebsites.net/dev/v1/getpolicybyid";

    public static final String GET_SELECTED_POLICY_PAYMENT_HISTORY = "https://dev-ayubolife-insurance-payments.azurewebsites.net/dev/v1/getpaymentsforuser";
    public static final String GET_SELECTED_POLICY_PAYMENT_HISTORY_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-insurance-payments/v1/getpaymentsforuser";

    public static final String GET_SELECTED_POLICY_ENTITLEMENT = "https://dev-ayubolife-insurance-entitlements.azurewebsites.net/dev/v1/getentitlementsforpolicy";
    public static final String GET_SELECTED_POLICY_ENTITLEMENT_HEMAS = "https://hemas-apim.azure-api.net/hemas-insurance-entitlements/v1/getentitlementsforpolicy";

    public static final String CREATE_INSURANCE_BANK_ACCOUNT = "https://dev-ayubolife-insurance-bankaccounts.azurewebsites.net/dev/v1/create";
    public static final String CREATE_INSURANCE_BANK_ACCOUNT_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-insurance-bankaccounts/v1/create";

    public static final String CREATE_FILE_CLAIM = "https://dev-ayubo-insurance-claims.azurewebsites.net/dev/v1/create";
    public static final String CREATE_FILE_CLAIM_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-insurance-claims/v1/create";

    public static final String IMAGE_UPLOAD = "https://devayubo-media.azurewebsites.net/dev/v1/upload";
    public static final String IMAGE_UPLOAD_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-media/v1/upload";

    public static final String GET_BENEFACTOR_BY_USER = "https://devayubo-insurance-benefactor.azurewebsites.net/dev/v1/getbenefactorsforusers";
    public static final String GET_BENEFACTOR_BY_USER_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-insurance-benefactor/v1/getbenefactorsforusers";

    public static final String CREATE_BENEFACTOR_BY_USER = "https://devayubo-insurance-benefactor.azurewebsites.net/dev/v1/create";
    public static final String CREATE_BENEFACTOR_BY_USER_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-insurance-benefactor/v1/create";

    public static final String GET_CLAIM_TYPES_BY_USER = "https://dev-ayubo-insurance-claims.azurewebsites.net/dev/v1/getclaimtypes";
    public static final String GET_CLAIM_TYPES_BY_USER_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-insurance-claims/v1/getclaimtypes";

    public static final String REMOVE_IMAGE = "https://devayubo-media.azurewebsites.net/dev/v1/remove";
    public static final String REMOVE_IMAGE_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-media/v1/remove";

    public static final String GET_ALL_REPORT_TYPES = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-records/v1/get-all-report-types";
    public static final String GET_ALL_REPORT_TYPES_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-records/v1/get-all-report-types";

    public static final String GET_REPORT_BY_ID = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-records/v1/get-report-by-id";
    public static final String GET_REPORT_BY_ID_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-records/v1/get-report-by-id";

    public static final String CREATE_RECORD = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-records/v1/create-record";
    public static final String CREATE_RECORD_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-records/v1/create-record";


    // Walk-win APIs
    public static final String SAVE_DAILY_STEP = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-wnw-stepdata/v2/savedailysteps";
    public static final String SAVE_DAILY_STEP_FOR_HEMAS = "https://hemas-apim.azure-api.net/hemas-walknwin-stepdata/v2/savedailysteps";

    public static final String GET_DAILY_TOTAL_STEP_BY_DATES = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-wnw-stepdata/v1/getsteps";
    public static final String GET_DAILY_TOTAL_STEP_BY_DATES_HEMAS = "https://hemas-apim.azure-api.net/hemas-walknwin-stepdata/v1/getsteps";

    public static final String GET_CHALLENGE_BY_ID = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-wnw-challenges/v1/getchallengebyid";
    public static final String GET_CHALLENGE_BY_ID_FOR_HEMAS = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-wnw-challenges/v1/getchallengebyid";

    public static final String JOIN_CHALLENGE = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-wnw-challenges/v1/join-challenge";
    public static final String JOIN_CHALLENGE_HEMAS = "https://hemas-apim.azure-api.net/hemas-user-dashboard/v1/join-challenge";

    public static final String CLAIM_REWARD = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-wnw-challenges/v1/claim-reward";
    public static final String CLAIM_REWARD_HEMAS = "https://hemas-apim.azure-api.net/hemas-walknwin-challenges/v1/claim-reward";


//    public static final String GET_DOCTORS_LIST = "https://livehappy.ayubo.life/custom/service/v4_1_custom/rest.php";
    public static final String GET_DOCTORS_LIST = "https://livehappy.ayubo.life/custom/service/v7/rest.php";

    public static final String GET_FEED_BACK = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-feedback/v1/get-all-categories";

    public static final String CREATE_SUBMIT_FEEDBACK = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-feedback/v1/create";
    public static final String UPDATE_CORPORATE_EMAIL = "update-corporate";

}
