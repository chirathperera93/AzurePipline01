package com.ayubo.life.ayubolife.insurances.Classes;

import java.util.ArrayList;

public class RequestFileClaim {

    private String cl_title;
    private String cl_type;
    private ArrayList<String> cl_uploads;
    private String cl_currency;
    private String cl_amount;
    private String cl_status;
    private String cl_notes;

    public RequestFileClaim(String cl_title, String cl_type, ArrayList<String> cl_uploads, String cl_currency, String cl_amount, String cl_status, String cl_notes) {
        this.cl_title = cl_title;
        this.cl_type = cl_type;
        this.cl_uploads = cl_uploads;
        this.cl_currency = cl_currency;
        this.cl_amount = cl_amount;
        this.cl_status = cl_status;
        this.cl_notes = cl_notes;
    }

    public String getCl_title() {
        return cl_title;
    }

    public void setCl_title(String cl_title) {
        this.cl_title = cl_title;
    }

    public String getCl_type() {
        return cl_type;
    }

    public void setCl_type(String cl_type) {
        this.cl_type = cl_type;
    }

    public ArrayList<String> getCl_uploads() {
        return cl_uploads;
    }

    public void setCl_uploads(ArrayList<String> cl_uploads) {
        this.cl_uploads = cl_uploads;
    }

    public String getCl_currency() {
        return cl_currency;
    }

    public void setCl_currency(String cl_currency) {
        this.cl_currency = cl_currency;
    }

    public String getCl_amount() {
        return cl_amount;
    }

    public void setCl_amount(String cl_amount) {
        this.cl_amount = cl_amount;
    }

    public String getCl_status() {
        return cl_status;
    }

    public void setCl_status(String cl_status) {
        this.cl_status = cl_status;
    }

    public String getCl_notes() {
        return cl_notes;
    }

    public void setCl_notes(String cl_notes) {
        this.cl_notes = cl_notes;
    }
}
