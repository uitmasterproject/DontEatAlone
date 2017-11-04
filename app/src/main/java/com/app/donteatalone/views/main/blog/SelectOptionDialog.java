package com.app.donteatalone.views.main.blog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.donteatalone.R;

/**
 * Created by ChomChom on 17-Aug-17
 */

public class SelectOptionDialog {
    private Activity activity;

    public SelectOptionDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(ImageView imageView, final int count, LinearLayout linearLayout){
        Dialog dialog=new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view= ((LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_dialog_activity_blog_write_status_option,null,false);

        TextView txtDeleteImage=(TextView) view.findViewById(R.id.custom_dialog_activity_blog_write_status_option_txt_delete_image);
        TextView txtInsertImage=(TextView) view.findViewById(R.id.custom_dialog_activity_blog_write_status_option_txt_insert_image);
        TextView txtInsertContent=(TextView) view.findViewById(R.id.custom_dialog_activity_blog_write_status_option_txt_insert_content);

        txtDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                imageView.setImageBitmap(null);
                imageView.setVisibility(View.GONE);
            }
        });

        txtInsertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                activity.startActivityForResult(intent, count+3);
            }
        });

        txtInsertContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EditText edtAddContentBlog = new EditText(activity);
                edtAddContentBlog.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                edtAddContentBlog.setBackgroundResource(R.drawable.design_blog_edt_write_status);
                edtAddContentBlog.requestFocus();
                edtAddContentBlog.setTextColor(Color.BLACK);

                linearLayout.addView(edtAddContentBlog,count+1);

                for(int count=0;count<linearLayout.getChildCount();count++) {
                    if (linearLayout.getChildAt(count).getClass().getName().equals("android.widget.ImageView")) {
                        final ImageView dynamicImage = (ImageView) linearLayout.getChildAt(count);
                        int finalCount = count;
                        dynamicImage.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                new SelectOptionDialog(activity).showDialog(dynamicImage, finalCount,linearLayout);
                                return false;
                            }
                        });
                    }
                }
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }
}
