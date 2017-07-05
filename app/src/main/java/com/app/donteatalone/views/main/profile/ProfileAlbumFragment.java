package com.app.donteatalone.views.main.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Le Hoang Han on 5/19/2017.
 */

public class ProfileAlbumFragment extends Fragment {
    private View viewGroup;
    private GridView gridview;
    private ProfileAlbumAdapter adapter;
    private ArrayList<String> listImage;

    public static ProfileAlbumFragment newInstance() {
        ProfileAlbumFragment fragment = new ProfileAlbumFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_profile_album, null);
        init();
        setValueGridView();
        return viewGroup;
    }

    private void init(){
        gridview = (GridView) viewGroup.findViewById(R.id.fragment_profile_album_gv_image);
        listImage=new ArrayList<>();
        adapter=new ProfileAlbumAdapter(getContext(),listImage);
        gridview.setAdapter(adapter);
    }

    private void setValueGridView(){
        Connect connect=new Connect();
        Call<ArrayList<String>> getAllImage=connect.getRetrofit().getAllImage(storeReference("phoneLogin"));
        getAllImage.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                for (String temp:response.body()) {
                    listImage.add(temp);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });
    }

    private String storeReference(String str) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("account", MODE_PRIVATE);
        String avatar = sharedPreferences.getString(str, "");
        return avatar;
    }
}
