package com.app.donteatalone.views.register;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.donteatalone.R;

/**
 * Created by ChomChom on 4/10/2017.
 */

public class RegisterStep7Fragment extends Fragment {
    private View viewGroup;

    public static Fragment newInstance(Context context) {
        RegisterStep7Fragment f = new RegisterStep7Fragment();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup=inflater.inflate(R.layout.fragment_register_step7,null);
        return viewGroup;
    }

}
