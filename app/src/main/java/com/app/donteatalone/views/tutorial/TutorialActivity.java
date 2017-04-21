package com.app.donteatalone.views.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseActivity;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.PrefManager;
import com.app.donteatalone.views.login.LoginActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_tutorial)
public class TutorialActivity extends BaseActivity {

    @ViewById(R.id.activity_tutorial_viewpager)
    ViewPager viewPager;
    @ViewById(R.id.activity_tutorial_circle_indicator)
    com.app.donteatalone.widgets.CircleIndicator cirIndicator;
    @ViewById(R.id.activity_tutorial_rl_next)
    RelativeLayout rlNext;
    @ViewById(R.id.activity_tutorial_tv_next)
    TextView tvNext;

    private TutorialAdapter tutorialAdapter;

    @AfterViews
    void init() {
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

    @Click(R.id.activity_tutorial_rl_next)
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
}
