package com.app.donteatalone.connectmongo;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by ChomChom on 3/13/2017.
 */

public class Connect {

//    private static String API_BASE_URL = "http://10.0.214.87:3000/"; //KTX
    private static String API_BASE_URL = "http://10.0.128.134:3000/"; //KTX's Nga

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
