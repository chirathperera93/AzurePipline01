package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 3/15/2017.
 */

public class MinitSlotObj {


    Integer startTime;
    Integer endTime;
    String availability;
    String type;
    String app_userid;
    public MinitSlotObj(Integer startTime, Integer endTime, String availability, String type,String app_userid) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.availability = availability;
        this.type = type;  this.app_userid = app_userid;
    }

    public String getApp_userid() {
        return app_userid;
    }

    public void setApp_userid(String app_userid) {
        this.app_userid = app_userid;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
