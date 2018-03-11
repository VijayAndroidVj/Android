package com.peeyemcar.retrofit;

import com.peeyemcar.models.EventResponse;
import com.peeyemcar.models.Vechile;

import java.util.ArrayList;

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
    Call<EventResponse> register(@Field("aid") String aid, @Field("mobile") String mobile, @Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("userVehicleType") String userVehicleType, @Field("userVehicleNumber") String userVehicleNumber);


    @POST("customer_login.php")
    @FormUrlEncoded
    Call<EventResponse> login(@Field("aid") String aid, @Field("mobile") String mobile, @Field("password") String password, @Field("userVehicleType") String userVehicleType, @Field("userVehicleNumber") String userVehicleNumber);


    @POST("post_feedback.php")
    @FormUrlEncoded
    Call<EventResponse> post_feedback(@Field("aid") String aid, @Field("email") String email, @Field("service_quality") Float service_quality, @Field("service_initiation") Float service_initiation, @Field("service_advisor") Float service_advisor, @Field("service_facility") Float service_facility, @Field("remarks") String remarks);

    @POST("getmyvechile.php")
    @FormUrlEncoded
    Call<ArrayList<Vechile>> getmyvechile(@Field("aid") String aid, @Field("email") String email, @Field("mobile") String mobile);


    @POST("book_service.php")
    @FormUrlEncoded
    Call<EventResponse> book_service(@Field("aid") String aid,
                                     @Field("service_type") String service_type,
                                     @Field("model") String model,
                                     @Field("registration_number") String registration_number,
                                     @Field("mileage") String mileage,
                                     @Field("service_date") String service_date,
                                     @Field("pickup") String pickup,
                                     @Field("name") String name,
                                     @Field("mobilenumber") String mobilenumber,
                                     @Field("email") String email,
                                     @Field("state") String state,
                                     @Field("city") String city,
                                     @Field("description") String description);

    @POST("customer_testdrive.php")
    @FormUrlEncoded
    Call<EventResponse> customer_testdrive(@Field("aid") String aid,
                                           @Field("fuel_type") String fuel_type,
                                           @Field("model") String model,
                                           @Field("prefered_date") String prefered_date,
                                           @Field("name") String name,
                                           @Field("mobilenumber") String mobilenumber,
                                           @Field("email") String email,
                                           @Field("state") String state,
                                           @Field("city") String city,
                                           @Field("location") String location,
                                           @Field("vehicle") String vehicle,
                                           @Field("comment") String comment);


}


