package com.ayubo.life.ayubolife.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HealthInfo implements Serializable
{

    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("waist_size")
    @Expose
    private Integer waistSize;
    @SerializedName("bmi")
    @Expose
    private Integer bmi;
    private final static long serialVersionUID = -4823015561451297920L;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getWaistSize() {
        return waistSize;
    }

    public void setWaistSize(Integer waistSize) {
        this.waistSize = waistSize;
    }

    public Integer getBmi() {
        return bmi;
    }

    public void setBmi(Integer bmi) {
        this.bmi = bmi;
    }

}
