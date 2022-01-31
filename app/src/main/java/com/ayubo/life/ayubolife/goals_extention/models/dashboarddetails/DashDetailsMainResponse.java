package com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashDetailsMainResponse implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private Data data;
    public final static Parcelable.Creator<DashDetailsMainResponse> CREATOR = new Creator<DashDetailsMainResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DashDetailsMainResponse createFromParcel(Parcel in) {
            return new DashDetailsMainResponse(in);
        }

        public DashDetailsMainResponse[] newArray(int size) {
            return (new DashDetailsMainResponse[size]);
        }

    }
            ;

    protected DashDetailsMainResponse(Parcel in) {
        this.result = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
    }

    public DashDetailsMainResponse() {
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(result);
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}





