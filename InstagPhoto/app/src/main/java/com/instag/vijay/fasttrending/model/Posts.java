package com.instag.vijay.fasttrending.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 25/11/17.
 */

public class Posts implements Parcelable {

    @SerializedName("post_id")
    private String post_id;

    @SerializedName("image")
    private String image;

    @SerializedName("username")
    private String username;

    @SerializedName("profile_image")
    private String profile_image;

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

    protected Posts(Parcel in) {
        post_id = in.readString();
        image = in.readString();
        username = in.readString();
        profile_image = in.readString();
        created_date = in.readString();
        postmail = in.readString();
        description = in.readString();
        total_likes = in.readInt();
        liked = in.readByte() != 0;
    }

    public static final Creator<Posts> CREATOR = new Creator<Posts>() {
        @Override
        public Posts createFromParcel(Parcel in) {
            return new Posts(in);
        }

        @Override
        public Posts[] newArray(int size) {
            return new Posts[size];
        }
    };

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(post_id);
        parcel.writeString(image);
        parcel.writeString(username);
        parcel.writeString(profile_image);
        parcel.writeString(created_date);
        parcel.writeString(postmail);
        parcel.writeString(description);
        parcel.writeInt(total_likes);
        parcel.writeByte((byte) (liked ? 1 : 0));
    }
}
