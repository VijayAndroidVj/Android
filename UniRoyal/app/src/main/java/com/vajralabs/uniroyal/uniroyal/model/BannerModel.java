package com.vajralabs.uniroyal.uniroyal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 26/11/17.
 */

public class BannerModel {


    @SerializedName("banner_description")
    private String banner_description;

    @SerializedName("banner_image")
    private String banner_image;

    public String getBanner_description() {
        return banner_description;
    }

    public void setBanner_description(String banner_description) {
        this.banner_description = banner_description;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }
}
