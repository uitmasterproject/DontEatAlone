package com.app.donteatalone.views.main.blog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.views.main.MainActivity;
import com.app.donteatalone.views.main.require.main_require.on_require.ProfileAccordantUser;

/**
 * Created by ChomChom on 19-Aug-17
 */

public class DetailBlogActivity extends AppCompatActivity {
    private InfoBlog infoBlog;
    private String own;
    private TextView txtTitle;
    private TextView txtFeel;
    private ImageView imgFeel;
    private TextView txtLimit;
    private TextView txtDate;
    private RelativeLayout rlCancel;
    private RelativeLayout rlEdit;
    private LinearLayout llContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getParcelableExtra("infoBlog") != null) {
            infoBlog = getIntent().getParcelableExtra("infoBlog");
        }
        own = getIntent().getStringExtra("own");
        setContentView(R.layout.activity_detail_blog);
        init();
        setData();
        clickBtnCancel();
        clickBtnEdit();
    }

    private void init() {
        txtTitle = (TextView) findViewById(R.id.activity_detail_blog_txt_title);
        txtFeel = (TextView) findViewById(R.id.activity_detail_blog_txt_feel);
        imgFeel = (ImageView) findViewById(R.id.activity_detail_blog_img_feel);
        txtLimit = (TextView) findViewById(R.id.activity_detail_blog_txt_limit);
        txtDate = (TextView) findViewById(R.id.activity_detail_blog_txt_date);
        rlCancel = (RelativeLayout) findViewById(R.id.activity_detail_blog_btn_cancel);
        rlEdit = (RelativeLayout) findViewById(R.id.activity_detail_blog_btn_edit);
        llContainer = (LinearLayout) findViewById(R.id.activity_detail_blog_ll_container);
        llContainer.removeAllViews();
        if (own.equals("ownBlogFragmment") == true || own.equals("ownViewProfile")) {
        } else {
            rlEdit.setVisibility(View.INVISIBLE);
        }
    }

    private void setData() {
        txtTitle.setText(infoBlog.getTitle());
        imgFeel.setImageResource(defineIconforFeeling(infoBlog.getFeeling()));
        txtFeel.setText(infoBlog.getFeeling());
        txtLimit.setText(infoBlog.getLimit());
        txtDate.setText(infoBlog.getDate());
        setValueContent();

    }

    private void clickBtnCancel() {
        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (own.equals("ownBlogFragmment") == true || own.equals("ownViewProfile") == true) {
                    intent = new Intent(DetailBlogActivity.this, MainActivity.class);
                    if (own.equals("ownViewProfile") == true)
                        intent.putExtra("viewProfile", "viewProfile");
                    if (own.equals("ownBlogFragmment") == true)
                        intent.putExtra("viewProfile", "blogFragmment");
                } else {
                    intent = new Intent(DetailBlogActivity.this, ProfileAccordantUser.class);
                    intent.putExtra("PhoneAccordantUser", own);
                }
                startActivity(intent);
            }
        });
    }

    private void clickBtnEdit() {
        rlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailBlogActivity.this, StatusActivity.class);
                intent.putExtra("infoBlog", infoBlog);
                startActivity(intent);
            }
        });
    }

    private void setValueContent() {
        String str = infoBlog.getInfoStatus();
        while (str.length() != 0) {
            if (str.startsWith("<text>") == true) {
                TextView txtText = new TextView(this);
                int index = str.indexOf("</text>");
                txtText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                txtText.setTextColor(getResources().getColor(R.color.black));
                txtText.setText(str.substring(6, index));
                llContainer.addView(txtText);
                str = str.substring(index + 7);
            } else if (str.startsWith("<image>") == true) {
                ImageView imgImage = new ImageView(this);
                int index = str.indexOf("</image>");
                Bitmap bitmap = decodeBitmap(str.substring(7, index));
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

    private Bitmap decodeBitmap(String avatar) {
        Bitmap bitmap = null;
        if (avatar.equals("") != true) {
            try {
                byte[] encodeByte = Base64.decode(avatar, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return bitmap;
    }

    private int defineIconforFeeling(String feeling) {
        int icon = 0;
        if (feeling.equals("feeling đáng yêu") == true) {
            icon = R.drawable.ic_felling_cute;
            return icon;
        } else if (feeling.equals("feeling thú vị") == true) {
            icon = R.drawable.ic_felling_exciting;
            return icon;
        } else if (feeling.equals("feeling vui vẻ") == true) {
            icon = R.drawable.ic_felling_smile;
            return icon;
        } else if (feeling.equals("feeling buồn") == true) {
            icon = R.drawable.ic_felling_sad;
            return icon;
        }
        return icon;
    }

}
