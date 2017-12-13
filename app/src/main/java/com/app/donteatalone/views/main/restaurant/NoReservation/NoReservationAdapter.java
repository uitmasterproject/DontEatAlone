package com.app.donteatalone.views.main.restaurant.NoReservation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.Restaurant;

import java.util.ArrayList;

/**
 * Created by ChomChom on 09-Dec-17
 */

public class NoReservationAdapter extends RecyclerView.Adapter<NoReservationAdapter.MyViewHolder>{
    private ArrayList<Restaurant> listRestaurant;

    public NoReservationAdapter(ArrayList<Restaurant> listRestaurant) {
        this.listRestaurant = listRestaurant;
    }

    @Override
    public NoReservationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_fragment_restaurant_not_reservation,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoReservationAdapter.MyViewHolder holder, int position) {
        //holder.txtName.setText(listRestaurant.get(position).getName());
        holder.txtAddress.setText(listRestaurant.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return listRestaurant.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgAvatar;
        private TextView txtName, txtAddress, txtOpenDay;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgAvatar=(ImageView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_restaurant_not_reservation_iv_image);
            txtName=(TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_restaurant_not_reservation_tv_name);
            txtAddress=(TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_restaurant_not_reservation_tv_address);
            txtOpenDay=(TextView) itemView.findViewById(R.id.custom_adapter_recycerview_fragment_restaurant_not_reservation_tv_open_time);
        }
    }
}
