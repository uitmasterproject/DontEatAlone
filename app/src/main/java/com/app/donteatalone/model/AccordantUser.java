package com.app.donteatalone.model;

/**
 * Created by ChomChom on 30-May-17
 */

public class AccordantUser {
    private String accordantUser;
    private String avatar;
    private String fullName;
    private int percent;
    private String gender;
    private int age;
    private String address;
    private String latlng;
    private String character;
    private Boolean control=true;

    public AccordantUser(String accordantUser, String avatar, String fullName, int percent, String gender, int age, String address, String latlng, String character) {
        this.accordantUser = accordantUser;
        this.avatar = avatar;
        this.fullName = fullName;
        this.percent = percent;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.latlng=latlng;
        this.character = character;
    }

    public Boolean getControl() {
        return control;
    }

    public void setControl(Boolean control) {
        this.control = control;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getAccordantUser() {
        return accordantUser;
    }

    public void setAccordantUser(String accordantUser) {
        this.accordantUser = accordantUser;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
