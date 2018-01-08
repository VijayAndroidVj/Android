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

    @Override
    public String toString() {
        return "result = " + result + " , message = " + message;
    }
}
