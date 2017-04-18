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
    private List<UserName> temp;
    private String passUserformData;

    @ViewById(R.id.activity_login_edt_phone)
    EditText edtPhone;
    @ViewById(R.id.activity_login_edt_password)
    EditText edtPassword;
    @ViewById(R.id.activity_login_ckb_remember)
    CheckBox ckbRememberMe;
    @ViewById(R.id.activity_login_rl_register)
    RelativeLayout rlRegister;
    @ViewById(R.id.activity_login_rl_forgot_password)
    RelativeLayout rlForgotPassword;

    @AfterViews
    public void init() {
        /*Set Status bar color*/
        AppUtils.changeStatusBarColor(this);

        storeReference();
    }

    @Click(R.id.activity_login_btn_login)
    void btnLoginClick() {
        checkAccount();
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
            saveReference();
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

    public void checkPassDatafromRegister() {
        if (null != passPhone || null != passPassword) {
            edtPhone.setText("");
            edtPassword.setText("");
        } else {
            edtPhone.setText(passPhone);
            edtPassword.setText(passPassword);
        }
    }

    private void saveReference() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", edtPhone.getText().toString());
        editor.putString("password", edtPassword.getText().toString());
        editor.apply();
    }

    private void clearReference() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
    }

    private void storeReference() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        Boolean bchk = sharedPreferences.getBoolean("checked", false);
        if (!bchk) {
            edtPhone.setText(sharedPreferences.getString("phone", ""));
            edtPassword.setText(sharedPreferences.getString("password", ""));
        } else {
            edtPhone.setText("");
            edtPassword.setText("");
        }
    }

    private void checkAccount() {
        String testPhone = edtPhone.getText().toString();
        if (null != edtPhone.getText().toString() && null != edtPassword.getText().toString()) {
            temp = new ArrayList<>();
            Connect connect = new Connect();
            Call<List<UserName>> getPass = Connect.getRetrofit().getPhoneUser(edtPhone.getText().toString());

            getPass.enqueue(new Callback<List<UserName>>() {
                @Override
                public void onResponse(Call<List<UserName>> call, Response<List<UserName>> response) {

                    temp = response.body();
                    passUserformData = temp.get(0).getPassword();
                    if (passUserformData.equals(edtPassword.getText().toString())) {
                        Intent intent = new Intent(LoginActivity.this, BlogActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, "Password incorecct", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<UserName>> call, Throwable t) {
                    Log.e("error", t.toString());
                }
            });
        }
    }
}
