package com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChartData implements Parcelable
{

    @SerializedName("me")
    @Expose
    private Me me;
    @SerializedName("others")
    @Expose
    private Others others;
    public final static Parcelable.Creator<ChartData> CREATOR = new Creator<ChartData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChartData createFromParcel(Parcel in) {
            return new ChartData(in);
        }

        public ChartData[] newArray(int size) {
            return (new ChartData[size]);
        }

    }
            ;

    protected ChartData(Parcel in) {
        this.me = ((Me) in.readValue((Me.class.getClassLoader())));
        this.others = ((Others) in.readValue((Others.class.getClassLoader())));
    }

    public ChartData() {
    }

    public Me getMe() {
        return me;
    }

    public void setMe(Me me) {
        this.me = me;
    }

    public Others getOthers() {
        return others;
    }

    public void setOthers(Others others) {
        this.others = others;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(me);
        dest.writeValue(others);
    }

    public int describeContents() {
        return 0;
    }

}
