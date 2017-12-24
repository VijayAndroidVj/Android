package com.instag.vijay.fasttrending.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 24/12/17.
 */

public class Notification {

    @SerializedName("notificationid")
    private String notificationid;

    @SerializedName("username")
    private String username;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String description;

    @SerializedName("postimage")
    private String postimage;

    @SerializedName("profile_image")
    private String profile_image;

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(String notificationid) {
        this.notificationid = notificationid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
