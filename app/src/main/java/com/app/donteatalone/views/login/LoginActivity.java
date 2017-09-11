package com.app.donteatalone.views.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.model.UserName;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.MainActivity;
import com.app.donteatalone.views.register.RegisterActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtPhone, edtPassword;
    private Button btnLogin;
    private RelativeLayout rlRegister, rlForgetPass;
    private CheckBox ckbRemember;
    private MySharePreference mySharePreference;
    private BaseProgress dialog;
    private FrameLayout flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppUtils.changeStatusBarColor(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!AppUtils.isNetworkAvailable(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
        }
        init();
        clickLogin();
        clickRegister();
        clickForgetPassword();
        flRootTouch();
    }

    public void init() {
        mySharePreference = new MySharePreference(LoginActivity.this);
        edtPhone = (EditText) findViewById(R.id.activity_login_edt_phone);
        edtPassword = (EditText) findViewById(R.id.activity_login_edt_password);
        edtPhone.setText(mySharePreference.getValue("phoneLogin"));
        edtPassword.setText(mySharePreference.getValue("passwordLogin"));
        ckbRemember = (CheckBox) findViewById(R.id.activity_login_ckb_remember);
        rlRegister = (RelativeLayout) findViewById(R.id.activity_login_rl_register);
        rlForgetPass = (RelativeLayout) findViewById(R.id.activity_login_rl_forgot_password);
        btnLogin = (Button) findViewById(R.id.activity_login_btn_login);
        flContainer=(FrameLayout) findViewById(R.id.activity_login_fl);
        dialog=new BaseProgress();
    }

    public void checkRemember() {
        if (ckbRemember.isChecked()) {
            mySharePreference.setValue("phoneLogin", edtPhone.getText().toString());
            mySharePreference.setValue("passwordLogin", edtPassword.getText().toString());
        } else {
            mySharePreference.setValue("phoneLogin", "");
            mySharePreference.setValue("passwordLogin", "");
        }
    }

    public void checkAccount() {
        dialog.showProgressLoading(LoginActivity.this);
            Call<Status> getPass = Connect.getRetrofit().checkAccount(edtPhone.getText().toString(), edtPassword.getText().toString());
            getPass.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {

                    if (response.body() == null) {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.body().getStatus().equals("0")) {
                            if (mySharePreference.getValue("phoneLogin").equals(edtPhone.getText().toString())) {
                                dialog.hideProgressLoading();
                                checkRemember();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                saveInfoUser();
                            }
                        } else if (response.body().getStatus().equals("1")) {
                            dialog.hideProgressLoading();
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.password_incorecct), Toast.LENGTH_LONG).show();
                        } else {
                            dialog.hideProgressLoading();
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.phone_isnt_exist), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    dialog.hideProgressLoading();
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void saveInfoUser() {
        Call<UserName> userLogin = Connect.getRetrofit().getProfileUser(edtPhone.getText().toString());
        userLogin.enqueue(new Callback<UserName>() {
            @Override
            public void onResponse(Call<UserName> call, Response<UserName> response) {
                dialog.hideProgressLoading();
                if(response.body()!=null){
                    UserName userName = response.body();
                    mySharePreference.saveAccountInfo(userName);
                    checkRemember();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UserName> call, Throwable t) {
                dialog.hideProgressLoading();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.animator.animator_left_in, R.animator.animator_right_out);
    }

    public void clickRegister() {
        rlRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clickForgetPassword() {
        rlForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ForgetPasswordDialog(LoginActivity.this).showDialog(edtPassword);
            }
        });
    }

    public void clickLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    edtPhone.setError("Invalid Phone");
                }
                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    edtPassword.setError("Invalid Password");
                }
                if (!TextUtils.isEmpty(edtPhone.getText().toString()) && !TextUtils.isEmpty(edtPassword.getText().toString())) {
                    checkAccount();
                }
            }
        });
    }

    /*Hide softkeyboard when touch outsite edittext*/
    private void flRootTouch() {
        flContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideSoftKeyboard(LoginActivity.this);
                return true;
            }
        });
    }
}