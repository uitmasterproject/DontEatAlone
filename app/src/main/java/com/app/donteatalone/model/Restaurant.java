package com.app.donteatalone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ChomChom on 26-Jun-17
 */

public class Restaurant implements Parcelable{
    @SerializedName("idRestaurant")
    private int id;
    private String avatar;
    private String name;
    private String address;
    private String rate;
    private String latlng;
    private String openDay;
    private String price;

    public Restaurant() {
    }

    protected Restaurant(Parcel in) {
        id = in.readInt();
        avatar = in.readString();
        name = in.readString();
        address = in.readString();
        rate = in.readString();
        latlng = in.readString();
        openDay = in.readString();
        price = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public Restaurant(RestaurantDetail restaurantDetail){
        id = restaurantDetail.getId();
        avatar = restaurantDetail.getAvatar();
        name = restaurantDetail.getName();
        address = restaurantDetail.getAddress();
        rate = restaurantDetail.getRate();
        latlng = restaurantDetail.getLatlng();
        openDay = restaurantDetail.getOpenDay();
        price = restaurantDetail.getPrice();
    }

    public Restaurant(Restaurant restaurant){
        id = restaurant.getId();
        avatar = restaurant.getAvatar();
        name = restaurant.getName();
        address = restaurant.getAddress();
        rate = restaurant.getRate();
        latlng = restaurant.getLatlng();
        openDay = restaurant.getOpenDay();
        price = restaurant.getPrice();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getOpenDay() {
        return openDay;
    }

    public void setOpenDay(String openDay) {
        this.openDay = openDay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(avatar);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(rate);
        dest.writeString(latlng);
        dest.writeString(openDay);
        dest.writeString(price);
    }
}
