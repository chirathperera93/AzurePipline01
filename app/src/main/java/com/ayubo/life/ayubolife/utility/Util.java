package com.ayubo.life.ayubolife.utility;

import android.text.TextUtils;

import com.ayubo.life.ayubolife.activity.PrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class Util {

    /**
     * @return milliseconds since 1.1.1970 for today 0:00:00 local timezone
     */
    public static long getToday() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     * @return milliseconds since 1.1.1970 for tomorrow 0:00:01 local timezone
     */
    public static long getTimestampAsMinits(long oldTime) {

        long now = System.currentTimeMillis(); // See note below obj.getTimestamp()

        long waitTimeInMiliies = now - oldTime;
        // long waitTimeInMiliies = TimeUnit.MILLISECONDS.toMinutes(now - oldTime);
        long noofMinits=waitTimeInMiliies/60000;
        System.out.println("==========noofMinits======================="+noofMinits);
        return noofMinits;
    }

    public static long getTimestampAsMinits(String sMin) {
        Long oldTime=null;
        if((sMin==null) || (sMin.equals(""))){
            sMin="0";
        }else{ }
        oldTime=Long.parseLong(sMin);
        long now = System.currentTimeMillis(); // See note below obj.getTimestamp()

        long waitTimeInMiliies = now - oldTime;
       // long waitTimeInMiliies = TimeUnit.MILLISECONDS.toMinutes(now - oldTime);
        long noofMinits=waitTimeInMiliies/60000;
        System.out.println("==========noofMinits======================="+noofMinits);
        return noofMinits;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getTodayString(){
        return (getDate(getToday(),"yyyy-MM-dd"));
    }

    public static String getTommorowString(){
        return (getDate(getTomorrow(),"yyyy-MM-dd"));
    }
    public static long getTomorrow() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 1);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, 1);
        return c.getTimeInMillis();
    }


    public static boolean isANewDate(String date1Str, String date2Str){
        boolean isAnewDate = false;
        if(TextUtils.isEmpty(date1Str) || TextUtils.isEmpty(date2Str)){
            return isAnewDate;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(date1Str);
            date2 = sdf.parse(date2Str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date2.after(date1)) {
            isAnewDate = true;
        }

        return isAnewDate;
    }

    public static long getMillisecondsTillNextMidnight() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 23); //then set the other fields to 0
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long midNightMili = c.getTimeInMillis();
        return midNightMili;
    }
}
