package com.ayubo.life.ayubolife.insurances.GenericItems;

public class ClaimHistoryItem {

    private String mClaimHistoryTitle;
    private String mClaimHistoryAmount;
    private String mClaimHistoryDate;
    private String mClaimHistoryStatus;

    public ClaimHistoryItem(String claimTitle, String claimAmount, String claimDate, String claimStatus) {
        this.mClaimHistoryTitle = claimTitle;
        this.mClaimHistoryAmount = claimAmount;
        this.mClaimHistoryDate = claimDate;
        this.mClaimHistoryStatus = claimStatus;
    }


    public String getClaimTitle() {
        return mClaimHistoryTitle;
    }

    public String getClaimAmount() {
        return mClaimHistoryAmount;
    }

    public String getClaimDate() {
        return mClaimHistoryDate;
    }

    public String getClaimStatus() {
        return mClaimHistoryStatus;
    }
}
