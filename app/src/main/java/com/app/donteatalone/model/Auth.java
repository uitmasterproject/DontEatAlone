package com.app.donteatalone.model;

/**
 * Created by ChomChom on 14-Oct-17
 */

public class Auth {
    private String phone;

    public Auth(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
