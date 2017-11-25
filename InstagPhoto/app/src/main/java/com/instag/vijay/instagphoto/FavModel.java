package com.instag.vijay.instagphoto;

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

    @SerializedName("following")
    private boolean following;

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
