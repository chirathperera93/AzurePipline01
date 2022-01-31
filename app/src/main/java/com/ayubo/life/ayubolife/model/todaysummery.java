package com.ayubo.life.ayubolife.model;

/**
 * Created by AppDev5 on 1/21/2016.
 */
public class todaysummery {

    int id;
    String date;
    int step_tot;
    int run_tot;
    int jump_tot;
    float met_tot;
    String userid;
    float enegy_persent;
    float cal_tot;


    public todaysummery(int id, String userid, String date, int step_tot, int run_tot, int jump_tot, float met_tot, float enegy_persent, float cal_tot) {
        this.id = id;
        this.userid = userid;
        this.date = date;
        this.step_tot = step_tot;
        this.run_tot = run_tot;
        this.jump_tot = jump_tot;
        this.met_tot = met_tot;
        this.enegy_persent = enegy_persent;
        this.cal_tot = cal_tot;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRun_tot() {
        return run_tot;
    }

    public void setRun_tot(int run_tot) {
        this.run_tot = run_tot;
    }

    public int getJump_tot() {
        return jump_tot;
    }

    public void setJump_tot(int jump_tot) {
        this.jump_tot = jump_tot;
    }

    public float getEnegy_persent() {
        return enegy_persent;
    }

    public void setEnegy_persent(float enegy_persent) {
        this.enegy_persent = enegy_persent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStep_tot() {
        return step_tot;
    }

    public void setStep_tot(int step_tot) {
        this.step_tot = step_tot;
    }

    public float getMet_tot() {
        return met_tot;
    }

    public void setMet_tot(float met_tot) {
        this.met_tot = met_tot;
    }

    public float getCal_tot() {
        return cal_tot;
    }

    public void setCal_tot(float cal_tot) {
        this.cal_tot = cal_tot;
    }
}
