package com.app.donteatalone.views.main.profile.event_history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.ProfileHistoryModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Le Hoang Han on 5/19/2017
 */

public class ProfileHistoryFragment extends Fragment {
    private static String ARG_PROFILE_HISTORY_PHONE = "ARG_PROFILE_HISTORY_PHONE";
    private static String ARG_PROFILE_HISTORY_NAME = "ARG_PROFILE_HISTORY_NAME";

    private View viewGroup;
    private RecyclerView rclvHistory;
    private ProfileHistoryAdapter profileHistoryAdapter;
    private ArrayList<ProfileHistoryModel> listProfileHistory;
    private SwipeRefreshLayout srlRefresh;
    private String phoneHistory;
    private String name;

    private LinearLayout llEmptyHistory;

    public ProfileHistoryFragment() {
    }

    public static ProfileHistoryFragment newInstance(String phoneHistory, String name) {
        ProfileHistoryFragment fm = new ProfileHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PROFILE_HISTORY_PHONE, phoneHistory);
        bundle.putString(ARG_PROFILE_HISTORY_NAME, name);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().getString(ARG_PROFILE_HISTORY_PHONE) != null && getArguments().getString(ARG_PROFILE_HISTORY_NAME) != null) {
            phoneHistory = getArguments().getString(ARG_PROFILE_HISTORY_PHONE);
            name = getArguments().getString(ARG_PROFILE_HISTORY_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_profile_history, container, false);
        init();
        getEventHistory();
        return viewGroup;
    }


    private void init() {
        llEmptyHistory = (LinearLayout) viewGroup.findViewById(R.id.fragment_history_ll_entry);

        rclvHistory = (RecyclerView) viewGroup.findViewById(R.id.fragment_profile_history_rclv_history);
        srlRefresh = (SwipeRefreshLayout) viewGroup.findViewById(R.id.fragment_profile_history_srl_refresh);

        rclvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        listProfileHistory = new ArrayList<>();
        profileHistoryAdapter = new ProfileHistoryAdapter(listProfileHistory, getContext(), phoneHistory, name);

        rclvHistory.setAdapter(profileHistoryAdapter);

        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEventHistory();
            }
        });
    }

    private void getEventHistory() {
        Call<ArrayList<ProfileHistoryModel>> getEventHistory = Connect.getRetrofit().getEventHistory(phoneHistory);
        getEventHistory.enqueue(new Callback<ArrayList<ProfileHistoryModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProfileHistoryModel>> call, Response<ArrayList<ProfileHistoryModel>> response) {
                srlRefresh.setRefreshing(false);
                if (response.body() != null && response.body().size() > 0) {
                    llEmptyHistory.setVisibility(View.GONE);

                    rclvHistory.setVisibility(View.VISIBLE);

                    listProfileHistory.clear();
                    listProfileHistory.addAll(response.body());

                    profileHistoryAdapter.notifyDataSetChanged();
                } else {
                    rclvHistory.setVisibility(View.GONE);

                    llEmptyHistory.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProfileHistoryModel>> call, Throwable t) {
                srlRefresh.setRefreshing(false);
            }
        });
    }
}
