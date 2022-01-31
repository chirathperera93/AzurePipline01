package com.ayubo.life.ayubolife.pojo.timeline;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pagination implements Parcelable
{

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("maxPostId")
    @Expose
    private String maxPostId;
    public final static Parcelable.Creator<Pagination> CREATOR = new Creator<Pagination>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Pagination createFromParcel(Parcel in) {
            return new Pagination(in);
        }

        public Pagination[] newArray(int size) {
            return (new Pagination[size]);
        }

    }
            ;

    protected Pagination(Parcel in) {
        this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.pages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.maxPostId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Pagination() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getMaxPostId() {
        return maxPostId;
    }

    public void setMaxPostId(String maxPostId) {
        this.maxPostId = maxPostId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(count);
        dest.writeValue(page);
        dest.writeValue(pages);
        dest.writeValue(maxPostId);
    }

    public int describeContents() {
        return 0;
    }

}
