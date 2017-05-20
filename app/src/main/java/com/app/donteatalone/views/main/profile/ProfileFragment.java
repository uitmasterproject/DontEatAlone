package com.app.donteatalone.views.main.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.donteatalone.R;
import com.app.donteatalone.views.register.CustomViewPager;
import com.app.donteatalone.views.register.ViewPagerAdapter;

/**
 * Created by ChomChom on 4/13/2017.
 */

public class ProfileFragment extends Fragment {
    private View viewGroup;
    private TabLayout tabLayout;
    private CustomViewPager customViewPager;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_profile, null);

        tabLayout = (TabLayout) viewGroup.findViewById(R.id.fragment_profile_tl_tabs);
        customViewPager = (CustomViewPager) viewGroup.findViewById(R.id.fragment_profile_vp_album_history);

        FragmentManager manager = getFragmentManager();
        ProfileAdapter adapter = new ProfileAdapter(manager);
        customViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(customViewPager);
        customViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);

        return viewGroup;
    }
}
