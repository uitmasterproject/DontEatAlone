package com.example.app.donteatalone.register;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.app.donteatalone.R;

import java.lang.reflect.Field;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterStep2Fragment extends Fragment {

    private Spinner spnDay,spnMonth, spnYear;
    private ViewGroup viewGroup;
    public static Fragment newInstance(Context context) {
        RegisterStep2Fragment f = new RegisterStep2Fragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGroup=(ViewGroup)inflater.inflate(R.layout.fragment_register_step2, null);
        init();
        initSpinner();
        //checkDate();

        return viewGroup;
    }


    private void init(){
        spnDay= (Spinner) viewGroup.findViewById(R.id.activity_register_material_spinner_day);
        spnMonth= (Spinner) viewGroup.findViewById(R.id.activity_register_material_spinner_month);
        spnYear= (Spinner) viewGroup.findViewById(R.id.activity_register_material_spinner_year);
    }

    private void initSpinner(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.custom_spinner_text, getResources().getStringArray(R.array.Day_31));
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spnDay.setAdapter(arrayAdapter);

        //limit show item in spinner
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spnDay);
            popupWindow.setHeight(500);//500px
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
        }
        //
    }


    private void checkDate(){
        spnMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),(getResources().getStringArray(R.array.Day_31))[position],Toast.LENGTH_LONG).show();
            }
        });

        spnDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),(getResources().getStringArray(R.array.Day_31))[position],Toast.LENGTH_LONG).show();
            }
        });
    }


}
