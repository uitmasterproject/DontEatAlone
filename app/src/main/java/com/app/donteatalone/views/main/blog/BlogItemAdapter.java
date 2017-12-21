package com.app.donteatalone.views.main.blog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.lang3.StringUtils;

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
    private ArrayList<InnerVH> views;

    private ArrayList<Target> listTarget = new ArrayList<>();

    public BlogItemAdapter(ArrayList<InfoBlog> listInnerBlog, Context context) {
        this.listInnerBlog = listInnerBlog;
        this.context = context;
        baseProgress = new BaseProgress();
        views = new ArrayList<>();
    }

    @Override
    public InnerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_adapter_inner_recycler_my_status_blog, parent, false);
        return new InnerVH(view);
    }

    @Override
    public void onBindViewHolder(final InnerVH holder, final int position) {

        views.add(holder);

        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (bitmap != null) {
                    if (bitmap.getHeight() < AppUtils.convertDpToPx(120)) {

                        bitmap = Bitmap.createScaledBitmap(bitmap, AppUtils.convertDpToPx(120) * bitmap.getWidth() / bitmap.getHeight(), AppUtils.convertDpToPx(120), true);
                    }
                    holder.imgImage.setImageBitmap(bitmap);
                } else {
                    holder.imgImage.setImageBitmap(null);
                }
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        listTarget.add(target);

        holder.txtTitle.setText(StringUtils.capitalize(listInnerBlog.get(position).getTitle()));
        if (listInnerBlog.get(position).getLimit().equals("private")) {
            holder.imgIcon.setImageResource(R.drawable.ic_private);
        } else {
            holder.imgIcon.setImageResource(R.drawable.ic_public);
        }

        holder.progressBar.setVisibility(View.VISIBLE);
        if (listInnerBlog.get(position).getImage().size() > 0) {
            Picasso.with(context)
                    .load(listInnerBlog.get(position).getImage().get(0))
                    .into(listTarget.get(position));

        } else {
            holder.imgImage.setImageBitmap(null);
            holder.progressBar.setVisibility(View.GONE);
        }

        if (listInnerBlog.get(position).getDate().contains(" ")) {
            holder.txtDate.setText(listInnerBlog.get(position).getDate().substring(0,
                    listInnerBlog.get(position).getDate().lastIndexOf(" ")));
        }

        if(listInnerBlog.get(position).getFeeling()==0){
            holder.imgEmotion.setImageResource(R.drawable.ic_happy);
        }else {
            try {
                holder.imgEmotion.setImageResource(listInnerBlog.get(position).getFeeling());
            }catch (Exception e){
                holder.imgEmotion.setImageResource(R.drawable.ic_happy);
            }
        }

        holder.txtImageNumber.setText(listInnerBlog.get(position).getImage().size()+"");

        holder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.llControl.getVisibility() == View.GONE) {
                    TranslateAnimation anim = new TranslateAnimation(250f, 0f, 0f, 0f);
                    anim.setDuration(300);
                    for (int i = 0; i < views.size(); i++) {
                        if (i != position) {
                            InnerVH view = views.get(i);
                            LinearLayout ll = view.llControl;
                            if (ll.getVisibility() == View.VISIBLE) {
                                ll.setVisibility(View.GONE);
                            }
                        }
                    }
                    holder.llControl.setAnimation(anim);
                    holder.llControl.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.llControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation anim = new TranslateAnimation(0f, 250f, 0f, 00f);
                anim.setDuration(300);
                holder.llControl.setAnimation(anim);
                holder.llControl.setVisibility(View.GONE);
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
                Call<Status> deleteStatus = Connect.getRetrofit().deleteStatusBlog(new MySharePreference((Activity) context).getPhoneLogin(), listInnerBlog.get(position).getDate());
                deleteStatus.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        baseProgress.hideProgressLoading();
                        if (response.body() != null) {
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

        private LinearLayout llContent;
        private TextView txtTitle;
        private ImageView imgIcon;
        private ImageView imgImage;
        private TextView txtDate;
        private LinearLayout llControl;
        private Button btnRead;
        private Button btnEdit;
        private Button btnDelete;
        private ImageView imgEmotion;
        private TextView txtImageNumber;
        private ProgressBar progressBar;

        public InnerVH(View itemView) {
            super(itemView);
            llContent = (LinearLayout) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_ll_content);
            txtTitle = (TextView) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_txt_title);
            imgIcon = (ImageView) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_img_icon);
            imgImage = (ImageView) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_img_image);
            txtDate = (TextView) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_txt_date);
            llControl = (LinearLayout) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_ll_control);
            btnRead = (Button) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_btn_read);
            btnEdit = (Button) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_btn_edit);
            btnDelete = (Button) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_btn_delete);
            imgEmotion = (ImageView) itemView.findViewById(R.id.img_emotion);
            txtImageNumber = (TextView) itemView.findViewById(R.id.txt_image_number);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }
}
