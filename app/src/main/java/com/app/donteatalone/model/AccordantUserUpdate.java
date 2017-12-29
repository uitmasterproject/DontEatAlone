package com.app.donteatalone.model;

/**
 * Created by ChomChom on 27-Dec-17
 */

public class AccordantUserUpdate extends AccordantUser{
    private String listUserUpdate;

    public AccordantUserUpdate(String listUserUpdate) {
        this.listUserUpdate = listUserUpdate;
    }

    public AccordantUserUpdate(String accordantUser, String avatar, String fullName, String percent, String gender, int age, String address, String latlng, String myCharacter, String myStyle, String targetFood, String listUserUpdate) {
        super(accordantUser, avatar, fullName, percent, gender, age, address, latlng, myCharacter, myStyle, targetFood);
        this.listUserUpdate = listUserUpdate;
    }

    public String getListUserUpdate() {
        return listUserUpdate;
    }

    public void setListUserUpdate(String listUserUpdate) {
        this.listUserUpdate = listUserUpdate;
    }
}
