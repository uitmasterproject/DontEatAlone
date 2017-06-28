package com.app.donteatalone.connectmongo;

import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.model.InfoNotification;
import com.app.donteatalone.model.InfoProfileUpdate;
import com.app.donteatalone.model.Restaurant;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.model.UserName;

import java.util.ArrayList;
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



        @POST("/statusblog/{phone}")
        Call<Status> addStatusBlog(@Body InfoBlog infoBlog, @Path("phone") String phone);
        @GET("/statusblog/{phone}")
        Call<ArrayList<InfoBlog>> getListInfoBlog(@Path("phone") String phone);
        @DELETE("/statusblog/{phone}/{date}")
        Call<Status> deleteStatusBlog(@Path("phone")String phone,@Path("date") String date);


        @GET("/notification/{phone}")
        Call<ArrayList<InfoNotification>> getNotification(@Path("phone") String phone);
        @PUT("/notification_update_read/{phoneRecevice}/{phoneSend}/{status}/{timeSend}")
        Call<Status> updateReadNotification(@Path("phoneRecevice") String phoneRecevice, @Path("phoneSend") String phoneSend,
                                                @Path("status") String status,@Path("timeSend") String timeSend);


        @POST("/profile_update")
        Call<Status> updateProfile(@Body InfoProfileUpdate item);

        @GET("/restaurant/{latlng}")
        Call<ArrayList<Restaurant>> getRestaurant(@Path("latlng") String latlng);
}
