package com.app.donteatalone.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ChomChom on 26-Jun-17
 */

public class Restaurant {
    private int id;
    @SerializedName("title")
    private String name;
    private String address;
    private String latlng;
    private String openDay;

    public Restaurant(int id, String name, String address, String latlng, String openDay) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latlng = latlng;
        this.openDay = openDay;
    }

    public Restaurant() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getOpenDay() {
        return openDay;
    }

    public void setOpenDay(String openDay) {
        this.openDay = openDay;
    }
}
