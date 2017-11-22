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

    @SerializedName("userName")
    private String userName;


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

    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }
}
