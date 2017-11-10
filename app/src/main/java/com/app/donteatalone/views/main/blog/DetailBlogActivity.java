package com.app.donteatalone.views.main.blog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.utils.ImageProcessor;
import com.app.donteatalone.views.main.MainActivity;
import com.app.donteatalone.views.main.require.main_require.on_require.ProfileAccordantUser;

import static com.app.donteatalone.views.main.MainActivity.ARG_DETAIL_BLOG_ACTIVITY;
import static com.app.donteatalone.views.main.MainActivity.ARG_FROM_VIEW;

/**
 * Created by ChomChom on 19-Aug-17
 */

public class DetailBlogActivity extends AppCompatActivity {
    public static final String ARG_ITEM_BLOG = "ARG_ITEM_BLOG";
    public static final String ARG_OWN_BLOG = "ARG_OWN_BLOG";
    public static final String ARG_BLOG_FRAGMENT = "ARG_BLOG_FRAGMENT";
    public static final String ARG_PHONE_NUMBER="ARG_PHONE_NUMBER";

    private InfoBlog infoBlog;
    private String own;
    private TextView txtTitle;
    private ImageView imgFeel;
    private TextView txtLimit;
    private TextView txtDate;
    private TextView txtCancel;
    private TextView txtEdit;
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
        txtCancel = (TextView) findViewById(R.id.activity_detail_blog_btn_cancel);
        txtEdit = (TextView) findViewById(R.id.activity_detail_blog_btn_edit);
        llContainer = (LinearLayout) findViewById(R.id.activity_detail_blog_ll_container);
        llContainer.removeAllViews();
        if (!own.equals(ARG_BLOG_FRAGMENT)) {
            txtEdit.setVisibility(View.INVISIBLE);
        }
    }

    private void setData() {
        txtTitle.setText(infoBlog.getTitle());
        imgFeel.setImageResource(infoBlog.getFeeling());
        txtLimit.setText(infoBlog.getLimit());
        txtDate.setText(infoBlog.getDate());
        setValueContent();

    }

    private void clickBtnCancel() {
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (own.equals(ARG_BLOG_FRAGMENT)) {
                    intent = new Intent(DetailBlogActivity.this, MainActivity.class);
                    intent.putExtra(ARG_FROM_VIEW, ARG_DETAIL_BLOG_ACTIVITY);
                } else {
                    intent = new Intent(DetailBlogActivity.this, ProfileAccordantUser.class);
                    intent.putExtra(ARG_PHONE_NUMBER, own);
                }
                startActivity(intent);
            }
        });
    }

    private void clickBtnEdit() {
        txtEdit.setOnClickListener(new View.OnClickListener() {
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
        while (str.length() != 0) {
            if (str.startsWith("<text>")) {
                TextView txtText = new TextView(this);
                int index = str.indexOf("</text>");
                txtText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                txtText.setTextColor(ContextCompat.getColor(DetailBlogActivity.this, R.color.black));
                txtText.setText(str.substring(6, index));
                llContainer.addView(txtText);
                str = str.substring(index + 7);
            } else if (str.startsWith("<image>")) {
                ImageView imgImage = new ImageView(this);
                int index = str.indexOf("</image>");
                Bitmap bitmap = ImageProcessor.decodeBitmap(str.substring(7, index));
                imgImage.setLayoutParams(new LinearLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
                imgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imgImage.setImageBitmap(bitmap);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imgImage.getLayoutParams();
                layoutParams.setMargins(10, 10, 20, 10);
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                imgImage.setLayoutParams(layoutParams);

                llContainer.addView(imgImage);

                str = str.substring(index + 8);
            }
        }
    }


}
