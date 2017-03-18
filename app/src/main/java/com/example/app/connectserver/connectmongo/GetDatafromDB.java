package com.example.app.connectserver.connectmongo;

import android.os.AsyncTask;

import com.example.app.connectserver.model.UserName;

import java.util.List;

/**
 * Created by ChomChom on 3/16/2017.
 */

public class GetDatafromDB extends AsyncTask<Void,List<UserName>,Void> {
    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    public GetDatafromDB() {
        super();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(List<UserName>... values) {
        super.onProgressUpdate(values);
    }
}
