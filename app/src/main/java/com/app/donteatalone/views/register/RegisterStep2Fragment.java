package com.app.donteatalone.views.register;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.app.donteatalone.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterStep2Fragment extends Fragment {
    private ViewGroup viewGroup;
    private EditText edtFullName, edtPassword;
    private RelativeLayout rlNext;
    private ViewPager _mViewPager;

    public static Fragment newInstance(Context context) {
        RegisterStep2Fragment f = new RegisterStep2Fragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_register_step2, null);
        init();
        changeDataEditFullName();
        clickButtonNextStep();
        return viewGroup;
    }


    public void init(){
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        edtFullName= (EditText) viewGroup.findViewById(R.id.fragment_register_step2_edt_fullname);
        edtPassword=(EditText) viewGroup.findViewById(R.id.fragment_register_step2_edt_password);
        edtPassword.setEnabled(false);
        rlNext=(RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step2_btn_next);
        rlNext.setEnabled(false);
    }

    private void changeDataEditFullName(){
        edtFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!edtFullName.getText().toString().equals("")){
                    edtPassword.setEnabled(true);
                    changDataEditPassword();
                    if(edtPassword.getText().toString().length()==0) {
                        rlNext.setEnabled(false);
                    }
                    else {
                        rlNext.setEnabled(true);
                        edtPassword.setEnabled(true);
                    }
                }
                else {
                    rlNext.setEnabled(false);
                }
            }
        });

    }

    private void changDataEditPassword(){
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!edtPassword.getText().toString().equals("")){
                    rlNext.setEnabled(true);
                }
                else {
                    rlNext.setEnabled(false);
                }
            }
        });
    }


    private void clickButtonNextStep(){
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPassword.getText().toString().length()<6){
                    edtPassword.setError("Length password is longer 6 character");
                }
                else {
                    RegisterStep1Fragment.userName.setFullname(edtFullName.getText().toString());
                    RegisterStep1Fragment.userName.setPassword(edtPassword.getText().toString());
                    saveReference();
                    _mViewPager.setCurrentItem(2,true);
                }
            }
        });
    }


    private void saveReference(){
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("fullname",edtFullName.getText().toString());
        editor.putString("password", edtPassword.getText().toString());
        editor.apply();
    }
}
