package com.ayubo.life.ayubolife.goals_extention.models.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Goal implements Parcelable
{

    @SerializedName("user_goal_id")
    @Expose
    private String userGoalId;
    @SerializedName("goal_name")
    @Expose
    private String goalName;
    @SerializedName("tile_image_url")
    @Expose
    private String tileImageUrl;
    @SerializedName("large_image_url")
    @Expose
    private String largeImageUrl;
    @SerializedName("goal_picked_date")
    @Expose
    private String goalPickedDate;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("share_image")
    @Expose
    private String shareImage;
    @SerializedName("sponser_large_image_url")
    @Expose
    private String sponserLargeImageUrl;
    @SerializedName("is_achieved")
    @Expose
    private String isAchieved;
    public final static Parcelable.Creator<Goal> CREATOR = new Creator<Goal>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        public Goal[] newArray(int size) {
            return (new Goal[size]);
        }

    }
            ;

    protected Goal(Parcel in) {
        this.userGoalId = ((String) in.readValue((String.class.getClassLoader())));
        this.goalName = ((String) in.readValue((String.class.getClassLoader())));
        this.tileImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.largeImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.goalPickedDate = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.shareImage = ((String) in.readValue((String.class.getClassLoader())));
        this.sponserLargeImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.isAchieved = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Goal() {
    }

    public String getUserGoalId() {
        return userGoalId;
    }

    public void setUserGoalId(String userGoalId) {
        this.userGoalId = userGoalId;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getTileImageUrl() {
        return tileImageUrl;
    }

    public void setTileImageUrl(String tileImageUrl) {
        this.tileImageUrl = tileImageUrl;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public String getGoalPickedDate() {
        return goalPickedDate;
    }

    public void setGoalPickedDate(String goalPickedDate) {
        this.goalPickedDate = goalPickedDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public String getSponserLargeImageUrl() {
        return sponserLargeImageUrl;
    }

    public void setSponserLargeImageUrl(String sponserLargeImageUrl) {
        this.sponserLargeImageUrl = sponserLargeImageUrl;
    }

    public String getIsAchieved() {
        return isAchieved;
    }

    public void setIsAchieved(String isAchieved) {
        this.isAchieved = isAchieved;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userGoalId);
        dest.writeValue(goalName);
        dest.writeValue(tileImageUrl);
        dest.writeValue(largeImageUrl);
        dest.writeValue(goalPickedDate);
        dest.writeValue(type);
        dest.writeValue(shareImage);
        dest.writeValue(sponserLargeImageUrl);
        dest.writeValue(isAchieved);
    }

    public int describeContents() {
        return 0;
    }

}