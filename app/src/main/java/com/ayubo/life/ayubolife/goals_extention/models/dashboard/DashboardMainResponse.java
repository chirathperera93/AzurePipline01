package com.ayubo.life.ayubolife.goals_extention.models.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardMainResponse  implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private Data data;
    public final static Parcelable.Creator<DashboardMainResponse> CREATOR = new Creator<DashboardMainResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DashboardMainResponse createFromParcel(Parcel in) {
            return new DashboardMainResponse(in);
        }

        public DashboardMainResponse[] newArray(int size) {
            return (new DashboardMainResponse[size]);
        }

    }
            ;

    protected DashboardMainResponse(Parcel in) {
        this.result = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
    }

    public DashboardMainResponse() {
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
