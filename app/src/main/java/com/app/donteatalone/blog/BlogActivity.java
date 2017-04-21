package com.app.donteatalone.blog;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.app.donteatalone.R;
import com.astuetz.PagerSlidingTabStrip;

public class BlogActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        ViewPager viewPager=(ViewPager)findViewById(R.id.activity_blog_viewpager);
        CustomAdapterPagerSlidingTabStrip adapter=new CustomAdapterPagerSlidingTabStrip(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        PagerSlidingTabStrip pagerSlidingTabStrip=(PagerSlidingTabStrip)findViewById(R.id.activity_blog_tabs);
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
