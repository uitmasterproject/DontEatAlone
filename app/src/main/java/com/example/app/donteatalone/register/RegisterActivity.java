package com.example.app.donteatalone.register;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.example.app.donteatalone.R;

public class RegisterActivity extends FragmentActivity {
    private ViewPager _mViewPager;
    private ViewPagerAdapter _adapter;
    private Button _btn1,_btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.animator.animator_right_in,R.animator.animator_left_out);
        setUpView();
        setTab();
    }

    private void setUpView(){
        _mViewPager = (ViewPager) findViewById(R.id.activity_register_viewPager);
        _adapter = new ViewPagerAdapter(getApplicationContext(),getSupportFragmentManager());
        _mViewPager.setAdapter(_adapter);
        _mViewPager.setCurrentItem(0);
        initButton();
    }

    private void initButton(){
        _btn1=(Button)findViewById(R.id.activity_register_btn_next);
        _btn2=(Button)findViewById(R.id.activity_register_btn_finish);
        setButton(_btn1,R.drawable.design_button_selected, 20, 20);
        setButton(_btn2, R.drawable.design_button_default, 7, 7);
    }

    private void setButton(Button btn,int res,int h, int w){
        btn.setWidth(w);
        btn.setHeight(h);
        btn.setBackgroundResource(res);
    }

    private void setTab(){
        _mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int position) {}
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                btnAction(position);
            }

        });

    }
    private void btnAction(int action) {
        switch (action) {
            case 0:
                setButton(_btn1, R.drawable.design_button_selected, 20, 20);
                setButton(_btn2, R.drawable.design_button_default, 7, 7);
                break;

            case 1:
                setButton(_btn2, R.drawable.design_button_selected, 20, 20);
                setButton(_btn1,R.drawable.design_button_default , 7, 7);
                break;
        }
    }

}
