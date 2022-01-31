package com.ayubo.life.ayubolife.ayubolifestepcounter;

import android.content.Context;
import android.content.SharedPreferences;

import com.ayubo.life.ayubolife.webrtc.App;

public class PrefManagerForSensorManager {
    private static PrefManagerForSensorManager instance;

    private static final String GOAL = "goal";
    private static final String IS_STARTED = "isStarted";
    private static final String THRESH_HOLD = "threshHold";
    private static final String LAST_REAL_STEPS = "lastRealSteps";

    SharedPreferences prefs;

    public PrefManagerForSensorManager() {
        prefs = App.getInstance().getSharedPreferences("AyuboLifeSensorPref", Context.MODE_PRIVATE);
    }

    public static PrefManagerForSensorManager getInstance() {
        if (instance == null) {
            instance = new PrefManagerForSensorManager();
        }
        return instance;
    }

    public void saveThreshHoldStep(int steps) {
        prefs.edit().putInt(THRESH_HOLD, steps).apply();
    }

    public int getThreshHoldSteps() {
        return prefs.getInt(THRESH_HOLD, 0);
    }

    public int getLastRealSteps() {
        return prefs.getInt(LAST_REAL_STEPS, 0);
    }

    public void saveRealSteps(int steps) {
        int totalSteps = getLastRealSteps() + steps;
        prefs.edit().putInt(LAST_REAL_STEPS, totalSteps).apply();
    }

    public void resetRealSteps() {
        prefs.edit().putInt(LAST_REAL_STEPS, 0).apply();
    }

    public void setStarted(Boolean status) {
        prefs.edit().putBoolean(IS_STARTED, status).apply();
    }

    public boolean isStarted() {
        return prefs.getBoolean(IS_STARTED, false);
    }

    public void saveGoal(int steps) {
        prefs.edit().putInt(GOAL, steps).apply();
    }

    public int getGoal() {
        return prefs.getInt(GOAL, 0);
    }
}
