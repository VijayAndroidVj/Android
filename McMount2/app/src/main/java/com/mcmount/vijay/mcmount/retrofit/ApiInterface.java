package com.mcmount.vijay.mcmount.retrofit;

import com.mcmount.vijay.mcmount.CategoryListModel;
import com.mcmount.vijay.mcmount.EventResponse;

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


    @POST("api/api-category.php")
    Call<CategoryListModel> category();

    @POST("api/api-product.php")
    @FormUrlEncoded
    Call<EventResponse> product(@Field("randuniq") String randuniq);

    @POST("api/api-brand.php")
    @FormUrlEncoded
    Call<EventResponse> brand(@Field("parent_id") String parent_id);

    @POST("api/api-model.php")
    Call<EventResponse> model(@Field("model_id") String model_id);

    @POST("api/mc-login/register.php")
    @FormUrlEncoded
    Call<EventResponse> register(@Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("mobile") String mobile);


    @POST("api/mc-login/login.php")
    @FormUrlEncoded
    Call<EventResponse> login(@Field("email") String email, @Field("password") String password);
}
