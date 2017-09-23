package com.mcmount.vijay.mcmount;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by iyara_rajan on 18-07-2017.
 */

public class CategoryListModel {


    @SerializedName("Category_name")
    private ArrayList<Cateegory> Category_name;

    @SerializedName("Product_name")
    private ArrayList<Cateegory> Product_name;

    @SerializedName("Brand_name")
    private ArrayList<Cateegory> Brand_name;

    @SerializedName("Model_name")
    private ArrayList<Cateegory> Model_name;


    public ArrayList<Cateegory> getCategory_name() {
        return Category_name;
    }

    public void setCategory_name(ArrayList<Cateegory> category_name) {
        Category_name = category_name;
    }

    public ArrayList<Cateegory> getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(ArrayList<Cateegory> product_name) {
        Product_name = product_name;
    }

    public ArrayList<Cateegory> getBrand_name() {
        return Brand_name;
    }

    public void setBrand_name(ArrayList<Cateegory> brand_name) {
        Brand_name = brand_name;
    }

    public ArrayList<Cateegory> getModel_name() {
        return Model_name;
    }

    public void setModel_name(ArrayList<Cateegory> model_name) {
        Model_name = model_name;
    }
}
