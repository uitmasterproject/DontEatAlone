package com.app.donteatalone.views.tutorial;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.donteatalone.R;

/**
 * -> Created by LeHoangHan on 4/6/2017.
 */

public class TutorialAdapter extends PagerAdapter {

    private Context context;
    private int[] layouts;

    public TutorialAdapter(Context context){
        this.context = context;
        layouts = new int[] {
            //Set tutorial screen in here
                R.layout.tutorial_screen_step_1,
                R.layout.tutorial_screen_step_2,
                R.layout.tutorial_screen_step_4,
                R.layout.tutorial_screen_step_5
        };
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layouts[position], container, false);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
