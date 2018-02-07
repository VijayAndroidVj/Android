package com.mcmount.vijay.mcmount.retrofit;

import com.mcmount.vijay.mcmount.CategoryListModel;
import com.mcmount.vijay.mcmount.EventResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("api/api-product.php")
    Call<CategoryListModel> product(@Query("randuniq") String randuniq);

    @GET("api/api-brand.php")
    Call<CategoryListModel> brand(@Query("parent_id") String parent_id);

    @GET("api/api-model.php")
    Call<CategoryListModel> model(@Query("model_id") String model_id);

    @POST("api/change_password.php")
    @FormUrlEncoded
    Call<EventResponse> change_password(@Field("oldpass") String oldpass, @Field("newpass") String newpass, @Field("userlogin") String userlogin);

    @POST("api/register.php")
    @FormUrlEncoded
    Call<EventResponse> register(@Field("name") String name, @Field("email") String email, @Field("userlogin") String userlogin, @Field("mobile") String mobile, @Field("state") String state, @Field("pin") String pin, @Field("city") String city, @Field("address") String address);

    //    http://mcmount.com/api/login.php?name=9790187922&pass=841138
    @GET("api/login.php")
    Call<EventResponse> login(@Query("name") String name, @Query("pass") String pass);

    //    http://mcmount.com/api/api-login-otp.php?mobile=9790187922
    @POST("api/api-login-otp.php")
    @FormUrlEncoded
    Call<EventResponse> login_otp(@Field("mobile") String mobile);

    //    http://mcmount.com/api/api-check-otp.php?mobile=9790187922&otp=6801
    @POST("api/api-check-otp.php")
    @FormUrlEncoded
    Call<EventResponse> check_otp(@Field("mobile") String mobile, @Field("otp") String otp);
}
