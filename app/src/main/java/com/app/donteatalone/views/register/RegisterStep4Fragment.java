package com.app.donteatalone.views.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.FormatDate;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static com.app.donteatalone.views.register.RegisterStep1Fragment.userName;

/**
 * Created by ChomChom on 4/7/2017
 */

public class RegisterStep4Fragment extends Fragment {

    private View view;
    private WheelPicker dayWheelPicker, monthWhellPicker;
    private WheelYearPicker yearWheelPicker;
    private Button btnNext;
    private ViewPager _mViewPager;
    private LinearLayout llRoot;
    private TextView txtTutorial;

    public static Fragment newInstance() {
        RegisterStep4Fragment f = new RegisterStep4Fragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_step4, container, false);
        init();
        llRootTouch();
        ChangeDate();
        clickButtonNextStep();
        return view;
    }

    private void init() {
        dayWheelPicker = (WheelPicker) view.findViewById(R.id.fragment_register_step4_wheelpicker_day);
        monthWhellPicker = (WheelPicker) view.findViewById(R.id.fragment_register_step4_wheelpicker_month);
        yearWheelPicker = (WheelYearPicker) view.findViewById(R.id.fragment_register_step4_wheelpicker_year);
        btnNext = (Button) view.findViewById(R.id.fragment_register_step4_btn_next);
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        llRoot = (LinearLayout) view.findViewById(R.id.fragment_register_step4_ll_root);
        txtTutorial = (TextView) view.findViewById(R.id.fragment_register_step4_txt_tutorial);

        LocalDate localDate = new LocalDate();
        dayWheelPicker.setSelectedItemPosition(localDate.getDayOfMonth() - 1);
        monthWhellPicker.setSelectedItemPosition(localDate.getMonthOfYear() - 1);
        yearWheelPicker.setSelectedYear(localDate.getYear());
    }


    private ArrayList<String> getDateData(int arr) {
        String[] temp = getResources().getStringArray(arr);
        return new ArrayList<String>(Arrays.asList(temp));
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
                dayWheelPicker.setData(getDateData(FormatDate.checkDateInMonth(monthWhellPicker.getCurrentItemPosition(), yearWheelPicker.getCurrentYear())));
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
                dayWheelPicker.setData(getDateData(FormatDate.checkDateInMonth(monthWhellPicker.getCurrentItemPosition(), yearWheelPicker.getCurrentYear())));
            }
        });
    }

    private void clickButtonNextStep() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                if ((calendar.get(Calendar.YEAR) - Integer.parseInt(yearWheelPicker.getData().get(yearWheelPicker.getCurrentItemPosition()).toString())) > 15) {
                    userName.setBirthday(toStringDate());
                    _mViewPager.setCurrentItem(4, true);
                } else {
                    txtTutorial.setText(getResources().getString(R.string.invalid_age));
                    txtTutorial.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_orange_pressed));
                }
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
        return dayWheelPicker.getData().get(dayWheelPicker.getCurrentItemPosition()).toString()
                + "/" +
                monthWhellPicker.getData().get(monthWhellPicker.getCurrentItemPosition()).toString()
                + "/" +
                yearWheelPicker.getData().get(yearWheelPicker.getCurrentItemPosition()).toString();
    }
}
