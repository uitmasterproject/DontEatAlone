package com.app.donteatalone.views.main.restaurant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.donteatalone.R;

/**
 * Created by ChomChom on 16-Jun-17.
 */

public class RestaurantFragment extends Fragment {
    private View viewGroup;
    private SwitchCompat scControl;

    public static RestaurantFragment newInstance() {

        RestaurantFragment fragment = new RestaurantFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_restaurant, null);
        return viewGroup;
    }
}
