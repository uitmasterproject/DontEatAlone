package com.app.donteatalone.views.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseActivity;
import com.app.donteatalone.views.main.MainActivity;

/**
 * Created by Le Hoang Han on 5/21/2017.
 */

public class ProfileCustomDialogName extends BaseActivity {
    private RelativeLayout rlClose, rlDone;
    private EditText edtName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layout=getIntent().getExtras().getInt("layout");
        setContentView(layout);
        init();
        rlCloseClick();
        rlDoneClick();
    }

    private void init() {
        rlClose = (RelativeLayout) findViewById(R.id.custom_dialog_profile_name_rl_close);
        rlDone = (RelativeLayout) findViewById(R.id.custom_dialog_profile_name_rl_done);
        edtName = (EditText) findViewById(R.id.custom_dialog_profile_name_edt_name);
        edtName.setText(getIntent().getExtras().getString("value_current_name"));
    }

    private void rlCloseClick() {
        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void rlDoneClick() {
        rlDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Send data new name from here to ProfileFragment
//                Bundle bundle = new Bundle();
//                bundle.putString("value_new_name",edtName.getText().toString());
//                ProfileFragment profileFragment = new ProfileFragment();
//                profileFragment.setArguments(bundle);

                Intent intent = new Intent(ProfileCustomDialogName.this, MainActivity.class);
                intent.putExtra("value_new_name",edtName.getText().toString());
                startActivity(intent);

                onBackPressed();
            }
        });
    }
}
