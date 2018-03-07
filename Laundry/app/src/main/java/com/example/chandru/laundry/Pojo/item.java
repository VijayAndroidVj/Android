package com.example.chandru.laundry.Pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 2/14/2018.
 */

public class item {

    @SerializedName("id")
    private String id;
    @SerializedName("laundry_item")
    private String laundry_item;
    @SerializedName("laundry_price")
    private String laundry_price;
    @SerializedName("laundry_quantity")
    private String laundry_quantity;
    @SerializedName("laundry_id")
    private String laundry_id;
    @SerializedName("service")
    private String service;

    public item(){

    }


    public item(String id, String laundry_item,String laundry_price, String laundry_quantity, String laundry_id, String service) {
        this.id = id;
        this.laundry_item = laundry_item;
        this.laundry_price = laundry_price;
        this.laundry_quantity = laundry_quantity;
        this.laundry_id = laundry_id;
        this.service = service;



    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLaundry_item() {
        return laundry_item;
    }

    public void setLaundry_item(String laundry_item) {
        this.laundry_item = laundry_item;
    }

    public String getLaundry_price() {
        return laundry_price;
    }

    public void setLaundry_price(String laundry_price) {
        this.laundry_price = laundry_price;
    }

    public String getLaundry_quantity() {
        return laundry_quantity;
    }

    public void setLaundry_quantity(String laundry_quantity) {
        this.laundry_quantity = laundry_quantity;
    }

    public String getLaundry_id() {
        return laundry_id;
    }

    public void setLaundry_id(String laundry_id) {
        this.laundry_id = laundry_id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
