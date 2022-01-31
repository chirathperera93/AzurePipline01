package com.ayubo.life.ayubolife.lifeplus;

public class UserStepSyncObject {

    String steps;
    Long startTime;
    Long endTime;

    public UserStepSyncObject(String steps, Long startTime, Long endTime) {
        this.steps = steps;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getSteps() {
        return steps;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }
}
