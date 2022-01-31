package com.ayubo.life.ayubolife.goals_extention.models.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable
{

    @SerializedName("profile_picture_link")
    @Expose
    private String profilePictureLink;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("steps")
    @Expose
    private String steps;

    @SerializedName("average_category")
    @Expose
    private AverageCategory averageCategory;
    @SerializedName("goal")
    @Expose
    private Goal goal;
    public final static Parcelable.Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
            ;

    protected Data(Parcel in) {
        this.profilePictureLink = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.steps = ((String) in.readValue((String.class.getClassLoader())));
        this.averageCategory = ((AverageCategory) in.readValue((AverageCategory.class.getClassLoader())));
        this.goal = ((Goal) in.readValue((Goal.class.getClassLoader())));
    }

    public Data() {
    }

    public String getProfilePictureLink() {
        return profilePictureLink;
    }

    public void setProfilePictureLink(String profilePictureLink) {
        this.profilePictureLink = profilePictureLink;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AverageCategory getAverageCategory() {
        return averageCategory;
    }

    public void setAverageCategory(AverageCategory averageCategory) {
        this.averageCategory = averageCategory;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(profilePictureLink);
        dest.writeValue(name);
        dest.writeValue(steps);
        dest.writeValue(averageCategory);
        dest.writeValue(goal);
    }

    public int describeContents() {
        return 0;
    }

}