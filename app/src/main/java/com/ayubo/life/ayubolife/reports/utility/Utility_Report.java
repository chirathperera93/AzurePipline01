package com.ayubo.life.ayubolife.reports.utility;

import android.os.Environment;

import com.ayubo.life.ayubolife.reports.model.AllRecordsMainResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Utility_Report {

    public static boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
    public static String makeFirstLetterCapital(String str)
    {

        // Create a char array of given String
        char ch[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {

            // If first character of a word is found
            if (i == 0 && ch[i] != ' ' ||
                    ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char)(ch[i] - 'a' + 'A');
                }
            }

            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')

                // Convert into Lower-Case
                ch[i] = (char)(ch[i] + 'a' - 'A');
        }

        // Convert the char array to equivalent String
        String st = new String(ch);
        return st;
    }

    public static String[] getSortedDateHeadersLis2(String[] newArray){
        String[] sortednewArray =null;
        Arrays.sort(newArray, new Comparator<String>() {
            private SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
            @Override
            public int compare(String o1, String o2) {
                int result = -1;
                try {
                    result = sdf.parse(o2).compareTo(sdf.parse(o1));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                return result;
            }
        });

        for (String date : newArray) {
            System.out.println(date);
        }
        sortednewArray=newArray;

        return newArray;
    }

    public static String[] getSortedDateHeadersLis(String[] newArray){
        String[] sortednewArray =null;
        Arrays.sort(newArray, new Comparator<String>() {
            private SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
            @Override
            public int compare(String o1, String o2) {
                int result = -1;
                try {
                    result = sdf.parse(o2).compareTo(sdf.parse(o1));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                return result;
            }
        });

        for (String date : newArray) {
            System.out.println(date);
        }
        sortednewArray=newArray;

        return newArray;
    }
    public static String[] sortingONDates(List<String> dateHeadersLis){

        String[] newArray = new String[dateHeadersLis.size()];
        String[] sortedNewArray = new String[dateHeadersLis.size()];


        newArray= getReformateddateHeadersLis(dateHeadersLis);

        sortedNewArray= getSortedDateHeadersLis(newArray);

        String[] newArraySame= getReformateddateHeadersListSame(sortedNewArray);

        return newArraySame;

    }

    public static String[] getReformateddateHeadersListSame(String[] sortedNewAbbrray){

        String[] newArray = new String[sortedNewAbbrray.length];
        for(int cnt=0;cnt<sortedNewAbbrray.length;cnt++)
        { String reportDate=null;
            Date date1=null;
            try {

                reportDate=sortedNewAbbrray[cnt];

                String[] parts = reportDate.split("-");
                String dat = parts[0]; // 004
                String mon = parts[1];
                String year = parts[2];
                reportDate=year+"-"+mon+"-"+dat;

            } catch (Exception e) {
                e.printStackTrace();
            }
            newArray[cnt] = reportDate;
        }

        return newArray;
    }


    public static String[] getReformateddateHeadersLis(List<String> dateHeadersLis){

        String[] newArray = new String[dateHeadersLis.size()];
        for(int cnt=0;cnt<dateHeadersLis.size();cnt++)
        { String reportDate=null;
            Date date1=null;
            try {

                reportDate=dateHeadersLis.get(cnt);

                String[] parts = reportDate.split("-");
                String year = parts[0]; // 004
                String mon = parts[1];
                String dat = parts[2];
                reportDate=dat+"-"+mon+"-"+year;

            } catch (Exception e) {
                e.printStackTrace();
            }
            newArray[cnt] = reportDate;
        }

        return newArray;
    }

    public static List<String> RemovingDuplicateDates_WithObjects(List<Object> reportsList) {
        //=======Removing Duplicate Report Dates.. ========================================
        List<String> unOrderedUnique_dateHeadersList = null;
        unOrderedUnique_dateHeadersList = new ArrayList<>();

        for (int j = 0; j < reportsList.size(); j++) {
            Object obj = reportsList.get(j);
            if (obj instanceof AllRecordsMainResponse.AllRecordsReport) {
                AllRecordsMainResponse.AllRecordsReport objReport = (AllRecordsMainResponse.AllRecordsReport) obj;

                String strDate = objReport.getReportDate();

                if (!unOrderedUnique_dateHeadersList.contains(strDate)) {
                    unOrderedUnique_dateHeadersList.add(strDate);
                }
            }
        }

        return unOrderedUnique_dateHeadersList;
    }
    public static List<String> RemovingDuplicateDates(List<AllRecordsMainResponse.AllRecordsReport> reportsList) {
        //=======Removing Duplicate Report Dates.. ========================================
        List<String> unOrderedUnique_dateHeadersList = null;
        unOrderedUnique_dateHeadersList = new ArrayList<>();

        for (int j = 0; j < reportsList.size(); j++) {
            AllRecordsMainResponse.AllRecordsReport objReport = reportsList.get(j);
            String strDate = objReport.getReportDate();

            if (!unOrderedUnique_dateHeadersList.contains(strDate)) {
                unOrderedUnique_dateHeadersList.add(strDate);
            }
        }

        return unOrderedUnique_dateHeadersList;
    }

    public static List<Object> getreportDataList(String firstRepotID, List<AllRecordsMainResponse.AllRecordsReport> reportsList){
        List<Object> dataList=null;
        dataList=new ArrayList<>();

        List<Object> wantToRemoveThisList=null;
        wantToRemoveThisList=new ArrayList<>();

        dataList.add("Header");

        //=======Adding Un Assign Reports ========================================
        for (int c=0;c<reportsList.size();c++)
        {
            AllRecordsMainResponse.AllRecordsReport objReport= reportsList.get(c);
            if((objReport.getAssign_user_id()==null)){
                dataList.add(objReport);
                wantToRemoveThisList.add(objReport);
            }
        }
        //=======Ending Un Assign Reports ========================================


        //Removing un wanted objects..........
        for (int j=0;j<wantToRemoveThisList.size();j++){
            Object obj=wantToRemoveThisList.get(j);
            reportsList.remove(obj);
        }
        //Ended Removing un wanted objects..........



        //=======Sorting Based On Report Dates.. ========================================
        String[] newArray =  sortingONDates(RemovingDuplicateDates(reportsList));


        for (int j=0;j<newArray.length;j++) {
            String strDate= newArray[j];
            dataList.add(strDate);
            for (int c=0;c<reportsList.size();c++) {

                if(strDate.equals(reportsList.get(c).getReportDate())){
                    if(reportsList.get(c).getAssign_user_id()!=null){
                        dataList.add(reportsList.get(c));
                    }
                }
            }
        }
        //======= Ended  Sorting Based OnDatess.... Reports ========================================


        return dataList;
    }


    public static List<Object> getreportDataList2(String firstRepotID, List<Object> reportsList){
        List<Object> dataList=null;
        dataList=new ArrayList<>();

        List<Object> wantToRemoveThisList=null;
        wantToRemoveThisList=new ArrayList<>();

        dataList.add("Header");

//        if(reportsList.size()>0){
//            AllRecordsMainResponse.AllRecordsReport firstObj = reportsList.get(0);
//            firstRepotID=  firstObj.getId();
//        }


        //=======Adding new Title========================================
        boolean isFirst=true;
        for (int j=0;j<reportsList.size();j++)
        {
            Object obj= reportsList.get(j);

            if(obj instanceof AllRecordsMainResponse.AllRecordsReport ){
            AllRecordsMainResponse.AllRecordsReport objReport= (AllRecordsMainResponse.AllRecordsReport) obj;
            if(objReport.getAssign_user_id()==null){
                if(isFirst){
                    dataList.add("New");
                    isFirst=false;
                }
            }

            if((objReport.getRead()==0)){
                dataList.add(objReport);
                wantToRemoveThisList.add(objReport);
            }
            }
        }
        //=======Ended Adding Un Assign Reports ========================================

        //Removing un wanted objects..........
        for (int j=0;j<wantToRemoveThisList.size();j++){
            Object obj=wantToRemoveThisList.get(j);
            reportsList.remove(obj);
        }
        //Ended Removing un wanted objects..........



        //=======Sorting Based On Report Dates.. ========================================
        String[] newArray =  sortingONDates(RemovingDuplicateDates_WithObjects(reportsList));


        for (int j=0;j<newArray.length;j++) {

            String strDate= newArray[j];
            dataList.add(strDate);

            for (int c=0;c<reportsList.size();c++) {

                Object obj= reportsList.get(j);
                if(obj instanceof AllRecordsMainResponse.AllRecordsReport){
                    AllRecordsMainResponse.AllRecordsReport objReport= (AllRecordsMainResponse.AllRecordsReport) obj;
                    if(strDate.equals(objReport.getReportDate())){
                        if(objReport.getAssign_user_id()!=null){
                            dataList.add(reportsList.get(c));
                        }

                    }
                }

            }

        }
        //======= Ended  Sorting Based OnDatess.... Reports ========================================


        return dataList;
    }





}
