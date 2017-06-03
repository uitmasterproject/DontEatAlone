package com.app.donteatalone.views.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.app.donteatalone.R;

import java.lang.reflect.Field;

/**
 * Created by ChomChom on 4/7/2017.
 */

public class RegisterStep4Fragment extends Fragment {

    private View viewGroup;
    private Spinner spnDay, spnMonth, spnYear;
    private String bDay, bMonth, bYear;
    private Button btnNextStep;
    private ViewPager _mViewPager;

    public static Fragment newInstance(Context context) {
        RegisterStep4Fragment f = new RegisterStep4Fragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup=inflater.inflate(R.layout.fragment_register_step4,null);
        init();
        checkDate();
        clickButtonNextStep();
        return viewGroup;
    }

    private void init() {
        spnDay = (Spinner) viewGroup.findViewById(R.id.fragment_register_step4_spn_day);
        spnMonth = (Spinner) viewGroup.findViewById(R.id.fragment_register_step4_spn_month);
        spnYear = (Spinner) viewGroup.findViewById(R.id.fragment_register_step4_spn_year);
        initSpinner(spnDay, R.array.Day_31);
        initSpinner(spnMonth, R.array.Month);
        initSpinner(spnYear, R.array.Year);
        btnNextStep=(Button) viewGroup.findViewById(R.id.fragment_register_step4_btn_next);
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
    }

    //khoi tao gia tri cho spinner ngay thang nam
    private void initSpinner(Spinner sp, int source) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.custom_spinner_text, getResources().getStringArray(source));
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        sp.setAdapter(arrayAdapter);

        //limit show item in spinner
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(sp);
            popupWindow.setHeight(500);//500px
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
        }
        //
    }

    //kiem tra ngay thang nam co phu hop khong
    private void checkDate() {

        spnDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bDay = (getResources().getStringArray(R.array.Day_31))[position];
                if (bDay.equals("Day")) {
                    bDay = "1";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bMonth = (getResources().getStringArray(R.array.Month))[position];
                int iMonth = parseMonth(bMonth);
                if (iMonth == 2) {
                    if(checkYear(Integer.parseInt(bYear))==1){
                        YearcheckMonth(R.array.Day_29,29);
                    }
                    else {
                        YearcheckMonth(R.array.Day_28,28);
                    }
                }
                if ((iMonth == 4 || iMonth == 6 || iMonth == 9 || iMonth == 11)) {
                    YearcheckMonth(R.array.Day_30,30);
                }
                if ((iMonth == 1 || iMonth == 3 || iMonth == 6 || iMonth == 10||iMonth==12)) {
                    YearcheckMonth(R.array.Day_31,31);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //if user click first year
        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bYear = (getResources().getStringArray(R.array.Year))[position];
                if (bYear.equals("Year")) {
                    bYear = "1970";
                }
                if (checkYear(Integer.parseInt(bYear))==1) {
                    int iMonth = parseMonth(bMonth);
                    if (iMonth == 2) {
                        YearcheckMonth(R.array.Day_29,29);
                    }
                }
                else {
                    int iMonth = parseMonth(bMonth);
                    if (iMonth == 2) {
                        YearcheckMonth(R.array.Day_28,28);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //check month==2, nam nhuan bao nhieu ngay, nam khong nhuan bao nhieu ngay.
    private void YearcheckMonth(int source, int maxposition ){
        int pos = positionDay(bDay, source);
        initSpinner(spnDay, source);
        spnDay.setSelection(pos);
        if (Integer.parseInt(bDay) > maxposition) {
            initSpinner(spnDay, source);
            spnDay.setSelection(maxposition-1);
        }
    }

    //ep kieu cho thang, tu kieu chuoi sang so
    private int parseMonth(String month) {
        int imonth = 0;
        switch (month) {
            case "Month":
                imonth = 1;
                break;
            case "Jan":
                imonth = 1;
                break;
            case "Feb":
                imonth = 2;
                break;
            case "Mar":
                imonth = 3;
                break;
            case "Apr":
                imonth = 4;
                break;
            case "May":
                imonth = 5;
                break;
            case "Jun":
                imonth = 6;
                break;
            case "Jul":
                imonth = 7;
                break;
            case "Aug":
                imonth = 8;
                break;
            case "Sep":
                imonth = 9;
                break;
            case "Oct":
                imonth = 10;
                break;
            case "Nov":
                imonth = 11;
                break;
            case "Dec":
                imonth = 12;
                break;
        }
        return imonth;
    }

    //luu lai ngay dang chon la ngay nao
    private int positionDay(String d, int source) {
        int i=0;
        for (String day :
                getResources().getStringArray(source)) {
            if(day.equals(d))
                return i;
            i++;
        }
        return i;
    }

    private int checkYear(int year){
        if(year%4==0){
            if(year%100!=0)
                return 1;
        }
        if(year%100==0){
            if(year%400==0)
                return 1;
        }
        return 0;
    }

    private void clickButtonNextStep(){
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterStep1Fragment.userName.setBirthday(toStringDate());
                _mViewPager.setCurrentItem(4,true);
            }
        });
    }

    private String toStringDate(){
        String date="";
        if(bDay.equals("Day")||bMonth.equals("Month")||bYear.equals("Year")){
            date="1/1/1970";
        }
        else {
            date=bDay+"/"+bMonth+"/"+bYear;
        }
        Log.e("Date",date+"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        return date;
    }
}
