package com.ayubo.life.ayubolife.pojo.doctor_search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by appdev on 4/26/2018.
 */

public class Appointments implements Parcelable
{

    @SerializedName("today")
    @Expose
    private List<Previou> today = null;
    @SerializedName("upcoming")
    @Expose
    private List<Previou> upcoming = null;
    @SerializedName("previous")
    @Expose
    private List<Previou> previous = null;
    public final static Parcelable.Creator<Appointments> CREATOR = new Creator<Appointments>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Appointments createFromParcel(Parcel in) {
            return new Appointments(in);
        }

        public Appointments[] newArray(int size) {
            return (new Appointments[size]);
        }

    }
            ;

    protected Appointments(Parcel in) {
        in.readList(this.today, (java.lang.Object.class.getClassLoader()));
        in.readList(this.upcoming, (java.lang.Object.class.getClassLoader()));
        in.readList(this.previous, (Previou.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Appointments() {
    }

    /**
     *
     * @param upcoming
     * @param previous
     * @param today
     */
    public Appointments(List<Previou> today, List<Previou> upcoming, List<Previou> previous) {
        super();
        this.today = today;
        this.upcoming = upcoming;
        this.previous = previous;
    }

    public List<Previou> getToday() {
        return today;
    }

    public void setToday(List<Previou> today) {
        this.today = today;
    }

    public List<Previou> getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(List<Previou> upcoming) {
        this.upcoming = upcoming;
    }

    public List<Previou> getPrevious() {
        return previous;
    }

    public void setPrevious(List<Previou> previous) {
        this.previous = previous;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(today);
        dest.writeList(upcoming);
        dest.writeList(previous);
    }

    public int describeContents() {
        return 0;
    }

}