package com.app.donteatalone.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ChomChom on 30-May-17
 */

public class AccordantUser implements Parcelable {
    private String accordantUser;
    private String avatar;
    private String fullName;
    private String percent;
    private String gender;
    private int age;
    private String address;
    private String latlng;
    private String myCharacter;
    private String myStyle;
    private String targetFood;
    private Boolean control=true;

    public AccordantUser() {
    }

    public AccordantUser(String accordantUser, String avatar, String fullName, String percent, String gender, int age,
                         String address, String latlng, String myCharacter, String myStyle, String targetFood) {
        this.accordantUser = accordantUser;
        this.avatar = avatar;
        this.fullName = fullName;
        this.percent = percent;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.latlng = latlng;
        this.myCharacter = myCharacter;
        this.myStyle = myStyle;
        this.targetFood = targetFood;
    }

    public AccordantUser(AccordantUserUpdate accordantUserUpdate){
        this.accordantUser = accordantUserUpdate.getAccordantUser();
        this.avatar = accordantUserUpdate.getAvatar();
        this.fullName = accordantUserUpdate.getFullName();
        this.percent = accordantUserUpdate.getPercent();
        this.gender = accordantUserUpdate.getGender();
        this.age = accordantUserUpdate.getAge();
        this.address = accordantUserUpdate.getAddress();
        this.latlng = accordantUserUpdate.getLatlng();
        this.myCharacter = accordantUserUpdate.getMyCharacter();
        this.myStyle = accordantUserUpdate.getMyStyle();
        this.targetFood = accordantUserUpdate.getTargetFood();
    }

    protected AccordantUser(Parcel in) {
        accordantUser = in.readString();
        avatar = in.readString();
        fullName = in.readString();
        percent = in.readString();
        gender = in.readString();
        age = in.readInt();
        address = in.readString();
        latlng = in.readString();
        myCharacter = in.readString();
        myStyle = in.readString();
        targetFood = in.readString();
        byte tmpControl = in.readByte();
        control = tmpControl == 0 ? null : tmpControl == 1;
    }

    public static final Creator<AccordantUser> CREATOR = new Creator<AccordantUser>() {
        @Override
        public AccordantUser createFromParcel(Parcel in) {
            return new AccordantUser(in);
        }

        @Override
        public AccordantUser[] newArray(int size) {
            return new AccordantUser[size];
        }
    };

    public String getAccordantUser() {
        return accordantUser;
    }

    public void setAccordantUser(String accordantUser) {
        this.accordantUser = accordantUser;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
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

    public String getTargetFood() {
        return targetFood;
    }

    public void setTargetFood(String targetFood) {
        this.targetFood = targetFood;
    }

    public Boolean getControl() {
        return control;
    }

    public void setControl(Boolean control) {
        this.control = control;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accordantUser);
        dest.writeString(avatar);
        dest.writeString(fullName);
        dest.writeString(percent);
        dest.writeString(gender);
        dest.writeInt(age);
        dest.writeString(address);
        dest.writeString(latlng);
        dest.writeString(myCharacter);
        dest.writeString(myStyle);
        dest.writeString(targetFood);
        dest.writeByte((byte) (control == null ? 0 : control ? 1 : 2));
    }
}
