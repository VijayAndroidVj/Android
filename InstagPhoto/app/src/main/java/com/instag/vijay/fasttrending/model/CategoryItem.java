package com.instag.vijay.fasttrending.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 19/3/18.
 */

public class CategoryItem implements Parcelable {

    @SerializedName("lid")
    private String _id;

    @SerializedName("title")
    private String title;

    @SerializedName("address")
    private String address;

    @SerializedName("image")
    private String image;

    @SerializedName("state")
    private String state;

    @SerializedName("city")
    private String city;

    @SerializedName("e_contact")
    private String e_contact;

    @SerializedName("category")
    private String category;

    @SerializedName("subcategory")
    private String subcategory;

    @SerializedName("e_status")
    private String e_status;

    @SerializedName("keyword")
    private String keyword;

    @SerializedName("s_date")
    private String s_date;

    @SerializedName("e_date")
    private String e_date;

    @SerializedName("regno")
    private String regno;

    @SerializedName("des")
    private String des;

    @SerializedName("e_phone")
    private String e_phone;

    @SerializedName("email")
    private String email;

    @SerializedName("membership")
    private String membership;

    @SerializedName("image1")
    private String image1;

    @SerializedName("image2")
    private String image2;

    @SerializedName("image3")
    private String image3;

    @SerializedName("shop_id")
    private String shop_id;

    @SerializedName("established")
    private String established;

    @SerializedName("website")
    private String website;

    @SerializedName("created_on")
    private String created_on;

    @SerializedName("updated_on")
    private String updated_on;

    @SerializedName("verified")
    private String verified;

    protected CategoryItem(Parcel in) {
        _id = in.readString();
        title = in.readString();
        address = in.readString();
        image = in.readString();
        state = in.readString();
        city = in.readString();
        e_contact = in.readString();
        category = in.readString();
        subcategory = in.readString();
        e_status = in.readString();
        keyword = in.readString();
        s_date = in.readString();
        e_date = in.readString();
        regno = in.readString();
        des = in.readString();
        e_phone = in.readString();
        email = in.readString();
        membership = in.readString();
        image1 = in.readString();
        image2 = in.readString();
        image3 = in.readString();
        shop_id = in.readString();
        established = in.readString();
        website = in.readString();
        created_on = in.readString();
        updated_on = in.readString();
        verified = in.readString();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getE_contact() {
        return e_contact;
    }

    public void setE_contact(String e_contact) {
        this.e_contact = e_contact;
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

    public String getE_status() {
        return e_status;
    }

    public void setE_status(String e_status) {
        this.e_status = e_status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getS_date() {
        return s_date;
    }

    public void setS_date(String s_date) {
        this.s_date = s_date;
    }

    public String getE_date() {
        return e_date;
    }

    public void setE_date(String e_date) {
        this.e_date = e_date;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getE_phone() {
        return e_phone;
    }

    public void setE_phone(String e_phone) {
        this.e_phone = e_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getEstablished() {
        return established;
    }

    public void setEstablished(String established) {
        this.established = established;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(title);
        dest.writeString(address);
        dest.writeString(image);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(e_contact);
        dest.writeString(category);
        dest.writeString(subcategory);
        dest.writeString(e_status);
        dest.writeString(keyword);
        dest.writeString(s_date);
        dest.writeString(e_date);
        dest.writeString(regno);
        dest.writeString(des);
        dest.writeString(e_phone);
        dest.writeString(email);
        dest.writeString(membership);
        dest.writeString(image1);
        dest.writeString(image2);
        dest.writeString(image3);
        dest.writeString(shop_id);
        dest.writeString(established);
        dest.writeString(website);
        dest.writeString(created_on);
        dest.writeString(updated_on);
        dest.writeString(verified);
    }
}