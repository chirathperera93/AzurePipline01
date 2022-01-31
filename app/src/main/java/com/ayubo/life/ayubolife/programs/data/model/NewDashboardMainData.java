package com.ayubo.life.ayubolife.programs.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NewDashboardMainData implements Serializable
{

    @SerializedName("life_points")
    @Expose
    private String lifePoints;

    @SerializedName("steps")
    @Expose
    private String steps;

    @SerializedName("engagement")
    @Expose
    private String engagement;

    @SerializedName("engagement_level")
    @Expose
    private String engagementLevel;


    @SerializedName("health_stat_text")
    @Expose
    private String healthStatText;

    @SerializedName("medical_charts")
    @Expose
    private List<MedicalChart> medicalCharts = null;

    @SerializedName("text_chart")
    @Expose
    private List<TextChart> textChart = null;

    @SerializedName("goals")
    @Expose
    private List<Goal> goals = null;

    @SerializedName("entitlements")
    @Expose
    private List<Entitlement> entitlements = null;


    private final static long serialVersionUID = -8781089979611773607L;

    public String getEngagement() {
        return engagement;
    }

    public void setEngagement(String engagement) {
        this.engagement = engagement;
    }

    public String getEngagementLevel() {
        return engagementLevel;
    }

    public void setEngagementLevel(String engagementLevel) {
        this.engagementLevel = engagementLevel;
    }

    public String getDashboardAvailable() {
        return steps;
    }

    public void setDashboardAvailable(String steps) {
        this.steps = steps;
    }


    public String getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(String lifePoints) {
        this.lifePoints = lifePoints;
    }



    public String getHealthStatText() {
        return healthStatText;
    }

    public void setHealthStatText(String healthStatText) {
        this.healthStatText = healthStatText;
    }

    public List<MedicalChart> getMedicalCharts() {
        return medicalCharts;
    }

    public void setMedicalCharts(List<MedicalChart> medicalCharts) {
        this.medicalCharts = medicalCharts;
    }

    public List<TextChart> getTextChart() {
        return textChart;
    }

    public void setTextChart(List<TextChart> textChart) {
        this.textChart = textChart;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<Entitlement> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(List<Entitlement> entitlements) {
        this.entitlements = entitlements;
    }

}