package com.instag.vijay.fasttrending.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by vijay on 25/11/17.
 */

public class PostModelMain {

    @SerializedName("totalposts")
    private int totalposts;

    @SerializedName("total_followering")
    private int total_followering;

    @SerializedName("total_followers")
    private int total_followers;

    @SerializedName("profile_image")
    private String profile_image;

    @SerializedName("username")
    private String username;

    @SerializedName("data")
    private ArrayList<Posts> postsArrayList = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public int getTotalposts() {
        return totalposts;
    }

    public void setTotalposts(int totalposts) {
        this.totalposts = totalposts;
    }

    public int getTotal_followering() {
        return total_followering;
    }

    public void setTotal_followering(int total_followering) {
        this.total_followering = total_followering;
    }

    public int getTotal_followers() {
        return total_followers;
    }

    public void setTotal_followers(int total_followers) {
        this.total_followers = total_followers;
    }

    public ArrayList<Posts> getPostsArrayList() {
        return postsArrayList;
    }

    public void setPostsArrayList(ArrayList<Posts> postsArrayList) {
        this.postsArrayList = postsArrayList;
    }
}
