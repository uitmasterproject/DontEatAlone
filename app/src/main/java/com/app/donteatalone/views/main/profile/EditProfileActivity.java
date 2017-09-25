package com.app.donteatalone.views.main.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.app.donteatalone.R;
import com.google.android.gms.gcm.Task;

/**
 * Created by Delbert on 9/23/2017.
 */

public class EditProfileActivity extends AppCompatActivity {

    private RelativeLayout rlCancel, rlSave;
    private ImageView ivAvatar;
    private LinearLayout llEditAvatar, llGender, llDoB, llDobCustomize;
    private EditText etName;
    private RadioGroup radioGroupGender;
    private AppCompatRadioButton rbMale, rbFemale;
    private WheelPicker wpDobMonths, wpDobDays;
    private WheelYearPicker wpDobYears;
    private MultiAutoCompleteTextView mactvCharacters, mactvStyles, mactvFoods, mactvTargetCharacters, mactvTargetStyles;
    private Animation animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
        itemClick();
    }

    private void init() {
        /*Toolbar*/
        rlCancel = (RelativeLayout) findViewById(R.id.activity_edit_profile_rl_cancel);
        rlSave = (RelativeLayout) findViewById(R.id.activity_edit_profile_rl_save);
        /*Personal Information*/
        ivAvatar = (ImageView) findViewById(R.id.activity_edit_profile_iv_avatar);
        llEditAvatar = (LinearLayout) findViewById(R.id.activity_edit_profile_ll_edit_avatar);
        etName = (EditText) findViewById(R.id.activity_edit_profile_et_name);
            /*Gender - show and hide*/
        llGender = (LinearLayout) findViewById(R.id.activity_edit_profile_ll_gender);
        radioGroupGender = (RadioGroup) findViewById(R.id.activity_edit_profile_radio_group);
        rbMale = (AppCompatRadioButton) findViewById(R.id.activity_edit_profile_radio_male);
        rbFemale = (AppCompatRadioButton) findViewById(R.id.activity_edit_profile_radio_female);
            /*Day of Birth - show and hide*/
        llDoB = (LinearLayout) findViewById(R.id.activity_edit_profile_ll_dob);
        llDobCustomize = (LinearLayout) findViewById(R.id.activity_edit_profile_ll_dob_customize);
        wpDobDays = (WheelPicker) findViewById(R.id.activity_edit_profile_wheelpicker_dob_days);
        wpDobMonths = (WheelPicker) findViewById(R.id.activity_edit_profile_wheelpicker_dob_months);
        wpDobYears = (WheelYearPicker) findViewById(R.id.activity_edit_profile_wheelpicker_dob_years);
        /*More Information*/
        mactvCharacters = (MultiAutoCompleteTextView) findViewById(R.id.activity_edit_profile_mactv_characters);
        mactvStyles = (MultiAutoCompleteTextView) findViewById(R.id.activity_edit_profile_mactv_styles);
        mactvFoods = (MultiAutoCompleteTextView) findViewById(R.id.activity_edit_profile_mactv_foods);
        /*Target's information*/
        mactvTargetCharacters = (MultiAutoCompleteTextView) findViewById(R.id.activity_edit_profile_mactv_target_characters);
        mactvTargetStyles = (MultiAutoCompleteTextView) findViewById(R.id.activity_edit_profile_mactv_target_styles);
    }

    private void itemClick() {

        /*Personal Information item*/
        /*Gender Click*/
        llGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroupGender.getVisibility() == View.GONE) {
                    radioGroupGender.animate().translationY(1f).setDuration(500);
                    radioGroupGender.setVisibility(View.VISIBLE);
                } else {
                    radioGroupGender.animate().translationY(0).setDuration(500);
                    radioGroupGender.setVisibility(View.GONE);
                }
            }
        });

        /*Dob Click*/
        llDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llDobCustomize.getVisibility() == View.GONE) {
                    llDobCustomize.setVisibility(View.VISIBLE);
                    llDobCustomize.animate().translationY(1f).setDuration(500);
                } else {
                    llDobCustomize.animate().translationY(0).setDuration(500);
                    llDobCustomize.setVisibility(View.GONE);
                }
            }
        });

        /*Toolbar item*/
        rlCancel.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*Set khi co su thay doi moi cho bat su kien click nay nhe Nga :))*/
        rlSave.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfileActivity.this, "Save changes", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
