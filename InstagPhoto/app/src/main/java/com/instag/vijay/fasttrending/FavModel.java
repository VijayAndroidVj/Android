package com.instag.vijay.fasttrending;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 22/11/17.
 */

public class FavModel {

    @SerializedName("who")
    private String who;

    @SerializedName("whom")
    private String whom;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("userName")
    private String userName;

    @SerializedName("profile_image")
    private String profile_image;

    @SerializedName("following")
    private boolean following;

    @SerializedName("type")
    private String type;

    @SerializedName("shop_id")
    private String shop_id;

    @SerializedName("category")
    private String category;

    @SerializedName("subcategory")
    private String subcategory;


    @SerializedName("group_type")

    private String group_type;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getGroup_type() {
        return group_type;
    }

    public void setGroup_type(String group_type) {
        this.group_type = group_type;
    }

    public String getType() {
        return type;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getWhom() {
        return whom;
    }

    public void setWhom(String whom) {
        this.whom = whom;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
