package com.example.app.donteatalone.register;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.app.donteatalone.R;
public class RegisterStep1Fragment extends Fragment {

    private ViewPager _mViewPager;
    private Button btnNextStep, btnNext, btnFinish;
    private ViewGroup viewGroup;
    private LinearLayout linearLayout;

    public static Fragment newInstance(Context context) {
        RegisterStep1Fragment f = new RegisterStep1Fragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup=(ViewGroup)inflater.inflate(R.layout.fragment_register_step1,null);
        init();
        clickButtonNextStep();
        return viewGroup;
    }

    public void init(){
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        btnNext=(Button) getActivity().findViewById(R.id.activity_register_btn_next);
        btnFinish=(Button) getActivity().findViewById(R.id.activity_register_btn_finish);
        btnNextStep=(Button) viewGroup.findViewById(R.id.fragment_register_step1_btn_next);
    }

    private void clickButtonNextStep(){
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButton(btnFinish, R.drawable.design_button_selected, 20, 20);
                setButton(btnNext,R.drawable.design_button_default , 7, 7);
                _mViewPager.setCurrentItem(1,true);
//                RegisterStep2Fragment newFragment = new RegisterStep2Fragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.remove(RegisterStep1Fragment.this);
//                transaction.replace(R.id.root_frame, newFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        });
    }


    private void setButton(Button btn,int res,int h, int w){
        btn.setWidth(w);
        btn.setHeight(h);
        btn.setBackgroundResource(res);
    }

}
