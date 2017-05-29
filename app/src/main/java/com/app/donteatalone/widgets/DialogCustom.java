package com.app.donteatalone.widgets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.widget.EditText;
import android.widget.Switch;

/**
 * Created by Le Hoang Han on 5/21/2017.
 */

public class DialogCustom {
    private Context context;
    private int intLayout;
    private String title;

    public DialogCustom(Context _context, int _intLayout, String _title) {
        this.context = _context;
        this.intLayout = _intLayout;
        this.title = _title;
    }

    public void showDialogCustom() {
        Dialog dialog = new Dialog(this.context);
        dialog.setContentView(intLayout);
        dialog.setTitle(title);

        //code in here

        dialog.show();
    }
}

