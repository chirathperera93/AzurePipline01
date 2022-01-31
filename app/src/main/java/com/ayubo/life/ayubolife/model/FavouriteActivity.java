package com.ayubo.life.ayubolife.model;

/**
 * Created by AppDev5 on 1/4/2016.
 */

public class FavouriteActivity {


    String userid;
    String name;
    String met;
    String image;
    public FavouriteActivity(String userid, String name, String image, String met) {

        this.userid=userid;
        this.name = name;
        this.image = image;
        this.met = met;
    }
    public String getMet() {
        return met;
    }

    public void setMet(String met) {
        this.met = met;
    }

    // constructors
    public FavouriteActivity() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }




    // setters



    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }



    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return this.image;
    }

    // getters





}