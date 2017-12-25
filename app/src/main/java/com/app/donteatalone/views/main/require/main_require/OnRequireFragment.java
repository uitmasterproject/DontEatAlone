package com.app.donteatalone.views.main.require.main_require;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.app.donteatalone.R;
import com.app.donteatalone.model.AccordantUser;
import com.app.donteatalone.model.CustomSocket;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.MainActivity;
import com.app.donteatalone.views.main.require.main_require.on_require.AccordantUserAdapter;
import com.app.donteatalone.views.main.require.main_require.on_require.CustomDialogInfoAccordantUser;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * Created by ChomChom on 5/8/2017
 */

public class OnRequireFragment extends Fragment {

    public static Socket socketIO;
    private View viewGroup;
    private String phone;
    private RecyclerView rcvListAccordantUser;
    private AccordantUserAdapter accordantUserAdapter;
    private ArrayList<AccordantUser> listAccordantUser;
    private ViewPager viewPager;
    private MySharePreference infoRequireSharePreference;

    private SwipeRefreshLayout srlResultsList;

    public static OnRequireFragment newInstance(ViewPager viewPager) {
        OnRequireFragment fragment = new OnRequireFragment();
        fragment.setViewPager(viewPager);
        return fragment;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_require_on, container,false);
        phone = new MySharePreference(getActivity()).getPhoneLogin();

        Log.e("activity On", getActivity()+"++++++++++++++++++++++++++++++++++++++++");

        return viewGroup;
    }

    @Override
    public void onStart() {
        Log.e("activity On on Start", getActivity()+"++++++++++++++++++++++++++++++++++++++++");
        super.onStart();
        if (socketIO == null) {
            try {
                initSocket();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        init();

        socketIO.emit("UserOnline", getInforRequire());
        listenComfortableUserOnline();

        listenInvitation();
        listenResultInvitation();
        swipeRefreshLayout();
    }

    private void init() {
        infoRequireSharePreference = new MySharePreference(getActivity(), phone);

        listAccordantUser = new ArrayList<AccordantUser>();

        rcvListAccordantUser = (RecyclerView) viewGroup.findViewById(R.id.fragment_require_on_rcv_list_accordant_user);
        LinearLayoutManager llManager = new LinearLayoutManager(getContext());
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvListAccordantUser.setLayoutManager(llManager);

        accordantUserAdapter = new AccordantUserAdapter(listAccordantUser, getContext(), socketIO);
        rcvListAccordantUser.setAdapter(accordantUserAdapter);

        srlResultsList = (SwipeRefreshLayout) viewGroup.findViewById(R.id.fragment_require_on_srl_results);
    }

    private void initSocket() throws URISyntaxException {
        socketIO = new CustomSocket().getmSocket();
        socketIO.connect();
    }

    private void swipeRefreshLayout() {
        srlResultsList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listAccordantUser.clear();
                accordantUserAdapter.notifyDataSetChanged();
                socketIO.emit("UserOnline", getInforRequire());

                // Stop refresh animation
                srlResultsList.setRefreshing(false);
            }
        });
    }

    private void listenComfortableUserOnline() {
        socketIO.on("userLike", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject data = (JSONObject) args[0];
                            Log.e("userLike", "user like +++++++++++++++++++++++++++++++++++++++++");

                            try {
                                Log.e("userLike", data.getJSONArray("listUserLike").length() + "+++++++++++++++++++");
                                for (int i = 0; i < data.getJSONArray("listUserLike").length(); i++) {

                                    listAccordantUser.add(new AccordantUser(data.getJSONArray("listUserLike").getJSONObject(i).getString("accordantUser"),
                                            data.getJSONArray("listUserLike").getJSONObject(i).getString("avatar"),
                                            data.getJSONArray("listUserLike").getJSONObject(i).getString("fullName"),
                                            Integer.parseInt(data.getJSONArray("listUserLike").getJSONObject(i).getString("percent")),
                                            data.getJSONArray("listUserLike").getJSONObject(i).getString("gender"),
                                            Integer.parseInt(data.getJSONArray("listUserLike").getJSONObject(i).getString("age")),
                                            data.getJSONArray("listUserLike").getJSONObject(i).getString("address"),
                                            data.getJSONArray("listUserLike").getJSONObject(i).getString("latlng"),
                                            data.getJSONArray("listUserLike").getJSONObject(i).getString("character")));
                                }
                                accordantUserAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private void listenInvitation() {
        socketIO.on("sendInvite", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("sendInvite", "sendInvite +++++++++++++++++++++++++++++++++++++++++");
                        try {
                            if (((JSONObject) args[0]).getString("phoneInvited").equals(phone)) {
                                JSONObject data = (JSONObject) args[0];
                                if (getActivity() != null) {
                                    Dialog dialog = new Dialog(getActivity(), R.style.MyDialogTheme);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.custom_dialog_require_on_info_accordant_user);
                                    CustomDialogInfoAccordantUser customDialog = new CustomDialogInfoAccordantUser(dialog, getContext(), data, socketIO,
                                            new MySharePreference(getActivity()).getFullNameLogin(), viewPager);
                                    customDialog.setDefaultValue();
                                    dialog.show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void listenResultInvitation() {
        socketIO.on("resultInvitation", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (((JSONObject) args[0]).getString("phoneReceiver").equals(phone)) {
                                //update title + 1 in notification

                                Intent intent = new Intent(MainActivity.BROADCAST_NAME);
                                intent.putExtra(MainActivity.SEND_BROADCAST_TITLE, 1);
                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private String getInforRequire() {
        String require;
        require = phone + "|" + setDefaultValue(infoRequireSharePreference.getGenderRequire(), "all") + "|" +
                setDefaultValue(infoRequireSharePreference.getAgeMinRequire(), "18") + "|" +
                setDefaultValue(infoRequireSharePreference.getAgeMaxRequire(), "25") + "|" +
                setDefaultValue(infoRequireSharePreference.getAddressRequire(), StringEscapeUtils.escapeJava(getString(R.string.default_address))) + "|" +
                setDefaultValue(infoRequireSharePreference.getLatLngAddressRequire(), getString(R.string.default_lat_lng)) + "|" +
                setDefaultValue(infoRequireSharePreference.getTargetFoodRequire(), "") + "|" +
                setDefaultValue(infoRequireSharePreference.getTargetCharacterRequire(), "") + "|" +
                setDefaultValue(infoRequireSharePreference.getTargetStyleRequire(), "") ;

        return require;
    }

    @Override
    public void onStop() {
        Log.e("activity onStop", getActivity()+"++++++++++++++++++++++++++++++++++++++++");
        super.onStop();
        socketIO.disconnect();
        socketIO = null;
    }

    @Override
    public void onDestroy() {
        listAccordantUser.clear();
        Log.e("activity onDestroy", getActivity()+"++++++++++++++++++++++++++++++++++++++++");
        super.onDestroy();
    }


    private String setDefaultValue(String value, String defaultValue) {
        if (TextUtils.isEmpty(value))
            return defaultValue;
        else
            return value;
    }
}
