package com.app.donteatalone.views.main.restaurant.AllowReservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.base.OnRecyclerItemClickListener;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.RestaurantDetail;
import com.app.donteatalone.utils.MySharePreference;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 09-Dec-17
 */

public class AllowReservationFragment extends Fragment {

    private View view;
    private Spinner snCity;
    private Spinner snDistrict;
    private RecyclerView rcvListRestaurant;
    private ArrayList<RestaurantDetail> listRestaurant;
    private AllowRestaurantAdapter adapter;

    private MySharePreference mySharePreference;
    private BaseProgress baseProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_restaurant_reservation, container, false);

        init();

        return view;
    }

    private void init() {
        mySharePreference = new MySharePreference(getActivity());

        baseProgress = new BaseProgress();

        snCity = (Spinner) view.findViewById(R.id.choose_city);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.City));
        snCity.setAdapter(cityAdapter);

        snDistrict = (Spinner) view.findViewById(R.id.choose_district);
        final ArrayList<String> listDistrict = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.District)));
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listDistrict);
        snDistrict.setAdapter(districtAdapter);
        for (int i = 0; i < listDistrict.size(); i++) {
            if (!TextUtils.isEmpty(mySharePreference.getAddressLogin())) {
                if (StringEscapeUtils.unescapeJava(mySharePreference.getAddressLogin()).contains(listDistrict.get(i))) {
                    snDistrict.setSelection(i);
                    break;
                }
            } else {
                snDistrict.setSelection(0);
            }
        }

        rcvListRestaurant = (RecyclerView) view.findViewById(R.id.rcv_list_restaurant);
        rcvListRestaurant.setLayoutManager(new LinearLayoutManager(getActivity()));

        listRestaurant = new ArrayList<>();
        adapter = new AllowRestaurantAdapter(listRestaurant, getActivity(), view);

        rcvListRestaurant.setAdapter(adapter);

        adapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int resource) {
                Intent intent = new Intent(getActivity(), AllowReservationDetail.class);
                listRestaurant.get(resource).setCity(snCity.getSelectedItem().toString());
                listRestaurant.get(resource).setDistrict(snDistrict.getSelectedItem().toString());
                intent.putExtra(AllowReservationDetail.RESTAURANT_DETAIL_DATA, listRestaurant.get(resource));
                startActivity(intent);
            }
        });

        snDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                baseProgress.showProgressLoading(getActivity());
                Call<ArrayList<RestaurantDetail>> listRestaurantReservation = Connect.getRetrofit().getListRestaurantReservation(snCity.getSelectedItem().toString(),
                        snDistrict.getSelectedItem().toString());
                listRestaurantReservation.enqueue(new Callback<ArrayList<RestaurantDetail>>() {
                    @Override
                    public void onResponse(Call<ArrayList<RestaurantDetail>> call, Response<ArrayList<RestaurantDetail>> response) {
                        baseProgress.hideProgressLoading();
                        if (response.body() != null) {
                            listRestaurant.clear();
                            listRestaurant.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<RestaurantDetail>> call, Throwable t) {
                        baseProgress.hideProgressLoading();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

    }
}
