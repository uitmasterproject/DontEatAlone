package com.app.donteatalone.views.register;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.model.UserName;
import com.app.donteatalone.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStep1Fragment extends Fragment {

    private ViewPager _mViewPager;
    private ViewGroup viewGroup;
    private RelativeLayout rlNext, rlSendCode, rlVerifyCode;
    private EditText edtCode, edtPhone;
    private RelativeLayout rlClose;
    private LinearLayout llRoot;
    public static UserName userName;
    private BaseProgress progressDialog;

    public static Fragment newInstance() {
        return new RegisterStep1Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_register_step1, null);
        init();
        llRootTouch();
        changeDataEdtPhone();
        rlSendCodeClick();
        rlNextClick();
        rlCloseClick();
        return viewGroup;
    }

    public void init() {
        llRoot = (LinearLayout) viewGroup.findViewById(R.id.fragment_register_step1_ll_root);
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        rlSendCode = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step1_rl_send_code);
        edtPhone = (EditText) viewGroup.findViewById(R.id.fragment_register_step1_edt_phone);
        rlNext = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step1_rl_next);
        edtCode = (EditText) viewGroup.findViewById(R.id.fragment_register_step1_edt_code);
        rlVerifyCode = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step1_tutorial_verify_code);
        rlClose = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step1_close);
        userName = new UserName(getContext());
        progressDialog=new BaseProgress();
    }

    public void changeDataEdtPhone() {
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                rlVerifyCode.setVisibility(View.INVISIBLE);
                edtCode.setVisibility(View.INVISIBLE);
                rlNext.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void checkExitsPhone() {
        progressDialog.showProgressLoading(getActivity());
        Call<Status> checkPhone = Connect.getRetrofit().checkPhoneExits(edtPhone.getText().toString());
        checkPhone.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.body()!=null) {
                    progressDialog.hideProgressLoading();
                    if (response.body().getStatus().equals("0")) {
                        rlVerifyCode.setVisibility(View.VISIBLE);
                        edtCode.setVisibility(View.VISIBLE);
                        rlNext.setVisibility(View.VISIBLE);
                        edtCode.requestFocus();
                    } else {
                        edtPhone.setError("This phone was exit");
                        edtPhone.setText("");
                    }
                }
                else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                progressDialog.hideProgressLoading();
                Toast.makeText(getActivity(), getResources().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

   /* hide soft keyboard when touch outsite edittext*/
   private void llRootTouch() {
       llRoot.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               AppUtils.hideSoftKeyboard(getActivity());
               return true;
           }
       });
   }

    private void rlSendCodeClick() {
        rlSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideSoftKeyboard(getActivity());
                if (edtPhone.getText().toString().equals("")) {
                    edtPhone.setError("Mobile Number field not entry");
                } else {
                    checkExitsPhone();
                }
            }
        });
    }

    //IN HERE, HAVEN'T METHOD CHECK CODE
    private void rlNextClick() {
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCode.getText().toString().equals("")) {
                    edtCode.setError("Verify code field not entry");
                } else {
                    userName.setPhone(edtPhone.getText().toString());
                    _mViewPager.setCurrentItem(1, true);
                }
            }
        });
    }

    private void rlCloseClick() {
        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().onBackPressed();
            }
        });
    }
}
