package com.app.donteatalone.connectmongo;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by ChomChom on 3/13/2017.
 */

public class Connect {
    //private static String API_BASE_URL = "http://10.0.128.134:3000/"; //KTX

    //private static String API_BASE_URL = "http://192.168.5.46:3000/"; //Test

   private static String API_BASE_URL = "http://192.168.31.168:3000/"; //Test

    //private static String API_BASE_URL ="http://192.168.28.232:3000";//Feel

    public static Structure getRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        Structure client = retrofit.create(Structure.class);

        return client;
    }
}
