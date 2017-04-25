package com.app.donteatalone.widgets;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * -> Created by LeHoangHan on 4/24/2017.
 */

@EActivity(R.layout.view_toolbar)
public class Toolbar extends RelativeLayout {
    private static final String TAG = Toolbar.class.getSimpleName();

    @ViewById(R.id.view_toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @ViewById(R.id.view_toolbar_close)
    View toolbarClose;
    @ViewById(R.id.view_toolbar_tv_title)
    TextView tvTitle;

    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void applyCommonUi(AppCompatActivity activity) {
        //Setup toolbar
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    //Update for register ui
    public void applyRegisterUi(AppCompatActivity activity, String title, final RegisterItemClick itemClick) {
        //Setup toolbar
        applyCommonUi(activity);

        tvTitle.setText(title);
        toolbarClose.setVisibility(View.VISIBLE);

        toolbarClose.setOnClickListener(v -> {
            itemClick.toolbarCloseClick();
        });

    }

    public interface RegisterItemClick {
        void toolbarCloseClick();
    }
}
