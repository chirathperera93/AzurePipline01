package com.ayubo.life.ayubolife.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;



public class AllRecordsMainResponse implements Serializable {

    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("data")
    @Expose
    private ReportsData data;


    private final static long serialVersionUID = 216683907202970101L;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public ReportsData getData() {
        return data;
    }

    public void setData(ReportsData data) {
        this.data = data;
    }

    public class ReportsData implements Serializable {

        @SerializedName("members")
        @Expose
        private List<AllRecordsMember> members = null;
        @SerializedName("reports")
        @Expose
        private List<AllRecordsReport> reports = null;
        private final static long serialVersionUID = 4100040497751128689L;

        public List<AllRecordsMember> getMembers() {
            return members;
        }

        public void setMembers(List<AllRecordsMember> members) {
            this.members = members;
        }

        public List<AllRecordsReport> getReports() {
            return reports;
        }

        public void setReports(List<AllRecordsReport> reports) {
            this.reports = reports;
        }

    }


    public static class AllRecordsMember implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("uhid")
        @Expose
        private String uhid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("relationship")
        @Expose
        private String relationship;
        @SerializedName("profile_picture_url")
        @Expose
        private String profilePictureUrl;
        @SerializedName("notification_count")
        @Expose
        private String notificationCount;

        public AllRecordsMember(String id, String uhid, String name, String relationship, String profilePictureUrl, String notificationCount) {
            this.id = id;
            this.uhid = uhid;
            this.name = name;
            this.relationship = relationship;
            this.profilePictureUrl = profilePictureUrl;
            this.notificationCount = notificationCount;
        }

        private final static long serialVersionUID = -3048782450343766021L;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUhid() {
            return uhid;
        }

        public void setUhid(String uhid) {
            this.uhid = uhid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public String getProfilePictureUrl() {
            return profilePictureUrl;
        }

        public void setProfilePictureUrl(String profilePictureUrl) {
            this.profilePictureUrl = profilePictureUrl;
        }

        public String getNotificationCount() {
            return notificationCount;
        }

        public void setNotificationCount(String notificationCount) {
            this.notificationCount = notificationCount;
        }

    }

    public class AllRecordsReport implements Serializable {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("hospital")
        @Expose
        private String hospital;
        @SerializedName("reportName")
        @Expose
        private String reportName;
        @SerializedName("illness")
        @Expose
        private Object illness;
        @SerializedName("doctor_name")
        @Expose
        private Object doctorName;
        @SerializedName("description")
        @Expose
        private Object description;
        @SerializedName("reportDate")
        @Expose
        private String reportDate;
        @SerializedName("reportType")
        @Expose
        private String reportType;
        @SerializedName("ocrnumber")
        @Expose
        private Object ocrnumber;
        @SerializedName("hos_uid")
        @Expose
        private Object hosUid;
        @SerializedName("table_id")
        @Expose
        private String tableId;
        @SerializedName("enc_id")
        @Expose
        private String encId;
        @SerializedName("full_name")
        @Expose
        private String fullName;
        @SerializedName("read")
        @Expose
        private Integer read;


        @SerializedName("icon")
        @Expose
        private String icon;

        @SerializedName("assigned_user_id")
        @Expose
        private String assign_user_id;


        @SerializedName("download_url")
        @Expose
        private String download_url;

        @SerializedName("testorder_id")
        @Expose
        private String testorder_id;




        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getReportName() {
            return reportName;
        }

        public void setReportName(String reportName) {
            this.reportName = reportName;
        }

        public Object getIllness() {
            return illness;
        }

        public void setIllness(Object illness) {
            this.illness = illness;
        }

        public Object getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(Object doctorName) {
            this.doctorName = doctorName;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getReportDate() {
            return reportDate;
        }

        public void setReportDate(String reportDate) {
            this.reportDate = reportDate;
        }

        public String getReportType() {
            return reportType;
        }

        public void setReportType(String reportType) {
            this.reportType = reportType;
        }

        public Object getOcrnumber() {
            return ocrnumber;
        }

        public void setOcrnumber(Object ocrnumber) {
            this.ocrnumber = ocrnumber;
        }

        public Object getHosUid() {
            return hosUid;
        }

        public void setHosUid(Object hosUid) {
            this.hosUid = hosUid;
        }

        public String getTableId() {
            return tableId;
        }

        public void setTableId(String tableId) {
            this.tableId = tableId;
        }

        public String getEncId() {
            return encId;
        }

        public void setEncId(String encId) {
            this.encId = encId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Integer getRead() {
            return read;
        }

        public void setRead(Integer read) {
            this.read = read;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getAssign_user_id() {
            return assign_user_id;
        }

        public void setAssign_user_id(String assign_user_id) {
            this.assign_user_id = assign_user_id;
        }

        public String getTestorder_id() {
            return testorder_id;
        }

        public void setTestorder_id(String testorder_id) {
            this.testorder_id = testorder_id;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }


    }

}