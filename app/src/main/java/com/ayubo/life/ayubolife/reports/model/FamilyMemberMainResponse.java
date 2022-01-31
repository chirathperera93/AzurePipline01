package com.ayubo.life.ayubolife.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FamilyMemberMainResponse implements Serializable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private List<FamilyMemberData> data = null;
    private final static long serialVersionUID = 8843417729006829006L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<FamilyMemberData> getData() {
        return data;
    }

    public void setData(List<FamilyMemberData> data) {
        this.data = data;
    }

}