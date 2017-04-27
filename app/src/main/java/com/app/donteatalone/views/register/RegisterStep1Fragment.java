package com.app.donteatalone.views.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.model.UserName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class RegisterStep1Fragment extends Fragment {

    private ViewPager _mViewPager;
    private ViewGroup viewGroup;
    private RelativeLayout rlNext, rlSendCode;
    private EditText edtCode, edtPhone;
    public static UserName userName;

    public static Fragment newInstance(Context context) {
        RegisterStep1Fragment f = new RegisterStep1Fragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup=(ViewGroup)inflater.inflate(R.layout.fragment_register_step1,null);
        init();
        changeDataEdtPhone(edtPhone,rlSendCode);
        rlSendCodeClick();
        rlNextClick();
        return viewGroup;
    }

    public void init(){
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        rlSendCode=(RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step1_rl_send_code);
        edtPhone=(EditText) viewGroup.findViewById(R.id.fragment_register_step1_edt_phone);
        rlNext=(RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step1_rl_next);
        edtCode=(EditText) viewGroup.findViewById(R.id.fragment_register_step1_edt_code);
        userName=new UserName();
    }

    public void changeDataEdtPhone(final EditText edt, final RelativeLayout rl){
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edt.getText().toString().equals("")==false){
                    rl.setVisibility(View.VISIBLE);
                    if(edt.getId()==R.id.fragment_register_step1_edt_phone) {

                    }
                }
                else {
                    rl.setVisibility(View.GONE);
                    if(edt.getId()==R.id.fragment_register_step1_edt_phone) {

                    }
                }
            }
        });
    }

    private void checkExitsPhone(){
        Connect connect=new Connect();
        Call<Status> checkPhone=connect.getRetrofit().checkPhoneExits(edtPhone.getText().toString());
        checkPhone.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.body().getStatus().equals("this phone isnt exits")==true){
                    changeDataEdtPhone(edtCode,rlNext);
                }
                else {
                    edtPhone.setError("This phone was exit");
                    edtPhone.setText("");
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.e("ERROR Phone Exits",t.toString()+"*************************************");
            }
        });
    }

    private void rlSendCodeClick(){
        rlSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExitsPhone();
            }
        });
    }

    //IN HERE, HAVEN'T METHOD CHECK CODE
    private void rlNextClick(){
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setPhone(edtPhone.getText().toString());
                saveReference();
                _mViewPager.setCurrentItem(1,true);
            }
        });
    }

    public void saveReference(){
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("phone",edtPhone.getText().toString());
        editor.apply();
    }


}
