package com.instag.vijay.fasttrending.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 25/11/17.
 */

public class BusinessPageModel implements Parcelable {

    @SerializedName("title")
    private String title;

    @SerializedName("email")
    private String email;

    @SerializedName("image")
    private String image;

    @SerializedName("category")
    private String category;

    @SerializedName("subcategory")
    private String subcategory;

    @SerializedName("shop_id")
    private String shop_id;

    @SerializedName("follow")
    private boolean follow;

    public BusinessPageModel() {

    }

    protected BusinessPageModel(Parcel in) {
        title = in.readString();
        email = in.readString();
        image = in.readString();
        category = in.readString();
        subcategory = in.readString();
        shop_id = in.readString();
        follow = in.readByte() != 0;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(email);
        dest.writeString(image);
        dest.writeString(category);
        dest.writeString(subcategory);
        dest.writeString(shop_id);
        dest.writeByte((byte) (follow ? 1 : 0));
    }
}
