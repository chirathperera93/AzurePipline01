package com.ayubo.life.ayubolife.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllRecordsData implements Serializable
{

    @SerializedName("members")
    @Expose
    private List<AllRecordsMainResponse.AllRecordsMember> members = null;
    @SerializedName("reports")
    @Expose
    private List<AllRecordsMainResponse.AllRecordsReport> reports = null;
    private final static long serialVersionUID = -391307255829662127L;

    public List<AllRecordsMainResponse.AllRecordsMember> getMembers() {
        return members;
    }

    public void setMembers(List<AllRecordsMainResponse.AllRecordsMember> members) {
        this.members = members;
    }

    public List<AllRecordsMainResponse.AllRecordsReport> getReports() {
        return reports;
    }

    public void setReports(List<AllRecordsMainResponse.AllRecordsReport> reports) {
        this.reports = reports;
    }

}
