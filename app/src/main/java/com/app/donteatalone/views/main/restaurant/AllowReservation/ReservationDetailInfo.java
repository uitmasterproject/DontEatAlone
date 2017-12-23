package com.app.donteatalone.views.main.restaurant.AllowReservation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.RestaurantDetail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager;

/**
 * Created by ChomChom on 20-Dec-17
 */

public class ReservationDetailInfo extends Fragment {

    private View view;
    private RestaurantDetail restaurantDetail;

    private RecyclerView recyclerView;
    private TextView txtName;
    private TextView txtStatus, txtTime;
    private TextView txtPhone;
    private LinearLayout llServiceContainer;
    private TextView txtAddress;
    private TextView txtType;
    private TextView txtPrice;

    public static ReservationDetailInfo newInstance(RestaurantDetail restaurantDetail) {
        ReservationDetailInfo fm = new ReservationDetailInfo();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AllowReservationDetail.RESTAURANT_DETAIL_DATA, restaurantDetail);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().getParcelable(AllowReservationDetail.RESTAURANT_DETAIL_DATA) != null) {
            restaurantDetail = getArguments().getParcelable(AllowReservationDetail.RESTAURANT_DETAIL_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_restaurant_reservation_info, container, false);
        init();
        return view;
    }

    private void init() {
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_restaurant_reservation_info_rv_images);

        PickerLayoutManager pickerLayoutManager = new PickerLayoutManager(getActivity(), PickerLayoutManager.HORIZONTAL, false);
        pickerLayoutManager.setChangeAlpha(true);
        pickerLayoutManager.setScaleDownBy(0.1f);
        pickerLayoutManager.setScaleDownDistance(0.4f);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(pickerLayoutManager);

        recyclerView.setAdapter(new RealImageAdapter(restaurantDetail.getRealImage(), getActivity()));

        txtName = (TextView) view.findViewById(R.id.fragment_restaurant_reservation_info_tv_name);
        txtTime = (TextView) view.findViewById(R.id.fragment_restaurant_reservation_info_tv_open_time);
        txtStatus = (TextView) view.findViewById(R.id.fragment_restaurant_reservation_info_tv_status);
        txtPhone = (TextView) view.findViewById(R.id.fragment_restaurant_reservation_info_tv_contact);
        llServiceContainer = (LinearLayout) view.findViewById(R.id.ll_service_container);
        txtAddress = (TextView) view.findViewById(R.id.fragment_restaurant_reservation_info_tv_address);
        txtType = (TextView) view.findViewById(R.id.fragment_restaurant_reservation_info_tv_types);
        txtPrice = (TextView) view.findViewById(R.id.fragment_restaurant_reservation_info_tv_price);

        txtName.setText(restaurantDetail.getName());
        txtTime.setText(restaurantDetail.getOpenDay());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String openDay = restaurantDetail.getOpenDay();
        if (!TextUtils.isEmpty(openDay)){
            if(openDay.contains("|") && openDay.contains("-")){
                String [] listTimeLine = openDay.split(" | ");

                if(listTimeLine[0].contains("-")&&listTimeLine[1].contains("-")){

                    if ((format.format(calendar.getTime()).compareTo(listTimeLine[0].split(" - ")[0]) > 0 &&
                            format.format(calendar.getTime()).compareTo(listTimeLine[0].split(" - ")[1]) < 0)||
                            (format.format(calendar.getTime()).compareTo(listTimeLine[1].split(" - ")[0]) > 0 &&
                                    format.format(calendar.getTime()).compareTo(listTimeLine[1].split(" - ")[1]) < 0)) {
                        txtStatus.setText(getString(R.string.open_door));
                    } else {
                        txtStatus.setText(getString(R.string.close_door));
                    }
                }else {
                    txtStatus.setText(getString(R.string.up_to_date));
                }
            }else if(openDay.contains("-")){
                if (format.format(calendar.getTime()).compareTo(openDay.split(" - ")[0]) > 0 &&
                        format.format(calendar.getTime()).compareTo(openDay.split(" - ")[1]) < 0) {
                    txtStatus.setText(getString(R.string.open_door));
                } else {
                    txtStatus.setText(getString(R.string.close_door));
                }
            }else {
                txtStatus.setText(getString(R.string.up_to_date));
            }
        } else {
            txtStatus.setText(getString(R.string.up_to_date));
        }

        if (TextUtils.isEmpty(restaurantDetail.getPhoneNumber())) {
            txtPhone.setText(getString(R.string.up_to_date));
        } else {
            txtPhone.setText(restaurantDetail.getPhoneNumber());
        }

        if (restaurantDetail.getService() != null && restaurantDetail.getService().size() > 0) {
            for (int i = 0; i < restaurantDetail.getService().size(); i++) {
                ImageView imageView = (ImageView) llServiceContainer.getChildAt(i);
                if (Integer.parseInt(restaurantDetail.getService().get(i)) == 1) {
                    imageView.setColorFilter(Color.GREEN);
                } else {
                    imageView.setColorFilter(Color.GRAY);
                }
            }
        }

        txtAddress.setText(restaurantDetail.getAddress());
        txtType.setText(restaurantDetail.getType());
        txtPrice.setText(restaurantDetail.getPrice());
    }

}
