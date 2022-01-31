package com.ayubo.life.ayubolife.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by appdev on 11/9/2017.
 */

public class RoadLocationObj implements Parcelable {

    double lat;
    double longitude;
    String distance;
    String steps;
    String action;
    String meta;
    String flag_act;
    String flag_deact;
    String stepstonext;
    String nextcity;
    String city;
    String citymsg;
    String cityimg;
    String disableimg;
    String zooml;
    String wc;
    String bubble_txt;
    String bubble_link;
    String auto_hide;

    public RoadLocationObj(double lat, double longitude, String distance, String steps, String action, String meta, String flag_act, String flag_deact, String stepstonext, String nextcity, String city, String citymsg, String cityimg,String disableimg,String wc,String zooml,String bubble_txt,String bubble_link,String auto_hide) {

        this.lat = lat;
        this.longitude = longitude;
        this.distance = distance;
        this.steps = steps;
        this.action = action;
        this.meta = meta;
        this.flag_act = flag_act;
        this.flag_deact = flag_deact;
        this.stepstonext = stepstonext;
        this.nextcity = nextcity;
        this.city = city;
        this.citymsg = citymsg;
        this.cityimg = cityimg;
        this.disableimg = disableimg;
        this.wc = wc;
        this.zooml = zooml;
        this.bubble_txt = bubble_txt;
        this.bubble_link = bubble_link;
        this.auto_hide = auto_hide;

    }

    protected RoadLocationObj(Parcel in) {
        lat = in.readDouble();
        longitude = in.readDouble();
        distance = in.readString();
        steps = in.readString();
        action = in.readString();
        meta = in.readString();
        flag_act = in.readString();
        flag_deact = in.readString();
        stepstonext = in.readString();
        nextcity = in.readString();
        city = in.readString();
        citymsg = in.readString();
        cityimg = in.readString();
        disableimg = in.readString();
        wc = in.readString();
        zooml = in.readString();
        bubble_txt = in.readString();
        bubble_link = in.readString();
        auto_hide = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(longitude);
        dest.writeString(distance);
        dest.writeString(steps);
        dest.writeString(action);
        dest.writeString(meta);
        dest.writeString(flag_act);
        dest.writeString(flag_deact);
        dest.writeString(stepstonext);
        dest.writeString(nextcity);
        dest.writeString(city);
        dest.writeString(citymsg);
        dest.writeString(cityimg);
        dest.writeString(disableimg);
        dest.writeString(wc);
        dest.writeString(zooml);
        dest.writeString(bubble_txt);
        dest.writeString(bubble_link);
        dest.writeString(auto_hide);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RoadLocationObj> CREATOR = new Creator<RoadLocationObj>() {
        @Override
        public RoadLocationObj createFromParcel(Parcel in) {
            return new RoadLocationObj(in);
        }

        @Override
        public RoadLocationObj[] newArray(int size) {
            return new RoadLocationObj[size];
        }
    };

    public String getAuto_hide() {
        return auto_hide;
    }

    public void setAuto_hide(String auto_hide) {
        this.auto_hide = auto_hide;
    }

    public String getWc() {
        return wc;
    }

    public void setWc(String wc) {
        this.wc = wc;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getFlag_act() {
        return flag_act;
    }

    public void setFlag_act(String flag_act) {
        this.flag_act = flag_act;
    }

    public String getFlag_deact() {
        return flag_deact;
    }

    public void setFlag_deact(String flag_deact) {
        this.flag_deact = flag_deact;
    }

    public String getStepstonext() {
        return stepstonext;
    }

    public void setStepstonext(String stepstonext) {
        this.stepstonext = stepstonext;
    }

    public String getNextcity() {
        return nextcity;
    }

    public void setNextcity(String nextcity) {
        this.nextcity = nextcity;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCitymsg() {
        return citymsg;
    }

    public void setCitymsg(String citymsg) {
        this.citymsg = citymsg;
    }

    public String getCityimg() {
        return cityimg;
    }

    public void setCityimg(String cityimg) {
        this.cityimg = cityimg;
    }

    public String getDisableimg() {
        return disableimg;
    }

    public void setDisableimg(String disableimg) {
        this.disableimg = disableimg;
    }

    public String getZooml() {
        return zooml;
    }

    public void setZooml(String zooml) {
        this.zooml = zooml;
    }

    public String getBubble_txt() {
        return bubble_txt;
    }

    public void setBubble_txt(String bubble_txt) {
        this.bubble_txt = bubble_txt;
    }

    public String getBubble_link() {
        return bubble_link;
    }

    public void setBubble_link(String bubble_link) {
        this.bubble_link = bubble_link;
    }
}
