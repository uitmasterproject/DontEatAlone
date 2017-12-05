package com.app.donteatalone.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.donteatalone.model.UserName;


/**
 * Created by ChomChom on 10-Sep-17
 */

public class MySharePreference {
    public static final String PRE_PHONE = "PRE_PHONE";
    public static final String PRE_UUID = "PRE_UUID";
    public static final String PRE_FULL_NAME = "PRE_FULL_NAME";
    public static final String PRE_PASSWORD = "PRE_PASSWORD";
    public static final String PRE_GENDER = "PRE_GENDER";
    public static final String PRE_AVATAR = "PRE_AVATAR";
    public static final String PRE_BIRTHDAY = "PRE_BIRTHDAY";
    public static final String PRE_LAT_LNG_ADDRESS = "PRE_LAT_LNG_ADDRESS";
    public static final String PRE_ADDRESS = "PRE_ADDRESS";
    public static final String PRE_MY_CHARACTER = "PRE_MY_CHARACTER";
    public static final String PRE_MY_STYLE = "PRE_MY_STYLE";
    public static final String PRE_TARGET_CHARACTER = "PRE_TARGET_CHARACTER";
    public static final String PRE_TARGET_STYLE = "PRE_TARGET_STYLE";
    public static final String PRE_TARGET_FOOD = "PRE_TARGET_FOOD";
    public static final String PRE_AGE_MIN = "PRE_AGE_MIN";
    public static final String PRE_AGE_MAX = "PRE_AGE_MAX";
    public static final String PRE_TYPE_LOGIN = "_LOGIN";
    public static final String PRE_TYPE_REQUIRE = "_REQUIRE";
    private SharedPreferences sharedPreferences;
    private Activity activity;

    public MySharePreference(Activity activity) {
        sharedPreferences = activity.getSharedPreferences("DONTEATALONE.ACCOUNT", Context.MODE_PRIVATE);
        this.activity = activity;
    }

    public MySharePreference(Activity activity, String phone) {
        sharedPreferences = activity.getSharedPreferences("DONTEATALONE.INFORREQUIRE" + "_" + phone, Context.MODE_PRIVATE);
    }

    public String getPhoneLogin() {
        return sharedPreferences.getString(PRE_PHONE + PRE_TYPE_LOGIN, "");
    }

    public void setPhoneLogin(String phoneLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_PHONE + PRE_TYPE_LOGIN, phoneLogin);
        editor.apply();
    }

    public String getPhoneRequire() {
        return sharedPreferences.getString(PRE_PHONE + PRE_TYPE_REQUIRE, "");
    }

    public void setPhoneRequire(String phoneRequire) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_PHONE + PRE_TYPE_REQUIRE, phoneRequire);
        editor.apply();
    }

    public String getUUIDLogin() {
        return sharedPreferences.getString(PRE_UUID + PRE_TYPE_LOGIN, "");
    }

    public void setUUIDLogin(String UUIDLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_UUID + PRE_TYPE_LOGIN, UUIDLogin);
        editor.apply();
    }

    public String getFullNameLogin() {
        return sharedPreferences.getString(PRE_FULL_NAME + PRE_TYPE_LOGIN, "");
    }

    public void setFullNameLogin(String fullNameLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_FULL_NAME + PRE_TYPE_LOGIN, fullNameLogin);
        editor.apply();
    }

    public String getPasswordLogin() {
        return sharedPreferences.getString(PRE_PASSWORD + PRE_TYPE_LOGIN, "");
    }

    public void setPasswordLogin(String passwordLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_PASSWORD + PRE_TYPE_LOGIN, passwordLogin);
        editor.apply();
    }

    public String getGenderLogin() {
        return sharedPreferences.getString(PRE_GENDER + PRE_TYPE_LOGIN, "");
    }

    public void setGenderLogin(String genderLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_GENDER + PRE_TYPE_LOGIN, genderLogin);
        editor.apply();
    }

    public String getGenderRequire() {
        return sharedPreferences.getString(PRE_GENDER + PRE_TYPE_REQUIRE, "");
    }

    public void setGenderRequire(String genderRequire) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_GENDER + PRE_TYPE_REQUIRE, genderRequire);
        editor.apply();
    }

    public String getAvatarLogin() {
        return sharedPreferences.getString(PRE_AVATAR + PRE_TYPE_LOGIN, "");
    }

    public void setAvatarLogin(String avatarLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_AVATAR + PRE_TYPE_LOGIN, avatarLogin);
        editor.apply();
    }

    public String getLatLngAddressLogin() {
        return sharedPreferences.getString(PRE_LAT_LNG_ADDRESS + PRE_TYPE_LOGIN, "");
    }

    public void setLatLngAddressLogin(String latlngLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_LAT_LNG_ADDRESS + PRE_TYPE_LOGIN, latlngLogin);
        editor.apply();
    }

    public String getLatLngAddressRequire() {
        return sharedPreferences.getString(PRE_LAT_LNG_ADDRESS + PRE_TYPE_REQUIRE, "");
    }

    public void setLatLngAddressRequire(String latlngLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_LAT_LNG_ADDRESS + PRE_TYPE_REQUIRE, latlngLogin);
        editor.apply();
    }

    public String getAddressLogin() {
        return sharedPreferences.getString(PRE_ADDRESS + PRE_TYPE_LOGIN, "");
    }

    public void setAddressLogin(String addressLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_ADDRESS + PRE_TYPE_LOGIN, addressLogin);
        editor.apply();
    }

    public String getAddressRequire() {
        return sharedPreferences.getString(PRE_ADDRESS + PRE_TYPE_REQUIRE, "");
    }

    public void setAddressRequire(String addressLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_ADDRESS + PRE_TYPE_REQUIRE, addressLogin);
        editor.apply();
    }

    public String getMyCharacterLogin() {
        return sharedPreferences.getString(PRE_MY_CHARACTER + PRE_TYPE_LOGIN, "");
    }

    public void setMyCharacterLogin(String myCharacterLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_MY_CHARACTER + PRE_TYPE_LOGIN, myCharacterLogin);
        editor.apply();
    }

    public String getMyCharacterRequire() {
        return sharedPreferences.getString(PRE_MY_CHARACTER + PRE_TYPE_REQUIRE, "");
    }

    public void setMyCharacterRequire(String myCharacterLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_MY_CHARACTER + PRE_TYPE_REQUIRE, myCharacterLogin);
        editor.apply();
    }

    public String getMyStyleLogin() {
        return sharedPreferences.getString(PRE_MY_STYLE + PRE_TYPE_LOGIN, "");
    }

    public void setMyStyleLogin(String myStyleLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_MY_STYLE + PRE_TYPE_LOGIN, myStyleLogin);
        editor.apply();
    }

    public String getMyStyleRequire() {
        return sharedPreferences.getString(PRE_MY_STYLE + PRE_TYPE_REQUIRE, "");
    }

    public void setMyStyleRequire(String myStyleLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_MY_STYLE + PRE_TYPE_REQUIRE, myStyleLogin);
        editor.apply();
    }

    public String getTargetCharacterLogin() {
        return sharedPreferences.getString(PRE_TARGET_CHARACTER + PRE_TYPE_LOGIN, "");
    }

    public void setTargetCharacterLogin(String targetCharacterLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_TARGET_CHARACTER + PRE_TYPE_LOGIN, targetCharacterLogin);
        editor.apply();
    }

    public String getTargetCharacterRequire() {
        return sharedPreferences.getString(PRE_TARGET_CHARACTER + PRE_TYPE_REQUIRE, "");
    }

    public void setTargetCharacterRequire(String targetCharacterLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_TARGET_CHARACTER + PRE_TYPE_REQUIRE, targetCharacterLogin);
        editor.apply();
    }

    public String getTargetStyleLogin() {
        return sharedPreferences.getString(PRE_TARGET_STYLE + PRE_TYPE_LOGIN, "");
    }

    public void setTargetStyleLogin(String targetStyleLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_TARGET_STYLE + PRE_TYPE_LOGIN, targetStyleLogin);
        editor.apply();
    }

    public String getTargetStyleRequire() {
        return sharedPreferences.getString(PRE_TARGET_STYLE + PRE_TYPE_REQUIRE, "");
    }

    public void setTargetStyleRequire(String targetStyleLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_TARGET_STYLE + PRE_TYPE_REQUIRE, targetStyleLogin);
        editor.apply();
    }

    public String getTargetFoodLogin() {
        return sharedPreferences.getString(PRE_TARGET_FOOD + PRE_TYPE_LOGIN, "");
    }

    public void setTargetFoodLogin(String targetFoodLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_TARGET_FOOD + PRE_TYPE_LOGIN, targetFoodLogin);
        editor.apply();
    }

    public String getTargetFoodRequire() {
        return sharedPreferences.getString(PRE_TARGET_FOOD + PRE_TYPE_REQUIRE, "");
    }

    public void setTargetFoodRequire(String targetFoodLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_TARGET_FOOD + PRE_TYPE_REQUIRE, targetFoodLogin);
        editor.apply();
    }

    public String getBirthdayLogin() {
        return sharedPreferences.getString(PRE_BIRTHDAY + PRE_TYPE_LOGIN, "");
    }

    public void setBirthdayLogin(String birthdayLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_BIRTHDAY + PRE_TYPE_LOGIN, birthdayLogin);
        editor.apply();
    }

    public String getBirthdayRequire() {
        return sharedPreferences.getString(PRE_BIRTHDAY + PRE_TYPE_REQUIRE, "");
    }

    public void setBirthdayRequire(String birthdayLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_BIRTHDAY + PRE_TYPE_REQUIRE, birthdayLogin);
        editor.apply();
    }

    public String getAgeMinRequire() {
        return sharedPreferences.getString(PRE_AGE_MIN + PRE_TYPE_REQUIRE, "");
    }

    public void setAgeMinRequire(String ageMin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_AGE_MIN + PRE_TYPE_REQUIRE, ageMin);
        editor.apply();
    }

    public String getAgeMaxRequire() {
        return sharedPreferences.getString(PRE_AGE_MAX + PRE_TYPE_REQUIRE, "");
    }

    public void setAgeMaxRequire(String ageMax) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_AGE_MAX + PRE_TYPE_REQUIRE, ageMax);
        editor.apply();
    }

    public void saveAccountInfo(UserName userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_PHONE + PRE_TYPE_LOGIN, userName.getPhone());
        editor.putString(PRE_FULL_NAME + PRE_TYPE_LOGIN, userName.getFullName());
        editor.putString(PRE_GENDER + PRE_TYPE_LOGIN, userName.getGender());
        editor.putString(PRE_AVATAR + PRE_TYPE_LOGIN, userName.getAvatar());
        editor.putString(PRE_BIRTHDAY + PRE_TYPE_LOGIN, userName.getBirthday());
        editor.putString(PRE_ADDRESS + PRE_TYPE_LOGIN, userName.getAddress());
        editor.putString(PRE_LAT_LNG_ADDRESS + PRE_TYPE_LOGIN, userName.getLatlngAdress());
        editor.putString(PRE_MY_CHARACTER + PRE_TYPE_LOGIN, userName.getMyCharacter());
        editor.putString(PRE_MY_STYLE + PRE_TYPE_LOGIN, userName.getMyStyle());
        editor.putString(PRE_TARGET_CHARACTER + PRE_TYPE_LOGIN, userName.getTargetCharacter());
        editor.putString(PRE_TARGET_STYLE + PRE_TYPE_LOGIN, userName.getTargetStyle());
        editor.putString(PRE_TARGET_FOOD + PRE_TYPE_LOGIN, userName.getTargetFood());
        editor.apply();
    }

    public void saveProfileUpdate(UserName userName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRE_MY_CHARACTER + PRE_TYPE_LOGIN, userName.getMyCharacter());
        editor.putString(PRE_MY_STYLE + PRE_TYPE_LOGIN, userName.getMyStyle());
        editor.putString(PRE_TARGET_CHARACTER + PRE_TYPE_LOGIN, userName.getTargetCharacter());
        editor.putString(PRE_TARGET_STYLE + PRE_TYPE_LOGIN, userName.getTargetStyle());
        editor.putString(PRE_TARGET_FOOD + PRE_TYPE_LOGIN, userName.getTargetFood());
        editor.apply();
    }

    public UserName createObjectLogin() {
        return new UserName(activity, getPhoneLogin(), getFullNameLogin(), getAvatarLogin(), getBirthdayLogin(),
                getGenderLogin(), getAddressLogin(), getLatLngAddressLogin(), getMyCharacterLogin(), getMyStyleLogin(),
                getTargetCharacterLogin(), getTargetStyleLogin(), getTargetFoodLogin());
    }

    public UserName createObjectRequire() {
        return new UserName(activity, getPhoneLogin(), getFullNameLogin(), getAvatarLogin(), getBirthdayRequire(),
                getGenderRequire(), getAddressRequire(), getLatLngAddressRequire(), getMyCharacterRequire(), getMyStyleRequire(),
                getTargetCharacterRequire(), getTargetStyleRequire(), getTargetFoodRequire());
    }
}
