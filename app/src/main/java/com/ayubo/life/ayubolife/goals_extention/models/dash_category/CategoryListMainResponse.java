package com.ayubo.life.ayubolife.goals_extention.models.dash_category;



        import java.util.List;
        import android.os.Parcel;
        import android.os.Parcelable;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class CategoryListMainResponse implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    public final static Parcelable.Creator<CategoryListMainResponse> CREATOR = new Creator<CategoryListMainResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CategoryListMainResponse createFromParcel(Parcel in) {
            return new CategoryListMainResponse(in);
        }

        public CategoryListMainResponse[] newArray(int size) {
            return (new CategoryListMainResponse[size]);
        }

    }
            ;

    protected CategoryListMainResponse(Parcel in) {
        this.result = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.data, (Datum.class.getClassLoader()));
    }

    public CategoryListMainResponse() {
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