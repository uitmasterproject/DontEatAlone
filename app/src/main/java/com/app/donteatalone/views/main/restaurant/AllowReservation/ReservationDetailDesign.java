package com.app.donteatalone.views.main.restaurant.AllowReservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.base.OnRecyclerItemClickListener;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.ReservationDetail;
import com.app.donteatalone.model.RestaurantDetail;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.model.UserReservation;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.MainActivity;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 20-Dec-17
 */

public class ReservationDetailDesign extends Fragment implements View.OnClickListener {

    private final String endLunchTime = "15:00";
    private final String startEveningTime = "17:00";

    private View view;

    private RestaurantDetail restaurantDetail;

    private ViewPager viewPager;
    private RadioGroup radioGroup;

    private Spinner spnListTable;
    private Spinner spnListSession;
    private RecyclerView rcvListTime;
    private TimeAdapter adapter;
    private TextView txtEmptySessionTime;
    private String timeSession;

    private Button btnReservation;

    private BaseProgress baseProgress;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position < radioGroup.getChildCount()) {
                ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public static ReservationDetailDesign newInstance(RestaurantDetail restaurantDetail) {
        ReservationDetailDesign fm = new ReservationDetailDesign();
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
        view = inflater.inflate(R.layout.fragment_restaurant_reservation_book, container, false);
        init();
        return view;
    }

    private void init() {
        baseProgress = new BaseProgress();

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new ImagePagerAdapter(getActivity(), restaurantDetail.getDesignImage()));

        radioGroup = (RadioGroup) view.findViewById(R.id.radio);

        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(pageChangeListener);

        txtEmptySessionTime = (TextView) view.findViewById(R.id.txt_empty_time_session);

        spnListTable = (Spinner) view.findViewById(R.id.spn_list_table);
        spnListSession = (Spinner) view.findViewById(R.id.spn_list_session);
        rcvListTime = (RecyclerView) view.findViewById(R.id.rcv_list_time);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rcvListTime.setLayoutManager(horizontalLayoutManager);

        final ArrayList<String> listTimeSession = new ArrayList<>();

        adapter = new TimeAdapter(listTimeSession, getActivity());
        rcvListTime.setAdapter(adapter);

        adapter.setClickItem(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int resource) {
                timeSession = listTimeSession.get(resource);
            }
        });

        ArrayList<String> listAvailableTables = new ArrayList<>();

        ArrayList<String> listTables = new ArrayList<>();
        listTables.addAll(restaurantDetail.getListTables());

        ArrayList<ReservationDetail> listReservations = new ArrayList<>();
        listReservations.addAll(restaurantDetail.getListReservations());

        final ArrayList<String> listSessions = new ArrayList<>();
        listSessions.addAll(restaurantDetail.getListSessions());

        listAvailableTables.clear();
        for (int i = 0; i < listTables.size(); i++) {

            boolean countTable = false;
            for (int j = 0; j < listReservations.size(); j++) {
                if (!TextUtils.isEmpty(listReservations.get(j).getTable()) && listTables.get(i).equals(listReservations.get(j).getTable())) {
                    countTable = true;
                    int countSession = 0;
                    for (int k = 0; k < listSessions.size(); k++) {
                        if (!TextUtils.isEmpty(listReservations.get(j).getSession()) && listReservations.get(j).getSession().contains(listSessions.get(k))) {
                            countSession += 1;
                        }
                    }

                    if (countSession != listSessions.size()) {
                        listAvailableTables.add(listTables.get(i));
                    }
                }
            }

            if (!countTable) {
                listAvailableTables.add(listTables.get(i));
            }
        }

        spnListTable.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listAvailableTables));

        final ArrayList<String> listAvailableSession = new ArrayList<>();

        spnListTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (AppUtils.isNetworkAvailable(getActivity())) {
                    Call<ReservationDetail> getReservation = Connect.getRetrofit().getReservation(restaurantDetail.getCity(), restaurantDetail.getDistrict(),
                            restaurantDetail.getId() + "", spnListTable.getSelectedItem().toString());

                    baseProgress.showProgressLoading(getActivity());
                    getReservation.enqueue(new Callback<ReservationDetail>() {
                        @Override
                        public void onResponse(Call<ReservationDetail> call, Response<ReservationDetail> response) {
                            baseProgress.hideProgressLoading();
                            listAvailableSession.clear();
                            if (response.body() != null) {
                                ReservationDetail reservationDetail = response.body();

                                if (TextUtils.isEmpty(reservationDetail.getSession()) || reservationDetail.getSession().trim().length()<2) {
                                    listAvailableSession.addAll(listSessions);
                                } else {

                                    for (int i = 0; i < listSessions.size(); i++) {
                                        if (!reservationDetail.getSession().contains(listSessions.get(i))) {
                                            listAvailableSession.add(listSessions.get(i));
                                        }
                                    }

                                    if (listAvailableSession.size() == 0) {
                                        listAvailableSession.add(getString(R.string.empty_session));
                                    }
                                }
                            } else {
                                listAvailableSession.addAll(listSessions);
                            }

                            spnListSession.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listAvailableSession));
                        }

                        @Override
                        public void onFailure(Call<ReservationDetail> call, Throwable t) {
                            baseProgress.hideProgressLoading();
                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spnListSession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeSession=null;

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                String currentTime = format.format(calendar.getTime());

                String openDay = restaurantDetail.getOpenDay();
                if (spnListSession.getSelectedItem().toString().equals(getString(R.string.lunch_session))) {
                    if (!TextUtils.isEmpty(openDay) && openDay.contains(" | ")) {
                        String[] timeLine = openDay.split("|");
                        if (timeLine[0].trim().contains(" - ")) {
                            setTimeSession(currentTime, timeLine[0].split(" - ")[1].trim(), timeLine[0].split(" - ")[1].trim(), format, listTimeSession);
                        }
                    } else {
                        if (!TextUtils.isEmpty(openDay) && openDay.contains(" - ")) {
                            setTimeSession(currentTime, openDay.split(" - ")[0].trim(), endLunchTime, format, listTimeSession);
                        }

                    }
                } else if (spnListSession.getSelectedItem().toString().equals(getString(R.string.evening_session))) {
                    if (!TextUtils.isEmpty(openDay) && openDay.contains(" | ")) {
                        String[] timeLine = openDay.split("|");
                        if (timeLine[0].trim().contains(" - ")) {
                            setTimeSession(currentTime, timeLine[1].split(" - ")[0].trim(), timeLine[1].split(" - ")[1].trim(), format, listTimeSession);
                        }
                    } else {
                        if (!TextUtils.isEmpty(openDay) && openDay.contains(" - ")) {
                            setTimeSession(currentTime, startEveningTime, openDay.split(" - ")[1].trim(), format, listTimeSession);
                        }

                    }
                }

                if (listTimeSession.size() <= 0) {
                    rcvListTime.setVisibility(View.GONE);
                    txtEmptySessionTime.setVisibility(View.VISIBLE);
                } else {
                    rcvListTime.setVisibility(View.VISIBLE);
                    txtEmptySessionTime.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnReservation = (Button) view.findViewById(R.id.btn_reservation);
        btnReservation.setOnClickListener(this);
    }

    public String convertMinute(String time) {
        if (time.split(":")[1].compareTo("00") >= 0 && time.split(":")[1].compareTo("30") <= 0) {
            return time.split(":")[0] + ":" + "30";
        } else if (time.split(":")[1].compareTo("30") > 0) {
            if(Integer.parseInt(time.split(":")[0])<9) {
                return "0"+(Integer.parseInt(time.split(":")[0]) + 1) + ":" + "00";
            }else {
                return (Integer.parseInt(time.split(":")[0]) + 1) + ":" + "00";
            }
        }
        return (Integer.parseInt(time.split(":")[0]) + 1) + ":" + "00";
    }

    public void setTimeSession(String currentTime, String openDay, String endTime, SimpleDateFormat format, ArrayList<String> listTimeSession) {
        listTimeSession.clear();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm");
        if (openDay.trim().compareTo(currentTime) < 0) {
            currentTime = convertMinute(currentTime);

            while (currentTime.compareTo(endTime) <= 0) {
                listTimeSession.add(currentTime);

                LocalTime lt = formatter.parseLocalTime(currentTime);
                currentTime = formatter.print(lt.plusMinutes(30));
            }

            adapter.notifyDataSetChanged();

        } else {
            currentTime = openDay.trim();
            while (currentTime.compareTo(endTime) <= 0) {
                listTimeSession.add(currentTime);

                LocalTime lt = formatter.parseLocalTime(currentTime);
                currentTime = formatter.print(lt.plusMinutes(30));
            }

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reservation:
                if(!spnListSession.getSelectedItem().toString().equals(getString(R.string.empty_session)) && timeSession!=null) {

                    final UserReservation userReservation = new UserReservation();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy", Locale.ENGLISH);
                    String currentTime = format.format(calendar.getTime());
                    userReservation.setPhone(new MySharePreference(getActivity()).getPhoneLogin());
                    userReservation.setTimeReservation(currentTime);

                    RestaurantDetail restaurant = new RestaurantDetail();
                    restaurant.setCity(restaurantDetail.getCity());
                    restaurant.setDistrict(restaurantDetail.getDistrict());
                    restaurant.setId(restaurantDetail.getId());

                    userReservation.getLisReservationDetail().add(restaurant);

                    ReservationDetail reservationDetail = new ReservationDetail();
                    reservationDetail.setSession(spnListSession.getSelectedItem().toString());
                    reservationDetail.setTable(spnListTable.getSelectedItem().toString());
                    reservationDetail.setTime(timeSession);

                    userReservation.getLisReservationDetail().get(0).getListReservations().add(reservationDetail);

                    if (AppUtils.isNetworkAvailable(getActivity())) {
                        Call<Status> reserveRestaurant = Connect.getRetrofit().reserveRestaurant(userReservation);

                        baseProgress.showProgressLoading(getActivity());
                        reserveRestaurant.enqueue(new Callback<Status>() {
                            @Override
                            public void onResponse(Call<Status> call, Response<Status> response) {
                                baseProgress.hideProgressLoading();
                                if (response.body() != null) {
                                    if (response.body().getRestaurantReservation() != null) {
                                        Intent intent = new Intent(MainActivity.BROADCAST_RESTAURANT_NAME);
                                        intent.putExtra(MainActivity.SEND_BROADCAST_RESTAURANT_DATA, response.body().getRestaurantReservation());
                                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

                                        getActivity().onBackPressed();
                                    } else {
                                        if (response.body().getStatus().equals("true")) {
                                            Toast.makeText(getActivity(), getString(R.string.was_reserve), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), getString(R.string.error_reserve), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Status> call, Throwable t) {
                                baseProgress.hideProgressLoading();
                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
                    }
                }else if(spnListSession.getSelectedItem().toString().equals(getString(R.string.empty_session))){
                    Toast.makeText(getActivity(),getString(R.string.choose_session),Toast.LENGTH_SHORT).show();
                }else if(timeSession==null){
                    Toast.makeText(getActivity(),getString(R.string.choose_time_session),Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
