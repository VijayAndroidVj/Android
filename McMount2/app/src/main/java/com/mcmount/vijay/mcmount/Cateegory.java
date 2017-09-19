package com.mcmount.vijay.mcmount;

import com.google.gson.annotations.SerializedName;

/**
 * Created by iyara_rajan on 18-07-2017.
 */

public class Cateegory {


    @SerializedName("id")
    private String id;

    @SerializedName("randuniq")
    private String randuniq;

    @SerializedName("name")
    private String name;

    @SerializedName("icon_image")
    private String icon_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRanduniq() {
        return randuniq;
    }

    public void setRanduniq(String randuniq) {
        this.randuniq = randuniq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon_image() {
        return icon_image;
    }

    public void setIcon_image(String icon_image) {
        this.icon_image = icon_image;
    }

    @Override
    public String toString() {
        return "id = " + id + " , name = " + name;
    }
}
