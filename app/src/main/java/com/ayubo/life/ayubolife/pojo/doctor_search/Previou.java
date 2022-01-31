package com.ayubo.life.ayubolife.pojo.doctor_search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by appdev on 4/26/2018.
 */


public class Previou implements Parcelable
{

    @SerializedName("ends")
    @Expose
    private String ends;
    @SerializedName("starts")
    @Expose
    private String starts;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("patientID")
    @Expose
    private String patientID;
    @SerializedName("patient")
    @Expose
    private String patient;
    @SerializedName("service_type")
    @Expose
    private String serviceType;
    @SerializedName("consultant")
    @Expose
    private String consultant;
    @SerializedName("Account")
    @Expose
    private String account;
    @SerializedName("phone_mobile")
    @Expose
    private String phoneMobile;
    @SerializedName("channel_number")
    @Expose
    private String channelNumber;
    @SerializedName("uhid")
    @Expose
    private String uhid;
    @SerializedName("channel_ref")
    @Expose
    private String channelRef;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("patientOnline")
    @Expose
    private String patientOnline;
    @SerializedName("cancel")
    @Expose
    private String cancel;
    @SerializedName("speciality")
    @Expose
    private String speciality;
    @SerializedName("examination")
    @Expose
    private String examination;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("docname")
    @Expose
    private String docname;
    @SerializedName("specialization_c")
    @Expose
    private String specializationC;
    public final static Parcelable.Creator<Previou> CREATOR = new Creator<Previou>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Previou createFromParcel(Parcel in) {
            return new Previou(in);
        }

        public Previou[] newArray(int size) {
            return (new Previou[size]);
        }

    }
            ;

    protected Previou(Parcel in) {
        this.ends = ((String) in.readValue((String.class.getClassLoader())));
        this.starts = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.patientID = ((String) in.readValue((String.class.getClassLoader())));
        this.patient = ((String) in.readValue((String.class.getClassLoader())));
        this.serviceType = ((String) in.readValue((String.class.getClassLoader())));
        this.consultant = ((String) in.readValue((String.class.getClassLoader())));
        this.account = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneMobile = ((String) in.readValue((String.class.getClassLoader())));
        this.channelNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.uhid = ((String) in.readValue((String.class.getClassLoader())));
        this.channelRef = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.dob = ((String) in.readValue((String.class.getClassLoader())));
        this.location = ((String) in.readValue((String.class.getClassLoader())));
        this.paymentStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.patientOnline = ((String) in.readValue((String.class.getClassLoader())));
        this.cancel = ((String) in.readValue((String.class.getClassLoader())));
        this.speciality = ((String) in.readValue((String.class.getClassLoader())));
        this.examination = ((String) in.readValue((String.class.getClassLoader())));
        this.link = ((String) in.readValue((String.class.getClassLoader())));
        this.docname = ((String) in.readValue((String.class.getClassLoader())));
        this.specializationC = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Previou() {
    }

    /**
     *
     * @param examination
     * @param location
     * @param link
     * @param cancel
     * @param type
     * @param docname
     * @param id
     * @param patient
     * @param channelNumber
     * @param specializationC
     * @param gender
     * @param uhid
     * @param channelRef
     * @param ends
     * @param status
     * @param speciality
     * @param consultant
     * @param paymentStatus
     * @param serviceType
     * @param patientID
     * @param phoneMobile
     * @param starts
     * @param dob
     * @param patientOnline
     * @param account
     */
    public Previou(String ends, String starts, String type, String status, String patientID, String patient, String serviceType, String consultant, String account, String phoneMobile, String channelNumber, String uhid, String channelRef, String gender, String dob, String location, String paymentStatus, String id, String patientOnline, String cancel, String speciality, String examination, String link, String docname, String specializationC) {
        super();
        this.ends = ends;
        this.starts = starts;
        this.type = type;
        this.status = status;
        this.patientID = patientID;
        this.patient = patient;
        this.serviceType = serviceType;
        this.consultant = consultant;
        this.account = account;
        this.phoneMobile = phoneMobile;
        this.channelNumber = channelNumber;
        this.uhid = uhid;
        this.channelRef = channelRef;
        this.gender = gender;
        this.dob = dob;
        this.location = location;
        this.paymentStatus = paymentStatus;
        this.id = id;
        this.patientOnline = patientOnline;
        this.cancel = cancel;
        this.speciality = speciality;
        this.examination = examination;
        this.link = link;
        this.docname = docname;
        this.specializationC = specializationC;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getConsultant() {
        return consultant;
    }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public String getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getUhid() {
        return uhid;
    }

    public void setUhid(String uhid) {
        this.uhid = uhid;
    }

    public String getChannelRef() {
        return channelRef;
    }

    public void setChannelRef(String channelRef) {
        this.channelRef = channelRef;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientOnline() {
        return patientOnline;
    }

    public void setPatientOnline(String patientOnline) {
        this.patientOnline = patientOnline;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getSpecializationC() {
        return specializationC;
    }

    public void setSpecializationC(String specializationC) {
        this.specializationC = specializationC;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ends);
        dest.writeValue(starts);
        dest.writeValue(type);
        dest.writeValue(status);
        dest.writeValue(patientID);
        dest.writeValue(patient);
        dest.writeValue(serviceType);
        dest.writeValue(consultant);
        dest.writeValue(account);
        dest.writeValue(phoneMobile);
        dest.writeValue(channelNumber);
        dest.writeValue(uhid);
        dest.writeValue(channelRef);
        dest.writeValue(gender);
        dest.writeValue(dob);
        dest.writeValue(location);
        dest.writeValue(paymentStatus);
        dest.writeValue(id);
        dest.writeValue(patientOnline);
        dest.writeValue(cancel);
        dest.writeValue(speciality);
        dest.writeValue(examination);
        dest.writeValue(link);
        dest.writeValue(docname);
        dest.writeValue(specializationC);
    }

    public int describeContents() {
        return 0;
    }

}