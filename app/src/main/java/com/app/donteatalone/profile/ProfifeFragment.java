package com.app.donteatalone.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.donteatalone.R;

/**
 * Created by ChomChom on 4/13/2017.
 */

public class ProfifeFragment extends Fragment {
    private View viewGroup;

    public static ProfifeFragment newInstance(){
        ProfifeFragment fragment=new ProfifeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup=inflater.inflate(R.layout.fragment_profile,null);
        return viewGroup;
    }
}
