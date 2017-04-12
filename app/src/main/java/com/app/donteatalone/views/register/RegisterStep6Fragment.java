package com.app.donteatalone.views.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.app.donteatalone.R;
import com.app.donteatalone.model.Hobby;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ChomChom on 4/7/2017.
 */

public class RegisterStep6Fragment extends Fragment {
    private AutoCompleteTextView actvHobby;
    private ArrayList<String> headerHobby;
    private ArrayList<Hobby> hobbies;
    private Button btnNextStep;
    private View viewGroup;
    private ViewPager _mViewPager;

    public static Fragment newInstance(Context context) {
        RegisterStep6Fragment f = new RegisterStep6Fragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup=inflater.inflate(R.layout.fragment_register_step6,null);
        init();
        setActvHobby();
        clickButtonNextStep();
        return viewGroup;
    }

    private void init(){
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        actvHobby=(AutoCompleteTextView) viewGroup.findViewById(R.id.fragment_register_step6_actv_hobby);
        btnNextStep=(Button) viewGroup.findViewById(R.id.fragment_register_step6_btn_next);
    }

    private void setActvHobby(){
        clonebdHobby();
        CustomAdapterCompleteTextView customAdapterCompleteTextView=new CustomAdapterCompleteTextView(this.getContext(), android.R.layout.simple_dropdown_item_1line,headerHobby,hobbies,actvHobby);
        actvHobby.setAdapter(customAdapterCompleteTextView);

    }

    private void clickButtonNextStep(){
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterStep1Fragment.userName.setHobby(actvHobby.getText().toString());
                saveReference();
                _mViewPager.setCurrentItem(6,true);
            }
        });
    }

    private void clonebdHobby(){
        headerHobby=new ArrayList<>();
        headerHobby.add("Món ăn");
        headerHobby.add("Tính cách");
        headerHobby.add("Phong cách");

        hobbies=new ArrayList<>();
        hobbies.add(new Hobby("Món ăn","",true));
        hobbies.add(new Hobby("Món ăn","các món từ gà, gà rán, gà nướng, gà quay...",false));
        hobbies.add(new Hobby("Món ăn","các loại lẩu",false));
        hobbies.add(new Hobby("Món ăn","đồ xiên que",false));
        hobbies.add(new Hobby("Món ăn","đồ ăn liền",false));
        hobbies.add(new Hobby("Món ăn","các món ngọt",false));
        hobbies.add(new Hobby("Món ăn","phở, bún, bánh canh cua...",false));

        hobbies.add(new Hobby("Tính cách","",true));
        hobbies.add(new Hobby("Tính cách","vui vẻ",false));
        hobbies.add(new Hobby("Tính cách","trầm tĩnh",false));
        hobbies.add(new Hobby("Tính cách","hóm hĩnh",false));
        hobbies.add(new Hobby("Tính cách","thoải mái",false));

        hobbies.add(new Hobby("Phong cách","",true));
        hobbies.add(new Hobby("Phong cách","tự do",false));
        hobbies.add(new Hobby("Phong cách","quái dị",false));
        hobbies.add(new Hobby("Phong cách","trưởng thành",false));
    }

    private void saveReference(){
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("hobby",actvHobby.getText().toString());
        editor.commit();
    }
}
