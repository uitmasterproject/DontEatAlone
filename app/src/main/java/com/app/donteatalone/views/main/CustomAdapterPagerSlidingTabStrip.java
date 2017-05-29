package com.app.donteatalone.views.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.donteatalone.R;
import com.app.donteatalone.views.main.blog.BlogFragment;
import com.app.donteatalone.views.main.profile.ProfileFragment;
import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by ChomChom on 4/13/2017.
 */

public class CustomAdapterPagerSlidingTabStrip extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{


    int size_fragment=5;
    private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab3","Tab4","Tab5" };
    private int srcIcon[]=new int[]{R.drawable.blogger,R.drawable.notification,R.drawable.avatar,R.drawable.salver,R.drawable.require};

    public CustomAdapterPagerSlidingTabStrip(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f =new Fragment();
        switch (position){
            case 0:
                f= BlogFragment.newInstance();
                break;
            case 1:
                break;
            case 2:
                f= ProfileFragment.newInstance();
                break;
            case 3:
                break;
            case 4:
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return size_fragment;
    }

    @Override
    public int getPageIconResId(int position) {
        return srcIcon[position];
    }
}
