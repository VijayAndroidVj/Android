package com.vajralabs.uniroyal.uniroyal.retrofit;


import com.vajralabs.uniroyal.uniroyal.model.BannerModel;
import com.vajralabs.uniroyal.uniroyal.model.CategoryModel;
import com.vajralabs.uniroyal.uniroyal.model.EventResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by iyara_rajan on 06-07-2017.
 */

public interface ApiInterface {

    @POST("download_catelog.php")
    @FormUrlEncoded
    Call<EventResponse> download_catelog(@Field("name") String name, @Field("company_name") String company_name, @Field("email") String email, @Field("mobile") String phoneNumber);

    @POST("customer_enquiry.php")
    @FormUrlEncoded
    Call<EventResponse> customer_enquiry(@Field("name") String name, @Field("email") String email, @Field("mobile") String mobile, @Field("customerEnquiry") String customerEnquiry);

    @POST("get_banners.php")
    Call<ArrayList<BannerModel>> get_banners();

    @POST("getallcategories.php")
    Call<ArrayList<CategoryModel>> getcategoryList();
}


