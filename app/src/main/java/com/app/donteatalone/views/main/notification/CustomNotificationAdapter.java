package com.app.donteatalone.views.main.notification;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.InfoNotification;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by ChomChom on 09-Jun-17
 */

public class CustomNotificationAdapter extends RecyclerView.Adapter<CustomNotificationAdapter.CustomViewHolder> {

    private ArrayList<InfoNotification> listInfoNotification;
    private Context context;

    public CustomNotificationAdapter(ArrayList<InfoNotification> listInfoNotification, Context context) {
        this.listInfoNotification = listInfoNotification;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_notification, null);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.txtName.setText(listInfoNotification.get(position).getNameSend());
        if (listInfoNotification.get(position).getStatus().equals("accept")) {
            holder.txtContent.setText("was accepted your invite. Please, Let's contact their by phone: " + listInfoNotification.get(position).getUserSend());
            holder.imgIcon.setImageResource(R.drawable.ic_accepted);
            holder.txtTimer.setText(listInfoNotification.get(position).getTimeSend());
            if (listInfoNotification.get(position).getRead().equals("0"))
                holder.llContainer.setBackgroundColor(Color.CYAN);
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.llContainer.setBackgroundColor(Color.WHITE);
                    showDialog(position);
                }
            });
        } else if (listInfoNotification.get(position).getStatus().equals("refuse")) {
            holder.txtContent.setText("was refuse your invite. Please, Let's choose a friend who is the best accordant than with you");
            holder.imgIcon.setImageResource(R.drawable.ic_refuse);
            holder.txtTimer.setText(listInfoNotification.get(position).getTimeSend());
            if (listInfoNotification.get(position).getRead().equals("0"))
                holder.llContainer.setBackgroundColor(Color.CYAN);
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.llContainer.setBackgroundColor(Color.WHITE);
                    showDialog(position);
                }
            });
        } else {
            holder.txtContent.setText("was invited you eat their together                                                            ");
            holder.imgIcon.setImageResource(R.drawable.ic_invite);
            holder.txtTimer.setText(listInfoNotification.get(position).getTimeSend());
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(position);
                }
            });
        }
    }

    private void showDialog(int position) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_require_on_result_invite);
        CustomDialogItemNotification customDialog = new CustomDialogItemNotification(dialog, context, listInfoNotification.get(position));
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
        private TextView txtName, txtContent, txtTimer;
        private ImageView imgIcon;
        private LinearLayout llContainer;

        public CustomViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_notification_txt_name);
            txtContent = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_notification_txt_content);
            txtTimer = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_notification_txt_timer);
            imgIcon = (ImageView) itemView.findViewById(R.id.custom_adapter_recyclerview_notification_img_icon);
            llContainer = (LinearLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_notification_ll_container);
        }
    }

}
