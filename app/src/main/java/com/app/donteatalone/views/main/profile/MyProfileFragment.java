package com.app.donteatalone.views.main.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Achievement;
import com.app.donteatalone.model.InfoProfileUpdate;
import com.app.donteatalone.model.ProfileHistoryModel;
import com.app.donteatalone.model.UserName;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.profile.event_history.ProfileHistoryAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 4/13/2017
 */

public class MyProfileFragment extends Fragment implements View.OnClickListener {
    private View viewGroup;

    private ImageView ivAvatar;
    private TextView tvAge, tvGender, tvName;
    private TextView tvPhone, tvAddress, tvCharacters, tvStyles;
    private TextView tvTargetCharacters, tvTargetFoods, tvTargetStyles;
    private TextView tvCountsLike, tvCountsAppointment, tvCountsStar;
    private LinearLayout llEditProfile;
    private RecyclerView rvHistory;

    private LinearLayout llEmptyHistory;

    private LinearLayout llDisplayCharacter;
    private LinearLayout llDisplayStyle;
    private LinearLayout llDisplayTargetCharacter;
    private LinearLayout llDisplayTargetStyle;
    private LinearLayout llDisplayTargetFood;
    private RelativeLayout rlEdit;

    private MultiAutoCompleteTextView mactvEdit;
    private ImageView imgSave;
    private ImageView imgRefresh;
    private String contentBeforeEdit;

    private ProgressBar progressBar;

    private Target target;

    private ImageView imgReLoad;

    private UserName userName;

    private Animation animation;


    private ProfileHistoryAdapter profileHistoryAdapter;
    private ArrayList<ProfileHistoryModel> listProfileHistory;

    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_my_profile, container, false);

        init();

        getEventHistory(false);

        getAchievement();

        itemClick();
        return viewGroup;
    }


    @Override
    public void onStart() {
        super.onStart();
        setDefaultValue();
    }

    private void init() {

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
        tvTargetFoods = (TextView) viewGroup.findViewById(R.id.fragment_my_profile_tv_target_foods);
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

        llEmptyHistory = (LinearLayout) viewGroup.findViewById(R.id.fragment_history_ll_entry);

        llDisplayCharacter = (LinearLayout) viewGroup.findViewById(R.id.ll_display_character);
        llDisplayCharacter.setOnClickListener(this);

        llDisplayStyle = (LinearLayout) viewGroup.findViewById(R.id.ll_display_style);
        llDisplayStyle.setOnClickListener(this);

        llDisplayTargetFood = (LinearLayout) viewGroup.findViewById(R.id.ll_display_target_food);
        llDisplayTargetFood.setOnClickListener(this);

        llDisplayTargetCharacter = (LinearLayout) viewGroup.findViewById(R.id.ll_display_target_character);
        llDisplayTargetCharacter.setOnClickListener(this);

        llDisplayTargetStyle = (LinearLayout) viewGroup.findViewById(R.id.ll_display_target_style);
        llDisplayTargetStyle.setOnClickListener(this);

        progressBar = (ProgressBar) viewGroup.findViewById(R.id.progress);

        animation= new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(1000);
        animation.setDuration(2000);

        imgReLoad = (ImageView) viewGroup.findViewById(R.id.img_reload);

        imgReLoad.setOnClickListener(this);
    }

    private void getAchievement() {

        if (new MySharePreference(getActivity()).getPhoneLogin() != null) {
            Call<Achievement> getAchievement = Connect.getRetrofit().getAchievement(new MySharePreference(getActivity()).getPhoneLogin());
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

    private void getEventHistory(final boolean rotate) {
        Call<ArrayList<ProfileHistoryModel>> getEventHistory = Connect.getRetrofit().getEventHistory(new MySharePreference(getActivity()).getPhoneLogin());
        getEventHistory.enqueue(new Callback<ArrayList<ProfileHistoryModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProfileHistoryModel>> call, Response<ArrayList<ProfileHistoryModel>> response) {
                if(rotate){
                    imgReLoad.clearAnimation();
                }
                if (response.body() != null && response.body().size() > 0) {
                    llEmptyHistory.setVisibility(View.GONE);

                    rvHistory.setVisibility(View.VISIBLE);

                    listProfileHistory.clear();
                    listProfileHistory.addAll(response.body());

                    profileHistoryAdapter.notifyDataSetChanged();
                } else {
                    llEmptyHistory.setVisibility(View.VISIBLE);

                    rvHistory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProfileHistoryModel>> call, Throwable t) {
                if(rotate){
                    imgReLoad.clearAnimation();
                }
            }
        });
    }

    private void setDefaultValue() {
        userName = new MySharePreference(getActivity()).createObjectLogin();

        progressBar.setVisibility(View.VISIBLE);

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ivAvatar.setImageBitmap(bitmap);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        if (!TextUtils.isEmpty(userName.getAvatar())) {
            Picasso.with(getActivity())
                    .load(userName.getAvatar())
                    .into(target);
        } else {
            progressBar.setVisibility(View.GONE);
            if (userName.getFormatGender().equals("F")) {
                ivAvatar.setImageResource(R.drawable.avatar_woman);
            } else {
                ivAvatar.setImageResource(R.drawable.avatar_man);
            }
        }
        tvName.setText(StringEscapeUtils.unescapeJava(userName.getFullName()));
        tvAge.setText(userName.getAge() + "");
        tvGender.setText(userName.getFormatGender());
        tvPhone.setText(setMultiColorText(getResources().getString(R.string.phone_is), userName.getPhone()));
        tvAddress.setText(setMultiColorText(getResources().getString(R.string.live_in), userName.getAddress()));


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
        tvTargetFoods.setText(setMultiColorText(getResources().getString(R.string.food), userName.getTargetFood()));
        tvTargetCharacters.setText(setMultiColorText(getResources().getString(R.string.target_character), userName.getTargetCharacter()));
        tvTargetStyles.setText(setMultiColorText(getResources().getString(R.string.target_style), userName.getTargetStyle()));
    }

    private void putDataPersonalIntoReference() {

        tvCharacters.setText(setMultiColorText(getResources().getString(R.string.my_characters), userName.getMyCharacter()));
        tvStyles.setText(setMultiColorText(getResources().getString(R.string.my_styles), userName.getMyStyle()));

    }

    private Spannable setMultiColorText(String defaultString, String text) {
        Spannable spannable = new SpannableString(defaultString + StringUtils.capitalize(StringEscapeUtils.unescapeJava(text)));

        spannable.setSpan(new ForegroundColorSpan(Color.BLACK), defaultString.length(), defaultString.length() + StringEscapeUtils.unescapeJava(text).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_display_character:
                editInfo(R.id.rl_edit_character, R.id.mactv_my_character, R.array.character, R.string.my_characters,
                        R.id.img_save_character, R.id.refresh_character, llDisplayCharacter, tvCharacters);
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
            case R.id.img_save_character:
                saveInfo(llDisplayCharacter, tvCharacters, R.string.my_characters);
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
            case R.id.ll_display_style:
                editInfo(R.id.rl_edit_style, R.id.mactv_my_style, R.array.style, R.string.my_styles,
                        R.id.img_save_style, R.id.refresh_style, llDisplayStyle, tvStyles);
                break;
            case R.id.img_save_style:
                saveInfo(llDisplayStyle, tvStyles, R.string.my_styles);
                break;
            case R.id.ll_display_target_food:
                editInfo(R.id.rl_edit_target_food, R.id.mactv_target_food, R.array.food, R.string.food,
                        R.id.img_save_target_food, R.id.refresh_target_food, llDisplayTargetFood, tvTargetFoods);
                break;
            case R.id.img_save_target_food:
                saveInfo(llDisplayTargetFood, tvTargetFoods, R.string.food);
                break;
            case R.id.ll_display_target_character:
                editInfo(R.id.rl_edit_target_character, R.id.mactv_target_character, R.array.character,
                        R.string.target_character, R.id.img_save_target_character, R.id.refresh_target_character,
                        llDisplayTargetCharacter, tvTargetCharacters);
                break;
            case R.id.img_save_target_character:
                saveInfo(llDisplayTargetCharacter, tvTargetCharacters, R.string.target_character);
                break;
            case R.id.ll_display_target_style:
                editInfo(R.id.rl_edit_target_style, R.id.mactv_target_style, R.array.style, R.string.target_style,
                        R.id.img_save_target_style, R.id.refresh_target_style, llDisplayTargetStyle, tvTargetStyles);
                break;
            case R.id.img_save_target_style:
                saveInfo(llDisplayTargetStyle, tvTargetStyles, R.string.target_style);
                break;
            case R.id.img_reload:
                imgReLoad.setAnimation(animation);
                getEventHistory(true);
                break;
        }
    }

    private void editInfo(int rlEditSource, int macTvEditSource, int listDataSource, int titleDefaultSource,
                          int imgSaveSource, int imgRefreshSource, LinearLayout llDisplay, TextView tvContent) {

        AppUtils.hideSoftKeyboard(getActivity());

        int [] listEdit =new int[]{R.id.rl_edit_character, R.id.rl_edit_style, R.id.rl_edit_target_food,
        R.id.rl_edit_target_character, R.id.rl_edit_target_style};
        LinearLayout [] listDisplay=new LinearLayout[]{llDisplayCharacter, llDisplayStyle, llDisplayTargetFood,
        llDisplayTargetCharacter, llDisplayTargetStyle};

        for(int i = 0; i< listEdit.length; i++){
            if(listEdit[i]!=rlEditSource){
                RelativeLayout rl=(RelativeLayout) viewGroup.findViewById(listEdit[i]);
                if(rl.getVisibility()==View.VISIBLE){
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

        contentBeforeEdit = tvContent.getText().toString().substring(getResources().getString(titleDefaultSource).length());
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
        final MySharePreference mySharePreference=new MySharePreference(getActivity());
        if(AppUtils.isNetworkAvailable(getActivity())) {
            final InfoProfileUpdate infoUpdate = new InfoProfileUpdate(getActivity());
            infoUpdate.setPhone(userName.getPhone());

            if (mactvEdit.getText().toString().trim().endsWith(",")) {
                infoUpdate.setContent(StringUtils.capitalize(StringEscapeUtils.escapeJava(mactvEdit.getText().toString().trim().substring(0, mactvEdit.getText().toString().trim().lastIndexOf(",")))));
            } else {
                infoUpdate.setContent(StringUtils.capitalize(StringEscapeUtils.escapeJava(mactvEdit.getText().toString())));
            }

            switch (titleDefaultResource) {
                case R.string.my_characters:
                    infoUpdate.setType("myCharacter");
                    break;
                case R.string.my_styles:
                    infoUpdate.setType("myStyle");
                    break;
                case R.string.food:
                    infoUpdate.setType("targetFood");
                    break;
                case R.string.target_character:
                    infoUpdate.setType("targetCharacter");
                    break;
                case R.string.target_style:
                    infoUpdate.setType("targetStyle");
                    break;
            }

            Call<UserName> updateInfo = Connect.getRetrofit().updateProfile(infoUpdate);
            updateInfo.enqueue(new Callback<UserName>() {
                @Override
                public void onResponse(Call<UserName> call, Response<UserName> response) {
                    if(response.body()!=null){
                        if(response.body().getUuid().equals(mySharePreference.getUUIDLogin())){
                            switch (titleDefaultResource) {
                                case R.string.my_characters:
                                    mySharePreference.setMyCharacterLogin(infoUpdate.getContent());
                                    break;
                                case R.string.my_styles:
                                    mySharePreference.setMyStyleLogin(infoUpdate.getContent());
                                    break;
                                case R.string.food:
                                    mySharePreference.setTargetFoodLogin(infoUpdate.getContent());
                                    break;
                                case R.string.target_character:
                                    mySharePreference.setTargetCharacterLogin(infoUpdate.getContent());
                                    break;
                                case R.string.target_style:
                                    mySharePreference.setTargetStyleLogin(infoUpdate.getContent());
                                    break;
                            }

                        }else {
                            UserName userName =response.body();
                            mySharePreference.saveProfileUpdate(userName);
                        }

                        llDisplay.setVisibility(View.VISIBLE);

                        tvContent.setText(setMultiColorText(getResources().getString(titleDefaultResource), infoUpdate.getContent()));

                        rlEdit.setVisibility(View.GONE);

                        AppUtils.hideSoftKeyboard(getActivity());
                    }else {
                        Toast.makeText(getActivity(), getString(R.string.not_update),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserName> call, Throwable t) {

                }
            });

        }else {
            Toast.makeText(getActivity(),getString(R.string.invalid_network),Toast.LENGTH_SHORT).show();
        }
    }

}
