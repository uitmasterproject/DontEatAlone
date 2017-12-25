package com.app.donteatalone.views.main.require.main_require.on_require;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.AccordantUser;
import com.app.donteatalone.model.Restaurant;
import com.app.donteatalone.model.RestaurantDetail;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.restaurant.AllowReservation.ReservationAdapter;
import com.app.donteatalone.views.main.restaurant.NoReservation.NoReservationAdapter;
import com.github.nkzawa.socketio.client.Socket;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.donteatalone.views.main.blog.DetailBlogActivity.ARG_PHONE_NUMBER;

/**
 * Created by ChomChom on 30-May-17
 */

public class AccordantUserAdapter extends RecyclerView.Adapter<AccordantUserAdapter.CustomViewHolder> {
    private ArrayList<AccordantUser> listAccordantUser;
    private Context context;
    private Socket socketIO;

    private ArrayList<Target> listTarget = new ArrayList<>();

    public AccordantUserAdapter(ArrayList<AccordantUser> _listAccordantUser, Context _context, Socket _socketIO) {
        this.listAccordantUser = _listAccordantUser;
        this.context = _context;
        this.socketIO = _socketIO;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_fragment_require_on, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.progressBar.setVisibility(View.GONE);
                if (bitmap != null) {
                    holder.imgAvatar.setImageBitmap(bitmap);
                } else {
                    holder.imgAvatar.setImageResource(R.drawable.avatar);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        listTarget.add(position, target);

        Picasso.with(context)
                .load(listAccordantUser.get(position).getAvatar())
                .into(listTarget.get(position));

        holder.txtName.setText(StringEscapeUtils.unescapeJava(listAccordantUser.get(position).getFullName()));

        holder.txtPercent.setText(listAccordantUser.get(position).getPercent() + " %");

        if (listAccordantUser.get(position).getGender().equals("Male")) {
            holder.imgGender.setImageResource(R.drawable.ic_male);
        } else {
            holder.imgGender.setImageResource(R.drawable.ic_female);
        }

        holder.txtAge.setText(listAccordantUser.get(position).getAge() + "");

        holder.txtAddress.setText(StringEscapeUtils.unescapeJava(listAccordantUser.get(position).getAddress().substring(0,
                listAccordantUser.get(position).getAddress().lastIndexOf(","))));

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileAccordantUser.class);
                intent.putExtra(ARG_PHONE_NUMBER, listAccordantUser.get(position).getAccordantUser());
                context.startActivity(intent);
            }
        });

        if (listAccordantUser.get(position).getControl())
            holder.ibtnInvite.setVisibility(View.VISIBLE);
        else
            holder.ibtnInvite.setVisibility(View.INVISIBLE);
        holder.ibtnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_require_on_invite_setting_time);
                initDialog(dialog, position);
                dialog.show();
            }
        });
    }

    private void initDialog(final Dialog dialog, final int position) {
        final TextView txtDate, txtTimer, txtAddress;
        Button btnInvite;
        final LinearLayout llContainerSetTime;
        LinearLayout llContainerTime;
        RadioButton rbNoReservation, rbReservation;
        RecyclerView rcvNoReservation, rcvReservation;
        TextView txtEmptyRestaurant;

        final ArrayList<Restaurant> listRestaurant = new ArrayList<>();
        final ArrayList<RestaurantDetail> listRestaurantDetail = new ArrayList<>();

        txtDate = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_invite_txt_date);
        txtTimer = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_invite_txt_time);
        txtAddress = (TextView) dialog.findViewById(R.id.custom_dialog_require_on_invite_txt_address);

        btnInvite = (Button) dialog.findViewById(R.id.custom_dialog_require_on_invite_btn_invite);

        llContainerTime = (LinearLayout) dialog.findViewById(R.id.custom_dialog_require_on_invite_ll_container_time);
        llContainerSetTime = (LinearLayout) dialog.findViewById(R.id.custom_dialog_require_on_invite_ll_container_set_time);

        rcvNoReservation = (RecyclerView) dialog.findViewById(R.id.custom_dialog_require_on_invite_setting_time_rcv_place_have_not_restaurant);
        rcvReservation = (RecyclerView) dialog.findViewById(R.id.custom_dialog_require_on_invite_setting_time_rcv_place_have_restaurant);

        rcvNoReservation.setLayoutManager(new LinearLayoutManager(context));
        NoReservationAdapter noReservationAdapter = new NoReservationAdapter(listRestaurant, context);
        rcvNoReservation.setAdapter(noReservationAdapter);

        rcvReservation.setLayoutManager(new LinearLayoutManager(context));
        ReservationAdapter reservationAdapter = new ReservationAdapter(listRestaurantDetail, context);
        rcvReservation.setAdapter(reservationAdapter);

        txtEmptyRestaurant = (TextView) dialog.findViewById(R.id.txt_empty_restaurant);

        rbNoReservation = (RadioButton) dialog.findViewById(R.id.custom_dialog_require_on_invite_setting_time_rb_have_not_restaurant);
        rbReservation = (RadioButton) dialog.findViewById(R.id.custom_dialog_require_on_invite_setting_time_rb_have_restaurant);

        setValueTime(txtTimer, txtDate);

        if (rbNoReservation.isChecked()) {
            setValuePlaceNoReservation(txtAddress, position, listRestaurant, noReservationAdapter, rcvNoReservation, txtEmptyRestaurant);
        } else if (rbReservation.isChecked()) {
            setValuePlaceReservation(txtAddress, position, listRestaurantDetail, reservationAdapter, rcvReservation, txtEmptyRestaurant);
        }


        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                String formattedDate = df.format(c.getTime());

                socketIO.emit("invite", new MySharePreference((Activity) context).getPhoneLogin() + "|" + listAccordantUser.get(position).getAccordantUser() + "|" +
                        txtDate.getText().toString() + "|" + txtTimer.getText().toString() + "|" + AppUtils.convertStringToNFD(txtAddress.getText().toString()) + "|" + formattedDate);
                for (AccordantUser temp : listAccordantUser) {
                    temp.setControl(false);
                }
                notifyDataSetChanged();
                countDown5Minute();
                dialog.cancel();
            }
        });

        llContainerTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llContainerSetTime.getVisibility() == View.VISIBLE)
                    llContainerSetTime.setVisibility(View.GONE);
                else {
                    llContainerSetTime.setVisibility(View.VISIBLE);
                    setOnclickllContainerTime(dialog, txtDate, txtTimer);
                }
            }
        });

    }


    private void countDown5Minute() {
        new CountDownTimer(300000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                for (AccordantUser temp : listAccordantUser) {
                    temp.setControl(true);
                }
                notifyDataSetChanged();
            }
        }.start();
    }

    @Override
    public int getItemCount() {
        return listAccordantUser.size();
    }

    private void setValueTime(TextView txtTime, TextView txtDate) {
        Calendar calendar = Calendar.getInstance();
        Log.e("time", calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1));
        txtTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        txtDate.setText(calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1));
    }

    private void setValuePlaceNoReservation(final TextView txtPlace, int position, final ArrayList<Restaurant> listRestaurant, final NoReservationAdapter adapter, final RecyclerView rcv, final TextView txtEmpty) {

        MySharePreference requireInfoSharePreference = new MySharePreference((Activity) context, new MySharePreference((Activity) context).getPhoneLogin());

        String latlng = Math.abs((Float.parseFloat(requireInfoSharePreference.getLatLngAddressRequire().split(",")[0].trim()) -
                Float.parseFloat(listAccordantUser.get(position).getLatlng().split(",")[0].trim())) / 2) + "," +
                Math.abs((Float.parseFloat(requireInfoSharePreference.getLatLngAddressRequire().split(",")[1].trim()) -
                        Float.parseFloat(listAccordantUser.get(position).getLatlng().split(",")[1].trim())) / 2);

        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(context.getResources().getStringArray(R.array.City)));

        String city = null, district = null;

        for (int i = 0; i < list.size(); i++) {
            if (StringEscapeUtils.unescapeJava(requireInfoSharePreference.getAddressRequire()).contains(list.get(i))) {
                city = list.get(i);
            }
        }

        list.clear();
        list.addAll(Arrays.asList(context.getResources().getStringArray(R.array.District)));
        for (int i = 0; i < list.size(); i++) {
            if (StringEscapeUtils.unescapeJava(requireInfoSharePreference.getAddressRequire()).contains(list.get(i))) {
                district = list.get(i);
            }
        }
        Log.e("city",requireInfoSharePreference.getAddressRequire()+"+++++++++++++++++++++++++++++++++++++++++++++");

        Log.e("city",city+"+++++++++++++++++++++++++++++++++++++++++++++");
        Log.e("district",district+"+++++++++++++++++++++++++++++++++++++++++++++");
        if (AppUtils.isNetworkAvailable(context)) {
            if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
                Call<ArrayList<Restaurant>> getListRestaurant = Connect.getRetrofit().getRestaurant(latlng, city, district);
                getListRestaurant.enqueue(new Callback<ArrayList<Restaurant>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                        if (response.body() != null && response.body().size() > 0) {

                                txtPlace.setText(response.body().get(0).getName());
                                listRestaurant.addAll(response.body());
                                adapter.notifyDataSetChanged();
                        } else {
                            rcv.setVisibility(View.GONE);
                            txtEmpty.setVisibility(View.VISIBLE);
                            txtEmpty.setText(context.getString(R.string.empty_no_restaurant));
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                rcv.setVisibility(View.GONE);
                txtEmpty.setVisibility(View.VISIBLE);
                txtEmpty.setText(context.getString(R.string.empty_no_restaurant));
            }
        } else {
            Toast.makeText(context, context.getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void setValuePlaceReservation(final TextView txtPlace, int position, final ArrayList<RestaurantDetail> listRestaurant, final ReservationAdapter adapter, final RecyclerView rcv, final TextView txtEmpty) {

        MySharePreference mySharePreference = new MySharePreference((Activity) context);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM:dd:yyyy", Locale.ENGLISH);
        String currentTime = format.format(calendar.getTime());

        if (AppUtils.isNetworkAvailable(context)) {
            Call<ArrayList<RestaurantDetail>> getAllReservation = Connect.getRetrofit().getAllReservation(mySharePreference.getPhoneLogin(), currentTime);
            getAllReservation.enqueue(new Callback<ArrayList<RestaurantDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<RestaurantDetail>> call, Response<ArrayList<RestaurantDetail>> response) {
                    if (response.body() != null) {
                        listRestaurant.clear();
                        listRestaurant.addAll(response.body());
                        Collections.reverse(listRestaurant);

                        adapter.notifyDataSetChanged();
                    } else {
                        rcv.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setText(context.getString(R.string.empty_restaurant));
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<RestaurantDetail>> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, context.getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void setOnclickllContainerTime(Dialog dialog, final TextView txtDate, final TextView txtTime) {
        final WheelPicker wpdate, wpmonth, wphour, wpminute;
        wpdate = (WheelPicker) dialog.findViewById(R.id.custom_dialog_require_on_invite_wp_date);
        wpmonth = (WheelPicker) dialog.findViewById(R.id.custom_dialog_require_on_invite_wp_month);
        wphour = (WheelPicker) dialog.findViewById(R.id.custom_dialog_require_on_invite_wp_hour);
        wpminute = (WheelPicker) dialog.findViewById(R.id.custom_dialog_require_on_invite_wp_minute);

        wpdate.setData(Arrays.asList(context.getResources().getStringArray(R.array.Day_31)));
        wpdate.setSelectedItemPosition(Integer.parseInt(txtDate.getText().toString().trim().split("/")[0]) - 1);
        wpmonth.setData(Arrays.asList(context.getResources().getStringArray(R.array.Month)));
        wpmonth.setSelectedItemPosition(Integer.parseInt(txtDate.getText().toString().trim().split("/")[1]) - 1);
        wphour.setData(Arrays.asList(context.getResources().getStringArray(R.array.Hour)));
        wphour.setSelectedItemPosition(Integer.parseInt(txtTime.getText().toString().trim().split(":")[0]));
        wpminute.setData(Arrays.asList(context.getResources().getStringArray(R.array.Minute)));
        wpminute.setSelectedItemPosition((Integer.parseInt(txtTime.getText().toString().trim().split(":")[1]) / 10) + 1);

        final LocalDate localDate = new LocalDate();
        wpmonth.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                txtDate.setText((wpdate.getCurrentItemPosition() + 1) + "/" + (wpmonth.getCurrentItemPosition() + 1));
                setDataforWhellPicker(i, wpdate, localDate.getYear());
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });

        wpdate.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                txtDate.setText((wpdate.getCurrentItemPosition() + 1) + "/" + txtDate.getText().toString().trim().split("/")[1]);
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });

        wphour.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                txtTime.setText((wphour.getCurrentItemPosition()) + ":" + txtTime.getText().toString().trim().split(":")[1]);
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });

        wpminute.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                txtTime.setText(txtTime.getText().toString().trim().split(":")[0] + ":" + wpminute.getCurrentItemPosition() * 10);
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

    private void setDataforWhellPicker(int position, WheelPicker wpDate, int year) {
        switch (position) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                wpDate.setData(Arrays.asList(context.getResources().getStringArray(R.array.Day_31)));
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                wpDate.setData(Arrays.asList(context.getResources().getStringArray(R.array.Day_30)));
                break;
            case 1:
                if (checkLeapYear(year)) {
                    wpDate.setData(Arrays.asList(context.getResources().getStringArray(R.array.Day_29)));
                } else {
                    wpDate.setData(Arrays.asList(context.getResources().getStringArray(R.array.Day_28)));
                }
        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar, imgGender;
        private TextView txtName, txtPercent, txtAge, txtAddress;
        private LinearLayout llContainer;
        private ImageButton ibtnInvite;

        private ProgressBar progressBar;

        public CustomViewHolder(View itemView) {
            super(itemView);
            llContainer = (LinearLayout) itemView.findViewById(R.id.fragment_require_on_custom_cardview_ll_container_item);
            imgAvatar = (ImageView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_img_avatar);
            imgGender = (ImageView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_img_gender);
            txtName = (TextView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_txt_name);
            txtPercent = (TextView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_txt_percent);
            txtAge = (TextView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_txt_age);
            txtAddress = (TextView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_txt_address);
            ibtnInvite = (ImageButton) itemView.findViewById(R.id.fragment_require_on_custom_cardview_ibtn_invite);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}
