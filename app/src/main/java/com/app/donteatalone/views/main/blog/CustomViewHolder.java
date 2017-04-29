package com.app.donteatalone.views.main.blog;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;

/**
 * Created by ChomChom on 4/23/2017.
 */

public class CustomViewHolder extends ViewHolder {

    protected ImageButton imgbtnDelete, imgbtnSharefb;
    protected Button btnContinue;
    protected ImageView imgAvatar,imgFeeling;
    protected TextView txtNameUser,txtFeeling, txtDate, txtInfoStatus, txtStatusMore;
    protected LinearLayout llContainerFeeling,llContainerImage;
    protected HorizontalScrollView hsvImage;

    public CustomViewHolder(View itemView) {
        super(itemView);
        Log.e("CustomViewHolder", "Show"+"================");
        imgbtnDelete=(ImageButton) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_imgbtn_delete);
        imgbtnSharefb=(ImageButton) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_imgbtn_share_fb);
        btnContinue=(Button) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_btn_continue);
        imgAvatar=(ImageView) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_img_avatar);
        imgFeeling=(ImageView) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_img_feeling);
        txtNameUser=(TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_txt_name);
        txtFeeling=(TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_txt_feeling);
        txtDate=(TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_txt_date);
        txtInfoStatus=(TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_txt_info_status);
        txtStatusMore=(TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_txt_status_more);
        llContainerFeeling=(LinearLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_ll_container_feeling);
        llContainerImage=(LinearLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_ll_container_image);
        hsvImage=(HorizontalScrollView) itemView.findViewById(R.id.custom_adapter_recyclerview_my_status_blog_hsv_image);
        llContainerFeeling.setVisibility(View.GONE);
        btnContinue.setVisibility(View.GONE);
        txtStatusMore.setVisibility(View.GONE);
        hsvImage.setVisibility(View.GONE);

    }
}
