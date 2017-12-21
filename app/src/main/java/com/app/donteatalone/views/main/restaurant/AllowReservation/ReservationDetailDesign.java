package com.app.donteatalone.views.main.restaurant.AllowReservation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.base.OnRecyclerItemClickListener;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.ReservationDetail;
import com.app.donteatalone.model.RestaurantDetail;

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

public class ReservationDetailDesign extends Fragment {

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
    private String timeSession;

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
                Toast.makeText(getActivity(),resource+"",Toast.LENGTH_SHORT).show();
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

                            if (TextUtils.isEmpty(reservationDetail.getSession())) {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spnListSession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                String currentTime = format.format(calendar.getTime());

                String openDay = restaurantDetail.getOpenDay();
                if (spnListSession.getSelectedItem().toString().equals(getString(R.string.lunch_session))) {
                    if (!TextUtils.isEmpty(openDay)&&openDay.contains(" | ")){
                        String [] timeLine=openDay.split("|");
                        if(timeLine[0].trim().contains(" - ")){
                            setTimeSession(currentTime,timeLine[0].split(" - ")[1].trim(),timeLine[0].split(" - ")[1].trim(),format,listTimeSession);
                        }
                    }else {
                        if(!TextUtils.isEmpty(openDay)&&openDay.contains(" - ")) {
                            setTimeSession(currentTime, openDay.split(" - ")[0].trim(), endLunchTime, format, listTimeSession);
                        }

                    }
                }else if (spnListSession.getSelectedItem().toString().equals(getString(R.string.evening_session))){
                    if (!TextUtils.isEmpty(openDay)&&openDay.contains(" | ")){
                        String [] timeLine=openDay.split("|");
                        if(timeLine[0].trim().contains(" - ")){
                            setTimeSession(currentTime,timeLine[1].split(" - ")[0].trim(),timeLine[1].split(" - ")[1].trim(),format,listTimeSession);
                        }
                    }else {
                        if(!TextUtils.isEmpty(openDay)&&openDay.contains(" - ")) {
                            setTimeSession(currentTime, startEveningTime, openDay.split(" - ")[1].trim(), format, listTimeSession);
                        }

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public String convertMinute(String time){
            if(time.split(":")[1].compareTo("00")>=0 && time.split(":")[1].compareTo("30")<=0){
                return time.split(":")[0]+":"+"30";
            }else if(time.split(":")[1].compareTo("30")>0){
                return (Integer.parseInt(time.split(":")[0])+1)+":"+"00";
            }
        return (Integer.parseInt(time.split(":")[0])+1)+":"+"00";
    }

    public void setTimeSession(String currentTime,String openDay, String endTime, SimpleDateFormat format, ArrayList<String> listTimeSession){
        listTimeSession.clear();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm");
            if(openDay.trim().compareTo(currentTime)<0){
                currentTime = convertMinute(currentTime);

                while (currentTime.compareTo(endTime)<=0){
                    listTimeSession.add(currentTime);

                    LocalTime lt = formatter.parseLocalTime(currentTime);
                    currentTime= formatter.print(lt.plusMinutes(30));
                }

                adapter.notifyDataSetChanged();

            }else {
                currentTime = openDay.trim();
                while (currentTime.compareTo(endTime)<=0){
                    listTimeSession.add(currentTime);

                    LocalTime lt = formatter.parseLocalTime(currentTime);
                    currentTime= formatter.print(lt.plusMinutes(30));
                }

                adapter.notifyDataSetChanged();
            }
    }
}
