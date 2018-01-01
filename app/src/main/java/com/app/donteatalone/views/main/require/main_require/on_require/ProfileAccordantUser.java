package com.app.donteatalone.views.main.require.main_require.on_require;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Achievement;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.model.UserName;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.profile.ProfileBlogFragment;
import com.app.donteatalone.views.main.profile.event_history.ProfileHistoryFragment;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.LocalDate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.donteatalone.views.main.blog.DetailBlogActivity.ARG_PHONE_NUMBER;

/**
 * Created by ChomChom on 01-Aug-17
 */

public class ProfileAccordantUser extends AppCompatActivity {

    private ImageView ivAvatar, ivLike;
    private TextView tvName1, tvName2, tvName, tvAge, tvGender, tvPhone, tvAddress;
    private TextView tvTargetCharacters, tvTargetStyles, tvCharacters, tvStyles, tvTargetFoods;
    private RelativeLayout rlBack;
    private TextView tvCountsLike, tvCountsAppointment, tvCountsStar;
    private LinearLayout llButtonLike;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BaseProgress dialog;

    private String phoneNumber;

    private String myPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accordant_user_profile);

        if (getIntent().getStringExtra(ARG_PHONE_NUMBER) != null) {
            phoneNumber = getIntent().getStringExtra(ARG_PHONE_NUMBER);
        }

        init();
        getValueProfilefromDatabase();
        setClickButtonExit();
        setClickButtonLike();
        setValueViewPager();
        setValueTabLayout();
    }

    private void init() {
        myPhone = new MySharePreference(ProfileAccordantUser.this).getPhoneLogin();
        /*Toolbar*/
        rlBack = (RelativeLayout) findViewById(R.id.fragment_accordant_user_profile_rl_back);
        tvName1 = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_target_name_1);
        tvName2 = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_target_name_2);
        /*Personal Information*/
        ivAvatar = (ImageView) findViewById(R.id.fragment_accordant_user_profile_iv_avatar);
        tvName = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_name);
        tvAge = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_age);
        tvGender = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_gender);
        /*Achievements*/
        tvCountsLike = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_counts_like);
        tvCountsAppointment = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_counts_appointment);
        tvCountsStar = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_counts_star);
        /*Button Like - dislike*/
        llButtonLike = (LinearLayout) findViewById(R.id.fragment_accordant_user_profile_ll_like);
        ivLike = (ImageView) findViewById(R.id.fragment_accordant_user_profile_iv_like);
        /*More information*/
        tvAddress = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_address);
        tvCharacters = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_character);
        tvStyles = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_styles);
        tvTargetFoods = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_target_foods);
        tvPhone = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_phone);
        /*Target's Information*/
        tvTargetCharacters = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_target_characters);
        tvTargetStyles = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_target_styles);
        /*Show Blog and History*/
        viewPager = (ViewPager) findViewById(R.id.fragment_accordant_user_profile_cvp_blog_history);
        tabLayout = (TabLayout) findViewById(R.id.fragment_accordant_user_profile_tl_tabs);

    }

    private void setValueViewPager() {
        ArrayList<Fragment> listFragment = new ArrayList<>();
        if (phoneNumber != null) {
            listFragment.add(ProfileBlogFragment.newInstance(phoneNumber));
            listFragment.add(ProfileHistoryFragment.newInstance(phoneNumber));
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), listFragment);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setValueTabLayout() {
        tabLayout.getTabAt(0).setText("BLOG");
        tabLayout.getTabAt(1).setText("HISTORY");
    }

    private void getValueProfilefromDatabase() {
        dialog = new BaseProgress();
        dialog.showProgressLoading(ProfileAccordantUser.this);
        if (phoneNumber != null) {
            Call<UserName> getProFile = Connect.getRetrofit().getProfileUser(phoneNumber);
            getProFile.enqueue(new Callback<UserName>() {
                @Override
                public void onResponse(Call<UserName> call, Response<UserName> response) {
                    if (response.body() != null) {

                        setValueDefaultProfile(response.body());

                        Call<Achievement> getAchievement = Connect.getRetrofit().getAchievement(phoneNumber);
                        getAchievement.enqueue(new Callback<Achievement>() {
                            @Override
                            public void onResponse(Call<Achievement> call, Response<Achievement> response) {
                                dialog.hideProgressLoading();
                                if (response.body() != null) {
                                    Achievement achievement = response.body();
                                    tvCountsLike.setText(achievement.getLike() + "");
                                    tvCountsAppointment.setText(achievement.getAppointment() + "");
                                    tvCountsStar.setText(achievement.getRate() + "");

                                    if(achievement.getListUser()!=null && achievement.getListUser().size()>0){
                                        for(int i=0;i<achievement.getListUser().size();i++){
                                            if(achievement.getListUser().get(i).equals(myPhone)){
                                                ivLike.setColorFilter(Color.BLUE);
                                                llButtonLike.setOnClickListener(null);
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Achievement> call, Throwable t) {

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<UserName> call, Throwable t) {
                    Toast.makeText(ProfileAccordantUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setClickButtonExit() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ProfileAccordantUser.this, MainActivity.class);
//                intent.putExtra(ARG_FROM_VIEW, ARG_PROFILE_ACCORDANT_USER_ACTIVITY);
//                startActivity(intent);
                onBackPressed();
            }
        });
    }

    private void setValueDefaultProfile(UserName userName) {
        LocalDate date = new LocalDate();

        Picasso.with(ProfileAccordantUser.this)
                .load(userName.getAvatar())
                .error(R.drawable.avatar)
                .into(ivAvatar);

        tvName.setText(StringEscapeUtils.unescapeJava(userName.getFullName()));
        tvName1.setText(StringEscapeUtils.unescapeJava(userName.getFullName()));
        tvName2.setText(StringEscapeUtils.unescapeJava(userName.getFullName()));
        if (userName.getBirthday().equals(""))
            tvAge.setText("");
        else
            tvAge.setText((date.getYear() - Integer.parseInt(userName.getBirthday().split("/")[2])) + "");
        if (userName.getGender().equals("Female")) {
            tvGender.setText("F");
        } else {
            tvGender.setText("M");
        }
        tvPhone.setText(userName.getPhone());
        if (userName.getAddress().equals("")) {
            tvAddress.setText("Ho Chi Minh");
        } else {
            tvAddress.setText(StringEscapeUtils.unescapeJava(userName.getAddress()));
        }

        putDataHobbyIntoReference(userName);
        putDataPersonalIntoReference(userName);
    }


    private void putDataHobbyIntoReference(UserName userName) {
            tvTargetFoods.setText(setMultiColorText(getResources().getString(R.string.food), userName.getTargetFood()));
        tvTargetCharacters.setText(setMultiColorText(getResources().getString(R.string.target_character), userName.getTargetCharacter()));
        tvTargetStyles.setText(setMultiColorText(getResources().getString(R.string.target_style), userName.getTargetStyle()));
    }

    private void putDataPersonalIntoReference(UserName userName) {
        tvCharacters.setText(setMultiColorText(getResources().getString(R.string.my_characters), userName.getMyCharacter()));
        tvStyles.setText(setMultiColorText(getResources().getString(R.string.my_styles), userName.getMyStyle()));
    }

    private void setClickButtonLike() {
        if (phoneNumber != null) {
            llButtonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call<Status> addLike = Connect.getRetrofit().addLike(phoneNumber, myPhone, 0);
                    addLike.enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            if (response.body() != null) {
                                if (response.body().getStatus().equals("1")) {
                                    tvCountsLike.setText((Integer.parseInt(tvCountsLike.getText().toString()) + 1) + "");
                                    ivLike.setColorFilter(Color.BLUE);
                                    llButtonLike.setOnClickListener(null);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {

                        }
                    });
                }
            });
        }
    }

    private Spannable setMultiColorText(String defaultText,String text) {
        Spannable spannable = new SpannableString(defaultText+StringEscapeUtils.unescapeJava(text));

        spannable.setSpan(new ForegroundColorSpan(Color.BLACK), defaultText.length(), defaultText.length()+StringEscapeUtils.unescapeJava(text).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> listFragment;

        private ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> listFragment) {
            super(fm);
            this.listFragment = listFragment;
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }
    }

}
