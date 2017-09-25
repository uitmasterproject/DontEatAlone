package com.app.donteatalone.views.main.require.main_require.on_require;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.UserName;
import com.app.donteatalone.views.main.MainActivity;
import com.app.donteatalone.views.main.profile.ProfileBlogFragment;
import com.app.donteatalone.views.main.profile.event_history.ProfileHistoryFragment;

import org.joda.time.LocalDate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 01-Aug-17
 */

public class ProfileAccordantUser extends AppCompatActivity {

    private ImageView ivAvatar, ivLike;
    private TextView tvName1, tvName2, tvName, tvAge, tvGender, tvPhone, tvAddress;
    private TextView tvTargetCharacters, tvTargetStyles, tvCharacters, tvStyles, tvFoods;
    private RelativeLayout rlBack;
    private TextView tvCountsLike, tvCountsAppointment, tvCountsStar;
    private LinearLayout llButtonLike;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_accordant_user_profile);
        init();
        getValueProfilefromDatabase();
        setClickButtonExit();
        setValueViewPager();
        setValueTabLayout();
    }

    private void init() {
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
        tvFoods = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_foods);
        tvPhone = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_phone);
        /*Target's Information*/
        tvTargetCharacters = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_target_characters);
        tvTargetStyles = (TextView) findViewById(R.id.fragment_accordant_user_profile_tv_target_styles);
        /*Show Blog and History*/
        tabLayout = (TabLayout) findViewById(R.id.fragment_accordant_user_profile_tl_tabs);
        viewPager = (ViewPager) findViewById(R.id.fragment_accordant_user_profile_cvp_blog_history);

    }

    private void setValueViewPager() {
        ArrayList<Fragment> listFragment = new ArrayList<>();
        if (getIntent().getStringExtra("PhoneAccordantUser") != null) {
            listFragment.add(new ProfileBlogFragment(getIntent().getStringExtra("PhoneAccordantUser")));
            listFragment.add(new ProfileHistoryFragment(getIntent().getStringExtra("PhoneAccordantUser")));
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), listFragment);

        viewPager.setAdapter(adapter);
    }

    private void setValueTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("BLOG");
        tabLayout.getTabAt(1).setText("HISTORY");
    }

    private void getValueProfilefromDatabase() {
        Connect connect = new Connect();
        if (getIntent().getStringExtra("PhoneAccordantUser") != null) {
            Call<UserName> getProFile = connect.getRetrofit().getProfileUser(getIntent().getStringExtra("PhoneAccordantUser"));
            getProFile.enqueue(new Callback<UserName>() {
                @Override
                public void onResponse(Call<UserName> call, Response<UserName> response) {
                    if (response.body() != null) {
                        UserName userName = new UserName(response.body().getPhone(), response.body().getFullname(),
                                response.body().getPassword(), response.body().getAvatar(), response.body().getBirthday(),
                                response.body().getGender(), response.body().getAddress(), response.body().getHobby(),
                                response.body().getCharacter());
                        setValueDefaultProfile(userName);
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
                Intent intent = new Intent(ProfileAccordantUser.this, MainActivity.class);
                intent.putExtra("viewProfile", "notification");
                startActivity(intent);
            }
        });
    }

    private void setValueDefaultProfile(UserName userName) {
        LocalDate date = new LocalDate();
        ivAvatar.setImageBitmap(decodeBitmap(userName.getAvatar()));
        tvName.setText(userName.getFullname());
        if (userName.getBirthday().equals("") == true)
            tvAge.setText("");
        else
            tvAge.setText((date.getYear() - Integer.parseInt(userName.getBirthday().split("/")[2])) + "");
        if (userName.getGender().equals("Female") == true) {
            tvGender.setText("F");
        } else {
            tvGender.setText("M");
        }
        tvPhone.setText(userName.getPhone());
        if (userName.getAddress().equals("") == true) {
            tvAddress.setText("Ho Chi Minh");
        } else {
            tvAddress.setText(userName.getAddress());
        }

        putDataHobbyintoReference(userName.getHobby());

        tvCharacters.setText(userName.getCharacter());
    }

    private void putDataHobbyintoReference(String strHobby) {
        String str = "";
        String[] temp = strHobby.trim().split(",");
        String[] temhobby = getResources().getStringArray(R.array.food);
        for (int j = 0; j < temhobby.length; j++) {
            for (int i = 0; i < temp.length; i++) {
                if (temhobby[j].equals(temp[i]) == true) {
                    str += temp[i] + ",";
                }
            }
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        tvFoods.setText(str);

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
            str = str.substring(0, str.length() - 1);
        }
        tvTargetCharacters.setText(str);

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
            str = str.substring(0, str.length() - 1);
        }
        tvTargetStyles.setText(str);
    }

    private Bitmap decodeBitmap(String str) {
        Bitmap bitmap = null;
        if (str.equals("") != true) {
            try {
                byte[] encodeByte = Base64.decode(str, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return bitmap;
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> listFragment;

        public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> listFragment) {
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
