package com.app.donteatalone.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDate;

/**
 * Created by ChomChom on 3/13/2017
 */

public class UserName extends Imei implements Parcelable {
    @SerializedName("phone")
    private String phone;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("password")
    private String password;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("gender")
    private String gender;
    @SerializedName("address")
    private String address;
    @SerializedName("latlngAdress")
    private String latlngAdress;
    @SerializedName("myCharacter")
    private String myCharacter;
    @SerializedName("myStyle")
    private String myStyle;
    @SerializedName("targetCharacter")
    private String targetCharacter;
    @SerializedName("targetStyle")
    private String targetStyle;
    @SerializedName("targetFood")
    private String targetFood;

    public UserName(Context context) {
        super(context);
    }

    public UserName(Context context, String phone, String fullName, String password, String avatar, String birthday, String gender, String address,
                     String latlngAdress, String myCharacter, String myStyle, String targetCharacter, String targetStyle,
                     String targetFood) {
        super(context);
        this.phone = phone;
        this.fullName = fullName;
        this.password = password;
        this.avatar = avatar;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.latlngAdress = latlngAdress;
        this.myCharacter = myCharacter;
        this.myStyle = myStyle;
        this.targetCharacter = targetCharacter;
        this.targetStyle = targetStyle;
        this.targetFood = targetFood;
    }

    public UserName(Context context, String phone, String imei, String fullName, String password, String gender, String avatar, String birthday,
                    String address, String latlngAdress, String myCharacter, String myStyle, String targetCharacter, String targetStyle,
                    String targetFood) {
        super(context);
        this.phone = phone;
        this.setImei(imei);
        this.fullName = fullName;
        this.password = password;
        this.avatar = avatar;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.latlngAdress = latlngAdress;
        this.myCharacter = myCharacter;
        this.myStyle = myStyle;
        this.targetCharacter = targetCharacter;
        this.targetStyle = targetStyle;
        this.targetFood = targetFood;
    }

    public UserName(Parcel in) {
        super(in);
        phone = in.readString();
        fullName = in.readString();
        password = in.readString();
        avatar = in.readString();
        birthday = in.readString();
        gender = in.readString();
        address = in.readString();
        latlngAdress = in.readString();
        myCharacter = in.readString();
        myStyle = in.readString();
        targetCharacter = in.readString();
        targetStyle = in.readString();
        targetFood = in.readString();
    }

    public static final Creator<UserName> CREATOR = new Creator<UserName>() {
        @Override
        public UserName createFromParcel(Parcel in) {
            return new UserName(in);
        }

        @Override
        public UserName[] newArray(int size) {
            return new UserName[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phone);
        dest.writeString(fullName);
        dest.writeString(password);
        dest.writeString(avatar);
        dest.writeString(birthday);
        dest.writeString(gender);
        dest.writeString(address);
        dest.writeString(latlngAdress);
        dest.writeString(myCharacter);
        dest.writeString(myStyle);
        dest.writeString(targetCharacter);
        dest.writeString(targetStyle);
        dest.writeString(targetFood);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getAge(){
        LocalDate date = new LocalDate();
        if(!TextUtils.isEmpty(birthday)){
            try {
                return date.getYear() - Integer.parseInt(birthday.split("/")[2]);
            }catch (Exception e){
                return 0;
            }

        }
        return 0;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getFormatGender(){
        if(gender.equals("Female")){
            return "F";
        }
        return "M";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatlngAdress() {
        return latlngAdress;
    }

    public void setLatlngAdress(String latlngAdress) {
        this.latlngAdress = latlngAdress;
    }

    public String getMyCharacter() {
        return myCharacter;
    }

    public void setMyCharacter(String myCharacter) {
        this.myCharacter = myCharacter;
    }

    public String getMyStyle() {
        return myStyle;
    }

    public void setMyStyle(String myStyle) {
        this.myStyle = myStyle;
    }

    public String getTargetCharacter() {
        return targetCharacter;
    }

    public void setTargetCharacter(String targetCharacter) {
        this.targetCharacter = targetCharacter;
    }

    public String getTargetStyle() {
        return targetStyle;
    }

    public void setTargetStyle(String targetStyle) {
        this.targetStyle = targetStyle;
    }

    public String getTargetFood() {
        return targetFood;
    }

    public void setTargetFood(String targetFood) {
        this.targetFood = targetFood;
    }
}
