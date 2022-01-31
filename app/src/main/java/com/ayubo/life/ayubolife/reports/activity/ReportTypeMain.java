package com.ayubo.life.ayubolife.reports.activity;

import java.util.ArrayList;

public class ReportTypeMain {

    String name;
    ArrayList<ReportTypeItem> parameter;
    String id;
    ArrayList<String> uploads;

    public ReportTypeMain(String name, ArrayList<ReportTypeItem> reportTypeItemArrayList, String id, ArrayList<String> uploads) {
        this.name = name;
        this.parameter = reportTypeItemArrayList;
        this.id = id;
        this.uploads = uploads;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ReportTypeItem> getParameter() {
        return parameter;
    }

    public ArrayList<ReportTypeItem> getReportTypeItemArrayList() {
        return parameter;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getUploads() {
        return uploads;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParameter(ArrayList<ReportTypeItem> parameter) {
        this.parameter = parameter;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUploads(ArrayList<String> uploads) {
        this.uploads = uploads;
    }
}
