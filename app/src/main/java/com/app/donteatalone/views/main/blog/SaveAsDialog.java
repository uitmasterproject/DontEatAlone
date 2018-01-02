package com.app.donteatalone.views.main.blog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 01-Nov-17
 */

public class SaveAsDialog implements View.OnClickListener {
    private Activity activity;
    private Dialog dialog;
    private InfoBlog infoBlog;
    private EditText edtNameBlog;
    private BaseProgress baseProgress;
    private MySharePreference mySharePreference;
    private boolean isEdit;

    public SaveAsDialog(Activity activity, InfoBlog infoBlog, boolean isEdit) {
        this.activity = activity;
        this.infoBlog = infoBlog;
        this.dialog = new Dialog(activity);
        this.mySharePreference = new MySharePreference(activity);
        this.isEdit = isEdit;
    }

    public void showDialog() {

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_dialog_save_as, null);

        edtNameBlog = (EditText) view.findViewById(R.id.edt_name_blog);
        if(isEdit){
            edtNameBlog.setText(infoBlog.getTitle());
        }

        view.findViewById(R.id.btn_post).setOnClickListener(this);

        view.findViewById(R.id.img_close).setOnClickListener(this);

        dialog.setContentView(view);

        dialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post:
                if (!TextUtils.isEmpty(edtNameBlog.getText().toString())) {
                    infoBlog.setTitle(edtNameBlog.getText().toString());

                    dialog.dismiss();

                    baseProgress = new BaseProgress();
                    baseProgress.showProgressLoading(activity);

                    Call<Status> modifiedInfoBlog;

                    if (!isEdit) {
                        modifiedInfoBlog = Connect.getRetrofit().addStatusBlog(infoBlog, mySharePreference.getPhoneLogin());
                    } else {
                        modifiedInfoBlog = Connect.getRetrofit().editStatusBlog(infoBlog, mySharePreference.getPhoneLogin());
                    }
                    modifiedInfoBlog.enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            baseProgress.hideProgressLoading();
                            if (response.body() != null) {
                                if (response.body().getStatus().equals("0")) {
//                                    Intent intent = new Intent(activity, MainActivity.class);
//                                    activity.startActivity(intent);
                                    Intent intent = new Intent(MainActivity.BROADCAST_MODIFY_BLOG_NAME);
                                    intent.putExtra(MainActivity.SEND_BROADCAST_MODIFY_BLOG_DATA, true);
                                    LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);

                                    activity.onBackPressed();
                                } else {
                                    Toast.makeText(activity,  activity.getResources().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {

                        }
                    });
                } else {
                    edtNameBlog.setError(activity.getResources().getString(R.string.empty_blog_title));
                }
                break;
            case R.id.img_close:
                dialog.dismiss();
                break;
        }
    }
}
