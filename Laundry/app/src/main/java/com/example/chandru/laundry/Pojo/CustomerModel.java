package com.example.chandru.laundry.Pojo;

import com.google.gson.annotations.SerializedName;


public class CustomerModel {


    @SerializedName("id")
    private String id;

    @SerializedName("customer_name")
    private String customer_name;
    @SerializedName("customer_address")
    private String customer_address;
    @SerializedName("customer_contact1")
    private String customer_contact1;

    public CustomerModel() {

    }

    public String getCustomer_contact1() {
        return customer_contact1;
    }

    public void setCustomer_contact1(String customer_contact1) {
        this.customer_contact1 = customer_contact1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }
}


