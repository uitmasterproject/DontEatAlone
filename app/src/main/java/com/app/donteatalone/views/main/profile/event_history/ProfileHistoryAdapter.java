package com.app.donteatalone.views.main.profile.event_history;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.ProfileHistoryModel;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.require.main_require.on_require.ProfileAccordantUser;

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

    public ProfileHistoryAdapter(ArrayList<ProfileHistoryModel> listProfileHistory, Context context) {
        this.listProfileHistory = listProfileHistory;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_fragment_profile_history, null);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        if(listProfileHistory.get(position).getTimer()!=null) {
            holder.txtDate.setText(listProfileHistory.get(position).getDate() + " " + listProfileHistory.get(position).getTimer());
        }
        holder.txtAccordantName.setText(listProfileHistory.get(position).getAccordantFullName());
        holder.txtPlace.setText(listProfileHistory.get(position).getPlace());

        /*Event click to friend name*/
        holder.txtAccordantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ProfileAccordantUser.class);
                intent.putExtra(ARG_PHONE_NUMBER,listProfileHistory.get(position).getAccordantPhone());
                context.startActivity(intent);
            }
        });

        /*Event click to place*/
        holder.txtPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Link to restaurant", Toast.LENGTH_SHORT).show();
            }
        });
        /*Event Click to icon Share*/
        holder.llAppraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.llContainer.getVisibility() == View.VISIBLE) {
                    holder.llContainer.setVisibility(View.GONE);
                } else {
                    holder.llContainer.setVisibility(View.VISIBLE);
                    if (listProfileHistory.get(position).getAccordantAppraise() == null && !listProfileHistory.get(position).getAccordantRate()) {
                        holder.llAccordantAppraise.setVisibility(View.GONE);
                    } else {
                        if (listProfileHistory.get(position).getAccordantRate()) {
                            holder.imgAccordantHeart.setImageResource(R.drawable.ic_heart_red);
                        }
                        if (listProfileHistory.get(position).getAccordantAppraise() != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                holder.txtAccordantAppraise.setText(
                                        Html.fromHtml("<b>" + listProfileHistory.get(position).getAccordantFullName() + ": </b>", Html.FROM_HTML_MODE_COMPACT) +
                                                listProfileHistory.get(position).getAccordantAppraise());
                            } else {
                                holder.txtAccordantAppraise.setText(
                                        Html.fromHtml("<b>" + listProfileHistory.get(position).getAccordantFullName() + ": </b>") +
                                                listProfileHistory.get(position).getAccordantAppraise());
                            }
                        }
                    }

                    if (listProfileHistory.get(position).getMyAppraise() == null && !listProfileHistory.get(position).getMyRate()) {
                        holder.txtMyAppraise.setVisibility(View.GONE);
                        holder.llWriteAppraise.setVisibility(View.VISIBLE);
                        holder.imgMyHeart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!listProfileHistory.get(position).getMyRate()) {
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
                                BaseProgress dialog = new BaseProgress();
                                dialog.showProgressLoading(context);
                                listProfileHistory.get(position).setMyAppraise(AppUtils.convertStringToNFD(holder.edtWriteAppraise.getText().toString()));
                                Call<ArrayList<ProfileHistoryModel>> editEventHistory = Connect.getRetrofit().editEventHistory(new MySharePreference((Activity) context).getPhoneLogin(),
                                        listProfileHistory.get(position));
                                editEventHistory.enqueue(new Callback<ArrayList<ProfileHistoryModel>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<ProfileHistoryModel>> call, Response<ArrayList<ProfileHistoryModel>> response) {
                                        if (response.body() != null) {
                                            dialog.hideProgressLoading();
                                            listProfileHistory.clear();
                                            listProfileHistory.addAll(response.body());
                                            notifyItemChanged(position);
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
                        setMyAppraise(holder,listProfileHistory.get(position).getMyAppraise(), listProfileHistory.get(position).getMyRate());
                    }

                }
            }
        });
    }

    private void setMyAppraise(CustomViewHolder holder, String myAppraise, boolean myRate){
        holder.txtMyAppraise.setVisibility(View.VISIBLE);
        holder.llWriteAppraise.setVisibility(View.GONE);
        if (myRate) {
            holder.imgMyHeart.setImageResource(R.drawable.ic_heart_red);
        }
        if (myAppraise != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.txtMyAppraise.setText(
                        Html.fromHtml(
                                "<b>" + new MySharePreference((Activity) context).getFullNameLogin() + ": </b>", Html.FROM_HTML_MODE_COMPACT) +
                                myAppraise);
            } else {
                holder.txtMyAppraise.setText(
                        Html.fromHtml(
                                "<b>" + new MySharePreference((Activity) context).getFullNameLogin() + ": </b>") +
                                myAppraise);
            }
        }
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
