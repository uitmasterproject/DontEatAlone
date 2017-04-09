package com.app.donteatalone.views.register;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ChomChom on 3/6/2017.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context _context;
    public static int totalPage=6;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context=context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch(position){
            case 0:
                f=RegisterStep1Fragment.newInstance(_context);
                break;
            case 1:
                f=RegisterStep2Fragment.newInstance(_context);
                break;
            case 2:
                f=RegisterStep3Fragment.newInstance(_context);
                break;
            case 3:
                f=RegisterStep4Fragment.newInstance(_context);
                break;
            case 4:
                f=RegisterStep5Fragment.newInstance(_context);
                break;
            case 5:
                f=RegisterStep6Fragment.newInstance(_context);
                break;
            case 6:
                f=RegisterStep7Fragment.newInstance(_context);
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return totalPage;
    }

}
