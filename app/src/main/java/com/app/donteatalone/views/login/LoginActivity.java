package com.app.donteatalone.views.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InitParam;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.model.UserName;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.MainActivity;
import com.app.donteatalone.views.register.RegisterActivity;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtPhone, edtPassword;
    private TextView tvRegister, tvForgetPass;
    private Button btnLogin;
    private CheckBox ckbRemember;
    private MySharePreference mySharePreference;
    private BaseProgress dialog;
    private LinearLayout llRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppUtils.changeStatusBarColor(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (TextUtils.isEmpty(new MySharePreference(LoginActivity.this).getUUIDLogin())) {
            String uuid = UUID.randomUUID().toString();
            new MySharePreference(LoginActivity.this).setUUIDLogin(uuid);
        }

        init();
        clickLogin();
        clickRegister();
        clickForgetPassword();
        llRootTouch();
    }

    public void init() {
        mySharePreference = new MySharePreference(LoginActivity.this);
        edtPhone = (EditText) findViewById(R.id.activity_login_edt_phone);
        edtPassword = (EditText) findViewById(R.id.activity_login_edt_password);
        edtPhone.setText(mySharePreference.getPhoneLogin());
        ckbRemember = (CheckBox) findViewById(R.id.activity_login_ckb_remember);
        tvRegister = (TextView) findViewById(R.id.activity_login_tv_register);
        tvForgetPass = (TextView) findViewById(R.id.activity_login_tv_forgot_password);
        btnLogin = (Button) findViewById(R.id.activity_login_btn_login);
        llRoot = (LinearLayout) findViewById(R.id.activity_login_ll_root);
        dialog = new BaseProgress();
    }

    public void checkRemember() {
        if (ckbRemember.isChecked()) {
            mySharePreference.setPhoneLogin(edtPhone.getText().toString());
        } else {
            mySharePreference.setPhoneLogin("");
        }
    }

    public void checkAccount() {
        dialog.showProgressLoading(LoginActivity.this);

        Call<InitParam> init = Connect.getRetrofit().getInitParam(edtPhone.getText().toString());
        init.enqueue(new Callback<InitParam>() {
            @Override
            public void onResponse(Call<InitParam> call, Response<InitParam> response) {
                if (response.body() != null) {
                    String password = AppUtils.encrypt(response.body().getInitParam(), edtPassword.getText().toString());

                    UserName userName = new UserName(edtPhone.getText().toString(), password);

                    Call<Status> getPass = Connect.getRetrofit().checkAccount(userName);
                    getPass.enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            if (response.body() == null) {
                                dialog.hideProgressLoading();
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
                            } else {
                                if (response.body().getStatus().equals("0")) {
                                    if (!response.body().getUuid().equals(mySharePreference.getUUIDLogin()) ||
                                            !edtPhone.getText().toString().equals(mySharePreference.getPhoneLogin())) {
                                        saveInfoUser();
                                    } else {
                                        checkRemember();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        dialog.hideProgressLoading();
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
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    dialog.hideProgressLoading();
                    Toast.makeText(LoginActivity.this, getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InitParam> call, Throwable t) {
                dialog.hideProgressLoading();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveInfoUser() {
        Call<UserName> userLogin = Connect.getRetrofit().getProfileUser(edtPhone.getText().toString());
        userLogin.enqueue(new Callback<UserName>() {
            @Override
            public void onResponse(Call<UserName> call, Response<UserName> response) {
                dialog.hideProgressLoading();
                if (response.body() != null) {
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
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clickForgetPassword() {
        tvForgetPass.setOnClickListener(new View.OnClickListener() {
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
                    edtPhone.setError(getString(R.string.invalid_phone));
                }
                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    edtPassword.setError(getString(R.string.invalid_password));
                }
                if (!TextUtils.isEmpty(edtPhone.getText().toString()) && !TextUtils.isEmpty(edtPassword.getText().toString())) {
                    if (AppUtils.isNetworkAvailable(LoginActivity.this)) {
                        checkAccount();
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /*Hide softkeyboard when touch outsite edittext*/
    private void llRootTouch() {
        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideSoftKeyboard(LoginActivity.this);
            }
        });
    }
}