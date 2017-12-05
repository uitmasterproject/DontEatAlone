package com.app.donteatalone.views.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.views.main.blog.BlogFragment;
import com.app.donteatalone.views.main.notification.NotificationFragment;
import com.app.donteatalone.views.main.profile.MyProfileFragment;
import com.app.donteatalone.views.main.require.RequireFragment;
import com.app.donteatalone.views.main.restaurant.RestaurantFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * -> Created by LeHoangHan on 4/22/2017
 */

public class MainActivity extends AppCompatActivity {

    public static final String BROADCAST_NAME = "DONTEATALONE.BROADCAST_NAME";
    public static final String SEND_BROADCAST_DATA = "DONTEATALONE.BROADCAST_DATA";
    public static final String SEND_BROADCAST_TITLE = "DONTEATALONE.BROADCAST_TITLE";

    public static final String ARG_FROM_VIEW = "ARG_FROM_VIEW";
    public static final String ARG_DETAIL_BLOG_ACTIVITY = "ARG_DETAIL_BLOG_ACTIVITY";
    public static final String ARG_PROFILE_ACCORDANT_USER_ACTIVITY = "ARG_PROFILE_ACCORDANT_USER_ACTIVITY";
    public static final String ARG_STATUS_ACTIVITY_DATA = "ARG_STATUS_ACTIVITY_DATA";
    public static final String ARG_EDIT_PROFILE_ACTIVITY = "ARG_EDIT_PROFILE_ACTIVITY";

    public static final String URL_STORAGE_FIRE_BASE = "gs://dont-eat-alone-storage.appspot.com";
    public static final String REGISTER_PATH_STORAGE_FIRE_BASE = "register/";
    public static final String BLOG_PATH_STORAGE_FIRE_BASE = "blog/";

    public static final String ARG_LIST_BLOG = "ARG_LIST_BLOG";

    public ViewPager viewPager;
    public View view;
    private TabLayout tabLayout;
    private FragmentAdapter mFragmentAdapter;
    private int srcIcon[] = new int[]{R.drawable.ic_blog, R.drawable.ic_notification, R.drawable.ic_profile, R.drawable.ic_restaurant, R.drawable.ic_require};
    private int srcIconSelected[] = new int []{R.drawable.ic_blog_selected,  R.drawable.ic_notification_selected, R.drawable.ic_profile_selected, R.drawable.ic_restaurant_selected, R.drawable.ic_require_selected};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.activity_blog_viewpager);
        view = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab_notification, null);
        setViewPager();

        viewPager.setOffscreenPageLimit(5);

        tabLayout = (TabLayout) findViewById(R.id.activity_blog_tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        if (getIntent().getStringExtra(ARG_FROM_VIEW) != null) {
            if (getIntent().getStringExtra(ARG_FROM_VIEW).equals(ARG_PROFILE_ACCORDANT_USER_ACTIVITY))
                viewPager.setCurrentItem(1);
            else if (getIntent().getStringExtra(ARG_FROM_VIEW).equals(ARG_EDIT_PROFILE_ACTIVITY))
                viewPager.setCurrentItem(2);
            else if (getIntent().getStringExtra(ARG_FROM_VIEW).equals(ARG_DETAIL_BLOG_ACTIVITY))
                viewPager.setCurrentItem(0);
        }

    }

    private void setViewPager() {
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mFragmentAdapter.mFragmentList.add(BlogFragment.newInstance());
        mFragmentAdapter.mFragmentList.add(NotificationFragment.newInstance(viewPager, view));
        mFragmentAdapter.mFragmentList.add(MyProfileFragment.newInstance());
        mFragmentAdapter.mFragmentList.add(RestaurantFragment.newInstance());
        mFragmentAdapter.mFragmentList.add(RequireFragment.newInstance(viewPager));

        viewPager.setAdapter(mFragmentAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                AppUtils.hideSoftKeyboard(MainActivity.this);
            }

            @Override
            public void onPageSelected(int position) {
                AppUtils.hideSoftKeyboard(MainActivity.this);
                if(position!=1) {
                    tabLayout.getTabAt(position).setIcon(srcIconSelected[position]);
                }else {
                    View view=tabLayout.getTabAt(position).getCustomView();
                    ImageView imageView=(ImageView)view.findViewById(R.id.img_icon);
                    imageView.setImageResource(srcIconSelected[position]);
                }
                    for(int i=0;i< mFragmentAdapter.mFragmentList.size();i++){
                    if(i!=position){
                        if(i!=1) {
                            tabLayout.getTabAt(i).setIcon(srcIcon[i]);
                        }else {
                            View view=tabLayout.getTabAt(i).getCustomView();
                            ImageView imageView=(ImageView)view.findViewById(R.id.img_icon);
                            imageView.setImageResource(srcIcon[i]);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(srcIconSelected[0]);
        tabLayout.getTabAt(1).setCustomView(view);
        tabLayout.getTabAt(2).setIcon(srcIcon[2]);
        tabLayout.getTabAt(3).setIcon(srcIcon[3]);
        tabLayout.getTabAt(4).setIcon(srcIcon[4]);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if(i!=1) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) tab.setCustomView(R.layout.custom_tab);
            }
        }
    }

    public class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

}
