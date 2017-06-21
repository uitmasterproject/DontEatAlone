package com.app.donteatalone.views.register;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.Hobby;

import java.util.ArrayList;

/**
 * Created by ChomChom on 3/31/2017.
 */

public class CustomAdapterCompleteTextView extends BaseAdapter implements Filterable {

    private Context context;
    private int resource;
    private ArrayList<String> header;
    private ArrayList<Hobby> hobbies, temphobbies;
    private boolean control = true;
    private AutoCompleteTextView actvHobby;

    private Filter mFilter = new Filter() {
        private Object lock;

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            if (((Hobby) resultValue).isType() == true)
                return ((Hobby) resultValue).getHeaderHobby();
            else
                return ((Hobby) resultValue).getItemHobby();
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                synchronized (lock) {

                    ArrayList<Hobby> list = new ArrayList<Hobby>(hobbies);
                    results.values = list;
                    results.count = list.size();
                }

            } else {

                final String prefixString = constraint.toString().toLowerCase();

                ArrayList<Hobby> values = hobbies;
                int count = values.size();


                ArrayList<Hobby> newValues = new ArrayList<Hobby>();
                ArrayList<String> headerItem = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    String item = values.get(i).getItemHobby();
                    if (item.toLowerCase().contains(prefixString)) {
                        if (headerItem.size() == 0) {
                            headerItem.add(values.get(i).getHeaderHobby());
                            newValues.add(new Hobby(values.get(i).getHeaderHobby(), values.get(i).getItemHobby(), false));
                        } else {
                            int check = 0;
                            for (int j = 0; j < headerItem.size(); j++) {
                                if (headerItem.get(j).equals(values.get(i).getHeaderHobby()) == true)
                                    check = 1;
                            }
                            if (check == 0)
                                headerItem.add(values.get(i).getHeaderHobby());
                            newValues.add(new Hobby(values.get(i).getHeaderHobby(), values.get(i).getItemHobby(), false));
                        }
                    }

                }

                ArrayList<Hobby> result = new ArrayList<Hobby>();
                int j = 0;
                for (int i = 0; i < newValues.size(); i++) {
                    if (j < headerItem.size()) {
                        if (newValues.get(i).getHeaderHobby().equals(headerItem.get(j)) == true) {
                            result.add(new Hobby(headerItem.get(j), "", true));
                            j++;
                        }
                    }
                    result.add(newValues.get(i));
                }


                results.values = result;
                results.count = result.size();

            }

            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                hobbies = (ArrayList<Hobby>) results.values;
            } else {
                hobbies = (ArrayList<Hobby>) temphobbies;
            }
            notifyDataSetChanged();

        }
    };


    public CustomAdapterCompleteTextView(Context context, int resource, ArrayList<String> header, ArrayList<Hobby> hobbies, AutoCompleteTextView actvHobby) {
        this.context = context;
        this.resource = resource;
        this.header = header;
        this.hobbies = hobbies;
        this.actvHobby = actvHobby;
        this.temphobbies = new ArrayList<Hobby>(hobbies);
        Log.e("size", hobbies.size() + "");
    }

    @Override
    public int getCount() {

        return hobbies.size();
    }

    @Override
    public Object getItem(int position) {
        Log.e("size of header", hobbies.get(position) + "");
        return hobbies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (hobbies.get(position).isType() == true) {
            if (v == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = layoutInflater.inflate(R.layout.custom_adapter_completetextview_register_final, null);
                final TextView txtTitle = (TextView) v.findViewById(R.id.custom_adapter_completetextview_register_final_txt_content);
                txtTitle.setText(hobbies.get(position).getHeaderHobby());
                txtTitle.setBackgroundColor(Color.GRAY);
                txtTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actvHobby.setText(actvHobby.getText().toString());
                    }
                });
            }
        } else {
            if (v == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = layoutInflater.inflate(R.layout.custom_adapter_completetextview_register_final, null);
                final TextView txtTitle = (TextView) v.findViewById(R.id.custom_adapter_completetextview_register_final_txt_content);
                txtTitle.setText(hobbies.get(position).getItemHobby());
                txtTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actvHobby.getText().toString();

                        if(actvHobby.getText().toString()==null){
                            actvHobby.setText(txtTitle.getText().toString());
                        }
                        else if(actvHobby.getText().toString().endsWith(",")==true) {
                            actvHobby.setText(actvHobby.getText().toString() + txtTitle.getText().toString()+",");
                        }
                        else {
                            actvHobby.setText(actvHobby.getText().toString().substring(0,actvHobby.getText().toString().lastIndexOf(",")+1)+
                                    txtTitle.getText().toString()+",");
                        }
                    }
                });
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
}
