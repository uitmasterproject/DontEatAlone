package com.app.donteatalone.views.register;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ChomChom on 3/6/2017
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public static int totalPage=7;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch(position){
            case 0:
                f=RegisterStep1Fragment.newInstance();
                break;
            case 1:
                f=RegisterStep2Fragment.newInstance();
                break;
            case 2:
                f=RegisterStep3Fragment.newInstance();
                break;
            case 3:
                f=RegisterStep4Fragment.newInstance();
                break;
            case 4:
                f=RegisterStep5Fragment.newInstance();
                break;
            case 5:
                f=RegisterStep6Fragment.newInstance();
                break;
            case 6:
                f=RegisterStep7Fragment.newInstance();
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return totalPage;
    }

}
