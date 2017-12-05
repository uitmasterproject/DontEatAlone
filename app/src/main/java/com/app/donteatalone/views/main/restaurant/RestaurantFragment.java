package com.app.donteatalone.views.main.restaurant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Restaurant;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.require.main_require.on_require.CustomInvitedRestaurantAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 16-Jun-17
 */

public class RestaurantFragment extends Fragment {
    private View viewGroup;
    private AutoCompleteTextView atctInputSearch;
    private ImageButton ibtnSearch;
    private RecyclerView rcvListRestaurant;
    private CustomInvitedRestaurantAdapter adapter;
    private ArrayList<Restaurant> listRestaurant;
    private BaseProgress dialog;

    public static RestaurantFragment newInstance() {

        return new RestaurantFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_restaurant, null);
        init();
        showListRestaurant();
        setClickSearch();
        return viewGroup;
    }

    private void init() {
        dialog = new BaseProgress();
        atctInputSearch = (AutoCompleteTextView) viewGroup.findViewById(R.id.fragment_restaurant_atct_inputSearch);
        ArrayAdapter adapterView = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getContext().getResources().getStringArray(R.array.District));
        atctInputSearch.setAdapter(adapterView);
        ibtnSearch = (ImageButton) viewGroup.findViewById(R.id.fragment_restaurant_ibtn_search);
        rcvListRestaurant = (RecyclerView) viewGroup.findViewById(R.id.fragment_restaurant_rcv_lst_place);
        rcvListRestaurant.setLayoutManager(new LinearLayoutManager(getContext()));
        listRestaurant = new ArrayList<Restaurant>();
        adapter = new CustomInvitedRestaurantAdapter(listRestaurant);
        rcvListRestaurant.setAdapter(adapter);
    }

    private void showListRestaurant() {
        String latlng;
        if (TextUtils.isEmpty(new MySharePreference(getActivity()).getLatLngAddressLogin())) {
            latlng = "10.8231,106.6297";
        } else {
            latlng = new MySharePreference(getActivity()).getLatLngAddressLogin();
        }
        dialog.showProgressLoading(getContext());
        Call<ArrayList<Restaurant>> methodListRestaurant = Connect.getRetrofit().getRestaurantFollowLatlng(latlng);
        methodListRestaurant.enqueue(new Callback<ArrayList<Restaurant>>() {
            @Override
            public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                dialog.hideProgressLoading();
                if (response.body() != null) {
                    for (Restaurant res : response.body()) {
                        listRestaurant.add(new Restaurant(res.getName(), res.getAddress(), res.getLatlng(), res.getOpenDay()));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {
                dialog.hideProgressLoading();
            }
        });
    }

    private void setClickSearch() {
        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listRestaurant.clear();
                if (atctInputSearch.getText().toString().equals("")) {
                    dialog.showProgressLoading(getContext());
                    Call<ArrayList<Restaurant>> methodlistAllRestaurant = Connect.getRetrofit().getAllRestaurant();
                    methodlistAllRestaurant.enqueue(new Callback<ArrayList<Restaurant>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                            if (response.body() !=null) {
                                dialog.hideProgressLoading();
                                for (Restaurant res : response.body()) {
                                    listRestaurant.add(new Restaurant(res.getName(), res.getAddress(), res.getLatlng(), res.getOpenDay()));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {
                            dialog.hideProgressLoading();
                        }
                    });
                } else {
                    Call<ArrayList<Restaurant>> methodlistRestaurantFollowDistrict = Connect.getRetrofit().getRestaurantFollowDistrict(atctInputSearch.getText().toString());
                    dialog.showProgressLoading(getContext());
                    methodlistRestaurantFollowDistrict.enqueue(new Callback<ArrayList<Restaurant>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                            dialog.hideProgressLoading();
                            if (response.body()!=null) {
                                for (Restaurant res : response.body()) {
                                    listRestaurant.add(new Restaurant(res.getName(), res.getAddress(), res.getLatlng(), res.getOpenDay()));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {
                            dialog.hideProgressLoading();
                        }
                    });
                }
            }
        });
    }

}
