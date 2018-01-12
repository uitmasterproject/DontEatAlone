package com.app.donteatalone.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ChomChom on 17-Dec-17
 */

public class ReservationDetail implements Parcelable{
    private String table;
    private String time;
    private String session;



    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public ReservationDetail() {
    }

    public ReservationDetail(String table, String time, String session) {

        this.table = table;
        this.time = time;
        this.session = session;
    }

    public ReservationDetail(ReservationDetail reservationDetail) {

        this.table = reservationDetail.table;
        this.time = reservationDetail.time;
        this.session = reservationDetail.session;
    }

    protected ReservationDetail(Parcel in) {
        table = in.readString();
        time = in.readString();
        session = in.readString();
    }

    public static final Creator<ReservationDetail> CREATOR = new Creator<ReservationDetail>() {
        @Override
        public ReservationDetail createFromParcel(Parcel in) {
            return new ReservationDetail(in);
        }

        @Override
        public ReservationDetail[] newArray(int size) {
            return new ReservationDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(table);
        dest.writeString(time);
        dest.writeString(session);
    }
}
