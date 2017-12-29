package com.app.donteatalone.model;

/**
 * Created by ChomChom on 27-Dec-17
 */

public class SocketInfoUser {
    private String phone;
    private String gender;
    private String ageMin;
    private String ageMax;
    private String address;
    private String latlng;
    private String targetFood;
    private String targetCharacter;
    private String targetStyle;

    public SocketInfoUser(String phone, String gender, String ageMin, String ageMax, String address, String latlng, String targetFood, String targetCharacter, String targetStyle) {
        this.phone = phone;
        this.gender = gender;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.address = address;
        this.latlng = latlng;
        this.targetFood = targetFood;
        this.targetCharacter = targetCharacter;
        this.targetStyle = targetStyle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(String ageMin) {
        this.ageMin = ageMin;
    }

    public String getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(String ageMax) {
        this.ageMax = ageMax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getTargetFood() {
        return targetFood;
    }

    public void setTargetFood(String targetFood) {
        this.targetFood = targetFood;
    }

    public String getTargetCharacter() {
        return targetCharacter;
    }

    public void setTargetCharacter(String targetCharacter) {
        this.targetCharacter = targetCharacter;
    }

    public String getTargetStyle() {
        return targetStyle;
    }

    public void setTargetStyle(String targetStyle) {
        this.targetStyle = targetStyle;
    }
}
