package com.app.donteatalone.views.register;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.app.donteatalone.R;

public class RegisterActivity extends FragmentActivity {
    private CustomViewPager _mViewPager;
    private ViewPagerAdapter _adapter;
    private Button btnStep1,btnStep2,btnStep3,btnStep4,btnStep5,btnStep6,btnStep7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.animator.animator_right_in,R.animator.animator_left_out);
        setUpView();
        setTab();
    }

    private void setUpView(){
        _mViewPager = (CustomViewPager) findViewById(R.id.activity_register_viewPager);
        _adapter = new ViewPagerAdapter(getApplicationContext(),getSupportFragmentManager());
        _mViewPager.setAdapter(_adapter);
        _mViewPager.setCurrentItem(0);
        initButton();
    }

    private void initButton(){
        btnStep1=(Button)findViewById(R.id.activity_register_btn_next_step1);
        btnStep2=(Button)findViewById(R.id.activity_register_btn_next_step2);
        btnStep3=(Button)findViewById(R.id.activity_register_btn_next_step3);
        btnStep4=(Button)findViewById(R.id.activity_register_btn_next_step4);
        btnStep5=(Button)findViewById(R.id.activity_register_btn_next_step5);
        btnStep6=(Button)findViewById(R.id.activity_register_btn_next_step6);
        btnStep7=(Button)findViewById(R.id.activity_register_btn_finish);
        setStatusButton(btnStep1,btnStep2,btnStep3,btnStep4,btnStep5,btnStep6,btnStep7);
    }

    private void setButton(Button btn,int res,int h, int w){
        btn.getLayoutParams().width=w;
        btn.getLayoutParams().height=h;
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
                setStatusButton(btnStep1,btnStep2,btnStep3,btnStep4,btnStep5,btnStep6,btnStep7);
                break;
            case 1:
                setStatusButton(btnStep2,btnStep1,btnStep3,btnStep4,btnStep5,btnStep6,btnStep7);
                break;
            case 2:
                setStatusButton(btnStep3,btnStep2,btnStep1,btnStep4,btnStep5,btnStep6,btnStep7);
                break;
            case 3:
                setStatusButton(btnStep4,btnStep2,btnStep3,btnStep1,btnStep5,btnStep6,btnStep7);
                break;
            case 4:
                setStatusButton(btnStep5,btnStep2,btnStep3,btnStep4,btnStep1,btnStep6,btnStep7);
                break;
            case 5:
                setStatusButton(btnStep6,btnStep2,btnStep3,btnStep4,btnStep5,btnStep1,btnStep7);
                break;
            case 6:
                setStatusButton(btnStep7,btnStep2,btnStep3,btnStep4,btnStep5,btnStep6,btnStep1);
                break;
        }
    }

    private void setStatusButton(Button b1,Button b2, Button b3, Button b4, Button b5, Button b6, Button b7){
        setButton(b1, R.drawable.design_button_selected, 10, 10);
        setButton(b2, R.drawable.design_button_default, 10, 10);
        setButton(b3, R.drawable.design_button_default, 10, 10);
        setButton(b4, R.drawable.design_button_default, 10, 10);
        setButton(b5, R.drawable.design_button_default, 10, 10);
        setButton(b6, R.drawable.design_button_default, 10, 10);
        setButton(b7, R.drawable.design_button_default, 10, 10);
    }

}
