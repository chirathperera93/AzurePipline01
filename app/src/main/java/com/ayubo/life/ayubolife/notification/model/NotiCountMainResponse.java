package com.ayubo.life.ayubolife.notification.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotiCountMainResponse implements Serializable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private NotiCountMainData data;
    private final static long serialVersionUID = 216683907202970101L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public NotiCountMainData getData() {
        return data;
    }

    public void setData(NotiCountMainData data) {
        this.data = data;
    }

}