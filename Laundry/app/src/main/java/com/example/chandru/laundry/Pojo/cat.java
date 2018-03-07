package com.example.chandru.laundry.Pojo;

import com.google.gson.annotations.SerializedName;


public class cat {


    @SerializedName("id")
    private String id;
    @SerializedName("category_name")
    private String category_name;
    @SerializedName("category_description")
    private String category_description;
    @SerializedName("randuniq")
    private String randuniq;
    @SerializedName("icon_image")
    private String icon_image;

    public cat() {

    }


    public cat(String id, String category_name, String category_description, String randuniq, String icon_image) {
        this.id = id;
        this.category_name = category_name;
        this.category_description = category_description;
        this.randuniq = randuniq;
        this.icon_image = icon_image;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_description() {
        return category_description;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }

    public String getRanduniq() {
        return randuniq;
    }

    public void setRanduniq(String randuniq) {
        this.randuniq = randuniq;
    }

    public String getIcon_image() {
        return icon_image;
    }

    public void setIcon_image(String icon_image) {
        this.icon_image = icon_image;
    }
}


