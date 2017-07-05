package com.app.donteatalone.views.login;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.model.UserName;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.views.main.MainActivity;
import com.app.donteatalone.views.register.RegisterActivity;
import com.victor.loading.rotate.RotateLoading;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtPhone, edtPassword;
    private Button btnLogin;
    private RelativeLayout rlRegister, rlForgetPass;
    private CheckBox ckbRemember;
    private RotateLoading loading;
    private Dialog dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppUtils.changeStatusBarColor(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        clickLogin();
        clickRegister();
        clickForgetPassword();
    }

    public void init() {
        edtPhone = (EditText) findViewById(R.id.activity_login_edt_phone);
        edtPassword = (EditText) findViewById(R.id.activity_login_edt_password);
        edtPhone.setText(storeReference("phoneLogin"));
        edtPassword.setText(storeReference("passwordLogin"));
        ckbRemember = (CheckBox) findViewById(R.id.activity_login_ckb_remember);
        rlRegister = (RelativeLayout) findViewById(R.id.activity_login_rl_register);
        rlForgetPass = (RelativeLayout) findViewById(R.id.activity_login_rl_forgot_password);
        btnLogin = (Button) findViewById(R.id.activity_login_btn_login);
    }

    public void checkRemember() {
        if (ckbRemember.isChecked() == true) {
            saveReference("phoneLogin", edtPhone.getText().toString());
            saveReference("passwordLogin", edtPassword.getText().toString());
        } else {
            notRemember();
        }
    }

    public void saveReference(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void notRemember() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phoneLogin", "");
        editor.putString("passwordLogin", "");
    }

    private String storeReference(String key) {
        String value = "";
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        value = sharedPreferences.getString(key, "");
        return value;
    }

    public void checkAccount() {
        if(null!=edtPhone.getText().toString()&&null!=edtPassword.getText().toString()) {
            Connect connect = new Connect();
            Call<Status> getPass = connect.getRetrofit().checkAccount(edtPhone.getText().toString(), edtPassword.getText().toString());
            getPass.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {

                    if (response.body() == null) {
                        Toast.makeText(LoginActivity.this,"Don't connect server",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (response.body().getStatus().equals("Login success") == true) {
                            if(storeReference("phoneLogin").equals(edtPhone.getText().toString())==true){
                                Log.e("not save user","not save user");
                                checkRemember();
                                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Log.e("save user","save user");
                                saveInfoUser();
                            }
                        } else if (response.body().getStatus().equals("This phone isnt exits") == true) {
                            Toast.makeText(LoginActivity.this, "This phone isn't exits. Press register to create account", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Password incorecct, Check phone or password again.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    Log.e("error", t.toString());
                }
            });
        }
    }

    private void saveInfoUser() {
        Connect connect = new Connect();
        final UserName[] user = {new UserName()};
        Call<UserName> userLogin = connect.getRetrofit().getProfileUser(edtPhone.getText().toString());
        userLogin.enqueue(new Callback<UserName>() {
            @Override
            public void onResponse(Call<UserName> call, Response<UserName> response) {
                user[0] = response.body();
                saveInfoReference(user[0]);
                checkRemember();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<UserName> call, Throwable t) {

            }
        });
    }

    private void setDialogLoading() {
        dialogLoading = new Dialog(LoginActivity.this);
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoading.setContentView(R.layout.custom_dialog_processbar);
        loading = (RotateLoading) dialogLoading.findViewById(R.id.custom_dialog_processbar_newton_cradle_loading);
        dialogLoading.show();
    }

    private void saveInfoReference(UserName userName) {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phoneLogin", userName.getPhone());
        editor.putString("fullnameLogin", userName.getFullname());
        editor.putString("passwordLogin", userName.getPassword());
        editor.putString("genderLogin", userName.getGender());
        editor.putString("avatarLogin", userName.getAvatar());
        editor.putString("birthdayLogin", userName.getBirthday());
        editor.putString("addressLogin", userName.getAddress());
        editor.putString("latlngaddressLogin", userName.getLatLngAdress());
        editor.putString("hobbyLogin", userName.getHobby());
        editor.putString("characterLogin", userName.getCharacter());
        editor.commit();
    }

    public boolean checkEntry(String values) {
        if ((values.trim()).equals("") != true || null != values) {
            return false;
        } else {
            return true;
        }
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
                Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setTitle("Change Password");
                dialog.setContentView(R.layout.custom_dialog_forgetpassword);
                initElementfordialogForgetPassword(dialog);
                dialog.show();
            }
        });
    }

    private void initElementfordialogForgetPassword(Dialog dialog) {
        final EditText edtPhoneReset = (EditText) dialog.findViewById(R.id.custom_dialog_forgetpassword_edt_phone);
        EditText edtCodeReset = (EditText) dialog.findViewById(R.id.custom_dialog_forgetpassword_edt_code);
        EditText edtNewPassword = (EditText) dialog.findViewById(R.id.custom_dialog_forgetpassword_edt_newpassword);
        TextView txtReceivCodeAgain = (TextView) dialog.findViewById(R.id.custom_dialog_forgetpassword_txt_receivcodeagain);
        final Button btnOKPhone = (Button) dialog.findViewById(R.id.custom_dialog_forgetpassword_btn_ok);
        Button btnOKCode = (Button) dialog.findViewById(R.id.custom_dialog_forgetpassword_btn_okcode);
        Button btnFinish = (Button) dialog.findViewById(R.id.custom_dialog_forgetpassword_btn_finish);
        edtPhoneReset.setText(edtPhone.getText().toString());
        if (edtPhoneReset.getText().toString().length() == 0) {
            btnOKPhone.setVisibility(View.GONE);
        }
        edtCodeReset.setVisibility(View.GONE);
        edtNewPassword.setVisibility(View.GONE);
        txtReceivCodeAgain.setVisibility(View.GONE);
        btnOKCode.setVisibility(View.GONE);
        btnFinish.setVisibility(View.GONE);

        edtPhoneReset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtPhoneReset.getText().toString().length() == 0) {
                    btnOKPhone.setVisibility(View.GONE);
                } else {
                    btnOKPhone.setVisibility(View.VISIBLE);
                }
            }
        });
        clickButtonOKPhoneinDialogForgetPassword(btnOKPhone, edtPhoneReset, edtCodeReset, txtReceivCodeAgain);
        changeDataEditTextCodeinDialogForgetPassword(edtCodeReset, btnOKCode);
        clickButtonbtnOKCodeinDialogForgetPassword(btnOKCode, edtNewPassword, edtCodeReset, txtReceivCodeAgain);
        changeDataEditTextNewPassinDialogForgetPassword(edtNewPassword, btnFinish);
        clickButtonbtnFinishinDialogForgetPassword(dialog, edtNewPassword, btnFinish, edtPhoneReset);
    }

    private void clickButtonOKPhoneinDialogForgetPassword(final Button btnOKPhone, final EditText edtPhoneReset,
                                                          final EditText edtCodeReset, final TextView txtReceivCodeAgain) {
        btnOKPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect connect = new Connect();
                Call<Status> checkPhoneExits = connect.getRetrofit().checkPhoneExits(edtPhoneReset.getText().toString());
                checkPhoneExits.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if (response.body().getStatus().equals("this phone is exits") == true) {
                            edtCodeReset.setVisibility(View.VISIBLE);
                            txtReceivCodeAgain.setVisibility(View.VISIBLE);
                            edtPhoneReset.setVisibility(View.GONE);
                            btnOKPhone.setVisibility(View.GONE);
                        } else {
                            edtPhoneReset.setError("This phone is exits. Click Register for create your account.");
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void changeDataEditTextCodeinDialogForgetPassword(final EditText edtCodeReset, final Button btnOKCode) {
        edtCodeReset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtCodeReset.getText().toString().length() != 0) {
                    btnOKCode.setVisibility(View.VISIBLE);
                } else {
                    btnOKCode.setVisibility(View.GONE);
                }
            }
        });
    }


    //IN HERE, HAVEN'T METHOD CHECK CODE
    private void clickButtonbtnOKCodeinDialogForgetPassword(final Button btnOKCode, final EditText edtNewPassword,
                                                            final EditText edtCodeReset, final TextView txtReceivCodeAgain) {
        btnOKCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check code user input in edtCodeReset.
                //if incorrect
                //edtCodeReset.setError("Code incorrect. Inputcode again")
                //if correct
                edtCodeReset.setVisibility(View.GONE);
                txtReceivCodeAgain.setVisibility(View.GONE);
                btnOKCode.setVisibility(View.GONE);
                edtNewPassword.setVisibility(View.VISIBLE);
            }
        });
    }

    private void changeDataEditTextNewPassinDialogForgetPassword(final EditText edtNewPassword, final Button btnFinish) {
        edtNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtNewPassword.getText().toString().length() != 0) {
                    btnFinish.setVisibility(View.VISIBLE);
                } else {
                    btnFinish.setVisibility(View.GONE);
                }
            }
        });
    }

    private void clickButtonbtnFinishinDialogForgetPassword(final Dialog dialog, final EditText edtNewPassword, final Button btnFinish, final EditText edtPhoneReset) {
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNewPassword.getText().toString().length() >= 6) {
                    Connect connect = new Connect();
                    Log.e("Phone Reset", edtPhoneReset.getText().toString() + "--------------------------");
                    Log.e("New Password", edtNewPassword.getText().toString() + "************************");
                    Call<Status> changePass = connect.getRetrofit().changePass(edtPhoneReset.getText().toString(), edtNewPassword.getText().toString());
                    changePass.enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            if (response.body().getStatus().equals("Update password success") == true) {
                                saveReference("phoneLogin", edtPhoneReset.getText().toString());
                                saveReference("passwordLogin", edtNewPassword.getText().toString());
                                dialog.cancel();
                                edtPhone.setText(storeReference("phoneLogin"));
                                edtPassword.setText(storeReference("passwordLogin"));
                            } else {
                                edtNewPassword.setError("Update password error");
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {

                        }
                    });
                } else {
                    edtNewPassword.setError("Password is longer than 6 character");
                }
            }
        });
    }

    public void clickLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEntry(edtPhone.getText().toString()) == true) {
                    edtPhone.setError("Invalid Phone");
                }
                if (checkEntry(edtPassword.getText().toString()) == true) {
                    edtPassword.setError("Invalid Password");
                }
                if (checkEntry(edtPhone.getText().toString()) == false && checkEntry(edtPassword.getText().toString()) == false) {
                    checkAccount();
                }
            }
        });
    }
}