package com.instag.vijay.instagphoto.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 25/11/17.
 */

public class Posts {

    @SerializedName("post_id")
    private String post_id;

    @SerializedName("image")
    private String image;

    @SerializedName("username")
    private String username;

    @SerializedName("created_date")
    private String created_date;

    @SerializedName("postmail")
    private String postmail;

    @SerializedName("description")
    private String description;

    @SerializedName("total_likes")
    private int total_likes;


    @SerializedName("liked")
    private boolean liked;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(int total_likes) {
        this.total_likes = total_likes;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getPostmail() {
        return postmail;
    }

    public void setPostmail(String postmail) {
        this.postmail = postmail;
    }
}
