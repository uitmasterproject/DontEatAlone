package com.app.donteatalone.views.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ChomChom on 4/7/2017.
 */

public class RegisterStep4Fragment extends Fragment {

    private View viewGroup;
    private WheelPicker dayWheelPicker, monthWhellPicker;
    private WheelYearPicker yearWheelPicker;
    private RelativeLayout rlNext, rlClose;
    private ViewPager _mViewPager;
    private int intSelectYear, intSelectMonth;
    private LocalDate localDate;
    private LinearLayout llRoot;

    public static Fragment newInstance(Context context) {
        RegisterStep4Fragment f = new RegisterStep4Fragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_register_step4, null);
        init();
        ChangeDate();
        clickButtonNextStep();
        rlCloseClick();
        return viewGroup;
    }

    private void init() {
        dayWheelPicker = (WheelPicker) viewGroup.findViewById(R.id.fragment_register_step4_wheelpicker_day);
        monthWhellPicker = (WheelPicker) viewGroup.findViewById(R.id.fragment_register_step4_wheelpicker_month);
        yearWheelPicker = (WheelYearPicker) viewGroup.findViewById(R.id.fragment_register_step4_wheelpicker_year);
        rlNext = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step4_rl_next);
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        rlClose = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step4_close);
        llRoot = (LinearLayout) viewGroup.findViewById(R.id.fragment_register_step4_ll_root);

        localDate = new LocalDate();
        dayWheelPicker.setSelectedItemPosition(localDate.getDayOfMonth() - 1);
        monthWhellPicker.setSelectedItemPosition(localDate.getMonthOfYear() - 1);
        yearWheelPicker.setSelectedYear(localDate.getYear());
    }

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
        String[] temp= getResources().getStringArray(arr);
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

    private void clickButtonNextStep() {
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RegisterStep1Fragment.userName.setBirthday(toStringDate());
                saveReference();
                _mViewPager.setCurrentItem(4, true);
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

    private String toStringDate() {
        String date = "";
       date = dayWheelPicker.getData().get(dayWheelPicker.getCurrentItemPosition()).toString()
               + "/"
               + monthWhellPicker.getData().get(monthWhellPicker.getCurrentItemPosition()).toString()
               + "/"
               + yearWheelPicker.getData().get(yearWheelPicker.getCurrentItemPosition()).toString();
        Log.e("Date", date + "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        return date;
    }

    private void saveReference() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("birthday", toStringDate());
        editor.apply();
    }
}
