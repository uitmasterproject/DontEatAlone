package com.app.donteatalone.views.main.require.main_require.on_require;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.base.OnRecyclerItemClickListener;
import com.app.donteatalone.model.AccordantUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.app.donteatalone.views.main.blog.DetailBlogActivity.ARG_PHONE_NUMBER;

/**
 * Created by ChomChom on 30-May-17
 */

public class AccordantUserAdapter extends RecyclerView.Adapter<AccordantUserAdapter.CustomViewHolder> {
    private ArrayList<AccordantUser> listAccordantUser;
    private Context context;
    private ArrayList<Target> listTarget = new ArrayList<>();
    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public AccordantUserAdapter(ArrayList<AccordantUser> _listAccordantUser, Context _context) {
        this.listAccordantUser = _listAccordantUser;
        this.context = _context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_recyclerview_fragment_require_on, null);
        return new CustomViewHolder(view);
    }

    public void setOnClickItemRecyclerView(OnRecyclerItemClickListener onClickItemRecyclerView){
        this.onRecyclerItemClickListener = onClickItemRecyclerView;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.progressBar.setVisibility(View.GONE);
                if (bitmap != null) {
                    holder.imgAvatar.setImageBitmap(bitmap);
                } else {
                    holder.imgAvatar.setImageResource(R.drawable.avatar);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        listTarget.add(position, target);

        Picasso.with(context)
                .load(listAccordantUser.get(position).getAvatar())
                .into(listTarget.get(position));

        holder.txtName.setText(StringEscapeUtils.unescapeJava(listAccordantUser.get(position).getFullName()));

        Float litersOfPetrol=Float.parseFloat(listAccordantUser.get(position).getPercent());
        DecimalFormat df = new DecimalFormat("00.0");
        df.setMaximumFractionDigits(2);
        holder.txtPercent.setText(df.format(litersOfPetrol) + "%");

        if (listAccordantUser.get(position).getGender().equals("Male")) {
            holder.imgGender.setImageResource(R.drawable.ic_male);
        } else {
            holder.imgGender.setImageResource(R.drawable.ic_female);
        }

        holder.txtAge.setText(listAccordantUser.get(position).getAge() + "");

        holder.txtAddress.setText(StringEscapeUtils.unescapeJava(listAccordantUser.get(position).getAddress().substring(0,
                listAccordantUser.get(position).getAddress().lastIndexOf(","))));

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileAccordantUser.class);
                intent.putExtra(ARG_PHONE_NUMBER, listAccordantUser.get(position).getAccordantUser());
                context.startActivity(intent);
            }
        });

        if (listAccordantUser.get(position).getControl())
            holder.ibtnInvite.setVisibility(View.VISIBLE);
        else
            holder.ibtnInvite.setVisibility(View.INVISIBLE);
        holder.ibtnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerItemClickListener!=null) {
                    onRecyclerItemClickListener.onItemClick(v,position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listAccordantUser.size();
    }



    class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar, imgGender;
        private TextView txtName, txtPercent, txtAge, txtAddress;
        private LinearLayout llContainer;
        private ImageButton ibtnInvite;

        private ProgressBar progressBar;

        public CustomViewHolder(View itemView) {
            super(itemView);
            llContainer = (LinearLayout) itemView.findViewById(R.id.fragment_require_on_custom_cardview_ll_container_item);
            imgAvatar = (ImageView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_img_avatar);
            imgGender = (ImageView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_img_gender);
            txtName = (TextView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_txt_name);
            txtPercent = (TextView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_txt_percent);
            txtAge = (TextView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_txt_age);
            txtAddress = (TextView) itemView.findViewById(R.id.fragment_require_on_custom_cardview_txt_address);
            ibtnInvite = (ImageButton) itemView.findViewById(R.id.fragment_require_on_custom_cardview_ibtn_invite);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}
