package com.ayubo.life.ayubolife.pojo.goals.deleteGoal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    public final static Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {


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
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public Data() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
    }

    public int describeContents() {
        return 0;
    }

}