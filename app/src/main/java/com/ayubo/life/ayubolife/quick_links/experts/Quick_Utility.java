package com.ayubo.life.ayubolife.quick_links.experts;

import com.ayubo.life.ayubolife.home_popup_menu.ChatObj;
import com.ayubo.life.ayubolife.home_popup_menu.DoctorObj;

import java.util.ArrayList;
import java.util.List;

public class Quick_Utility {



    public static ArrayList<Object> getSortedAskDataList(ArrayList<Object> reportsList) {
        ArrayList<Object> dataList = null;
        dataList = new ArrayList<>();
        ArrayList<Object> wantToRemoveThisList = null;
        wantToRemoveThisList = new ArrayList<>();


        for (int c=0;c<reportsList.size();c++) {
            Object obj = reportsList.get(c);
            if (obj instanceof String) {
                reportsList.remove(c);
            }
        }

        for (int j = 0; j < reportsList.size(); j++) {
            Object obj = reportsList.get(j);

            if (obj instanceof ChatObj) {
            ChatObj docObj = (ChatObj) obj;

            if (!wantToRemoveThisList.contains(docObj.getSpeciality())) {
                wantToRemoveThisList.add(docObj.getSpeciality());
                String specilization = docObj.getSpeciality();
                dataList.add(specilization);
                System.out.println("=specilization=======================" + specilization);
                for (int jj = 0; jj < reportsList.size(); jj++) {

                      ChatObj objn = (ChatObj) reportsList.get(jj);

                    if (objn.getSpeciality().equals(specilization)) {
                        dataList.add(reportsList.get(jj));
                    }
                }
            }
        }
        }
        return dataList;
    }

    public static ArrayList<Object> getRemovedStringsDataList(ArrayList<Object> reportsList){
        ArrayList<Object> dataList=null;

        for (int c=0;c<reportsList.size();c++) {
            Object obj = reportsList.get(c);
            if (obj instanceof String) {
                reportsList.remove(c);
            }
        }
        return dataList;
    }

    public static ArrayList<Object> getSortedDoctorDataList(ArrayList<Object> reportsList){
        ArrayList<Object> dataList=null;


        dataList=new ArrayList<>();
        ArrayList<Object> wantToRemoveThisList=null;
        wantToRemoveThisList=new ArrayList<>();

        for (int c=0;c<reportsList.size();c++) {
            Object obj = reportsList.get(c);
            if (obj instanceof String) {
                reportsList.remove(c);
            }
        }

        //Removing un wanted objects..........
        for (int j=0;j<reportsList.size();j++) {

            Object obj = reportsList.get(j);

            if (obj instanceof DoctorObj) {
            DoctorObj docObj = (DoctorObj) obj;
            if (!wantToRemoveThisList.contains(docObj.getSpec())) {
                wantToRemoveThisList.add(docObj.getSpec());
                String specilization = docObj.getSpec();
                dataList.add(specilization);
                System.out.println("=specilization=======================" + specilization);
                for (int jj = 0; jj < reportsList.size(); jj++) {

                    DoctorObj objn = (DoctorObj) reportsList.get(jj);

                    if (objn.getSpec().equals(specilization)) {
                        dataList.add(reportsList.get(jj));
                    }
                }
            }
        }

        }
        return dataList;
    }


}
