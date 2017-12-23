package com.app.donteatalone.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ChomChom on 4/10/2017
 */

public class Status{
    @SerializedName("status")
    private String status;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("restaurantReservation")
    private RestaurantDetail restaurantReservation;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public RestaurantDetail getRestaurantReservation() {
        return restaurantReservation;
    }

    public void setRestaurantReservation(RestaurantDetail restaurantReservation) {
        this.restaurantReservation = restaurantReservation;
    }
}
