package com.app.donteatalone.views.main.blog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
 * Created by ChomChom on 4/23/2017.
 */

public class CustomBlogRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private ArrayList<InfoBlog> myInfoBlogses;
    private String name,phone;

    public CustomBlogRecyclerViewAdapter(Context context, ArrayList<InfoBlog> myInfoBlog,String phone){
        this.context=context;
        this.myInfoBlogses=myInfoBlog;
        this.phone=phone;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_my_status_blog, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.imgAvatar.setImageBitmap(decodeBitmap(storeReference()));
        holder.txtNameUser.setText(name);
        if(myInfoBlogses.get(position).getFeeling().equals("")!=true){
            holder.llContainerFeeling.setVisibility(View.VISIBLE);
            holder.imgFeeling.setImageResource(defineIconforFeeling(myInfoBlogses.get(position).getFeeling()));
            holder.txtFeeling.setText(myInfoBlogses.get(position).getFeeling());
        }
        holder.txtDate.setText(myInfoBlogses.get(position).getDate());
        if(myInfoBlogses.get(position).getInfoStatus().length()>100){
            holder.txtInfoStatus.setText(myInfoBlogses.get(position).getInfoStatus().substring(0,100)+"...");
            holder.btnContinue.setVisibility(View.VISIBLE);
            holder.txtStatusMore.setText(myInfoBlogses.get(position).getInfoStatus().substring(100));
        }
        else {
            holder.txtInfoStatus.setText(myInfoBlogses.get(position).getInfoStatus());
        }
        if(myInfoBlogses.get(position).getImage().size()!=0){
            holder.hsvImage.setVisibility(View.VISIBLE);
            holder.llContainerImage.removeAllViews();
            for(int i=0;i<myInfoBlogses.get(position).getImage().size();i++) {
                Bitmap bmp=decodeBitmap(myInfoBlogses.get(position).getImage().get(i));
                ImageView img = new ImageView(context);
                if(bmp.getWidth()< bmp.getHeight()) {
                    img.setLayoutParams(new LinearLayout.LayoutParams(300, bmp.getHeight()*300/100));
                }
                else {
                    img.setLayoutParams(new LinearLayout.LayoutParams(bmp.getWidth() * 300 / 100, 300));
                }
                img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                img.setImageBitmap(bmp);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) img.getLayoutParams();
                layoutParams.setMargins(10, 10, 20, 10);
                img.setLayoutParams(layoutParams);
                holder.llContainerImage.addView(img);
            }
        }
        holder.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btnContinue.getText().equals("Continue")==true) {
                    holder.txtInfoStatus.setText(holder.txtInfoStatus.getText().toString().substring(0, 100)+
                            holder.txtStatusMore.getText().toString());
                    holder.btnContinue.setText("Less");
                }
                else {
                    holder.txtInfoStatus.setText(holder.txtInfoStatus.getText().toString().substring(0,100)+"...");
                    holder.btnContinue.setText("Continue");
                }
            }
        });

        holder.imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect connect=new Connect();
                Call<Status> deleteStatus = connect.getRetrofit().deleteStatusBlog(phone,holder.txtDate.getText().toString());
                deleteStatus.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if(response.body().getStatus().equals("Delete success")==true){
                            myInfoBlogses.remove(position);
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
        Log.e("Create",myInfoBlogses.size()+"Create======================");
        return myInfoBlogses.size();
    }

    private Bitmap decodeBitmap(String avatar){
        Bitmap bitmap=null;
        if(avatar.equals("")!=true) {
            try {
                byte[] encodeByte = Base64.decode(avatar, Base64.DEFAULT);
                bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return bitmap;
    }

    private String storeReference(){
        SharedPreferences sharedPreferences=context.getSharedPreferences("account",MODE_PRIVATE);
        Boolean bchk=sharedPreferences.getBoolean("checked", false);
        String avatar="";
        if(bchk==false)
        {
            avatar=sharedPreferences.getString("avatarLogin", "");
            name=sharedPreferences.getString("fullnameLogin","").toUpperCase();
        }
        return avatar;
    }

    private int defineIconforFeeling(String feeling){
        int icon = 0;
        if(feeling.equals("feeling đáng yêu")==true) {
            icon = R.drawable.ic_felling_cute;
            return icon;
        }
        else if(feeling.equals("feeling thú vị")==true) {
            icon = R.drawable.ic_felling_exciting;
            return icon;
        }
        else if(feeling.equals("feeling vui vẻ")==true) {
            icon = R.drawable.ic_felling_smile;
            return icon;
        }
        else if(feeling.equals("feeling buồn")==true) {
            icon = R.drawable.ic_felling_sad;
            return icon;
        }
        return icon;
    }
}
