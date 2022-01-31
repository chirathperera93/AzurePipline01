package com.ayubo.life.ayubolife.model;

/**
 * Created by appdev on 3/20/2017.
 */

public class SelectedDateObj {

    String consultant_id;
    String location;
    String selectedDate;
    String selectedMonth;

    public SelectedDateObj(String consultant_id, String location, String selectedDate, String selectedMonth) {
        this.consultant_id = consultant_id;
        this.location = location;
        this.selectedDate = selectedDate;
        this.selectedMonth = selectedMonth;
    }

    public String getConsultant_id() {
        return consultant_id;
    }

    public void setConsultant_id(String consultant_id) {
        this.consultant_id = consultant_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }
}
