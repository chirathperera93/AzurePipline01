package com.ayubo.life.ayubolife.ayubolifestepcounter.models;

public class StepsDetails {
    private boolean isSensorAvailable;
    private int currentStepsTreshHoldCount;
    private int currentSensorStepsCount;
    private int currentRealStepsCount;
    private int lastTotalStepCount;


    public int getCurrentSensorStepsCount() {
        return currentSensorStepsCount;
    }

    public void setCurrentSensorStepsCount(int currentSensorStepsCount) {
        this.currentSensorStepsCount = currentSensorStepsCount;
    }

    public int getLastTotatStepCount() {
        return lastTotalStepCount;
    }

    public void setLastTotalStepCount(int lastTotalStepCount) {
        this.lastTotalStepCount = lastTotalStepCount;
    }

    public int getCurrentRealStepsCount() {
        return currentRealStepsCount;
    }

    public void setCurrentRealStepsCount(int currentRealStepsCount) {
        this.currentRealStepsCount = currentRealStepsCount;
    }

    public int getCurrentStepsTreshHoldCount() {
        return currentStepsTreshHoldCount;
    }

    public void setCurrentStepsTreshHoldCount(int currentStepsTreshHoldCount) {
        this.currentStepsTreshHoldCount = currentStepsTreshHoldCount;
    }

    public boolean isSensorAvailable() {
        return isSensorAvailable;
    }

    public void setSensorAvailable(boolean sensorAvailable) {
        isSensorAvailable = sensorAvailable;
    }
}
