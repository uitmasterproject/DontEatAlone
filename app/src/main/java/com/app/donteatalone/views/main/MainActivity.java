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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.views.main.blog.BlogFragment;
import com.app.donteatalone.views.main.notification.NotificationFragment;
import com.app.donteatalone.views.main.profile.ProfileFragment;
import com.app.donteatalone.views.main.require.RequireFragment;
import com.app.donteatalone.views.main.restaurant.RestaurantFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * -> Created by LeHoangHan on 4/22/2017.
 */

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private FragmentAdapter mFragmentAdapter;
    public  ViewPager viewPager;
    public TextView txtNotification;
    public View view;
    public LinearLayout llContainer;

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

        if (getIntent().getStringExtra("viewProfile") != null) {
            if (getIntent().getStringExtra("viewProfile").equals("notification"))
                viewPager.setCurrentItem(1);
            else if (getIntent().getStringExtra("viewProfile").equals("viewProfile"))
                viewPager.setCurrentItem(2);
            else if(getIntent().getStringExtra("viewProfile").equals("blogFragmment"))
                viewPager.setCurrentItem(0);
        }
    }

    private void setViewPager() {
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mFragmentAdapter.mFragmentList.add(BlogFragment.newInstance(viewPager));
        mFragmentAdapter.mFragmentList.add(NotificationFragment.newInstance(viewPager,view));
        mFragmentAdapter.mFragmentList.add(ProfileFragment.newInstance());
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
