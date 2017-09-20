package com.app.donteatalone.views.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.donteatalone.R.array.character;


/**
 * Created by ChomChom on 4/10/2017.
 */

public class RegisterStep7Fragment extends Fragment {
    private View viewGroup;
    private MultiAutoCompleteTextView actvCharacter;
    private MultiAutoCompleteTextView actvStyle;
    private RelativeLayout rlNextStep, rlClose;
    private LinearLayout llRoot;
    private BaseProgress dialog;

    public static Fragment newInstance() {
        return new RegisterStep7Fragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup=inflater.inflate(R.layout.fragment_register_step7,null);
        init();
        llRootTouch();
        setActvHobby();
        clickButtonNextStep();
        rlCloseClick();
        return viewGroup;
    }
    private void init(){
        actvCharacter=(MultiAutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step7_actv_character);
        actvCharacter.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        actvStyle=(MultiAutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step7_actv_style);
        actvStyle.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        rlNextStep=(RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step7_rl_register);
        rlClose = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step7_close);
        llRoot = (LinearLayout) viewGroup.findViewById(R.id.fragment_register_step7_ll_root);
        dialog=new BaseProgress();
    }

    private void setActvHobby(){
        ArrayAdapter hobbyAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(character));
        actvCharacter.setAdapter(hobbyAdapter);
        actvCharacter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actvCharacter.setText(AppUtils.convertStringToNFD(actvCharacter.getText().toString()));
                actvCharacter.setSelection(actvCharacter.getText().toString().length());
            }
        });
        hobbyAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.style));
        actvStyle.setAdapter(hobbyAdapter);
        actvStyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actvStyle.setText(AppUtils.convertStringToNFD(actvStyle.getText().toString()));
                actvStyle.setSelection(actvStyle.getText().toString().length());
            }
        });
    }

    /*Hide softkeyboard when touch outsite edittext*/
    private void llRootTouch() {
        llRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideSoftKeyboard(getActivity());
                return true;
            }
        });
    }

    private void clickButtonNextStep(){
        rlNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actvCharacter.getText().toString().trim().endsWith(",")){
                    actvCharacter.setText(actvCharacter.getText().toString().trim().substring(0,actvCharacter.getText().toString().lastIndexOf(",")));
                }

                if(actvStyle.getText().toString().trim().endsWith(",")){
                    actvStyle.setText(actvStyle.getText().toString().substring(0,actvStyle.getText().toString().trim().lastIndexOf(",")));
                }
                RegisterStep1Fragment.userName.setCharacter(actvCharacter.getText().toString()+","+actvStyle.getText().toString());
                new MySharePreference(getActivity()).saveAccountInfo(RegisterStep1Fragment.userName);
                InsertUserintoDB();
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
        dialog.showProgressLoading(getActivity());
        Call<Status> insertUser = Connect.getRetrofit().insertUser(RegisterStep1Fragment.userName);
        insertUser.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                dialog.hideProgressLoading();
                if(response.body()!=null){
                    if (response.body().getStatus().equals("0")){
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getActivity(),getResources().getString(R.string.register_fail),Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.invalid_network),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                dialog.hideProgressLoading();
                Toast.makeText(getActivity(),getResources().getString(R.string.invalid_network),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
