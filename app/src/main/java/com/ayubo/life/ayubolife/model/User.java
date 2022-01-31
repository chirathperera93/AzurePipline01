package com.ayubo.life.ayubolife.model;

/**
 * Created by AppDev5 on 2/13/2016.
 */
public class User {

    String usernameid;
    String fname;
    String lname;
    String imagepath;
    String email;
    String gender;
    String birthday;
    String date;
    String datemodified;
    String password;
    String bodyage;

    public String getBodyage() {
        return bodyage;
    }

    public void setBodyage(String bodyage) {
        this.bodyage = bodyage;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public User(String usernameid, String fname, String lname, String imagepath, String email, String gender, String birthday, String date,
                String datemodified, String password,String bodyage) {
        this.usernameid = usernameid;
        this.fname = fname;
        this.lname = lname;
        this.imagepath = imagepath;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.date = date;
        this.datemodified = datemodified;
        this.password = password;
        this.bodyage=bodyage;
    }


    public String getUsernameid() {
        return usernameid;
    }

    public void setUsernameid(String usernameid) {
        this.usernameid = usernameid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDatemodified() {
        return datemodified;
    }

    public void setDatemodified(String datemodified) {
        this.datemodified = datemodified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
