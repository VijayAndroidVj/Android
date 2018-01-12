package com.peeyemcar.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by iyara_rajan on 18-07-2017.
 */

public class EventResponse {

    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("name")
    private String name;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("items")
    private ArrayList<CarModel> items = new ArrayList<>();

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

    public ArrayList<CarModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<CarModel> items) {
        this.items = items;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "result = " + result + " , message = " + message;
    }
}
