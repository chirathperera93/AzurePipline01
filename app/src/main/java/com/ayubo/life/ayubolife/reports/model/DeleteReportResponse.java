package com.ayubo.life.ayubolife.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteReportResponse {
    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("data")
    @Expose
    private DeleteData data;

    @SerializedName("message")
    @Expose
    private String message;

    private final static long serialVersionUID = 216683907202970101L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public DeleteData getData() {
        return data;
    }

    public void setData(DeleteData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
