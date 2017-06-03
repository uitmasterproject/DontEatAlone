package com.app.donteatalone.views.main.require.main_require;

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
import android.widget.Button;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.model.AccordantUser;
import com.app.donteatalone.model.CustomSocket;
import com.app.donteatalone.views.main.require.main_require.on_require.AccordantUserAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.internal.zzir.runOnUiThread;

/**
 * Created by ChomChom on 5/8/2017.
 */

public class OnRequireFragment extends Fragment {

    private View viewGroup;
    private Socket socketIO;
    private String phone;
    private Button btnLoad;
    private RecyclerView rcvListAccordantUser;
    private AccordantUserAdapter accordantUserAdapter;
    private ArrayList<AccordantUser> listAccordantUser;

    public static OnRequireFragment newInstance() {

        OnRequireFragment fragment = new OnRequireFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_require_on, null);
        phone=getInfointoSharedPreferences("phoneLogin");
        try {
            initSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        init();
        socketIO.emit("UserOnline",getInforRequire());
        listenComfortableUserOnline();
        setClickbtnLoad();
        listenInvitation();
        return viewGroup;
    }

    private void init(){
        btnLoad=(Button) viewGroup.findViewById(R.id.fragment_require_on_btn_load);
        listAccordantUser=new ArrayList<AccordantUser>();
        rcvListAccordantUser=(RecyclerView) viewGroup.findViewById(R.id.fragment_require_on_rcv_list_accordant_user);
        LinearLayoutManager llManager=new LinearLayoutManager(getContext());
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvListAccordantUser.setLayoutManager(llManager);
        accordantUserAdapter=new AccordantUserAdapter(listAccordantUser,getContext(),socketIO,phone);
        rcvListAccordantUser.setAdapter(accordantUserAdapter);
    }

    private void initSocket() throws URISyntaxException {
        CustomSocket socket=new CustomSocket();
        socketIO=socket.getmSocket();
        socketIO.connect();
    }

    private void listenComfortableUserOnline(){
        socketIO.on("userLike", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        listAccordantUser.clear();
                        try {
                            for(int i=0;i<data.getJSONArray("listUserLike").length();i++){
                                Log.e("data",data.getJSONArray("listUserLike").getJSONObject(i)+"");
                                listAccordantUser.add(new AccordantUser(data.getJSONArray("listUserLike").getJSONObject(i).getString("accordantUser"),
                                        data.getJSONArray("listUserLike").getJSONObject(i).getString("avatar"),
                                        data.getJSONArray("listUserLike").getJSONObject(i).getString("fullName"),
                                        Integer.parseInt(data.getJSONArray("listUserLike").getJSONObject(i).getString("percent")),
                                        data.getJSONArray("listUserLike").getJSONObject(i).getString("gender"),
                                        Integer.parseInt(data.getJSONArray("listUserLike").getJSONObject(i).getString("age")),
                                        data.getJSONArray("listUserLike").getJSONObject(i).getString("address"),
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

    private void listenInvitation(){
        socketIO.on("sendInvite", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data=(JSONObject) args[0];
                        Log.e("dataListen",data+"");
                        Toast.makeText(getContext(),"listen",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setClickbtnLoad(){
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketIO.emit("UserOnline",getInforRequire());
            }
        });
    }

    private String getInforRequire(){
        String require;
        require=getInfointoSharedPreferences("phoneLogin")+"|"+getRequireintoSharedPreferences("genderRequire")+"|"+getRequireintoSharedPreferences("ageminRequire")+"|"+
                getRequireintoSharedPreferences("agemaxRequire")+"|"+getRequireintoSharedPreferences("addressRequire")+"|"+
                getRequireintoSharedPreferences("latlngaddressRequire")+"|"+ getRequireintoSharedPreferences("hobbyFoodRequire")+"|"+
                getRequireintoSharedPreferences("hobbyCharacterRequire")+"|"+getRequireintoSharedPreferences("hobbyStyleRequire");
        return require;
    }

    private String getInfointoSharedPreferences(String str){
        SharedPreferences pre=getContext().getSharedPreferences ("account",MODE_PRIVATE);
        String data=pre.getString(str,"");
        return data;
    }
    private String getRequireintoSharedPreferences(String str){
        SharedPreferences pre=getContext().getSharedPreferences ("inforRequire"+"_"+phone,MODE_PRIVATE);
        String data=pre.getString(str,"");
        return data;
    }
}
