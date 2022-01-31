package com.ayubo.life.ayubolife.pojo.goals;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainResponse implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private List<com.ayubo.life.ayubolife.pojo.goals.Data> data = null;
    public final static Parcelable.Creator<MainResponse> CREATOR = new Creator<MainResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MainResponse createFromParcel(Parcel in) {
            return new MainResponse(in);
        }

        public MainResponse[] newArray(int size) {
            return (new MainResponse[size]);
        }

    }
            ;

    protected MainResponse(Parcel in) {
        this.result = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.data, (com.ayubo.life.ayubolife.pojo.goals.Data.class.getClassLoader()));
    }

    public MainResponse() {
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<com.ayubo.life.ayubolife.pojo.goals.Data> getData() {
        return data;
    }

    public void setData(List<com.ayubo.life.ayubolife.pojo.goals.Data> data) {
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

