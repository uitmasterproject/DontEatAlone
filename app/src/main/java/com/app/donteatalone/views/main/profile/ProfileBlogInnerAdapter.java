package com.app.donteatalone.views.main.profile;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.app.donteatalone.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ChomChom on 22-Aug-17
 */

public class ProfileBlogInnerAdapter extends BaseAdapter {
    private ArrayList<String> listImage;
    private Context context;

    public ProfileBlogInnerAdapter(ArrayList<String> listImage, Context context) {
        this.listImage = listImage;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listImage.size();
    }

    @Override
    public Object getItem(int position) {
        return listImage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        }
        else
        {
            imageView = (ImageView) convertView;
        }

        Picasso.with(context)
                .load(listImage.get(position))
                .error(R.drawable.temp)
                .into(imageView);
        return imageView;
    }
}
