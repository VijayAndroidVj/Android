package com.xr.vijay.tranzpost.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by iyara_rajan on 18-07-2017.
 */

public class EventResponse {

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    @SerializedName("user_type")
    private String user_type;

    @SerializedName("serverimage")
    private String serverimage;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getServerimage() {
        return serverimage;
    }

    public void setServerimage(String serverimage) {
        this.serverimage = serverimage;
    }

    @Override
    public String toString() {
        return "result = " + result + " , message = " + message;
    }
}
