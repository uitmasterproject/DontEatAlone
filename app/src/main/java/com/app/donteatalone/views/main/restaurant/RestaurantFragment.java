package com.app.donteatalone.views.main.restaurant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Restaurant;
import com.app.donteatalone.views.main.require.main_require.on_require.CustomInvitedRestaurantAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ChomChom on 16-Jun-17.
 */

public class RestaurantFragment extends Fragment {
    private View viewGroup;
    private AutoCompleteTextView atctInputSearch;
    private ImageButton ibtnSearch;
    private RecyclerView rcvListRestaurant;
    private CustomInvitedRestaurantAdapter adapter;
    private ArrayList<Restaurant> listRestaurant;

    public static RestaurantFragment newInstance() {

        RestaurantFragment fragment = new RestaurantFragment();
        return fragment;
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

    private void init(){
        atctInputSearch=(AutoCompleteTextView) viewGroup.findViewById(R.id.fragment_restaurant_atct_inputSearch);
        ArrayAdapter adapterView=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,getContext().getResources().getStringArray(R.array.District));
        atctInputSearch.setAdapter(adapterView);
        ibtnSearch=(ImageButton) viewGroup.findViewById(R.id.fragment_restaurant_ibtn_search);
        rcvListRestaurant=(RecyclerView) viewGroup.findViewById(R.id.fragment_restaurant_rcv_lst_place);
        rcvListRestaurant.setLayoutManager(new LinearLayoutManager(getContext()));
        listRestaurant=new ArrayList<Restaurant>();
        adapter=new CustomInvitedRestaurantAdapter(listRestaurant);
        rcvListRestaurant.setAdapter(adapter);
    }

    private void showListRestaurant(){
        Connect connect=new Connect();
        Call<ArrayList<Restaurant>> methodlistRestaurant=connect.getRetrofit().getRestaurantFollowLatlng(getInfointoSharedPreferences("latlngadressLogin"));
        methodlistRestaurant.enqueue(new Callback<ArrayList<Restaurant>>() {
            @Override
            public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                if(response.body().size()>0){
                    for (Restaurant res:response.body()) {
                        listRestaurant.add(new Restaurant(res.getName(),res.getAddress(),res.getLatlng(),res.getOpenDay()));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {

            }
        });
    }

    private void setClickSearch(){
        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listRestaurant.clear();
                Connect connect=new Connect();
                Log.e("Result",atctInputSearch.getText().toString().equals("")+"");
                if(atctInputSearch.getText().toString().equals("")==true){
                    Call<ArrayList<Restaurant>> methodlistAllRestaurant = connect.getRetrofit().getAllRestaurant();
                    methodlistAllRestaurant.enqueue(new Callback<ArrayList<Restaurant>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                            if (response.body().size() > 0) {
                                for (Restaurant res : response.body()) {
                                    listRestaurant.add(new Restaurant(res.getName(), res.getAddress(), res.getLatlng(), res.getOpenDay()));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {

                        }
                    });
                }
                else {
                    Log.e("District",atctInputSearch.getText().toString());
                    Call<ArrayList<Restaurant>> methodlistRestaurantFollowDistrict = connect.getRetrofit().getRestaurantFollowDistrict(atctInputSearch.getText().toString());
                    methodlistRestaurantFollowDistrict.enqueue(new Callback<ArrayList<Restaurant>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                            if (response.body().size() > 0) {
                                for (Restaurant res : response.body()) {
                                    listRestaurant.add(new Restaurant(res.getName(), res.getAddress(), res.getLatlng(), res.getOpenDay()));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private String getInfointoSharedPreferences(String str) {
        SharedPreferences pre = getContext().getSharedPreferences("account", MODE_PRIVATE);
        String data = pre.getString(str, "");
        return data;
    }
}
