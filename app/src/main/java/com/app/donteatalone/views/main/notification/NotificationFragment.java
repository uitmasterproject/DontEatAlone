package com.app.donteatalone.views.main.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoNotification;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.login.LoginActivity;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 08-Jun-17.
 */

public class NotificationFragment extends Fragment {

    private View viewGroup;
    public static RecyclerView rcvInfoNotification;
    private ImageButton ibtnExit;
    public static TextView txtNotification;
    private ViewPager viewPager;
    private View view;

    public static NotificationFragment newInstance(ViewPager viewPager, View view){
        NotificationFragment fragment=new NotificationFragment();
        fragment.setViewPager(viewPager);
        fragment.setView(view);
        Log.e("2","2"+"------------------------------------------------------");
        return fragment;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup=inflater.inflate(R.layout.fragment_notification,null);
        return viewGroup;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        initNotification();
        clickButtonExit();
    }

    public void init(){
        rcvInfoNotification=(RecyclerView) viewGroup.findViewById(R.id.fragment_notification_rcv_notification);
        rcvInfoNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        ibtnExit=(ImageButton) viewGroup.findViewById(R.id.fragment_notification_ibtn_exit);
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

    private void initNotification() {
        txtNotification = (TextView) view.findViewById(R.id.custom_tab_notification_txt_count);
        LinearLayout llContainer = (LinearLayout) view.findViewById(R.id.custom_tab_notification_ll_container);
        llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNotification.setText(0 + "");
                viewPager.setCurrentItem(1);
                setNotification();
            }
        });
    }

    public void setNotification() {
        final ArrayList<InfoNotification> listInfoNotification = new ArrayList();
        final CustomNotificationAdapter adapter = new CustomNotificationAdapter(listInfoNotification, getContext());
        Call<ArrayList<InfoNotification>> getInfoNotification = Connect.getRetrofit().getNotification(new MySharePreference(getActivity()).getValue("phoneLogin"));
        getInfoNotification.enqueue(new Callback<ArrayList<InfoNotification>>() {
            @Override
            public void onResponse(Call<ArrayList<InfoNotification>> call, Response<ArrayList<InfoNotification>> response) {
                if(response.body()!=null) {
                    for (InfoNotification element : response.body()) {
                        InfoNotification info = new InfoNotification(element.getUserSend(), element.getNameSend(), element.getTimeSend(),
                                element.getDate(), element.getTime(), element.getPlace(), element.getStatus(), element.getRead(), element.getSeen());
                        listInfoNotification.add(info);
                    }
                    Collections.reverse(listInfoNotification);
                }
                rcvInfoNotification.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<InfoNotification>> call, Throwable t) {
            }
        });
    }

}
