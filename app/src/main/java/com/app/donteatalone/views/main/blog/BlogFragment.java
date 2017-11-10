package com.app.donteatalone.views.main.blog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.ImageProcessor;
import com.app.donteatalone.utils.MySharePreference;

/**
 * Created by ChomChom on 4/13/2017
 */

public class BlogFragment extends Fragment {

    private View viewGroup;
    private Button btnStatus;
    private ImageView imgAvatar;
    private MySharePreference mySharePreference;


    public static BlogFragment newInstance() {
        return new BlogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_blog, container,false);
        init();
        setimgAvatar();
        GetDatafromDB getDatafromDB = new GetDatafromDB(getActivity(), viewGroup, imgAvatar);
        getDatafromDB.execute(mySharePreference.getPhoneLogin());
        clickButtonbtnStatus();
        return viewGroup;
    }

    private void init() {
        btnStatus = (Button) viewGroup.findViewById(R.id.fragment_blog_edt_status);
        imgAvatar = (ImageView) viewGroup.findViewById(R.id.fragment_blog_avatar);
        mySharePreference = new MySharePreference(getActivity());
    }

    private void clickButtonbtnStatus() {
        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StatusActivity.class);
                startActivity(intent);
                //getActivity().overridePendingTransition(R.animator.animator_bottom_up, R.animator.stay);
            }
        });
    }

    private void setimgAvatar() {
        imgAvatar.setImageBitmap(ImageProcessor.decodeBitmap(mySharePreference.getAvatarLogin()));
    }
}
