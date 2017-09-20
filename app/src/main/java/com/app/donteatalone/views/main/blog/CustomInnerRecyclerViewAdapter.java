package com.app.donteatalone.views.main.blog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.model.Status;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ChomChom on 19-Aug-17
 */

public class CustomInnerRecyclerViewAdapter extends RecyclerView.Adapter<CustomInnerRecyclerViewAdapter.InnerVH> {
    private ArrayList<InfoBlog> listInnerBlog;
    private Context context;

    public CustomInnerRecyclerViewAdapter(ArrayList<InfoBlog> listInnerBlog, Context context) {
        this.listInnerBlog = listInnerBlog;
        this.context = context;
    }

    @Override
    public InnerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_inner_recycler_my_status_blog, null, false);
        return new InnerVH(view);
    }

    @Override
    public void onBindViewHolder(InnerVH holder, int position) {
        holder.txtTitle.setText(listInnerBlog.get(position).getTitle());
        holder.imgIcon.setImageResource(defineIconforFeeling(listInnerBlog.get(position).getFeeling()));
        holder.txtFeel.setText(listInnerBlog.get(position).getFeeling());
        holder.imgImage.setImageBitmap(decodeBitmap(listInnerBlog.get(position).getImage().get(0)));
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
                intent.putExtra("infoBlog", listInnerBlog.get(position));
                intent.putExtra("own", "ownBlogFragmment");
                context.startActivity(intent);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StatusActivity.class);
                intent.putExtra("infoBlog", listInnerBlog.get(position));
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Status> deleteStatus = Connect.getRetrofit().deleteStatusBlog(storeReference("phoneLogin"), holder.txtDate.getText().toString());
                deleteStatus.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if (response.body().getStatus().equals("Delete success")) {
                            listInnerBlog.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listInnerBlog.size();
    }

    private int defineIconforFeeling(String feeling) {
        int icon = 0;
        if (feeling.equals("feeling đáng yêu") == true) {
            icon = R.drawable.ic_felling_cute;
            return icon;
        } else if (feeling.equals("feeling thú vị") == true) {
            icon = R.drawable.ic_felling_exciting;
            return icon;
        } else if (feeling.equals("feeling vui vẻ") == true) {
            icon = R.drawable.ic_felling_smile;
            return icon;
        } else if (feeling.equals("feeling buồn") == true) {
            icon = R.drawable.ic_felling_sad;
            return icon;
        }
        return icon;
    }

    private String storeReference(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account", MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    private Bitmap decodeBitmap(String avatar) {
        Bitmap bitmap = null;
        if (avatar.equals("") != true) {
            try {
                byte[] encodeByte = Base64.decode(avatar, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return bitmap;
    }

    public class InnerVH extends RecyclerView.ViewHolder {

        private RelativeLayout rlContent;
        private TextView txtTitle;
        private ImageView imgIcon;
        private TextView txtFeel;
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
            txtFeel = (TextView) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_txt_feel);
            imgImage = (ImageView) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_img_image);
            txtDate = (TextView) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_txt_date);
            rlControl = (RelativeLayout) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_rl_control);
            btnRead = (Button) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_btn_read);
            btnEdit = (Button) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_btn_edit);
            btnDelete = (Button) itemView.findViewById(R.id.custom_adapter_inner_recyclerview_my_status_blog_btn_delete);
        }
    }
}
