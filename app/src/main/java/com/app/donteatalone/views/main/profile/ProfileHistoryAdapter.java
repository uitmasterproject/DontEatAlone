package com.app.donteatalone.views.main.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.model.ProfileHistoryModel;
import com.app.donteatalone.views.register.Custom;

import java.util.ArrayList;

/**
 * Created by Le Hoang Han on 6/28/2017.
 */

public class ProfileHistoryAdapter extends RecyclerView.Adapter<ProfileHistoryAdapter.CustomViewHolder> {

    private ArrayList<ProfileHistoryModel> listProfileHistory;
    private Context context;

    public ProfileHistoryAdapter(ArrayList<ProfileHistoryModel> _listProfileHistory, Context context) {
        this.listProfileHistory = _listProfileHistory;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_fragment_profile_history,null);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.tvDate.setText(listProfileHistory.get(position).getDate());
        holder.tvFriendName.setText(listProfileHistory.get(position).getFullName());
        holder.tvPlace.setText(listProfileHistory.get(position).getPlace());

        /*Event click to friend name*/
        holder.tvFriendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Link to him/her profile",Toast.LENGTH_SHORT).show();
            }
        });

        /*Event click to place*/
        holder.tvPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Link to restaurant",Toast.LENGTH_SHORT).show();
            }
        });
        /*Event Click to icon Share*/
        holder.rlShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Share via Facebook",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return listProfileHistory.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        private TextView tvDate, tvTime, tvFriendName, tvPlace;
        private ImageView ivHeart;
        private RelativeLayout rlHeart, rlShare;

        public CustomViewHolder(View itemView){
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_tv_date);
            tvFriendName = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_tv_friend_name);
            tvPlace = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_tv_place);
            ivHeart = (ImageView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_iv_heart);
            rlHeart = (RelativeLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_rl_heart);
            rlShare = (RelativeLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_rl_share);
        }
    }


}
