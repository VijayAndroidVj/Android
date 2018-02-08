package com.vajralabs.uniroyal.uniroyal.retrofit;


import com.vajralabs.uniroyal.uniroyal.model.BannerModel;
import com.vajralabs.uniroyal.uniroyal.model.CategoryModel;
import com.vajralabs.uniroyal.uniroyal.model.EventResponse;
import com.vajralabs.uniroyal.uniroyal.model.PostModelMain;
import com.vajralabs.uniroyal.uniroyal.model.Posts;

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


    @POST("signup.php")
    @FormUrlEncoded
    Call<EventResponse> register(@Field("country") String country, @Field("gender") String gender, @Field("name") String name, @Field("username") String username, @Field("email") String email, @Field("password") String password);


    @POST("user_name_availability.php")
    @FormUrlEncoded
    Call<EventResponse> user_name_availability(@Field("searchname") String searchname);


    @POST("login.php")
    @FormUrlEncoded
    Call<EventResponse> login(@Field("username") String username, @Field("password") String password, @Field("email") String email);


    @POST("add_follow.php")
    @FormUrlEncoded
    Call<EventResponse> add_follow(@Field("useremail") String useremail, @Field("emailtofollow") String emailtofollow, @Field("follow") boolean follow);


    @Multipart
    @POST("insta_posts.php")
    Call<EventResponse> insta_posts(
            @Part MultipartBody.Part description,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part fileType,
            @Part MultipartBody.Part videoThumb,
            @Part MultipartBody.Part user_mail

    );

    @Multipart
    @POST("update_profile.php")
    Call<EventResponse> update_profile(
            @Part MultipartBody.Part uploadimage,
            @Part MultipartBody.Part username,
            @Part MultipartBody.Part email,
            @Part MultipartBody.Part password,
            @Part MultipartBody.Part profile_image,
            @Part MultipartBody.Part aboutmemul,
            @Part MultipartBody.Part statemul,
            @Part MultipartBody.Part countrymul,
            @Part MultipartBody.Part gendermul);

    @POST("getposts.php")
    @FormUrlEncoded
    Call<PostModelMain> getposts(@Field("useremail") String useremail, @Field("myemail") String myemail);

    @POST("getmynewsfeed.php")
    @FormUrlEncoded
    Call<PostModelMain> getmynewsfeed(@Field("useremail") String useremail);

    @POST("getsearchpost.php")
    @FormUrlEncoded
    Call<PostModelMain> getsearchpost(@Field("useremail") String useremail);


    @POST("get_banners.php")
    Call<ArrayList<BannerModel>> get_banners();


    @POST("delete_post.php")
    @FormUrlEncoded
    Call<EventResponse> delete_post(@Field("useremail") String useremail, @Field("post_id") String post_id);

    @POST("deleteNotification.php")
    @FormUrlEncoded
    Call<EventResponse> deleteNotification(@Field("notificationid") String post_id);

    @POST("delete_my_account.php")
    @FormUrlEncoded
    Call<EventResponse> delete_my_account(@Field("useremail") String useremail);

    @POST("post_like.php")
    @FormUrlEncoded
    Call<EventResponse> post_like(@Field("useremail") String useremail, @Field("username") String username, @Field("post_id") String post_id, @Field("like") boolean like);

    @POST("post_comments.php")
    @FormUrlEncoded
    Call<EventResponse> post_comments(@Field("user_email") String user_email, @Field("username") String username, @Field("post_id") String post_id, @Field("comment") String comment);

    @POST("delete_comment.php")
    @FormUrlEncoded
    Call<EventResponse> delete_comment(@Field("useremail") String useremail, @Field("comment_id") String comment_id);

    @POST("register_fcm.php")
    @FormUrlEncoded
    Call<EventResponse> register_fcm(@Field("email") String email, @Field("fcm_token") String fcm_token);

    @POST("getpostbyid.php")
    @FormUrlEncoded
    Call<Posts> getpostbyid(@Field("postId") String postId, @Field("email") String email);

    @POST("getallcategories.php")
    Call<ArrayList<CategoryModel>> getcategoryList();
}


