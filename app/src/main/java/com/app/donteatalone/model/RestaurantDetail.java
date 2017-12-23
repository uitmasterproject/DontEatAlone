package com.app.donteatalone.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ChomChom on 17-Dec-17
 */

public class RestaurantDetail extends Restaurant implements Parcelable {
    public static final Creator<RestaurantDetail> CREATOR = new Creator<RestaurantDetail>() {
        @Override
        public RestaurantDetail createFromParcel(Parcel in) {
            return new RestaurantDetail(in);
        }

        @Override
        public RestaurantDetail[] newArray(int size) {
            return new RestaurantDetail[size];
        }
    };
    private String city;
    private String district;
    private ArrayList<String> realImage;
    private ArrayList<String> designImage;
    private ArrayList<String> service;
    private String phoneNumber;
    private String type;
    private ArrayList<String> listTables;
    private ArrayList<String> listSessions;
    private ArrayList<ReservationDetail> listReservations;

    public RestaurantDetail() {
        this.listReservations=new ArrayList<>();
    }

    public RestaurantDetail(ArrayList<String> realImage, ArrayList<String> designImage,
                            ArrayList<String> service, String phoneNumber, String type,
                            ArrayList<String> listTables, ArrayList<String> listSessions,
                            ArrayList<ReservationDetail> listReservations) {
        this.realImage = realImage;
        this.designImage = designImage;
        this.service = service;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.listTables = listTables;
        this.listSessions = listSessions;
        this.listReservations = listReservations;
    }

    public RestaurantDetail(Parcel in, String city, String district, ArrayList<String> realImage, ArrayList<String> designImage, ArrayList<String> service, String phoneNumber, String type, ArrayList<String> listTables, ArrayList<String> listSessions, ArrayList<ReservationDetail> listReservations) {
        super(in);
        this.city = city;
        this.district = district;
        this.realImage = realImage;
        this.designImage = designImage;
        this.service = service;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.listTables = listTables;
        this.listSessions = listSessions;
        this.listReservations = listReservations;
    }

    protected RestaurantDetail(Parcel in) {
        super(in);
        city = in.readString();
        district = in.readString();
        realImage = in.createStringArrayList();
        designImage = in.createStringArrayList();
        service = in.createStringArrayList();
        phoneNumber = in.readString();
        type = in.readString();
        listTables = in.createStringArrayList();
        listSessions = in.createStringArrayList();
        listReservations = in.createTypedArrayList(ReservationDetail.CREATOR);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public ArrayList<String> getRealImage() {
        return realImage;
    }

    public void setRealImage(ArrayList<String> realImage) {
        this.realImage = realImage;
    }

    public ArrayList<String> getDesignImage() {
        return designImage;
    }

    public void setDesignImage(ArrayList<String> designImage) {
        this.designImage = designImage;
    }

    public ArrayList<String> getService() {
        return service;
    }

    public void setService(ArrayList<String> service) {
        this.service = service;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getListTables() {
        return listTables;
    }

    public void setListTables(ArrayList<String> listTables) {
        this.listTables = listTables;
    }

    public ArrayList<String> getListSessions() {
        return listSessions;
    }

    public void setListSessions(ArrayList<String> listSessions) {
        this.listSessions = listSessions;
    }

    public ArrayList<ReservationDetail> getListReservations() {
        return listReservations;
    }

    public void setListReservations(ArrayList<ReservationDetail> listReservations) {
        this.listReservations = listReservations;
    }

    @Override
    public int describeContents() {
        super.describeContents();
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(city);
        dest.writeString(district);
        dest.writeStringList(realImage);
        dest.writeStringList(designImage);
        dest.writeStringList(service);
        dest.writeString(phoneNumber);
        dest.writeString(type);
        dest.writeStringList(listTables);
        dest.writeStringList(listSessions);
        dest.writeTypedList(listReservations);
    }
}

