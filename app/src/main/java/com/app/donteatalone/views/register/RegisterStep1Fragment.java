package com.app.donteatalone.views.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.UserName;

import static android.content.Context.MODE_PRIVATE;

public class RegisterStep1Fragment extends Fragment {

    private ViewPager _mViewPager;
    private Button btnNextCode, btnNextStep;
    private ViewGroup viewGroup;
    private EditText edtCode, edtPhone;
    private TextView txtRecomment;
    public static UserName userName;

    public static Fragment newInstance(Context context) {
        RegisterStep1Fragment f = new RegisterStep1Fragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup=(ViewGroup)inflater.inflate(R.layout.fragment_register_step1,null);
        init();
        changeDataEdtPhone(edtPhone,btnNextCode);
        clickButtonNextCode();
        clickButtonNextStep();
        return viewGroup;
    }

    public void init(){
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        btnNextCode=(Button) viewGroup.findViewById(R.id.fragment_register_step1_btn_next);
        edtPhone=(EditText) viewGroup.findViewById(R.id.fragment_register_step1_edt_phone);
        btnNextStep=(Button) viewGroup.findViewById(R.id.fragment_register_step1_btn_nextnext);
        edtCode=(EditText) viewGroup.findViewById(R.id.fragment_register_step1_edt_code);
        txtRecomment=(TextView) viewGroup.findViewById(R.id.fragment_register_step1_txt_recomment);
        userName=new UserName();
        if(edtPhone.getText().toString().equals("")==true){
            btnNextCode.setVisibility(View.GONE);
        }
        txtRecomment.setVisibility(View.GONE);
        edtCode.setVisibility(View.GONE);
        btnNextStep.setVisibility(View.GONE);
    }

    public void changeDataEdtPhone(final EditText edt, final Button btn){
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edt.getText().toString().equals("")==false){
                    btn.setVisibility(View.VISIBLE);
                    if(edt.getId()==R.id.fragment_register_step1_edt_phone) {
                        edtCode.setVisibility(View.GONE);
                        btnNextStep.setVisibility(View.GONE);
                    }
                }
                else {
                    btn.setVisibility(View.GONE);
                    if(edt.getId()==R.id.fragment_register_step1_edt_phone) {
                        edtCode.setVisibility(View.GONE);
                        btnNextStep.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private boolean checkExitPhone(){
        return true;
    }

    private void clickButtonNextCode(){
        btnNextCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtRecomment.setVisibility(View.VISIBLE);
                if(checkExitPhone()==true){
                    txtRecomment.setText("Wait for minute to recept code. Input code");
                    txtRecomment.setTextColor(Color.GRAY);
                    edtCode.setVisibility(View.VISIBLE);
                    changeDataEdtPhone(edtCode,btnNextStep);
                }
                else {
                    txtRecomment.setText("This phone was exit");
                    txtRecomment.setTextColor(Color.RED);
                    edtPhone.setText("");
                }
                btnNextCode.setVisibility(View.GONE);
            }
        });
    }

    private void clickButtonNextStep(){
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setPhone(edtPhone.getText().toString());
                saveReference();
//                Bundle args = new Bundle();
//                FragmentTransaction fragmentTx= getFragmentManager().beginTransaction();
//                RegisterStep2Fragment ldf = new RegisterStep2Fragment ();
//                args.putSerializable("userName", userName);
//                ldf.setArguments(args);
                _mViewPager.setCurrentItem(1,true);
            }
        });
    }

    public void saveReference(){
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("phone",edtPhone.getText().toString());
        editor.commit();
    }


}
