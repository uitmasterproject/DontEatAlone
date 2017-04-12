package com.app.donteatalone.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ChomChom on 4/10/2017.
 */

public class Status {
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
