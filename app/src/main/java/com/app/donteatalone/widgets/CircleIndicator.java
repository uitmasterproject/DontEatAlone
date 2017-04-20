package com.app.donteatalone.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.donteatalone.R;

/**
 * -> Created by LeHoangHan on 4/6/2017.
 */

public class CircleIndicator extends LinearLayout {

    private Context context;
    private LinearLayout llParent;

    public CircleIndicator(Context context) {
        super(context);

        this.context = context;
        initViews();
    }

    @SuppressLint("NewApi")
    public CircleIndicator(Context context, AttributeSet attrs,
                           int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // TODO Auto-generated constructor stub
        this.context = context;
        initViews();
    }

    public CircleIndicator(Context context, AttributeSet attrs,
                           int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        this.context = context;
        initViews();
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        initViews();
    }

    private void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.view_circle_indicator, this);
        }

        // View by ID
        llParent = (LinearLayout) findViewById(R.id.view_circle_indicator_ll_parent);

    }

    public void setSeletedTab(int seleted, int count) {
        llParent.removeAllViews();

        // Correct numbers <=5
        if (count > 5) {
            if (seleted >= count / 5 * 5) {
                count = (count - 1) % 5 + 1;
            } else count = 5;

            if (seleted >= 5) seleted = seleted % 5;
        }

        for (int i = 0; i < count; i++) {
            if (i == seleted) {
                if (i == count - 1) {
                    llParent.addView(getView(true, true));
                } else {
                    llParent.addView(getView(true, false));
                }
            } else {
                if (i == count - 1) {
                    llParent.addView(getView(false, true));
                } else {
                    llParent.addView(getView(false, false));
                }
            }
        }
        llParent.requestLayout();
    }

    private View getView(boolean isSelected, boolean isEndList) {
        ImageView mIvCircle = new ImageView(context);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(7, 7);

        if (!isEndList) {
            mLayoutParams.setMargins(0, 0, 14, 0);
        } else {
            mLayoutParams.setMargins(0, 0, 0, 0);
        }
        mIvCircle.setLayoutParams(mLayoutParams);
        mIvCircle.setAdjustViewBounds(true);

        if (isSelected) mIvCircle.setImageResource(R.drawable.btn_circle_selected);
        else {
            mIvCircle.setImageResource(R.drawable.btn_circle_unselected);
        }
        return mIvCircle;
    }

}
