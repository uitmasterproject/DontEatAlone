package com.app.donteatalone.views.main.restaurant.AllowReservation;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.donteatalone.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChomChom on 20-Dec-17
 */

public class RealImageAdapter extends RecyclerView.Adapter<RealImageAdapter.ViewHolder> {

    private List<String> data;
    private ArrayList<Target> listTarget;
    private ArrayList<ViewHolder> views;

    public RealImageAdapter(List<String> data) {
        this.data = data;
        listTarget = new ArrayList<>();
        views = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.custom_adapter_popup_icon, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        views.add(holder);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (bitmap != null) {
                    holder.image.setImageBitmap(bitmap);
                } else {
                    holder.image.setImageResource(R.drawable.temp);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        listTarget.add(target);
        try {
            Picasso.with(holder.itemView.getContext())
                    .load(data.get(position))
                    .into(listTarget.get(position));
        }catch (Exception e){
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.img_popup_icon);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(700, 700);
            image.setLayoutParams(layoutParams);
        }
    }
}