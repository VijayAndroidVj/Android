package com.vajralabs.uniroyal.uniroyal.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by vijay on 26/11/17.
 */

public class CategoryModel {


    @SerializedName("parent_id")
    private String parent_id;

    @SerializedName("category_name")
    private String category_name;

    @SerializedName("category_item_lists")
    private ArrayList<CategoryItem> category_item_lists = new ArrayList<>();

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public ArrayList<CategoryItem> getCategory_item_lists() {
        return category_item_lists;
    }

    public void setCategory_item_lists(ArrayList<CategoryItem> category_item_lists) {
        this.category_item_lists = category_item_lists;
    }
}
