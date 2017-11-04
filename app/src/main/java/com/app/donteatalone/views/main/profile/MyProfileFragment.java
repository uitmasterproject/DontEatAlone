package com.app.donteatalone.views.main.profile;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Achievement;
import com.app.donteatalone.model.ProfileHistoryModel;
import com.app.donteatalone.model.UserName;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.login.LoginActivity;
import com.app.donteatalone.views.main.profile.event_history.ProfileHistoryAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 4/13/2017
 */

public class MyProfileFragment extends Fragment {
    private View viewGroup;

    private ImageView ivAvatar;
    private TextView tvNames, tvName, tvAge, tvGender;
    private TextView tvPhone, tvAddress, tvCharacters, tvStyles;
    private TextView tvTargetCharacters, tvTagetFoods, tvTargetStyles;
    private RelativeLayout rlBack;
    private TextView tvCountsLike, tvCountsAppointment, tvCountsStar;
    private LinearLayout llEditProfile;
    private RecyclerView rvHistory;

    private UserName userName;

    private ProfileHistoryAdapter profileHistoryAdapter;
    private ArrayList<ProfileHistoryModel> listProfileHistory;

    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_my_profile, null);
        init();
        itemClick();
        clickButtonExit();
        return viewGroup;
    }

    @Override
    public void onStart() {
        super.onStart();
        setDefaultValue();
    }

    private void init() {
        userName = new MySharePreference(getActivity()).createObject();

        rlBack = (RelativeLayout) viewGroup.findViewById(R.id.fragment_my_profile_rl_back);
        tvNames = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_name_s);

        /*Personal information*/
        ivAvatar = (ImageView) viewGroup.findViewById(R.id.fragment_my_profile_iv_avatar);
        tvName = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_name);
        tvAge = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_age);
        tvGender = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_gender);

        /*Achievements*/
        tvCountsLike = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_counts_like);
        tvCountsAppointment = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_counts_appointment);
        tvCountsStar = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_counts_star);

        /*Edit profile button*/
        llEditProfile = (LinearLayout) viewGroup.findViewById(R.id.fragment_my_profile_ll_edit_profile);

        /*Detail Information*/
        tvAddress = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_address);
        tvCharacters = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_character);
        tvStyles = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_styles);
        tvTagetFoods = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_target_foods);
        tvPhone = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_phone);

        /*Target Information*/
        tvTargetCharacters = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_target_characters);
        tvTargetStyles = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_target_styles);

        /*History*/
        rvHistory = (RecyclerView) viewGroup.findViewById(R.id.fragment_my_profile_rv_history);

        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        listProfileHistory = new ArrayList<>();
        profileHistoryAdapter = new ProfileHistoryAdapter(listProfileHistory, getContext());

        rvHistory.setAdapter(profileHistoryAdapter);

        getEventHistory();

        getAchievement();

    }

    private void getAchievement() {
        if (userName.getPhone() != null) {
            Call<Achievement> getAchievement = Connect.getRetrofit().getAchievement(userName.getPhone());
            getAchievement.enqueue(new Callback<Achievement>() {
                @Override
                public void onResponse(Call<Achievement> call, Response<Achievement> response) {
                    if (response.body() != null) {
                        Achievement achievement = response.body();
                        tvCountsLike.setText(achievement.getLike() + "");
                        tvCountsAppointment.setText(achievement.getAppointment() + "");
                        tvCountsStar.setText(achievement.getRate() + "");
                    }
                }

                @Override
                public void onFailure(Call<Achievement> call, Throwable t) {

                }
            });
        }
    }

    private void getEventHistory() {
        Call<ArrayList<ProfileHistoryModel>> getEventHistory = Connect.getRetrofit().getEventHistory(new MySharePreference(getActivity()).getValue("phoneLogin"));
        getEventHistory.enqueue(new Callback<ArrayList<ProfileHistoryModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProfileHistoryModel>> call, Response<ArrayList<ProfileHistoryModel>> response) {
                if (response.body() != null) {
                    listProfileHistory.clear();
                    listProfileHistory.addAll(response.body());

                    profileHistoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProfileHistoryModel>> call, Throwable t) {
            }
        });
    }

    private void setDefaultValue() {

        ivAvatar.setImageBitmap(AppUtils.decodeBitmap(userName.getAvatar()));
        tvName.setText(userName.getFullName());
        tvNames.setText(userName.getFullName());
        tvAge.setText(userName.getAge() + "");
        tvGender.setText(userName.getFormatGender());
        tvPhone.setText(userName.getPhone());
        tvAddress.setText(userName.getAddress());


        putDataHobbyIntoReference();

        putDataPersonalIntoReference();
    }

    private void itemClick() {

        llEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }


    private void putDataHobbyIntoReference() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTagetFoods.setText(Html.fromHtml("Food's Target are <font color='#000'>" + userName.getTargetFood() + "</font>", Html.FROM_HTML_MODE_COMPACT));
            tvTargetCharacters.setText(Html.fromHtml("Character's Target are <font color='#000'>" + userName.getTargetCharacter() + "</font>", Html.FROM_HTML_MODE_COMPACT));
            tvTargetStyles.setText(Html.fromHtml("Style's Target are <font color='#000'>" + userName.getTargetStyle() + "</font>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvTagetFoods.setText(Html.fromHtml("Food's Target are <font color='#000'>" + userName.getTargetFood() + "</font>"));
            tvTargetCharacters.setText(Html.fromHtml("Character's Target are <font color='#000'>" + userName.getTargetCharacter() + "</font>"));
            tvTargetStyles.setText(Html.fromHtml("Style's Target are <font color='#000'>" + userName.getTargetStyle() + "</font>"));
        }
    }

    private void putDataPersonalIntoReference() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvCharacters.setText(Html.fromHtml("Characters are <font color='#000'>" + userName.getMyCharacter() + "</font>", Html.FROM_HTML_MODE_COMPACT));
            tvStyles.setText(Html.fromHtml("Styles are <font color='#000'>" + userName.getMyStyle() + "</font>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvCharacters.setText(Html.fromHtml("Characters are <font color='#000'>" + userName.getMyCharacter() + "</font>"));
            tvStyles.setText(Html.fromHtml("Styles are <font color='#000'>" + userName.getMyStyle() + "</font>"));
        }
    }

    private void clickButtonExit() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}
