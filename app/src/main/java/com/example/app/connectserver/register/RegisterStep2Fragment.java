package com.example.app.connectserver.register;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.app.connectserver.R;
import com.example.app.connectserver.model.Hobby;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterStep2Fragment extends Fragment implements PlaceSelectionListener {

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int REQUEST_SELECT_PLACE = 1000;
    private static final String LOG_TAG = "PlaceSelectionListener";
    private Spinner spnDay, spnMonth, spnYear;
    private ViewGroup viewGroup;
    private String bDay, bMonth, bYear;
    private EditText edtAdress;
    private AutoCompleteTextView actvHobby;
    private ArrayList<String> headerHobby;
    private ArrayList<Hobby> hobbies;

    public static Fragment newInstance(Context context) {
        RegisterStep2Fragment f = new RegisterStep2Fragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_register_step2, null);
        init();
        checkDate();
        setEdtAdress();
        setActvHobby();
        return viewGroup;
    }


    //Khoi tao gia tri cho cac bien
    private void init() {
        spnDay = (Spinner) viewGroup.findViewById(R.id.fragment_register_step2_spn_day);
        spnMonth = (Spinner) viewGroup.findViewById(R.id.fragment_register_step2_spn_month);
        spnYear = (Spinner) viewGroup.findViewById(R.id.fragment_register_step2_spn_year);
        initSpinner(spnDay, R.array.Day_31);
        initSpinner(spnMonth, R.array.Month);
        initSpinner(spnYear, R.array.Year);
        edtAdress=(EditText) viewGroup.findViewById(R.id.fragment_register_step2_edt_address);
        actvHobby=(AutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step2_actv_hobby);
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

    //khoi tao gia tri cho address
    private void setEdtAdress(){
        edtAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_OVERLAY    )
                            .setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
                            .build(getActivity());
                    startActivityForResult(intent, REQUEST_SELECT_PLACE);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //luu lai noi duoc chon
    @Override
    public void onPlaceSelected(Place place) {
        Log.i(LOG_TAG, "Place Selected: " + place.getName());
        edtAdress.setText(getString(R.string.formatted_place_data, place.getName(), place.getAddress()));
        //, place.getPhoneNumber(), place.getWebsiteUri(), place.getRating(), place.getId()
        if (!TextUtils.isEmpty(place.getAttributions())){
            edtAdress.setText(Html.fromHtml(place.getAttributions().toString()));
        }
    }


    //kiem tra loi cho noi duoc chon
    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(getContext(), "Place selection failed: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
    }

    //tra ve gia tri khi thoat khoi trang tim kiem dia chi
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setActvHobby(){
        clonebdHobby();
        CustomAdapterCompleteTextView customAdapterCompleteTextView=new CustomAdapterCompleteTextView(this.getContext(), android.R.layout.simple_dropdown_item_1line,headerHobby,hobbies,actvHobby);
        actvHobby.setAdapter(customAdapterCompleteTextView);
        
    }

    private void clonebdHobby(){
        headerHobby=new ArrayList<>();
        headerHobby.add("Món ăn");
        headerHobby.add("Tính cách");
        headerHobby.add("Phong cách");

        hobbies=new ArrayList<>();
        hobbies.add(new Hobby("Món ăn","",true));
        hobbies.add(new Hobby("Món ăn","các món từ gà, gà rán, gà nướng, gà quay...",false));
        hobbies.add(new Hobby("Món ăn","các loại lẩu",false));
        hobbies.add(new Hobby("Món ăn","đồ xiên que",false));
        hobbies.add(new Hobby("Món ăn","đồ ăn liền",false));
        hobbies.add(new Hobby("Món ăn","các món ngọt",false));
        hobbies.add(new Hobby("Món ăn","phở, bún, bánh canh cua...",false));

        hobbies.add(new Hobby("Tính cách","",true));
        hobbies.add(new Hobby("Tính cách","vui vẻ",false));
        hobbies.add(new Hobby("Tính cách","trầm tĩnh",false));
        hobbies.add(new Hobby("Tính cách","hóm hĩnh",false));
        hobbies.add(new Hobby("Tính cách","thoải mái",false));

        hobbies.add(new Hobby("Phong cách","",true));
        hobbies.add(new Hobby("Phong cách","tự do",false));
        hobbies.add(new Hobby("Phong cách","quái dị",false));
        hobbies.add(new Hobby("Phong cách","trưởng thành",false));




    }

}
