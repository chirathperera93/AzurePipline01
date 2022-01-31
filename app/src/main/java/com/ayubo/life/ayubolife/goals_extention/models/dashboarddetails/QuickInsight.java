package com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuickInsight implements Parcelable
{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("experts")
    @Expose
    private List<Expert> experts = null;
    public final static Parcelable.Creator<QuickInsight> CREATOR = new Creator<QuickInsight>() {


        @SuppressWarnings({
                "unchecked"
        })
        public QuickInsight createFromParcel(Parcel in) {
            return new QuickInsight(in);
        }

        public QuickInsight[] newArray(int size) {
            return (new QuickInsight[size]);
        }

    }
            ;

    protected QuickInsight(Parcel in) {
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.experts, (Expert.class.getClassLoader()));
    }

    public QuickInsight() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Expert> getExperts() {
        return experts;
    }

    public void setExperts(List<Expert> experts) {
        this.experts = experts;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(text);
        dest.writeValue(title);
        dest.writeList(experts);
    }

    public int describeContents() {
        return 0;
    }

}
