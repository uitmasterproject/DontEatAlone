package com.app.donteatalone.connectmongo;

import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.model.UserName;

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




        @POST("/user/{phone}")
        Call<UserName> getProfileUser(@Path("phone") String phone);

        @POST("/login/{phone}/{password}")
        Call<Status> checkAccount(@Path("phone") String phone, @Path("password") String password);

        @POST("/register")
        Call<Status> insertUser(@Body UserName user);

        @POST("/register/{phone}")
        Call<Status> checkPhoneExits(@Path("phone") String phone);

        @PUT("/forgetpass/{phone}/{password}")
        Call<Status> changePass(@Path("phone") String phone, @Path("password") String password);


        @DELETE("/user/{id}")
        Call<List<UserName>> deleteUser(@Path("id") String id );
        @PUT("/user/{id}")
        Call<List<UserName>> updateUser(@Path("id") String id );



        @POST("/statusblog")
        Call<Status> addStatusBlog(@Body InfoBlog infoBlog);
}
