package com.app.donteatalone.views.main.profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.views.main.blog.DetailBlogActivity;

import java.util.ArrayList;

/**
 * Created by ChomChom on 05-Jul-17
 */

public class ProfileBlogAdapter extends RecyclerView.Adapter<ProfileBlogAdapter.ProfileBlogVH> {
    private Context context;
    private ArrayList<InfoBlog> listBlog;
    private String phoneBlog;

    public ProfileBlogAdapter(Context context, ArrayList<InfoBlog> listBlog, String phoneBlog) {
        this.context = context;
        this.listBlog = listBlog;
        this.phoneBlog = phoneBlog;
    }

    @Override
    public ProfileBlogVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_fragment_profile_blog, null);
        return new ProfileBlogVH(view);
    }

    @Override
    public void onBindViewHolder(ProfileBlogVH holder, int position) {
        holder.txtTitle.setText(listBlog.get(position).getTitle());
        holder.txtTime.setText(listBlog.get(position).getDate());
        ProfileBlogInnerAdapter adapter = new ProfileBlogInnerAdapter(listBlog.get(position).getImage(), context);
        holder.gvImage.setAdapter(adapter);
        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailBlogActivity.class);
                intent.putExtra("infoBlog", listBlog.get(position));
                intent.putExtra("own", phoneBlog);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listBlog.size();
    }


    public class ProfileBlogVH extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtTime;
        private GridView gvImage;
        private LinearLayout llContainer;

        public ProfileBlogVH(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_blog_txt_title);
            txtTime = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_blog_txt_time);
            gvImage = (GridView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_blog_gv_image);
            llContainer = (LinearLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_blog_ll_container);
        }
    }
}