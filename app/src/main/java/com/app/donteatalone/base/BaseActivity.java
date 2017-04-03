package com.app.donteatalone.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.app.donteatalone.R;
import com.wang.avi.AVLoadingIndicatorView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * -> Created by LeHoangHan on 4/4/2017.
 */

public class BaseActivity extends AppCompatActivity {

    private Dialog progressDialogLoading;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showProgressLoading() {

        if (progressDialogLoading != null) {
            progressDialogLoading.dismiss();
        }
        View view = getLayoutInflater().inflate(R.layout.layout_progress_loading, null);
        AVLoadingIndicatorView avLoadingIndicatorView =
                (AVLoadingIndicatorView) view.findViewById(R.id.layout_progress_loading_avliv);
        avLoadingIndicatorView.show();
        progressDialogLoading = new Dialog(this);
        progressDialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialogLoading.setContentView(view);
        progressDialogLoading.setCancelable(false);
        progressDialogLoading.setCanceledOnTouchOutside(false);

        Window window = progressDialogLoading.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.bg_layout_loading);
        }

        progressDialogLoading.show();
    }

    public void hideProgressLoading() {
        if (progressDialogLoading != null && progressDialogLoading.isShowing()) {
            progressDialogLoading.dismiss();
        }
    }
}
