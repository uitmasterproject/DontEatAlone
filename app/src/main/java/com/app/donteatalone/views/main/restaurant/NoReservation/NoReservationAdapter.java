package com.app.donteatalone.views.main.restaurant.NoReservation;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.base.OnRecyclerItemClickListener;
import com.app.donteatalone.model.Restaurant;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

/**
 * Created by ChomChom on 09-Dec-17
 */

public class NoReservationAdapter extends RecyclerView.Adapter<NoReservationAdapter.MyViewHolder> {
    private ArrayList<Restaurant> listRestaurant;
    private ArrayList<Target> listTarget = new ArrayList<>();
    private ArrayList<MyViewHolder> views;

    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    private Context context;

    public NoReservationAdapter(ArrayList<Restaurant> listRestaurant, Context context) {
        this.listRestaurant = listRestaurant;
        this.context = context;
        views = new ArrayList<>();
    }

    @Override
    public NoReservationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_fragment_restaurant_not_reservation, parent, false);
        return new MyViewHolder(view);
    }

    public void setClickItemRecyclerView(OnRecyclerItemClickListener onRecyclerItemClickListener){
        this.onRecyclerItemClickListener= onRecyclerItemClickListener;
    }

    @Override
    public void onBindViewHolder(final NoReservationAdapter.MyViewHolder holder, int position) {
        views.add(holder);

        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.progressBar.setVisibility(View.GONE);
                if (bitmap != null) {
                    holder.imgAvatar.setImageBitmap(bitmap);
                } else {
                    holder.imgAvatar.setImageResource(R.drawable.ic_home);
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
        listTarget.add(position, target);

        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.with(context)
                .load(listRestaurant.get(position).getAvatar())
                .into(listTarget.get(position));
        holder.txtRate.setText(listRestaurant.get(position).getRate());
        holder.txtName.setText(StringEscapeUtils.unescapeJava(listRestaurant.get(position).getName()));
        holder.txtAddress.setText(StringEscapeUtils.unescapeJava(listRestaurant.get(position).getAddress()));
        if (TextUtils.isEmpty(listRestaurant.get(position).getOpenDay())) {
            holder.txtOpenDay.setText("All day");
        }else {
            holder.txtOpenDay.setText(listRestaurant.get(position).getOpenDay());
        }
    }

    @Override
    public int getItemCount() {
        return listRestaurant.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgAvatar;
        private TextView txtName, txtAddress, txtOpenDay, txtRate;
        private ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_restaurant_not_reservation_iv_image);
            txtName = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_restaurant_not_reservation_tv_name);
            txtAddress = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_restaurant_not_reservation_tv_address);
            txtOpenDay = (TextView) itemView.findViewById(R.id.custom_adapter_recycerview_fragment_restaurant_not_reservation_tv_open_time);
            txtRate = (TextView) itemView.findViewById(R.id.txt_rate);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(context);
            switch (v.getId()) {
                case R.id.custom_dialog_restaurant_no_reservation_ib_close:
                    dialog.cancel();
                    break;
                default:
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_dialog_restaurant_no_reservation, null);

                    ImageView imgAvatar = (ImageView) view.findViewById(R.id.custom_dialog_restaurant_no_reservation_ib_image);
                    TextView txtName = (TextView) view.findViewById(R.id.custom_dialog_restaurant_no_reservation_name);
                    TextView txtRate = (TextView) view.findViewById(R.id.custom_dialog_restaurant_no_reservation_ib_rate);
                    TextView txtAddress = (TextView) view.findViewById(R.id.custom_dialog_restaurant_no_reservation_tv_address);
                    TextView txtTime = (TextView) view.findViewById(R.id.custom_dialog_restaurant_no_reservation_tv_open_time);
                    TextView txtPrice = (TextView) view.findViewById(R.id.custom_dialog_restaurant_no_reservation_tv_price);
                    Button btnDone = (Button) view.findViewById(R.id.custom_dialog_restaurant_no_reservation_ib_close);
                    ImageView imgClose = (ImageView) view.findViewById(R.id.img_close);
                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(onRecyclerItemClickListener!=null){
                                onRecyclerItemClickListener.onItemClick(v,getAdapterPosition());
                            }
                            dialog.cancel();
                        }
                    });

                    imgClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    imgAvatar.setImageBitmap(((BitmapDrawable) this.imgAvatar.getDrawable()).getBitmap());
                    txtName.setText(this.txtName.getText().toString());
                    txtRate.setText(this.txtRate.getText().toString());
                    txtAddress.setText(this.txtAddress.getText().toString());
                    if (TextUtils.isEmpty(listRestaurant.get(getAdapterPosition()).getOpenDay())) {
                        txtTime.setText("10:00 - 22:00");
                    } else {
                        txtTime.setText(listRestaurant.get(getAdapterPosition()).getOpenDay());
                    }

                    if (TextUtils.isEmpty(listRestaurant.get(getAdapterPosition()).getPrice())) {
                        txtPrice.setText("20.000 - 10.000");
                    } else {
                        txtPrice.setText(listRestaurant.get(getAdapterPosition()).getPrice());
                    }

                    dialog.setContentView(view);
                    dialog.show();
                    break;
            }


        }
    }
}
