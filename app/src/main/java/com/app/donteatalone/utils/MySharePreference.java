package com.app.donteatalone.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.donteatalone.model.UserName;


/**
 * Created by ChomChom on 10-Sep-17
 */

public class MySharePreference {
    private SharedPreferences sharedPreferences;
    private Activity activity;
    public MySharePreference(Activity activity){
        sharedPreferences=activity.getSharedPreferences("DONTEATALONE.ACCOUNT", Context.MODE_PRIVATE);
        this.activity=activity;
    }

    public MySharePreference(Activity activity, String phone){
        sharedPreferences=activity.getSharedPreferences("DONTEATALONE.INFORREQUIRE"+"_"+phone, Context.MODE_PRIVATE);
    }

    public void setValue (String key,String value){
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getValue(String key){
        return sharedPreferences.getString(key,"");
    }

    public void saveAccountInfo(UserName userName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phoneLogin", userName.getPhone());
        editor.putString("imeiLogin", userName.getImei());
        editor.putString("fullNameLogin", userName.getFullName());
        editor.putString("passwordLogin", userName.getPassword());
        editor.putString("genderLogin", userName.getGender());
        editor.putString("avatarLogin", userName.getAvatar());
        editor.putString("birthdayLogin", userName.getBirthday());
        editor.putString("addressLogin", userName.getAddress());
        editor.putString("latlngAddressLogin", userName.getLatlngAdress());
        editor.putString("myCharacterLogin", userName.getMyCharacter());
        editor.putString("myStyleLogin", userName.getMyStyle());
        editor.putString("targetCharacterLogin", userName.getTargetCharacter());
        editor.putString("targetStyleLogin", userName.getTargetStyle());
        editor.putString("targetFoodLogin", userName.getTargetFood());
        editor.apply();
    }

    public UserName createObject(){
        return  new UserName(activity, sharedPreferences.getString("phoneLogin",null),sharedPreferences.getString("imeiLogin",null),
                sharedPreferences.getString("fullNameLogin",null),null,sharedPreferences.getString("genderLogin",null),
                sharedPreferences.getString("avatarLogin",null), sharedPreferences.getString("birthdayLogin",null),sharedPreferences.getString("addressLogin","Ho Chi Minh"),
                sharedPreferences.getString("latlngAddressLogin","10.771423,106.698471"),sharedPreferences.getString("myCharacterLogin",null),
                sharedPreferences.getString("myStyleLogin",null), sharedPreferences.getString("targetCharacterLogin",null),sharedPreferences.getString("targetStyleLogin",null),
                sharedPreferences.getString("targetFoodLogin",null));
    }
}
