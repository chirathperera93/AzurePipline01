package com.ayubo.life.ayubolife.model;

import java.util.ArrayList;

/**
 * Created by appdev on 2/16/2018.
 */



public class AchievementObj
{

    String id;
    String title;
    ArrayList<Achievement> achievement;

    public AchievementObj(String id, String title, ArrayList<Achievement> achievement) {
        this.id = id;
        this.title = title;
        this.achievement = achievement;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Achievement> getAchievement() {
        return achievement;
    }

    public void setAchievement(ArrayList<Achievement> achievement) {
        this.achievement = achievement;
    }
}

