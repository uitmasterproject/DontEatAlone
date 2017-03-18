package com.example.app.connectserver.model;

/**
 * Created by ChomChom on 3/13/2017.
 */

public class UserName {
    private String Phone;
    private String Fullname;
    private String Password;
    private String Birthday;
    private String Gender;

    public UserName(){}

    public UserName(String phone, String fullname, String password, String birthday, String gender) {
        Phone = phone;
        Fullname = fullname;
        Password = password;
        Birthday = birthday;
        Gender = gender;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
