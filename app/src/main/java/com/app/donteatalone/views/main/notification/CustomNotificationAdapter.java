package com.app.donteatalone.views.main.notification;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoNotification;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.utils.MySharePreference;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Picasso.with(context)
                .load(listInfoNotification.get(position).getParticipant().getAvatar())
                .error(R.drawable.avatar)
                .into(holder.imgAvatar);

        if (listInfoNotification.get(position).getResultInvitation().equals("accept")) {
            holder.txtContent.setText(setMultiColorText(listInfoNotification.get(position).getParticipant().getFullName(),
                    context.getString(R.string.accept_content, listInfoNotification.get(position).getParticipant().getAccordantUser())));
            holder.imgIcon.setImageResource(R.drawable.ic_accepted);
            holder.txtTimer.setText(listInfoNotification.get(position).getCurrentTime());
            if (listInfoNotification.get(position).getRead().equals("0"))
                holder.llContainer.setBackgroundColor(Color.CYAN);
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.llContainer.setBackgroundColor(Color.WHITE);
                    updateData(listInfoNotification.get(position));
                    showDialog(position);
                }
            });
        } else if (listInfoNotification.get(position).getResultInvitation().equals("refuse")) {
            holder.txtContent.setText(setMultiColorText(listInfoNotification.get(position).getParticipant().getFullName(),
                    context.getString(R.string.refuse_content)));
            holder.imgIcon.setImageResource(R.drawable.ic_refuse);
            holder.txtTimer.setText(listInfoNotification.get(position).getCurrentTime());
            if (listInfoNotification.get(position).getRead().equals("0"))
                holder.llContainer.setBackgroundColor(Color.CYAN);
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.llContainer.setBackgroundColor(Color.WHITE);
                    updateData(listInfoNotification.get(position));
                    showDialog(position);
                }
            });
        } else {
            holder.txtContent.setText(setMultiColorText(listInfoNotification.get(position).getParticipant().getFullName(),
                    context.getString(R.string.invite_content)));
            holder.imgIcon.setImageResource(R.drawable.ic_invite);
            holder.txtTimer.setText(listInfoNotification.get(position).getCurrentTime());
            holder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(position);
                }
            });
        }
    }

    private Spannable setMultiColorText(String name, String content) {
        Spannable spannable = new SpannableString(StringUtils.capitalize(StringEscapeUtils.unescapeJava(name) + content));

        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_deep_orange)), 0, StringUtils.capitalize(StringEscapeUtils.unescapeJava(name)).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
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

    private void updateData(InfoNotification data) {
        Call<Status> updateReadNotification = Connect.getRetrofit().updateReadNotification(new MySharePreference((Activity) context).getPhoneLogin(),
                data.getParticipant().getAccordantUser(), data.getResultInvitation(), data.getCurrentTime());
        updateReadNotification.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listInfoNotification.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtContent, txtTimer;
        private ImageView imgIcon;
        private ImageView imgAvatar;
        private LinearLayout llContainer;

        public CustomViewHolder(View itemView) {
            super(itemView);
            txtContent = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_notification_txt_content);
            txtTimer = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_notification_txt_timer);
            imgIcon = (ImageView) itemView.findViewById(R.id.custom_adapter_recyclerview_notification_img_icon);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            llContainer = (LinearLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_notification_ll_container);
        }
    }

}
