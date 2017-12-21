package com.app.donteatalone.views.main.restaurant.AllowReservation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.RestaurantDetail;

/**
 * Created by ChomChom on 19-Dec-17
 */

public class AllowReservationDetail extends FragmentActivity implements View.OnClickListener{
    public static String RESTAURANT_DETAIL_DATA = "RESTAURANT_DETAIL_DATA";

    private RestaurantDetail restaurantDetail;

    private TextView txtInfo, txtReserve;
    private ImageView imgBack;

    private FragmentTransaction transaction;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_reservation);
        if(getIntent().getParcelableExtra(RESTAURANT_DETAIL_DATA)!=null){
            restaurantDetail=getIntent().getParcelableExtra(RESTAURANT_DETAIL_DATA);
        }
        init();
    }

    private void init(){
        txtInfo = (TextView) findViewById(R.id.activity_restaurant_reservation_tv_info);
        txtReserve = (TextView) findViewById(R.id.activity_restaurant_reservation_tv_reserve);
        imgBack = (ImageView) findViewById(R.id.btn_back);
        txtInfo.setOnClickListener(this);
        txtReserve.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_restaurant_reservation_fl_container, ReservationDetailInfo.newInstance(restaurantDetail));
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_restaurant_reservation_tv_info:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_restaurant_reservation_fl_container, ReservationDetailInfo.newInstance(restaurantDetail));
                transaction.commit();
                break;
            case R.id.activity_restaurant_reservation_tv_reserve:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_restaurant_reservation_fl_container, ReservationDetailDesign.newInstance(restaurantDetail));
                transaction.commit();
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}
