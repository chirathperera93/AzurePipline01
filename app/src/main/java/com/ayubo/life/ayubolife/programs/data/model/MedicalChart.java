package com.ayubo.life.ayubolife.programs.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MedicalChart implements Serializable
{

    @SerializedName("test_id")
    @Expose
    private String testId;
    @SerializedName("updatable")
    @Expose
    private Boolean updatable;
    @SerializedName("parameter_name")
    @Expose
    private String parameterName;
    @SerializedName("bar_min")
    @Expose
    private Double barMin;
    @SerializedName("bar_max")
    @Expose
    private Double barMax;
    @SerializedName("optimal_min")
    @Expose
    private Double optimalMin;
    @SerializedName("optimal_max")
    @Expose
    private Double optimalMax;
    @SerializedName("borderline_min")
    @Expose
    private Double borderlineMin;
    @SerializedName("borderline_max")
    @Expose
    private Double borderlineMax;
    @SerializedName("target")
    @Expose
    private Double target;


    @SerializedName("you")
    @Expose
    private String you;

    private final static long serialVersionUID = -2718542709494676118L;

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public Boolean getUpdatable() {
        return updatable;
    }

    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public Double getBarMin() {
        return barMin;
    }

    public void setBarMin(Double barMin) {
        this.barMin = barMin;
    }

    public Double getBarMax() {
        return barMax;
    }

    public void setBarMax(Double barMax) {
        this.barMax = barMax;
    }

    public Double getOptimalMin() {
        return optimalMin;
    }

    public void setOptimalMin(Double optimalMin) {
        this.optimalMin = optimalMin;
    }

    public Double getOptimalMax() {
        return optimalMax;
    }

    public void setOptimalMax(Double optimalMax) {
        this.optimalMax = optimalMax;
    }

    public Double getBorderlineMin() {
        return borderlineMin;
    }

    public void setBorderlineMin(Double borderlineMin) {
        this.borderlineMin = borderlineMin;
    }

    public Double getBorderlineMax() {
        return borderlineMax;
    }

    public void setBorderlineMax(Double borderlineMax) {
        this.borderlineMax = borderlineMax;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public String getYou() {
        return you;
    }

    public void setYou(String you) {
        this.you = you;
    }

}
