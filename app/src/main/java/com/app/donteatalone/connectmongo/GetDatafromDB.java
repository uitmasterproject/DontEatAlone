package com.app.donteatalone.connectmongo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.app.donteatalone.model.UserName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ChomChom on 3/16/2017.
 */

public class GetDatafromDB extends AsyncTask<String,Void,UserName> {

    private UserName user;
    private Context context;
    public GetDatafromDB(Context context) {
        super();
        user=new UserName();
        this.context=context;
    }

    @Override
    protected UserName doInBackground(String... params) {
        String phone=params[0];
        Connect connect=new Connect();
        Call<UserName> userLogin = connect.getRetrofit().getProfileUser(phone);
        userLogin.enqueue(new Callback<UserName>() {
            @Override
            public void onResponse(Call<UserName> call, Response<UserName> response) {
                user=response.body();
            }

            @Override
            public void onFailure(Call<UserName> call, Throwable t) {

            }
        });
        return user;
    }

    @Override
    protected void onPostExecute(UserName userName) {
        super.onPostExecute(userName);
        saveReference(userName);

    }

    private void saveReference(UserName userName){
        SharedPreferences sharedPreferences=context.getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("phoneLogin", userName.getPhone());
        editor.putString("fullnameLogin",userName.getFullname());
        editor.putString("passwordLogin", userName.getPassword());
        editor.putString("genderLogin",userName.getGender());
        editor.putString("avatarLogin",userName.getAvatar());
        editor.putString("birthdayLogin",userName.getBirthday());
        editor.putString("addressLogin",userName.getAddress());
        editor.putString("hobbyLogin",userName.getHobby());
        editor.putString("characterLogin",userName.getCharacter());
        editor.commit();
    }
}
