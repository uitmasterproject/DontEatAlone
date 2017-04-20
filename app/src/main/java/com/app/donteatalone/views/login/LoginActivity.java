package com.app.donteatalone.views.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.donteatalone.base.BaseActivity;
import com.app.donteatalone.connectmongo.GetDatafromDB;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.views.blog.BlogActivity;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.UserName;
import com.app.donteatalone.views.register.RegisterActivity;
import com.app.donteatalone.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private String passPhone, passPassword;
    private String testPhone;

    @ViewById(R.id.activity_login_edt_phone)
    EditText edtPhone;
    @ViewById(R.id.activity_login_edt_password)
    EditText edtPassword;
    @ViewById(R.id.activity_login_ckb_remember)
    CheckBox ckbRememberMe;

    @AfterViews
    public void init() {
        /*Set Status bar color*/
        AppUtils.changeStatusBarColor(this);
        storeReference();
    }

    @Click(R.id.activity_login_btn_login)
    void btnLoginClick() {
        if (checkEntry(edtPhone.getText().toString())) {
            edtPhone.setError("Invalid Phone");
        }
        if (checkEntry(edtPassword.getText().toString())) {
            edtPassword.setError("Invalid Password");
        }
        if (!checkEntry(edtPhone.getText().toString()) && !checkEntry(edtPassword.getText().toString())) {
            checkAccount();
        }
    }

    @Click(R.id.activity_login_rl_forgot_password)
    void rlForgotPasswordClick() {

    }

    @Click(R.id.activity_login_rl_register)
    void rlRegisterClick() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @CheckedChange(R.id.activity_login_ckb_remember)
    void ckbRememberMeCheckedChange() {
        if (ckbRememberMe.isChecked()) {
            saveReference(edtPhone.getText().toString(), edtPassword.getText().toString());
        } else {
            clearReference();
        }
    }

    //Hide soft keyboard when you touch outside
    @Touch(R.id.activity_login_ll_root)
    void llRootTouch() {
        AppUtils.hideSoftKeyboard(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.animator.animator_left_in, R.animator.animator_right_out);
    }

    @Override
    public void showProgressLoading() {
        super.showProgressLoading();
    }

    @Override
    public void hideProgressLoading() {
        super.hideProgressLoading();
    }

    public void checkPassDatafromRegister() {
        if ((null != passPhone) || (null != passPassword)) {
            edtPhone.setText("");
            edtPassword.setText("");
        } else {
            edtPhone.setText(passPhone);
            edtPassword.setText(passPassword);
        }
    }

    public void getDatafromRegister() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle.getString("phone") && null != bundle.getString("password")) {
            passPhone = bundle.getString("phone");
            passPassword = bundle.getString("password");
        } else {
            passPhone = "";
            passPassword = "";
        }
    }

    public void saveReference(String phone, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phoneLogin", phone);
        editor.putString("passwordLogin", password);
        editor.apply();
    }

    public void clearReference() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
    }

    public void storeReference() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        Boolean bchk = sharedPreferences.getBoolean("checked", false);
        if (!bchk) {
            edtPhone.setText(sharedPreferences.getString("phoneLogin", ""));
            edtPassword.setText(sharedPreferences.getString("passwordLogin", ""));
        } else {
            edtPhone.setText("");
            edtPassword.setText("");
        }
    }

    public void checkAccount() {
        testPhone = edtPhone.getText().toString();
        if (null != edtPhone.getText().toString() && null != edtPassword.getText().toString()) {
            Call<Status> getPass = Connect.getRetrofit().checkAccount(edtPhone.getText().toString(),
                    edtPassword.getText().toString());

            getPass.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {

                    if (response.body().getStatus().equals("Login success")) {
                        GetDatafromDB getData = new GetDatafromDB(getBaseContext());
                        getData.execute(edtPhone.getText().toString());
                        Intent intent = new Intent(LoginActivity.this, BlogActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, "Password incorecct, Check phone or password again.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    Log.e("error", t.toString());
                }
            });
        }
    }

    public boolean checkEntry(String values) {
        return !((values.trim()).equals("") != true || null != values);
    }


}