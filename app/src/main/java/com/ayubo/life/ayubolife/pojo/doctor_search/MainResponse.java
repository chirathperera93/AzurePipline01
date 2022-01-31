package com.ayubo.life.ayubolife.pojo.doctor_search;

/**
 * Created by appdev on 4/24/2018.
 */

        import android.os.Parcel;
        import android.os.Parcelable;
        import android.os.Parcelable.Creator;
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

    /**
     * No args constructor for use in serialization
     *
     */
    public MainResponse() {
    }

    /**
     *
     * @param result
     * @param data
     */
    public MainResponse(Integer result, Data data) {
        super();
        this.result = result;
        this.data = data;
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









class Location implements Parcelable
{

    @SerializedName("0")
    @Expose
    private String _0;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("link")
    @Expose
    private String link;
    public final static Parcelable.Creator<Location> CREATOR = new Creator<Location>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return (new Location[size]);
        }

    }
            ;

    protected Location(Parcel in) {
        this._0 = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.link = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Location() {
    }

    /**
     *
     * @param id
     * @param link
     * @param name
     * @param _0
     */
    public Location(String _0, String id, String name, String link) {
        super();
        this._0 = _0;
        this.id = id;
        this.name = name;
        this.link = link;
    }

    public String get0() {
        return _0;
    }

    public void set0(String _0) {
        this._0 = _0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(_0);
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(link);
    }

    public int describeContents() {
        return 0;
    }

}



