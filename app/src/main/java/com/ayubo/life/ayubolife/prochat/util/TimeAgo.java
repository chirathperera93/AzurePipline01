package com.ayubo.life.ayubolife.prochat.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeAgo {
    public static String covertTimeToText(Long timeStamp) {

        String convTime = null;

        String suffix = "ago";
        Date nowTime = new Date();

        long dateDiff = nowTime.getTime() - timeStamp;

        long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
        long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
        long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
        long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

        if (second < 60) {
            if (second > 1) {
                convTime = second + " seconds " + suffix;
            } else {
                convTime = second + " second " + suffix;
            }
        } else if (minute < 60) {
            if (minute > 1) {
                convTime = minute + " minutes " + suffix;
            } else {
                convTime = minute + " minute " + suffix;
            }
        } else if (hour < 24) {
            if (hour > 1) {
                convTime = hour + " hours " + suffix;
            } else {
                convTime = hour + " hour " + suffix;
            }
        } else if (day >= 7) {
            if (day > 30) {
                long months = (day / 30);
                if (months > 1) {
                    convTime = months + " months " + suffix;
                } else {
                    convTime = months + " month " + suffix;
                }
            } else if (day > 360) {
                long years = (day / 360);
                if (years > 1) {
                    convTime = years + " years " + suffix;
                } else {
                    convTime = years + " year " + suffix;
                }
            } else {
                long weeks = (day / 7);
                if (weeks > 1) {
                    convTime = weeks + " weeks " + suffix;
                } else {
                    convTime = weeks + " week " + suffix;
                }
            }
        } else if (day < 7) {
            if(day > 1) {
                convTime = day + " days " + suffix;
            }else{
                convTime = day + " day " + suffix;
            }
        }
        return convTime;
    }
}
