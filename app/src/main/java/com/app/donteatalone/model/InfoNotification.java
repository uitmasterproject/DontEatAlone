package com.app.donteatalone.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ChomChom on 09-Jun-17.
 */

public class InfoNotification implements Parcelable {
    private String userSend;
    private String nameSend;
    private String timeSend;
    private String date;
    private String time;
    private String place;
    private String status;
    private String read;
    private String seen;

    protected InfoNotification(Parcel in) {
        userSend = in.readString();
        nameSend = in.readString();
        timeSend = in.readString();
        date = in.readString();
        time = in.readString();
        place = in.readString();
        status = in.readString();
        read = in.readString();
        seen = in.readString();
    }

    public static final Creator<InfoNotification> CREATOR = new Creator<InfoNotification>() {
        @Override
        public InfoNotification createFromParcel(Parcel in) {
            return new InfoNotification(in);
        }

        @Override
        public InfoNotification[] newArray(int size) {
            return new InfoNotification[size];
        }
    };

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public InfoNotification() {
    }

    public InfoNotification(String userSend, String nameSend, String timeSend, String date, String time, String place, String status, String read, String seen) {
        this.userSend = userSend;
        this.nameSend = nameSend;
        this.timeSend = timeSend;
        this.date = date;
        this.time = time;
        this.place = place;
        this.status = status;
        this.read = read;
        this.seen = seen;
    }

    public String getNameSend() {
        return nameSend;
    }

    public void setNameSend(String nameSend) {
        this.nameSend = nameSend;
    }

    public String getUserSend() {
        return userSend;
    }

    public void setUserSend(String userSend) {
        this.userSend = userSend;
    }

    public String getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(String timeSend) {
        this.timeSend = timeSend;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userSend);
        dest.writeString(nameSend);
        dest.writeString(timeSend);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(place);
        dest.writeString(status);
        dest.writeString(read);
        dest.writeString(seen);
    }
}
