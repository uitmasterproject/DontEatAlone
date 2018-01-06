package com.app.donteatalone.views.main.blog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.base.OnRecyclerItemClickListener;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.MainActivity;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 4/13/2017
 */

public class BlogFragment extends Fragment {

    private View view;
    private Button btnStatus;
    private ImageView imgAvatar;
    private MySharePreference mySharePreference;

    private RecyclerView rcvListBlog;
    private ArrayList<InfoBlog> listInfoBlog;
    private BlogItemAdapter adapter;

    private LinearLayout llEntry;

    private BaseProgress dialog;

    private BroadcastReceiver broadcastReceiver;


    public static BlogFragment newInstance() {
        return new BlogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blog, container, false);
        init();
        getListBlog();
        clickButtonStatus();
        listenBroadcast();
        return view;
    }



    private void init() {
        btnStatus = (Button) view.findViewById(R.id.fragment_blog_edt_status);
        imgAvatar = (ImageView) view.findViewById(R.id.fragment_blog_avatar);
        mySharePreference = new MySharePreference(getActivity());

        dialog = new BaseProgress();

        rcvListBlog = (RecyclerView)view.findViewById(R.id.fragment_blog_rcv_my_blog);
        llEntry = (LinearLayout) view.findViewById(R.id.fragment_blog_ll_entry);
        rcvListBlog.setLayoutManager(new LinearLayoutManager(getActivity()));

        listInfoBlog = new ArrayList<>();

        adapter = new BlogItemAdapter(listInfoBlog, getActivity());
        rcvListBlog.setAdapter(adapter);

        adapter.setClickDelete(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int resource) {
                rcvListBlog.setVisibility(View.GONE);
                llEntry.setVisibility(View.VISIBLE);
            }
        });

    }

    private void listenBroadcast(){
        if(broadcastReceiver==null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getBooleanExtra(MainActivity.SEND_BROADCAST_MODIFY_BLOG_DATA, false)) {
                        getListBlog();
                    }
                }
            };
        }

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(MainActivity.BROADCAST_MODIFY_BLOG_NAME));

    }

    private void getListBlog() {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            Call<ArrayList<InfoBlog>> userLogin = Connect.getRetrofit().getListInfoBlog(mySharePreference.getPhoneLogin());
            dialog.showProgressLoading(getActivity());
            userLogin.enqueue(new Callback<ArrayList<InfoBlog>>() {
                @Override
                public void onResponse(Call<ArrayList<InfoBlog>> call, Response<ArrayList<InfoBlog>> response) {
                    dialog.hideProgressLoading();
                    if (response.body() != null && response.body().size() > 0) {
                        listInfoBlog.clear();

                        llEntry.setVisibility(View.GONE);

                        listInfoBlog.addAll(response.body());

                        Collections.reverse(listInfoBlog);

                        if (listInfoBlog.get(0).getFeeling() != 0) {
                            try {
                                imgAvatar.setImageResource(listInfoBlog.get(0).getFeeling());
                            }catch (Exception exception){
                                imgAvatar.setImageResource(R.drawable.ic_happy);
                            }
                        } else {
                            imgAvatar.setImageResource(R.drawable.ic_happy);
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        llEntry.setVisibility(View.VISIBLE);
                        imgAvatar.setImageResource(R.drawable.ic_happy);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<InfoBlog>> call, Throwable t) {
                    dialog.hideProgressLoading();
                }
            });
        } else {
            Toast.makeText(getActivity(), getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void clickButtonStatus() {
        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StatusActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        if(broadcastReceiver!=null){
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }
}
