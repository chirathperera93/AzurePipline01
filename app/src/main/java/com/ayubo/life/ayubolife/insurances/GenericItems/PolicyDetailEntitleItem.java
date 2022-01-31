package com.ayubo.life.ayubolife.insurances.GenericItems;

public class PolicyDetailEntitleItem {

    private String mTitle;
    private String mAction;
    private String mMeta;

    public PolicyDetailEntitleItem(String title, String action, String meta) {
        this.mTitle = title;
        this.mAction = action;
        this.mMeta = meta;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAction() {
        return mAction;
    }

    public String getMeta() {
        return mMeta;
    }
}
