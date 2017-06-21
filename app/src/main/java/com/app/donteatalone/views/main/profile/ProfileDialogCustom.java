package com.app.donteatalone.views.main.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Hobby;
import com.app.donteatalone.model.InfoProfileUpdate;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.views.register.CustomAdapterCompleteTextView;

import org.joda.time.LocalDate;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Le Hoang Han on 5/21/2017.
 */

public class ProfileDialogCustom {
    private Context context;
    private int intLayout;
    private TextView textView;
    private RelativeLayout rlClose, rlDone;
    private ArrayList<String> headerCharacter;
    private ArrayList<Hobby> character;

    //For avatar
    private ImageView imgAvatar, imgOldAvatar;
    private Bitmap bitmap;

    //For name
    private EditText edtName;
    //For Gender
    private RelativeLayout rlMale, rlFemale;
    //For age
    private WheelPicker dayWheelPicker, monthWhellPicker;
    private WheelYearPicker yearWheelPicker;
    private int intSelectYear, intSelectMonth;
    private LocalDate localDate;
    //For hobby
    private MultiAutoCompleteTextView actvHobbyAboutFood;
    private MultiAutoCompleteTextView actvHobbyAboutCharacter;
    private MultiAutoCompleteTextView actvHobbyAboutStyle;
    //For Character
    private AutoCompleteTextView actvCharacter;


    public ProfileDialogCustom(Context _context, int _intLayout, TextView _textView) {
        this.context = _context;
        this.intLayout = _intLayout;
        this.textView = _textView;
    }

    public ProfileDialogCustom(Context _context, int _intLayout, ImageView _imageView, Bitmap _bitmap) {
        this.context = _context;
        this.intLayout = _intLayout;
        this.imgOldAvatar = _imageView;
        this.bitmap=_bitmap;
    }

    public ProfileDialogCustom() {
    }

    public void saveDataAddress(String address, String latlngaddress){
        updateData("Address",address);
        updateData("LatLngAdress", latlngaddress);
    }

    //show all dialog custom rely profile's field
    public void showDialogCustom() {
        Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(intLayout);
        dialog.setCanceledOnTouchOutside(false);

        ArrayAdapter<String> adapter;

        //code in here
        switch (intLayout) {
            //Edit Profile Avatar
            case R.layout.custom_dialog_profile_avatar:
                rlClose = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_avatar_rl_close);
                rlDone = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_avatar_rl_done);
                imgAvatar=(ImageView) dialog.findViewById(R.id.custom_dialog_profile_avatar_img_avatar);
                imgAvatar.setImageBitmap(bitmap);
                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgOldAvatar.setImageBitmap(bitmap);
                        saveInfoReference("avatarLogin", convertBitmaptoString(bitmap));
                        updateData("Avatar",convertBitmaptoString(bitmap));
                        dialog.dismiss();
                    }
                });
                break;

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
                        textView.setText(edtName.getText().toString());
                        saveInfoReference("fullnameLogin", edtName.getText().toString());
                        updateData("FullName",edtName.getText().toString());
                        dialog.dismiss();
                    }
                });
                break;

            //Edit Profile Age
            case R.layout.custom_dialog_profile_age:
                rlClose = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_age_rl_close);
                rlDone = (RelativeLayout) dialog.findViewById(R.id.custom_dialog_profile_age_rl_done);
                dayWheelPicker = (WheelPicker) dialog.findViewById(R.id.custom_dialog_profile_wheelpicker_day);
                monthWhellPicker = (WheelPicker) dialog.findViewById(R.id.custom_dialog_profile_wheelpicker_month);
                yearWheelPicker = (WheelYearPicker) dialog.findViewById(R.id.custom_dialog_profile_wheelpicker_year);

                localDate = new LocalDate();
                dayWheelPicker.setSelectedItemPosition(localDate.getDayOfMonth() - 1);
                monthWhellPicker.setSelectedItemPosition(localDate.getMonthOfYear() - 1);
                yearWheelPicker.setSelectedYear(localDate.getYear() - Integer.parseInt(textView.getText().toString()));
                ChangeDate();

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setText((localDate.getYear() - yearWheelPicker.getCurrentYear()) + "");
                        saveInfoReference("birthdayLogin",
                                (dayWheelPicker.getCurrentItemPosition() - 1) + "/"
                                        + (monthWhellPicker.getCurrentItemPosition() - 1) + "/"
                                        + (yearWheelPicker.getCurrentYear()) + "");
                        updateData("Birthday",(dayWheelPicker.getCurrentItemPosition() - 1) + "/"
                                + (monthWhellPicker.getCurrentItemPosition() - 1) + "/"
                                + (yearWheelPicker.getCurrentYear()));
                        dialog.dismiss();
                    }
                });
                break;

            //Edit Profile Gender
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
                        textView.setText("M");
                    }
                });

                rlFemale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlFemale.setBackgroundResource(R.drawable.btn_round_orange);
                        rlMale.setBackgroundResource(R.drawable.bg_round);
                        textView.setText("F");
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
                        if (textView.getText().equals("F")) {
                            saveInfoReference("genderLogin", "Female");
                            updateData("Gender","Female");
                        } else {
                            saveInfoReference("genderLogin", "Male");
                            updateData("Gender","Male");
                        }
                        dialog.dismiss();
                    }
                });
                break;

            //Edit Profile Hobby about food
            case R.layout.custom_dialog_profile_hobby_food:
                rlClose = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_food_rl_close);
                rlDone = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_food_rl_done);
                actvHobbyAboutFood = (MultiAutoCompleteTextView)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_food_actv);

                actvHobbyAboutFood.setText(textView.getText().toString());
                adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,context.getResources().getStringArray(R.array.food));
                actvHobbyAboutFood.setAdapter(adapter);
                actvHobbyAboutFood.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                actvHobbyAboutFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setOnselectedIteminHobby(actvHobbyAboutFood);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

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
                        setOnclickDoneinHobby(actvHobbyAboutFood,textView);
                        dialog.dismiss();
                    }
                });
                break;

            //Hobby about character
            case R.layout.custom_dialog_profile_hobby_character:
                rlClose = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_character_rl_close);
                rlDone = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_character_rl_done);
                actvHobbyAboutCharacter = (MultiAutoCompleteTextView)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_character_actv);

                actvHobbyAboutCharacter.setText(textView.getText().toString());
                adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,context.getResources().getStringArray(R.array.character));
                actvHobbyAboutCharacter.setAdapter(adapter);
                actvHobbyAboutCharacter.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                actvHobbyAboutCharacter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setOnselectedIteminHobby(actvHobbyAboutCharacter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

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
                        setOnclickDoneinHobby(actvHobbyAboutCharacter,textView);
                        dialog.dismiss();
                    }
                });
                break;

            //Edit Profile hooby about style
            case R.layout.custom_dialog_profile_hobby_style:
                rlClose = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_style_rl_close);
                rlDone = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_style_rl_done);
                actvHobbyAboutStyle = (MultiAutoCompleteTextView)
                        dialog.findViewById(R.id.custom_dialog_profile_hobby_about_style_actv);

                actvHobbyAboutStyle.setText(textView.getText().toString());
                adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,context.getResources().getStringArray(R.array.style));
                actvHobbyAboutStyle.setAdapter(adapter);
                actvHobbyAboutStyle.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                actvHobbyAboutStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setOnselectedIteminHobby(actvHobbyAboutStyle);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

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
                        setOnclickDoneinHobby(actvHobbyAboutStyle,textView);
                        dialog.dismiss();
                    }
                });
                break;

                //Edit profile Character
            case R.layout.custom_dialog_profile_character:
                rlClose = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_character_rl_close);
                rlDone = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_profile_character_rl_done);
                actvCharacter = (AutoCompleteTextView)
                        dialog.findViewById(R.id.custom_dialog_profile_character_actv);
                clonebdHobby();
                CustomAdapterCompleteTextView customAdapterCompleteTextView=new CustomAdapterCompleteTextView(context, android.R.layout.simple_list_item_1,headerCharacter,character,actvCharacter);
                actvCharacter.setAdapter(customAdapterCompleteTextView);

                actvCharacter.setText(textView.getText().toString());

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tempCharacter;
                        if(checkComma(actvCharacter.getText().toString().trim())==true) {
                            tempCharacter=actvCharacter.getText().toString().trim().substring(0,actvCharacter.getText().toString().trim().length()-1).replaceAll(", ",",");
                        }
                        else {
                            tempCharacter=actvCharacter.getText().toString().trim().replaceAll(", ",",");
                        }
                        Log.e("Character",tempCharacter);
                        textView.setText(tempCharacter);
                        saveInfoReference("characterLogin",tempCharacter);
                        updateData("Character",tempCharacter);
                        dialog.dismiss();
                    }
                });
                break;
        }
        dialog.show();
    }


    // Nhung ham dung cho edit Age =================================================================
    private boolean checkLeapYear() {
        intSelectYear = yearWheelPicker.getCurrentYear();
        if (intSelectYear % 4 == 0) {
            if (intSelectYear % 100 != 0) {
                return true;
            }
        }
        if (intSelectYear % 100 == 0) {
            if (intSelectYear % 400 == 0) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<String> getDateData(int arr) {
        String[] temp = context.getResources().getStringArray(arr);
        ArrayList<String> day = new ArrayList<String>(Arrays.asList(temp));
        return day;
    }


    private void ChangeDate() {
        monthWhellPicker.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {
            }

            @Override
            public void onWheelSelected(int i) {
            }

            @Override
            public void onWheelScrollStateChanged(int i) {
                setDataforWhellPicker();
            }
        });

        yearWheelPicker.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {

            }

            @Override
            public void onWheelScrollStateChanged(int i) {
                setDataforWhellPicker();
            }
        });
    }

    private void setDataforWhellPicker() {
        intSelectMonth = monthWhellPicker.getCurrentItemPosition();
        switch (intSelectMonth) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                dayWheelPicker.setData(getDateData(R.array.Day_31));
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                dayWheelPicker.setData(getDateData(R.array.Day_30));
                break;
            case 1:
                if (checkLeapYear()) {
                    dayWheelPicker.setData(getDateData(R.array.Day_29));
                } else {
                    dayWheelPicker.setData(getDateData(R.array.Day_28));
                }
        }
    }

    //start methods use for hobby

    //set for event selected item in mutiautocomplete hobby
    private void setOnselectedIteminHobby(MultiAutoCompleteTextView actvHobby){
        if(actvHobby.getText().toString().length()>0) {
            actvHobby.setText(actvHobby.getText().toString().substring(0, actvHobby.getText().toString().lastIndexOf(",")) + actvHobby.getItemSelectedListener().toString());
        }
        else {
            actvHobby.setText(actvHobby.getText().toString().substring(0, actvHobby.getText().toString().lastIndexOf(","))+"," + actvHobby.getItemSelectedListener().toString());
        }
    }

    //set for event click btnDone in hobby
    private void setOnclickDoneinHobby(MultiAutoCompleteTextView actvHobby, TextView text){
        if(checkComma(actvHobby.getText().toString().trim())==true) {
            saveHobbyInfoReference(actvHobby.getText().toString().trim().substring(0,actvHobby.getText().toString().trim().length()-1), text.getText().toString().trim());
            textView.setText(actvHobby.getText().toString().trim().substring(0,actvHobby.getText().toString().trim().length()-1));
        }
        else {
            saveHobbyInfoReference(actvHobby.getText().toString().trim(), text.getText().toString().trim());
            textView.setText(actvHobby.getText().toString().trim());
        }
    }

    //save hobby was change into Reference
    private void saveHobbyInfoReference(String elementHobby, String oldHobby){
        String[] item=elementHobby.split(",");
        String[] oldItem=oldHobby.split(",");
        String temp=storeReference("hobbyLogin")+",";
        Log.e("tempHobby",temp);
        for(int i=0;i<oldItem.length;i++){
            if(temp.contains(oldItem[i])==true){
                Log.e("oldItem",oldItem[i]);
                temp=temp.replace(temp.substring(temp.indexOf(oldItem[i]),(temp.indexOf(oldItem[i])+oldItem[i].length()+1)),"");
                Log.e("oldItemTemp",temp);
            }
        }
        for(int i=0;i<item.length;i++){
            if(temp.contains(item[i])!=true){
                if(checkComma(temp)==true)
                    temp=temp+item[i]+",";
                else
                    temp=temp+","+item[i];
            }
        }
        Log.e("newItemTemp",temp);
        temp=temp.replaceAll(", ",",");
        Log.e("newChangeItemTemp",temp);
        saveInfoReference("hobbyLogin",temp.substring(0,temp.length()-1));
        updateData("Hobby",temp.substring(0,temp.length()-1));
    }

    //check string have contain comma?
    private Boolean checkComma(String str){
        if(str.lastIndexOf(",")==(str.length()-1)) {
            return true;
        }
        else {
            return false;
        }
    }

    //end methods use for hobby

    //method update content in data
    private void updateData(String which,String content){
        Connect connect=new Connect();
        Call<Status> updateProfile=connect.getRetrofit().updateProfile(new InfoProfileUpdate(storeReference("phoneLogin"),which,content));
        updateProfile.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }

    //save content was changed into Reference
    private void saveInfoReference(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //get data from Reference
    private String storeReference(String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account", MODE_PRIVATE);
        String avatar = sharedPreferences.getString(str, "");
        return avatar;
    }

    //convert data from bitmap to String
    private String convertBitmaptoString(Bitmap bm){
        String tempConvert="";
        ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,90,arrayOutputStream);
        byte[] b=arrayOutputStream.toByteArray();
        tempConvert= Base64.encodeToString(b,Base64.DEFAULT);
        return tempConvert;
    }

    //clone data of Character.
    private void clonebdHobby(){
        headerCharacter=new ArrayList<>();
        headerCharacter.add("Tính cách");
        headerCharacter.add("Phong cách");

        character=new ArrayList<>();
        character.add(new Hobby("Tính cách","",true));
        character.add(new Hobby("Tính cách","vui vẻ",false));
        character.add(new Hobby("Tính cách","trầm tĩnh",false));
        character.add(new Hobby("Tính cách","hóm hĩnh",false));
        character.add(new Hobby("Tính cách","thoải mái",false));

        character.add(new Hobby("Phong cách","",true));
        character.add(new Hobby("Phong cách","tự do",false));
        character.add(new Hobby("Phong cách","quái dị",false));
        character.add(new Hobby("Phong cách","trưởng thành",false));
    }
//=================================================================================================
}

