package com.ayubo.life.ayubolife.pojo.timeline.likedUsers;


        import java.util.List;
        import android.os.Parcel;
        import android.os.Parcelable;
        import android.os.Parcelable.Creator;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class LikedUsersMainResponse implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    public final static Parcelable.Creator<LikedUsersMainResponse> CREATOR = new Creator<LikedUsersMainResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LikedUsersMainResponse createFromParcel(Parcel in) {
            return new LikedUsersMainResponse(in);
        }

        public LikedUsersMainResponse[] newArray(int size) {
            return (new LikedUsersMainResponse[size]);
        }

    }
            ;

    protected LikedUsersMainResponse(Parcel in) {
        this.result = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.data, (Datum.class.getClassLoader()));
    }

    public LikedUsersMainResponse() {
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
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