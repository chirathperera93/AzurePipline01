package com.ayubo.life.ayubolife.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Values implements Serializable
{
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("risk")
    @Expose
    private String risk;
    private final static long serialVersionUID = 394592778490616160L;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

}