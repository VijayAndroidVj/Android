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

    @SerializedName("videoThumb")
    private String videoThumb;

    @SerializedName("fileType")
    private String fileType;

    @SerializedName("profile_image")
    private String profile_image;

    @SerializedName("post_id")
    private String post_id;

    @SerializedName("type")
    private String type;

    @SerializedName("following")
    private boolean following;

    @SerializedName("from_email")
    private String from_email;

    @SerializedName("created_date")
    private String created_date;

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public boolean getFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public String getFrom_email() {
        return from_email;
    }

    public void setFrom_email(String from_email) {
        this.from_email = from_email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

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

    public String getVideoThumb() {
        return videoThumb;
    }

    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
