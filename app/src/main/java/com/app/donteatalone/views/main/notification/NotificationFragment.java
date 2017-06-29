package com.app.donteatalone.views.main.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.donteatalone.R;

/**
 * Created by ChomChom on 08-Jun-17.
 */

public class NotificationFragment extends Fragment {

    private View viewGroup;
    public static RecyclerView rcvInfoNotification;

    public static NotificationFragment newInstance(){

        NotificationFragment fragment=new NotificationFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup=inflater.inflate(R.layout.fragment_notification,null);
        init();
        return viewGroup;
    }

    public void init(){
        rcvInfoNotification=(RecyclerView) viewGroup.findViewById(R.id.fragment_notification_rcv_notification);
        rcvInfoNotification.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
