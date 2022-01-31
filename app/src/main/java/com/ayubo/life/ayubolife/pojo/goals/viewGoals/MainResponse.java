package com.ayubo.life.ayubolife.pojo.goals.viewGoals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainResponse
{

@SerializedName("result")
@Expose
private Integer result;
@SerializedName("data")
@Expose
private List<com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum> data = null;

public Integer getResult() {
        return result;
        }

public void setResult(Integer result) {
        this.result = result;
        }

public List<Datum> getData() {
        return data;
        }

public void setData(List<com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum> data) {
        this.data = data;
        }

        }


