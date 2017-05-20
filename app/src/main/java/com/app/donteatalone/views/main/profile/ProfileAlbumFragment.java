package com.app.donteatalone.views.main.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.donteatalone.R;

/**
 * Created by Le Hoang Han on 5/19/2017.
 */

public class ProfileAlbumFragment extends Fragment {
    private View viewGroup;

    public static ProfileAlbumFragment newInstance() {
        ProfileAlbumFragment fragment = new ProfileAlbumFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_profile_album, null);
        return viewGroup;
    }
}
