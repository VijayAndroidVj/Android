package com.example.chandru.laundry.Api;


import com.example.chandru.laundry.Pojo.ServiceMain;
import com.example.chandru.laundry.Pojo.addservice;
import com.example.chandru.laundry.Pojo.customer;
import com.example.chandru.laundry.Pojo.login;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("login.php/api/login.php")
    Call<login> getLogin(@Query("username") String username, @Query("password") String password);

    @GET("add_customer.php?")
    Call<customer> getCustomer(@Query("name") String name, @Query("address") String address, @Query("contact") String contact);

    @GET("service.php")
    Call<ServiceMain> getService();

    @GET("location.php")
    Call<ServiceMain> location();

    @GET("customer_details.php")
    Call<ServiceMain> customer_details();

    @GET("add_location.php?")
    Call<customer> getlocation(@Query("location_name") String name, @Query("description") String address);


    @POST("/posts")
    @FormUrlEncoded
    Call<addservice> savePost(@Field("icon_image") String title,
                              @Field("name") String body,
                              @Field("description") String image);


    @Multipart
    @POST("add_service.php")
    Call<customer> add_service(
            @Part MultipartBody.Part name,
            @Part MultipartBody.Part description,
            @Part MultipartBody.Part icon_image

    );

    @Multipart
    @POST("add_product.php ")
    Call<customer> add_product(

            @Part MultipartBody.Part name,
            @Part MultipartBody.Part quantity,
            @Part MultipartBody.Part price,
            @Part MultipartBody.Part service_,
            @Part MultipartBody.Part icon_image

    );
}
