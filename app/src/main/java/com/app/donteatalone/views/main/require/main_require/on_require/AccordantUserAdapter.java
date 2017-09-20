package com.app.donteatalone.views.main.require.main_require.on_require;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.AccordantUser;
import com.app.donteatalone.model.Restaurant;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.MySharePreference;
import com.github.nkzawa.socketio.client.Socket;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 30-May-17.
 */

public class AccordantUserAdapter extends RecyclerView.Adapter<AccordantUserAdapter.CustomViewHolder> {
    private ArrayList<AccordantUser> listAccordantUser;
    private Context context;
    private Socket socketIO;

    public AccordantUserAdapter(ArrayList<AccordantUser> _listAccordantUser,Context _context, Socket _socketIO){
        this.listAccordantUser=_listAccordantUser;
        this.context=_context;
        this.socketIO=_socketIO;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_fragment_require_on, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        Bitmap bmp=decodeBitmap(listAccordantUser.get(position).getAvatar());
        if(bmp.getHeight()<=bmp.getWidth()){
            holder.imgAvatar.setLayoutParams(new LinearLayout.LayoutParams(100,100*bmp.getHeight()/bmp.getWidth()));
        }
        else {
            holder.imgAvatar.setLayoutParams(new LinearLayout.LayoutParams(100*bmp.getWidth()/bmp.getHeight(),100));
        }
        holder.imgAvatar.setScaleType(ImageView.ScaleType.FIT_CENTER);
        holder.imgAvatar.setImageBitmap(bmp);

        holder.txtName.setText(listAccordantUser.get(position).getFullName());

        holder.txtPercent.setText(listAccordantUser.get(position).getPercent()+" %");

        if(listAccordantUser.get(position).getGender().equals("Male")){
            holder.imgGender.setImageResource(R.drawable.ic_male);
        }
        else {
            holder.imgGender.setImageResource(R.drawable.ic_female);
        }

        holder.txtAge.setText(listAccordantUser.get(position).getAge()+"");

        holder.txtAddress.setText(listAccordantUser.get(position).getAddress().substring(0,
                listAccordantUser.get(position).getAddress().lastIndexOf(",")));

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"profile",Toast.LENGTH_SHORT).show();
            }
        });

        if(listAccordantUser.get(position).getControl())
            holder.ibtnInvite.setVisibility(View.VISIBLE);
        else
            holder.ibtnInvite.setVisibility(View.INVISIBLE);
        holder.ibtnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_require_on_invite_setting_time);
                initDialog(dialog,position);
                dialog.show();
            }
        });
    }

    private void initDialog(final Dialog dialog, final int position){
        final TextView txtDate, txtTimer, txtAddress;
        Button btnInvite;
        LinearLayout llContainerTime, llContainerPlace, llContainerSearch;
        RelativeLayout rlContainerTime,rlContainerSearch;
        ArrayList<Restaurant> listRestaurant=new ArrayList<>();
        txtDate=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_invite_txt_date);
        txtTimer=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_invite_txt_time);
        txtAddress=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_invite_txt_address);
        btnInvite=(Button) dialog.findViewById(R.id.custom_dialog_require_on_invite_btn_invite);
        rlContainerTime=(RelativeLayout) dialog.findViewById(R.id.custom_dialog_require_on_invite_rl_container_time);
        llContainerTime=(LinearLayout) dialog.findViewById(R.id.custom_dialog_require_on_invite_ll_container_time);
        llContainerPlace=(LinearLayout) dialog.findViewById(R.id.custom_dialog_require_on_invite_ll_container_place);
        llContainerSearch=(LinearLayout) dialog.findViewById(R.id.custom_dialog_require_on_invite_ll_container_search);
        rlContainerSearch=(RelativeLayout) dialog.findViewById(R.id.custom_dialog_require_on_invite_rl_container_search);
        setValueTime(txtTimer,txtDate);
        setValuePlace(txtAddress,position, listRestaurant);
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                String formattedDate = df.format(c.getTime());

                socketIO.emit("invite",new MySharePreference((Activity)context).getValue("phoneLogin")+"|"+listAccordantUser.get(position).getAccordantUser()+"|"+
                txtDate.getText().toString()+"|"+txtTimer.getText().toString()+"|"+ AppUtils.convertStringToNFD(txtAddress.getText().toString())+"|"+formattedDate);
                for(AccordantUser temp:listAccordantUser){
                    temp.setControl(false);
                }
                notifyDataSetChanged();
                countDown5Minute();
                dialog.cancel();
            }
        });

        rlContainerTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("visibility",llContainerTime.getVisibility()+"");
                if(llContainerTime.getVisibility()==View.VISIBLE)
                    llContainerTime.setVisibility(View.GONE);
                else {
                    llContainerTime.setVisibility(View.VISIBLE);
                    setOnclickllContainerTime(dialog,txtDate,txtTimer);
                }
            }
        });

        llContainerPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(llContainerSearch.getVisibility()==View.VISIBLE)
                    llContainerSearch.setVisibility(View.GONE);
                else {
                    llContainerSearch.setVisibility(View.VISIBLE);
                    setOnclickllContainerPlace(dialog,txtAddress,listRestaurant);
                }
            }
        });
    }


    private void countDown5Minute(){
        new CountDownTimer(300000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                for(AccordantUser temp:listAccordantUser){
                    temp.setControl(true);
                }
                notifyDataSetChanged();
            }
        }.start();
    }

    @Override
    public int getItemCount() {
        return listAccordantUser.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar, imgGender;
        private TextView txtName,txtPercent, txtAge, txtAddress;
        private LinearLayout llContainer;
        private ImageButton ibtnInvite;

        public CustomViewHolder(View itemView) {
            super(itemView);
            llContainer=(LinearLayout) itemView.findViewById(R.id.fragment_require_on_custom_cardview_ll_container_item);
            imgAvatar=(ImageView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_img_avatar);
            imgGender=(ImageView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_img_gender);
            txtName=(TextView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_txt_name);
            txtPercent=(TextView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_txt_percent);
            txtAge=(TextView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_txt_age);
            txtAddress=(TextView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_txt_address);
            ibtnInvite=(ImageButton) itemView.findViewById(R.id.fragment_require_on_custom_cardview_ibtn_invite);
        }
    }

    private Bitmap decodeBitmap(String avatar){
        Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.avatar);
        if(avatar.equals("")!=true) {
            try {
                byte[] encodeByte = Base64.decode(avatar, Base64.DEFAULT);
                bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return bitmap;
    }

    private void setValueTime(TextView txtTime, TextView txtDate){
        Calendar calendar=Calendar.getInstance();
        Log.e("time",calendar.get(Calendar.DATE)+"/"+(calendar.get(Calendar.MONTH)+1));
        txtTime.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
        txtDate.setText(calendar.get(Calendar.DATE)+"/"+(calendar.get(Calendar.MONTH)+1));
    }

    private void setValuePlace(TextView txtPlace,int position, ArrayList<Restaurant>listRestaurant){

        MySharePreference requireInfoSharePreference=new MySharePreference((Activity)context,new MySharePreference((Activity)context).getValue("phoneLogin"));

        String latlng=Math.abs((Float.parseFloat(requireInfoSharePreference.getValue("latlngaddressRequire").split(",")[0].trim())-
                Float.parseFloat(listAccordantUser.get(position).getLatlng().split(",")[0].trim()))/2)+","+
                Math.abs((Float.parseFloat(requireInfoSharePreference.getValue("latlngaddressRequire").split(",")[1].trim())-
                        Float.parseFloat(listAccordantUser.get(position).getLatlng().split(",")[1].trim()))/2);

        Call<ArrayList<Restaurant>>getListRestaurant = Connect.getRetrofit().getRestaurant(latlng);
        getListRestaurant.enqueue(new Callback<ArrayList<Restaurant>>() {
            @Override
            public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                if(response.body().size()>0){
                    txtPlace.setText(response.body().get(0).getName());
                    for (Restaurant res:response.body()) {
                        listRestaurant.add(new Restaurant(res.getName(),res.getAddress(),res.getLatlng(),res.getOpenDay()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {

            }
        });
    }

    private void setOnclickllContainerTime(Dialog dialog, TextView txtDate, TextView txtTime){
        WheelPicker wpdate, wpmonth, wphour, wpminute;
        wpdate=(WheelPicker) dialog.findViewById(R.id.custom_dialog_require_on_invite_wp_date);
        wpmonth=(WheelPicker) dialog.findViewById(R.id.custom_dialog_require_on_invite_wp_month);
        wphour=(WheelPicker)dialog.findViewById(R.id.custom_dialog_require_on_invite_wp_hour);
        wpminute=(WheelPicker)dialog.findViewById(R.id.custom_dialog_require_on_invite_wp_minute);

        wpdate.setData(Arrays.asList(context.getResources().getStringArray(R.array.Day_31)));
        wpdate.setSelectedItemPosition(Integer.parseInt(txtDate.getText().toString().trim().split("/")[0])-1);
        wpmonth.setData(Arrays.asList(context.getResources().getStringArray(R.array.Month)));
        wpmonth.setSelectedItemPosition(Integer.parseInt(txtDate.getText().toString().trim().split("/")[1])-1);
        wphour.setData(Arrays.asList(context.getResources().getStringArray(R.array.Hour)));
        wphour.setSelectedItemPosition(Integer.parseInt(txtTime.getText().toString().trim().split(":")[0]));
        wpminute.setData(Arrays.asList(context.getResources().getStringArray(R.array.Minute)));
        wpminute.setSelectedItemPosition((Integer.parseInt(txtTime.getText().toString().trim().split(":")[1])/10)+1);

        LocalDate localDate=new LocalDate();
        wpmonth.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                txtDate.setText((wpdate.getCurrentItemPosition()+1)+"/"+(wpmonth.getCurrentItemPosition()+1));
                setDataforWhellPicker(i,wpdate,localDate.getYear());
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });

        wpdate.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                txtDate.setText((wpdate.getCurrentItemPosition()+1)+"/"+txtDate.getText().toString().trim().split("/")[1]);
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });

        wphour.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                txtTime.setText((wphour.getCurrentItemPosition())+":"+txtTime.getText().toString().trim().split(":")[1]);
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });

        wpminute.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int i) {

            }

            @Override
            public void onWheelSelected(int i) {
                txtTime.setText(txtTime.getText().toString().trim().split(":")[0]+":"+wpminute.getCurrentItemPosition()*10);
            }

            @Override
            public void onWheelScrollStateChanged(int i) {

            }
        });
    }

    private boolean checkLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 != 0) {
                return true;
            }
        }
        if (year % 100 == 0) {
            if (year % 400 == 0) {
                return true;
            }
        }
        return false;
    }

    private void setDataforWhellPicker(int position, WheelPicker wpDate, int year) {
        switch (position) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                wpDate.setData(Arrays.asList(context.getResources().getStringArray(R.array.Day_31)));
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                wpDate.setData(Arrays.asList(context.getResources().getStringArray(R.array.Day_30)));
                break;
            case 1:
                if (checkLeapYear(year)) {
                    wpDate.setData(Arrays.asList(context.getResources().getStringArray(R.array.Day_29)));
                } else {
                    wpDate.setData(Arrays.asList(context.getResources().getStringArray(R.array.Day_28)));
                }
        }
    }

    private void setOnclickllContainerPlace(Dialog dialog,TextView txtPlace, ArrayList<Restaurant> listRestaurant){
        RecyclerView recycleview=(RecyclerView)dialog.findViewById(R.id.custom_dialog_require_on_invite_lst_place);
        recycleview.setLayoutManager(new LinearLayoutManager(context));
        CustomInvitedRestaurantAdapter adapter=new CustomInvitedRestaurantAdapter(listRestaurant,txtPlace);
        recycleview.setAdapter(adapter);
    }
}
