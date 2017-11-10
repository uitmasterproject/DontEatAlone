package com.app.donteatalone.views.main.require.main_require.on_require;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoNotification;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.MainActivity;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.donteatalone.views.main.blog.DetailBlogActivity.ARG_PHONE_NUMBER;

/**
 * Created by ChomChom on 05-Jun-17
 */

public class CustomDialogInfoAccordantUser {
    private Dialog dialog;
    private Context context;
    private JSONObject data;
    private Socket socketIO;
    private String name;
    private MySharePreference mySharePreference;
    private ViewPager viewPager;
    public CustomDialogInfoAccordantUser(Dialog dialog, Context context, JSONObject data, Socket socketIO, String name, ViewPager viewpager) {
        this.dialog=dialog;
        this.context=context;
        mySharePreference=new MySharePreference((Activity)context);
        this.data=data;
        this.socketIO=socketIO;
        this.name=name;
        this.viewPager=viewpager;
    }

    public void setDefaultValue() throws JSONException {
        ImageView imgClose=(ImageView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_img_close);
        ImageView imgAvatar=(ImageView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_img_avatar);
        TextView txtAge=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_age);
        ImageView imgGender=(ImageView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_img_gender);
        TextView txtFullName=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_fullname);
        TextView txtPhone=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_phone);
        TextView txtCharacter=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_character);
        TextView txtDate=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_date);
        TextView txtTime=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_time);
        TextView txtPlace=(TextView)dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_place);
        Button btnViewProfile=(Button) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_btn_viewprofile);
        Button btnAccept=(Button) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_btn_accept);
        Button btnRefuse=(Button) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_btn_refuse);
        setClickClose(imgClose);
        setValueAvatar(imgAvatar);
        setValueAge(txtAge);
        setValueGender(imgGender);
        setValueFullName(txtFullName);
        setValuePhone(txtPhone);
        setValueCharacter(txtCharacter);
        setValueTime(txtTime,txtDate);
        setValuePlace(txtPlace);
        setClickViewProfile(btnViewProfile);
        setClickAccept(btnAccept);
        setClickRefuse(btnRefuse);
    }
    private void setClickClose(ImageView imgClose){
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    socketIO.emit("reponseInvitation", sendData("nothing"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                if(viewPager.getCurrentItem()==1) {
                    setNotification();
                }
            }
        });
    }

    private void setValueAvatar(ImageView imgAvatar) throws JSONException {
        imgAvatar.setImageBitmap(convertStringtoBitmap(data.getJSONObject("invitation").getString("avatar")));
    }

    private void setValueAge(TextView txtAge) throws JSONException {
        txtAge.setText(data.getJSONObject("invitation").getInt("age")+"");
    }

    private void setValueGender(ImageView imgGender) throws JSONException {
        if(data.getJSONObject("invitation").getString("gender").equals("Male")){
            imgGender.setImageResource(R.drawable.ic_male);
        }
        else {
            imgGender.setImageResource(R.drawable.ic_female);
        }
    }

    private void setValueFullName(TextView txtName) throws JSONException {
        txtName.setText(data.getJSONObject("invitation").getString("fullName"));
    }

    private void setValuePhone(TextView txtPhone) throws JSONException {
        txtPhone.setText(data.getJSONObject("invitation").getString("phoneInviter"));
    }

    private void setValueCharacter(TextView txtCharacter) throws JSONException {
        txtCharacter.setText(data.getJSONObject("invitation").getString("character"));
    }

    private void setValueTime(TextView txtTime, TextView txtDate) throws JSONException {
        txtTime.setText(data.getJSONObject("invitation").getString("timer"));
        txtDate.setText(data.getJSONObject("invitation").getString("date"));
    }

    private void setValuePlace(TextView txtPlace) throws JSONException {
        txtPlace.setText(data.getJSONObject("invitation").getString("place"));
    }

    private void setClickViewProfile(Button btnViewProfile){
        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    socketIO.emit("reponseInvitation", sendData("nothing"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                Intent intent=new Intent(context, ProfileAccordantUser.class);
                try {
                    intent.putExtra(ARG_PHONE_NUMBER,data.getJSONObject("invitation").getString("phoneInviter"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                context.startActivity(intent);
            }
        });
    }

    private void setClickAccept(Button btnAccept){
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    socketIO.emit("reponseInvitation", sendData("accept"));
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setClickRefuse(Button btnRefuse) {
        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    socketIO.emit("reponseInvitation", sendData("refuse"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
    }

    private String sendData( String result) throws JSONException {
        String temp=data.getJSONObject("invitation").getString("phoneInviter")+"|"+
                data.getJSONObject("invitation").getString("fullName")+"|"+
                data.getString("phoneInvited")+"|"+
                name+"|"+
                data.getJSONObject("invitation").getString("timeSend")+"|"+
                data.getJSONObject("invitation").getString("date")+"|"+
                data.getJSONObject("invitation").getString("timer")+"|"+
                data.getJSONObject("invitation").getString("place")+"|"+
                result;
        return temp;
    }

    private Bitmap convertStringtoBitmap(String str){
        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.avatar);
        if(!str.equals("")){
            try {
                byte[] encodeByte = Base64.decode(str, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            }
            catch (Exception e){
                Log.e("exception",e.toString());
            }
        }
        return bitmap;
    }

    public void setNotification() {
        final ArrayList<InfoNotification> listInfoNotification = new ArrayList<>();
        Call<ArrayList<InfoNotification>> getInfoNotification = Connect.getRetrofit().getNotification(mySharePreference.getPhoneLogin());
        getInfoNotification.enqueue(new Callback<ArrayList<InfoNotification>>() {
            @Override
            public void onResponse(Call<ArrayList<InfoNotification>> call, Response<ArrayList<InfoNotification>> response) {
                for (InfoNotification element : response.body()) {
                    InfoNotification info = new InfoNotification(element.getUserSend(), element.getNameSend(), element.getTimeSend(),
                            element.getDate(), element.getTime(), element.getPlace(), element.getStatus(), element.getRead(), element.getSeen());
                    listInfoNotification.add(info);
                }
                Collections.reverse(listInfoNotification);

                Intent intent=new Intent(MainActivity.BROADCASTNAME);
                intent.putExtra(MainActivity.SENDBROADCASTDATA,listInfoNotification);
                context.sendBroadcast(intent);

            }

            @Override
            public void onFailure(Call<ArrayList<InfoNotification>> call, Throwable t) {
                Log.e("listInfoNotification2", t.toString() + "");
            }
        });
    }

}
