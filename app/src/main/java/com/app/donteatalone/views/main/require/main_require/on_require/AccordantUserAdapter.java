package com.app.donteatalone.views.main.require.main_require.on_require;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.model.AccordantUser;
import com.github.nkzawa.socketio.client.Socket;

import java.util.ArrayList;

/**
 * Created by ChomChom on 30-May-17.
 */

public class AccordantUserAdapter extends RecyclerView.Adapter<AccordantUserAdapter.CustomViewHolder> {
    private ArrayList<AccordantUser> listAccordantUser;
    private Context context;
    private Socket socketIO;
    private String ownPhone;

    public AccordantUserAdapter(ArrayList<AccordantUser> _listAccordantUser,Context _context, Socket _socketIO, String _ownPhone){
        this.listAccordantUser=_listAccordantUser;
        this.context=_context;
        this.socketIO=_socketIO;
        this.ownPhone=_ownPhone;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_fragment_require_on, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
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

        if(listAccordantUser.get(position).getGender().equals("Man")==true){
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

        holder.ibtnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_require_on_invite);
                initDialog(dialog,position);
                dialog.show();
            }
        });
    }

    private void initDialog(final Dialog dialog, final int position){
        final TextView txtDate, txtTimer, txtAddress;
        Button btnInvite;
        txtDate=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_invite_txt_date);
        txtTimer=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_invite_txt_time);
        txtAddress=(TextView) dialog.findViewById(R.id.custom_dialog_require_on_invite_txt_address);
        btnInvite=(Button) dialog.findViewById(R.id.custom_dialog_require_on_invite_btn_invite);
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketIO.emit("invite",ownPhone+"|"+listAccordantUser.get(position).getAccordantUser()+"|"+
                txtDate.getText().toString()+"|"+txtTimer.getText().toString()+"|"+txtAddress.getText().toString());
                dialog.cancel();
            }
        });
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
}
