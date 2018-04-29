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

    @SerializedName("fileType")
    private String fileType;

    @SerializedName("username")
    private String username;

    @SerializedName("state")
    private String state;

    @SerializedName("country")
    private String country;

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

    @SerializedName("isSaved")
    private boolean isSaved;

    @SerializedName("videoThumb")
    private String videoThumb;

    @SerializedName("totalComments")
    private String totalComments;


    protected Posts(Parcel in) {
        post_id = in.readString();
        image = in.readString();
        fileType = in.readString();
        username = in.readString();
        state = in.readString();
        country = in.readString();
        profile_image = in.readString();
        created_date = in.readString();
        postmail = in.readString();
        description = in.readString();
        total_likes = in.readInt();
        liked = in.readByte() != 0;
        isSaved = in.readByte() != 0;
        videoThumb = in.readString();
        totalComments = in.readString();
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

    public String getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(post_id);
        dest.writeString(image);
        dest.writeString(fileType);
        dest.writeString(username);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(profile_image);
        dest.writeString(created_date);
        dest.writeString(postmail);
        dest.writeString(description);
        dest.writeInt(total_likes);
        dest.writeByte((byte) (liked ? 1 : 0));
        dest.writeByte((byte) (isSaved ? 1 : 0));
        dest.writeString(videoThumb);
        dest.writeString(totalComments);
    }
}
