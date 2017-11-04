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
 * Created by ChomChom on 4/7/2017
 */

public class RegisterStep6Fragment extends Fragment {
    private MultiAutoCompleteTextView mactvCharacter;
    private MultiAutoCompleteTextView mactvStyle;
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
        mactvCharacter = (MultiAutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step6_mactv_my_character);
        mactvCharacter.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mactvStyle = (MultiAutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step6_mactv_my_style);
        mactvStyle.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        rlNext = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step6_btn_next);
        rlClose = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step6_close);
        llRoot = (LinearLayout) viewGroup.findViewById(R.id.fragment_register_step6_ll_root);
    }

    private void setActvHobby() {
        ArrayAdapter hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.character));
        mactvCharacter.setAdapter(hobbyAdapter);
        mactvCharacter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mactvCharacter.setText(AppUtils.convertStringToNFD(mactvCharacter.getText().toString()));
                mactvCharacter.setSelection(mactvCharacter.getText().toString().length());
            }
        });
        hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.style));
        mactvStyle.setAdapter(hobbyAdapter);
        mactvStyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mactvStyle.setText(AppUtils.convertStringToNFD(mactvStyle.getText().toString()));
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
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mactvCharacter.getText().toString().trim().endsWith(",")) {
                    mactvCharacter.setText(mactvCharacter.getText().toString().substring(0, mactvCharacter.getText().toString().lastIndexOf(",")));
                }

                if (mactvStyle.getText().toString().trim().endsWith(",")) {
                    mactvStyle.setText(mactvStyle.getText().toString().substring(0, mactvStyle.getText().toString().lastIndexOf(",")));
                }
                RegisterStep1Fragment.userName.setMyCharacter(mactvCharacter.getText().toString());
                RegisterStep1Fragment.userName.setMyStyle(mactvStyle.getText().toString());
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
