package com.app.donteatalone.views.main.require;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.app.donteatalone.R;
import com.app.donteatalone.views.login.LoginActivity;
import com.app.donteatalone.views.main.require.main_require.OffRequireFragment;
import com.app.donteatalone.views.main.require.main_require.OnRequireFragment;

/**
 * Created by ChomChom on 5/7/2017
 */

public class RequireFragment extends Fragment {

    private View viewGroup;
    private SwitchCompat scControl;
    private ImageButton ibtnExit;
    private ViewPager viewPager;

    public static RequireFragment newInstance(ViewPager viewPager) {

        RequireFragment fragment = new RequireFragment();
        fragment.setViewPager(viewPager);
        return fragment;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_require, container, false);
        init();
        clickSwitchCompatControl();
        clickButtonExit();
        return viewGroup;
    }

    private void init() {
        scControl=(SwitchCompat) viewGroup.findViewById(R.id.fragment_require_sc_control);
        ibtnExit=(ImageButton) viewGroup.findViewById(R.id.fragment_require_ibtn_exit);
    }

    private void clickSwitchCompatControl(){
        scControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!scControl.isChecked()){
                    OffRequireFragment fragment= OffRequireFragment.newInstance();
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_require_fl_container, fragment).commit();
                }
                else {
                    OnRequireFragment fragmentOn= OnRequireFragment.newInstance(viewPager);
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_require_fl_container, fragmentOn).commit();
                }
            }
        });
    }

    private void clickButtonExit(){
        ibtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!scControl.isChecked()){
            OffRequireFragment fragment= new OffRequireFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_require_fl_container, fragment).commit();
        }
        else {
            OnRequireFragment fragmentOn= new OnRequireFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_require_fl_container, fragmentOn).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        }
}
