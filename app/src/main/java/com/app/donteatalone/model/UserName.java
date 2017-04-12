package com.app.donteatalone.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ChomChom on 3/13/2017.
 */

public class UserName implements Serializable {
    @SerializedName("Phone")
    private String Phone;
    private String Fullname;
    private String Password;
    private String Avatar;
    private String Birthday;
    private String Gender;
    private String Address;
    private String Hobby;
    private String Character;

    public UserName(){}

    public UserName(String phone, String fullname, String password, String avatar, String birthday, String gender, String address, String hobby, String character) {
        Phone = phone;
        Fullname = fullname;
        Password = password;
        Avatar=avatar;
        Birthday = birthday;
        Gender = gender;
        Address=address;
        Hobby=hobby;
        Character=character;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getHobby() {
        return Hobby;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public void setHobby(String hobby) {
        Hobby = hobby;
    }

    public String getCharacter() {
        return Character;
    }

    public void setCharacter(String character) {
        Character = character;
    }


}
