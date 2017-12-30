package com.app.donteatalone.views.main.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoBlog;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Le Hoang Han on 5/19/2017
 */

public class ProfileBlogFragment extends Fragment {
    private static String ARG_PROFILE_BLOG_PHONE = "ARG_PROFILE_BLOG_PHONE";

    private View viewGroup;
    private RecyclerView rvListBlog;
    private ProfileBlogAdapter adapter;
    private ArrayList<InfoBlog> listBlog;
    private String phoneBlog;
    private String limit;

    public ProfileBlogFragment() {
    }

    public static ProfileBlogFragment newInstance(String phoneBlog) {
        ProfileBlogFragment fm = new ProfileBlogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PROFILE_BLOG_PHONE, phoneBlog);
        fm.setArguments(bundle);
        return fm;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().getString(ARG_PROFILE_BLOG_PHONE) != null) {
            phoneBlog = getArguments().getString(ARG_PROFILE_BLOG_PHONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_profile_blog, null);
        init();
        setValueRecyclerView();
        return viewGroup;
    }

    private void init(){
        limit="all";
        if(phoneBlog!=null){
            limit="public";
        }
        rvListBlog = (RecyclerView) viewGroup.findViewById(R.id.fragment_profile_blog_rv_blog);
        rvListBlog.setLayoutManager(new LinearLayoutManager(getContext()));
        listBlog=new ArrayList<>();
        adapter=new ProfileBlogAdapter(viewGroup.getContext(),listBlog,phoneBlog);
        rvListBlog.setAdapter(adapter);
    }

    private void setValueRecyclerView(){
        Call<ArrayList<InfoBlog>> getPublicBlog=Connect.getRetrofit().getPublicBlog(phoneBlog,limit);
        getPublicBlog.enqueue(new Callback<ArrayList<InfoBlog>>() {
            @Override
            public void onResponse(Call<ArrayList<InfoBlog>> call, Response<ArrayList<InfoBlog>> response) {
                if(response.body()!=null) {
                    listBlog.clear();
                    listBlog.addAll(response.body());
                    Collections.reverse(listBlog);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InfoBlog>> call, Throwable t) {

            }
        });
    }
}
