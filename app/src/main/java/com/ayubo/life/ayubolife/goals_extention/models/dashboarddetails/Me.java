package com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Me implements Parcelable
{

    @SerializedName("steps")
    @Expose
    private List<String> steps = null;
    @SerializedName("dates")
    @Expose
    private List<String> dates = null;
    public final static Parcelable.Creator<Me> CREATOR = new Creator<Me>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Me createFromParcel(Parcel in) {
            return new Me(in);
        }

        public Me[] newArray(int size) {
            return (new Me[size]);
        }

    }
            ;

    protected Me(Parcel in) {
        in.readList(this.steps, (java.lang.String.class.getClassLoader()));
        in.readList(this.dates, (java.lang.String.class.getClassLoader()));
    }

    public Me() {
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
