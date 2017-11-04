package com.app.donteatalone.views.main.blog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.donteatalone.views.main.blog.DetailBlogActivity.ARG_BLOG_FRAGMENT;
import static com.app.donteatalone.views.main.blog.DetailBlogActivity.ARG_ITEM_BLOG;
import static com.app.donteatalone.views.main.blog.DetailBlogActivity.ARG_OWN_BLOG;

/**
 * Created by ChomChom on 19-Aug-17
 */

public class BlogItemAdapter extends RecyclerView.Adapter<BlogItemAdapter.InnerVH> {
    private ArrayList<InfoBlog> listInnerBlog;
    private Context context;
    private BaseProgress baseProgress;

    public BlogItemAdapter(ArrayList<InfoBlog> listInnerBlog, Context context) {
        this.listInnerBlog = listInnerBlog;
        this.context = context;
        baseProgress=new BaseProgress();
    }

    @Override
    public InnerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_inner_recycler_my_status_blog, null, false);
        return new InnerVH(view);
    }

    @Override
    public void onBindViewHolder(InnerVH holder, int position) {
        holder.txtTitle.setText(listInnerBlog.get(position).getTitle());
        holder.imgIcon.setImageResource(listInnerBlog.get(position).getFeeling());
        if(listInnerBlog.get(position).getImage().size()>0) {
            holder.imgImage.setImageBitmap(AppUtils.decodeBitmap(listInnerBlog.get(position).getImage().get(0)));
        }else {
            holder.imgImage.setImageBitmap(null);
        }

        holder.txtDate.setText(listInnerBlog.get(position).getDate());

        holder.rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rlContent.setRotation(360f);
                holder.rlContent.setVisibility(View.GONE);
                holder.rlControl.setVisibility(View.VISIBLE);
            }
        });

        holder.rlControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rlControl.setRotation(360f);
                holder.rlControl.setVisibility(View.GONE);
                holder.rlContent.setVisibility(View.VISIBLE);
            }
        });

        holder.btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailBlogActivity.class);
                intent.putExtra(ARG_ITEM_BLOG, listInnerBlog.get(position));
                intent.putExtra(ARG_OWN_BLOG, ARG_BLOG_FRAGMENT);
                context.startActivity(intent);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StatusActivity.class);
                intent.putExtra(ARG_ITEM_BLOG, listInnerBlog.get(position));
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseProgress.showProgressLoading(context);
                Call<Status> deleteStatus = Connect.getRetrofit().deleteStatusBlog(new MySharePreference((Activity)context).getValue("phoneLogin"), holder.txtDate.getText().toString());
                deleteStatus.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        baseProgress.hideProgressLoading();
                        if(response.body()!=null) {
                            if (response.body().getStatus().equals("0")) {
                                listInnerBlog.remove(position);
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        baseProgress.hideProgressLoading();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listInnerBlog.size();
    }


    public class InnerVH extends RecyclerView.ViewHolder {

        private RelativeLayout rlContent;
        private TextView txtTitle;
        private ImageView imgIcon;
        private ImageView imgImage;
        private TextView txtDate;
        private RelativeLayout rlControl;
        private Button btnRead;
        private Button btnEdit;
        private Button btnDelete;

        public InnerVH(View itemView) {
            super(itemView);
            rlContent = (RelativeLayout) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_rl_content);
            txtTitle = (TextView) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_txt_title);
            imgIcon = (ImageView) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_img_icon);
            imgImage = (ImageView) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_img_image);
            txtDate = (TextView) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_txt_date);
            rlControl = (RelativeLayout) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_rl_control);
            btnRead = (Button) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_btn_read);
            btnEdit = (Button) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_btn_edit);
            btnDelete = (Button) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_btn_delete);
        }
    }
}
