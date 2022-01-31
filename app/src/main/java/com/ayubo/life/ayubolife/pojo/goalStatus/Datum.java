package com.ayubo.life.ayubolife.pojo.goalStatus;

import android.os.Parcelable;


import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Parcelable
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
    @SerializedName("is_achieved")
    @Expose
    private String isAchieved;
    public final static Parcelable.Creator<Datum> CREATOR = new Creator<Datum>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Datum createFromParcel(Parcel in) {
            return new Datum(in);
        }

        public Datum[] newArray(int size) {
            return (new Datum[size]);
        }

    }
            ;

    protected Datum(Parcel in) {
        this.userGoalId = ((String) in.readValue((String.class.getClassLoader())));
        this.goalName = ((String) in.readValue((String.class.getClassLoader())));
        this.tileImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.largeImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.goalPickedDate = ((String) in.readValue((String.class.getClassLoader())));
        this.isAchieved = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Datum() {
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
        dest.writeValue(isAchieved);
    }

    public int describeContents() {
        return 0;
    }

}