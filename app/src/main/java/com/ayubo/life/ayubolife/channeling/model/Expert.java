package com.ayubo.life.ayubolife.channeling.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sabri on 3/30/2018. model for experts
 */

public class Expert implements Serializable {

    private String id;
    private String title;
    private String name;
    private String speciality;
    private String picture;

    private String next;
    private List<Location> locations;
    private Channel channel;
    private Video video;
    private Review review;
    private Ask ask;
    private Profile profile;
    private Booking booking;

    public static class Booking implements Serializable {
        private String action;
        private String meta;

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

        public Booking(String action, String meta) {
            this.action = action;
            this.meta = meta;
        }
    }

    public static class Profile implements Serializable {
        private String action;
        private String meta;

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

        public Profile(String action, String meta) {
            this.action = action;
            this.meta = meta;
        }
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Ask getAsk() {
        return ask;
    }

    public void setAsk(Ask ask) {
        this.ask = ask;
    }

    public static class Review implements Serializable {
        private Integer enable;
        private String meta;

        public Integer getEnable() {
            return enable;
        }

        public void setEnable(Integer enable) {
            this.enable = enable;
        }

        public String getMeta() {
            return meta;
        }

        public void setMeta(String meta) {
            this.meta = meta;
        }

        public Review(Integer enable, String meta) {
            this.enable = enable;
            this.meta = meta;
        }
    }

    public static class Ask implements Serializable {
        private Integer enable;
        private String meta;

        public Integer getEnable() {
            return enable;
        }

        public void setEnable(Integer enable) {
            this.enable = enable;
        }

        public String getMeta() {
            return meta;
        }

        public void setMeta(String meta) {
            this.meta = meta;
        }

        public Ask(Integer enable, String meta) {
            this.enable = enable;
            this.meta = meta;
        }
    }

    public static class Video implements Serializable {
        private Integer enable;
        private String meta;

        public Integer getEnable() {
            return enable;
        }

        public void setEnable(Integer enable) {
            this.enable = enable;
        }

        public String getMeta() {
            return meta;
        }

        public void setMeta(String meta) {
            this.meta = meta;
        }

        public Video(Integer enable, String meta) {
            this.enable = enable;
            this.meta = meta;
        }
    }

    public static class Channel implements Serializable {
        private Integer enable;
        private String meta;

        public Integer getEnable() {
            return enable;
        }

        public void setEnable(Integer enable) {
            this.enable = enable;
        }

        public String getMeta() {
            return meta;
        }

        public void setMeta(String meta) {
            this.meta = meta;
        }

        public Channel(Integer enable, String meta) {
            this.enable = enable;
            this.meta = meta;
        }
    }

    public static class Location implements Serializable {
        private String id;
        private String name;
        private String fee;
        private String fee_value;
        private String next_available;


        public String getFee_value() {
            return fee_value;
        }

        public void setFee_value(String fee_value) {
            this.fee_value = fee_value;
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

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getNext_available() {
            return next_available;
        }

        public void setNext_available(String next_available) {
            this.next_available = next_available;
        }

        public Location(String id, String name, String fee, String fee_value, String next_available) {
            this.id = id;
            this.name = name;
            this.fee = fee;
            this.fee_value = fee_value;
            this.next_available = next_available;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Expert(String id, String title, String name, String speciality, String picture, String next, List<Location> locations, Channel channel, Video video, Review review, Ask ask, Profile profile, Booking booking) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.speciality = speciality;
        this.picture = picture;
        this.next = next;
        this.locations = locations;
        this.channel = channel;
        this.video = video;
        this.review = review;
        this.ask = ask;
        this.profile = profile;
        this.booking = booking;
    }
}
