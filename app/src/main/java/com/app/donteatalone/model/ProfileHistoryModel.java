package com.app.donteatalone.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Le Hoang Han on 6/28/2017
 */

public class ProfileHistoryModel {
    @SerializedName("AccordantPhone")
    private String accordantPhone;

    @SerializedName("AccordantFullName")
    private String accordantFullName;

    @SerializedName("TimeSend")
    private String timeSend;

    @SerializedName("Timer")
    private String timer;

    @SerializedName("Date")
    private String date;

    @SerializedName("Place")
    private String place;

    @SerializedName("MyRate")
    private boolean myRate;

    @SerializedName("AccordantRate")
    private boolean accordantRate;

    @SerializedName("MyAppraise")
    private String myAppraise;

    @SerializedName("AccordantAppraise")
    private String accordantAppraise;
    /*private boolean heart;*/

    public ProfileHistoryModel() {
        super();
    }

    public ProfileHistoryModel(String accordantPhone, String accordantFullName, String timeSend, String timer,
                               String date, String place, boolean myRate, boolean accordantRate, String myAppraise,
                               String accordantAppraise) {
        this.accordantPhone = accordantPhone;
        this.accordantFullName = accordantFullName;
        this.timeSend = timeSend;
        this.timer = timer;
        this.date = date;
        this.place = place;
        this.myRate = myRate;
        this.accordantRate = accordantRate;
        this.myAppraise = myAppraise;
        this.accordantAppraise = accordantAppraise;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccordantFullName() {
        return accordantFullName;
    }

    public void setAccordantFullName(String accordantFullName) {
        this.accordantFullName = accordantFullName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAccordantPhone() {
        return accordantPhone;
    }

    public void setAccordantPhone(String accordantPhone) {
        this.accordantPhone = accordantPhone;
    }

    public String getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(String timeSend) {
        this.timeSend = timeSend;
    }

    public boolean getMyRate() {
        return myRate;
    }

    public void setMyRate(boolean myRate) {
        this.myRate = myRate;
    }

    public boolean getAccordantRate() {
        return accordantRate;
    }

    public void setAccordantRate(boolean accordantRate) {
        this.accordantRate = accordantRate;
    }

    public String getMyAppraise() {
        return myAppraise;
    }

    public void setMyAppraise(String myAppraise) {
        this.myAppraise = myAppraise;
    }

    public String getAccordantAppraise() {
        return accordantAppraise;
    }

    public void setAccordantAppraise(String accordantAppraise) {
        this.accordantAppraise = accordantAppraise;
    }
}
