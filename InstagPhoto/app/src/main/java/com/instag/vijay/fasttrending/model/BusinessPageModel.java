package com.instag.vijay.fasttrending.model;

import com.google.gson.annotations.SerializedName;
import com.instag.vijay.fasttrending.UserModel;

import java.util.ArrayList;

/**
 * Created by vijay on 25/11/17.
 */

public class BusinessPageModel {

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    @SerializedName("category")
    private String category;

    @SerializedName("subcategory")
    private String subcategory;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
}
