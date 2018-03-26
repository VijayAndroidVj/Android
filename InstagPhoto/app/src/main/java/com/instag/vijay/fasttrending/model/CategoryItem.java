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

    @SerializedName("mail")
    private String mail;

    @SerializedName("business_name")
    private String business_name;

    @SerializedName("business_desc")
    private String business_desc;


    @SerializedName("phone")
    private String phone;

    @SerializedName("business_location")
    private String business_location;

    @SerializedName("state")
    private String state;

    @SerializedName("country")
    private String country;

    @SerializedName("image")
    private String image;


    protected CategoryItem(Parcel in) {
        _id = in.readString();
        mail = in.readString();
        business_name = in.readString();
        business_desc = in.readString();
        phone = in.readString();
        business_location = in.readString();
        state = in.readString();
        country = in.readString();
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusiness_location() {
        return business_location;
    }

    public void setBusiness_location(String business_location) {
        this.business_location = business_location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBusiness_desc() {
        return business_desc;
    }

    public void setBusiness_desc(String business_desc) {
        this.business_desc = business_desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(mail);
        dest.writeString(business_name);
        dest.writeString(business_desc);
        dest.writeString(phone);
        dest.writeString(business_location);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(image);
    }
}