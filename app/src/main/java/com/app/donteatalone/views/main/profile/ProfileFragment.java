package com.app.donteatalone.views.main.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.login.LoginActivity;
import com.app.donteatalone.views.main.profile.event_history.ProfileHistoryFragment;
import com.app.donteatalone.views.register.CustomViewPager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.joda.time.LocalDate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ChomChom on 4/13/2017.
 */

public class ProfileFragment extends Fragment implements PlaceSelectionListener {
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int REQUEST_SELECT_PLACE = 1000;

    private View viewGroup;
    private TabLayout tabLayout;
    private CustomViewPager customViewPager;

    private ImageView ivAvatar;
    private TextView tvName, tvAge, tvGender, tvPhone, tvAddress;
    private TextView tvHobbyFood, tvHobbyCharacter, tvHobbyStyle, tvCharacter;
    private RelativeLayout rlName, rlAge, rlGender;
    private LinearLayout llPhone, llAddress, llHobbyFood;
    private LinearLayout llHobbyCharacter, llHobbyStyle, llCharacter;
    private ImageButton ibtnExit;
    private ArrayList<Fragment> listFragment;
    private MySharePreference mySharePreference;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_profile, null);
        init();
        itemClick();
        clickButtonExit();

        FragmentManager manager = getChildFragmentManager();
        listFragment=new ArrayList<>();
        listFragment.add(new ProfileBlogFragment("ownViewProfile"));
        listFragment.add(new ProfileHistoryFragment("ownViewProfile"));
        tabLayout.setupWithViewPager(customViewPager);
        customViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        ProfileAdapter adapter = new ProfileAdapter(manager,listFragment);
        customViewPager.setAdapter(adapter);
        tabLayout.getTabAt(0).setText("Blog");
        tabLayout.getTabAt(1).setText("History");

        return viewGroup;
    }

    @Override
    public void onStart() {
        super.onStart();
        setDefaultValue();
    }

    private void init() {
        mySharePreference=new MySharePreference(getActivity());
        tabLayout = (TabLayout) viewGroup.findViewById(R.id.fragment_profile_tl_tabs);
        customViewPager = (CustomViewPager) viewGroup.findViewById(R.id.fragment_profile_vp_album_history);
        ibtnExit=(ImageButton) viewGroup.findViewById(R.id.fragment_profile_ibtn_exit);

        ivAvatar = (ImageView) viewGroup.findViewById(R.id.fragment_profile_iv_avatar);
        tvName = (TextView) viewGroup.findViewById(R.id.fragment_profile_tv_name);
        tvAge = (TextView) viewGroup.findViewById(R.id.fragment_profile_tv_age);
        tvGender = (TextView) viewGroup.findViewById(R.id.fragment_profile_tv_gender);
        tvPhone = (TextView) viewGroup.findViewById(R.id.fragment_profile_tv_phone);
        tvAddress = (TextView) viewGroup.findViewById(R.id.fragment_profile_tv_address);
        tvHobbyFood = (TextView) viewGroup.findViewById(R.id.fragment_profile_tv_hobby_food);
        tvHobbyCharacter = (TextView) viewGroup.findViewById(R.id.fragment_profile_tv_hobby_character);
        tvHobbyStyle = (TextView) viewGroup.findViewById(R.id.fragment_profile_tv_hobby_style);
        tvCharacter = (TextView) viewGroup.findViewById(R.id.fragment_profile_tv_character);

        rlName = (RelativeLayout) viewGroup.findViewById(R.id.fragment_profile_rl_name);
        rlAge = (RelativeLayout) viewGroup.findViewById(R.id.fragment_profile_rl_age);
        rlGender = (RelativeLayout) viewGroup.findViewById(R.id.fragment_profile_rl_gender);
        llPhone = (LinearLayout) viewGroup.findViewById(R.id.fragment_profile_ll_phone);
        llAddress = (LinearLayout) viewGroup.findViewById(R.id.fragment_profile_ll_address);
        llHobbyFood = (LinearLayout) viewGroup.findViewById(R.id.fragment_profile_ll_hobby_food);
        llHobbyCharacter = (LinearLayout) viewGroup.findViewById(R.id.fragment_profile_ll_hobby_character);
        llHobbyStyle = (LinearLayout) viewGroup.findViewById(R.id.fragment_profile_ll_hobby_style);
        llCharacter = (LinearLayout) viewGroup.findViewById(R.id.fragment_profile_ll_character);

        /* Set value for tvName from ProfileCustomDialogName => ProfileFragment*/

    }

    private void setDefaultValue() {
        LocalDate date = new LocalDate();
        ivAvatar.setImageBitmap(AppUtils.decodeBitmap(mySharePreference.getValue("avatarLogin")));
        tvName.setText(mySharePreference.getValue("fullnameLogin"));
        if(mySharePreference.getValue("birthdayLogin").equals(""))
            tvAge.setText("");
        else
            tvAge.setText((date.getYear() - Integer.parseInt(mySharePreference.getValue("birthdayLogin").split("/")[2])) + "");
        if (mySharePreference.getValue("genderLogin").equals("Female")) {
            tvGender.setText("F");
        } else {
            tvGender.setText("M");
        }
        tvPhone.setText(mySharePreference.getValue("phoneLogin"));
        if (mySharePreference.getValue("addressLogin").equals("")) {
            tvAddress.setText("Ho Chi Minh");
        } else {
            tvAddress.setText(mySharePreference.getValue("addressLogin"));
        }

        putDataHobbyintoReference();

        tvCharacter.setText(mySharePreference.getValue("characterLogin" + ""));
    }

    private void itemClick() {

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        rlName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom
                        (viewGroup.getContext(), R.layout.custom_dialog_profile_name, tvName);
                profileDialogCustom.showDialogCustom();

            }
        });

        rlAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom
                        (viewGroup.getContext(), R.layout.custom_dialog_profile_age, tvAge);
                profileDialogCustom.showDialogCustom();
            }
        });

        rlGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom
                        (viewGroup.getContext(), R.layout.custom_dialog_profile_gender, tvGender);
                profileDialogCustom.showDialogCustom();
            }
        });

        llAddress.setOnClickListener(new View.OnClickListener() {
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

        llHobbyFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(
                        viewGroup.getContext(), R.layout.custom_dialog_profile_hobby_food, tvHobbyFood);
                profileDialogCustom.showDialogCustom();
            }
        });

        llHobbyCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(
                        viewGroup.getContext(), R.layout.custom_dialog_profile_hobby_character, tvHobbyCharacter);
                profileDialogCustom.showDialogCustom();
            }
        });

        llHobbyStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(
                        viewGroup.getContext(), R.layout.custom_dialog_profile_hobby_style, tvHobbyStyle);
                profileDialogCustom.showDialogCustom();
            }
        });

        llCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(
                        viewGroup.getContext(), R.layout.custom_dialog_profile_character, tvCharacter);
                profileDialogCustom.showDialogCustom();
            }
        });
    }

    public void SelectImage() {
        final CharSequence[] options = {"Take photo", "Choose from Gallery", "Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);

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
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    Bitmap tempbitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    tempbitmap = Bitmap.createScaledBitmap(tempbitmap, 90, 90, true);
                    ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(
                            viewGroup.getContext(), R.layout.custom_dialog_profile_avatar,ivAvatar,tempbitmap);
                    profileDialogCustom.showDialogCustom();
                    String path = android.os.Environment.getExternalStorageDirectory().toString();
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        tempbitmap.compress(Bitmap.CompressFormat.JPEG, 90, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                final Uri imageUri = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap tempbitmap = BitmapFactory.decodeStream(imageStream);
                tempbitmap = Bitmap.createScaledBitmap(tempbitmap, 90, 90, true);
                ProfileDialogCustom profileDialogCustom = new ProfileDialogCustom(getContext(), R.layout.custom_dialog_profile_avatar,ivAvatar,tempbitmap);
                profileDialogCustom.showDialogCustom();
            } else if (requestCode == 3) {
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
            }
            else if (requestCode == REQUEST_SELECT_PLACE){
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                    this.onPlaceSelected(place);
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                    this.onError(status);
                }
            }
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        tvAddress.setText(getString(R.string.formatted_place_data,place.getName(),place.getAddress()));
        mySharePreference.setValue("addressLogin",tvAddress.getText().toString());
        mySharePreference.setValue("latlngaddressLogin",place.getLatLng().toString().substring(10,place.getLatLng().toString().length()-1));
        ProfileDialogCustom dialogCustom=new ProfileDialogCustom(getActivity());
        dialogCustom.saveDataAddress(tvAddress.getText().toString(),place.getLatLng().toString().substring(10,place.getLatLng().toString().length()-1));
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(getContext(), "Place selection failed: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
    }

    private void putDataHobbyintoReference(){
        String str="";
        String[] temp =mySharePreference.getValue("hobbyLogin").trim().split(",");
        String[] temhobby=getResources().getStringArray(R.array.food);
        for(int j=0;j<temhobby.length;j++) {
            for (int i = 0; i < temp.length; i++) {
                if (AppUtils.convertStringToNFD(temhobby[j]).equals(temp[i])){
                    str+=temp[i]+",";
                }
            }
        }
        if(str.length()>0) {
            str=str.substring(0, str.length() - 1);
        }
        tvHobbyFood.setText(str);

        str="";
        temhobby=getResources().getStringArray(R.array.character);
        for(int j=0;j<temhobby.length;j++) {
            for (int i = 0; i < temp.length; i++) {
                if (AppUtils.convertStringToNFD(temhobby[j]).equals(temp[i])){
                    str+=temp[i]+",";
                }
            }
        }
        if(str.length()>0) {
            str=str.substring(0, str.length() - 1);
        }
        tvHobbyCharacter.setText(str);

        str="";
        temhobby=getResources().getStringArray(R.array.style);
        for(int j=0;j<temhobby.length;j++) {
            for (int i = 0; i < temp.length; i++) {
                if (AppUtils.convertStringToNFD(temhobby[j]).equals(temp[i])){
                    str+=temp[i]+",";
                }
            }
        }
        if(str.length()>0) {
            str=str.substring(0, str.length() - 1);
        }
        tvHobbyStyle.setText(str);
    }

    private void clickButtonExit(){
        ibtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}
