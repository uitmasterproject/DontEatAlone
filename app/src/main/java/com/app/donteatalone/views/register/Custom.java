package com.app.donteatalone.views.register;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.Hobby;

import java.util.ArrayList;

/**
 * Created by ChomChom on 4/1/2017.
 */

public class Custom extends BaseAdapter implements Filterable {
    private Context context;
    private int resource;
    private ArrayList<Hobby > hobbies;

    Custom(Context context, int resource, ArrayList<Hobby> hobbies){
        this.context=context;
        this.resource=resource;
        this.hobbies=hobbies;
    }

    @Override
    public int getCount() {
        return hobbies.size();
    }

    @Override
    public Object getItem(int position) {
        return hobbies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("Hello",hobbies.size()+"");
        View v=convertView;
        if(v==null){
            LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=layoutInflater.inflate(R.layout.custom_adapter_completetextview_register_final,null);
        }

        TextView textView=(TextView) v.findViewById(R.id.text);
        textView.setText(hobbies.get(position).getItemHobby());
        return null;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
