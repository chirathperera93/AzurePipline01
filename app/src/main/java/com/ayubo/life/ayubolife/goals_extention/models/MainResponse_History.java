package com.ayubo.life.ayubolife.goals_extention.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainResponse_History  implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private List<Data_StepHistory> data = null;
    public final static Parcelable.Creator<MainResponse_History> CREATOR = new Creator<MainResponse_History>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MainResponse_History createFromParcel(Parcel in) {
            return new MainResponse_History(in);
        }

        public MainResponse_History[] newArray(int size) {
            return (new MainResponse_History[size]);
        }

    }
            ;

    protected MainResponse_History(Parcel in) {
        this.result = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.data, (Data_StepHistory.class.getClassLoader()));
    }

    public MainResponse_History() {
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<Data_StepHistory> getData() {
        return data;
    }

    public void setData(List<Data_StepHistory> data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(result);
        dest.writeList(data);
    }

    public int describeContents() {
        return 0;
    }

}