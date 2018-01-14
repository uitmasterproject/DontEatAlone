package com.app.donteatalone.views.main.notification;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.CustomSocket;
import com.app.donteatalone.model.InfoInvitation;
import com.app.donteatalone.model.InfoNotification;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.require.main_require.on_require.ProfileAccordantUser;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.donteatalone.views.main.blog.DetailBlogActivity.ARG_PHONE_NUMBER;
import static com.app.donteatalone.views.main.require.main_require.OnRequireFragment.socketIO;

/**
 * Created by ChomChom on 13-Jun-17
 */

public class CustomDialogItemNotification {
    private Dialog dialog;
    private Context context;
    private InfoNotification data;
    private Boolean control = true;

    public CustomDialogItemNotification(Dialog dialog, Context context, InfoNotification data) {
        this.dialog = dialog;
        this.context = context;
        this.data = data;
    }

    public void setDefaultValue() throws ParseException {
        TextView txtTitle = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_title);
        if (!data.getResultInvitation().equals("nothing")) {
            if (data.getResultInvitation().equals("accept"))
                txtTitle.setText(R.string.accept);
            else txtTitle.setText(R.string.refuse);
        } else txtTitle.setText(R.string.notification);



        TextView txtFullName = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_name);
        txtFullName.setText(StringEscapeUtils.unescapeJava(data.getParticipant().getFullName()));

        TextView txtPhone = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_phone);
        txtPhone.setText(data.getParticipant().getAccordantUser());

        TextView txtTime = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_time);
        txtTime.setText(data.getTimeInvite());

        TextView txtPlace = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_place);
        txtPlace.setText(StringEscapeUtils.unescapeJava(data.getRestaurantInfo().getAddress()));

        ImageView imgClose = (ImageView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_img_close);
        setClickClose(imgClose);

        LinearLayout llContainerOk = (LinearLayout) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_ll_container_ok);
        LinearLayout llContainer = (LinearLayout) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_ll_container);
        if (data.getResultInvitation().equals("accept") || data.getResultInvitation().equals("refuse")) {
            llContainerOk.setVisibility(View.VISIBLE);
            llContainer.setVisibility(View.GONE);
            setClickInRelative();
        } else if (data.getResultInvitation().equals("nothing")) {
            llContainerOk.setVisibility(View.GONE);
            llContainer.setVisibility(View.VISIBLE);
            setClickInLinner();
        }
    }

    private void setClickInRelative() {
        Button btnOK = (Button) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateData();
                dialog.cancel();
            }
        });
    }

    private void setClickInLinner() throws ParseException {
        Button btnViewProfile = (Button) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_btn_viewprofile);
        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileAccordantUser.class);
                intent.putExtra(ARG_PHONE_NUMBER, data.getParticipant().getAccordantUser());
                context.startActivity(intent);
            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        int time = (int) ((df.parse(formattedDate).getTime() - df.parse(data.getCurrentTime()).getTime()) / 60000);
        Button btnAccept = (Button) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_btn_accept);
        Button btnRefuse = (Button) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_btn_refuse);
        TextView txtResponse = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_result_invite_txt_suggest);
        txtResponse.setVisibility(View.GONE);
        if (time > 5) {
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
        if (socketIO == null) {
            CustomSocket socket = new CustomSocket();
            socketIO = socket.getmSocket();
            socketIO.connect();
            setEventSendResult(result, false);
        } else {
            setEventSendResult(result, true);
        }
    }

    private void setEventSendResult(String result, final Boolean status) {
        data.setResultInvitation(result);
        InfoInvitation infoInvitation = new InfoInvitation(context, data);

        Gson gson = new Gson();
        String json = gson.toJson(infoInvitation);
        socketIO.emit("responseInvitation", json);

        socketIO.on("userInviteOff", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            if (control) {

                                Gson gson = new Gson();
                                InfoInvitation infoInvitation = gson.fromJson(data.getString("userOff"), InfoInvitation.class);
                                listenInviterOffline(infoInvitation, status);
                                control = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void listenInviterOffline(InfoInvitation infoInvitation, final Boolean status) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_require_on_inviter_offline);
        TextView txtName, txtNameRepeat, txtPhone;
        Button btnOk;
        ImageView imgClose;
        txtName = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_txt_name);
        txtName.setText(infoInvitation.getOwnInvitation().getFullName());
        txtNameRepeat = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_txt_repeat_name);
        txtNameRepeat.setText(infoInvitation.getOwnInvitation().getFullName());
        txtPhone = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_txt_phone);
        txtPhone.setText(infoInvitation.getOwnInvitation().getAccordantUser());
        btnOk = (Button) dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status) {
                    socketIO.disconnect();
                    socketIO = null;
                }
                dialog.cancel();
            }
        });
        imgClose = (ImageView) dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status) {
                    socketIO.disconnect();
                    socketIO = null;
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void setClickClose(ImageView imgClose) {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getResultInvitation().equals("accept") || data.getResultInvitation().equals("refuse"))
                    updateData();
                dialog.cancel();
            }
        });
    }

    private void updateData() {
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
}
