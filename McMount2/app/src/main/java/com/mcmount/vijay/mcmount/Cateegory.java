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

    @SerializedName("parent_id")
    private String parent_id;

    @SerializedName("brand_id")
    private String brand_id;

    @SerializedName("model_id")
    private String model_id;

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

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    @Override
    public String toString() {
        return "id = " + id + " , name = " + name;
    }
}
