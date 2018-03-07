package com.vajralabs.uniroyal.uniroyal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 6/2/18.
 */

public class CategoryItem implements Parcelable {

    @SerializedName("parent_id")
    private String parent_id;

    @SerializedName("item_name")
    private String item_name;

    @SerializedName("category_name")
    private String category_name;

    @SerializedName("image_path")
    private String image_path;

    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    protected CategoryItem(Parcel in) {
        parent_id = in.readString();
        item_name = in.readString();
        category_name = in.readString();
        image_path = in.readString();
    }

    public static final Creator<CategoryItem> CREATOR = new Creator<CategoryItem>() {
        @Override
        public CategoryItem createFromParcel(Parcel in) {
            return new CategoryItem(in);
        }

        @Override
        public CategoryItem[] newArray(int size) {
            return new CategoryItem[size];
        }
    };

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(parent_id);
        parcel.writeString(item_name);
        parcel.writeString(category_name);
        parcel.writeString(image_path);
    }
}
