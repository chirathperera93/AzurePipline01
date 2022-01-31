package com.ayubo.life.ayubolife.pojo.timeline;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LikeMainResponse implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private Object data;
    public final static Parcelable.Creator<LikeMainResponse> CREATOR = new Creator<LikeMainResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LikeMainResponse createFromParcel(Parcel in) {
            return new LikeMainResponse(in);
        }

        public LikeMainResponse[] newArray(int size) {
            return (new LikeMainResponse[size]);
        }

    }
            ;

    protected LikeMainResponse(Parcel in) {
        this.result = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.data = ((Object) in.readValue((Object.class.getClassLoader())));
    }

    public LikeMainResponse() {
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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