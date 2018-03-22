package com.instag.vijay.fasttrending.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 19/3/18.
 */

public class CategoryItem implements Parcelable {

    @SerializedName("_id")
    private String _id;

    @SerializedName("shopname")
    private String shopname;

    @SerializedName("address")
    private String address;

    @SerializedName("subcat")
    private String subcat;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("ratings")
    private int ratings;

    @SerializedName("image")
    private String image;

    protected CategoryItem(Parcel in) {
        _id = in.readString();
        shopname = in.readString();
        address = in.readString();
        subcat = in.readString();
        city = in.readString();
        state = in.readString();
        ratings = in.readInt();
        image = in.readString();
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubcat() {
        return subcat;
    }

    public void setSubcat(String subcat) {
        this.subcat = subcat;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
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
        dest.writeString(_id);
        dest.writeString(shopname);
        dest.writeString(address);
        dest.writeString(subcat);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeInt(ratings);
        dest.writeString(image);
    }
}