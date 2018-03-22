package com.instag.vijay.fasttrending.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 19/3/18.
 */

public class CategoryMain implements Parcelable {

    @SerializedName("_id")
    private String _id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    protected CategoryMain(Parcel in) {
        _id = in.readString();
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<CategoryMain> CREATOR = new Creator<CategoryMain>() {
        @Override
        public CategoryMain createFromParcel(Parcel in) {
            return new CategoryMain(in);
        }

        @Override
        public CategoryMain[] newArray(int size) {
            return new CategoryMain[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        dest.writeString(name);
        dest.writeString(image);
    }
}
