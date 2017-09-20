package com.app.donteatalone.views.main.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
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
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.register.CustomAdapterCompleteTextView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.donteatalone.utils.AppUtils.convertBitmaptoString;

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

    //Check infor from offRequireFragment?
    private Boolean offRequireFragment=false;

    private MySharePreference accoutSharePreference;
    private MySharePreference infoRequireSharePreference;


    public ProfileDialogCustom(Context _context, int _intLayout, TextView _textView) {
        this.context = _context;
        this.accoutSharePreference=new MySharePreference((Activity)context);
        this.accoutSharePreference=new MySharePreference((Activity)context,accoutSharePreference.getValue("phoneLogin"));
        this.intLayout = _intLayout;
        this.textView = _textView;
    }

    public ProfileDialogCustom(Context _context, int _intLayout, ImageView _imageView, Bitmap _bitmap) {
        this.context = _context;
        this.accoutSharePreference=new MySharePreference((Activity)context);
        this.accoutSharePreference=new MySharePreference((Activity)context,accoutSharePreference.getValue("phoneLogin"));
        this.intLayout = _intLayout;
        this.imgOldAvatar = _imageView;
        this.bitmap=_bitmap;
    }

    public ProfileDialogCustom(Context _context) {
        this.context=_context;
        this.accoutSharePreference=new MySharePreference((Activity)context);
        this.accoutSharePreference=new MySharePreference((Activity)context,accoutSharePreference.getValue("phoneLogin"));
    }

    public ProfileDialogCustom (Context _context, int _intLayout, TextView _textView, Boolean _offRequireFragment){
        this.context = _context;
        this.accoutSharePreference=new MySharePreference((Activity)context);
        this.accoutSharePreference=new MySharePreference((Activity)context,accoutSharePreference.getValue("phoneLogin"));
        this.intLayout = _intLayout;
        this.textView = _textView;
        this.offRequireFragment=_offRequireFragment;
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
                        accoutSharePreference.setValue("avatarLogin", convertBitmaptoString(bitmap));
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
                        accoutSharePreference.setValue("fullnameLogin", edtName.getText().toString());
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
                        accoutSharePreference.setValue("birthdayLogin",
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
                            accoutSharePreference.setValue("genderLogin", "Female");
                            updateData("Gender","Female");
                        } else {
                            accoutSharePreference.setValue("genderLogin", "Male");
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

                AppUtils.setOnSelectedItemInMACT(actvHobbyAboutFood);

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOnclickDoneinHobby(actvHobbyAboutFood,textView,"hobbyFoodRequire");
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

                AppUtils.setOnSelectedItemInMACT(actvHobbyAboutCharacter);

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOnclickDoneinHobby(actvHobbyAboutCharacter,textView,"hobbyCharacterRequire");
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

                AppUtils.setOnSelectedItemInMACT(actvHobbyAboutStyle);

                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOnclickDoneinHobby(actvHobbyAboutStyle,textView,"hobbyStyleRequire");
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
                        if(actvCharacter.getText().toString().trim().equals("")){
                            tempCharacter="";
                        }
                        else {
                            if (checkComma(actvCharacter.getText().toString().trim())) {
                                tempCharacter = actvCharacter.getText().toString().trim().substring(0, actvCharacter.getText().toString().trim().length() - 1).replaceAll(", ", ",");
                            } else {
                                tempCharacter = actvCharacter.getText().toString().trim().replaceAll(", ", ",");
                            }
                        }
                        textView.setText(tempCharacter);
                        accoutSharePreference.setValue("characterLogin",tempCharacter);
                        updateData("Character",tempCharacter);
                        dialog.dismiss();
                    }
                });
                break;
            case R.layout.custom_dialog_require_off_choose_age:
                rlClose = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_require_off_choose_age_rl_close);
                rlDone = (RelativeLayout)
                        dialog.findViewById(R.id.custom_dialog_require_off_choose_age_rl_done);
                WheelPicker wpkAgeMin=(WheelPicker) dialog.findViewById(R.id.custom_dialog_require_off_choose_age_wpk_min);
                WheelPicker wpkAgeMax=(WheelPicker) dialog.findViewById(R.id.custom_dialog_require_off_choose_age_wpk_max);
                wpkAgeMin.setSelectedItemPosition(Integer.parseInt(textView.getText().toString().trim().substring(0,2).trim())-10);
                if(Integer.parseInt(textView.getText().toString().trim().substring(0,2).trim())<10){
                    wpkAgeMax.setSelectedItemPosition(Integer.parseInt(textView.getText().toString().trim().substring(4).trim())-10);
                }
                else {
                    wpkAgeMax.setSelectedItemPosition(Integer.parseInt(textView.getText().toString().trim().substring(5).trim())-10);
                }
                //get value in resource
                String[] list = context.getResources().getStringArray(R.array.age_limit);
                final ArrayList<String> ageLimit=new ArrayList(Arrays.asList(list));

                //set data for age min and age max
                wpkAgeMin.setData(ageLimit);
                wpkAgeMax.setData(ageLimit);
                setEventChooseValueAge(wpkAgeMin,wpkAgeMax);
                rlClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                rlDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Integer.parseInt(wpkAgeMin.getData().get(wpkAgeMin.getCurrentItemPosition()).toString())!=
                                Integer.parseInt(wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString())){
                            textView.setText(Integer.parseInt(wpkAgeMin.getData().get(wpkAgeMin.getCurrentItemPosition()).toString())+ " - "+
                                    Integer.parseInt(wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString()) );
                        }
                        else {
                            textView.setText(Integer.parseInt(wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString()));
                        }
                        infoRequireSharePreference.setValue("ageRequire",textView.getText().toString());
                        dialog.cancel();
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


    //set for event click btnDone in hobby
    private void setOnclickDoneinHobby(MultiAutoCompleteTextView actvHobby, TextView text, String key){
        String tempHobby="";
        if(actvHobby.getText().toString().trim().equals("")){
            tempHobby=actvHobby.getText().toString().trim();
        }
        else {
            if (checkComma(actvHobby.getText().toString().trim())) {
                tempHobby=actvHobby.getText().toString().trim().substring(0, actvHobby.getText().toString().trim().length() - 1);
            } else {
                tempHobby=actvHobby.getText().toString().trim();
            }
        }

        if(!offRequireFragment){
            saveHobbyInfoReference(tempHobby,text.getText().toString().trim());
            textView.setText(tempHobby);
        }
        else {
            infoRequireSharePreference.setValue(key,tempHobby);
            textView.setText(tempHobby);
        }
    }

    //save hobby was change into Reference
    private void saveHobbyInfoReference(String elementHobby, String oldHobby){
        String[] item=elementHobby.split(",");
        String[] oldItem=oldHobby.split(",");
        String temp=accoutSharePreference.getValue("hobbyLogin")+",";
        for(int i=0;i<oldItem.length;i++){
            if(temp.contains(oldItem[i])){
                temp=temp.replace(temp.substring(temp.indexOf(oldItem[i]),(temp.indexOf(oldItem[i])+oldItem[i].length()+1)),"");
            }
        }
        for(int i=0;i<item.length;i++){
            if(!temp.contains(item[i])){
                if(checkComma(temp))
                    temp=temp+item[i]+",";
                else
                    temp=temp+","+item[i];
            }
        }
        temp=temp.replaceAll(", ",",");
        try {
            accoutSharePreference.setValue("hobbyLogin", temp.substring(0, temp.length() - 1));
            updateData("Hobby", temp.substring(0, temp.length() - 1));
        }
        catch (Exception e){
            accoutSharePreference.setValue("hobbyLogin","");
            updateData("Hobby", "");
        }
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

    //method set event when choose limit age for require off
    private void setEventChooseValueAge(final WheelPicker wpkAgeMin, final WheelPicker wpkAgeMax){

        //listen event when choose value
        wpkAgeMax.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {
            }

            @Override
            public void onWheelSelected(int i) {
                if(Integer.parseInt(wpkAgeMin.getData().get(wpkAgeMin.getCurrentItemPosition()).toString())>
                        Integer.parseInt(wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString())) {
                    wpkAgeMin.setSelectedItemPosition(wpkAgeMax.getCurrentItemPosition());
                }}
            @Override
            public void onWheelScrollStateChanged(int i) {
            }
        });

        wpkAgeMin.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {
            }
            @Override
            public void onWheelSelected(int i) {
                if(Integer.parseInt(wpkAgeMin.getData().get(wpkAgeMin.getCurrentItemPosition()).toString())>
                        Integer.parseInt(wpkAgeMax.getData().get(wpkAgeMax.getCurrentItemPosition()).toString())){
                    wpkAgeMin.setSelectedItemPosition(wpkAgeMax.getCurrentItemPosition());
                }}
            @Override
            public void onWheelScrollStateChanged(int i) {
            }
        });
    }

    //method update content in data
    private void updateData(String which,String content){
        Call<Status> updateProfile=Connect.getRetrofit().updateProfile(new InfoProfileUpdate(accoutSharePreference.getValue("phoneLogin"),which,content));
        updateProfile.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
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
}

