package com.app.donteatalone.blog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.Felling;

import java.util.ArrayList;

/**
 * Created by ChomChom on 4/17/2017.
 */

public class CustomAdapterGridViewShowFelling extends BaseAdapter implements Filterable{


    private ArrayList<Felling> fellings;
    private ArrayList<Felling> fellingsResult;
    private Filter filter;
    private Context context;

    public CustomAdapterGridViewShowFelling(Context context,ArrayList<Felling> fellings){
        this.fellings=fellings;
        this.fellingsResult=fellings;
        this.context=context;
    }

    @Override
    public int getCount() {
        return fellings.size();
    }

    @Override
    public Object getItem(int position) {
        return fellings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            view=layoutInflater.inflate(R.layout.custom_adapter_gridview_dialog_show_felling,null);
        }
        else {
            view=(View) convertView;
        }
        TextView textView=(TextView) view.findViewById(R.id.custom_adapter_gridview_dialog_show_felling_txt_felling);
        ImageView imageView=(ImageView) view.findViewById(R.id.custom_adapter_gridview_dialog_show_felling_img_icon);
        textView.setText(fellings.get(position).getFelling());
        imageView.setImageResource(fellings.get(position).getResourceIcon());
        return view;
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter=new CustomFilter();
        }
        return filter;
    }

    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            ArrayList<Felling> listFilter=new ArrayList<>();
            if(constraint!=null&&constraint.length()>0){
                for(int i=0;i<fellingsResult.size();i++){
                    if(fellingsResult.get(i).getFelling().contains(constraint)==true){
                        listFilter.add(fellingsResult.get(i));
                    }
                }
            }

            if(listFilter.size()!=0) {
                results.count = listFilter.size();
                results.values = listFilter;
            }
            else {
                results.count=fellingsResult.size();
                results.values=fellingsResult;
            }


            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                fellings = (ArrayList<Felling>) results.values;
            }
            else
            {
                fellings=(ArrayList<Felling>) fellingsResult;
            }
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Felling)resultValue).getFelling();
        }
    }
}
