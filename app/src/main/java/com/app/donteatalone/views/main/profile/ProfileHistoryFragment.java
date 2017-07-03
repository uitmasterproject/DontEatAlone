package com.app.donteatalone.views.main.profile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.donteatalone.R;
import com.app.donteatalone.model.ProfileHistoryModel;

import java.util.ArrayList;

/**
 * Created by Le Hoang Han on 5/19/2017.
 */

public class ProfileHistoryFragment extends android.support.v4.app.Fragment {
    private View viewGroup;
    private RecyclerView rclvHistory;
    private ProfileHistoryAdapter profileHistoryAdapter;
    private ArrayList<ProfileHistoryModel> listProfileHistory;

    public static ProfileHistoryFragment newInstance() {
        return new ProfileHistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_profile_history, null);
        init();
        return viewGroup;
    }

    private void init(){
        rclvHistory = (RecyclerView) viewGroup.findViewById(R.id.fragment_profile_history_rclv_history);
        rclvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        profileHistoryAdapter = new ProfileHistoryAdapter(listProfileHistory,getContext());
//        rclvHistory.setAdapter(profileHistoryAdapter);
    }
}
