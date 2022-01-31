package com.ayubo.life.ayubolife.pojo.timeline.main;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Goal implements Parcelable
{

    @SerializedName("user_goal_id")
    @Expose
    private String userGoalId;


    @SerializedName("bg_image")
    @Expose
    private String bgImage;

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


    @SerializedName("share_image")
    @Expose
    private String ShareImageUrl;


    @SerializedName("sponser_large_image_url")
    @Expose
    private String SponserImageUrl;



    @SerializedName("is_achieved")
    @Expose
    private String isAchieved;

    @SerializedName("from_date")
    @Expose
    private String from_date;

    @SerializedName("to_date")
    @Expose
    private String to_date ;

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
        this.ShareImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.SponserImageUrl = ((String) in.readValue((String.class.getClassLoader())));

        this.largeImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.goalPickedDate = ((String) in.readValue((String.class.getClassLoader())));
        this.isAchieved = ((String) in.readValue((String.class.getClassLoader())));

        this.from_date = ((String) in.readValue((String.class.getClassLoader())));
        this.to_date = ((String) in.readValue((String.class.getClassLoader())));

    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public Goal() {
    }

    public String getShareImageUrl() {
        return ShareImageUrl;
    }

    public void setShareImageUrl(String shareImageUrl) {
        ShareImageUrl = shareImageUrl;
    }

    public String getSponserImageUrl() {
        return SponserImageUrl;
    }

    public void setSponserImageUrl(String sponserLargeShareImageUrl) {
        SponserImageUrl = sponserLargeShareImageUrl;
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

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeValue(userGoalId);
        dest.writeValue(goalName);
        dest.writeValue(tileImageUrl);
        dest.writeValue(largeImageUrl);

        dest.writeValue(ShareImageUrl);
        dest.writeValue(SponserImageUrl);

        dest.writeValue(goalPickedDate);
        dest.writeValue(isAchieved);

        dest.writeValue(from_date);
        dest.writeValue(to_date);


    }

    public int describeContents() {
        return 0;
    }

}