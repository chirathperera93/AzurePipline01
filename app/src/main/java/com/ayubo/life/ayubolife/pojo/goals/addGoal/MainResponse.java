package com.ayubo.life.ayubolife.pojo.goals.addGoal;


import android.os.Parcel;
        import android.os.Parcelable;
        import android.os.Parcelable.Creator;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class MainResponse implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private Data data;
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
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
    }

    public MainResponse() {
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