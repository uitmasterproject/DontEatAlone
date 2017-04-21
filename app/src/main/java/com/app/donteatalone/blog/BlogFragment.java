package com.app.donteatalone.blog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.app.donteatalone.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ChomChom on 4/13/2017.
 */

public class BlogFragment extends Fragment {

    private View viewGroup;
    private Button btnStatus;
    private ImageView imgAvatar;

    public static BlogFragment newInstance(){

        BlogFragment fragment=new BlogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup=inflater.inflate(R.layout.fragment_blog,null);
        init();
        clickButtonbtnStatus();
        setimgAvatar();
        return viewGroup;
    }

    private void init(){
        btnStatus=(Button) viewGroup.findViewById(R.id.fragment_blog_edt_status);
        imgAvatar=(ImageView) viewGroup.findViewById(R.id.fragment_blog_avatar);
    }

    private void clickButtonbtnStatus(){
        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),StatusActivity.class);
                startActivity(intent);
                //getActivity().overridePendingTransition(R.animator.animator_bottom_up, R.animator.stay);
            }
        });
    }

    private void setimgAvatar(){
        imgAvatar.setImageBitmap(decodeBitmap());
    }

    private String storeReference(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("account",MODE_PRIVATE);
        Boolean bchk=sharedPreferences.getBoolean("checked", false);
        String avatar="";
        if(bchk==false)
        {
            avatar=sharedPreferences.getString("avatarLogin", "");

        }
        return avatar;
    }


    private Bitmap decodeBitmap(){
        String avatar = storeReference();
        Bitmap bitmap=null;
        if(avatar.equals("")!=true) {
            try {
                byte[] encodeByte = Base64.decode(avatar, Base64.DEFAULT);
                 bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                Log.e("bitmap",bitmap.getWidth()+"-----------"+bitmap.getHeight()+"------------");
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return bitmap;
    }
}
