package com.example.app.connectserver.connectmongo;

import com.example.app.connectserver.model.UserName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ChomChom on 3/13/2017.
 */

public interface Structure {
        @GET("/user")
        Call<List<UserName>> getUser();
        @GET("/user/{phone}")
        Call<List<UserName>> getPhoneUser(@Path("phone") String phone);
        @POST("/user")
        Call<List<UserName>> setUser(@Body UserName user);
        @DELETE("/user/{id}")
        Call<List<UserName>> deleteUser(@Path("id") String id );
        @PUT("/user/{id}")
        Call<List<UserName>> updateUser(@Path("id") String id );
}
