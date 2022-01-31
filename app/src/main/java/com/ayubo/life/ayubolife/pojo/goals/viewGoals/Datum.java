package com.ayubo.life.ayubolife.pojo.goals.viewGoals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("goal_name")
    @Expose
    private String goalName;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("image_url")
    @Expose
    private String imageId;


    @SerializedName("sponser_name")
    @Expose
    private String sponserName;


    @SerializedName("sponser_image_url")
    @Expose
    private String sponserImageUrl;

    @SerializedName("share_image")
    @Expose
    private String ShareImageUrl;


    @SerializedName("goal_category_id")
    @Expose
    private String goalCategoryId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getSponserName() {
        return sponserName;
    }

    public void setSponserName(String sponserName) {
        this.sponserName = sponserName;
    }

    public String getSponserImageUrl() {
        return sponserImageUrl;
    }

    public void setSponserImageUrl(String sponserImageUrl) {
        this.sponserImageUrl = sponserImageUrl;
    }

    public String getShareImageUrl() {
        return ShareImageUrl;
    }

    public void setShareImageUrl(String shareImageUrl) {
        ShareImageUrl = shareImageUrl;
    }

    public String getGoalCategoryId() {
        return goalCategoryId;
    }

    public void setGoalCategoryId(String goalCategoryId) {
        this.goalCategoryId = goalCategoryId;
    }

}