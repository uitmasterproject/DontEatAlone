package com.app.donteatalone.views.main.require.main_require;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.app.donteatalone.R;
import com.app.donteatalone.base.OnRecyclerItemClickListener;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.AccordantUser;
import com.app.donteatalone.model.AccordantUserUpdate;
import com.app.donteatalone.model.CustomSocket;
import com.app.donteatalone.model.InfoInvitation;
import com.app.donteatalone.model.Restaurant;
import com.app.donteatalone.model.RestaurantDetail;
import com.app.donteatalone.model.SocketInfoUser;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.MainActivity;
import com.app.donteatalone.views.main.require.main_require.on_require.AccordantUserAdapter;
import com.app.donteatalone.views.main.require.main_require.on_require.CustomDialogInfoAccordantUser;
import com.app.donteatalone.views.main.restaurant.AllowReservation.ReservationAdapter;
import com.app.donteatalone.views.main.restaurant.NoReservation.NoReservationAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by ChomChom on 5/8/2017
 */

public class OnRequireFragment extends Fragment {

    public static Socket socketIO;
    private long limitTime = 60 * 60 * 1000;
    private long limitTimeReservation = 30 * 60 * 1000;
    private View viewGroup;
    private String phone;
    private RecyclerView rcvListAccordantUser;
    private AccordantUserAdapter accordantUserAdapter;
    private ArrayList<AccordantUser> listAccordantUser;
    private ViewPager viewPager;
    private MySharePreference infoRequireSharePreference;

    private long countReverse = 0;

    private SwipeRefreshLayout srlResultsList;

    public static OnRequireFragment newInstance(ViewPager viewPager) {
        OnRequireFragment fragment = new OnRequireFragment();
        fragment.setViewPager(viewPager);
        return fragment;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_require_on, container, false);
        phone = new MySharePreference(getActivity()).getPhoneLogin();

        init();

        if (socketIO == null) {
            try {
                initSocket();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        socketIO.emit("UserOnline", getInforRequire());

        listenComfortableUserOnline();

        accordantUserAdapter.setOnClickItemRecyclerView(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int resource) {
                Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_require_on_invite_setting_time);
                initDialog(dialog, resource);
                dialog.show();
            }
        });

        return viewGroup;
    }

    @Override
    public void onStart() {
        super.onStart();

        listenUserLikeUpdate();

        listenInvitation();
        listenResultInvitation();
        listenUserOff();
        swipeRefreshLayout();
    }

    private void init() {
        infoRequireSharePreference = new MySharePreference(getActivity(), phone);

        listAccordantUser = new ArrayList<AccordantUser>();

        rcvListAccordantUser = (RecyclerView) viewGroup.findViewById(R.id.fragment_require_on_rcv_list_accordant_user);
        LinearLayoutManager llManager = new LinearLayoutManager(getContext());
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvListAccordantUser.setLayoutManager(llManager);

        accordantUserAdapter = new AccordantUserAdapter(listAccordantUser, getActivity());
        rcvListAccordantUser.setAdapter(accordantUserAdapter);

        srlResultsList = (SwipeRefreshLayout) viewGroup.findViewById(R.id.fragment_require_on_srl_results);
    }

    private void initSocket() throws URISyntaxException {
        socketIO = new CustomSocket().getmSocket();
        socketIO.connect();
    }

    private void swipeRefreshLayout() {
        srlResultsList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listAccordantUser.clear();
                accordantUserAdapter.notifyDataSetChanged();
                socketIO.emit("UserOnline", getInforRequire());

                // Stop refresh animation
                srlResultsList.setRefreshing(false);
            }
        });
    }

    private void listenComfortableUserOnline() {
        socketIO.on("userLike", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject data = (JSONObject) args[0];
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<AccordantUser>>() {
                                }.getType();
                                ArrayList<AccordantUser> list = gson.fromJson(data.getString("listUserLike"), type);

                                if(countReverse<1000) {
                                    listAccordantUser.addAll(list);
                                }else {
                                    for(int i=0;i<list.size();i++){
                                        list.get(i).setControl(false);
                                        listAccordantUser.add(list.get(i));
                                    }
                                }

                                accordantUserAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                    });
                }
            }
        });
    }

    private void listenUserLikeUpdate() {
        socketIO.on("updateUserLike", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject data = (JSONObject) args[0];
                            try {
                                Gson gson = new Gson();
                                AccordantUserUpdate userUpdate = gson.fromJson(data.getString("userLikeUpdate"), AccordantUserUpdate.class);

                                if (userUpdate != null) {

                                    if (!TextUtils.isEmpty(userUpdate.getListUserUpdate()) && userUpdate.getListUserUpdate().contains(",")) {
                                        String[] phoneUpdate = userUpdate.getListUserUpdate().split(",");
                                        for (int i = 0; i < phoneUpdate.length; i++) {
                                            if (phone.equals(phoneUpdate[i])) {

                                                boolean check = false;
                                                for (int j = 0; j < listAccordantUser.size(); i++) {
                                                    if (listAccordantUser.get(i).getAccordantUser().equals(userUpdate.getAccordantUser())) {
                                                        check = true;
                                                        break;
                                                    }
                                                }

                                                if (!check) {
                                                    String percent = "10";
                                                    if (!TextUtils.isEmpty(userUpdate.getPercent()) && userUpdate.getListUserUpdate().contains(",")) {
                                                        String[] percentUpdate = userUpdate.getPercent().split(",");
                                                        percent = percentUpdate[i];
                                                    }
                                                    userUpdate.setPercent(percent);
                                                    AccordantUser accordantUser = new AccordantUser(userUpdate);

                                                    if(countReverse<1000) {
                                                        listAccordantUser.add(accordantUser);
                                                    }else {
                                                        accordantUser.setControl(false);
                                                        listAccordantUser.add(accordantUser);
                                                    }

                                                    accordantUserAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                    });
                }
            }
        });
    }

    private void listenInvitation() {
        socketIO.on("sendInvite", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject data = (JSONObject) args[0];
                            try {
                                Gson gson = new Gson();
                                InfoInvitation infoInvitation = gson.fromJson(data.getString("invitation"), InfoInvitation.class);

                                if(phone.equals(infoInvitation.getParticipant().getAccordantUser())) {
                                    Dialog dialog = new Dialog(getActivity());
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.custom_dialog_require_on_info_accordant_user);
                                    CustomDialogInfoAccordantUser customDialog = new CustomDialogInfoAccordantUser(dialog, getContext(), infoInvitation, socketIO);
                                    customDialog.setDefaultValue();
                                    dialog.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private void listenResultInvitation() {
        socketIO.on("resultInvitation", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject data = (JSONObject) args[0];
                                Gson gson = new Gson();
                                InfoInvitation infoInvitation = gson.fromJson(data.getString("result"), InfoInvitation.class);

                                if (infoInvitation.getOwnInvitation().getAccordantUser().equals(phone)) {
                                    //update title + 1 in notification

                                    Intent intent = new Intent(MainActivity.BROADCAST_NAME);
                                    intent.putExtra(MainActivity.SEND_BROADCAST_TITLE, 1);
                                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }


    private void listenUserOff() {
        socketIO.on("userParticipantOff", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];

                        Gson gson = new Gson();
                        try {
                            InfoInvitation infoInvitation = gson.fromJson(data.getString("userOff"), InfoInvitation.class);

                            listenInviterOffline(infoInvitation);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    private void listenInviterOffline(InfoInvitation infoInvitation) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_require_on_inviter_offline);
        TextView txtName, txtNameRepeat, txtPhone;
        Button btnOk;
        ImageView imgClose;
        txtName = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_txt_name);
        txtName.setText(infoInvitation.getParticipant().getFullName());
        txtNameRepeat = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_txt_repeat_name);
        txtNameRepeat.setText(infoInvitation.getParticipant().getFullName());
        txtPhone = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_txt_phone);
        txtPhone.setText(infoInvitation.getParticipant().getAccordantUser());
        btnOk = (Button) dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        imgClose = (ImageView) dialog.findViewById(R.id.custom_dialog_require_on_inviter_offline_img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private String getInforRequire() {

        SocketInfoUser socketInfoUser = new SocketInfoUser(phone, setDefaultValue(infoRequireSharePreference.getGenderRequire(), "all"),
                setDefaultValue(infoRequireSharePreference.getAgeMinRequire(), "18"),
                setDefaultValue(infoRequireSharePreference.getAgeMaxRequire(), "25"),
                setDefaultValue(infoRequireSharePreference.getAddressRequire(), StringEscapeUtils.escapeJava(getString(R.string.default_address))),
                setDefaultValue(infoRequireSharePreference.getLatLngAddressRequire(), getString(R.string.default_lat_lng)),
                setDefaultValue(infoRequireSharePreference.getTargetFoodRequire(), ""),
                setDefaultValue(infoRequireSharePreference.getTargetCharacterRequire(), ""),
                setDefaultValue(infoRequireSharePreference.getTargetStyleRequire(), ""));

        Gson gson = new Gson();
        String json = gson.toJson(socketInfoUser);

        return json;
    }

    @Override
    public void onStop() {
        Log.e("activity onStop", getActivity() + "++++++++++++++++++++++++++++++++++++++++");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        listAccordantUser.clear();
        socketIO.disconnect();
        socketIO = null;
        Log.e("activity onDestroy", getActivity() + "++++++++++++++++++++++++++++++++++++++++");
        super.onDestroy();
    }


    private String setDefaultValue(String value, String defaultValue) {
        if (TextUtils.isEmpty(value))
            return defaultValue;
        else
            return value;
    }


    private void initDialog(final Dialog dialog, final int position) {
        final InfoInvitation infoInvitation = new InfoInvitation(new MySharePreference(getActivity()).createInfoUser(), listAccordantUser.get(position));

        TextView txtTitle;

        final TextView txtDate, txtTimer, txtAddress;

        Button btnInvite;

        final LinearLayout llContainerSetTime;
        final LinearLayout llContainerTime;

        final WheelPicker wpDate, wpMonth, wpHour, wpMinute;

        final RadioButton rbNoReservation, rbReservation;

        final ProgressBar progressBar;

        final RecyclerView rcvRestaurant;

        final TextView txtEmptyRestaurant;

        final ArrayList<Restaurant> listRestaurant = new ArrayList<>();
        final ArrayList<RestaurantDetail> listRestaurantDetail = new ArrayList<>();

        txtTitle = (TextView) dialog.findViewById(R.id.txt_title);
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        txtDate = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_invite_txt_date);
        txtTimer = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_invite_txt_time);
        txtAddress = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_invite_txt_address);

        btnInvite = (Button) dialog.findViewById(R.id.custom_dialog_require_on_invite_btn_invite);

        llContainerTime = (LinearLayout) dialog.findViewById(R.id.custom_dialog_require_on_invite_ll_container_time);
        llContainerSetTime = (LinearLayout) dialog.findViewById(R.id.custom_dialog_require_on_invite_ll_container_set_time);

        wpDate = (WheelPicker) dialog.findViewById(R.id.custom_dialog_require_on_invite_wp_date);
        wpMonth = (WheelPicker) dialog.findViewById(R.id.custom_dialog_require_on_invite_wp_month);
        wpHour = (WheelPicker) dialog.findViewById(R.id.custom_dialog_require_on_invite_wp_hour);
        wpMinute = (WheelPicker) dialog.findViewById(R.id.custom_dialog_require_on_invite_wp_minute);

        progressBar = (ProgressBar) dialog.findViewById(R.id.progress);

        rcvRestaurant = (RecyclerView) dialog.findViewById(R.id.custom_dialog_require_on_invite_setting_time_rcv_restaurant);

        rcvRestaurant.setLayoutManager(new LinearLayoutManager(getActivity()));
        final NoReservationAdapter noReservationAdapter = new NoReservationAdapter(listRestaurant, getActivity());

        txtEmptyRestaurant = (TextView) dialog.findViewById(R.id.txt_empty_restaurant);

        rbNoReservation = (RadioButton) dialog.findViewById(R.id.custom_dialog_require_on_invite_setting_time_rb_have_not_restaurant);
        rbReservation = (RadioButton) dialog.findViewById(R.id.custom_dialog_require_on_invite_setting_time_rb_have_restaurant);

        noReservationAdapter.setClickItemRecyclerView(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int resource) {

                txtAddress.setText(listRestaurant.get(resource).getName());

                infoInvitation.setRestaurantInfo(listRestaurant.get(resource));

                rcvRestaurant.setVisibility(View.GONE);

                rbNoReservation.setText(getString(R.string.choose_other_restaurant));
                rbNoReservation.setChecked(false);
            }
        });

        final ReservationAdapter reservationAdapter = new ReservationAdapter(listRestaurantDetail, getActivity(), false);

        reservationAdapter.setOnClickRecyclerView(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int resource) {

                txtAddress.setText(listRestaurantDetail.get(resource).getName());

                txtTimer.setText(listRestaurantDetail.get(resource).getListReservations().get(0).getTime());

                if (llContainerTime.getVisibility() == View.VISIBLE) {

                    wpHour.setSelectedItemPosition(Integer.parseInt(txtTimer.getText().toString().trim().split(":")[0]));

                    wpMinute.setSelectedItemPosition(((Integer.parseInt(txtTimer.getText().toString().trim().split(":")[1]) / 10) + 1));
                }

                infoInvitation.setRestaurantInfo(new Restaurant(listRestaurantDetail.get(resource)));
                infoInvitation.setReservationDetail(listRestaurantDetail.get(resource).getListReservations().get(0));

                rcvRestaurant.setVisibility(View.GONE);

                rbReservation.setChecked(false);
            }
        });

        setValueTime(txtTimer, txtDate);

        rbNoReservation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rcvRestaurant.setVisibility(View.VISIBLE);
                    rcvRestaurant.setAdapter(noReservationAdapter);
                    setValuePlaceNoReservation(position, progressBar, listRestaurant, noReservationAdapter, rcvRestaurant,txtEmptyRestaurant);

                }
            }
        });

        rbReservation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rcvRestaurant.setVisibility(View.VISIBLE);
                    rcvRestaurant.setAdapter(reservationAdapter);
                    setValuePlaceReservation(txtDate.getText().toString(), progressBar, listRestaurantDetail, reservationAdapter,
                            rcvRestaurant, txtEmptyRestaurant);

                }
            }
        });


        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInvite(rbReservation, txtDate.getText().toString(), txtTimer.getText().toString(), txtAddress.getText().toString(),
                        infoInvitation, dialog);
            }
        });

        llContainerTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llContainerSetTime.getVisibility() == View.VISIBLE)
                    llContainerSetTime.setVisibility(View.GONE);
                else {
                    llContainerSetTime.setVisibility(View.VISIBLE);
                    setOnClickContainerTime(wpDate, wpMonth, wpHour, wpMinute, txtDate, txtTimer,progressBar, listRestaurantDetail,
                            reservationAdapter, rcvRestaurant, txtEmptyRestaurant, rbReservation);
                }
            }
        });

    }

    private void clickInvite(RadioButton rbReservation, String date, String time, String restaurant,
                             InfoInvitation infoInvitation, Dialog dialog) {

        if (!TextUtils.isEmpty(restaurant) && !restaurant.equals(getString(R.string.choose_address))) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            String currentTime = date + "/" + calendar.get(Calendar.YEAR) + " " + time + ":00";
            Date currentDate = null;
            try {
                currentDate = format.parse(currentTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (rbReservation.isChecked()) {
                SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                Date formatTimeReservation = null;
                Date formatTimer = null;
                try {
                    formatTimer = formatTime.parse(time);
                    formatTimeReservation = formatTime.parse(infoInvitation.getReservationDetail().getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (formatTimer != null && formatTimeReservation != null) {
                    if (formatTimer.after(formatTimeReservation) && (formatTimer.getTime() - formatTimeReservation.getTime()) > limitTimeReservation) {
                        Toast.makeText(getActivity(), getString(R.string.limit_time_reservation), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            if (currentDate != null && currentDate.after(calendar.getTime()) &&
                    (currentDate.getTime() - calendar.getTime().getTime()) > limitTime) {

                    infoInvitation.setTimeInvite(date + " " + time);
                    infoInvitation.setCurrentTime(format.format(calendar.getTime()));

                    Gson gson = new Gson();
                    String json = gson.toJson(infoInvitation);

                    socketIO.emit("invite", json);

                    for (AccordantUser temp : listAccordantUser) {
                        temp.setControl(false);
                    }
                    accordantUserAdapter.notifyDataSetChanged();
                    countDown5Minute();
                    dialog.cancel();

            } else {
                Toast.makeText(getActivity(), getString(R.string.limit_time), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.choose_restaurant), Toast.LENGTH_SHORT).show();
        }


    }


    private void countDown5Minute() {
        new CountDownTimer(300000, 500) {
            public void onTick(long millisUntilFinished) {
                countReverse =millisUntilFinished;
            }

            public void onFinish() {
                for (AccordantUser temp : listAccordantUser) {
                    temp.setControl(true);
                }
                accordantUserAdapter.notifyDataSetChanged();
            }
        }.start();
    }

    private void setValueTime(TextView txtTime, TextView txtDate) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM,HH:mm", Locale.ENGLISH);
        String currentTime = format.format(calendar.getTime());
        txtTime.setText(currentTime.split(",")[1]);
        txtDate.setText(currentTime.split(",")[0]);
    }

    private void setValuePlaceNoReservation(int position, final ProgressBar progressBar, final ArrayList<Restaurant> listRestaurant,
                                            final NoReservationAdapter adapter, final RecyclerView rcv, final TextView txtEmpty) {
        progressBar.setVisibility(View.VISIBLE);
        MySharePreference requireInfoSharePreference = new MySharePreference(getActivity(), new MySharePreference(getActivity()).getPhoneLogin());

        String latlng = Math.abs((Float.parseFloat(requireInfoSharePreference.getLatLngAddressRequire().split(",")[0].trim()) -
                Float.parseFloat(listAccordantUser.get(position).getLatlng().split(",")[0].trim())) / 2) + "," +
                Math.abs((Float.parseFloat(requireInfoSharePreference.getLatLngAddressRequire().split(",")[1].trim()) -
                        Float.parseFloat(listAccordantUser.get(position).getLatlng().split(",")[1].trim())) / 2);

        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.City)));

        String city = null, district = null;

        for (int i = 0; i < list.size(); i++) {
            if (StringEscapeUtils.unescapeJava(requireInfoSharePreference.getAddressRequire()).contains(list.get(i))) {
                city = list.get(i);
            }
        }

        list.clear();
        list.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.District)));
        for (int i = 0; i < list.size(); i++) {
            if (StringEscapeUtils.unescapeJava(requireInfoSharePreference.getAddressRequire()).contains(list.get(i))) {
                district = list.get(i);
            }
        }

        if (AppUtils.isNetworkAvailable(getActivity())) {
            if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
                Call<ArrayList<Restaurant>> getListRestaurant = Connect.getRetrofit().getRestaurant(city, district, latlng);
                getListRestaurant.enqueue(new Callback<ArrayList<Restaurant>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.body() != null && response.body().size() > 0) {

                            if(rcv.getVisibility()==View.GONE) {
                                rcv.setVisibility(View.VISIBLE);
                            }
                            txtEmpty.setVisibility(View.GONE);

                            listRestaurant.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        } else {
                            rcv.setVisibility(View.GONE);
                            txtEmpty.setVisibility(View.VISIBLE);
                            txtEmpty.setText(getActivity().getString(R.string.empty_no_restaurant));
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                rcv.setVisibility(View.GONE);
                txtEmpty.setVisibility(View.VISIBLE);
                txtEmpty.setText(getActivity().getString(R.string.empty_no_restaurant));
            }
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), getActivity().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void setValuePlaceReservation(String date, final ProgressBar progressBar, final ArrayList<RestaurantDetail> listRestaurant,
                                          final ReservationAdapter adapter, final RecyclerView rcv, final TextView txtEmpty) {
        progressBar.setVisibility(View.VISIBLE);

        MySharePreference mySharePreference = new MySharePreference(getActivity());

        Calendar calendar = Calendar.getInstance();

        date = date.replace("/", ":") + ":" + calendar.get(Calendar.YEAR);

        if (AppUtils.isNetworkAvailable(getActivity())) {
            Call<ArrayList<RestaurantDetail>> getAllReservation = Connect.getRetrofit().getAllReservation(mySharePreference.getPhoneLogin(), date);
            getAllReservation.enqueue(new Callback<ArrayList<RestaurantDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<RestaurantDetail>> call, Response<ArrayList<RestaurantDetail>> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.body() != null && response.body().size() > 0) {
                        if(rcv.getVisibility()==View.GONE) {
                            rcv.setVisibility(View.VISIBLE);
                        }
                        txtEmpty.setVisibility(View.GONE);

                        listRestaurant.clear();
                        listRestaurant.addAll(response.body());
                        Collections.reverse(listRestaurant);

                        adapter.notifyDataSetChanged();
                    } else {
                        rcv.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setText(getActivity().getString(R.string.empty_restaurant));
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<RestaurantDetail>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), getActivity().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void setDefaultValueWP(WheelPicker wp, int source, int position) {
        wp.setData(Arrays.asList(getActivity().getResources().getStringArray(source)));
        wp.setSelectedItemPosition(position);
    }

    private void setOnClickContainerTime(final WheelPicker wpDate, final WheelPicker wpMonth, final WheelPicker wpHour,
                                         final WheelPicker wpMinute, final TextView txtDate, final TextView txtTime, final ProgressBar progress,
                                         final ArrayList<RestaurantDetail> list, final ReservationAdapter reservationAdapter,
                                         final RecyclerView rcvReservation, final TextView txtEmpty, final RadioButton rbReservation) {

        setDefaultValueWP(wpDate, R.array.Day_31, (Integer.parseInt(txtDate.getText().toString().trim().split("/")[0]) - 1));

        setDefaultValueWP(wpMonth, R.array.Month, (Integer.parseInt(txtDate.getText().toString().trim().split("/")[1]) - 1));

        setDefaultValueWP(wpHour, R.array.Hour, Integer.parseInt(txtTime.getText().toString().trim().split(":")[0]));

        setDefaultValueWP(wpMinute, R.array.Minute, ((Integer.parseInt(txtTime.getText().toString().trim().split(":")[1]) / 10) + 1));

        final LocalDate localDate = new LocalDate();
        wpMonth.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                if (wpMonth.getCurrentItemPosition() < 9) {
                    txtDate.setText((wpDate.getCurrentItemPosition() + 1) + "/" + "0" + (wpMonth.getCurrentItemPosition() + 1));
                } else {
                    txtDate.setText((wpDate.getCurrentItemPosition() + 1) + "/" + (wpMonth.getCurrentItemPosition() + 1));
                }
                setDataForWhellPicker(i, wpDate, localDate.getYear());

                if (rbReservation.isChecked()) {
                    setValuePlaceReservation(txtDate.getText().toString(), progress, list, reservationAdapter, rcvReservation, txtEmpty);
                }
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });

        wpDate.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                if (wpDate.getCurrentItemPosition() < 9) {
                    txtDate.setText("0" + (wpDate.getCurrentItemPosition() + 1) + "/" + txtDate.getText().toString().trim().split("/")[1]);
                } else {
                    txtDate.setText((wpDate.getCurrentItemPosition() + 1) + "/" + txtDate.getText().toString().trim().split("/")[1]);
                }

                if (rbReservation.isChecked()) {
                    setValuePlaceReservation(txtDate.getText().toString(), progress, list, reservationAdapter, rcvReservation, txtEmpty);
                }
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });

        wpHour.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                if (wpHour.getCurrentItemPosition() < 10) {
                    txtTime.setText("0" + (wpHour.getCurrentItemPosition()) + ":" + txtTime.getText().toString().trim().split(":")[1]);
                } else {
                    txtTime.setText((wpHour.getCurrentItemPosition()) + ":" + txtTime.getText().toString().trim().split(":")[1]);
                }
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });

        wpMinute.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                if (wpMinute.getCurrentItemPosition() == 0) {
                    txtTime.setText(txtTime.getText().toString().trim().split(":")[0] + ":" + "00");
                } else {
                    txtTime.setText(txtTime.getText().toString().trim().split(":")[0] + ":" + wpMinute.getCurrentItemPosition() * 10);
                }
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });
    }

    private boolean checkLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 != 0) {
                return true;
            }
        }
        if (year % 100 == 0) {
            if (year % 400 == 0) {
                return true;
            }
        }
        return false;
    }

    private void setDataForWhellPicker(int position, WheelPicker wpDate, int year) {
        switch (position) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                wpDate.setData(Arrays.asList(getActivity().getResources().getStringArray(R.array.Day_31)));
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                wpDate.setData(Arrays.asList(getActivity().getResources().getStringArray(R.array.Day_30)));
                break;
            case 1:
                if (checkLeapYear(year)) {
                    wpDate.setData(Arrays.asList(getActivity().getResources().getStringArray(R.array.Day_29)));
                } else {
                    wpDate.setData(Arrays.asList(getActivity().getResources().getStringArray(R.array.Day_28)));
                }
        }
    }
}

