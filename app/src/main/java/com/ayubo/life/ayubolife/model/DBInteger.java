package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 3/8/2017.
 */

public class DBInteger {

    Integer startTime;
    Integer endTime;
    String availability;

    public DBInteger(Integer startTime, Integer endTime, String availability) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.availability = availability;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
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
}
