package com.instag.vijay.instagphoto.retrofit;

import com.instag.vijay.instagphoto.EventResponse;
import com.instag.vijay.instagphoto.FavModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by iyara_rajan on 06-07-2017.
 */

public interface ApiInterface {

//    http://mcmount.com/api/api-category.php
//    http://mcmount.com/api/api-product.php?randuniq=XXXXXX
//    http://mcmount.com/api/api-brand.php?parent_id=XXXXXX
//    http://mcmount.com/api/api-model.php?model_id=XXXXXXX
//    http://mcmount.com/api/mc-login/register.php?name=xxx&email=xxx&password=xxx&mobile=xxx
//    http://mcmount.com/api/mc-login/login.php?email=xxx&password=xxxx


    @POST("signup.php")
    @FormUrlEncoded
    Call<EventResponse> register(@Field("name") String name, @Field("username") String username, @Field("email") String email, @Field("password") String password);


    @POST("login.php")
    @FormUrlEncoded
    Call<EventResponse> login(@Field("username") String username, @Field("password") String password);


    @POST("add_follow.php")
    @FormUrlEncoded
    Call<EventResponse> add_follow(@Field("useremail") String useremail, @Field("emailtofollow") String emailtofollow, @Field("follow") boolean follow);

    @POST("follow_followers.php")
    @FormUrlEncoded
    Call<ArrayList<FavModel>> follow_followers(@Field("useremail") String useremail, @Field("following") boolean following);
}

