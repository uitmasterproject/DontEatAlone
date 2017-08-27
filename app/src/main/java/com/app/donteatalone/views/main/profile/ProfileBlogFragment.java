package com.app.donteatalone.views.main.profile;

import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Le Hoang Han on 5/19/2017
 */

public class ProfileBlogFragment extends Fragment {
    private View viewGroup;
    private RecyclerView rvListBlog;
    private ProfileBlogAdapter adapter;
    private ArrayList<InfoBlog> listBlog;
    private String phoneBlog;
    private String limit;

    public ProfileBlogFragment(String phoneBlog) {
        this.phoneBlog = phoneBlog;
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
        Connect connect=new Connect();
        String phone;
        if(phoneBlog.equals("ownViewProfile")){
            phone=storeReference("phoneLogin");
        }
        else {
            phone=phoneBlog;
        }
        Call<ArrayList<InfoBlog>> getPublicBlog=connect.getRetrofit().getPublicBlog(storeReference("phoneLogin"),limit);
        getPublicBlog.enqueue(new Callback<ArrayList<InfoBlog>>() {
            @Override
            public void onResponse(Call<ArrayList<InfoBlog>> call, Response<ArrayList<InfoBlog>> response) {
                listBlog.clear();
                listBlog.addAll(response.body());
                Collections.reverse(listBlog);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<InfoBlog>> call, Throwable t) {

            }
        });
    }

    private String storeReference(String str) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("account", MODE_PRIVATE);
        String avatar = sharedPreferences.getString(str, "");
        return avatar;
    }
}
