package com.app.donteatalone.views.main.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;

/**
 * Created by Le Hoang Han on 5/21/2017.
 */

public class ProfileDialogCustom {
    private Context context;
    private int intLayout;
    private String title;
    private TextView textView;
    private RelativeLayout rlClose, rlDone;

    private EditText edtName;
    public String text;


    public ProfileDialogCustom(Context _context, int _intLayout, String _title, TextView _textView) {
        this.context = _context;
        this.intLayout = _intLayout;
        this.title = _title;
        this.textView = _textView;
    }

    public void showDialogCustom() {
        Dialog dialog = new Dialog(this.context);
        dialog.setContentView(intLayout);
        dialog.setTitle(title);
        dialog.setCanceledOnTouchOutside(false);

        //code in here
        switch (intLayout) {

            //Edit Profile Name
            case R.layout.custom_dialog_profile_name:
                rlClose = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_name_rl_close);
                rlDone = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_name_rl_done);
                edtName = (EditText) dialog.findViewById(R.id.custom_dialog_profile_name_edt_name);
                edtName.setText(textView.getText().toString());

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //code for save new name to database ==> Nga ;)))

                        //code for show new name on profile screen

                        dialog.dismiss();
                    }
                });
                break;

        }


        dialog.show();
    }
}

