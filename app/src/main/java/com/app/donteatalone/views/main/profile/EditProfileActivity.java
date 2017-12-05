package com.app.donteatalone.views.main.profile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.model.UserName;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.ImageProcessor;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.MainActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.donteatalone.R.id.activity_edit_profile_et_name;
import static com.app.donteatalone.utils.ImageProcessor.PERMISSION_CAMERA_REQUEST_CODE;
import static com.app.donteatalone.utils.ImageProcessor.PERMISSION_READ_REQUEST_CODE;
import static com.app.donteatalone.utils.ImageProcessor.RESULT_CHOOSE_IMAGE;
import static com.app.donteatalone.utils.ImageProcessor.RESULT_TAKE_PHOTO;
import static com.app.donteatalone.views.main.MainActivity.ARG_EDIT_PROFILE_ACTIVITY;
import static com.app.donteatalone.views.main.MainActivity.ARG_FROM_VIEW;

/**
 * Created by Delbert on 9/23/2017
 */

public class EditProfileActivity extends AppCompatActivity implements PlaceSelectionListener, View.OnClickListener, View.OnTouchListener {

    private static final int REQUEST_SELECT_PLACE = 1000;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private ImageView ivCancel, ivSave;
    private ImageView ivAvatar;
    private LinearLayout llEditAvatar, llGender, llDoB, llDobCustomize;
    private EditText etName;
    private RadioGroup radioGroupGender;
    private WheelPicker wpDobMonths, wpDobDays;
    private WheelYearPicker wpDobYears;
    private MultiAutoCompleteTextView mactvCharacters, mactvStyles, mactvTagetFoods, mactvTargetCharacters, mactvTargetStyles;
    private TextView tvGender, tvAge, tvAddress;
    private int intSelectYear, intSelectMonth;
    private UserName userName;
    private String latlngAddress;

    private boolean editAvatar = false;

    private StorageReference storageRefImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        ivCancel = (ImageView) findViewById(R.id.activity_edit_profile_iv_cancel);
        ivSave = (ImageView) findViewById(R.id.activity_edit_profile_iv_save);
        /*Personal Information*/
        ivAvatar = (ImageView) findViewById(R.id.activity_edit_profile_iv_avatar);
        llEditAvatar = (LinearLayout) findViewById(R.id.activity_edit_profile_ll_edit_avatar);
        etName = (EditText) findViewById(activity_edit_profile_et_name);
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

        etName.setOnClickListener(this);
        mactvCharacters.setOnClickListener(this);
        mactvStyles.setOnClickListener(this);
        mactvTagetFoods.setOnClickListener(this);
        mactvTargetCharacters.setOnClickListener(this);
        mactvTargetStyles.setOnClickListener(this);

        mactvCharacters.setOnTouchListener(this);
        mactvStyles.setOnTouchListener(this);
        mactvTagetFoods.setOnTouchListener(this);
        mactvTargetCharacters.setOnTouchListener(this);
        mactvTargetStyles.setOnTouchListener(this);
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

                    SimpleDateFormat format = new SimpleDateFormat("MMMM/dd/yyyy", Locale.ENGLISH);
                    Date date = null;
                    try {
                        date = format.parse(tvAge.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    if (date != null) {
                        wpDobDays.setSelectedItemPosition(calendar.get(Calendar.DATE) - 1);
                        wpDobMonths.setSelectedItemPosition(calendar.get(Calendar.MONTH));
                        wpDobYears.setSelectedYear(calendar.get(Calendar.YEAR));
                    }

                } else {
                    llDobCustomize.animate().translationY(0).setDuration(500);
                    llDobCustomize.setVisibility(View.GONE);
                }
            }
        });

        /*Toolbar item*/
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideSoftKeyboard(EditProfileActivity.this);
                BaseProgress baseProgress = new BaseProgress();

                SimpleDateFormat format = new SimpleDateFormat("MMMM/dd/yyyy", Locale.ENGLISH);
                Date date = null;
                try {
                    date = format.parse(tvAge.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    if (Calendar.getInstance().get(Calendar.YEAR) - calendar.get(Calendar.YEAR) >= 15) {

                        baseProgress.showProgressLoading(EditProfileActivity.this);
                        if (editAvatar) {
                            if (AppUtils.isNetworkAvailable(EditProfileActivity.this)) {
                                storageRefImage = FirebaseStorage.getInstance().getReferenceFromUrl(MainActivity.URL_STORAGE_FIRE_BASE).
                                        child(MainActivity.REGISTER_PATH_STORAGE_FIRE_BASE + userName.getPhone());
                                UploadTask uploadTask = storageRefImage.putBytes(ImageProcessor.convertBitmapToByte(((BitmapDrawable) ivAvatar.getDrawable()).getBitmap()));
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        @SuppressWarnings("VisibleForTests")
                                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                        if (downloadUrl != null) {
                                            userName.setAvatar(downloadUrl.toString());
                                        }

                                        setValueAfterEdit(baseProgress);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(EditProfileActivity.this, getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            setValueAfterEdit(baseProgress);
                        }
                    } else {
                        Toast.makeText(EditProfileActivity.this, getString(R.string.tutorial_step_4), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private void setValueAfterEdit(BaseProgress baseProgress) {
        userName.setFullName(StringEscapeUtils.escapeJava(etName.getText().toString()));
        userName.setGender(tvGender.getText().toString());
        userName.setBirthday(tvAge.getText().toString());
        userName.setAddress(StringEscapeUtils.escapeJava(tvAddress.getText().toString()));
        if(latlngAddress!=null) {
            userName.setLatlngAdress(latlngAddress);
        }
        if (mactvCharacters.getText().toString().trim().endsWith(",")) {
            userName.setMyCharacter(StringEscapeUtils.escapeJava(mactvCharacters.getText().toString().substring(0, mactvCharacters.getText().toString().lastIndexOf(","))));
        } else {
            userName.setMyCharacter(StringEscapeUtils.escapeJava(mactvCharacters.getText().toString()));
        }

        if (mactvStyles.getText().toString().trim().endsWith(",")) {
            userName.setMyStyle(StringEscapeUtils.escapeJava(mactvStyles.getText().toString().substring(0, mactvStyles.getText().toString().lastIndexOf(","))));
        } else {
            userName.setMyStyle(StringEscapeUtils.escapeJava(mactvStyles.getText().toString()));
        }

        if (mactvTargetCharacters.getText().toString().trim().endsWith(",")) {
            userName.setTargetCharacter(StringEscapeUtils.escapeJava(mactvTargetCharacters.getText().toString().substring(0, mactvTargetCharacters.getText().toString().lastIndexOf(","))));
        } else {
            userName.setTargetCharacter(StringEscapeUtils.escapeJava(mactvTargetCharacters.getText().toString()));
        }

        if (mactvTargetStyles.getText().toString().trim().endsWith(",")) {
            userName.setTargetStyle(StringEscapeUtils.escapeJava(mactvTargetStyles.getText().toString().substring(0, mactvTargetStyles.getText().toString().lastIndexOf(","))));
        } else {
            userName.setTargetStyle(StringEscapeUtils.escapeJava(mactvTargetStyles.getText().toString()));
        }

        if (mactvTagetFoods.getText().toString().trim().endsWith(",")) {
            userName.setTargetFood(StringEscapeUtils.escapeJava(mactvTagetFoods.getText().toString().substring(0, mactvTagetFoods.getText().toString().lastIndexOf(","))));
        } else {
            userName.setTargetFood(StringEscapeUtils.escapeJava(mactvTagetFoods.getText().toString()));
        }
        Call<Status> editProfile = Connect.getRetrofit().editProfile(userName);
        editProfile.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                baseProgress.hideProgressLoading();
                if (response.body() != null) {
                    if (response.body().getStatus().equals("0")) {

                        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                        intent.putExtra(ARG_FROM_VIEW, ARG_EDIT_PROFILE_ACTIVITY);

                        new MySharePreference(EditProfileActivity.this).saveAccountInfo(userName);

                        startActivity(intent);
                    }else {
                        Toast.makeText(EditProfileActivity.this, getString(R.string.not_update), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                baseProgress.hideProgressLoading();
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
        final CharSequence[] options = getResources().getStringArray(R.array.option);
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);

        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
                            }
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, RESULT_TAKE_PHOTO);
                        }
                        break;
                    case 1:
                        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_REQUEST_CODE);
                            }
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent, RESULT_CHOOSE_IMAGE);
                        }
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        if (dialog.getWindow() != null && window != null) {
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            layoutParams.x = 0;
            layoutParams.y = 0;
            layoutParams.alpha = 0.8f;
            window.setAttributes(layoutParams);
        }
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PLACE) {
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(EditProfileActivity.this, data);
                    this.onPlaceSelected(place);
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    com.google.android.gms.common.api.Status status = PlaceAutocomplete.getStatus(EditProfileActivity.this, data);
                    this.onError(status);
                }
            } else {
                editAvatar = true;
                ImageProcessor.activityImageResult(resultCode, requestCode, data, null, EditProfileActivity.this, ivAvatar);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ImageProcessor.requestPermissionsResult(requestCode, grantResults[0], null, EditProfileActivity.this);

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
        userName = new MySharePreference(EditProfileActivity.this).createObjectLogin();
        if (!TextUtils.isEmpty(userName.getAvatar())) {
            Picasso.with(EditProfileActivity.this)
                    .load(userName.getAvatar())
                    .into(ivAvatar);
        } else {
            if (userName.getFormatGender().equals("F")) {
                ivAvatar.setImageResource(R.drawable.avatar_woman);
            } else {
                ivAvatar.setImageResource(R.drawable.avatar_man);
            }
        }
        etName.setText(StringEscapeUtils.unescapeJava(userName.getFullName()));
        tvAge.setText(userName.getBirthday());
        tvGender.setText(userName.getGender());
        tvAddress.setText(StringEscapeUtils.unescapeJava(userName.getAddress()));

        mactvTagetFoods.setText(StringEscapeUtils.unescapeJava(userName.getTargetFood()));
        mactvTargetCharacters.setText(StringEscapeUtils.unescapeJava(userName.getTargetCharacter()));
        mactvTargetStyles.setText(StringEscapeUtils.unescapeJava(userName.getTargetStyle()));

        mactvCharacters.setText(StringEscapeUtils.unescapeJava(userName.getMyCharacter()));
        mactvStyles.setText(StringEscapeUtils.unescapeJava(userName.getMyStyle()));

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
                setDataForWheelPicker();
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
                setDataForWheelPicker();
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

    private void setDataForWheelPicker() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_edit_profile_et_name:
                etName.setSelection(etName.getText().toString().length());
                break;
            case R.id.activity_edit_profile_mactv_characters:
                editInfo(mactvCharacters);
                break;
            case R.id.activity_edit_profile_mactv_styles:
                editInfo(mactvStyles);
                break;
            case R.id.activity_edit_profile_mactv_target_foods:
                editInfo(mactvTagetFoods);
                break;
            case R.id.activity_edit_profile_mactv_target_characters:
                editInfo(mactvTargetCharacters);
                break;
            case R.id.activity_edit_profile_mactv_target_styles:
                editInfo(mactvTargetStyles);
                break;
        }
    }

    private void editInfo(MultiAutoCompleteTextView multi) {
        if (!TextUtils.isEmpty(multi.getText().toString()) && !multi.getText().toString().trim().endsWith(",")) {
            multi.setText(multi.getText().toString() + ", ");
        }
        multi.setSelection(multi.getText().toString().length());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.activity_edit_profile_mactv_characters:
                editInfo(mactvCharacters);
                break;
            case R.id.activity_edit_profile_mactv_styles:
                editInfo(mactvStyles);
                break;
            case R.id.activity_edit_profile_mactv_target_foods:
                editInfo(mactvTagetFoods);
                break;
            case R.id.activity_edit_profile_mactv_target_characters:
                editInfo(mactvTargetCharacters);
                break;
            case R.id.activity_edit_profile_mactv_target_styles:
                editInfo(mactvTargetStyles);
                break;
        }
        return false;
    }
}
