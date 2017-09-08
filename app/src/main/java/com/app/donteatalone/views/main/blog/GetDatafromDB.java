package com.app.donteatalone.views.main.blog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.model.UserName;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ChomChom on 3/16/2017
 */

public class GetDatafromDB extends AsyncTask<String,ArrayList<InfoBlog>, ArrayList<InfoBlog>> {

    private ArrayList<InfoBlog> infoBlog;
    private Context context;
    private View view;
    private String phone;
    public GetDatafromDB(Context context, View view) {
        super();
        infoBlog=new ArrayList<>();
        this.context=context;
        this.view=view;
    }

    @Override
    protected ArrayList<InfoBlog> doInBackground(String... params) {
        phone=params[0];
        Connect connect=new Connect();
        Call<ArrayList<InfoBlog>> userLogin = connect.getRetrofit().getListInfoBlog(phone);
        userLogin.enqueue(new Callback<ArrayList<InfoBlog>>() {
            @Override
            public void onResponse(Call<ArrayList<InfoBlog>> call, Response<ArrayList<InfoBlog>> response) {
                if(response.body()!=null) {
                    infoBlog = response.body();
                    Collections.reverse(infoBlog);
                    onProgressUpdate(infoBlog);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InfoBlog>> call, Throwable t) {
            }
        });
        return infoBlog;
    }

    @Override
    protected void onProgressUpdate(ArrayList<InfoBlog>... infoBlog) {
        super.onProgressUpdate(infoBlog);
        ArrayList<ArrayList<InfoBlog>> myInfoBlogs = new ArrayList<>();
        for(int i=0;i<infoBlog[0].size();i=i+2){
            ArrayList<InfoBlog> temp=new ArrayList<>();
            temp.add(infoBlog[0].get(i));
            if((i+1)< infoBlog[0].size()) {
                temp.add(infoBlog[0].get(i + 1));
            }
            myInfoBlogs.add(temp);
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_blog_rcv_my_blog);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        CustomBlogRecyclerViewAdapter adapter=new CustomBlogRecyclerViewAdapter(context,myInfoBlogs);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPostExecute(ArrayList<InfoBlog> infoBlog) {
        super.onPostExecute(infoBlog);
        //saveReference(userName);

    }

    private void saveReference(UserName userName){
        SharedPreferences sharedPreferences=context.getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("phoneLogin", userName.getPhone());
        editor.putString("fullnameLogin",userName.getFullname());
        editor.putString("passwordLogin", userName.getPassword());
        editor.putString("genderLogin",userName.getGender());
        editor.putString("avatarLogin",userName.getAvatar());
        editor.putString("birthdayLogin",userName.getBirthday());
        editor.putString("addressLogin",userName.getAddress());
        editor.putString("hobbyLogin",userName.getHobby());
        editor.putString("characterLogin",userName.getCharacter());
        editor.commit();
    }
}
