package com.app.donteatalone.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ChomChom on 22-Dec-17
 */

public class UserReservation implements Parcelable{
    private String phone;
    private String timeReservation;
    private ArrayList<RestaurantDetail> lisReservationDetail;

    public UserReservation() {
        this.lisReservationDetail=new ArrayList<>();
    }


    public UserReservation(String phone, String timeReservation, ArrayList<RestaurantDetail> lisReservationDetail) {
        this.phone = phone;
        this.timeReservation = timeReservation;
        this.lisReservationDetail = lisReservationDetail;
    }

    protected UserReservation(Parcel in) {
        phone = in.readString();
        timeReservation = in.readString();
        lisReservationDetail = in.createTypedArrayList(RestaurantDetail.CREATOR);
    }

    public static final Creator<UserReservation> CREATOR = new Creator<UserReservation>() {
        @Override
        public UserReservation createFromParcel(Parcel in) {
            return new UserReservation(in);
        }

        @Override
        public UserReservation[] newArray(int size) {
            return new UserReservation[size];
        }
    };

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTimeReservation() {
        return timeReservation;
    }

    public void setTimeReservation(String timeReservation) {
        this.timeReservation = timeReservation;
    }

    public ArrayList<RestaurantDetail> getLisReservationDetail() {
        return lisReservationDetail;
    }

    public void setLisReservationDetail(ArrayList<RestaurantDetail> lisReservationDetail) {
        this.lisReservationDetail = lisReservationDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phone);
        dest.writeString(timeReservation);
        dest.writeTypedList(lisReservationDetail);
    }
}
