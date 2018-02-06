package com.vajralabs.uniroyal.uniroyal.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by vijay on 26/11/17.
 */

public class CategoryModel {


    @SerializedName("categoryname")
    private String categoryname;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("categoryItems")
    private ArrayList<CategoryItem> categoryItems = new ArrayList<>();

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public ArrayList<CategoryItem> getCategoryItems() {
        return categoryItems;
    }

    public void setCategoryItems(ArrayList<CategoryItem> categoryItems) {
        this.categoryItems = categoryItems;
    }
}
