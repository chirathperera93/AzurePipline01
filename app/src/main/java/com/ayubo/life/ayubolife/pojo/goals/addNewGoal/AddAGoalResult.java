package com.ayubo.life.ayubolife.pojo.goals.addNewGoal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddAGoalResult {

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private ResultData data;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


}



