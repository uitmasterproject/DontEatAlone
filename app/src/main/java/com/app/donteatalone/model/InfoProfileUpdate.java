package com.app.donteatalone.model;

/**
 * Created by ChomChom on 20-Jun-17.
 */

public class InfoProfileUpdate {
    private String phone;
    private String which;
    private String content;

    public InfoProfileUpdate(String phone, String which, String content) {
        this.phone = phone;
        this.which = which;
        this.content = content;
    }
}
