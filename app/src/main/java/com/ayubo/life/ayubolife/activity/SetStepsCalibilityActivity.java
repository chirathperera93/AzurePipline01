package com.ayubo.life.ayubolife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.service.AyuboStepService;
import com.ayubo.life.ayubolife.utility.Ram;

import java.util.Timer;
import java.util.TimerTask;

public class SetStepsCalibilityActivity extends AppCompatActivity {
    //SeekBar simpleSeekBar;
    int startSteps, endSteps;
    Button btn_set_sensityvity;
    boolean isStarted = false;
    TextView lbl_level, lbl_level2, lbl_level5, lbl_level3, lbl_step;
    Timer timer_foutTimePerDay;
    TimerTask timer_foutTimePerDay_doAsynchronousTask;
    float stepSensitivity;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_steps_calibility);

        // simpleSeekBar = (SeekBar) findViewById(R.id.seekBar);
        btn_set_sensityvity = (Button) findViewById(R.id.btn_set_sensityvity);

        lbl_level = (TextView) findViewById(R.id.lbl_level);
        lbl_level2 = (TextView) findViewById(R.id.lbl_level2);
        lbl_level3 = (TextView) findViewById(R.id.lbl_level3);
        lbl_level5 = (TextView) findViewById(R.id.lbl_level5);

        //  lbl_level5.setText(Integer.toString(Ram.getCurrentRealStepCount()));


        lbl_step = (TextView) findViewById(R.id.lbl_step);
        btn_set_sensityvity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double stepDiff;
                double t = 50;
                int c = 0;
                double z = 0;
                double yourSteps = 0;
                if (isStarted) {

                    endSteps = Ram.getCurrentWalkStepCount();

                    stepDiff = endSteps - startSteps;

                    z = t / stepDiff;

                    yourSteps = z * stepDiff;

                    String Yourcorrectsteps = String.valueOf(yourSteps);

                    String Str_stepDiff = String.valueOf(stepDiff);

                    String sz = String.valueOf(z);
                    lbl_level.setText("Expected steps : 20");
                    //  lbl_level.setText("Expected steps : 20");
                    lbl_level2.setText("Counted steps : " + Str_stepDiff);
                    lbl_level3.setText("Your correct steps : " + Yourcorrectsteps);

                    //  Ram.setStepSensitivity(z);

                    Intent myIntent = new Intent(SetStepsCalibilityActivity.this, AyuboStepService.class);
                    SetStepsCalibilityActivity.this.startService(myIntent);

                } else {

                    isStarted = true;
                    btn_set_sensityvity.setText("STOP");
                    startSteps = Ram.getCurrentWalkStepCount();

                    lbl_level.setText(Integer.toString(startSteps));

                    startTimer_OnlineUpdate();
                }

            }
        });
        //  btn_set_sensityvity

        //  int maxValue=simpleSeekBar.getMax();
        //  int seekBarValue= simpleSeekBar.getProgress();
//        simpleSeekBar.setMax(10);
//        simpleSeekBar.setProgress(5);
//
//        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int progressChangedValue = 0;
//
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                progressChangedValue = progress;
//            }
//
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                // TODO Auto-generated method stub
//            }
//
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                stepSensitivity=progressChangedValue;
//                lbl_level.setText(Integer.toString(progressChangedValue));
//                Toast.makeText(SetStepsCalibilityActivity.this, "Sensitivity level :" + progressChangedValue,
//                        Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    public void saveSensitivity(View v) {


//        Intent myIntent = new Intent(SetStepsCalibilityActivity.this, AyuboStepService.class);
//        SetStepsCalibilityActivity.this.startService(myIntent);


        //startTimer_OnlineUpdate();
    }

    public void startTimer_OnlineUpdate() {
        timer_foutTimePerDay = new Timer();
        initializeTimerTaskS();
        timer_foutTimePerDay.schedule(timer_foutTimePerDay_doAsynchronousTask, 10, 1000);
        //  timer_foutTimePerDay.schedule(timer_foutTimePerDay_doAsynchronousTask, 10, 900000);
    }


    public void initializeTimerTaskS() {
        timer_foutTimePerDay_doAsynchronousTask = new TimerTask() {

            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        // lbl_level5.setText(Integer.toString(Ram.getCurrentRealStepCount()));
                        //  System.out.println("========================"+Ram.getCurrentWalkStepCount());
                    }
                });
            }

        };
    }

//    @Override
//    public void onBackPressed() {
//
//    }

}
