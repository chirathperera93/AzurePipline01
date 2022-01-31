package com.ayubo.life.ayubolife.timeline.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PopupMainResponse  implements Serializable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private PopupMainData data = null;
    private final static long serialVersionUID = 8843417729006829006L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public PopupMainData getData() {
        return data;
    }

    public void setData(PopupMainData data) {
        this.data = data;
    }

}