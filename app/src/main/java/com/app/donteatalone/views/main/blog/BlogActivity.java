package com.app.donteatalone.views.main.blog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoNotification;
import com.app.donteatalone.profile.ProfifeFragment;
import com.app.donteatalone.views.main.notification.CustomNotificationAdapter;
import com.app.donteatalone.views.main.notification.NotificationFragment;
import com.app.donteatalone.views.main.require.RequireFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.donteatalone.views.main.notification.NotificationFragment.rcvInfoNotification;
import static java.security.AccessController.getContext;

public class BlogActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private FragmentAdapter mFragmentAdapter;
    public  ViewPager viewPager;
    public static TextView txtNotification;
    public View view;
    public LinearLayout llContainer;

    private int srcIcon[]=new int[]{R.drawable.ic_blog,R.drawable.ic_notification,R.drawable.ic_profile,R.drawable.ic_restaurant,R.drawable.ic_require};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        initNotification();

        viewPager=(ViewPager)findViewById(R.id.activity_blog_viewpager);
        setViewPager(viewPager);

        tabLayout=(TabLayout) findViewById(R.id.activity_blog_tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        if(getIntent().getStringExtra("viewProfile")!=null){
            viewPager.setCurrentItem(2);
        }
    }

    private void initNotification(){
        view= LayoutInflater.from(BlogActivity.this).inflate(R.layout.custom_tab_notification,null);
        txtNotification=(TextView) view.findViewById(R.id.custom_tab_notification_txt_count);
        llContainer=(LinearLayout) view.findViewById(R.id.custom_tab_notification_ll_container);
        llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNotification.setText(0+"");
                viewPager.setCurrentItem(1);
                setNotification();
            }
        });
    }

    public void setNotification(){
        Connect connect=new Connect();
        Log.e("listInfoNotification","notification");
        final ArrayList<InfoNotification> listInfoNotification=new ArrayList();
        final CustomNotificationAdapter adapter=new CustomNotificationAdapter(listInfoNotification,BlogActivity.this,getInfointoSharedPreferences("phoneLogin"));
        Call<ArrayList<InfoNotification>> getInfoNotification=connect.getRetrofit().getNotification(getInfointoSharedPreferences("phoneLogin"));
        getInfoNotification.enqueue(new Callback<ArrayList<InfoNotification>>() {
            @Override
            public void onResponse(Call<ArrayList<InfoNotification>> call, Response<ArrayList<InfoNotification>> response) {
                for (InfoNotification element:response.body()) {
                    InfoNotification info=new InfoNotification(element.getUserSend(),element.getNameSend(),element.getTimeSend(),
                            element.getDate(),element.getTime(),element.getPlace(),element.getStatus(),element.getRead(),element.getSeen());
                    listInfoNotification.add(info);
                }
                Collections.reverse(listInfoNotification);
                rcvInfoNotification.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<InfoNotification>> call, Throwable t) {
                Log.e("listInfoNotification2",t.toString()+"");
            }
        });
    }

    private void setViewPager(ViewPager viewPager) {
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mFragmentAdapter.mFragmentList.add(RequireFragment.newInstance());
        mFragmentAdapter.mFragmentList.add(NotificationFragment.newInstance());
        mFragmentAdapter.mFragmentList.add(BlogFragment.newInstance());
        mFragmentAdapter.mFragmentList.add(ProfifeFragment.newInstance());
        mFragmentAdapter.mFragmentList.add(ProfifeFragment.newInstance());

        viewPager.setAdapter(mFragmentAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==1){
                    setNotification();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(srcIcon[0]);
        tabLayout.getTabAt(1).setCustomView(view);
        tabLayout.getTabAt(2).setIcon(srcIcon[2]);
        tabLayout.getTabAt(3).setIcon(srcIcon[3]);
        tabLayout.getTabAt(4).setIcon(srcIcon[4]);
    }

    public class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

    private String getInfointoSharedPreferences(String str){
        String data="";
        if(getContext()!=null) {
            SharedPreferences pre = getSharedPreferences("account", MODE_PRIVATE);
            data = pre.getString(str, "");
        }
        return data;
    }

}