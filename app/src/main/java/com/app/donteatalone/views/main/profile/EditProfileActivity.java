package com.app.donteatalone.views.main.profile;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.UserName;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.MainActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.app.donteatalone.model.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Delbert on 9/23/2017.
 */

public class EditProfileActivity extends AppCompatActivity implements PlaceSelectionListener {

    private static final int REQUEST_SELECT_PLACE = 1000;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private RelativeLayout rlCancel, rlSave;
    private ImageView ivAvatar;
    private LinearLayout llEditAvatar, llGender, llDoB, llDobCustomize;
    private EditText etName;
    private RadioGroup radioGroupGender;
    private WheelPicker wpDobMonths, wpDobDays;
    private WheelYearPicker wpDobYears;
    private MultiAutoCompleteTextView mactvCharacters, mactvStyles, mactvTagetFoods, mactvTargetCharacters, mactvTargetStyles;
    private Animation animation;
    private TextView tvGender, tvAge, tvAddress;
    private int intSelectYear, intSelectMonth;
    private UserName userName;
    private String latlngAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
        setDefaultValue();
        itemClick();
        editAvatar();
        selectGender();
        changeDate();
        editAddress();
        editMultiAutoCompleteTextView(mactvCharacters, R.array.character);
        editMultiAutoCompleteTextView(mactvStyles, R.array.style);
        editMultiAutoCompleteTextView(mactvTagetFoods, R.array.food);
        editMultiAutoCompleteTextView(mactvTargetCharacters, R.array.character);
        editMultiAutoCompleteTextView(mactvTargetStyles, R.array.style);
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

        tvGender = (TextView) findViewById(R.id.activity_edit_profile_tv_gender);
        tvAge = (TextView) findViewById(R.id.activity_edit_profile_tv_dob);
        tvAddress = (TextView) findViewById(R.id.activity_edit_profile_tv_address);
            /*Day of Birth - show and hide*/
        llDoB = (LinearLayout) findViewById(R.id.activity_edit_profile_ll_dob);
        llDobCustomize = (LinearLayout) findViewById(R.id.activity_edit_profile_ll_dob_customize);
        wpDobDays = (WheelPicker) findViewById(R.id.activity_edit_profile_wheelpicker_dob_days);
        wpDobMonths = (WheelPicker) findViewById(R.id.activity_edit_profile_wheelpicker_dob_months);
        wpDobYears = (WheelYearPicker) findViewById(R.id.activity_edit_profile_wheelpicker_dob_years);
        /*More Information*/
        mactvCharacters = (MultiAutoCompleteTextView) findViewById(R.id.activity_edit_profile_mactv_characters);
        mactvStyles = (MultiAutoCompleteTextView) findViewById(R.id.activity_edit_profile_mactv_styles);
        mactvTagetFoods = (MultiAutoCompleteTextView) findViewById(R.id.activity_edit_profile_mactv_target_foods);
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
                    if (tvGender.getText().toString().equals("Male")) {
                        radioGroupGender.check(R.id.activity_edit_profile_radio_male);
                    } else {
                        radioGroupGender.check(R.id.activity_edit_profile_radio_female);
                    }
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
        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        rlSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseProgress baseProgress=new BaseProgress();

                userName.setAvatar(AppUtils.convertBitmaptoString(((BitmapDrawable)ivAvatar.getDrawable()).getBitmap()));
                userName.setFullName(AppUtils.convertStringToNFD(etName.getText().toString()));
                userName.setGender(tvGender.getText().toString());
                userName.setBirthday(tvAge.getText().toString());
                userName.setAddress(AppUtils.convertStringToNFD(tvAddress.getText().toString()));
                userName.setLatlngAdress(latlngAddress);
                if (mactvCharacters.getText().toString().trim().endsWith(",")) {
                    userName.setMyCharacter(mactvCharacters.getText().toString().substring(0, mactvCharacters.getText().toString().lastIndexOf(",")));
                }

                if (mactvStyles.getText().toString().trim().endsWith(",")) {
                    userName.setMyStyle(mactvStyles.getText().toString().substring(0, mactvStyles.getText().toString().lastIndexOf(",")));
                }

                if (mactvTargetCharacters.getText().toString().trim().endsWith(",")) {
                    userName.setTargetCharacter(mactvTargetCharacters.getText().toString().substring(0, mactvTargetCharacters.getText().toString().lastIndexOf(",")));
                }

                if (mactvTargetStyles.getText().toString().trim().endsWith(",")) {
                    userName.setTargetStyle(mactvTargetStyles.getText().toString().substring(0, mactvTargetStyles.getText().toString().lastIndexOf(",")));
                }

                if (mactvTagetFoods.getText().toString().trim().endsWith(",")) {
                    userName.setTargetFood(mactvTagetFoods.getText().toString().substring(0, mactvTagetFoods.getText().toString().lastIndexOf(",")));
                }

                new MySharePreference(EditProfileActivity.this).saveAccountInfo(userName);

                baseProgress.showProgressLoading(EditProfileActivity.this);
                Call<Status> editProfile= Connect.getRetrofit().editProfile(userName);
                editProfile.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        baseProgress.hideProgressLoading();
                        if(response.body()!=null){
                            if(response.body().getStatus().equals("0")){
                                Intent intent=new Intent(EditProfileActivity.this, MainActivity.class);
                                intent.putExtra("viewProfile","viewProfile");
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        baseProgress.hideProgressLoading();
                    }
                });


            }
        });

    }

    private void editAddress() {
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                try {
                    intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_OVERLAY)
                            .setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
                            .build(EditProfileActivity.this);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, REQUEST_SELECT_PLACE);
            }
        });
    }

    public void selectImage() {
        final CharSequence[] options = {"Take photo", "Choose from Gallery", "Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this, R.style.MyDialogTheme);

        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Take photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[which].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                } else if (options[which].equals("Cancel"))
                    dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.alpha = 0.7f;
        window.setAttributes(layoutParams);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                performCrop(data.getData());
            } else if (requestCode == 2) {
                performCrop(data.getData());
            } else if (requestCode == 3) {
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
                ivAvatar.setImageBitmap(thePic);
            } else if (requestCode == REQUEST_SELECT_PLACE) {
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(EditProfileActivity.this, data);
                    this.onPlaceSelected(place);
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    com.google.android.gms.common.api.Status status = PlaceAutocomplete.getStatus(EditProfileActivity.this, data);
                    this.onError(status);
                }
            }
        }
    }

    private void performCrop(Uri uri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop", true);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, 3);
        } catch (ActivityNotFoundException anfe) {

        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        tvAddress.setText(getString(R.string.formatted_place_data, place.getName(), place.getAddress()));
        latlngAddress = place.getLatLng().toString().substring(10, place.getLatLng().toString().length() - 1);
    }

    @Override
    public void onError(com.google.android.gms.common.api.Status status) {
        Toast.makeText(EditProfileActivity.this, "Place selection failed: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
    }

    private void setDefaultValue() {
        userName = new MySharePreference(EditProfileActivity.this).createObject();
        ivAvatar.setImageBitmap(AppUtils.decodeBitmap(userName.getAvatar()));
        etName.setText(userName.getFullName());
        tvAge.setText(userName.getBirthday());
        tvGender.setText(userName.getGender());
        tvAddress.setText(userName.getAddress());

        if (!TextUtils.isEmpty(userName.getTargetFood()))
            mactvTagetFoods.setText(userName.getTargetFood() + ", ");

        if (!TextUtils.isEmpty(userName.getTargetCharacter()))
            mactvTargetCharacters.setText(userName.getTargetCharacter() + ", ");

        if (!TextUtils.isEmpty(userName.getTargetStyle()))
            mactvTargetStyles.setText(userName.getTargetStyle() + ", ");

        if (!TextUtils.isEmpty(userName.getMyCharacter()))
            mactvCharacters.setText(userName.getMyCharacter() + ", ");

        if (!TextUtils.isEmpty(userName.getMyStyle()))
            mactvStyles.setText(userName.getMyStyle() + ", ");

    }

    private void editAvatar() {
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectGender() {
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.activity_edit_profile_radio_male) {
                    tvGender.setText("Male");
                } else {
                    tvGender.setText("Female");
                }
            }
        });
    }


    private boolean checkLeapYear() {
        intSelectYear = wpDobYears.getCurrentYear();
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


    private ArrayList<String> getDateData(int arr) {
        String[] temp = getResources().getStringArray(arr);
        ArrayList<String> day = new ArrayList<String>(Arrays.asList(temp));
        return day;
    }

    private void changeDate() {
        wpDobMonths.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {
            }

            @Override
            public void onWheelSelected(int i) {
                tvAge.setText(getDateData(R.array.Month).get(wpDobMonths.getCurrentItemPosition()) + "/" +
                        tvAge.getText().toString().split("/")[1] + "/" + tvAge.getText().toString().split("/")[2]);
            }

            @Override
            public void onWheelScrollStateChanged(int i) {
                setDataforWhellPicker();
            }
        });

        wpDobYears.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                tvAge.setText(tvAge.getText().toString().split("/")[0] + "/" + tvAge.getText().toString().split("/")[1] +
                        "/" + wpDobYears.getSelectedYear());
            }

            @Override
            public void onWheelScrollStateChanged(int i) {
                setDataforWhellPicker();
            }
        });

        wpDobDays.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                tvAge.setText(tvAge.getText().toString().split("/")[0] + "/" +
                        getDateData(R.array.Day_31).get(wpDobDays.getCurrentItemPosition()) + "/" +
                        tvAge.getText().toString().split("/")[2]);
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });
    }

    private void setDataforWhellPicker() {
        intSelectMonth = wpDobMonths.getCurrentItemPosition();
        switch (intSelectMonth) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                wpDobDays.setData(getDateData(R.array.Day_31));
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                wpDobDays.setData(getDateData(R.array.Day_30));
                break;
            case 1:
                if (checkLeapYear()) {
                    wpDobDays.setData(getDateData(R.array.Day_29));
                } else {
                    wpDobDays.setData(getDateData(R.array.Day_28));
                }
        }
    }

    private void editMultiAutoCompleteTextView(MultiAutoCompleteTextView mactv, int source) {
        mactv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        ArrayAdapter hobbyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(source));
        mactv.setAdapter(hobbyAdapter);
        mactv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mactv.setText(AppUtils.convertStringToNFD(mactv.getText().toString()));
                mactv.setSelection(mactv.getText().toString().length());
            }
        });
    }
}
