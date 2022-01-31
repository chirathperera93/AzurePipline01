package com.ayubo.life.ayubolife.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JoinChallengeObj  implements Serializable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private JoinChallengeDataObj data;
    private final static long serialVersionUID = 216683907202970101L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public JoinChallengeDataObj getData() {
        return data;
    }

    public void setData(JoinChallengeDataObj data) {
        this.data = data;
    }

}