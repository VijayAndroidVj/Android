package com.vajralabs.uniroyal.uniroyal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 6/2/18.
 */

public class CategoryItem {

    @SerializedName("itemname")
    private String itemname;

    @SerializedName("item_id")
    private String item_id;

    @SerializedName("item_image_path")
    private String item_image_path;

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_image_path() {
        return item_image_path;
    }

    public void setItem_image_path(String item_image_path) {
        this.item_image_path = item_image_path;
    }
}
