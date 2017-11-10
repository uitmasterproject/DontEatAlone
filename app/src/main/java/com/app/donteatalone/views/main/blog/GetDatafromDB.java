package com.app.donteatalone.views.main.blog;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoBlog;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 3/16/2017
 */

public class GetDatafromDB extends AsyncTask<String, ArrayList<InfoBlog>, ArrayList<InfoBlog>> {

    private ArrayList<InfoBlog> infoBlog;
    private Context context;
    private View view;
    private String phone;
    private BaseProgress dialog;
    private ImageView imgAvatar;

    public GetDatafromDB(Context context, View view, ImageView imgAvatar) {
        super();
        infoBlog = new ArrayList<>();
        this.context = context;
        this.view = view;
        dialog = new BaseProgress();
        dialog.showProgressLoading(context);
        this.imgAvatar = imgAvatar;
    }

    @Override
    protected ArrayList<InfoBlog> doInBackground(String... params) {
        phone = params[0];
        Call<ArrayList<InfoBlog>> userLogin = Connect.getRetrofit().getListInfoBlog(phone);
        userLogin.enqueue(new Callback<ArrayList<InfoBlog>>() {
            @Override
            public void onResponse(Call<ArrayList<InfoBlog>> call, Response<ArrayList<InfoBlog>> response) {
                infoBlog = response.body();
                onProgressUpdate(infoBlog);
            }

            @Override
            public void onFailure(Call<ArrayList<InfoBlog>> call, Throwable t) {
                dialog.hideProgressLoading();
            }
        });
        return infoBlog;
    }

    @Override
    protected void onProgressUpdate(ArrayList<InfoBlog>... infoBlog) {
        super.onProgressUpdate(infoBlog);
        dialog.hideProgressLoading();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_blog_rcv_my_blog);
        LinearLayout llEntry = (LinearLayout) view.findViewById(R.id.fragment_blog_ll_entry);
        if (infoBlog[0] != null) {
            llEntry.setVisibility(View.GONE);
            Collections.reverse(infoBlog[0]);
            if(infoBlog[0].get(0).getFeeling()!=0) {
                imgAvatar.setImageResource(infoBlog[0].get(0).getFeeling());
            }else {
                imgAvatar.setImageResource(R.drawable.ic_happy);
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            BlogItemAdapter adapter = new BlogItemAdapter(infoBlog[0], context);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onPostExecute(ArrayList<InfoBlog> infoBlog) {
        super.onPostExecute(infoBlog);

    }
}
