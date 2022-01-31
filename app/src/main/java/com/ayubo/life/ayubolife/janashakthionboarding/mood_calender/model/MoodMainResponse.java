package com.ayubo.life.ayubolife.janashakthionboarding.mood_calender.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MoodMainResponse  implements Serializable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private MoodMainData data;
    private final static long serialVersionUID = 216683907202970101L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public MoodMainData getData() {
        return data;
    }

    public void setData(MoodMainData data) {
        this.data = data;
    }

}