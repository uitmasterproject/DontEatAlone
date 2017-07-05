package com.app.donteatalone.views.main.require.main_require;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ChomChom on 5/8/2017.
 */

public class OffRequireFragment extends Fragment implements PlaceSelectionListener {
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int REQUEST_SELECT_PLACE = 1000;
    private static final String LOG_TAG = "PlaceSelectionListener";

    private View viewGroup;
    private RelativeLayout rlGenderAll, rlGenderFemale, rlGenderMale, rlAgeClose;
    private TextView txtGenderAll, txtGenderFemale, txtGenderMale;
    private LinearLayout llContainerAge, llContainerAddress, llAgeAccept;
    private LinearLayout llContainerHobbyFood, llContainerHobbyCharacter, llContainerHobbyStyle;
    private TextView txtAge, txtAdress, txtHobbyFood, txtHobbyCharacter, txtHobbyStyle;
    private String location = "", valuetemp, phone;

    public static OffRequireFragment newInstance() {
        OffRequireFragment fragment = new OffRequireFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_require_off, null);
        phone = getInforfromShareReference("account", "phoneLogin");
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
        txtAge.setText(setDefaultValue("ageminRequire", "20") + " - " + setDefaultValue("agemaxRequire", "25"));
        llContainerAddress = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_ll_container_address);
        txtAdress = (TextView) viewGroup.findViewById(R.id.fragment_required_off_txt_address);
        txtAdress.setText(setDefaultValue("addressRequire", "Chợ Bến Thành, Quận 1, Hồ Chí Minh, Vietnam"));
        location = setDefaultValue("latlngRequire", "10.771423,106.698471");
        llContainerHobbyFood = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_ll_container_hobby_food);
        llContainerHobbyCharacter = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_ll_container_hobby_character);
        llContainerHobbyStyle = (LinearLayout) viewGroup.findViewById(R.id.fragment_require_off_ll_container_hobby_style);
        txtHobbyFood = (TextView) viewGroup.findViewById(R.id.fragment_required_off_txt_hobby_food);
        txtHobbyFood.setText(setDefaultValue("hobbyFoodRequire", ""));
        txtHobbyCharacter = (TextView) viewGroup.findViewById(R.id.fragment_required_off_txt_hobby_character);
        txtHobbyCharacter.setText(setDefaultValue("hobbyCharacterRequire", ""));
        txtHobbyStyle = (TextView) viewGroup.findViewById(R.id.fragment_required_off_txt_hobby_style);
        txtHobbyStyle.setText(setDefaultValue("hobbyStyleRequire", ""));
    }

    private String setDefaultValue(String key, String defaul) {
        if (getInforfromShareReference("inforRequire" + "_" + phone, key).equals("") == true)
            return defaul;
        else
            return getInforfromShareReference("inforRequire" + "_" + phone, key).trim();
    }

    //Select Gender, Choose gender for require
    private void setSelectedGender() {
        rlGenderAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGenderAll.setTextColor(getResources().getColor(R.color.color_deep_orange_1));
                txtGenderFemale.setTextColor(Color.GRAY);
                txtGenderMale.setTextColor(Color.GRAY);
                setClickGender(R.drawable.design_require_off_txt_gender_leftsight_selector,
                        R.color.gray_1,
                        R.drawable.design_require_off_txt_gender_rightsight_default);
                editInforRequireintoShareReference("gender", "all");
            }
        });

        rlGenderFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGenderAll.setTextColor(Color.GRAY);
                txtGenderFemale.setTextColor(getResources().getColor(R.color.color_deep_orange_1));
                txtGenderMale.setTextColor(Color.GRAY);
                setClickGender(R.drawable.design_require_off_txt_gender_leftsight_default,
                        R.color.require_off_txt_gender_selected,
                        R.drawable.design_require_off_txt_gender_rightsight_default);
                editInforRequireintoShareReference("gender", "female");
            }
        });

        rlGenderMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGenderAll.setTextColor(Color.GRAY);
                txtGenderFemale.setTextColor(Color.GRAY);
                txtGenderMale.setTextColor(getResources().getColor(R.color.color_deep_orange_1));
                setClickGender(R.drawable.design_require_off_txt_gender_leftsight_default,
                        R.color.gray_1,
                        R.drawable.design_require_off_txt_gender_rightsight_selector);
                editInforRequireintoShareReference("gender", "male");
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
        txtAdress.setText(getString(R.string.formatted_place_data, place.getName(), place.getAddress()));
        location = place.getLatLng().toString().substring(10, place.getLatLng().toString().length() - 1);
        editInforRequireintoShareReference("addressRequire", txtAdress.getText().toString());
        editInforRequireintoShareReference("latlngaddressRequire", location);
    }

    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(getContext(), "Place selection failed: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
    }

    //Select hobby, in here, change value hobby
    private void setClickEditHobby() {
        llContainerHobbyFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(
                        viewGroup.getContext(), R.layout.custom_dialog_profile_hobby_food, txtHobbyFood, true);
                profileDialogCustom.showDialogCustom();
            }
        });

        llContainerHobbyCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(
                        viewGroup.getContext(), R.layout.custom_dialog_profile_hobby_character, txtHobbyCharacter, true);
                profileDialogCustom.showDialogCustom();
            }
        });

        llContainerHobbyStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(
                        viewGroup.getContext(), R.layout.custom_dialog_profile_hobby_style, txtHobbyStyle, true);
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
                editInforRequireintoShareReference("hobbyStyleRequire", valuetemp);
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

    private void editInforRequireintoShareReference(String key, String value) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("inforRequire" + "_" + phone, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void putInforRequireintoShareReference() {
        File f = new File("/data/data/" + getContext().getPackageName() + "/shared_prefs/" + "inforRequire" + "_" + phone + ".xml");
        if (f.exists() == false) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("inforRequire" + "_" + phone, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("genderRequire", "all");

            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            Log.e("birthday", getInforfromShareReference("account", "birthdayLogin") + "");
            int age = currentYear - Integer.parseInt(getInforfromShareReference("account", "birthdayLogin").trim().split("/")[2]);
            editor.putString("ageminRequire", (age - 4) + "");
            editor.putString("agemaxRequire", (age + 4) + "");

            editor.putString("addressRequire", getInforfromShareReference("account", "addressLogin"));
            Log.e("latlng", getInforfromShareReference("account", "latlngaddressLogin"));
            editor.putString("latlngaddressRequire", getInforfromShareReference("account", "latlngaddressLogin"));

            putDataHobbyintoReference(editor);
            editor.commit();
        }
    }

    private void putDataHobbyintoReference(SharedPreferences.Editor editor) {
        String str = "";
        String[] temp = getInforfromShareReference("account", "hobbyLogin").toString().trim().split(",");
        String[] temhobby = getResources().getStringArray(R.array.food);
        for (int j = 0; j < temhobby.length; j++) {
            for (int i = 0; i < temp.length; i++) {
                if (temhobby[j].equals(temp[i]) == true) {
                    str += temp[i] + ",";
                }
            }
        }
        if (str.length() > 0) {
            str = str.trim().substring(0, str.length() - 1);
        }
        editor.putString("hobbyFoodRequire", str);

        str = "";
        temhobby = getResources().getStringArray(R.array.character);
        for (int j = 0; j < temhobby.length; j++) {
            for (int i = 0; i < temp.length; i++) {
                if (temhobby[j].equals(temp[i]) == true) {
                    str += temp[i] + ",";
                }
            }
        }
        if (str.length() > 0) {
            str = str.trim().substring(0, str.length() - 1);
        }
        editor.putString("hobbyCharacterRequire", str);

        str = "";
        temhobby = getResources().getStringArray(R.array.style);
        for (int j = 0; j < temhobby.length; j++) {
            for (int i = 0; i < temp.length; i++) {
                if (temhobby[j].equals(temp[i]) == true) {
                    str += temp[i] + ",";
                }
            }
        }

        if (str.length() > 0) {
            str = str.trim().substring(0, str.length() - 1);
        }
        editor.putString("hobbyStyleRequire", str);
    }

    private String getInforfromShareReference(String file, String key) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(file, MODE_PRIVATE);
        String value = sharedPreferences.getString(key, "");
        return value;
    }
}
