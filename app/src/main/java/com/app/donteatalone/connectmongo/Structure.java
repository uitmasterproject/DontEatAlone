package com.app.donteatalone.connectmongo;

import com.app.donteatalone.model.Achievement;
import com.app.donteatalone.model.Auth;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.model.InfoNotification;
import com.app.donteatalone.model.InfoProfileUpdate;
import com.app.donteatalone.model.InitParam;
import com.app.donteatalone.model.ProfileHistoryModel;
import com.app.donteatalone.model.Request;
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
 * Created by ChomChom on 3/13/2017
 */

public interface Structure {

        @POST("/token")
        Call<Request> getToken(@Body Auth data);

        @GET("/init/{phone}")
        Call<InitParam> getInitParam(@Path("phone") String phone);

        @POST("/user/{phone}")
        Call<UserName> getProfileUser(@Path("phone") String phone);

        @POST("/login")
        Call<Status> checkAccount(@Body UserName user);

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
        @PUT("/statusblog/edit/{phone}")
        Call<Status> editStatusBlog(@Body InfoBlog infoBlog, @Path("phone") String phone);

        @GET("/notification/{phone}")
        Call<ArrayList<InfoNotification>> getNotification(@Path("phone") String phone);
        @PUT("/notification_update_read/{phoneRecevice}/{phoneSend}/{status}/{timeSend}")
        Call<Status> updateReadNotification(@Path("phoneRecevice") String phoneRecevice, @Path("phoneSend") String phoneSend,
                                                @Path("status") String status,@Path("timeSend") String timeSend);


        @POST("/profile_update")
        Call<UserName> updateProfile(@Body InfoProfileUpdate item);
        @GET("/profile/allImage/{phone}")
        Call<ArrayList<String>> getAllImage(@Path("phone") String phone);
        @GET("/profile/publicBlog/{phone}/{limit}")
        Call<ArrayList<InfoBlog>> getPublicBlog(@Path("phone") String phone, @Path("limit") String limit);
        @POST("/profile/edit")
        Call<Status> editProfile(@Body UserName userName);


        @GET("/restaurant/{latlng}")
        Call<ArrayList<Restaurant>> getRestaurant(@Path("latlng") String latlng);
        @GET("/restaurant/latlng/{latlng}")
        Call<ArrayList<Restaurant>> getRestaurantFollowLatlng(@Path("latlng") String latlng);
        @GET("/restaurant/district/{district}")
        Call<ArrayList<Restaurant>> getRestaurantFollowDistrict(@Path("district") String district);
        @GET("/restaurant")
        Call<ArrayList<Restaurant>> getAllRestaurant();

        @GET("/eventhistory/{phone}")
        Call<ArrayList<ProfileHistoryModel>> getEventHistory(@Path("phone") String phone);
        @POST("/eventhistory/edit/{phone}")
        Call<ArrayList<ProfileHistoryModel>> editEventHistory(@Path("phone") String phone, @Body ProfileHistoryModel profileHistoryModel);


        @GET("/accordant/{phone}/{type}")
        Call<Status> addLike(@Path("phone") String phone, @Path("type") int type);

        @GET("/accordant/{phone}")
        Call<Achievement> getAchievement(@Path("phone") String phone);
}
