package com.ayubo.life.ayubolife.programs.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NewDashboardMainResponse implements Serializable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private NewDashboardMainData data;
    private final static long serialVersionUID = 216683907202970101L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public NewDashboardMainData getData() {
        return data;
    }

    public void setData(NewDashboardMainData data) {
        this.data = data;
    }

}
