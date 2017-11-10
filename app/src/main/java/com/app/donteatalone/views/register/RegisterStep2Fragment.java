package com.app.donteatalone.views.register;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;

import static com.app.donteatalone.views.register.RegisterStep1Fragment.userName;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterStep2Fragment extends Fragment {
    private View view;
    private EditText edtFullName, edtPassword;
    private TextInputLayout tilErrorFullName, tilErrorPassword;
    private Button btnNext;
    private ViewPager _mViewPager;
    private LinearLayout llRoot;
    private TextWatcher errorFullName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            tilErrorFullName.setError(null);
            tilErrorFullName.setErrorEnabled(false);
            tilErrorPassword.setErrorEnabled(false);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher errorPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            tilErrorPassword.setErrorEnabled(false);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static Fragment newInstance() {
        return new RegisterStep2Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register_step2, container, false);
        init();
        llRootTouch();
        clickButtonNextStep();
        return view;
    }

    public void init() {
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);

        edtFullName = (EditText) view.findViewById(R.id.fragment_register_step2_edt_full_name);
        edtPassword = (EditText) view.findViewById(R.id.fragment_register_step2_edt_password);

        tilErrorFullName = (TextInputLayout) view.findViewById(R.id.til_error_full_name);
        tilErrorPassword = (TextInputLayout) view.findViewById(R.id.til_error_password);

        edtFullName.addTextChangedListener(errorFullName);
        edtPassword.addTextChangedListener(errorPassword);

        btnNext = (Button) view.findViewById(R.id.fragment_register_step2_btn_next);
        llRoot = (LinearLayout) view.findViewById(R.id.fragment_register_step2_ll_root);
    }

    /*Hide keyboard when touch outsite edittext*/
    private void llRootTouch() {
        llRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideSoftKeyboard(getActivity());
                if (edtFullName.hasFocus()) {
                    edtPassword.requestFocus();
                } else {
                    edtFullName.requestFocus();
                }
                return true;
            }
        });
    }

    private void clickButtonNextStep() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtFullName.getText().toString()) ||
                        TextUtils.isEmpty(edtPassword.getText().toString())) {
                    if (TextUtils.isEmpty(edtFullName.getText().toString())) {
                        tilErrorFullName.setError("Full Name field not entry");
                    }
                    if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                        tilErrorPassword.setError("Password field not entry");
                    }
                } else {
                    if (edtPassword.getText().toString().length() < 6) {
                        tilErrorPassword.setError("Length password is longer 6 character");
                    } else {
                        userName.setFullName(AppUtils.convertStringToNFD(edtFullName.getText().toString()));
                        userName.setPassword(edtPassword.getText().toString());
                        _mViewPager.setCurrentItem(2, true);
                    }
                }
            }
        });
    }
}
