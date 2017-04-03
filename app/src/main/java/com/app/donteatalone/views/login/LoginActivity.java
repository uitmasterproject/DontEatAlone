package com.app.donteatalone.views.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.blog.BlogActivity;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.UserName;
import com.app.donteatalone.views.register.RegisterActivity;
import com.app.donteatalone.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private String passPhone, passPassword;
    private Bundle bundle;
    private EditText edtPhone, edtPassword;
    private Button btnLogin;
    private TextView txtRegister, txtForgetPass;
    private CheckBox ckbRemember;
    private List<UserName> temp;
    private String passUserformData,testPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getDatafromRegister();
        init();
        storeReference();
        clickLogin();
        clickRegister();
    }

    public void getDatafromRegister(){
        bundle=getIntent().getExtras();
        if(null!=bundle.getString("phone")&&null!=bundle.getString("password")) {
            passPhone = bundle.getString("phone");
            passPassword = bundle.getString("password");
        }
        else {
            passPhone = "";
            passPassword="";
        }
    }

    public void init(){
        edtPhone=(EditText) findViewById(R.id.activity_login_edt_phone);
        edtPassword=(EditText) findViewById(R.id.activity_login_edt_password);
        ckbRemember=(CheckBox) findViewById(R.id.activity_login_ckb_remember);
        txtRegister=(TextView) findViewById(R.id.activity_login_txt_register);
        txtForgetPass=(TextView) findViewById(R.id.activity_login_txt_forgetPass);
        btnLogin=(Button) findViewById(R.id.activity_login_btn_login);
        /*Paint p = new Paint();
        p.setColor(Color.RED);

        txtRegister.setPaintFlags(p.getColor());
        txtRegister.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtRegister.setText("Don't have a account");*/

        /*String styledText = "<u style=\"color:'#FF4081'\"><font>Don't have a account</font></u>.";
        txtRegister.setText(Html.fromHtml(styledText),TextView.BufferType.SPANNABLE);*/
    }

    public void checkPassDatafromRegister(){
        if ((null!=passPhone)|| (null!=passPassword)){
            edtPhone.setText("");
            edtPassword.setText("");
        }
        else{
            edtPhone.setText(passPhone);
            edtPassword.setText(passPassword);
        }
    }

    public void checkRemember(){
        if(ckbRemember.isChecked()==true){
            saveReference();
        }
        else {
            clearReference();
        }
    }

    public void saveReference(){
        SharedPreferences sharedPreferences=getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("phone",edtPhone.getText().toString());
        editor.putString("password", edtPassword.getText().toString());
        editor.commit();
    }

    public void clearReference(){
        SharedPreferences sharedPreferences=getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
    }

    public void storeReference(){
        SharedPreferences sharedPreferences=getSharedPreferences("account",MODE_PRIVATE);
        Boolean bchk=sharedPreferences.getBoolean("checked", false);
        if(bchk==false)
        {
            edtPhone.setText(sharedPreferences.getString("phone", ""));
            edtPassword.setText(sharedPreferences.getString("password", ""));
        }
        else {
            edtPhone.setText("");
            edtPassword.setText("");
        }
    }

    public void checkAccount(){
        testPhone=edtPhone.getText().toString();
        if(null!=edtPhone.getText().toString()&&null!=edtPassword.getText().toString()) {
            temp=new ArrayList<>();
            Connect connect = new Connect();
            Call<List<UserName>> getPass = connect.getRetrofit().getPhoneUser(edtPhone.getText().toString());

            getPass.enqueue(new Callback<List<UserName>>() {
                @Override
                public void onResponse(Call<List<UserName>> call, Response<List<UserName>> response) {;
                    temp = response.body();
                    passUserformData =temp.get(0).getPassword();
                    if (passUserformData.equals(edtPassword.getText().toString())==true){
                        Intent intent=new Intent(LoginActivity.this, BlogActivity.class);
                        startActivity(intent);

                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Password incorecct", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<UserName>> call, Throwable t) {
                    Log.e("error",t.toString());
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.animator.animator_left_in,R.animator.animator_right_out);
    }

    public void clickRegister(){
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void clickLogin(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRemember();
                checkAccount();
            }
        });
    }
}
