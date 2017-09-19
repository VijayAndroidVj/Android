package com.mcmount.vijay.mcmount;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by iyara_rajan on 18-07-2017.
 */

public class CategoryListModel {


    @SerializedName("Category_name")
    private ArrayList<Cateegory> Category_name;

    public ArrayList<Cateegory> getCategory_name() {
        return Category_name;
    }

    public void setCategory_name(ArrayList<Cateegory> category_name) {
        Category_name = category_name;
    }
}
