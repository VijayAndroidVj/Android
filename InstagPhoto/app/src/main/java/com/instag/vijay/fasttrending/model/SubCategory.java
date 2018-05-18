package com.instag.vijay.fasttrending.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 19/3/18.
 */

public class SubCategory implements Parcelable {

    @SerializedName("subid")
    private String subid;

    @SerializedName("scategory")
    private String scategory;

    @SerializedName("image")
    private String image;

    @SerializedName("category")
    private String category;

    @SerializedName("description")
    private String description;

    @SerializedName("branch_id")
    private String branch_id;


    protected SubCategory(Parcel in) {
        subid = in.readString();
        scategory = in.readString();
        image = in.readString();
        category = in.readString();
        description = in.readString();
        branch_id = in.readString();
    }

    public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {
        @Override
        public SubCategory createFromParcel(Parcel in) {
            return new SubCategory(in);
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[size];
        }
    };

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

    public String getScategory() {
        return scategory;
    }

    public void setScategory(String scategory) {
        this.scategory = scategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subid);
        dest.writeString(scategory);
        dest.writeString(image);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeString(branch_id);
    }
}
