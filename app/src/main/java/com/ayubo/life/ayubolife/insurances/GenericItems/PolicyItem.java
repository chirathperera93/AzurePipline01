package com.ayubo.life.ayubolife.insurances.GenericItems;

public class PolicyItem {

    private String mId;
    private String mHeading;
    private String mSubHeading;
    private String mStatus;
    private String mPolicyNo;
    private int mClickable;


    public PolicyItem(String id, String heading, String subHeading, String status, int clickable, String policyNo) {
        this.mId = id;
        this.mHeading = heading;
        this.mSubHeading = subHeading;
        this.mStatus = status;
        this.mClickable = clickable;
        this.mPolicyNo = policyNo;
    }

    public String getId() {
        return mId;
    }

    public String getHeading() {
        return mHeading;
    }

    public String getSubHeading() {
        return mSubHeading;
    }

    public String getStatus() {
        return mStatus;
    }

    public int getClickable() {
        return mClickable;
    }

    public String getPolicyNo() {
        return mPolicyNo;
    }

    public void setPolicyNo(String mPolicyNo) {
        this.mPolicyNo = mPolicyNo;
    }
}
