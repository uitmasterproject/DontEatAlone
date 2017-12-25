package com.app.donteatalone.model;

import java.util.ArrayList;

/**
 * Created by ChomChom on 04-Oct-17
 */

public class Achievement {
    private int like;
    private int appointment;
    private int rate;
    private ArrayList<String> listUser;

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getAppointment() {
        return appointment;
    }

    public void setAppointment(int appointment) {
        this.appointment = appointment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public ArrayList<String> getListUser() {
        return listUser;
    }

    public void setListUser(ArrayList<String> listUser) {
        this.listUser = listUser;
    }
}
