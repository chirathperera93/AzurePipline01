package com.ayubo.life.ayubolife.insurances.Classes;

public class RequestBenefactor {

    private String bf_firstname;
    private String bf_lastname;
    private String bf_identification;
    private String bf_contactnumber;
    private String bf_status;
    private String bf_relationship;

    public RequestBenefactor(String bf_firstname, String bf_lastname, String bf_identification, String bf_contactnumber, String bf_status, String bf_relationship) {
        this.bf_firstname = bf_firstname;
        this.bf_lastname = bf_lastname;
        this.bf_identification = bf_identification;
        this.bf_contactnumber = bf_contactnumber;
        this.bf_status = bf_status;
        this.bf_relationship = bf_relationship;
    }

    public String getBf_firstname() {
        return bf_firstname;
    }

    public String getBf_lastname() {
        return bf_lastname;
    }

    public String getBf_identification() {
        return bf_identification;
    }

    public String getBf_contactnumber() {
        return bf_contactnumber;
    }

    public String getBf_status() {
        return bf_status;
    }

    public String getBf_relationship() {
        return bf_relationship;
    }
}
