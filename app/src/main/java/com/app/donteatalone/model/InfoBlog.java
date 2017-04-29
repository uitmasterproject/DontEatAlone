package com.app.donteatalone.model;

import java.util.ArrayList;

/**
 * Created by ChomChom on 4/21/2017.
 */

public class InfoBlog {
    @SuppressWarnings("date")
    private String date;
    @SuppressWarnings("infoStatus")
    private String infoStatus;
    @SuppressWarnings("feeling")
    private String feeling;
    @SuppressWarnings("image")
    private ArrayList<String> image;

    public InfoBlog(){}

    public InfoBlog(String date, String infoStatus, String feeling, ArrayList<String> image) {
        this.date = date;
        this.infoStatus = infoStatus;
        this.feeling = feeling;
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfoStatus() {
        return infoStatus;
    }

    public void setInfoStatus(String infoStatus) {
        this.infoStatus = infoStatus;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }
}
