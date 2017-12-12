package com.app.donteatalone.connectmongo;

import com.app.donteatalone.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by ChomChom on 3/13/2017
 */

public class Connect {

//    private static String API_BASE_URL = "http://10.0.214.87:3000/"; //KTX'Han
    private static String API_BASE_URL = "https://firstapp0609.herokuapp.com/"; //KTX's Nga

    private static String API_RESTAURANT_URL = "http://api.foodcute.tk/";

    private static final long CONNECT_TIMEOUT = 30000;
    private static final long READ_TIMEOUT = 30000;

    public static Structure getRetrofit() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit =
                builder
                        .client(httpClient.build())
                        .build();

        return retrofit.create(Structure.class);
    }


    public static Structure getRestaurant() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_RESTAURANT_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit =
                builder
                        .client(httpClient.build())
                        .build();

        return retrofit.create(Structure.class);
    }

}
