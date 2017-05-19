package com.app.donteatalone.views.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseActivity;
import com.app.donteatalone.views.main.blog.CustomAdapterPagerSlidingTabStrip;
import com.app.donteatalone.widgets.Toolbar;
import com.astuetz.PagerSlidingTabStrip;

/**
 * -> Created by LeHoangHan on 4/22/2017.
 */

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Set tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.applyMainUi(this, "DEA", new Toolbar.MainItemClick() {
            @Override
            public void toolbarCloseClick() {
                onBackPressed();
            }

            @Override
            public void toolbar3DotClick() {

            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
        CustomAdapterPagerSlidingTabStrip adapter = new CustomAdapterPagerSlidingTabStrip(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.activity_main_tabs);
        pagerSlidingTabStrip.setViewPager(viewPager);

        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
}
