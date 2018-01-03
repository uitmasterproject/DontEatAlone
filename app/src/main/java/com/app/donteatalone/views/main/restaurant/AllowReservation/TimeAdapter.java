package com.app.donteatalone.views.main.restaurant.AllowReservation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.base.OnRecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by ChomChom on 21-Dec-17
 */

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyViewHolder> {

    private ArrayList<String> listTimeSession;
    private Context context;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    private ArrayList<MyViewHolder> views;

    public TimeAdapter(ArrayList<String> listTimeSession, Context context) {
        this.listTimeSession = listTimeSession;
        this.context = context;
        this.views=new ArrayList<>();
    }

    public void setClickItem(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.textview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        views.add(holder);
        holder.txtTimeSession.setText(listTimeSession.get(position));

        holder.txtTimeSession.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (onRecyclerItemClickListener != null) {
                    for(int i=0;i<views.size();i++){
                            TextView textView = views.get(i).txtTimeSession;
                            textView.setBackgroundColor(android.R.color.darker_gray);
                    }
                    holder.txtTimeSession.setBackgroundColor(Color.GREEN);
                    onRecyclerItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTimeSession.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTimeSession;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTimeSession = (TextView) itemView.findViewById(R.id.txt_time_session);
        }
    }
}
