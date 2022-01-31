package com.ayubo.life.ayubolife.insurances;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class InsurancePrefManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "ayubolife";

    private static final String KEY_SELECTED_POLICY_ID = "KEY_SELECTED_POLICY_ID";
    private static final String KEY_SELECTED_POLICY_NAME = "KEY_SELECTED_POLICY_NAME";
    private static final String KEY_SELECTED_POLICY_STATUS = "KEY_SELECTED_POLICY_STATUS";

    private static final String KEY_POLICY_DETAIL_POLICY_DATA_OBJECT = "policy_detail_policy_data_object";
    private static final String KEY_POLICY_DETAIL_CLAIM_HISTORY_OBJECT = "policy_detail_claim_history_object";
    private static final String KEY_SELECTED_CLAIM_TYPE = "selected_claim_type";

    public InsurancePrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setSelectedPolicyDetail(String policyId, String policyName, String policyStatus) {
        editor.putString(KEY_SELECTED_POLICY_ID, policyId);
        editor.putString(KEY_SELECTED_POLICY_NAME, policyName);
        editor.putString(KEY_SELECTED_POLICY_STATUS, policyStatus);
        editor.commit();
    }

    public String getSelectedPolicyId() {
        return pref.getString(KEY_SELECTED_POLICY_ID, "");
    }

    public String getSelectedPolicyName() {
        return pref.getString(KEY_SELECTED_POLICY_NAME, "");
    }

    public String getSelectedPolicyStatus() {
        return pref.getString(KEY_SELECTED_POLICY_STATUS, "");
    }

    public void setSelectedPolicyOverviewPolicyDetail(String policyDetailPolicyDataJsonObject) {
        editor.putString(KEY_POLICY_DETAIL_POLICY_DATA_OBJECT, policyDetailPolicyDataJsonObject);
        editor.commit();
    }

    public HashMap<String, String> getSelectedPolicyOverviewPolicyDetail() {
        HashMap<String, String> policyDetail = new HashMap<>();
        policyDetail.put("policy_detail_policy_data_object", pref.getString(KEY_POLICY_DETAIL_POLICY_DATA_OBJECT, ""));
        return policyDetail;
    }

    public void setSelectedPolicyOverviewClaimsHistory(String claimsHistoryDataJsonObject) {
        editor.putString(KEY_POLICY_DETAIL_CLAIM_HISTORY_OBJECT, claimsHistoryDataJsonObject);
        editor.commit();
    }

    public HashMap<String, String> getSelectedPolicyOverviewClaimsHistory() {
        HashMap<String, String> claimsHistory = new HashMap<>();
        claimsHistory.put("policy_detail_claim_history_object", pref.getString(KEY_POLICY_DETAIL_CLAIM_HISTORY_OBJECT, ""));
        return claimsHistory;
    }

    public void setSelectedClaimTypeDetail(JsonObject clickedItem) {
        editor.putString(KEY_SELECTED_CLAIM_TYPE, String.valueOf(clickedItem));
        editor.commit();
    }

    public HashMap<String, String> getSelectedClaimTypeDetail() {
        HashMap<String, String> selectedClaim = new HashMap<>();
        selectedClaim.put("selected_claim_type", pref.getString(KEY_SELECTED_CLAIM_TYPE, ""));
        return selectedClaim;
    }

}
