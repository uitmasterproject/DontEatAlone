package com.app.donteatalone.views.main.restaurant.AllowReservation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.base.OnRecyclerItemClickListener;
import com.app.donteatalone.model.RestaurantDetail;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by ChomChom on 17-Dec-17
 */

public class AllowRestaurantAdapter extends RecyclerView.Adapter<AllowRestaurantAdapter.MyViewHolder> {
    private ArrayList<RestaurantDetail> listRestaurant;
    private Context context;
    private View view;
    private ArrayList<Target> listTarget;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public AllowRestaurantAdapter(ArrayList<RestaurantDetail> listRestaurant, Context context, View view) {
        this.listRestaurant = listRestaurant;
        this.context = context;
        this.listTarget = new ArrayList<>();
        this.view=view;
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_fragment_restaurant_reservation, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.progressBar.setVisibility(View.GONE);
                if(bitmap!=null){
                    holder.imgAvatar.setImageBitmap(bitmap);
                }else {
                    holder.imgAvatar.setImageResource(R.drawable.temp);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        listTarget.add(position,target);

        if(TextUtils.isEmpty(listRestaurant.get(position).getAvatar())){
            holder.progressBar.setVisibility(View.GONE);
            holder.imgAvatar.setImageResource(R.drawable.temp);
        }else {
            Picasso.with(context)
                    .load(listRestaurant.get(position).getAvatar())
                    .into(listTarget.get(position));
        }

        holder.txtName.setText(listRestaurant.get(position).getName());
        holder.txtAddress.setText(listRestaurant.get(position).getAddress());
        if(TextUtils.isEmpty(listRestaurant.get(position).getOpenDay())) {
            holder.txtTime.setText("All day");
        }else {
            holder.txtTime.setText(listRestaurant.get(position).getOpenDay());
        }

    }

    @Override
    public int getItemCount() {
        return listRestaurant.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgAvatar;
        private ProgressBar progressBar;
        private TextView txtName;
        private TextView txtAddress;
        private TextView txtTime;
        private Button btnReservation;


        public MyViewHolder(View itemView) {
            super(itemView);

            imgAvatar = (ImageView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_restaurant_reservation_image);
            txtName = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_restaurant_reservation_tv_name);
            txtAddress = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_restaurant_reservation_tv_address);
            txtTime = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_restaurant_reservation_tv_open_time);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            btnReservation = (Button) itemView.findViewById(R.id.btn_reservation);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onRecyclerItemClickListener != null) {
                onRecyclerItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
