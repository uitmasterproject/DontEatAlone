package com.app.donteatalone.views.main.notification;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.CustomSocket;
import com.app.donteatalone.model.InfoNotification;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.views.main.require.main_require.on_require.ProfileAccordantUser;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.app.donteatalone.views.main.require.main_require.OnRequireFragment.socketIO;
import static com.google.android.gms.internal.zzir.runOnUiThread;

/**
 * Created by ChomChom on 13-Jun-17.
 */

public class CustomDialogItemNotification {
    private Dialog dialog;
    private Context context;
    private InfoNotification data;
    private String phone;
    private Boolean control=true;
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
                Intent intent=new Intent(context, ProfileAccordantUser.class);
                intent.putExtra("PhoneAccordantUser",data.getUserSend());
                context.startActivity(intent);
            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        int time=(int)((df.parse(formattedDate).getTime()-df.parse(data.getTimeSend()).getTime())/60000);
        Button btnAccept=(Button) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_btn_accept);
        Button btnRefuse=(Button) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_btn_refuse);
        TextView txtResponse=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_suggest);
        txtResponse.setVisibility(View.GONE);
        if(time > 5){
            txtResponse.setVisibility(View.VISIBLE);
            txtResponse.setText(context.getResources().getString(R.string.overTimeRespone));
            btnAccept.setEnabled(false);
            btnAccept.setBackgroundResource(R.drawable.bt_rectangle_ripple_5_radius);
            btnRefuse.setEnabled(true);
            btnRefuse.setBackgroundResource(R.drawable.bt_rectangle_ripple_5_radius);
        }
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    clickbtnResponseInvatition("accept");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.cancel();
            }
        });

        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    clickbtnResponseInvatition("refuse");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.cancel();
            }
        });
    }

    private void clickbtnResponseInvatition(String result) throws URISyntaxException, JSONException {
        if(socketIO==null){
            CustomSocket socket=new CustomSocket();
            socketIO=socket.getmSocket();
            socketIO.connect();
            setEventSendResult(result,false);
        }
        else {
            //Toast.makeText(context,socketIO+"",Toast.LENGTH_SHORT).show();
            setEventSendResult(result, true);
        }
    }

    private void setEventSendResult(String result, Boolean status) throws JSONException {
        Log.e("send","send");
        socketIO.emit("reponseInvitation",sendData(result));
        socketIO.on("userInviteOff", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data=(JSONObject) args[0];
                        Log.e("user offline", args[0].toString());
                        try {
                            if(control==true) {
                                listenInviterOffline(data.getString("nameReceiver"), data.getString("phoneReceiver"), status);
                                control=false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void listenInviterOffline(String name,String phone, Boolean status){
        Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_require_on_inviter_offline);
        TextView txtName, txtNameRepeat, txtPhone;
        Button btnOk;
        ImageView imgClose;
        txtName=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_txt_name);
        txtName.setText(name);
        txtNameRepeat=(TextView)dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_txt_repeat_name);
        txtNameRepeat.setText(name);
        txtPhone=(TextView)dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_txt_phone);
        txtPhone.setText(phone);
        btnOk=(Button)dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status==false) {
                    socketIO.disconnect();
                    socketIO = null;
                }
                dialog.cancel();
            }
        });
        imgClose=(ImageView)dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status==false) {
                    socketIO.disconnect();
                    socketIO = null;
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private String sendData( String result) throws JSONException {
        String temp=data.getUserSend()+"|"+
                data.getNameSend()+"|"+
                phone+"|"+
                getInfointoSharedPreferences("fullNameLogin")+"|"+
                data.getTimeSend()+"|"+
                data.getDate()+"|"+
                data.getTime()+"|"+
                data.getPlace()+"|"+
                result;
        return temp;
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

    private String getInfointoSharedPreferences(String str) {
        SharedPreferences pre = context.getSharedPreferences("account", MODE_PRIVATE);
        String data = pre.getString(str, "");
        return data;
    }
}
