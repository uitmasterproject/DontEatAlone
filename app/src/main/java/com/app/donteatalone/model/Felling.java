package com.app.donteatalone.model;

/**
 * Created by ChomChom on 4/18/2017
 */

public class Felling {
    private int resourceIcon;
    private String felling;

    public Felling(int resourceIcon, String felling) {
        this.resourceIcon = resourceIcon;
        this.felling = felling;
    }

    public int getResourceIcon() {
        return resourceIcon;
    }

    public void setResourceIcon(int resourceIcon) {
        this.resourceIcon = resourceIcon;
    }

    public String getFelling() {
        return felling;
    }

    public void setFelling(String felling) {
        this.felling = felling;
    }
}
