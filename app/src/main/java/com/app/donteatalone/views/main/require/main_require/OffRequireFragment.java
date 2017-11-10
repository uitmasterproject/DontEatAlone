package com.app.donteatalone.views.main.require.main_require;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.profile.ProfileDialogCustom;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.File;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ChomChom on 5/8/2017
 */

public class OffRequireFragment extends Fragment implements PlaceSelectionListener {
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int REQUEST_SELECT_PLACE = 1000;

    private View viewGroup;
    private RelativeLayout rlGenderAll, rlGenderFemale, rlGenderMale, rlAgeClose;
    private TextView txtGenderAll, txtGenderFemale, txtGenderMale;
    private LinearLayout llContainerAge, llContainerAddress, llAgeAccept;
    private LinearLayout llContainerHobbyFood, llContainerHobbyCharacter, llContainerHobbyStyle;
    private TextView txtAge, txtAdress, txtHobbyFood, txtHobbyCharacter, txtHobbyStyle;
    private String location = "", valuetemp, phone;
    private MySharePreference accountSharePreference;
    private MySharePreference infoRequireSharePreference;

    public static OffRequireFragment newInstance() {
        return new OffRequireFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_require_off, container, false);
        accountSharePreference = new MySharePreference(getActivity());
        phone = accountSharePreference.getPhoneLogin();
        putInforRequireintoShareReference();

        init();

        setSelectedGender();
        setClickEditAge();
        setClickEditAddress();
        setClickEditHobby();

        return viewGroup;
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
        txtAge = (TextView) viewGroup.findViewById(R.id.fragment_require_off_txt_age);

        txtAge.setText(setDefaultValue(infoRequireSharePreference.getAgeMinRequire(), "20") + " - " + setDefaultValue(infoRequireSharePreference.getAgeMaxRequire(), "25"));

        llContainerAddress = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_ll_container_address);

        txtAdress = (TextView) viewGroup.findViewById(R.id.fragment_required_off_txt_address);
        txtAdress.setText(setDefaultValue(infoRequireSharePreference.getAddressRequire(), "Cho Ben Thanh, Quan 1, Ho Chi Minh, Vietnam"));
        location = setDefaultValue(infoRequireSharePreference.getLatLngAddressRequire(), "10.771423,106.698471");

        llContainerHobbyFood = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_ll_container_hobby_food);
        llContainerHobbyCharacter = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_ll_container_hobby_character);
        llContainerHobbyStyle = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_ll_container_hobby_style);
        txtHobbyFood = (TextView) viewGroup.findViewById(R.id.fragment_required_off_txt_hobby_food);

        txtHobbyFood.setText(infoRequireSharePreference.getTargetFoodRequire());
        txtHobbyCharacter = (TextView) viewGroup.findViewById(R.id.fragment_required_off_txt_hobby_character);

        txtHobbyCharacter.setText(infoRequireSharePreference.getTargetCharacterRequire());
        txtHobbyStyle = (TextView) viewGroup.findViewById(R.id.fragment_required_off_txt_hobby_style);

        txtHobbyStyle.setText(infoRequireSharePreference.getTargetStyleRequire());
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
                infoRequireSharePreference.setGenderRequire("Female");
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
                infoRequireSharePreference.setGenderRequire("Male");
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
        llContainerAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(
                        viewGroup.getContext(), R.layout.custom_dialog_require_off_choose_age, txtAge);
                profileDialogCustom.showDialogCustom();
            }
        });
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
        txtAdress.setText(AppUtils.convertStringToNFD(getString(R.string.formatted_place_data, place.getName(), place.getAddress())));
        location = place.getLatLng().toString().substring(10, place.getLatLng().toString().length() - 1);
        infoRequireSharePreference.setAddressRequire(txtAdress.getText().toString());
        infoRequireSharePreference.setLatLngAddressRequire(location);
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(getContext(), "Place selection failed: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
    }

    //Select hobby, in here, change value hobby
    private void setClickEditHobby() {
        llContainerHobbyFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(
                        viewGroup.getContext(), R.layout.custom_dialog_profile_hobby_food, txtHobbyFood);
                profileDialogCustom.showDialogCustom();
            }
        });

        llContainerHobbyCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(
                        viewGroup.getContext(), R.layout.custom_dialog_profile_hobby_character, txtHobbyCharacter);
                profileDialogCustom.showDialogCustom();
            }
        });

        llContainerHobbyStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(
                        viewGroup.getContext(), R.layout.custom_dialog_profile_hobby_style, txtHobbyStyle);
                profileDialogCustom.showDialogCustom();
            }
        });
    }

    private void setClickrlContainerHobby(String title, TextView textView, int resource) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_require_off_choose_hobby, null);
        dialog.setView(dialogView);
        dialog.setTitle(title);
        final MultiAutoCompleteTextView atctcHobby = (MultiAutoCompleteTextView) dialogView.findViewById(R.id.custom_dialog_require_off_choose_hobby_atctv_hobby);
        atctcHobby.setText(textView.getText().toString());
        valuetemp = textView.getText().toString();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(resource));
        atctcHobby.setAdapter(adapter);
        atctcHobby.setThreshold(1);
        atctcHobby.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        atctcHobby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (atctcHobby.getText().toString().length() > 0) {
                    atctcHobby.setText(atctcHobby.getText().toString().substring(0, atctcHobby.getText().toString().lastIndexOf(",")) + atctcHobby.getItemSelectedListener().toString());
                } else {
                    atctcHobby.setText(atctcHobby.getText().toString().substring(0, atctcHobby.getText().toString().lastIndexOf(",")) + "," + atctcHobby.getItemSelectedListener().toString());
                }
                valuetemp = atctcHobby.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txtHobbyCharacter.setText(atctcHobby.getText().toString().substring(0, atctcHobby.getText().toString().length() - 1));
                infoRequireSharePreference.setTargetStyleRequire(valuetemp);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void putInforRequireintoShareReference() {

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

}
