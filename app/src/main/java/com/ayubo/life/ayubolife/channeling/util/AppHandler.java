package com.ayubo.life.ayubolife.channeling.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;

import com.ayubo.life.ayubolife.channeling.model.ChannelDoctor;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Sabri on 3/12/2018. App common methods
 */

public class AppHandler {

    private static final String SOAP_REQUEST_METHOD_KEY = "method";
    private static final String SOAP_REQUEST_INPUT_KEY = "input_type";
    private static final String SOAP_REQUEST_RESPONSE_KEY = "response_type";
    private static final String SOAP_REQUEST_DATA_KEY = "rest_data";
    private static final String SOAP_REQUEST_TYPE_VALUE = "JSON";



    public static HashMap<String, String> getSoapRequestParams(String methodName, String params) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(SOAP_REQUEST_METHOD_KEY, methodName);
        hashMap.put(SOAP_REQUEST_INPUT_KEY, SOAP_REQUEST_TYPE_VALUE);
        hashMap.put(SOAP_REQUEST_RESPONSE_KEY, SOAP_REQUEST_TYPE_VALUE);

        hashMap.put(SOAP_REQUEST_DATA_KEY, params);

        return hashMap;
    }


    public static String getMonth(String daysStr) {
        int mo = 0, da = 0;
        int days = getDays(daysStr);
        int month[] = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        String sMonth=null;
        for (int i = 0; i < month.length; i++) {
            if (days < month[i]) {
                mo = i + 1;
                da = days;
                break;
            } else {
                days = days - month[i];
            }
        }
        if(mo<10){
            sMonth="0"+mo;
        }else{
            sMonth=Integer.toString(mo);
        }

        // dd-MM-yyyy sMonth+" - "+
        String sDateVal=da+"/"+sMonth;


        return sDateVal;
    }
    public static int getDays(String daysStr) {
        int d = Integer.parseInt(daysStr);
        if (d > 500) {
            return (d - 500);
        } else {
            return d;
        }
    }
    public static String getBirthYearFromID(String nic){
        String yearStr=null;String monthWithDate=null;
        boolean isNIC=false;
        if(nic.length()==10){
            String lastCh=nic.substring(9, 10);
            isNIC=true;
            if(lastCh.toLowerCase().equals("v")){
                yearStr=nic.substring(0, 2);

                int lastTowOfYear=Integer.parseInt(yearStr);
                int bYear=0;
                if(lastTowOfYear<20){
                    bYear=2000 +lastTowOfYear ;
                }else{
                    bYear=1900 +lastTowOfYear ;
                }
                yearStr=Integer.toString(bYear);
                String days= nic.substring(2, 5);
                monthWithDate= getMonth(days);
            }else{
                isNIC=true;
            }
        }
        else if(nic.length()==12){
            isNIC=true;
            yearStr=nic.substring(0, 4);
            String days= nic.substring(4, 7);
            monthWithDate= getMonth(days);
        }

        if(isNIC){
            return monthWithDate+"/"+yearStr;
        }else{
            return null;
        }


    }

    public static String convertHourFormat(String time){

        System.out.println("==========convertHourFormat====0======"+time);
        String timeHour=null;
        String yearStr=time.substring(0, 2);
        boolean isEvening;
        if(Integer.parseInt(yearStr)<12){
            isEvening=false;
        }else{
            isEvening=true;
        }

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);

            timeHour=new SimpleDateFormat("K:mm").format(dateObj);

            if(isEvening){
                timeHour= timeHour+"pm";
            }else{
                timeHour= timeHour+"am";
            }

            System.out.println(dateObj);
            System.out.println();
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        System.out.println("==========convertHourFormat===1======="+timeHour);
        return timeHour;
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNotValidateNIC(String value) {
        if (!(value.length() == 10 || value.length() == 12)) {
            return true;
        } else if (value.length() == 10 && !(value.toUpperCase().toCharArray()[9] == 'V' || value.toUpperCase().toCharArray()[9] == 'X')) {
            return true;
        }
        return false;
    }

    public static boolean isNotValidatePhone(String value) {
        if (value.length() < 9) {
            return true;
        }
        else if (value.length() > 11) {
            return true;
        }
        return false;
    }

    public static boolean isNotValidateEmail(String value) {
        if ((TextUtils.isEmpty(value) || !Patterns.EMAIL_ADDRESS.matcher(value).matches())) {
            return true;
        }
        return false;
    }

    public static int getDeviceHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(activity.getBaseContext());
        return deviceScreenDimension.getDisplayHeight();
    }

    public static void writeOfflineData(Context context, Object object, String fileName) {

        try {
            if(fileName==null){
                fileName="noname";
            }

            File file = new File(context.getFilesDir(), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
            oos.close();

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static Object readOfflineData(Context context, String fileName) {

        File file = new File(context.getFilesDir(), fileName);
        Object obj = null;

        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return null;
                }
            }

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            obj = ois.readObject();
            ois.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }
}
