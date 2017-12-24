package com.instag.vijay.fasttrending.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 26/11/17.
 */

public class Comments {


    @SerializedName("username")
    private String username;

    @SerializedName("user_email")
    private String user_email;

    @SerializedName("profile_image")
    private String profile_image;

    @SerializedName("comment_id")
    private String comment_id;

    @SerializedName("comment")
    private String comment;

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getComments_id() {
        return comment_id;
    }

    public void setComments_id(String comments_id) {
        this.comment_id = comments_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
