package com.ayubo.life.ayubolife.insurances.Classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InsuranceResponseDataObj implements Serializable {

    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("data")
    @Expose
    private Object data;

    @SerializedName("message")
    @Expose
    private String message;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
