package com.app.donteatalone.views.register;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;

import static com.app.donteatalone.views.register.RegisterStep1Fragment.userName;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterStep2Fragment extends Fragment {
    private ViewGroup viewGroup;
    private EditText edtFullName, edtPassword;
    private RelativeLayout rlNext;
    private ViewPager _mViewPager;
    private RelativeLayout rlClose;
    private LinearLayout llRoot;

    public static Fragment newInstance() {
        return new RegisterStep2Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_register_step2, null);
        init();
        llRootTouch();
        clickButtonNextStep();
        rlCloseClick();
        return viewGroup;
    }

    public void init(){
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        edtFullName= (EditText) viewGroup.findViewById(R.id.fragment_register_step2_edt_fullname);
        edtPassword=(EditText) viewGroup.findViewById(R.id.fragment_register_step2_edt_password);
        rlNext=(RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step2_btn_next);
        rlClose = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step2_close);
        llRoot = (LinearLayout) viewGroup.findViewById(R.id.fragment_register_step2_ll_root);
    }

    /*Hide keyboard when touch outsite edittext*/
    private void llRootTouch() {
        llRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideSoftKeyboard(getActivity());
                if(edtFullName.hasFocus()){
                    edtPassword.requestFocus();
                }
                else {
                    edtFullName.requestFocus();
                }
                return true;
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
    private void clickButtonNextStep(){
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtFullName.getText().toString().equals("") ||
                        edtPassword.getText().toString().equals("")) {
                    if (edtFullName.getText().toString().equals("")) {
                        edtFullName.setError("Full Name field not entry");
                    }
                    if (edtPassword.getText().toString().equals("")) {
                        edtPassword.setError("Password field not entry");
                    }
                } else {
                    if(edtPassword.getText().toString().length()<6){
                        edtPassword.setError("Length password is longer 6 character");
                    } else {
                        userName.setFullName(AppUtils.convertStringToNFD(edtFullName.getText().toString()));
                        userName.setPassword(edtPassword.getText().toString());
                        _mViewPager.setCurrentItem(2,true);
                    }
                }
            }
        });
    }
}
