package com.app.donteatalone.views.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.donteatalone.R;
import com.app.donteatalone.model.Hobby;
import com.app.donteatalone.utils.AppUtils;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ChomChom on 4/7/2017.
 */

public class RegisterStep6Fragment extends Fragment {
    private AutoCompleteTextView actvHobby;
    private ArrayList<String> headerHobby;
    private ArrayList<Hobby> hobbies;
    private RelativeLayout rlNext, rlClose;
    private View viewGroup;
    private ViewPager _mViewPager;
    private LinearLayout llRoot;

    public static Fragment newInstance(Context context) {
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
        actvHobby = (AutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step6_actv_hobby);
        rlNext = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step6_btn_next);
        rlClose = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step6_close);
        llRoot = (LinearLayout) viewGroup.findViewById(R.id.fragment_register_step6_ll_root);
    }

    private void setActvHobby() {
        clonebdHobby();
        CustomAdapterCompleteTextView customAdapterCompleteTextView = new CustomAdapterCompleteTextView(this.getContext(), android.R.layout.simple_dropdown_item_1line, headerHobby, hobbies, actvHobby);
        actvHobby.setAdapter(customAdapterCompleteTextView);

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
                if (actvHobby.getText().toString().endsWith(",")) {
                    actvHobby.setText(actvHobby.getText().toString().substring(0, actvHobby.getText().toString().length() - 1));
                }
                RegisterStep1Fragment.userName.setHobby(actvHobby.getText().toString());
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

    private void clonebdHobby() {
        headerHobby = new ArrayList<>();
        headerHobby.add("Món ăn");
        headerHobby.add("Tính cách");
        headerHobby.add("Phong cách");

        hobbies = new ArrayList<>();
        hobbies.add(new Hobby("Món ăn", "", true));
        hobbies.add(new Hobby("Món ăn", "các món từ gà, gà rán, gà nướng, gà quay...", false));
        hobbies.add(new Hobby("Món ăn", "các loại lẩu", false));
        hobbies.add(new Hobby("Món ăn", "đồ xiên que", false));
        hobbies.add(new Hobby("Món ăn", "đồ ăn liền", false));
        hobbies.add(new Hobby("Món ăn", "các món ngọt", false));
        hobbies.add(new Hobby("Món ăn", "phở, bún, bánh canh cua...", false));

        hobbies.add(new Hobby("Tính cách", "", true));
        hobbies.add(new Hobby("Tính cách", "vui vẻ", false));
        hobbies.add(new Hobby("Tính cách", "trầm tĩnh", false));
        hobbies.add(new Hobby("Tính cách", "hóm hĩnh", false));
        hobbies.add(new Hobby("Tính cách", "thoải mái", false));

        hobbies.add(new Hobby("Phong cách", "", true));
        hobbies.add(new Hobby("Phong cách", "tự do", false));
        hobbies.add(new Hobby("Phong cách", "quái dị", false));
        hobbies.add(new Hobby("Phong cách", "trưởng thành", false));
    }
}
