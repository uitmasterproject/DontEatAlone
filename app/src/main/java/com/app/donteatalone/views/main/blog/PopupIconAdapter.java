package com.app.donteatalone.views.main.blog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.donteatalone.R;

import java.util.ArrayList;

/**
 * Created by ChomChom on 01-Nov-17
 */

public class PopupIconAdapter extends RecyclerView.Adapter<PopupIconAdapter.PopupIconVH> {

    private Context context;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    private ArrayList<Integer> listIcon = new ArrayList<Integer>() {{
        add(R.drawable.ic_happy);
        add(R.drawable.ic_laugh);
        add(R.drawable.ic_exercise);
        add(R.drawable.ic_interesting);
        add(R.drawable.ic_lovely);
        add(R.drawable.ic_suprise);
        add(R.drawable.ic_crazy);
        add(R.drawable.ic_cry);
        add(R.drawable.ic_angry);
    }};

    public PopupIconAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PopupIconVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PopupIconVH(LayoutInflater.from(context).inflate(R.layout.custom_adapter_popup_icon, parent, false));
    }

    @Override
    public void onBindViewHolder(PopupIconVH holder, int position) {
        holder.imgIcon.setImageResource(listIcon.get(position));
    }

    @Override
    public int getItemCount() {
        return listIcon.size();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public class PopupIconVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgIcon;

        public PopupIconVH(View itemView) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.img_popup_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onRecyclerItemClickListener != null) {
                onRecyclerItemClickListener.onItemClick(v, listIcon.get(getAdapterPosition()));
            }
        }
    }
}
