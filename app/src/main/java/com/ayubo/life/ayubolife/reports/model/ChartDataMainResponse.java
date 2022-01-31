package com.ayubo.life.ayubolife.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChartDataMainResponse implements Serializable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private ChData data;
    private final static long serialVersionUID = 216683907202970101L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public ChData getData() {
        return data;
    }

    public void setData(ChData data) {
        this.data = data;
    }

}














