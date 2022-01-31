package com.ayubo.life.ayubolife.goals_extention;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class Utility {


    public static List<Date> getDatesBetweenUsingJava7(Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            System.out.println("===================result============= "+result.getDate());
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);

        }


        // Setup current days..........................................
        Calendar today = Calendar.getInstance();
        Date currentDate = today.getTime();
        System.out.println("===================result============= "+currentDate.getDate());
        datesInRange.add(currentDate);

        Calendar today2 = Calendar.getInstance();
        today2.add(Calendar.DATE, +1);
        Date date2 = today2.getTime();
        datesInRange.add(date2);

        Calendar today3 = Calendar.getInstance();
        today3.add(Calendar.DATE, +2);
        Date date3 = today3.getTime();
        datesInRange.add(date3);


        return datesInRange;
    }

    public static HashMap<String, String> makeDaysMapForGivenDays(Date startDate, Date endDate) {

        HashMap<String, String> hmap= new HashMap<String, String>();
        Calendar todayCalendar = new GregorianCalendar();
        todayCalendar.setTime(startDate);
        SimpleDateFormat formatWith_yyyy_MM_dd;
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);
        formatWith_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
        // Setup previous days..........................................
        while (todayCalendar.before(endCalendar)) {
            Date result = todayCalendar.getTime();
            System.out.println("===================result============= "+result.getTime());
            String strDate = formatWith_yyyy_MM_dd.format(result.getTime());
            hmap.put(strDate, "0");
            todayCalendar.add(Calendar.DATE, 1);
        }

        // Setup current days..........................................
        Calendar today = Calendar.getInstance();
        Date currentDate = today.getTime();
        String strToday = formatWith_yyyy_MM_dd.format(currentDate.getTime());
        hmap.put(strToday, "0");

        return hmap;
    }

    public static List<Date> getDatesBetweenTwoDays(Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();

        Calendar todayCalendar = new GregorianCalendar();
        todayCalendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        // Setup previous days..........................................
        while (todayCalendar.before(endCalendar)) {
            Date result = todayCalendar.getTime();
            System.out.println("===================result============= "+result.getDate());
            datesInRange.add(result);
            todayCalendar.add(Calendar.DATE, 1);
        }

        // Setup current days..........................................
        Calendar today = Calendar.getInstance();
        Date currentDate = today.getTime();
        System.out.println("===================result============= "+currentDate.getDate());
        datesInRange.add(currentDate);


        return datesInRange;
    }
}
