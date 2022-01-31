package com.ayubo.life.ayubolife.lifeplus;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FeedBackResponse {

    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("data")
    @Expose
    private JsonObject data;

    @SerializedName("message")
    @Expose
    private String message;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
