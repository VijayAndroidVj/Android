package com.example.chandru.laundry.Pojo;

import com.google.gson.annotations.SerializedName;


public class LocationModel {


    @SerializedName("id")
    private String id;
    @SerializedName("location_name")
    private String location_name;
    @SerializedName("location_description")
    private String location_description;

    public LocationModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLocation_description() {
        return location_description;
    }

    public void setLocation_description(String location_description) {
        this.location_description = location_description;
    }
}


