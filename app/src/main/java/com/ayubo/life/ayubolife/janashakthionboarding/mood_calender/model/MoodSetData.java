package com.ayubo.life.ayubolife.janashakthionboarding.mood_calender.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MoodSetData  implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("mood")
    @Expose
    private String mood;
    @SerializedName("mood_date")
    @Expose
    private String moodDate;
    @SerializedName("mood_time")
    @Expose
    private Object moodTime;
    @SerializedName("mood_feedback")
    @Expose
    private String moodFeedback;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    private final static long serialVersionUID = 7138841916650826791L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getMoodDate() {
        return moodDate;
    }

    public void setMoodDate(String moodDate) {
        this.moodDate = moodDate;
    }

    public Object getMoodTime() {
        return moodTime;
    }

    public void setMoodTime(Object moodTime) {
        this.moodTime = moodTime;
    }

    public String getMoodFeedback() {
        return moodFeedback;
    }

    public void setMoodFeedback(String moodFeedback) {
        this.moodFeedback = moodFeedback;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}