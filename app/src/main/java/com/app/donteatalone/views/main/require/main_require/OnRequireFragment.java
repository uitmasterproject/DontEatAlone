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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import static com.google.android.gms.internal.zzir.runOnUiThread;

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
        viewGroup = inflater.inflate(R.layout.fragment_require_on, null);
        phone = new MySharePreference(getActivity()).getValue("phoneLogin");
        return viewGroup;
    }

    @Override
    public void onStart() {
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];

                        try {

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
        });
    }

    private void listenInvitation() {
        socketIO.on("sendInvite", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (((JSONObject) args[0]).getString("phoneInvited").equals(phone)) {
                                JSONObject data = (JSONObject) args[0];
                                if (getActivity() != null) {
                                    Dialog dialog = new Dialog(getActivity(), R.style.MyDialogTheme);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.custom_dialog_require_on_info_accordant_user);
                                    CustomDialogInfoAccordantUser customDialog = new CustomDialogInfoAccordantUser(dialog, getContext(), data, socketIO, new MySharePreference(getActivity()).getValue("fullNameLogin"), viewPager);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (((JSONObject) args[0]).getString("phoneReceiver").equals(phone)) {
                                //update title + 1 in notification

                                Intent intent = new Intent(MainActivity.BROADCASTNAME);
                                intent.putExtra(MainActivity.SENDBROADCASTTITLE, 1);
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
        require = phone + "|" + setDefaultValue("genderRequire", "all") + "|" + setDefaultValue("ageMinRequire", "18") + "|" +
                setDefaultValue("ageMaxRequire", "25") + "|" + setDefaultValue("addressRequire", "Cho Ben Thanh, Quan 1, Ho Chi Minh, Vietnam") + "|" +
                setDefaultValue("latlngAddressRequire", "10.771423,106.698471") + "|" + setDefaultValue("targetFoodRequire", "") + "|" +
                setDefaultValue("targetCharacterRequire", "") + "|" + setDefaultValue("targetStyleRequire", "") ;




        return require;
    }

    @Override
    public void onStop() {
        super.onStop();
        socketIO.disconnect();
        socketIO = null;
    }

    private String setDefaultValue(String key, String defaul) {
        if (infoRequireSharePreference.getValue(key).isEmpty())
            return defaul;
        else
            return infoRequireSharePreference.getValue(key).trim();
    }
}
