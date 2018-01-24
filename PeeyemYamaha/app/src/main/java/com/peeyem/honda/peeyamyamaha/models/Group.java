package com.peeyem.honda.peeyamyamaha.models;

import java.util.ArrayList;

/**
 * Created by vijay on 9/11/17.
 */

public class Group {

    private String Name;
    private ArrayList<CarModel> labreports;
    private int count;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<CarModel> getLabreports() {
        return labreports;
    }

    public void setLabreports(ArrayList<CarModel> Items) {
        this.labreports = Items;
    }

}
