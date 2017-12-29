package com.app.donteatalone.model;

/**
 * Created by ChomChom on 09-Jun-17
 */

public class InfoNotification{
    private AccordantUser participant;
    private String currentTime;
    private String timeInvite;
    private Restaurant restaurantInfo;
    private ReservationDetail reservationDetail;
    private String resultInvitation;
    private String read;
    private String seen;

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

    public InfoNotification(AccordantUser participant, String currentTime, String timeInvite,
                            Restaurant restaurantInfo, ReservationDetail reservationDetail,
                            String resultInvitation, String read, String seen) {
        this.participant = participant;
        this.currentTime = currentTime;
        this.timeInvite = timeInvite;
        this.restaurantInfo = restaurantInfo;
        this.reservationDetail = reservationDetail;
        this.resultInvitation = resultInvitation;
        this.read = read;
        this.seen = seen;
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
}
