package com.xr.vijay.tranzpost.retrofit;


import com.xr.vijay.tranzpost.model.DocumentModel;
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
    Call<EventResponse> register(@Field("name") String name, @Field("mobile") String mobile, @Field("user_type") String user_type, @Field("email") String email, @Field("password") String password);


    @POST("signin.php")
    @FormUrlEncoded
    Call<EventResponse> signin(@Field("mobile") String username, @Field("password") String password);


    @POST("update_profile.php")
    @FormUrlEncoded
    Call<EventResponse> update_profile(@Field("name") String name, @Field("mobile") String mobile, @Field("email") String email, @Field("password") String password, @Field("alternate_mobile") String alternate_mobile, @Field("no_of_owned_trucks") String no_of_owned_trucks, @Field("no_of_attached_trucks") String no_of_attached_trucks, @Field("city") String city);


    @POST("saveandverify.php")
    @FormUrlEncoded
    Call<EventResponse> saveandverify(@Field("user_type") String user_type, @Field("mobile") String mobile, @Field("company") String company, @Field("date") String date);


    @Multipart
    @POST("upload_document.php")
    Call<EventResponse> upload_document(
            @Part MultipartBody.Part mobile,
            @Part MultipartBody.Part user_type,
            @Part MultipartBody.Part uploadDocumentType,
            @Part MultipartBody.Part company,
            @Part MultipartBody.Part date,
            @Part MultipartBody.Part uploadimage);

    @Multipart
    @POST("add_truck.php")
    Call<DocumentModel> add_truck(
            @Part MultipartBody.Part truck_model,
            @Part MultipartBody.Part mobile,
            @Part MultipartBody.Part body_type,
            @Part MultipartBody.Part truck_weight,
            @Part MultipartBody.Part permit_type,
            @Part MultipartBody.Part truck_photo1,
            @Part MultipartBody.Part truck_photo2,
            @Part MultipartBody.Part truck_photo3,
            @Part MultipartBody.Part truck_photo4);

}

