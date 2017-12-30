package com.app.donteatalone.views.main.restaurant.AllowReservation;

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
import com.app.donteatalone.model.RestaurantDetail;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

/**
 * Created by ChomChom on 23-Dec-17
 */

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.MyViewHolder> {
    private ArrayList<RestaurantDetail> listReservation;
    private ArrayList<Target> listTarget = new ArrayList<>();
    private ArrayList<ReservationAdapter.MyViewHolder> views;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    private OnRecyclerItemClickListener onClearRestaurant;
    private Context context;
    private boolean isClear;

    public ReservationAdapter(ArrayList<RestaurantDetail> listReservation, Context context, boolean isClear) {
        this.listReservation = listReservation;
        this.context = context;
        this.isClear = isClear;
        views = new ArrayList<>();
    }

    @Override
    public ReservationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_fragmnet_restaurant_reservation_booked, parent, false);
        return new ReservationAdapter.MyViewHolder(view);
    }

    public void setOnClickRecyclerView(OnRecyclerItemClickListener onClickRecyclerView) {
        this.onRecyclerItemClickListener = onClickRecyclerView;
    }

    public void setOnClearRestaurant(OnRecyclerItemClickListener onClearRestaurant) {
        this.onClearRestaurant = onClearRestaurant;
    }

    @Override
    public void onBindViewHolder(final ReservationAdapter.MyViewHolder holder, int position) {
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
                .load(listReservation.get(position).getAvatar())
                .into(listTarget.get(position));
        holder.txtName.setText(StringEscapeUtils.unescapeJava(listReservation.get(position).getName()));
        holder.txtAddress.setText(StringEscapeUtils.unescapeJava(listReservation.get(position).getAddress()));
        holder.txtSession.setText(listReservation.get(position).getListReservations().get(0).getSession());
        holder.txtTime.setText(listReservation.get(position).getListReservations().get(0).getTime());
        holder.txtTable.setText(context.getString(R.string.table) + listReservation.get(position).getListReservations().get(0).getTable());
    }

    @Override
    public int getItemCount() {
        return listReservation.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgAvatar;
        private TextView txtName, txtAddress, txtSession, txtTime, txtTable;
        private ProgressBar progressBar;
        private ImageView imgClear;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            txtName = (TextView) itemView.findViewById(R.id.txt_name_restaurant);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address_restaurant);
            txtSession = (TextView) itemView.findViewById(R.id.txt_session);
            txtTime = (TextView) itemView.findViewById(R.id.txt_time);
            txtTable = (TextView) itemView.findViewById(R.id.txt_table);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            imgClear = (ImageView) itemView.findViewById(R.id.img_clear);

            if (isClear) {
                imgClear.setVisibility(View.VISIBLE);
            } else {
                imgClear.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(context);
            switch (v.getId()) {
                case R.id.img_clear:
                    if (onClearRestaurant != null) {
                        onClearRestaurant.onItemClick(v, getAdapterPosition());
                    }
                    break;
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
                    ImageView imgClose = (ImageView) view.findViewById(R.id.img_close);
                    Button btnDone = (Button) view.findViewById(R.id.custom_dialog_restaurant_no_reservation_ib_close);
                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onRecyclerItemClickListener != null) {
                                onRecyclerItemClickListener.onItemClick(v, getAdapterPosition());
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
                    txtRate.setText(listReservation.get(getAdapterPosition()).getRate());
                    txtAddress.setText(this.txtAddress.getText().toString());
                    if (TextUtils.isEmpty(listReservation.get(getAdapterPosition()).getOpenDay())) {
                        txtTime.setText("10:00 - 22:00");
                    } else {
                        txtTime.setText(listReservation.get(getAdapterPosition()).getOpenDay());
                    }

                    if (TextUtils.isEmpty(listReservation.get(getAdapterPosition()).getPrice())) {
                        txtPrice.setText("20.000 - 10.000");
                    } else {
                        txtPrice.setText(listReservation.get(getAdapterPosition()).getPrice());
                    }

                    dialog.setContentView(view);
                    dialog.show();
                    break;
            }


        }
    }
}