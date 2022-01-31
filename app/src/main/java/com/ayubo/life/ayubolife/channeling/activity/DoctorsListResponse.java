package com.ayubo.life.ayubolife.channeling.activity;

import com.ayubo.life.ayubolife.channeling.model.Expert;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DoctorsListResponse implements Serializable {

    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("data")
    @Expose
    private ArrayList data;


    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }
}
