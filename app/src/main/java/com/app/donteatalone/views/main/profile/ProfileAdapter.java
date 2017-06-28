package com.app.donteatalone.views.main.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Le Hoang Han on 5/19/2017.
 */

public class ProfileAdapter extends FragmentStatePagerAdapter {
    public ProfileAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = new ProfileAlbumFragment();
                break;
            case 1:
                frag = new ProfileHistoryFragment();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = "";
        switch (position) {
            case 0:
                pageTitle = "Album";
                break;
            case 1:
                pageTitle = "History";
                break;
        }
        return pageTitle;
    }
}
