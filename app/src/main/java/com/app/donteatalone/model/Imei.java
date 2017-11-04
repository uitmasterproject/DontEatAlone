package com.app.donteatalone.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ChomChom on 14-Oct-17
 */

public class Imei implements Parcelable{
    @SerializedName("imei")
    private String imei;

    public Imei() {
    }

    public Imei(Context context) {
        TelephonyManager telephonyManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        imei=telephonyManager.getDeviceId();
        if(TextUtils.isEmpty(imei)||imei.length()<=0){
            imei= Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }

    protected Imei(Parcel in) {
        imei = in.readString();
    }

    public static final Creator<Imei> CREATOR = new Creator<Imei>() {
        @Override
        public Imei createFromParcel(Parcel in) {
            return new Imei(in);
        }

        @Override
        public Imei[] newArray(int size) {
            return new Imei[size];
        }
    };

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imei);
    }
}
