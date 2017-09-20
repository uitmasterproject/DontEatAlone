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
    public MySharePreference(Activity activity){
        sharedPreferences=activity.getSharedPreferences("DONTEATALONE.ACCOUNT", Context.MODE_PRIVATE);
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
        editor.putString("fullnameLogin", userName.getFullname());
        editor.putString("passwordLogin", userName.getPassword());
        editor.putString("genderLogin", userName.getGender());
        editor.putString("avatarLogin", userName.getAvatar());
        editor.putString("birthdayLogin", userName.getBirthday());
        editor.putString("addressLogin", userName.getAddress());
        editor.putString("latlngaddressLogin", userName.getLatLngAdress());
        editor.putString("hobbyLogin", userName.getHobby());
        editor.putString("characterLogin", userName.getCharacter());
        editor.apply();
    }
}
