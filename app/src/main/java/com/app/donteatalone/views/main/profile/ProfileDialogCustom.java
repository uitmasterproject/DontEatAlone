package com.app.donteatalone.views.main.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Le Hoang Han on 5/21/2017
 */

public class ProfileDialogCustom {
    private Context context;
    private int intLayout;
    private TextView textView;
    private RelativeLayout rlClose, rlDone;

    //For hobby
    private MultiAutoCompleteTextView actvHobbyAboutFood;
    private MultiAutoCompleteTextView actvHobbyAboutCharacter;
    private MultiAutoCompleteTextView actvHobbyAboutStyle;

    //Check infor from offRequireFragment?

    private MySharePreference accoutSharePreference;
    private MySharePreference infoRequireSharePreference;


    public ProfileDialogCustom(Context _context, int _intLayout, TextView _textView) {
        this.context = _context;
        this.accoutSharePreference = new MySharePreference((Activity) context);
        this.infoRequireSharePreference = new MySharePreference((Activity) context, accoutSharePreference.getValue("phoneLogin"));
        this.intLayout = _intLayout;
        this.textView = _textView;
    }

    //show all dialog custom rely profile's field
    public void showDialogCustom() {
        Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(intLayout);
        dialog.setCanceledOnTouchOutside(false);

        ArrayAdapter<String> adapter;

        //code in here
        switch (intLayout) {
            //Edit Profile Hobby about food
            case R.layout.custom_dialog_profile_hobby_food:
                rlClose = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_food_rl_close);
                rlDone = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_food_rl_done);
                actvHobbyAboutFood = (MultiAutoCompleteTextView)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_food_actv);

                if (!TextUtils.isEmpty(textView.getText().toString().trim())) {
                    if (!textView.getText().toString().endsWith(",")) {
                        actvHobbyAboutFood.setText(textView.getText().toString() + ", ");
                    } else {
                        actvHobbyAboutFood.setText(textView.getText().toString());
                    }
                } else {
                    actvHobbyAboutFood.setText(textView.getText().toString());
                }

                actvHobbyAboutFood.setSelection(actvHobbyAboutFood.getText().toString().length());

                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, context.getResources().getStringArray(R.array.food));
                actvHobbyAboutFood.setAdapter(adapter);
                actvHobbyAboutFood.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                AppUtils.setOnSelectedItemInMACT(actvHobbyAboutFood);

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOnclickDoneinHobby(actvHobbyAboutFood, textView, "targetFoodRequire");
                        dialog.dismiss();
                    }
                });
                break;

            //Hobby about character
            case R.layout.custom_dialog_profile_hobby_character:
                rlClose = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_character_rl_close);
                rlDone = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_character_rl_done);
                actvHobbyAboutCharacter = (MultiAutoCompleteTextView)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_character_actv);

                if (!TextUtils.isEmpty(textView.getText().toString().trim())) {
                    if (!textView.getText().toString().endsWith(",")) {
                        actvHobbyAboutCharacter.setText(textView.getText().toString() + ", ");
                    } else {
                        actvHobbyAboutCharacter.setText(textView.getText().toString());
                    }
                } else {
                    actvHobbyAboutCharacter.setText(textView.getText().toString());
                }

                actvHobbyAboutCharacter.setSelection(actvHobbyAboutCharacter.getText().toString().length());

                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, context.getResources().getStringArray(R.array.character));
                actvHobbyAboutCharacter.setAdapter(adapter);
                actvHobbyAboutCharacter.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                AppUtils.setOnSelectedItemInMACT(actvHobbyAboutCharacter);

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOnclickDoneinHobby(actvHobbyAboutCharacter, textView, "targetCharacterRequire");
                        dialog.dismiss();
                    }
                });
                break;

            //Edit Profile hooby about style
            case R.layout.custom_dialog_profile_hobby_style:
                rlClose = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_style_rl_close);
                rlDone = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_style_rl_done);
                actvHobbyAboutStyle = (MultiAutoCompleteTextView)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_style_actv);

                if (!TextUtils.isEmpty(textView.getText().toString().trim())) {
                    if (!textView.getText().toString().endsWith(",")) {
                        actvHobbyAboutStyle.setText(textView.getText().toString() + ", ");
                    } else {
                        actvHobbyAboutStyle.setText(textView.getText().toString());
                    }
                } else {
                    actvHobbyAboutStyle.setText(textView.getText().toString());
                }

                actvHobbyAboutStyle.setSelection(actvHobbyAboutStyle.getText().toString().length());

                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, context.getResources().getStringArray(R.array.style));
                actvHobbyAboutStyle.setAdapter(adapter);
                actvHobbyAboutStyle.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                AppUtils.setOnSelectedItemInMACT(actvHobbyAboutStyle);

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOnclickDoneinHobby(actvHobbyAboutStyle, textView, "targetStyleRequire");
                        dialog.dismiss();
                    }
                });
                break;

            case R.layout.custom_dialog_require_off_choose_age:
                rlClose = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_require_off_choose_age_rl_close);
                rlDone = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_require_off_choose_age_rl_done);
                WheelPicker wpkAgeMin = (WheelPicker) dialog.findViewById(R.id.custom_dialog_require_off_choose_age_wpk_min);
                WheelPicker wpkAgeMax = (WheelPicker) dialog.findViewById(R.id.custom_dialog_require_off_choose_age_wpk_max);
                wpkAgeMin.setSelectedItemPosition(Integer.parseInt(textView.getText().toString().trim().substring(0, 2).trim()) - 10);
                if (Integer.parseInt(textView.getText().toString().trim().substring(0, 2).trim()) < 10) {
                    wpkAgeMax.setSelectedItemPosition(Integer.parseInt(textView.getText().toString().trim().substring(4).trim()) - 10);
                } else {
                    wpkAgeMax.setSelectedItemPosition(Integer.parseInt(textView.getText().toString().trim().substring(5).trim()) - 10);
                }
                //get value in resource
                String[] list = context.getResources().getStringArray(R.array.age_limit);
                final ArrayList<String> ageLimit = new ArrayList(Arrays.asList(list));

                //set data for age min and age max
                wpkAgeMin.setData(ageLimit);
                wpkAgeMax.setData(ageLimit);
                setEventChooseValueAge(wpkAgeMin, wpkAgeMax);
                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setText(Integer.parseInt(wpkAgeMin.getData().get(wpkAgeMin.getCurrentItemPosition()).toString()) + " - " +
                                Integer.parseInt(wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString()));

                        infoRequireSharePreference.setValue("ageMinRequire", wpkAgeMin.getData().get(wpkAgeMin.getCurrentItemPosition()).toString());
                        infoRequireSharePreference.setValue("ageMaxRequire", wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString());
                        dialog.cancel();
                    }
                });
                break;
        }
        dialog.show();
    }


    //set for event click btnDone in hobby
    private void setOnclickDoneinHobby(MultiAutoCompleteTextView actvHobby, TextView text, String key) {
        String tempHobby = "";
        if (TextUtils.isEmpty(actvHobby.getText().toString().trim())) {
            tempHobby = actvHobby.getText().toString().trim();
        } else {
            if (actvHobby.getText().toString().trim().endsWith(",")) {
                tempHobby = actvHobby.getText().toString().trim().substring(0, actvHobby.getText().toString().trim().length() - 1);
            } else {
                tempHobby = actvHobby.getText().toString().trim();
            }
        }
        infoRequireSharePreference.setValue(key, tempHobby);
        textView.setText(tempHobby);
    }

    //method set event when choose limit age for require off
    private void setEventChooseValueAge(final WheelPicker wpkAgeMin, final WheelPicker wpkAgeMax) {

        //listen event when choose value
        wpkAgeMax.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {
            }

            @Override
            public void onWheelSelected(int i) {
                if (Integer.parseInt(wpkAgeMin.getData().get(wpkAgeMin.getCurrentItemPosition()).toString()) >=
                        Integer.parseInt(wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString())) {
                    wpkAgeMin.setSelectedItemPosition(wpkAgeMax.getCurrentItemPosition() - 1);
                }
            }

            @Override
            public void onWheelScrollStateChanged(int i) {
            }
        });

        wpkAgeMin.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {
            }

            @Override
            public void onWheelSelected(int i) {
                if (Integer.parseInt(wpkAgeMin.getData().get(wpkAgeMin.getCurrentItemPosition()).toString()) >=
                        Integer.parseInt(wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString())) {
                    wpkAgeMax.setSelectedItemPosition(wpkAgeMax.getCurrentItemPosition() + 1);
                }
            }

            @Override
            public void onWheelScrollStateChanged(int i) {
            }
        });
    }
}

