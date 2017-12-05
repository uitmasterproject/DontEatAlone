package com.app.donteatalone.views.main.blog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.views.main.require.main_require.on_require.ProfileAccordantUser;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by ChomChom on 19-Aug-17
 */

public class DetailBlogActivity extends AppCompatActivity {
    public static final String ARG_ITEM_BLOG = "ARG_ITEM_BLOG";
    public static final String ARG_OWN_BLOG = "ARG_OWN_BLOG";
    public static final String ARG_BLOG_FRAGMENT = "ARG_BLOG_FRAGMENT";
    public static final String ARG_PHONE_NUMBER = "ARG_PHONE_NUMBER";

    private InfoBlog infoBlog;
    private String own;
    private TextView txtTitle;
    private ImageView imgFeel;
    private TextView txtLimit;
    private TextView txtDate;
    private ImageView imgCancel;
    private ImageView imgEdit;
    private LinearLayout llContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getParcelableExtra(ARG_ITEM_BLOG) != null) {
            infoBlog = getIntent().getParcelableExtra(ARG_ITEM_BLOG);
        }
        own = getIntent().getStringExtra(ARG_OWN_BLOG);
        setContentView(R.layout.activity_detail_blog);
        init();
        setData();
        clickBtnCancel();
        clickBtnEdit();
    }

    private void init() {
        txtTitle = (TextView) findViewById(R.id.activity_detail_blog_txt_title);
        imgFeel = (ImageView) findViewById(R.id.activity_detail_blog_img_feel);
        txtLimit = (TextView) findViewById(R.id.activity_detail_blog_txt_limit);
        txtDate = (TextView) findViewById(R.id.activity_detail_blog_txt_date);
        imgCancel = (ImageView) findViewById(R.id.activity_detail_blog_btn_cancel);
        imgEdit = (ImageView) findViewById(R.id.activity_detail_blog_btn_edit);
        llContainer = (LinearLayout) findViewById(R.id.activity_detail_blog_ll_container);
        llContainer.removeAllViews();
        if (!own.equals(ARG_BLOG_FRAGMENT)) {
            imgEdit.setVisibility(View.INVISIBLE);
        }
    }

    private void setData() {
        txtTitle.setText(StringUtils.capitalize(infoBlog.getTitle()));
        if (infoBlog.getFeeling() == 0) {
            imgFeel.setImageResource(R.drawable.ic_happy);
        } else {
            imgFeel.setImageResource(infoBlog.getFeeling());
        }
        txtLimit.setText(infoBlog.getLimit());
        txtDate.setText("| "+infoBlog.getDate());
        setValueContent();

    }

    private void clickBtnCancel() {
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (own.equals(ARG_BLOG_FRAGMENT)) {
//                    intent = new Intent(DetailBlogActivity.this, MainActivity.class);
//                    intent.putExtra(ARG_FROM_VIEW, ARG_DETAIL_BLOG_ACTIVITY);

                    onBackPressed();
                } else {
                    intent = new Intent(DetailBlogActivity.this, ProfileAccordantUser.class);
                    intent.putExtra(ARG_PHONE_NUMBER, own);
                    startActivity(intent);
                }
            }
        });
    }

    private void clickBtnEdit() {
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailBlogActivity.this, StatusActivity.class);
                intent.putExtra(ARG_ITEM_BLOG, infoBlog);
                startActivity(intent);
            }
        });
    }

    private void setValueContent() {
        String str = infoBlog.getInfoStatus();
        int count = 0;

        while (str.length() != 0) {
            if (str.startsWith("<text>")) {
                TextView txtText = new TextView(this);
                int index = str.indexOf("</text>");
                txtText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                txtText.setTextColor(ContextCompat.getColor(DetailBlogActivity.this, R.color.black));
                txtText.setText(StringUtils.capitalize(str.substring(6, index)));
                txtText.setTextSize(17f);
                llContainer.addView(txtText);
                str = str.substring(index + 7);
            } else if (str.startsWith("<image>")) {
                ImageView imgImage = new ImageView(this);
                int index = str.indexOf("</image>");

                DisplayMetrics screen = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(screen);
                imgImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Picasso.with(DetailBlogActivity.this)
                        .load(infoBlog.getImage().get(count))
                        .into(imgImage);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imgImage.getLayoutParams();
                layoutParams.setMargins(0, 10, 0, 10);
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                imgImage.setLayoutParams(layoutParams);

                llContainer.addView(imgImage);

                str = str.substring(index + 8);

                count += 1;
            }
        }
    }


}
