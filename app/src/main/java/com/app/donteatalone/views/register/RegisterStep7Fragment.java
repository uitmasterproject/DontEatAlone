package com.app.donteatalone.views.register;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InitParam;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.login.LoginActivity;

import org.apache.commons.lang3.StringEscapeUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.donteatalone.R.array.character;
import static com.app.donteatalone.R.string.invalid_network;


/**
 * Created by ChomChom on 4/10/2017
 */

public class RegisterStep7Fragment extends Fragment {
    private View view;
    private ViewPager _mViewPager;
    private MultiAutoCompleteTextView mactvCharacter;
    private MultiAutoCompleteTextView mactvStyle;
    private MultiAutoCompleteTextView mactvFood;
    private Button btnNextStep;
    private LinearLayout llRoot;
    private BaseProgress dialog;

    public static Fragment newInstance() {
        return new RegisterStep7Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_step7, container, false);
        init();
        llRootTouch();
        setActvHobby();
        clickButtonNextStep();
        return view;
    }

    private void init() {
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        mactvCharacter = (MultiAutoCompleteTextView) view.findViewById(R.id.fragment_register_step7_actv_character);
        mactvCharacter.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mactvStyle = (MultiAutoCompleteTextView) view.findViewById(R.id.fragment_register_step7_actv_style);
        mactvStyle.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mactvFood = (MultiAutoCompleteTextView) view.findViewById(R.id.fragment_register_step7_actv_food);
        mactvFood.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        btnNextStep = (Button) view.findViewById(R.id.fragment_register_step7_btn_register);
        llRoot = (LinearLayout) view.findViewById(R.id.fragment_register_step7_ll_root);
        dialog = new BaseProgress();
    }

    private void setActvHobby() {
        ArrayAdapter hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(character));
        mactvCharacter.setAdapter(hobbyAdapter);
        mactvCharacter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mactvCharacter.getText().toString().endsWith(",")) {
                    mactvCharacter.setText(mactvCharacter.getText().toString().trim() + ",");
                }
                mactvCharacter.setSelection(mactvCharacter.getText().toString().length());
            }
        });
        hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.style));
        mactvStyle.setAdapter(hobbyAdapter);
        mactvStyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mactvStyle.getText().toString().endsWith(",")) {
                    mactvStyle.setText(mactvStyle.getText().toString().trim() + ",");
                }
                mactvStyle.setSelection(mactvStyle.getText().toString().length());
            }
        });
        hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.food));
        mactvFood.setAdapter(hobbyAdapter);
        mactvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mactvFood.getText().toString().endsWith(",")) {
                    mactvFood.setText(mactvFood.getText().toString().trim() + ",");
                }
                mactvFood.setSelection(mactvFood.getText().toString().length());
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

    private void clickButtonNextStep() {
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringCharacter = AppUtils.notRepeatElementInString(mactvCharacter.getText().toString().trim());
                String stringStyle = AppUtils.notRepeatElementInString(mactvStyle.getText().toString().trim());
                String stringFood = AppUtils.notRepeatElementInString(mactvFood.getText().toString().trim());
                if (stringCharacter.trim().endsWith(",")) {
                    mactvCharacter.setText(stringCharacter.trim().substring(0, stringCharacter.lastIndexOf(",")));
                }

                if (stringStyle.trim().endsWith(",")) {
                    mactvStyle.setText(stringStyle.substring(0, stringStyle.trim().lastIndexOf(",")));
                }

                if (stringFood.trim().endsWith(",")) {
                    mactvFood.setText(stringFood.substring(0, stringFood.trim().lastIndexOf(",")));
                }
                RegisterStep1Fragment.userName.setTargetCharacter(StringEscapeUtils.escapeJava(mactvCharacter.getText().toString()));
                RegisterStep1Fragment.userName.setTargetStyle(StringEscapeUtils.escapeJava(mactvStyle.getText().toString()));
                RegisterStep1Fragment.userName.setTargetFood(StringEscapeUtils.escapeJava(mactvFood.getText().toString()));
                InsertUserIntoDB();
            }
        });
    }

    private void InsertUserIntoDB() {
        if (!AppUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), getResources().getString(invalid_network), Toast.LENGTH_SHORT).show();
        } else {
            dialog.showProgressLoading(getActivity());

            Call<InitParam> init = Connect.getRetrofit().getInitParam(RegisterStep1Fragment.userName.getPhone());
            init.enqueue(new Callback<InitParam>() {
                @Override
                public void onResponse(Call<InitParam> call, Response<InitParam> response) {
                    if (response != null) {
                        String password = AppUtils.encrypt(response.body().getInitParam(), RegisterStep1Fragment.userName.getPassword());

                        RegisterStep1Fragment.userName.setPassword(password);

                        Call<Status> insertUser = Connect.getRetrofit().insertUser(RegisterStep1Fragment.userName);
                        insertUser.enqueue(new Callback<Status>() {
                            @Override
                            public void onResponse(Call<Status> call, Response<Status> response) {
                                dialog.hideProgressLoading();
                                if (response.body() != null) {
                                    if (response.body().getStatus().equals("0")) {
                                        new MySharePreference(getActivity()).saveAccountInfo(RegisterStep1Fragment.userName);
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getActivity(), getResources().getString(R.string.register_fail), Toast.LENGTH_SHORT).show();
                                        _mViewPager.setCurrentItem(0);
                                    }
                                } else {
                                    Toast.makeText(getActivity(), getResources().getString(invalid_network), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Status> call, Throwable t) {
                                dialog.hideProgressLoading();
                                Toast.makeText(getActivity(), getResources().getString(invalid_network), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<InitParam> call, Throwable t) {
                    dialog.hideProgressLoading();
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
