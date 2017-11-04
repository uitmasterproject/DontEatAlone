package com.app.donteatalone.views.main.require.main_require.on_require;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.Restaurant;

import java.util.ArrayList;

/**
 * Created by ChomChom on 27-Jun-17
 */

public class CustomInvitedRestaurantAdapter extends RecyclerView.Adapter<CustomInvitedRestaurantAdapter.MyViewHolder>{
    private ArrayList<Restaurant> listRestaurant;
    private TextView textView;

    public CustomInvitedRestaurantAdapter(ArrayList<Restaurant> _listRestaurant,TextView textView){
        this.listRestaurant=_listRestaurant;
        this.textView=textView;
    }

    public CustomInvitedRestaurantAdapter(ArrayList<Restaurant> _listRestaurant){
        this.listRestaurant=_listRestaurant;
    }

    @Override
    public CustomInvitedRestaurantAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_restaurant,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomInvitedRestaurantAdapter.MyViewHolder holder, int position) {
        holder.txtName.setText(listRestaurant.get(position).getName());
        holder.txtAddress.setText(listRestaurant.get(position).getAddress());
        holder.txtOpenDay.setText(listRestaurant.get(position).getOpenDay());
        holder.tblContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView!=null)
                    textView.setText(listRestaurant.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listRestaurant.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout tblContainer;
        private TextView txtName, txtAddress, txtOpenDay;

        public MyViewHolder(View itemView) {
            super(itemView);
            tblContainer=(LinearLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_restaurant_container);
            txtName=(TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_restaurant_txt_name);
            txtAddress=(TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_restaurant_txt_address);
            txtOpenDay=(TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_restaurant_txt_openDay);
        }
    }
}
