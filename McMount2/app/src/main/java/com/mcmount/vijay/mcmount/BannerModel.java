package com.mcmount.vijay.mcmount;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 23/9/17.
 */

public class BannerModel {

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
