package com.xr.vijay.tranzpost.retrofit;


import com.xr.vijay.tranzpost.model.EventResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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


    @POST("register.php")
    @FormUrlEncoded
    Call<EventResponse> register(@Field("name") String name, @Field("mobile") String username, @Field("email") String email, @Field("password") String password);


    @POST("signin.php")
    @FormUrlEncoded
    Call<EventResponse> signin(@Field("mobile") String username, @Field("password") String password);

}

