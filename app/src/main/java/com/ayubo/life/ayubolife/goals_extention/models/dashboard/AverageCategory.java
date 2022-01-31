package com.ayubo.life.ayubolife.goals_extention.models.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AverageCategory implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("steps")
    @Expose
    private String steps;

    @SerializedName("image_url")
    @Expose
    private String image_url;


    public final static Parcelable.Creator<AverageCategory> CREATOR = new Creator<AverageCategory>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AverageCategory createFromParcel(Parcel in) {
            return new AverageCategory(in);
        }

        public AverageCategory[] newArray(int size) {
            return (new AverageCategory[size]);
        }

    }
            ;

    protected AverageCategory(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.steps = ((String) in.readValue((String.class.getClassLoader())));
        this.image_url = ((String) in.readValue((String.class.getClassLoader())));

    }

    public AverageCategory() {
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

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(steps);
        dest.writeValue(image_url);
    }

    public int describeContents() {
        return 0;
    }

}

