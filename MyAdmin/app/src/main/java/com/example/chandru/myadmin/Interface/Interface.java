package com.example.chandru.myadmin.Interface;

import com.example.chandru.myadmin.Pojo.LoginRespose;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Interface {
    @GET("societyLogin/")
    Call<LoginRespose>getRespose(@Url String Url);

    @POST("RegistersocietyReq/")
    Call<LoginRespose>postDetails (@Url String url);
}
