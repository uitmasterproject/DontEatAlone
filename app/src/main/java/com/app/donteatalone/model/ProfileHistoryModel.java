package com.app.donteatalone.model;

import java.util.Timer;

/**
 * Created by Le Hoang Han on 6/28/2017.
 */

public class ProfileHistoryModel {
    private String time;
    private String date;
    private String fullName;
    private String place;
    /*private boolean heart;*/

    public ProfileHistoryModel() {
        super();
    }

    public ProfileHistoryModel(String time, String date, String fullName, String place) {
        this.time = time;
        this.date = date;
        this.fullName = fullName;
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
