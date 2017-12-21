package com.app.donteatalone.views.main.restaurant.AllowReservation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.donteatalone.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by ChomChom on 20-Dec-17
 */

public class ImagePagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> listDesignImage;
    private ArrayList<Target> listTarget;

    private DisplayMetrics metrics;

    public ImagePagerAdapter(Context context, ArrayList<String> listDesignImage) {
        this.context = context;
        this.listDesignImage = listDesignImage;
        listTarget = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return listDesignImage.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_adapter_popup_icon, container, false);

        final ImageView img = (ImageView) view.findViewById(R.id.img_popup_icon);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        img.setLayoutParams(layoutParams);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                progressBar.setVisibility(View.GONE);
                if (bitmap != null) {
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                            bitmap, metrics.widthPixels, (int)context.getResources().getDimension(R.dimen.image_size), false);
                    img.setImageBitmap(resizedBitmap);
                } else {
                    img.setImageResource(R.drawable.temp);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                progressBar.setVisibility(View.GONE);
                img.setImageResource(R.drawable.temp);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        listTarget.add(target);
        progressBar.setVisibility(View.VISIBLE);
        try {
            Picasso.with(context)
                    .load(listDesignImage.get(position))
                    .into(listTarget.get(position));
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
        }
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        // Remove viewpager_item.xml from ViewPager
//        container.removeView((LinearLayout) object);

    }
}
