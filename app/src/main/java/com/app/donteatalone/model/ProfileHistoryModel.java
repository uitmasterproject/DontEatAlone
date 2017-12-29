package com.app.donteatalone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Le Hoang Han on 6/28/2017
 */

public class ProfileHistoryModel implements Parcelable {
    private AccordantUser participant;
    private String currentTime;
    private String timeInvite;
    private Restaurant restaurantInfo;
    private ReservationDetail reservationDetail;
    private String resultInvitation;
    @SerializedName("MyRate")
    private boolean myRate;
    @SerializedName("AccordantRate")
    private boolean accordantRate;
    @SerializedName("MyAppraise")
    private String myAppraise;
    @SerializedName("AccordantAppraise")
    private String accordantAppraise;

    public ProfileHistoryModel() {
    }

    public ProfileHistoryModel(AccordantUser participant, String currentTime, String timeInvite, Restaurant restaurantInfo,
                               ReservationDetail reservationDetail, String resultInvitation, boolean myRate, boolean accordantRate,
                               String myAppraise, String accordantAppraise) {
        this.participant = participant;
        this.currentTime = currentTime;
        this.timeInvite = timeInvite;
        this.restaurantInfo = restaurantInfo;
        this.reservationDetail = reservationDetail;
        this.resultInvitation = resultInvitation;
        this.myRate = myRate;
        this.accordantRate = accordantRate;
        this.myAppraise = myAppraise;
        this.accordantAppraise = accordantAppraise;
    }

    public AccordantUser getParticipant() {
        return participant;
    }

    public void setParticipant(AccordantUser participant) {
        this.participant = participant;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getTimeInvite() {
        return timeInvite;
    }

    public void setTimeInvite(String timeInvite) {
        this.timeInvite = timeInvite;
    }

    public Restaurant getRestaurantInfo() {
        return restaurantInfo;
    }

    public void setRestaurantInfo(Restaurant restaurantInfo) {
        this.restaurantInfo = restaurantInfo;
    }

    public ReservationDetail getReservationDetail() {
        return reservationDetail;
    }

    public void setReservationDetail(ReservationDetail reservationDetail) {
        this.reservationDetail = reservationDetail;
    }

    public String getResultInvitation() {
        return resultInvitation;
    }

    public void setResultInvitation(String resultInvitation) {
        this.resultInvitation = resultInvitation;
    }

    public boolean isMyRate() {
        return myRate;
    }

    public void setMyRate(boolean myRate) {
        this.myRate = myRate;
    }

    public boolean isAccordantRate() {
        return accordantRate;
    }

    public void setAccordantRate(boolean accordantRate) {
        this.accordantRate = accordantRate;
    }

    public String getMyAppraise() {
        return myAppraise;
    }

    public void setMyAppraise(String myAppraise) {
        this.myAppraise = myAppraise;
    }

    public String getAccordantAppraise() {
        return accordantAppraise;
    }

    public void setAccordantAppraise(String accordantAppraise) {
        this.accordantAppraise = accordantAppraise;
    }

    protected ProfileHistoryModel(Parcel in) {
        participant = in.readParcelable(AccordantUser.class.getClassLoader());
        currentTime = in.readString();
        timeInvite = in.readString();
        restaurantInfo = in.readParcelable(Restaurant.class.getClassLoader());
        reservationDetail = in.readParcelable(ReservationDetail.class.getClassLoader());
        resultInvitation = in.readString();
        myRate = in.readByte() != 0;
        accordantRate = in.readByte() != 0;
        myAppraise = in.readString();
        accordantAppraise = in.readString();
    }

    public static final Creator<ProfileHistoryModel> CREATOR = new Creator<ProfileHistoryModel>() {
        @Override
        public ProfileHistoryModel createFromParcel(Parcel in) {
            return new ProfileHistoryModel(in);
        }

        @Override
        public ProfileHistoryModel[] newArray(int size) {
            return new ProfileHistoryModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(participant, flags);
        dest.writeString(currentTime);
        dest.writeString(timeInvite);
        dest.writeParcelable(restaurantInfo, flags);
        dest.writeParcelable(reservationDetail, flags);
        dest.writeString(resultInvitation);
        dest.writeByte((byte) (myRate ? 1 : 0));
        dest.writeByte((byte) (accordantRate ? 1 : 0));
        dest.writeString(myAppraise);
        dest.writeString(accordantAppraise);
    }
    /*private boolean heart;*/


}
