package com.ayubo.life.ayubolife.pojo.timeline;



        import java.util.List;
        import android.os.Parcel;
        import android.os.Parcelable;
        import android.os.Parcelable.Creator;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Comment implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private List<CommentDatum> data = null;
    public final static Parcelable.Creator<Comment> CREATOR = new Creator<Comment>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        public Comment[] newArray(int size) {
            return (new Comment[size]);
        }

    }
            ;

    protected Comment(Parcel in) {
        this.result = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.data, (CommentDatum.class.getClassLoader()));
    }

    public Comment() {
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<CommentDatum> getData() {
        return data;
    }

    public void setData(List<CommentDatum> data) {
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

