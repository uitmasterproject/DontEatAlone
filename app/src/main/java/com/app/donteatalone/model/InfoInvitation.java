package com.app.donteatalone.model;

import android.app.Activity;
import android.content.Context;

import com.app.donteatalone.utils.MySharePreference;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by ChomChom on 28-Dec-17
 */

public class InfoInvitation {
    private String timeInvite;
    private String currentTime;
    private String resultInvitation;
    private AccordantUser ownInvitation;
    private AccordantUser participant;
    private Restaurant restaurantInfo;
    private ReservationDetail reservationDetail;

    public InfoInvitation(AccordantUser ownInvitation, AccordantUser participant, Restaurant restaurantInfo, ReservationDetail reservationDetail) {
        this.ownInvitation = ownInvitation;
        this.participant = participant;
        this.restaurantInfo = restaurantInfo;
        this.reservationDetail = reservationDetail;
    }

    public InfoInvitation(AccordantUser ownInvitation, AccordantUser participant) {
        this.ownInvitation = ownInvitation;
        this.participant = participant;
    }

    public InfoInvitation(Context context,InfoNotification infoNotification){
        this.ownInvitation = new MySharePreference((Activity)context).createInfoUser();
        this.timeInvite = infoNotification.getTimeInvite();
        this.currentTime = infoNotification.getCurrentTime();
        this.resultInvitation = infoNotification.getResultInvitation();
        this.participant = infoNotification.getParticipant();
        this.restaurantInfo = infoNotification.getRestaurantInfo();
        this.reservationDetail = infoNotification.getReservationDetail();
    }

    public AccordantUser getOwnInvitation() {
        return ownInvitation;
    }

    public void setOwnInvitation(AccordantUser ownInvitation) {
        this.ownInvitation = ownInvitation;
    }

    public AccordantUser getParticipant() {
        return participant;
    }

    public void setParticipant(AccordantUser participant) {
        this.participant = participant;
    }

    public Restaurant getRestaurantInfo() {
        return restaurantInfo;
    }

    public void setRestaurantInfo(Restaurant restaurantInfo) {
        this.restaurantInfo = restaurantInfo;
        this.restaurantInfo.setAddress(StringEscapeUtils.escapeJava(this.restaurantInfo.getAddress()));
        this.restaurantInfo.setName(StringEscapeUtils.escapeJava(this.restaurantInfo.getName()));
    }

    public ReservationDetail getReservationDetail() {
        return reservationDetail;
    }

    public void setReservationDetail(ReservationDetail reservationDetail) {
        this.reservationDetail = reservationDetail;
        this.reservationDetail.setSession(StringEscapeUtils.escapeJava(this.reservationDetail.getSession()));
    }

    public String getTimeInvite() {
        return timeInvite;
    }

    public void setTimeInvite(String timeInvite) {
        this.timeInvite = timeInvite;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getResultInvitation() {
        return resultInvitation;
    }

    public void setResultInvitation(String resultInvitation) {
        this.resultInvitation = resultInvitation;
    }
}
