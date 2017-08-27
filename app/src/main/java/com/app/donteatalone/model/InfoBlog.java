package com.app.donteatalone.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ChomChom on 4/21/2017.
 */

public class InfoBlog implements Parcelable {
    @SuppressWarnings("title")
    private String title;
    @SuppressWarnings("date")
    private String date;
    @SuppressWarnings("infoStatus")
    private String infoStatus;
    @SuppressWarnings("feeling")
    private String feeling;
    @SuppressWarnings("image")
    private ArrayList<String> image;
    @SuppressWarnings("limit")
    private String limit;

    public InfoBlog() {
    }

    public InfoBlog(String title, String date, String infoStatus, String feeling, ArrayList<String> image, String limit) {
        this.title = title;
        this.date = date;
        this.infoStatus = infoStatus;
        this.feeling = feeling;
        this.image = image;
        this.limit = limit;
    }

    protected InfoBlog(Parcel in) {
        title = in.readString();
        date = in.readString();
        infoStatus = in.readString();
        feeling = in.readString();
        image = in.createStringArrayList();
        limit = in.readString();
    }

    public static final Creator<InfoBlog> CREATOR = new Creator<InfoBlog>() {
        @Override
        public InfoBlog createFromParcel(Parcel in) {
            return new InfoBlog(in);
        }

        @Override
        public InfoBlog[] newArray(int size) {
            return new InfoBlog[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfoStatus() {
        return infoStatus;
    }

    public void setInfoStatus(String infoStatus) {
        this.infoStatus = infoStatus;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(infoStatus);
        dest.writeString(feeling);
        dest.writeStringList(image);
        dest.writeString(limit);
    }
}
