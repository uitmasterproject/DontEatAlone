package com.app.donteatalone.views.main.notification;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.InfoNotification;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by ChomChom on 09-Jun-17.
 */

public class CustomNotificationAdapter extends RecyclerView.Adapter<CustomNotificationAdapter.CustomViewHolder>{

    private ArrayList<InfoNotification> listInfoNotification;
    private Context context;
    private String phone;

    public CustomNotificationAdapter(ArrayList<InfoNotification> _listInfoNotification, Context _context, String phone){
        this.listInfoNotification=_listInfoNotification;
        this.context=_context;
        this.phone=phone;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recycleview_notification,null);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        Log.e("data",listInfoNotification.get(position)+"");
        holder.txtName.setText(listInfoNotification.get(position).getNameSend());
        if(listInfoNotification.get(position).getStatus().equals("accept")==true){
            holder.txtContent.setText("was accepted your invite. Please, Let's contact their by phone: "+ listInfoNotification.get(position).getUserSend());
            if(listInfoNotification.get(position).getRead().equals("0")==true)
                holder.llContainer.setBackgroundColor(Color.CYAN);
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.llContainer.setBackgroundColor(Color.WHITE);
                    showDialog(position);
                }
            });
        }
        else if(listInfoNotification.get(position).getStatus().equals("refuse")==true){
            holder.txtContent.setText("was refuse your invite. Please, Let's choose a friend who is the best accordant than with you");
            if(listInfoNotification.get(position).getRead().equals("0")==true)
                holder.llContainer.setBackgroundColor(Color.CYAN);
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.llContainer.setBackgroundColor(Color.WHITE);
                    showDialog(position);
                }
            });
        }
        else{
            holder.txtContent.setText("was invited you eat their together                                                            ");
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(position);
                }
            });
        }
    }

    private void showDialog(int position){
        Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_require_on_result_invite);
        CustomDialogItemNotification customDialog=new CustomDialogItemNotification(dialog,context,listInfoNotification.get(position),phone);
        try {
            customDialog.setDefaultValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return listInfoNotification.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName,txtContent;
        private LinearLayout llContainer;
        public CustomViewHolder(View itemView) {
            super(itemView);
            txtName=(TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_notification_txt_name);
            txtContent=(TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_notification_txt_content);
            llContainer=(LinearLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_notification_ll_container);
        }
    }
}
