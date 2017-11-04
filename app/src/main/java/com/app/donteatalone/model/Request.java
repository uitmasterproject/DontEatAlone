package com.app.donteatalone.model;

/**
 * Created by ChomChom on 12-Oct-17.
 */

public class Request {
    private String token;

    public Request(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
