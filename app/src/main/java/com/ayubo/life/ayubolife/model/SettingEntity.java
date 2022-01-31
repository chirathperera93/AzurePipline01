package com.ayubo.life.ayubolife.model;

/**
 * Created by AppDev5 on 2/17/2016.
 */
public class SettingEntity {

    String userid;
    String stepgoal;
    String energygoal;
    String calerygoal;
    String heightgoal;
    String weightgoal;
    String waist;
    String bpmgoal;
    String updateddate;
    String updatedstatus;


    public SettingEntity(String userid,
                         String stepgoal,
                         String energygoal,
                         String calerygoal,
                         String heightgoal,
                         String weightgoal,
                         String waist,
                         String bpmgoal,
                         String updateddate,
                         String updatedstatus) {

        this.userid=userid;
        this.stepgoal = stepgoal;
        this.energygoal = energygoal;
        this.calerygoal = calerygoal;
        this.heightgoal = heightgoal;
        this.weightgoal = weightgoal;
        this.waist=waist;
        this.bpmgoal = bpmgoal;
        this.updateddate = updateddate;
        this.updatedstatus = updatedstatus;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStepgoal() {
        return stepgoal;
    }

    public void setStepgoal(String stepgoal) {
        this.stepgoal = stepgoal;
    }

    public String getEnergygoal() {
        return energygoal;
    }

    public void setEnergygoal(String energygoal) {
        this.energygoal = energygoal;
    }

    public String getCalerygoal() {
        return calerygoal;
    }

    public void setCalerygoal(String calerygoal) {
        this.calerygoal = calerygoal;
    }

    public String getHeightgoal() {
        return heightgoal;
    }

    public void setHeightgoal(String heightgoal) {
        this.heightgoal = heightgoal;
    }

    public String getWeightgoal() {
        return weightgoal;
    }

    public void setWeightgoal(String weightgoal) {
        this.weightgoal = weightgoal;
    }

    public String getBpmgoal() {
        return bpmgoal;
    }

    public void setBpmgoal(String bpmgoal) {
        this.bpmgoal = bpmgoal;
    }

    public String getUpdateddate() {
        return updateddate;
    }

    public void setUpdateddate(String updateddate) {
        this.updateddate = updateddate;
    }

    public String getUpdatedstatus() {
        return updatedstatus;
    }

    public void setUpdatedstatus(String updatedstatus) {
        this.updatedstatus = updatedstatus;
    }
}