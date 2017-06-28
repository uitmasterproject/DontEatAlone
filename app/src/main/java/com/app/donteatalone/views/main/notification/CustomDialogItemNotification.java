package com.app.donteatalone.views.main.notification;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoNotification;
import com.app.donteatalone.model.Status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 13-Jun-17.
 */

public class CustomDialogItemNotification {
    private Dialog dialog;
    private Context context;
    private InfoNotification data;
    private String phone;
    public CustomDialogItemNotification(Dialog dialog, Context context, InfoNotification data, String phone) {
        this.dialog=dialog;
        this.context=context;
        this.data=data;
        Log.e("data infoNotification",this.data+"");
        this.phone=phone;
    }

    public void setDefaultValue() throws ParseException {
        TextView txtTitle=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_title);
        if(data.getStatus().equals("nothing")!=true)
            txtTitle.setText(data.getStatus());
        TextView txtFullName=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_name);
        txtFullName.setText(data.getNameSend());
        TextView txtPhone=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_phone);
        txtPhone.setText(data.getUserSend());
        TextView txtDate=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_date);
        txtDate.setText(data.getDate());
        TextView txtTime=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_time);
        txtTime.setText(data.getTime());
        TextView txtPlace=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_place);
        txtPlace.setText(data.getPlace());
        ImageView imgClose=(ImageView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_img_close);
        setClickClose(imgClose);
        RelativeLayout rlContainer=(RelativeLayout) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_rl_container);
        LinearLayout llContainer=(LinearLayout) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_ll_container);
        if(data.getStatus().equals("accept")==true||data.getStatus().equals("refuse")==true){
            rlContainer.setVisibility(View.VISIBLE);
            llContainer.setVisibility(View.GONE);
            setClickinRelative();
        }
        else if(data.getStatus().equals("nothing")==true){
            rlContainer.setVisibility(View.GONE);
            llContainer.setVisibility(View.VISIBLE);
            setClickinLinner();
        }
    }

    private void setClickinRelative(){
        Button btnOK=(Button) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
                dialog.dismiss();
            }
        });
    }

    private void setClickinLinner() throws ParseException {
        Button btnViewProfile=(Button) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_btn_viewprofile);
        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String dateInvite=data.getDate()+"/"+c.get(Calendar.YEAR)+" "+data.getTime()+":00";
        int time=(int)((df.parse(formattedDate).getTime()-df.parse(dateInvite).getTime())/3600000);

        Button btnAccept=(Button) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_btn_accept);
        Button btnRefuse=(Button) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_btn_refuse);
        if(time < 3){
            btnAccept.setEnabled(false);
            btnRefuse.setEnabled(true);
        }
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setClickClose(ImageView imgClose){
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.getStatus().equals("accept")==true||data.getStatus().equals("refuse")==true)
                    updateData();
                dialog.dismiss();
            }
        });
    }

    private void updateData(){
        Connect connect=new Connect();
        Call<Status> updateReadNotification=connect.getRetrofit().updateReadNotification(phone,
                data.getUserSend(),data.getStatus(),data.getTimeSend());
        updateReadNotification.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }
}
