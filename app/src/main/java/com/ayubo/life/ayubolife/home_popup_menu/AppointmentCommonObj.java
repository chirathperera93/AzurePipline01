package com.ayubo.life.ayubolife.home_popup_menu;

/**
 * Created by appdev on 10/24/2017.
 */

public class AppointmentCommonObj {

   String docname,ends,location,patient,specialization_c,cancel,link,id,status;

    public AppointmentCommonObj(String docname, String ends, String location, String patient, String specialization_c, String cancel, String link, String id, String status) {
        this.docname = docname;
        this.ends = ends;
        this.location = location;
        this.patient = patient;
        this.specialization_c = specialization_c;
        this.cancel = cancel;
        this.link = link;
        this.id = id;
        this.status = status;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getSpecialization_c() {
        return specialization_c;
    }

    public void setSpecialization_c(String specialization_c) {
        this.specialization_c = specialization_c;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
