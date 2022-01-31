package com.ayubo.life.ayubolife.insurances.GenericItems;

public class PolicyDetailCoversClaimItem {

    private String dName;
    private String dValue;

    public PolicyDetailCoversClaimItem(String dName, String dValue) {
        this.dName = dName;
        this.dValue = dValue;
    }

    public String getName() {
        return dName;
    }

    public String getValue() {
        return dValue;
    }
}
