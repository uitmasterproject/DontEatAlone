package com.app.donteatalone.views.main.require.main_require;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ChomChom on 5/8/2017
 */

public class OffRequireFragment extends Fragment implements PlaceSelectionListener, View.OnClickListener {
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int REQUEST_SELECT_PLACE = 1000;

    private View viewGroup;
    private RelativeLayout rlGenderAll, rlGenderFemale, rlGenderMale;
    private TextView txtGenderAll, txtGenderFemale, txtGenderMale;
    private LinearLayout llContainerAge, llContainerAddress;
    private LinearLayout llContainerHobbyFood, llContainerHobbyCharacter, llContainerHobbyStyle;
    private TextView txtAge, txtAdress, txtHobbyFood, txtHobbyCharacter, txtHobbyStyle;
    private String location = "", phone;
    private MySharePreference accountSharePreference;
    private MySharePreference infoRequireSharePreference;

    private LinearLayout llEditAge;

    private RelativeLayout rlEdit;
    private MultiAutoCompleteTextView mactvEdit;
    private ImageView imgSave, imgRefresh;

    private String contentBeforeEdit;

    public static OffRequireFragment newInstance() {
        return new OffRequireFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_require_off, container, false);

        accountSharePreference = new MySharePreference(getActivity());
        phone = accountSharePreference.getPhoneLogin();
        putInfoRequireIntoShareReference();

        init();

        setSelectedGender();
        setClickEditAge();
        setClickEditAddress();
        setClickEditHobby();

        return viewGroup;
    }

    @Override
    public void onStop() {
        Log.e("activity onStop", getActivity()+"++++++++++++++++++++++++++++++++++++++++");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.e("activity onDestroy", getActivity()+"++++++++++++++++++++++++++++++++++++++++");
        super.onDestroy();
    }

    @Override
    public void onStart() {
        Log.e("activity Off on Start", getActivity()+"++++++++++++++++++++++++++++++++++++++++");
        super.onStart();
    }

    private void init() {
        rlGenderAll = (RelativeLayout) viewGroup.findViewById(R.id.fragment_require_off_rl_all);
        rlGenderFemale = (RelativeLayout) viewGroup.findViewById(R.id.fragment_require_off_rl_female);
        rlGenderMale = (RelativeLayout) viewGroup.findViewById(R.id.fragment_require_off_rl_male);

        txtGenderAll = (TextView) viewGroup.findViewById(R.id.fragment_require_off_txt_all);
        txtGenderMale = (TextView) viewGroup.findViewById(R.id.fragment_require_off_txt_male);
        txtGenderFemale = (TextView) viewGroup.findViewById(R.id.fragment_require_off_txt_female);

        txtGenderAll.setTextColor(getResources().getColor(R.color.color_deep_orange_1));

        llContainerAge = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_rl_container_range);

        llEditAge = (LinearLayout) viewGroup.findViewById(R.id.ll_edit_age);

        txtAge = (TextView) viewGroup.findViewById(R.id.fragment_require_off_txt_age);

        txtAge.setText(setDefaultValue(infoRequireSharePreference.getAgeMinRequire(), "20") + " - " + setDefaultValue(infoRequireSharePreference.getAgeMaxRequire(), "25"));

        llContainerAddress = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_ll_container_address);

        txtAdress = (TextView) viewGroup.findViewById(R.id.fragment_required_off_txt_address);
        txtAdress.setText(setDefaultValue(StringEscapeUtils.unescapeJava(infoRequireSharePreference.getAddressRequire()), getString(R.string.default_address)));
        location = setDefaultValue(infoRequireSharePreference.getLatLngAddressRequire(), getString(R.string.default_lat_lng));

        llContainerHobbyFood = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_ll_container_hobby_food);
        llContainerHobbyCharacter = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_ll_container_hobby_character);
        llContainerHobbyStyle = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_ll_container_hobby_style);

        txtHobbyFood = (TextView) viewGroup.findViewById(R.id.fragment_required_off_txt_hobby_food);
        txtHobbyFood.setText(StringEscapeUtils.unescapeJava(infoRequireSharePreference.getTargetFoodRequire()));

        txtHobbyCharacter = (TextView) viewGroup.findViewById(R.id.fragment_required_off_txt_hobby_character);
        txtHobbyCharacter.setText(StringEscapeUtils.unescapeJava(infoRequireSharePreference.getTargetCharacterRequire()));

        txtHobbyStyle = (TextView) viewGroup.findViewById(R.id.fragment_required_off_txt_hobby_style);
        txtHobbyStyle.setText(StringEscapeUtils.unescapeJava(infoRequireSharePreference.getTargetStyleRequire()));
    }

    private String setDefaultValue(String value, String defaultValue) {
        if (TextUtils.isEmpty(value))
            return defaultValue;
        else
            return value;
    }

    //Select Gender, Choose gender for require
    private void setSelectedGender() {
        rlGenderAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGenderAll.setTextColor(ContextCompat.getColor(getContext(), R.color.color_deep_orange_1));
                txtGenderFemale.setTextColor(Color.GRAY);
                txtGenderMale.setTextColor(Color.GRAY);
                setClickGender(R.drawable.design_require_off_txt_gender_leftsight_selector,
                        R.color.grey_1,
                        R.drawable.design_require_off_txt_gender_rightsight_default);
                infoRequireSharePreference.setGenderRequire("all");
            }
        });

        rlGenderFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGenderAll.setTextColor(Color.GRAY);
                txtGenderFemale.setTextColor(ContextCompat.getColor(getContext(), R.color.color_deep_orange_1));
                txtGenderMale.setTextColor(Color.GRAY);
                setClickGender(R.drawable.design_require_off_txt_gender_leftsight_default,
                        R.color.require_off_txt_gender_selected,
                        R.drawable.design_require_off_txt_gender_rightsight_default);
                infoRequireSharePreference.setGenderRequire(StringEscapeUtils.escapeJava("Nữ"));
            }
        });

        rlGenderMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGenderAll.setTextColor(Color.GRAY);
                txtGenderFemale.setTextColor(Color.GRAY);
                txtGenderMale.setTextColor(ContextCompat.getColor(getContext(), R.color.color_deep_orange_1));
                setClickGender(R.drawable.design_require_off_txt_gender_leftsight_default,
                        R.color.grey_1,
                        R.drawable.design_require_off_txt_gender_rightsight_selector);
                infoRequireSharePreference.setGenderRequire(StringEscapeUtils.escapeJava("Nam"));
            }
        });
    }

    private void setClickGender(int res1, int res2, int res3) {
        rlGenderAll.setBackgroundResource(res1);
        rlGenderFemale.setBackgroundResource(res2);
        rlGenderMale.setBackgroundResource(res3);
    }

    //Select Age
    private void setClickEditAge() {
        llContainerAge.setOnClickListener(this);
    }

    //Select Address
    private void setClickEditAddress() {
        llContainerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_OVERLAY)
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

    @Override
    public void onPlaceSelected(Place place) {
        txtAdress.setText(getString(R.string.formatted_place_data, place.getName(), place.getAddress()));
        location = place.getLatLng().toString().substring(10, place.getLatLng().toString().length() - 1);
        infoRequireSharePreference.setAddressRequire(StringEscapeUtils.escapeJava(txtAdress.getText().toString()));
        infoRequireSharePreference.setLatLngAddressRequire(location);
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(getContext(), "Place selection failed: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
    }

    //Select hobby, in here, change value hobby
    private void setClickEditHobby() {
        llContainerHobbyFood.setOnClickListener(this);

        llContainerHobbyCharacter.setOnClickListener(this);

        llContainerHobbyStyle.setOnClickListener(this);
    }

    private void putInfoRequireIntoShareReference() {

        File f = new File("/data/data/" + getContext().getPackageName() + "/shared_prefs/" + "DONTEATALONE.INFORREQUIRE" + "_" + phone + ".xml");
        if (!f.exists()) {
            infoRequireSharePreference = new MySharePreference(getActivity(), phone);
            infoRequireSharePreference.setGenderRequire("all");

            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int age = currentYear - Integer.parseInt(accountSharePreference.getBirthdayLogin().trim().split("/")[2]);
            infoRequireSharePreference.setAgeMinRequire((age - 4) + "");
            infoRequireSharePreference.setAgeMaxRequire((age + 4) + "");

            infoRequireSharePreference.setAddressRequire(accountSharePreference.getAddressLogin());
            infoRequireSharePreference.setLatLngAddressRequire(accountSharePreference.getLatLngAddressLogin());

            infoRequireSharePreference.setTargetFoodRequire(accountSharePreference.getTargetFoodLogin());
            infoRequireSharePreference.setTargetCharacterRequire(accountSharePreference.getTargetCharacterLogin());
            infoRequireSharePreference.setTargetStyleRequire(accountSharePreference.getTargetStyleLogin());
        } else {
            infoRequireSharePreference = new MySharePreference(getActivity(), phone);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_require_off_ll_container_hobby_food:
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                editInfo(R.id.rl_edit_target_food, R.id.mactv_target_food, R.array.food,
                        R.id.img_save_target_food, R.id.refresh_target_food, llContainerHobbyFood, txtHobbyFood);
                break;
            case R.id.img_save_target_food:
                saveInfo(llContainerHobbyFood, txtHobbyFood, R.id.img_save_target_food);
                break;
            case R.id.fragment_require_off_ll_container_hobby_character:
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                editInfo(R.id.rl_edit_target_character, R.id.mactv_target_character,
                        R.string.target_character, R.id.img_save_target_character, R.id.refresh_target_character,
                        llContainerHobbyCharacter, txtHobbyCharacter);
                break;
            case R.id.img_save_target_character:
                saveInfo(llContainerHobbyCharacter, txtHobbyCharacter, R.id.img_save_target_character);
                break;
            case R.id.fragment_require_off_ll_container_hobby_style:
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                editInfo(R.id.rl_edit_target_style, R.id.mactv_target_style, R.array.style,
                        R.id.img_save_target_style, R.id.refresh_target_style, llContainerHobbyStyle, txtHobbyStyle);
                break;
            case R.id.img_save_target_style:
                saveInfo(llContainerHobbyStyle, txtHobbyStyle, R.id.img_save_target_style);
                break;
            case R.id.mactv_my_character:
            case R.id.mactv_my_style:
            case R.id.mactv_target_food:
            case R.id.mactv_target_character:
            case R.id.mactv_target_style:
                if (!TextUtils.isEmpty(mactvEdit.getText().toString()) && !mactvEdit.getText().toString().trim().endsWith(",")) {
                    mactvEdit.setText(mactvEdit.getText().toString() + ", ");
                }
                mactvEdit.setSelection(mactvEdit.getText().toString().length());
                break;
            case R.id.refresh_character:
            case R.id.refresh_style:
            case R.id.refresh_target_food:
            case R.id.refresh_target_character:
            case R.id.refresh_target_style:
                mactvEdit.setText(contentBeforeEdit);
                if (!TextUtils.isEmpty(mactvEdit.getText().toString()) && !mactvEdit.getText().toString().trim().endsWith(",")) {
                    mactvEdit.setText(mactvEdit.getText().toString() + ", ");
                }
                mactvEdit.setSelection(mactvEdit.getText().toString().length());
                break;
            case R.id.fragment_require_off_rl_container_range:
                if (llEditAge.getVisibility() == View.GONE) {
                    llEditAge.setVisibility(View.VISIBLE);
                    editAge();
                } else {
                    llEditAge.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void editInfo(int rlEditSource, int macTvEditSource, int listDataSource,
                          int imgSaveSource, int imgRefreshSource, LinearLayout llDisplay, TextView tvContent) {

        AppUtils.hideSoftKeyboard(getActivity());

        int[] listEdit = new int[]{R.id.rl_edit_target_food, R.id.rl_edit_target_character, R.id.rl_edit_target_style};
        LinearLayout[] listDisplay = new LinearLayout[]{llContainerHobbyFood, llContainerHobbyCharacter, llContainerHobbyStyle};

        for (int i = 0; i < listEdit.length; i++) {
            if (listEdit[i] != rlEditSource) {
                RelativeLayout rl = (RelativeLayout) viewGroup.findViewById(listEdit[i]);
                if (rl.getVisibility() == View.VISIBLE) {
                    rl.setVisibility(View.GONE);
                    listDisplay[i].setVisibility(View.VISIBLE);
                }
            }
        }

        rlEdit = (RelativeLayout) viewGroup.findViewById(rlEditSource);

        rlEdit.setOnClickListener(this);
        rlEdit.setVisibility(View.VISIBLE);

        mactvEdit = (MultiAutoCompleteTextView) viewGroup.findViewById(macTvEditSource);
        mactvEdit.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        ArrayAdapter hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(listDataSource));
        mactvEdit.setAdapter(hobbyAdapter);

        contentBeforeEdit = tvContent.getText().toString();
        mactvEdit.setText(contentBeforeEdit);

        mactvEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mactvEdit.setSelection(mactvEdit.getText().toString().length());
            }
        });

        mactvEdit.setOnClickListener(this);

        imgSave = (ImageView) viewGroup.findViewById(imgSaveSource);
        imgSave.setOnClickListener(this);

        imgRefresh = (ImageView) viewGroup.findViewById(imgRefreshSource);
        imgRefresh.setOnClickListener(this);

        llDisplay.setVisibility(View.GONE);
    }

    private void saveInfo(final LinearLayout llDisplay, final TextView tvContent, final int titleDefaultResource) {
        String content;

        if (mactvEdit.getText().toString().trim().endsWith(",")) {
            content = StringEscapeUtils.escapeJava(mactvEdit.getText().toString().trim().
                    substring(0, mactvEdit.getText().toString().trim().lastIndexOf(",")));
        } else {
            content = StringEscapeUtils.escapeJava(mactvEdit.getText().toString());
        }

        switch (titleDefaultResource) {
            case R.id.img_save_target_food:
                infoRequireSharePreference.setTargetFoodRequire(content);
                break;
            case R.id.img_save_target_character:
                infoRequireSharePreference.setTargetCharacterRequire(content);
                break;
            case R.id.img_save_target_style:
                infoRequireSharePreference.setTargetStyleRequire(content);
                break;
        }

        llDisplay.setVisibility(View.VISIBLE);
        tvContent.setText(StringEscapeUtils.unescapeJava(content));
        rlEdit.setVisibility(View.GONE);
        AppUtils.hideSoftKeyboard(getActivity());

    }

    private void editAge() {
        WheelPicker wpkAgeMin = (WheelPicker) viewGroup.findViewById(R.id.fragment_require_off_wpk_min);
        WheelPicker wpkAgeMax = (WheelPicker) viewGroup.findViewById(R.id.fragment_require_off_wpk_max);
        wpkAgeMin.setSelectedItemPosition(Integer.parseInt(txtAge.getText().toString().trim().substring(0, 2).trim()) - 10);
        if (Integer.parseInt(txtAge.getText().toString().trim().substring(0, 2).trim()) < 10) {
            wpkAgeMax.setSelectedItemPosition(Integer.parseInt(txtAge.getText().toString().trim().substring(4).trim()) - 10);
        } else {
            wpkAgeMax.setSelectedItemPosition(Integer.parseInt(txtAge.getText().toString().trim().substring(5).trim()) - 10);
        }
        //get value in resource
        String[] list = getResources().getStringArray(R.array.age_limit);
        final ArrayList<String> ageLimit = new ArrayList<String>(Arrays.asList(list));

        //set data for age min and age max
        wpkAgeMin.setData(ageLimit);
        wpkAgeMax.setData(ageLimit);
        setEventChooseValueAge(wpkAgeMin, wpkAgeMax);
    }

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

                txtAge.setText(wpkAgeMin.getData().get(wpkAgeMin.getCurrentItemPosition()).toString() + " - " +
                        wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString());

                infoRequireSharePreference.setAgeMinRequire(wpkAgeMin.getData().get(wpkAgeMin.getCurrentItemPosition()).toString());
                infoRequireSharePreference.setAgeMaxRequire(wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString());
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
                    wpkAgeMax.setSelectedItemPosition(wpkAgeMin.getCurrentItemPosition() + 1);
                }
                txtAge.setText(wpkAgeMin.getData().get(wpkAgeMin.getCurrentItemPosition()).toString() + " - " +
                        wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString());
                infoRequireSharePreference.setAgeMinRequire(wpkAgeMin.getData().get(wpkAgeMin.getCurrentItemPosition()).toString());
                infoRequireSharePreference.setAgeMaxRequire(wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString());
            }

            @Override
            public void onWheelScrollStateChanged(int i) {
            }
        });
    }
}
