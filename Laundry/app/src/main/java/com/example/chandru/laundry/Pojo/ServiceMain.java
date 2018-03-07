package com.example.chandru.laundry.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by vijay on 22/2/18.
 */

public class ServiceMain {

    @SerializedName("location")
    private ArrayList<LocationModel> location;

    @SerializedName("customer")
    private ArrayList<CustomerModel> customer;

    @SerializedName("item")
    private ArrayList<cat> item;

    public ArrayList<LocationModel> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<LocationModel> location) {
        this.location = location;
    }

    public ArrayList<CustomerModel> getCustomer() {
        return customer;
    }

    public void setCustomer(ArrayList<CustomerModel> customer) {
        this.customer = customer;
    }

    public ArrayList<cat> getItem() {
        return item;
    }

    public void setItem(ArrayList<cat> item) {
        this.item = item;
    }
}
