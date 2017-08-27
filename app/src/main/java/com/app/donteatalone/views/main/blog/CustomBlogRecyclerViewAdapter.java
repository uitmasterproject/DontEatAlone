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
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
//        if(myInfoBlogses.get(position).getImage().size()!=0){
//            holder.hsvImage.setVisibility(View.VISIBLE);
//            holder.llContainerImage.removeAllViews();
//            for(int i=0;i<myInfoBlogses.get(position).getImage().size();i++) {
//                Bitmap bmp=decodeBitmap(myInfoBlogses.get(position).getImage().get(i));
//                ImageView img = new ImageView(context);
//                if(bmp.getWidth()< bmp.getHeight()) {
//                    img.setLayoutParams(new LinearLayout.LayoutParams(300, bmp.getHeight()*300/100));
//                }
//                else {
//                    img.setLayoutParams(new LinearLayout.LayoutParams(bmp.getWidth() * 300 / 100, 300));
//                }
//                img.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                img.setImageBitmap(bmp);
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) img.getLayoutParams();
//                layoutParams.setMargins(10, 10, 20, 10);
//                img.setLayoutParams(layoutParams);
//                holder.llContainerImage.addView(img);
//            }
//        }
//        holder.btnContinue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(holder.btnContinue.getText().equals("Continue")==true) {
//                    holder.txtInfoStatus.setText(holder.txtInfoStatus.getText().toString().substring(0, 100)+
//                            holder.txtStatusMore.getText().toString());
//                    holder.btnContinue.setText("Less");
//                }
//                else {
//                    holder.txtInfoStatus.setText(holder.txtInfoStatus.getText().toString().substring(0,100)+"...");
//                    holder.btnContinue.setText("Continue");
//                }
//            }
//        });
        CustomInnerRecyclerViewAdapter adapter=new CustomInnerRecyclerViewAdapter(myInfoBlogs.get(position),context);
        holder.rcListBlog.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return myInfoBlogs.size();
    }


}
