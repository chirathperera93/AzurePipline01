package com.ayubo.life.ayubolife.goals_extention.models;

import android.os.Parcelable;


       import android.os.Parcel;
               import android.os.Parcelable;
               import android.os.Parcelable.Creator;
               import com.google.gson.annotations.Expose;
               import com.google.gson.annotations.SerializedName;

public class Data_StepHistory implements Parcelable
{

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("steps")
    @Expose
    private String steps;
    @SerializedName("mets")
    @Expose
    private String calories;
    @SerializedName("calories")
    @Expose

    private String mets;
    @SerializedName("distance")
    @Expose
    private String distance;
    public final static Parcelable.Creator<Data_StepHistory> CREATOR = new Creator<Data_StepHistory>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Data_StepHistory createFromParcel(Parcel in) {
            return new Data_StepHistory(in);
        }

        public Data_StepHistory[] newArray(int size) {
            return (new Data_StepHistory[size]);
        }

    }
            ;

    protected Data_StepHistory(Parcel in) {
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.steps = ((String) in.readValue((String.class.getClassLoader())));
        this.mets = ((String) in.readValue((String.class.getClassLoader())));
        this.distance = ((String) in.readValue((String.class.getClassLoader())));
        this.calories = ((String) in.readValue((String.class.getClassLoader())));

    }

    public Data_StepHistory() {
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getMets() {
        return mets;
    }

    public void setMets(String mets) {
        this.mets = mets;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(date);
        dest.writeValue(steps);
        dest.writeValue(mets);
        dest.writeValue(distance);
    }

    public int describeContents() {
        return 0;
    }

}