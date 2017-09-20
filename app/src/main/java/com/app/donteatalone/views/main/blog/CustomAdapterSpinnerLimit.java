package com.app.donteatalone.views.main.blog;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.donteatalone.R;

import java.util.ArrayList;

/**
 * Created by ChomChom on 13-Aug-17.
 */

public class CustomAdapterSpinnerLimit extends ArrayAdapter {
    private ArrayList<String> listLimit;
    public CustomAdapterSpinnerLimit(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        listLimit= objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    private View getCustomView(int position,View convertView, ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_spinner_limit,parent,false);
        TextView txtContent=(TextView)view.findViewById(R.id.custom_adapter_spinner_limit_txt_content);
        txtContent.setText(listLimit.get(position));
        switch (position){
            case 0:
                txtContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.blue_dot,0,0,0);
                break;
            case 1:
                txtContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.blue_dot,0,0,0);
                break;
        }
        return view;
    }
}
