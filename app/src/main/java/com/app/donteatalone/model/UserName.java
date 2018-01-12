package com.app.donteatalone.model;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import com.app.donteatalone.utils.MySharePreference;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * Created by ChomChom on 3/13/2017
 */

public class UserName implements Parcelable {
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
    @SerializedName("uuid")
    private String uuid;
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

    public UserName(Activity activity) {
        uuid = new MySharePreference(activity).getUUIDLogin();
    }

    public UserName(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public UserName(Activity activity, String phone, String fullName, String password, String avatar, String birthday,
                    String gender, String address, String latlngAdress, String myCharacter, String myStyle,
                    String targetCharacter, String targetStyle, String targetFood) {
        this.uuid = new MySharePreference(activity).getUUIDLogin();
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

    public UserName(Activity activity, String phone, String fullName, String avatar, String birthday, String gender, String address,
                    String latlngAdress, String myCharacter, String myStyle, String targetCharacter, String targetStyle,
                    String targetFood) {
        this.uuid = new MySharePreference(activity).getUUIDLogin();
        this.phone = phone;
        this.fullName = fullName;
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

    protected UserName(Parcel in) {
        uuid = in.readString();
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

    public static Creator<UserName> getCREATOR() {
        return CREATOR;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(this.birthday.trim().split("/")[2]);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFormatGender() {
        if (this.gender.equals("Nam")) {
            return "Nam";
        } else {
            return "Nữ";
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
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
}
