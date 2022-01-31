package com.ayubo.life.ayubolife.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class PayHeraMainResponse implements Serializable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private PayData data;
    private final static long serialVersionUID = 216683907202970101L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public PayData getData() {
        return data;
    }

    public void setData(PayData data) {
        this.data = data;
    }

}