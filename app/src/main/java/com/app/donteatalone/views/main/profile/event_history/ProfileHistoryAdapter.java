package com.app.donteatalone.views.main.profile.event_history;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.ProfileHistoryModel;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.require.main_require.on_require.ProfileAccordantUser;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.donteatalone.views.main.blog.DetailBlogActivity.ARG_PHONE_NUMBER;

/**
 * Created by Le Hoang Han on 6/28/2017
 */

public class ProfileHistoryAdapter extends RecyclerView.Adapter<ProfileHistoryAdapter.CustomViewHolder> {

    private ArrayList<ProfileHistoryModel> listProfileHistory;
    private Context context;

    private String phoneNumber;

    public ProfileHistoryAdapter(ArrayList<ProfileHistoryModel> listProfileHistory, Context context) {
        this.listProfileHistory = listProfileHistory;
        this.context = context;
    }

    public ProfileHistoryAdapter(ArrayList<ProfileHistoryModel> listProfileHistory, Context context, String phoneNumber) {
        this.listProfileHistory = listProfileHistory;
        this.context = context;
        this.phoneNumber = phoneNumber;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_fragment_profile_history, null);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        if (listProfileHistory.get(position).getTimeInvite() != null) {
            holder.txtDate.setText(listProfileHistory.get(position).getTimeInvite());
        }
        holder.txtAccordantName.setText(StringUtils.capitalize(StringEscapeUtils.unescapeJava(listProfileHistory.get(position).getParticipant().getFullName())));
        holder.txtPlace.setText(StringUtils.capitalize(StringEscapeUtils.unescapeJava(listProfileHistory.get(position).getRestaurantInfo().getName())));

        /*Event click to friend name*/
        holder.txtAccordantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileAccordantUser.class);
                intent.putExtra(ARG_PHONE_NUMBER, listProfileHistory.get(position).getParticipant().getAccordantUser());
                context.startActivity(intent);
            }
        });

        /*Event click to place*/
        holder.txtPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClickRestaurant(holder.txtPlace, listProfileHistory.get(position));
            }
        });

        if(holder.llContainer.getVisibility()==View.VISIBLE){
            setValueComment(holder,position);
        }

        /*Event Click to icon Share*/
        holder.llAppraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.llContainer.getVisibility() == View.VISIBLE) {
                    holder.llContainer.setVisibility(View.GONE);
                } else {
                    holder.llContainer.setVisibility(View.VISIBLE);
                    setValueComment(holder,position);
                }
            }
        });
    }

    private void setValueComment(final CustomViewHolder holder, final int position){
        if (listProfileHistory.get(position).getAccordantAppraise() == null && !listProfileHistory.get(position).isAccordantRate()) {
            holder.llAccordantAppraise.setVisibility(View.GONE);
        } else {
            if (listProfileHistory.get(position).isAccordantRate()) {
                holder.imgAccordantHeart.setImageResource(R.drawable.ic_heart_red);
            }
            if (listProfileHistory.get(position).getAccordantAppraise() != null) {
                holder.txtAccordantAppraise.setText(setMultiColorText(listProfileHistory.get(position).getParticipant().getFullName(),
                        listProfileHistory.get(position).getAccordantAppraise()));

            }
        }

        if(TextUtils.isEmpty(phoneNumber) || !new MySharePreference((Activity)context).getPhoneLogin().equals(phoneNumber)) {
            if (listProfileHistory.get(position).getMyAppraise() == null && !listProfileHistory.get(position).isMyRate()) {
                holder.txtMyAppraise.setVisibility(View.GONE);
                holder.llWriteAppraise.setVisibility(View.VISIBLE);
                holder.imgMyHeart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!listProfileHistory.get(position).isMyRate()) {
                            holder.imgMyHeart.setImageResource(R.drawable.ic_heart_red);
                            listProfileHistory.get(position).setMyRate(true);
                        } else {
                            holder.imgMyHeart.setImageResource(R.drawable.ic_heart_black);
                            listProfileHistory.get(position).setMyRate(false);
                        }
                    }
                });

                holder.ibtnSendAppraise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final BaseProgress dialog = new BaseProgress();
                        dialog.showProgressLoading(context);
                        listProfileHistory.get(position).setMyAppraise(holder.edtWriteAppraise.getText().toString());
                        Call<ArrayList<ProfileHistoryModel>> editEventHistory = Connect.getRetrofit().editEventHistory(new MySharePreference((Activity) context).getPhoneLogin(),
                                listProfileHistory.get(position));
                        editEventHistory.enqueue(new Callback<ArrayList<ProfileHistoryModel>>() {
                            @Override
                            public void onResponse(Call<ArrayList<ProfileHistoryModel>> call, Response<ArrayList<ProfileHistoryModel>> response) {
                                dialog.hideProgressLoading();
                                if (response.body() != null) {
                                    listProfileHistory.clear();
                                    listProfileHistory.addAll(response.body());
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<ProfileHistoryModel>> call, Throwable t) {
                                dialog.hideProgressLoading();
                            }
                        });
                    }
                });

            } else {
                setMyAppraise(holder, listProfileHistory.get(position).getMyAppraise(), listProfileHistory.get(position).isMyRate());
            }
        }

    }

    private void setMyAppraise(CustomViewHolder holder, String myAppraise, boolean myRate) {
        holder.txtMyAppraise.setVisibility(View.VISIBLE);
        holder.llWriteAppraise.setVisibility(View.GONE);
        if (myRate) {
            holder.imgMyHeart.setImageResource(R.drawable.ic_heart_red);
        }
        if (myAppraise != null) {
            holder.txtMyAppraise.setText(setMultiColorText(new MySharePreference((Activity) context).getFullNameLogin(),myAppraise));
        }
    }

    private Spannable setMultiColorText(String name, String content) {
        Spannable spannable = new SpannableString(StringUtils.capitalize(StringEscapeUtils.unescapeJava(name) + ":  "+content));

        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, StringUtils.capitalize(StringEscapeUtils.unescapeJava(name)).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    private void setClickRestaurant(TextView txtRestaurant, final ProfileHistoryModel profileHistoryModel){
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
                        .load(profileHistoryModel.getRestaurantInfo().getAvatar())
                        .error(R.drawable.temp)
                        .into(imgAvatar);

                txtName.setText(StringEscapeUtils.unescapeJava(profileHistoryModel.getRestaurantInfo().getName()));
                txtAddress.setText(StringEscapeUtils.unescapeJava(profileHistoryModel.getRestaurantInfo().getAddress()));
                txtRate.setText(profileHistoryModel.getRestaurantInfo().getRate());
                if (TextUtils.isEmpty(profileHistoryModel.getRestaurantInfo().getPrice())) {
                    txtPrice.setText("20.000 - 10.000");
                } else {
                    txtPrice.setText(profileHistoryModel.getRestaurantInfo().getPrice());
                }

                if(profileHistoryModel.getReservationDetail()==null) {
                    if (TextUtils.isEmpty(profileHistoryModel.getRestaurantInfo().getOpenDay())) {
                        txtOpenDay.setText("10:00 - 22:00");
                    } else {
                        txtOpenDay.setText(profileHistoryModel.getRestaurantInfo().getOpenDay());
                    }
                }else {
                    txtOpenDay.setText(profileHistoryModel.getReservationDetail().getTime());
                    txtReserve.setText(context.getString(R.string.order_table)+" "+profileHistoryModel.getReservationDetail().getTable());
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

    @Override
    public int getItemCount() {
        return listProfileHistory.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDate, txtAccordantName, txtPlace, txtMyAppraise, txtAccordantAppraise;
        private ImageView imgMyHeart, imgAccordantHeart;
        private EditText edtWriteAppraise;
        private ImageButton ibtnSendAppraise;
        private LinearLayout llContainer, llAccordantAppraise, llAppraise, llWriteAppraise;

        public CustomViewHolder(View itemView) {
            super(itemView);
            txtDate = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_tv_date);
            txtAccordantName = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_tv_friend_name);
            txtPlace = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_tv_place);
            llAppraise = (LinearLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_ll_appraise);
            llContainer = (LinearLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_ll_container);
            llAccordantAppraise = (LinearLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_ll_accordantappraise);
            imgAccordantHeart = (ImageView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_iv_accordantrate);
            txtAccordantAppraise = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_txt_accordantappraise);
            imgMyHeart = (ImageView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_iv_myrate);
            txtMyAppraise = (TextView) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_txt_myappraise);
            llWriteAppraise = (LinearLayout) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_ll_writeappraise);
            edtWriteAppraise = (EditText) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_edt_writeappraise);
            ibtnSendAppraise = (ImageButton) itemView.findViewById(R.id.custom_adapter_recyclerview_fragment_profile_history_ibtn_send);

            llContainer.setVisibility(View.GONE);

        }
    }


}
