package com.app.donteatalone.views.main.require.main_require;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.app.donteatalone.R;
import com.app.donteatalone.model.AccordantUser;
import com.app.donteatalone.model.CustomSocket;
import com.app.donteatalone.views.main.require.main_require.on_require.AccordantUserAdapter;
import com.app.donteatalone.views.main.require.main_require.on_require.CustomDialogInfoAccordantUser;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.app.donteatalone.views.main.MainActivity.txtNotification;
import static com.google.android.gms.internal.zzir.runOnUiThread;

/**
 * Created by ChomChom on 5/8/2017
 */

public class OnRequireFragment extends Fragment {

    private View viewGroup;
    public static Socket socketIO;
    private String phone;
    private Button btnLoad;
    private RecyclerView rcvListAccordantUser;
    private AccordantUserAdapter accordantUserAdapter;
    private ArrayList<AccordantUser> listAccordantUser;

    private SwipeRefreshLayout srlResultsList;

    public static OnRequireFragment newInstance() {

        OnRequireFragment fragment = new OnRequireFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_require_on, null);
        phone = getInfointoSharedPreferences("phoneLogin");
        try {
            initSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        init();
        socketIO.emit("UserOnline", getInforRequire());
        listenComfortableUserOnline();
        setClickbtnLoad();
        listenInvitation();
        listenResultInvitation();
        SwipeRefreshLayout();
        return viewGroup;
    }

    private void init() {
        btnLoad = (Button) viewGroup.findViewById(R.id.fragment_require_on_btn_load);
        listAccordantUser = new ArrayList<AccordantUser>();
        rcvListAccordantUser = (RecyclerView) viewGroup.findViewById(R.id.fragment_require_on_rcv_list_accordant_user);
        LinearLayoutManager llManager = new LinearLayoutManager(getContext());
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvListAccordantUser.setLayoutManager(llManager);
        accordantUserAdapter = new AccordantUserAdapter(listAccordantUser, getContext(), socketIO, phone);
        rcvListAccordantUser.setAdapter(accordantUserAdapter);

        srlResultsList = (SwipeRefreshLayout) viewGroup.findViewById(R.id.fragment_require_on_srl_results);
    }

    private void initSocket() throws URISyntaxException {
        CustomSocket socket = new CustomSocket();
        socketIO = socket.getmSocket();
        socketIO.connect();
    }

    private void SwipeRefreshLayout() {
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
                                Log.e("data", data.getJSONArray("listUserLike").getJSONObject(i) + "");
                                Log.e("latlng", data.getJSONArray("listUserLike").getJSONObject(i).getString("latlng"));
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
                            Log.e("size", listAccordantUser.size() + "");
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
                            if (((JSONObject) args[0]).getString("phoneInvited").equals(getInfointoSharedPreferences("phoneLogin")) == true) {
                                JSONObject data = (JSONObject) args[0];
                                Dialog dialog = new Dialog(getContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.custom_dialog_require_on_info_accordant_user);
                                CustomDialogInfoAccordantUser customDialog = new CustomDialogInfoAccordantUser(dialog, getContext(), data, socketIO, getInfointoSharedPreferences("fullnameLogin"));
                                customDialog.setDefaultValue();
                                dialog.show();
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
                            Log.e("sendInvitetext", "text result");
                            if (((JSONObject) args[0]).getString("phoneReceiver").equals(getInfointoSharedPreferences("phoneLogin")) == true) {
                                Log.e("sendInvite", Integer.parseInt(txtNotification.getText().toString()) + 1 + "");
                                txtNotification.setText(Integer.parseInt(txtNotification.getText().toString()) + 1 + "");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    private void setClickbtnLoad() {
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAccordantUser.clear();
                accordantUserAdapter.notifyDataSetChanged();
                socketIO.emit("UserOnline", getInforRequire());
            }
        });
    }

    private String getInforRequire() {
        String require;
        require = getInfointoSharedPreferences("phoneLogin") + "|" + getRequireintoSharedPreferences("genderRequire") + "|" + getRequireintoSharedPreferences("ageminRequire") + "|" +
                getRequireintoSharedPreferences("agemaxRequire") + "|" + getRequireintoSharedPreferences("addressRequire") + "|" +
                getRequireintoSharedPreferences("latlngaddressRequire") + "|" + getRequireintoSharedPreferences("hobbyFoodRequire") + "|" +
                getRequireintoSharedPreferences("hobbyCharacterRequire") + "|" + getRequireintoSharedPreferences("hobbyStyleRequire");
        return require;
    }

    private String getInfointoSharedPreferences(String str) {
        String data = "";
        if (getContext() != null) {
            SharedPreferences pre = getContext().getSharedPreferences("account", MODE_PRIVATE);
            data = pre.getString(str, "");
        }
        return data;
    }

    private String getRequireintoSharedPreferences(String str) {
        SharedPreferences pre = getContext().getSharedPreferences("inforRequire" + "_" + phone, MODE_PRIVATE);
        String data = pre.getString(str, "");
        return data;
    }

    @Override
    public void onStop() {
        super.onStop();
        socketIO.disconnect();
        socketIO = null;
    }
}
