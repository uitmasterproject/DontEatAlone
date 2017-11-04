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

import com.app.donteatalone.R;
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

    public static final String BROADCASTNAME = "DONTEATALONE.BROADCASTNAME";
    public static final String SENDBROADCASTDATA = "DONTEATALONE.BROADCASTDATA";
    public static final String SENDBROADCASTTITLE = "DONTEATALONE.BROADCASTTITLE";

    public static final String ARG_FROM_VIEW="ARG_FROM_VIEW";
    public static final String ARG_DETAIL_BLOG_ACTIVITY="ARG_DETAIL_BLOG_ACTIVITY";
    public static final String ARG_PROFILE_ACCORDANT_USER_ACTIVITY ="ARG_PROFILE_ACCORDANT_USER_ACTIVITY";

    public ViewPager viewPager;
    public View view;
    private TabLayout tabLayout;
    private FragmentAdapter mFragmentAdapter;
    private int srcIcon[] = new int[]{R.drawable.ic_blog, R.drawable.ic_notification, R.drawable.ic_profile, R.drawable.ic_restaurant, R.drawable.ic_require};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.activity_blog_viewpager);
        view = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab_notification, null);
        setViewPager();

        tabLayout = (TabLayout) findViewById(R.id.activity_blog_tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        if (getIntent().getStringExtra(ARG_FROM_VIEW) != null) {
            if (getIntent().getStringExtra(ARG_FROM_VIEW).equals(ARG_PROFILE_ACCORDANT_USER_ACTIVITY))
                viewPager.setCurrentItem(1);
            else if (getIntent().getStringExtra(ARG_FROM_VIEW).equals("viewProfile"))
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

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(srcIcon[0]);
        tabLayout.getTabAt(1).setCustomView(view);
        tabLayout.getTabAt(2).setIcon(srcIcon[2]);
        tabLayout.getTabAt(3).setIcon(srcIcon[3]);
        tabLayout.getTabAt(4).setIcon(srcIcon[4]);
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
