package com.app.donteatalone.model;

import android.app.Activity;

import com.app.donteatalone.utils.MySharePreference;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ChomChom on 20-Jun-17
 */

public class InfoProfileUpdate {
    private String phone;
    private String type;
    private String content;
    @SerializedName("uuid")
    private String uuid;

    public InfoProfileUpdate(String phone, String type, String content, String uuid) {
        this.phone = phone;
        this.type = type;
        this.content = content;
        this.uuid = uuid;
    }

    public InfoProfileUpdate(Activity activity) {
        this.uuid = new MySharePreference(activity).getUUIDLogin();
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getUuid() {
        return uuid;
    }
}
