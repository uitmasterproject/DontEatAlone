package com.app.donteatalone.views.main.require.main_require.on_require;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.InfoInvitation;
import com.app.donteatalone.views.main.MainActivity;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import static com.app.donteatalone.views.main.blog.DetailBlogActivity.ARG_PHONE_NUMBER;

/**
 * Created by ChomChom on 05-Jun-17
 */

public class CustomDialogInfoAccordantUser {
    private Dialog dialog;
    private Context context;
    private InfoInvitation infoInvitation;
    private Socket socketIO;

    public CustomDialogInfoAccordantUser(Dialog dialog, Context context, InfoInvitation infoInvitation, Socket socketIO) {
        this.dialog = dialog;
        this.context = context;
        this.infoInvitation = infoInvitation;
        this.socketIO = socketIO;
    }

    public void setDefaultValue() throws JSONException {
        ImageView imgClose = (ImageView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_img_close);
        ImageView imgAvatar = (ImageView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_img_avatar);

        TextView txtAge = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_age);

        ImageView imgGender = (ImageView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_img_gender);

        TextView txtFullName = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_fullname);
        TextView txtPhone = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_phone);

        TextView txtCharacter = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_character);
        TextView txtStyle = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_style);
        TextView txtFood = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_food);

        TextView txtDate = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_date);

        TextView txtRestaurant = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_name_restaurant);
        TextView txtAddress = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_txt_address);

        Button btnViewProfile = (Button) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_btn_viewprofile);
        Button btnAccept = (Button) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_btn_accept);
        Button btnRefuse = (Button) dialog.findViewById(R.id.custom_dialog_require_on_info_account_user_btn_refuse);

        setValueAvatar(imgAvatar);

        setValueAge(txtAge);

        setValueGender(imgGender);

        setValueFullName(txtFullName);

        setValuePhone(txtPhone);

        setValueCharacter(txtCharacter, txtStyle, txtFood);

        setValueTime(txtDate);

        setValueRestaurant(txtRestaurant, txtAddress);

        setClickRestaurant(txtRestaurant);

        setClickClose(imgClose);

        setClickViewProfile(btnViewProfile);

        setClickAccept(btnAccept);

        setClickRefuse(btnRefuse);
    }

    private void setValueAvatar(ImageView imgAvatar) {
        Picasso.with(context)
                .load(infoInvitation.getOwnInvitation().getAvatar())
                .error(R.drawable.avatar)
                .into(imgAvatar);
    }

    private void setValueAge(TextView txtAge) {
        txtAge.setText(infoInvitation.getOwnInvitation().getAge() + "");
    }

    private void setValueGender(ImageView imgGender) {
        if (infoInvitation.getOwnInvitation().getGender().equals("Nam")) {
            imgGender.setImageResource(R.drawable.ic_male);
        } else {
            imgGender.setImageResource(R.drawable.ic_female);
        }
    }

    private void setValueFullName(TextView txtName) {
        txtName.setText(StringUtils.capitalize(StringEscapeUtils.unescapeJava(
                infoInvitation.getOwnInvitation().getFullName())));
    }

    private void setValuePhone(TextView txtPhone) {
        txtPhone.setText(infoInvitation.getOwnInvitation().getAccordantUser());
    }

    private void setValueCharacter(TextView txtCharacter, TextView txtStyle, TextView txtFood) throws JSONException {
        txtCharacter.setText(StringUtils.capitalize(StringEscapeUtils.unescapeJava(
                infoInvitation.getOwnInvitation().getMyCharacter())));
        txtStyle.setText(StringUtils.capitalize(StringEscapeUtils.unescapeJava(
                infoInvitation.getOwnInvitation().getMyStyle())));
        txtFood.setText(StringUtils.capitalize(StringEscapeUtils.unescapeJava(
                infoInvitation.getOwnInvitation().getTargetFood())));
    }

    private void setValueTime(TextView txtDate) {
        txtDate.setText(infoInvitation.getTimeInvite());
    }

    private void setValueRestaurant(TextView txtRestaurant, TextView txtPlace) {
        SpannableString content = new SpannableString(StringUtils.capitalize(StringEscapeUtils.unescapeJava(
                infoInvitation.getRestaurantInfo().getName())));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtRestaurant.setText(content);

        txtPlace.setText(StringUtils.capitalize(StringEscapeUtils.unescapeJava(
                infoInvitation.getRestaurantInfo().getAddress())));
    }

    private void setClickRestaurant(TextView txtRestaurant){
        txtRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_dialog_restaurant_no_reservation);

                ImageView imgAvatar;
                TextView txtName, txtAddress, txtOpenDay, txtRate, txtPrice, txtReserve;
                Button btnDone;

                imgAvatar = (ImageView) dialog.findViewById(R.id.custom_dialog_restaurant_no_reservation_ib_image);
                txtName = (TextView) dialog.findViewById(R.id.custom_dialog_restaurant_no_reservation_name);
                txtAddress = (TextView) dialog.findViewById(R.id.custom_dialog_restaurant_no_reservation_tv_address);
                txtOpenDay = (TextView) dialog.findViewById(R.id.custom_dialog_restaurant_no_reservation_tv_open_time);
                txtRate = (TextView) dialog.findViewById(R.id.custom_dialog_restaurant_no_reservation_ib_rate);
                txtPrice = (TextView) dialog.findViewById(R.id.custom_dialog_restaurant_no_reservation_tv_price);
                txtReserve = (TextView) dialog.findViewById(R.id.custom_dialog_restaurant_no_reservation_tv_table);
                btnDone = (Button) dialog.findViewById(R.id.custom_dialog_restaurant_no_reservation_ib_close);

                Picasso.with(context)
                        .load(infoInvitation.getRestaurantInfo().getAvatar())
                        .error(R.drawable.temp)
                        .into(imgAvatar);

                txtName.setText(StringEscapeUtils.unescapeJava(infoInvitation.getRestaurantInfo().getName()));
                txtAddress.setText(StringEscapeUtils.unescapeJava(infoInvitation.getRestaurantInfo().getAddress()));
                txtRate.setText(infoInvitation.getRestaurantInfo().getRate());
                if (TextUtils.isEmpty(infoInvitation.getRestaurantInfo().getPrice())) {
                    txtPrice.setText("20.000 - 10.000");
                } else {
                    txtPrice.setText(infoInvitation.getRestaurantInfo().getPrice());
                }

                if(infoInvitation.getReservationDetail()==null) {
                    if (TextUtils.isEmpty(infoInvitation.getRestaurantInfo().getOpenDay())) {
                        txtOpenDay.setText("10:00 - 22:00");
                    } else {
                        txtOpenDay.setText(infoInvitation.getRestaurantInfo().getOpenDay());
                    }
                }else {
                    txtOpenDay.setText(infoInvitation.getReservationDetail().getTime());
                    txtReserve.setText(context.getString(R.string.order_table)+" "+infoInvitation.getReservationDetail().getTable());
                }

                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }

    private void setClickClose(ImageView imgClose) {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                infoInvitation.setResultInvitation("nothing");
                String json = gson.toJson(infoInvitation);

                socketIO.emit("responseInvitation", json);
                dialog.dismiss();

                Intent intent = new Intent(MainActivity.BROADCAST_NAME);
                intent.putExtra(MainActivity.SEND_BROADCAST_DATA, true);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }

    private void setClickViewProfile(Button btnViewProfile) {
        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                infoInvitation.setResultInvitation("nothing");
                String json = gson.toJson(infoInvitation);

                socketIO.emit("responseInvitation", json);
                dialog.dismiss();

                Intent intent=new Intent(context, ProfileAccordantUser.class);
                intent.putExtra(ARG_PHONE_NUMBER,infoInvitation.getOwnInvitation().getAccordantUser());
                context.startActivity(intent);
            }
        });
    }

    private void setClickAccept(Button btnAccept) {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                infoInvitation.setResultInvitation("accept");
                String json = gson.toJson(infoInvitation);

                socketIO.emit("responseInvitation", json);
                dialog.dismiss();
            }
        });
    }

    private void setClickRefuse(Button btnRefuse) {
        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                infoInvitation.setResultInvitation("refuse");
                String json = gson.toJson(infoInvitation);

                socketIO.emit("responseInvitation", json);
                dialog.dismiss();
            }
        });
    }
}

//    public void setNotification() {
//        final ArrayList<InfoNotification> listInfoNotification = new ArrayList<>();
//        Call<ArrayList<InfoNotification>> getInfoNotification = Connect.getRetrofit().getNotification(mySharePreference.getPhoneLogin());
//        getInfoNotification.enqueue(new Callback<ArrayList<InfoNotification>>() {
//            @Override
//            public void onResponse(Call<ArrayList<InfoNotification>> call, Response<ArrayList<InfoNotification>> response) {
//                for (InfoNotification element : response.body()) {
//                    InfoNotification info = new InfoNotification(element.getUserSend(), element.getNameSend(), element.getTimeSend(),
//                            element.getDate(), element.getTime(), element.getPlace(), element.getStatus(), element.getRead(), element.getSeen());
//                    listInfoNotification.add(info);
//                }
//                Collections.reverse(listInfoNotification);
//
//                Intent intent=new Intent(MainActivity.BROADCAST_NAME);
//                intent.putExtra(MainActivity.SEND_BROADCAST_DATA,listInfoNotification);
//                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<InfoNotification>> call, Throwable t) {
//                Log.e("listInfoNotification2", t.toString() + "");
//            }
//        });
//    }

