package com.app.donteatalone.views.main.profile;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.views.register.CustomViewPager;

import org.joda.time.LocalDate;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ChomChom on 4/13/2017.
 */

public class ProfileFragment extends Fragment {
    private View viewGroup;
    private TabLayout tabLayout;
    private CustomViewPager customViewPager;

    private ImageView ivAvatar;
    private TextView tvName, tvAge, tvGender, tvPhone, tvAddress;
    private TextView tvHobbyFood, tvHobbyCharacter, tvHobbyStyle, tvCharacter;
    private RelativeLayout rlName, rlAge, rlGender;
    private LinearLayout llPhone, llAddress, llHobbyFood;
    private LinearLayout llHobbyCharacter, llHobbyStyle, llCharacter;

    private ProfileDialogCustom dialogCustom;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_profile, null);
        init();
        itemClick();

        FragmentManager manager = getFragmentManager();
        ProfileAdapter adapter = new ProfileAdapter(manager);
        customViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(customViewPager);
        customViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);

        return viewGroup;
    }

    @Override
    public void onStart() {
        super.onStart();
        setDefaultValue();
    }

    private void init() {
        tabLayout = (TabLayout) viewGroup.findViewById(R.id.fragment_profile_tl_tabs);
        customViewPager = (CustomViewPager) viewGroup.findViewById(R.id.fragment_profile_vp_album_history);

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
        ivAvatar.setImageBitmap(decodeBitmap());
        tvName.setText(storeReference("fullnameLogin"));
        tvAge.setText(date.getYear() - Integer.parseInt(storeReference("birthdayLogin").split("/")[2]) + "");
        if (storeReference("genderLogin").equals("Female") == true) {
            tvGender.setText("F");
        } else {
            tvGender.setText("M");
        }
        tvPhone.setText(storeReference("phoneLogin"));
        if (storeReference("addressLogin").equals("") == true) {
            tvAddress.setText("Hồ Chí Minh");
        } else {
            tvAddress.setText(storeReference("addressLogin"));
        }

        tvHobbyFood.setText(storeReference("hobbyLogin" + ""));
        tvHobbyCharacter.setText(storeReference("hobbyLogin" + ""));
        tvHobbyStyle.setText(storeReference("hobbyLogin" + ""));

        tvCharacter.setText(storeReference("characterLogin" + ""));
    }

    private void itemClick() {

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    private String storeReference(String str) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("account", MODE_PRIVATE);
        String avatar = sharedPreferences.getString(str, "");
        return avatar;
    }

    private Bitmap decodeBitmap() {
        String avatar = storeReference("avatarLogin");
        Bitmap bitmap = null;
        if (avatar.equals("") != true) {
            try {
                byte[] encodeByte = Base64.decode(avatar, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return bitmap;
    }
}
