package com.ayubo.life.ayubolife.pojo.doctor_search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by appdev on 4/24/2018.
 */

public class Data implements Parcelable
{

    @SerializedName("experts")
    @Expose
    private List<Expert> experts = null;
    @SerializedName("appointments")
    @Expose
    private Appointments appointments;
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
        in.readList(this.experts, (Expert.class.getClassLoader()));
        this.appointments = ((Appointments) in.readValue((Appointments.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Data() {
    }

    /**
     *
     * @param experts
     * @param appointments
     */
    public Data(List<Expert> experts, Appointments appointments) {
        super();
        this.experts = experts;
        this.appointments = appointments;
    }

    public List<Expert> getExperts() {
        return experts;
    }

    public void setExperts(List<Expert> experts) {
        this.experts = experts;
    }

    public Appointments getAppointments() {
        return appointments;
    }

    public void setAppointments(Appointments appointments) {
        this.appointments = appointments;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(experts);
        dest.writeValue(appointments);
    }

    public int describeContents() {
        return 0;
    }

}