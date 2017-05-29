package com.app.donteatalone.views.main.profile;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;

/**
 * Created by Le Hoang Han on 5/21/2017.
 */

public class ProfileDialogCustom {
    private Context context;
    private int intLayout;
    private TextView textView;
    private RelativeLayout rlClose, rlDone;

    private EditText edtName;
    private RelativeLayout rlMale, rlFemale;
    public String text;


    public ProfileDialogCustom(Context _context, int _intLayout, TextView _textView) {
        this.context = _context;
        this.intLayout = _intLayout;
        this.textView = _textView;
    }

    public void showDialogCustom() {
        Dialog dialog = new Dialog(this.context);
        dialog.setContentView(intLayout);
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

            //Edit Age

            //Edit Gender
            case R.layout.custom_dialog_profile_gender:
                rlClose = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_gender_rl_close);
                rlDone = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_gender_rl_done);
                rlMale = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_gender_rl_male);
                rlFemale = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_gender_rl_female);
                if (textView.getText().toString().equals("F")) {
                    rlFemale.setBackgroundResource(R.drawable.btn_round_orange);
                } else {
                    rlMale.setBackgroundResource(R.drawable.btn_round_orange);
                }

                rlMale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlFemale.setBackgroundResource(R.drawable.bg_round);
                        rlMale.setBackgroundResource(R.drawable.btn_round_orange);
                    }
                });

                rlFemale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlFemale.setBackgroundResource(R.drawable.btn_round_orange);
                        rlMale.setBackgroundResource(R.drawable.bg_round);
                    }
                });

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //code for save gender to database => Nga

                        dialog.dismiss();
                    }
                });
                break;
        }
        dialog.show();
    }
}

