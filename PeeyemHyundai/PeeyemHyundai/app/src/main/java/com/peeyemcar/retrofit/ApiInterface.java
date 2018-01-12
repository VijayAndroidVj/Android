package com.peeyemcar.retrofit;

import com.peeyemcar.models.EventResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by iyara_rajan on 06-07-2017.
 */

public interface ApiInterface {


    @POST("get_used_cars.php")
    @FormUrlEncoded
    Call<EventResponse> get_used_cars(@Field("aid") String aid);


    @POST("customer_signup.php")
    @FormUrlEncoded
    Call<EventResponse> register(@Field("aid") String aid, @Field("mobile") String mobile, @Field("name") String name, @Field("email") String email, @Field("password") String password);


    @POST("customer_login.php")
    @FormUrlEncoded
    Call<EventResponse> login(@Field("email") String email, @Field("password") String password);


}


