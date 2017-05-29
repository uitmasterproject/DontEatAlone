package com.app.donteatalone.views.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.views.register.CustomViewPager;
import com.app.donteatalone.widgets.DialogCustom;

import static com.app.donteatalone.views.main.MainActivity.stringTemp;

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

    private DialogCustom dialogCustom;

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
        if(!stringTemp.equals(""))
        {
            tvName.setText(stringTemp);
        }
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
//                dialogCustom = new DialogCustom(viewGroup.getContext(), R.layout.custom_dialog_profile_name, "Edit Name");
//                dialogCustom.showDialogCustom();
              /* Truyen du lieu tu ProfileFragment => ProfileCustomDialogName*/
                Intent intent = new Intent(getContext(), ProfileCustomDialogName.class);
                intent.putExtra("layout", R.layout.custom_dialog_profile_name);
                intent.putExtra("value_current_name", tvName.getText().toString());
                startActivity(intent);

            }
        });

        rlAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rlGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });

        llHobbyCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        llHobbyStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        llCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
