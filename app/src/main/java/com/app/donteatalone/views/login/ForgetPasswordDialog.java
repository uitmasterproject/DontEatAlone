package com.app.donteatalone.views.login;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 10-Sep-17
 */

public class ForgetPasswordDialog {
    private Dialog dialog;
    private Activity activity;
    private BaseProgress baseProgress;
    public ForgetPasswordDialog(Activity activity){
        this.activity=activity;
        baseProgress=new BaseProgress();
        dialog=new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void showDialog(EditText edtPass){
        if(activity!=null){
            dialog.setContentView(R.layout.custom_dialog_forgetpassword);
            RelativeLayout relativeLayout=(RelativeLayout) dialog.findViewById(R.id.custom_dialog_forgetpassword_rl_close);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            sendCode();
            changePassword();
            finishNewPassword(edtPass);
            dialog.show();
        }
    }

    private void sendCode(){
        LinearLayout llContainerPhone=(LinearLayout) dialog.findViewById(R.id.custom_dialog_forgetpassword_ll_container_phone);
        LinearLayout llContainerCode=(LinearLayout) dialog.findViewById(R.id.custom_dialog_forgetpassword_ll_container_code);
        EditText edtPhone = (EditText) dialog.findViewById(R.id.custom_dialog_forgetpassword_edt_phone);
        Button btnReceiveCode=(Button) dialog.findViewById(R.id.custom_dialog_forgetpassword_btn_ok);

        changeEditText(edtPhone);

        edtPhone.setText(new MySharePreference(activity).getValue("phoneLogin"));
        btnReceiveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPhone.getText().toString().equals("")){
                    edtPhone.setError("Phone field isn't entry");
                }
                else {
                    baseProgress.showProgressLoading(activity);
                    Call<Status> checkPhoneExist= Connect.getRetrofit().checkPhoneExits(edtPhone.getText().toString());
                    checkPhoneExist.enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            if(response.body()!=null){
                                baseProgress.hideProgressLoading();
                                if(AppUtils.isNull(response.body().getStatus()).equals("1")){
                                    //Send code
                                    llContainerPhone.setVisibility(View.GONE);
                                    llContainerCode.setVisibility(View.VISIBLE);
                                }
                                else {
                                    edtPhone.setError("This phone isn't register");
                                }
                            }else {
                                baseProgress.hideProgressLoading();
                                Toast.makeText(activity,activity.getResources().getString(R.string.invalid_network),Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {
                            baseProgress.hideProgressLoading();
                            Toast.makeText(activity,activity.getResources().getString(R.string.invalid_network),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void changePassword(){
        LinearLayout llContainerCode=(LinearLayout) dialog.findViewById(R.id.custom_dialog_forgetpassword_ll_container_code);
        EditText edtCode = (EditText) dialog.findViewById(R.id.custom_dialog_forgetpassword_edt_code);
        TextView txtResendCode=(TextView) dialog.findViewById(R.id.custom_dialog_forgetpassword_txt_receivecodeagain);
        Button btnChangePassword=(Button) dialog.findViewById(R.id.custom_dialog_forgetpassword_btn_okcode);
        LinearLayout llContainerFinish=(LinearLayout) dialog.findViewById(R.id.custom_dialog_forgetpassword_ll_container_newpassword);

        changeEditText(edtCode);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtCode.getText().toString().equals("")){
                    edtCode.setError("Input Code");
                }else {
                    llContainerCode.setVisibility(View.GONE);
                    llContainerFinish.setVisibility(View.VISIBLE);
                }
            }
        });

        txtResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Resend Code
            }
        });
    }

    private void finishNewPassword(EditText edtPass){
        EditText edtNewPassword= (EditText) dialog.findViewById(R.id.custom_dialog_forgetpassword_edt_newpassword);
        Button btnFinish=(Button) dialog.findViewById(R.id.custom_dialog_forgetpassword_btn_finish);

        changeEditText(edtNewPassword);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNewPassword.getText().toString().length()<6){
                    edtNewPassword.setError("Password field must longer than 6 character");
                }else {
                    Call<Status> changePassword=Connect.getRetrofit().changePass(new MySharePreference(activity).getValue("phoneLogin"),edtNewPassword.getText().toString());
                    changePassword.enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            if(response.body()!=null){
                                if(AppUtils.isNull(response.body().getStatus()).equals("1")){
                                    edtPass.setText(edtNewPassword.getText().toString());
                                    new MySharePreference(activity).setValue("phoneLogin",edtNewPassword.getText().toString());
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(activity,activity.getResources().getString(R.string.invalid_network),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {
                            Toast.makeText(activity,activity.getResources().getString(R.string.invalid_network),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void changeEditText(EditText edt){
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edt.getText().toString().length()>0){
                    edt.setError(null);
                }
            }
        });
    }
}
