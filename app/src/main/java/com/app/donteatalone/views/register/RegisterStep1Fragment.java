package com.app.donteatalone.views.register;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    public static UserName userName;

    private View view;

    private ViewPager _mViewPager;
    private TextView tvVerifyCode;
    private Button btnSendCode, btnNext;
    private EditText edtCode, edtPhone;
    private TextInputLayout tilErrorCode, tilErrorPhone;

    private LinearLayout llRoot;

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
        view = inflater.inflate(R.layout.fragment_register_step1, container, false);

        init();

        llRootTouch();

        changeDataEdtPhone();

        rlSendCodeClick();

        rlNextClick();

        return view;
    }

    public void init() {
        llRoot = (LinearLayout) view.findViewById(R.id.fragment_register_step1_ll_root);

        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);

        btnSendCode = (Button) view.findViewById(R.id.fragment_register_step1_btn_send_code);
        btnNext = (Button) view.findViewById(R.id.fragment_register_step1_btn_next);

        edtPhone = (EditText) view.findViewById(R.id.fragment_register_step1_edt_phone);
        edtCode = (EditText) view.findViewById(R.id.fragment_register_step1_edt_code);

        tilErrorPhone = (TextInputLayout) view.findViewById(R.id.til_error_phone);
        tilErrorCode = (TextInputLayout) view.findViewById(R.id.til_error_code);

        tvVerifyCode = (TextView) view.findViewById(R.id.fragment_register_step1_tutorial_verify_code);

        userName = new UserName(getActivity());

        progressDialog = new BaseProgress();
    }

    public void changeDataEdtPhone() {
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tilErrorPhone.setError(null);
                tilErrorPhone.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvVerifyCode.setVisibility(View.INVISIBLE);
                edtCode.setVisibility(View.INVISIBLE);
                tilErrorCode.setErrorEnabled(false);
                btnNext.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void checkExitsPhone() {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            progressDialog.showProgressLoading(getActivity());
            Call<Status> checkPhone = Connect.getRetrofit().checkPhoneExits(edtPhone.getText().toString());
            checkPhone.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    if (response.body() != null) {
                        progressDialog.hideProgressLoading();
                        if (response.body().getStatus().equals("0")) {
                            tvVerifyCode.setVisibility(View.VISIBLE);
                            edtCode.setVisibility(View.VISIBLE);
                            btnNext.setVisibility(View.VISIBLE);
                        } else {
                            edtPhone.setText("");
                            tilErrorPhone.setErrorEnabled(true);
                            tilErrorPhone.setError(getString(R.string.phone_exists));
                        }
                    }else{
                        progressDialog.hideProgressLoading();
                    }
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    progressDialog.hideProgressLoading();
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressDialog.hideProgressLoading();
            Toast.makeText(getActivity(), getResources().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
        }
    }

    /* hide soft keyboard when touch outsite edittext*/
    private void llRootTouch() {
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.touchOutsideHideSoftKeyboard(getActivity());
                }
            });
    }

    private void rlSendCodeClick() {
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideSoftKeyboard(getActivity());
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    tilErrorPhone.setError(getString(R.string.empty_phone));
                } else if (edtPhone.getText().toString().length() < 10 ||
                        edtPhone.getText().toString().length() > 11 ||
                        (!edtPhone.getText().toString().startsWith("0") &&
                        !edtPhone.getText().toString().startsWith("+84"))) {
                    tilErrorPhone.setError(getString(R.string.invalid_phone));
                } else {
                    checkExitsPhone();
                }
            }
        });
    }

    //IN HERE, HAVEN'T METHOD CHECK CODE
    private void rlNextClick() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtCode.getText().toString())) {
                    tilErrorCode.setError(getString(R.string.empty_verify));
                } else {
                    userName.setPhone(edtPhone.getText().toString());
                    _mViewPager.setCurrentItem(1, true);
                }
            }
        });
    }
}
