package com.instag.vijay.fasttrending.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.instag.vijay.fasttrending.UserModel;

import java.util.ArrayList;

/**
 * Created by vijay on 25/11/17.
 */

public class BusinessPageModel implements Parcelable{

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    @SerializedName("category")
    private String category;

    @SerializedName("subcategory")
    private String subcategory;

    protected BusinessPageModel(Parcel in) {
        title = in.readString();
        image = in.readString();
        category = in.readString();
        subcategory = in.readString();
    }

    public static final Creator<BusinessPageModel> CREATOR = new Creator<BusinessPageModel>() {
        @Override
        public BusinessPageModel createFromParcel(Parcel in) {
            return new BusinessPageModel(in);
        }

        @Override
        public BusinessPageModel[] newArray(int size) {
            return new BusinessPageModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(category);
        dest.writeString(subcategory);
    }
}
