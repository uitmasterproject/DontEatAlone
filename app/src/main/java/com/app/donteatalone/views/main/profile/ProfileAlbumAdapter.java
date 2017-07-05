package com.app.donteatalone.views.main.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by ChomChom on 05-Jul-17.
 */

public class ProfileAlbumAdapter  extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> listImage;

    // Constructor
    public ProfileAlbumAdapter(Context c, ArrayList<String>_listImage) {
        mContext = c;
        this.listImage=_listImage;
    }

    public int getCount() {
        return listImage.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        Log.e("position",position+"");
        imageView.setImageBitmap(decodeBitmap(listImage.get(position)));
        return imageView;
    }

    private Bitmap decodeBitmap(String avatar){
        Bitmap bitmap=null;
        if(avatar.equals("")!=true) {
            try {
                byte[] encodeByte = Base64.decode(avatar, Base64.DEFAULT);
                bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return bitmap;
    }
}