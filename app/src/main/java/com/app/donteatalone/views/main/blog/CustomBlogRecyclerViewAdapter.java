package com.app.donteatalone.views.main.blog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.donteatalone.R;
import com.app.donteatalone.model.InfoBlog;

import java.util.ArrayList;

/**
 * Created by ChomChom on 4/23/2017
 */

public class CustomBlogRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private ArrayList<ArrayList<InfoBlog>> myInfoBlogs;

    public CustomBlogRecyclerViewAdapter(Context context, ArrayList<ArrayList<InfoBlog>> myInfoBlogs) {
        this.context = context;
        this.myInfoBlogs = myInfoBlogs;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_my_status_blog, null);
        return  new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        CustomInnerRecyclerViewAdapter adapter=new CustomInnerRecyclerViewAdapter(myInfoBlogs.get(position),context);
        holder.rcListBlog.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return myInfoBlogs.size();
    }


}
