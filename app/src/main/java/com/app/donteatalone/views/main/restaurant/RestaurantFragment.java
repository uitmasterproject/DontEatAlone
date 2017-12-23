package com.app.donteatalone.views.main.restaurant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.donteatalone.R;
import com.app.donteatalone.views.main.restaurant.AllowReservation.AllowReservationFragment;
import com.app.donteatalone.views.main.restaurant.NoReservation.NoReservationFragment;

import java.util.ArrayList;

/**
 * Created by ChomChom on 16-Jun-17
 */

public class RestaurantFragment extends Fragment {
    private View view;
    private ViewPager viewPager;
    private TabLayout tabs;
    private TabsPagerAdapter adapter;

    private ArrayList<Fragment> listFragment;

    public static RestaurantFragment newInstance() {
        return new RestaurantFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        init();
        return view;
    }

    private void init() {
        viewPager = (ViewPager) view.findViewById(R.id.fragment_restaurant_viewpager);
        viewPager.setOffscreenPageLimit(2);

        listFragment = new ArrayList<>();
        listFragment.add(new NoReservationFragment());
        listFragment.add(new AllowReservationFragment());

        adapter = new TabsPagerAdapter(getChildFragmentManager(), listFragment);

        viewPager.setAdapter(adapter);

        tabs = (TabLayout) view.findViewById(R.id.fragment_restaurant_tabs);

        tabs.setupWithViewPager(viewPager);

        tabs.getTabAt(0).setText("no reservation");
        tabs.getTabAt(1).setText("allow reservation");
    }


    public class TabsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> listFragment;

        public TabsPagerAdapter(FragmentManager fm, ArrayList<Fragment> listFragment) {
            super(fm);
            this.listFragment = listFragment;
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }
    }
}
