package com.ayubo.life.ayubolife.map_challenges.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LeaderboardMainResponse implements Serializable
{

          @SerializedName("last_updated")
        @Expose
        private String lastUpdated;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("banner_image")
        @Expose
        private String bannerImage;
        @SerializedName("leaderboard")
        @Expose
        private List<NewLeaderboard> leaderboard = null;
        private final static long serialVersionUID = 3793773929974473230L;

        public String getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBannerImage() {
            return bannerImage;
        }

        public void setBannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
        }

        public List<NewLeaderboard> getLeaderboard() {
            return leaderboard;
        }

        public void setLeaderboard(List<NewLeaderboard> leaderboard) {
            this.leaderboard = leaderboard;
        }

    }

