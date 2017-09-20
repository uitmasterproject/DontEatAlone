package com.app.donteatalone.views.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;

/**
 * Created by ChomChom on 4/7/2017.
 */

public class RegisterStep6Fragment extends Fragment {
    private MultiAutoCompleteTextView actvFood;
    private MultiAutoCompleteTextView actvCharacter;
    private MultiAutoCompleteTextView actvStyle;
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
        actvFood = (MultiAutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step6_actv_food);
        actvFood.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        actvCharacter = (MultiAutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step6_actv_character);
        actvCharacter.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        actvStyle = (MultiAutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step6_actv_style);
        actvStyle.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        rlNext = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step6_btn_next);
        rlClose = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step6_close);
        llRoot = (LinearLayout) viewGroup.findViewById(R.id.fragment_register_step6_ll_root);
    }

    private void setActvHobby() {
        ArrayAdapter hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.food));
        actvFood.setAdapter(hobbyAdapter);
        actvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actvFood.setText(AppUtils.convertStringToNFD(actvFood.getText().toString()));
                actvFood.setSelection(actvFood.getText().toString().length());
            }
        });
        hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.character));
        actvCharacter.setAdapter(hobbyAdapter);
        actvCharacter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actvCharacter.setText(AppUtils.convertStringToNFD(actvCharacter.getText().toString()));
                actvCharacter.setSelection(actvCharacter.getText().toString().length());
            }
        });
        hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.style));
        actvStyle.setAdapter(hobbyAdapter);
        actvStyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actvStyle.setText(AppUtils.convertStringToNFD(actvStyle.getText().toString()));
                actvStyle.setSelection(actvStyle.getText().toString().length());
            }
        });

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
                if (actvFood.getText().toString().trim().endsWith(",")) {
                    actvFood.setText(actvFood.getText().toString().substring(0, actvFood.getText().toString().lastIndexOf(",")));
                }

                if (actvCharacter.getText().toString().trim().endsWith(",")) {
                    actvCharacter.setText(actvCharacter.getText().toString().substring(0, actvCharacter.getText().toString().lastIndexOf(",")));
                }

                if (actvStyle.getText().toString().trim().endsWith(",")) {
                    actvStyle.setText(actvStyle.getText().toString().substring(0, actvStyle.getText().toString().lastIndexOf(",")));
                }
                RegisterStep1Fragment.userName.setHobby(actvFood.getText().toString() + "," + actvCharacter.getText().toString() + "," + actvStyle.getText().toString());
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
