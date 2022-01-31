package com.ayubo.life.ayubolife.pojo.reports;

import android.os.Parcel;

/**
 * Created by appdev on 5/10/2018.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Data implements Parcelable
    {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("icon")
        @Expose
        private String icon;
        @SerializedName("new")
        @Expose
        private Boolean _new;
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
            this.id = ((String) in.readValue((String.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.type = ((String) in.readValue((String.class.getClassLoader())));
            this.count = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.link = ((String) in.readValue((String.class.getClassLoader())));
            this.icon = ((String) in.readValue((String.class.getClassLoader())));
            this._new = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        }

        public Data(String id, String name, String type, Integer count, String link, String icon, Boolean _new) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.count = count;
            this.link = link;
            this.icon = icon;
            this._new = _new;
        }

        public Data() {
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Boolean getNew() {
            return _new;
        }

        public void setNew(Boolean _new) {
            this._new = _new;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(id);
            dest.writeValue(name);
            dest.writeValue(type);
            dest.writeValue(count);
            dest.writeValue(link);
            dest.writeValue(icon);
            dest.writeValue(_new);
        }

        public int describeContents() {
            return 0;
        }

    }