package com.app.donteatalone.base;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.app.donteatalone.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * -> Created by LeHoangHan on 4/4/2017
 */

public class BaseProgress {

    private Dialog progressDialogLoading;

    public void showProgressLoading(Context context) {

        if (progressDialogLoading != null) {
            progressDialogLoading.dismiss();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.layout_progress_loading, null);
        AVLoadingIndicatorView avLoadingIndicatorView =
                (AVLoadingIndicatorView) view.findViewById(R.id.layout_progress_loading_avliv);
        avLoadingIndicatorView.show();
        progressDialogLoading = new Dialog(context);
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
