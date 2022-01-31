package com.ayubo.life.ayubolife.pojo.goalCategory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Parcelable
{

    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_image_url")
    @Expose
    private String categoryImageId;
    public final static Parcelable.Creator<Datum> CREATOR = new Creator<Datum>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Datum createFromParcel(Parcel in) {
            return new Datum(in);
        }

        public Datum[] newArray(int size) {
            return (new Datum[size]);
        }

    }
            ;

    protected Datum(Parcel in) {
        this.categoryId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryImageId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Datum() {
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImageId() {
        return categoryImageId;
    }

    public void setCategoryImageId(String categoryImageId) {
        this.categoryImageId = categoryImageId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(categoryId);
        dest.writeValue(categoryName);
        dest.writeValue(categoryImageId);
    }

    public int describeContents() {
        return 0;
    }

}
