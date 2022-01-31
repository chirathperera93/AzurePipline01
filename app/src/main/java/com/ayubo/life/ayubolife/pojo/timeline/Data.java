package com.ayubo.life.ayubolife.pojo.timeline;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data implements Parcelable
{

    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    public final static Parcelable.Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
            ;

    protected Data(Parcel in) {
        in.readList(this.posts, (Post.class.getClassLoader()));
        this.pagination = ((Pagination) in.readValue((Pagination.class.getClassLoader())));
    }

    public Data() {
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(posts);
        dest.writeValue(pagination);
    }

    public int describeContents() {
        return 0;
    }

}
