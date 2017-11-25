package com.instag.vijay.instagphoto.retrofit;

import com.instag.vijay.instagphoto.EventResponse;
import com.instag.vijay.instagphoto.FavModel;
import com.instag.vijay.instagphoto.model.PostModelMain;

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
    Call<EventResponse> register(@Field("name") String name, @Field("username") String username, @Field("email") String email, @Field("password") String password);


    @POST("login.php")
    @FormUrlEncoded
    Call<EventResponse> login(@Field("username") String username, @Field("password") String password);


    @POST("add_follow.php")
    @FormUrlEncoded
    Call<EventResponse> add_follow(@Field("useremail") String useremail, @Field("emailtofollow") String emailtofollow, @Field("follow") boolean follow);

    @POST("follow_followers.php")
    @FormUrlEncoded
    Call<ArrayList<FavModel>> follow_followers(@Field("useremail") String useremail, @Field("following") boolean following);

    @POST("search_user.php")
    @FormUrlEncoded
    Call<ArrayList<FavModel>> search_user(@Field("useremail") String useremail, @Field("searchname") String searchname);

    @Multipart
    @POST("insta_posts.php")
    Call<EventResponse> insta_posts(
            @Part MultipartBody.Part description,
            @Part MultipartBody.Part image
    );

    @POST("getposts.php")
    @FormUrlEncoded
    Call<PostModelMain> getposts(@Field("useremail") String useremail);


    @POST("delete_post.php")
    @FormUrlEncoded
    Call<EventResponse> delete_post(@Field("useremail") String useremail, @Field("post_id") String post_id);


    @POST("post_like.php")
    @FormUrlEncoded
    Call<EventResponse> post_like(@Field("useremail") String useremail, @Field("username") String username, @Field("post_id") String post_id, @Field("like") boolean like);


    @POST("post_comments.php")
    @FormUrlEncoded
    Call<EventResponse> post_comments(@Field("useremail") String useremail, @Field("username") String username, @Field("post_id") String post_id, @Field("comment") String comment);


}

