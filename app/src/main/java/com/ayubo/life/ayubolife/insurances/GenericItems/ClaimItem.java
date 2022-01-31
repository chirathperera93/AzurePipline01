package com.ayubo.life.ayubolife.insurances.GenericItems;

public class ClaimItem {

    private String cType;
    private String cDate;
    private String cStatus;

    public ClaimItem(String cType, String cDate, String cStatus) {
        this.cType = cType;
        this.cDate = cDate;
        this.cStatus = cStatus;
    }

    public String getType() {
        return cType;
    }

    public String getDate() {
        return cDate;
    }

    public String getStatus() {
        return cStatus;
    }
}
