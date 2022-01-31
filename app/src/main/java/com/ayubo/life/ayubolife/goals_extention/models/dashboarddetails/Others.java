package com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Others implements Parcelable
{

    @SerializedName("steps")
    @Expose
    private List<String> steps = null;
    @SerializedName("dates")
    @Expose
    private List<String> dates = null;
    public final static Parcelable.Creator<Others> CREATOR = new Creator<Others>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Others createFromParcel(Parcel in) {
            return new Others(in);
        }

        public Others[] newArray(int size) {
            return (new Others[size]);
        }

    }
            ;

    protected Others(Parcel in) {
        in.readList(this.steps, (java.lang.String.class.getClassLoader()));
        in.readList(this.dates, (java.lang.String.class.getClassLoader()));
    }

    public Others() {
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(steps);
        dest.writeList(dates);
    }

    public int describeContents() {
        return 0;
    }

}
