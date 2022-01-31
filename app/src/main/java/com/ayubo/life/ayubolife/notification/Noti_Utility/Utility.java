package com.ayubo.life.ayubolife.notification.Noti_Utility;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utility {

   public static String getHourAndMinutsFromTime(Date timetsmp){
        String timeVal=null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timetsmp);
        Integer hours = calendar.get(Calendar.HOUR_OF_DAY);
        Integer minutes = calendar.get(Calendar.MINUTE);
        String sMinits="";
        if(minutes<10){
            sMinits="0"+minutes.toString();
        }else{
            sMinits=minutes.toString();
        }

        Integer seconds = calendar.get(Calendar.SECOND);
        String postFix=null;
        if(hours >=12){
            postFix="pm";
        }else{
            postFix="am";
        }

        timeVal=hours.toString()+":"+sMinits+postFix;
        return  timeVal;
    }
    public static  boolean isToday(Long timetsmp){
        boolean isToday=false;

        Timestamp newTime=new Timestamp(timetsmp * 1000);
        Timestamp todayTime=new Timestamp(System.currentTimeMillis());

        Date date=new Date(newTime.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String sNewDate = sdf.format(date);

        Date dateToday=new Date(todayTime.getTime());
        String sTodayDate = sdf.format(dateToday);

        if(sNewDate.equals(sTodayDate)){
            isToday=true;
        }

        return isToday;
    }

   public static String getTimeFromTime(Long timetsmp){
        String sTimeValue=null;

        Timestamp ts1=new Timestamp(timetsmp * 1000);
        Date date1=new Date(ts1.getTime());

        if(isToday(timetsmp)){
            sTimeValue=getHourAndMinutsFromTime(date1);
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sTimeValue = sdf.format(date1);
        }

        return sTimeValue;
    }

}
