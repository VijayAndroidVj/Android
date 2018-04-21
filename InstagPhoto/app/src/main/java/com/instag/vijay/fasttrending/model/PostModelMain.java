package com.instag.vijay.fasttrending.model;

import com.google.gson.annotations.SerializedName;
import com.instag.vijay.fasttrending.UserModel;

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

    @SerializedName("friendsCount")
    private int friendsCount;

    @SerializedName("profile_image")
    private String profile_image;

    @SerializedName("username")
    private String username;

    @SerializedName("state")
    private String state;

    @SerializedName("country")
    private String country;

    @SerializedName("contact_number")
    private String contact_number;

    @SerializedName("aboutme")
    private String aboutme;

    @SerializedName("privacyOn")
    private String privacyOn;

    @SerializedName("profile_status")
    private String profileStatus;

    @SerializedName("web_info")
    private String webInfo;

    @SerializedName("data")
    private ArrayList<Posts> postsArrayList = new ArrayList<>();

    @SerializedName("friends")
    private ArrayList<UserModel> frindsList = new ArrayList<>();

    @SerializedName("followings")
    private ArrayList<UserModel> followingList = new ArrayList<>();

    @SerializedName("followers")
    private ArrayList<UserModel> followersList = new ArrayList<>();

    @SerializedName("businessPage")
    private ArrayList<BusinessPageModel> businessPagesList = new ArrayList<>();

    @SerializedName("yourPosts")
    private ArrayList<Posts> yourPostsList = new ArrayList<>();

    public ArrayList<BusinessPageModel> getBusinessPagesList() {
        return businessPagesList;
    }

    public void setBusinessPagesList(ArrayList<BusinessPageModel> businessPagesList) {
        this.businessPagesList = businessPagesList;
    }

    public ArrayList<Posts> getYourPostsList() {
        return yourPostsList;
    }

    public void setYourPostsList(ArrayList<Posts> yourPostsList) {
        this.yourPostsList = yourPostsList;
    }

    public ArrayList<UserModel> getFrindsList() {
        return frindsList;
    }

    public void setFrindsList(ArrayList<UserModel> frindsList) {
        this.frindsList = frindsList;
    }

    public ArrayList<UserModel> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(ArrayList<UserModel> followingList) {
        this.followingList = followingList;
    }

    public ArrayList<UserModel> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(ArrayList<UserModel> followersList) {
        this.followersList = followersList;
    }

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

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getPrivacyOn() {
        return privacyOn;
    }

    public void setPrivacyOn(String privacyOn) {
        this.privacyOn = privacyOn;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getWebInfo() {
        return webInfo;
    }

    public void setWebInfo(String webInfo) {
        this.webInfo = webInfo;
    }
}
