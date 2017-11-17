package com.instag.vijay.instagphoto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by iyara_rajan on 18-07-2017.
 */

public class EventResponse {

    @SerializedName("error")
    private boolean error;

    @SerializedName("error_msg")
    private String error_msg;

    @SerializedName("uid")
    private int uid;

    @SerializedName("user")
    private User user;

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "error = " + error + " , error_msg = " + error_msg;
    }
}
