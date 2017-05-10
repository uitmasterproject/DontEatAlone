package com.app.donteatalone.views.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Hobby;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.views.login.LoginActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by ChomChom on 4/10/2017.
 */

public class RegisterStep7Fragment extends Fragment {
    private View viewGroup;
    private AutoCompleteTextView actvCharacter;
    private ArrayList<String> headerCharacter;
    private ArrayList<Hobby> character;
    private RelativeLayout rlNextStep, rlClose;

    public static Fragment newInstance(Context context) {
        RegisterStep7Fragment f = new RegisterStep7Fragment();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup=inflater.inflate(R.layout.fragment_register_step7,null);
        init();
        setActvHobby();
        clickButtonNextStep();
        rlCloseClick();
        return viewGroup;
    }
    private void init(){
        actvCharacter=(AutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step7_actv_character);
        rlNextStep=(RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step7_rl_register);
        rlClose = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step7_close);
    }

    private void setActvHobby(){
        clonebdHobby();
        CustomAdapterCompleteTextView customAdapterCompleteTextView=new CustomAdapterCompleteTextView(this.getContext(), android.R.layout.simple_dropdown_item_1line,headerCharacter,character,actvCharacter);
        actvCharacter.setAdapter(customAdapterCompleteTextView);

    }

    private void clickButtonNextStep(){
        rlNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterStep1Fragment.userName.setCharacter(actvCharacter.getText().toString());
                saveReference();
                InsertUserintoDB();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private  void rlCloseClick() {
        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void InsertUserintoDB(){
        Connect connect=new Connect();
        Call<Status> insertUser = connect.getRetrofit().insertUser(RegisterStep1Fragment.userName);
        insertUser.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Log.e("result:",response.body().getStatus()+"++++++++++++++++++++++++++++++++++++");
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }

    private void clonebdHobby(){
        headerCharacter=new ArrayList<>();
        headerCharacter.add("Tính cách");
        headerCharacter.add("Phong cách");

        character=new ArrayList<>();
        character.add(new Hobby("Tính cách","",true));
        character.add(new Hobby("Tính cách","vui vẻ",false));
        character.add(new Hobby("Tính cách","trầm tĩnh",false));
        character.add(new Hobby("Tính cách","hóm hĩnh",false));
        character.add(new Hobby("Tính cách","thoải mái",false));

        character.add(new Hobby("Phong cách","",true));
        character.add(new Hobby("Phong cách","tự do",false));
        character.add(new Hobby("Phong cách","quái dị",false));
        character.add(new Hobby("Phong cách","trưởng thành",false));
    }

    private void saveReference(){
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("character",actvCharacter.getText().toString());
        editor.putString("phoneLogin", RegisterStep1Fragment.userName.getPhone().toString());
        editor.putString("passwordLogin", RegisterStep1Fragment.userName.getPassword().toString());
        editor.commit();
    }

}
