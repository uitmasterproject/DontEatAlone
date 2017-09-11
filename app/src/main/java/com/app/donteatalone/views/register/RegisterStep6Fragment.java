package com.app.donteatalone.views.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;

/**
 * Created by ChomChom on 4/7/2017.
 */

public class RegisterStep6Fragment extends Fragment {
    private AutoCompleteTextView actvFood;
    private AutoCompleteTextView actvCharacter;
    private AutoCompleteTextView actvStyle;
    private RelativeLayout rlNext, rlClose;
    private View viewGroup;
    private ViewPager _mViewPager;
    private LinearLayout llRoot;

    public static Fragment newInstance() {
        RegisterStep6Fragment f = new RegisterStep6Fragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_register_step6, null);
        init();
        llRootTouch();
        setActvHobby();
        rlNextClick();
        rlCloseClick();
        return viewGroup;
    }

    private void init() {
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        actvFood = (AutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step6_actv_food);
        actvCharacter = (AutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step6_actv_character);
        actvStyle = (AutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step6_actv_style);
        rlNext = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step6_btn_next);
        rlClose = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step6_close);
        llRoot = (LinearLayout) viewGroup.findViewById(R.id.fragment_register_step6_ll_root);
    }

    private void setActvHobby() {
        ArrayAdapter hobbyAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.food));
        actvFood.setAdapter(hobbyAdapter);
        hobbyAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.character));
        actvCharacter.setAdapter(hobbyAdapter);
        hobbyAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.style));
        actvStyle.setAdapter(hobbyAdapter);

    }

    /*Hide softkeyboard when touch outsite edittext*/
    private void llRootTouch() {
        llRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideSoftKeyboard(getActivity());
                return true;
            }
        });
    }

    private void rlNextClick() {
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actvFood.getText().toString().endsWith(",")) {
                    actvFood.setText(actvFood.getText().toString().substring(0, actvFood.getText().toString().length() - 1));
                }

                if(actvCharacter.getText().toString().endsWith(",")){
                    actvCharacter.setText(actvCharacter.getText().toString().substring(0, actvCharacter.getText().toString().length() - 1));
                }

                if(actvStyle.getText().toString().endsWith(",")){
                    actvStyle.setText(actvStyle.getText().toString().substring(0, actvStyle.getText().toString().length() - 1));
                }
                RegisterStep1Fragment.userName.setHobby(actvFood.getText().toString()+","+actvCharacter.getText().toString()+","+actvStyle.getText().toString());
                _mViewPager.setCurrentItem(6, true);
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
