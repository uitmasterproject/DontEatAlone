package com.example.app.connectserver.model;

/**
 * Created by ChomChom on 4/1/2017.
 */

public class Hobby  {
    private String headerHobby;
    private String itemHobby;
    private boolean type;

    public Hobby(String headerHobby, String itemHobby, boolean type) {
        this.headerHobby = headerHobby;
        this.itemHobby = itemHobby;
        this.type = type;
    }

    public String getHeaderHobby() {
        return headerHobby;
    }

    public void setHeaderHobby(String headerHobby) {
        this.headerHobby = headerHobby;
    }

    public String getItemHobby() {
        return itemHobby;
    }

    public void setItemHobby(String itemHobby) {
        this.itemHobby = itemHobby;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
