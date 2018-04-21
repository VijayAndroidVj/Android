package com.instag.vijay.fasttrending.retrofit;

import com.instag.vijay.fasttrending.EventResponse;
import com.instag.vijay.fasttrending.FavModel;
import com.instag.vijay.fasttrending.UserModel;
import com.instag.vijay.fasttrending.model.CategoryItem;
import com.instag.vijay.fasttrending.model.CategoryMain;
import com.instag.vijay.fasttrending.model.Comments;
import com.instag.vijay.fasttrending.model.Notification;
import com.instag.vijay.fasttrending.model.PostModelMain;
import com.instag.vijay.fasttrending.model.Posts;
import com.instag.vijay.fasttrending.model.SubCategory;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @POST("follow_followers.php")
    @FormUrlEncoded
    Call<ArrayList<FavModel>> follow_followers(@Field("useremail") String useremail, @Field("following") boolean following);

    @POST("likeslist.php")
    @FormUrlEncoded
    Call<ArrayList<FavModel>> likeslist(@Field("postid") String postid, @Field("myemail") String myemail);

    @POST("getnotification.php")
    @FormUrlEncoded
    Call<ArrayList<Notification>> getnotification(@Field("followers") boolean followers, @Field("useremail") String useremail);


    @POST("search_user.php")
    @FormUrlEncoded
    Call<ArrayList<FavModel>> search_user(@Field("useremail") String useremail, @Field("searchname") String searchname);

    @Multipart
    @POST("FEELOUTADMIN/list/add_business_page.php")
    Call<EventResponse> add_business_page(
            @Part MultipartBody.Part title,
            @Part MultipartBody.Part uploaded_file,
            @Part MultipartBody.Part txtEmpPreAddress,
            @Part MultipartBody.Part state,
            @Part MultipartBody.Part city,
            @Part MultipartBody.Part txtEmpContact,
            @Part MultipartBody.Part category,
            @Part MultipartBody.Part scategory,
            @Part MultipartBody.Part key,
            @Part MultipartBody.Part email
    );


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
            @Part MultipartBody.Part uploadCoverimage,
            @Part MultipartBody.Part profileName,
            @Part MultipartBody.Part username,
            @Part MultipartBody.Part email,
            @Part MultipartBody.Part password,
            @Part MultipartBody.Part profile_image,
            @Part MultipartBody.Part cover_image,
            @Part MultipartBody.Part aboutmemul,
            @Part MultipartBody.Part statemul,
            @Part MultipartBody.Part countrymul,
            @Part MultipartBody.Part contact,
            @Part MultipartBody.Part gendermul,
            @Part MultipartBody.Part privacyOn,
            @Part MultipartBody.Part statusmull,
            @Part MultipartBody.Part web_info_mul);

    @POST("getposts.php")
    @FormUrlEncoded
    Call<PostModelMain> getposts(@Field("useremail") String useremail, @Field("myemail") String myemail);

    @POST("getpostsnew.php")
    @FormUrlEncoded
    Call<PostModelMain> getpostsnew(@Field("myemail") String myemail);


    @POST("getallLists.php")
    @FormUrlEncoded
    Call<ArrayList<UserModel>> getallLists(@Field("myemail") String myemail, @Field("action") String action);

    @POST("getmynewsfeed.php")
    @FormUrlEncoded
    Call<PostModelMain> getmynewsfeed(@Field("useremail") String useremail);

    @POST("get_friends_post.php")
    @FormUrlEncoded
    Call<PostModelMain> get_friends_post(@Field("myemail") String myemail);

    @POST("get_liked_posts.php")
    @FormUrlEncoded
    Call<PostModelMain> get_liked_post(@Field("myemail") String myemail);

    @POST("getcategory.php")
    @FormUrlEncoded
    Call<ArrayList<CategoryMain>> getcategory(@Field("useremail") String useremail);

    @GET("get_business_subcategory_list.php")
    Call<ArrayList<SubCategory>> get_business_subcategory_list(@Query("category") String category);

    @POST("getbanner.php")
    @FormUrlEncoded
    Call<ArrayList<CategoryMain>> getbanner(@Field("useremail") String useremail);

    @POST("getcategory_item_list.php")
    @FormUrlEncoded
    Call<ArrayList<CategoryItem>> getcategoryItemList(@Field("_id") String _id);

    @POST("getTrendingVideos.php")
    @FormUrlEncoded
    Call<ArrayList<Posts>> getTrendingVideos(@Field("useremail") String useremail);


    @POST("getsearchpost.php")
    @FormUrlEncoded
    Call<PostModelMain> getsearchpost(@Field("useremail") String useremail);


    @POST("getcomments.php")
    @FormUrlEncoded
    Call<ArrayList<Comments>> getcomments(@Field("post_id") String post_id);


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
}


