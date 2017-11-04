package com.app.donteatalone.views.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.donteatalone.R.array.character;
import static com.app.donteatalone.R.string.invalid_network;


/**
 * Created by ChomChom on 4/10/2017.
 */

public class RegisterStep7Fragment extends Fragment {
    private View viewGroup;
    private MultiAutoCompleteTextView mactvCharacter;
    private MultiAutoCompleteTextView mactvStyle;
    private MultiAutoCompleteTextView mactvFood;
    private RelativeLayout rlNextStep, rlClose;
    private LinearLayout llRoot;
    private BaseProgress dialog;

    public static Fragment newInstance() {
        return new RegisterStep7Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_register_step7, null);
        init();
        llRootTouch();
        setActvHobby();
        clickButtonNextStep();
        rlCloseClick();
        return viewGroup;
    }

    private void init() {
        mactvCharacter = (MultiAutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step7_actv_character);
        mactvCharacter.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mactvStyle = (MultiAutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step7_actv_style);
        mactvStyle.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mactvFood = (MultiAutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step7_actv_food);
        mactvFood.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        rlNextStep = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step7_rl_register);
        rlClose = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step7_close);
        llRoot = (LinearLayout) viewGroup.findViewById(R.id.fragment_register_step7_ll_root);
        dialog = new BaseProgress();
    }

    private void setActvHobby() {
        ArrayAdapter hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(character));
        mactvCharacter.setAdapter(hobbyAdapter);
        mactvCharacter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mactvCharacter.getText().toString().endsWith(",")) {
                    mactvCharacter.setText(mactvCharacter.getText().toString().trim() + ", ");
                }
                mactvCharacter.setText(AppUtils.convertStringToNFD(mactvCharacter.getText().toString()));
                mactvCharacter.setSelection(mactvCharacter.getText().toString().length());
            }
        });
        hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.style));
        mactvStyle.setAdapter(hobbyAdapter);
        mactvStyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mactvStyle.getText().toString().endsWith(",")) {
                    mactvStyle.setText(mactvStyle.getText().toString().trim() + ", ");
                }
                mactvStyle.setText(AppUtils.convertStringToNFD(mactvStyle.getText().toString()));
                mactvStyle.setSelection(mactvStyle.getText().toString().length());
            }
        });
        hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.food));
        mactvFood.setAdapter(hobbyAdapter);
        mactvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mactvFood.getText().toString().endsWith(",")) {
                    mactvFood.setText(mactvFood.getText().toString().trim() + ", ");
                }
                mactvFood.setText(AppUtils.convertStringToNFD(mactvFood.getText().toString()));
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
        rlNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mactvCharacter.getText().toString().trim().endsWith(",")) {
                    mactvCharacter.setText(mactvCharacter.getText().toString().trim().substring(0, mactvCharacter.getText().toString().lastIndexOf(",")));
                }

                if (mactvStyle.getText().toString().trim().endsWith(",")) {
                    mactvStyle.setText(mactvStyle.getText().toString().substring(0, mactvStyle.getText().toString().trim().lastIndexOf(",")));
                }

                if (mactvFood.getText().toString().trim().endsWith(",")) {
                    mactvFood.setText(mactvFood.getText().toString().substring(0, mactvFood.getText().toString().trim().lastIndexOf(",")));
                }
                RegisterStep1Fragment.userName.setTargetCharacter(mactvCharacter.getText().toString());
                RegisterStep1Fragment.userName.setTargetStyle(mactvStyle.getText().toString());
                RegisterStep1Fragment.userName.setTargetFood(mactvFood.getText().toString());
                new MySharePreference(getActivity()).saveAccountInfo(RegisterStep1Fragment.userName);
                InsertUserintoDB();
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

    private void InsertUserintoDB() {
        if (!AppUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), getResources().getString(invalid_network), Toast.LENGTH_SHORT).show();
        } else {
            dialog.showProgressLoading(getActivity());
            Call<Status> insertUser = Connect.getRetrofit().insertUser(RegisterStep1Fragment.userName);
            insertUser.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    dialog.hideProgressLoading();
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("0")) {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.register_fail), Toast.LENGTH_SHORT).show();
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
}
