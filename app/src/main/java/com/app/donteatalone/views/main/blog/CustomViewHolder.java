package com.app.donteatalone.views.main.blog;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import com.app.donteatalone.R;

/**
 * Created by ChomChom on 4/23/2017.
 */

public class CustomViewHolder extends ViewHolder {

    protected RecyclerView rcListBlog;

    public CustomViewHolder(View itemView) {
        super(itemView);
        rcListBlog=(RecyclerView) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_rc_list_blog);
        rcListBlog.setLayoutManager(new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL, false));
    }
}
