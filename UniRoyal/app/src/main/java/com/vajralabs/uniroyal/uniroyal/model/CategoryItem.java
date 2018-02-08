package com.vajralabs.uniroyal.uniroyal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 6/2/18.
 */

public class CategoryItem {

    @SerializedName("parent_id")
    private String parent_id;

    @SerializedName("item_name")
    private String item_name;

    @SerializedName("image_path")
    private String image_path;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
