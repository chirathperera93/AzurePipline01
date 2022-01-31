package com.ayubo.life.ayubolife.pojo.timeline.commentadd_response;



        import android.os.Parcel;
        import android.os.Parcelable;
        import android.os.Parcelable.Creator;

        import com.ayubo.life.ayubolife.pojo.timeline.CommentDatum;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;



public class MainResponse implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private CommentDatum data;
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
        this.data = ((CommentDatum) in.readValue((CommentDatum.class.getClassLoader())));
    }

    public MainResponse() {
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public CommentDatum getData() {
        return data;
    }

    public void setData(CommentDatum data) {
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
