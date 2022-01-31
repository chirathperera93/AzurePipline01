package com.ayubo.life.ayubolife.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetDefaultLanguageMainResponse implements Serializable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private List<GetDefaultLanguageMainData> data = null;
    private final static long serialVersionUID = 8843417729006829006L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<GetDefaultLanguageMainData> getData() {
        return data;
    }

    public void setData(List<GetDefaultLanguageMainData> data) {
        this.data = data;
    }

}


