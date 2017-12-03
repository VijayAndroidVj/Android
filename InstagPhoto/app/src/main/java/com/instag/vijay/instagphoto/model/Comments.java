package com.instag.vijay.instagphoto.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 26/11/17.
 */

public class Comments {


    @SerializedName("username")
    private String username;

    @SerializedName("user_email")
    private String user_email;


    @SerializedName("comments_id")
    private String comments_id;

    @SerializedName("comment")
    private String comment;

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
        return comments_id;
    }

    public void setComments_id(String comments_id) {
        this.comments_id = comments_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
