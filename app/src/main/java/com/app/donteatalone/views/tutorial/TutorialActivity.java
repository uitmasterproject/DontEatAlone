package com.app.donteatalone.views.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.views.login.LoginActivity;
import com.app.donteatalone.widgets.CircleIndicator;


public class TutorialActivity extends AppCompatActivity implements View.OnClickListener{
    ViewPager viewPager;
    com.app.donteatalone.widgets.CircleIndicator cirIndicator;
    RelativeLayout rlNext;
    TextView tvNext;

    private TutorialAdapter tutorialAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        init();
    }

    void init() {
        viewPager=(ViewPager) findViewById(R.id.activity_tutorial_viewpager);
        cirIndicator = (CircleIndicator) findViewById(R.id.activity_tutorial_circle_indicator);
        rlNext=(RelativeLayout) findViewById(R.id.activity_tutorial_rl_next);
        tvNext=(TextView) findViewById(R.id.activity_tutorial_tv_next);

        rlNext.setOnClickListener(this);

        //Checking first login?
//        checkNextScreen();
        // Making notification bar transparent
        AppUtils.changeStatusBarColor(this);
        //Set adapter
        tutorialAdapter = new TutorialAdapter(this);
        viewPager.setAdapter(tutorialAdapter);
        viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                cirIndicator.setSeletedTab(position, tutorialAdapter.getCount());
                // Change TextView if currentPage is the last page
                if (position == tutorialAdapter.getCount() - 1) {
                    tvNext.setText("Done");
                } else {
                    tvNext.setText("Next");
                }
            }
        });
        cirIndicator.setSeletedTab(0, tutorialAdapter.getCount());
    }

    void rlNextClick() {
        int current = getItem(+1);
        if (current < tutorialAdapter.getCount()) {
            viewPager.setCurrentItem(current);
        } else {
//            PrefManager.getInstance().setTutorialLaunched(false);
            goToLoginActivity();
        }
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(TutorialActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.animator_right_in, R.animator.animator_left_out);
        finish();
    }

//    public void checkNextScreen() {
//        if (!PrefManager.getInstance().isTutorialLaunched()) {
//            goToLoginActivity();
//        }
//    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_tutorial_rl_next:
                rlNextClick();
                break;
        }
    }
}
