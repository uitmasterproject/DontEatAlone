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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by ChomChom on 4/7/2017
 */

public class RegisterStep6Fragment extends Fragment {
    private MultiAutoCompleteTextView mactvCharacter;
    private MultiAutoCompleteTextView mactvStyle;
    private Button btnNext;
    private View view;
    private ViewPager _mViewPager;
    private LinearLayout llRoot;

    public static Fragment newInstance() {
        RegisterStep6Fragment f = new RegisterStep6Fragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_step6, container, false);
        init();
        llRootTouch();
        setActvHobby();
        rlNextClick();
        return view;
    }

    private void init() {
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        mactvCharacter = (MultiAutoCompleteTextView) view.findViewById(R.id.fragment_register_step6_mactv_my_character);
        mactvCharacter.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mactvStyle = (MultiAutoCompleteTextView) view.findViewById(R.id.fragment_register_step6_mactv_my_style);
        mactvStyle.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        btnNext = (Button) view.findViewById(R.id.fragment_register_step6_btn_next);
        llRoot = (LinearLayout) view.findViewById(R.id.fragment_register_step6_ll_root);
    }

    private void setActvHobby() {
        ArrayAdapter hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.character));
        mactvCharacter.setAdapter(hobbyAdapter);
        mactvCharacter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mactvCharacter.setSelection(mactvCharacter.getText().toString().length());
            }
        });
        hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.style));
        mactvStyle.setAdapter(hobbyAdapter);
        mactvStyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mactvStyle.setSelection(mactvStyle.getText().toString().length());
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
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringCharacter = AppUtils.notRepeatElementInString(mactvCharacter.getText().toString().trim());
                String stringStyle = AppUtils.notRepeatElementInString(mactvStyle.getText().toString().trim());

                if (stringCharacter.trim().endsWith(",")) {
                    mactvCharacter.setText(stringCharacter.substring(0, stringCharacter.lastIndexOf(",")));
                }

                if (stringStyle.trim().endsWith(",")) {
                    mactvStyle.setText(stringStyle.substring(0, stringStyle.lastIndexOf(",")));
                }
                RegisterStep1Fragment.userName.setMyCharacter(StringUtils.capitalize(StringEscapeUtils.escapeJava(mactvCharacter.getText().toString())));
                RegisterStep1Fragment.userName.setMyStyle(StringUtils.capitalize(StringEscapeUtils.escapeJava(mactvStyle.getText().toString())));
                _mViewPager.setCurrentItem(6, true);
            }
        });
    }
}
