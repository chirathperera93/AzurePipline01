package com.ayubo.life.ayubolife.ayubolifestepcounter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.ayubolifestepcounter.models.StepsDetails;
import com.ayubo.life.ayubolife.webrtc.App;

import org.greenrobot.eventbus.EventBus;

import static android.content.Context.SENSOR_SERVICE;

public class SensorManagerAyuboLife implements SensorEventListener {
    private final static long MICROSECONDS_IN_ONE_MINUTE = 60000000;

    private static SensorManagerAyuboLife instance;
    private final Context context;
    private final PrefManagerForSensorManager mPrefman;
    private int steps;
    private SensorManager mSensman;
    private int goal;

    private SensorManagerAyuboLife(Context context) {
        this.context = context;
        this.mSensman = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        this.mPrefman = PrefManagerForSensorManager.getInstance();
    }

    public static SensorManagerAyuboLife getInstance() {
        if (instance == null) {
            instance = new SensorManagerAyuboLife(App.getInstance());
        }
        return instance;
    }

    public static boolean isSensorExist() {
        SensorManager mSensman = (SensorManager) App.getInstance().getSystemService(SENSOR_SERVICE);
        Sensor portrait = mSensman.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (portrait == null) {
            Toast.makeText(App.getInstance(), "Step Counter Sensor not found", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void register() {

        try {
            mSensman.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG) {
            if (mSensman.getSensorList(Sensor.TYPE_STEP_COUNTER).size() < 1) return; // emulator
        }

        Sensor portrait = mSensman.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (portrait == null) {
            Toast.makeText(context, "Step Counter Sensor not found", Toast.LENGTH_SHORT).show();
            return;
        }
        // enable batching with delay of max 5 min
        mSensman.registerListener(this, portrait,
                SensorManager.SENSOR_DELAY_NORMAL, (int) (5 * MICROSECONDS_IN_ONE_MINUTE));

        mPrefman.setStarted(false);
    }

    public void unRegister() {
        mSensman.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values[0] > Integer.MAX_VALUE) {
            return;
        } else {
            System.out.println("=========sensorEvent===step=======================" + sensorEvent.values[0]);
            setCurrentSteps((int) sensorEvent.values[0]);
            if (!mPrefman.isStarted()) {
                mPrefman.setStarted(true);
                mPrefman.saveThreshHoldStep(getCurrentSensorSteps());
            }
            postSteps(false);
        }
    }

    public void postSteps(boolean isStopping) {
        StepsDetails stepsDetails = new StepsDetails();
        stepsDetails.setSensorAvailable(isSensorExist());
        stepsDetails.setCurrentStepsTreshHoldCount(mPrefman.getThreshHoldSteps());
        stepsDetails.setCurrentSensorStepsCount(getCurrentSensorSteps());
        stepsDetails.setCurrentRealStepsCount(calculateRealSteps());
        stepsDetails.setLastTotalStepCount(mPrefman.getLastRealSteps() == 0 ? calculateRealSteps() : mPrefman.getLastRealSteps());

        if (!isStopping) {
            SensorManagerHandlerService.updateStepsNotification();
        } else {
            stepsDetails.setCurrentRealStepsCount(0);
        }

        EventBus.getDefault().post(stepsDetails);
    }

    public void updateTotalRealSteps() {
        if (calculateRealSteps() > 0) {
            mPrefman.saveRealSteps(calculateRealSteps());
        }
    }

    public void updateThreaHold() {
        mPrefman.saveThreshHoldStep(getCurrentSensorSteps());
    }

    public void resetTotalSteps() {
        //to make it looks like a fresh start
        mPrefman.resetRealSteps();
        mPrefman.setStarted(false);
    }

    private int calculateRealSteps() {
        int threshHoldSteps = mPrefman.getThreshHoldSteps();
        int realSteps = getCurrentSensorSteps() - threshHoldSteps;
        return realSteps;
    }

    public void setGoal(int steps) {
        mPrefman.saveGoal(steps);
    }

    @Override
    public void onAccuracyChanged(Sensor portrait, int i) {

    }

    public int getCurrentRealSteps() {
        return calculateRealSteps();
    }

    public int getCurrentSensorSteps() {
        return this.steps;
    }

    private void setCurrentSteps(int steps) {
        this.steps = steps;
    }

    public int getGoal() {
        return mPrefman.getGoal();
    }

    public int getLastTotalRealSteps() {
        return mPrefman.getLastRealSteps();
    }
}
