package com.app.donteatalone.views.main.profile;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.app.donteatalone.R;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Le Hoang Han on 5/21/2017.
 */

public class ProfileDialogCustom {
    private Context context;
    private int intLayout;
    private TextView textView;
    private RelativeLayout rlClose, rlDone;

    //For name
    private EditText edtName;
    //For Gender
    private RelativeLayout rlMale, rlFemale;
    //For age
    private WheelPicker dayWheelPicker, monthWhellPicker;
    private WheelYearPicker yearWheelPicker;
    private int intSelectYear, intSelectMonth;
    private LocalDate localDate;


    public ProfileDialogCustom(Context _context, int _intLayout, TextView _textView) {
        this.context = _context;
        this.intLayout = _intLayout;
        this.textView = _textView;
    }

    public void showDialogCustom() {
        Dialog dialog = new Dialog(this.context);
        dialog.setContentView(intLayout);
        dialog.setCanceledOnTouchOutside(false);

        //code in here
        switch (intLayout) {

            //Edit Profile Name
            case R.layout.custom_dialog_profile_name:
                rlClose = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_name_rl_close);
                rlDone = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_name_rl_done);
                edtName = (EditText) dialog.findViewById(R.id.custom_dialog_profile_name_edt_name);
                edtName.setText(textView.getText().toString());

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //code for save new name to database ==> Nga ;)))

                        //code for show new name on profile screen

                        dialog.dismiss();
                    }
                });
                break;

            //Edit Profile Age
            case R.layout.custom_dialog_profile_age:
                rlClose = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_age_rl_close);
                rlDone = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_age_rl_done);
                dayWheelPicker = (WheelPicker) dialog.findViewById(R.id.custom_dialog_profile_wheelpicker_day);
                monthWhellPicker = (WheelPicker) dialog.findViewById(R.id.custom_dialog_profile_wheelpicker_month);
                yearWheelPicker = (WheelYearPicker) dialog.findViewById(R.id.custom_dialog_profile_wheelpicker_year);

                localDate = new LocalDate();
                dayWheelPicker.setSelectedItemPosition(localDate.getDayOfMonth() - 2);
                monthWhellPicker.setSelectedItemPosition(localDate.getMonthOfYear() - 1);
                yearWheelPicker.setSelectedYear(localDate.getYear() - Integer.parseInt(textView.getText().toString()));
                ChangeDate();

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //code for save gender to database => Nga

                        dialog.dismiss();
                    }
                });
                break;

            //Edit Profile Gender
            case R.layout.custom_dialog_profile_gender:
                rlClose = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_gender_rl_close);
                rlDone = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_gender_rl_done);
                rlMale = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_gender_rl_male);
                rlFemale = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_gender_rl_female);
                if (textView.getText().toString().equals("F")) {
                    rlFemale.setBackgroundResource(R.drawable.btn_round_orange);
                } else {
                    rlMale.setBackgroundResource(R.drawable.btn_round_orange);
                }

                rlMale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlFemale.setBackgroundResource(R.drawable.bg_round);
                        rlMale.setBackgroundResource(R.drawable.btn_round_orange);
                    }
                });

                rlFemale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlFemale.setBackgroundResource(R.drawable.btn_round_orange);
                        rlMale.setBackgroundResource(R.drawable.bg_round);
                    }
                });

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //code for save gender to database => Nga

                        dialog.dismiss();
                    }
                });
                break;
        }
        dialog.show();
    }

// Nhung ham dung cho edit Age =================================================================
    private boolean checkLeapYear() {
        intSelectYear = yearWheelPicker.getCurrentYear();
        if (intSelectYear % 4 == 0) {
            if (intSelectYear % 100 != 0) {
                return true;
            }
        }
        if (intSelectYear % 100 == 0) {
            if (intSelectYear % 400 == 0) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<String> getDateData(int arr){
        String[] temp= context.getResources().getStringArray(arr);
        ArrayList<String> day = new ArrayList<String>(Arrays.asList(temp));
        return day;
    }


    private void ChangeDate() {
        monthWhellPicker.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {
            }

            @Override
            public void onWheelSelected(int i) {
            }

            @Override
            public void onWheelScrollStateChanged(int i) {
                setDataforWhellPicker();
            }
        });

        yearWheelPicker.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {

            }

            @Override
            public void onWheelScrollStateChanged(int i) {
                setDataforWhellPicker();
            }
        });
    }

    private void setDataforWhellPicker() {
        intSelectMonth = monthWhellPicker.getCurrentItemPosition();
        switch (intSelectMonth) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                dayWheelPicker.setData(getDateData(R.array.Day_31));
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                dayWheelPicker.setData(getDateData(R.array.Day_30));
                break;
            case 1:
                if (checkLeapYear()) {
                    dayWheelPicker.setData(getDateData(R.array.Day_29));
                } else {
                    dayWheelPicker.setData(getDateData(R.array.Day_28));
                }
        }
    }
//=================================================================================================
}

