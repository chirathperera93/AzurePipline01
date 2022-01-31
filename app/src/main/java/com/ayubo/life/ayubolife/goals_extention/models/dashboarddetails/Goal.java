package com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Goal implements Parcelable
{

    @SerializedName("goal_name")
    @Expose
    private String goalName;
    @SerializedName("goal_image_link")
    @Expose
    private String goalImageLink;
    @SerializedName("success_amount")
    @Expose
    private String successAmount;
    @SerializedName("success_amount_precentage")
    @Expose
    private String successAmountPrecentage;
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
        this.goalName = ((String) in.readValue((String.class.getClassLoader())));
        this.goalImageLink = ((String) in.readValue((String.class.getClassLoader())));
        this.successAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.successAmountPrecentage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Goal() {
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getGoalImageLink() {
        return goalImageLink;
    }

    public void setGoalImageLink(String goalImageLink) {
        this.goalImageLink = goalImageLink;
    }

    public String getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(String successAmount) {
        this.successAmount = successAmount;
    }

    public String getSuccessAmountPrecentage() {
        return successAmountPrecentage;
    }

    public void setSuccessAmountPrecentage(String successAmountPrecentage) {
        this.successAmountPrecentage = successAmountPrecentage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(goalName);
        dest.writeValue(goalImageLink);
        dest.writeValue(successAmount);
        dest.writeValue(successAmountPrecentage);
    }

    public int describeContents() {
        return 0;
    }

}
