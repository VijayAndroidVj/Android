package com.peeyemcar.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 8/1/18.
 */

public class CarModel implements Parcelable {

    @SerializedName("modelname")
    String modelname;

    @SerializedName("kmsRunned")
    String kmsRunned;

    @SerializedName("year")
    String year;

    @SerializedName("price")
    String price;

    @SerializedName("carImagePath")
    String carImagePath;

    protected CarModel(Parcel in) {
        modelname = in.readString();
        kmsRunned = in.readString();
        year = in.readString();
        price = in.readString();
        carImagePath = in.readString();
    }

    public static final Creator<CarModel> CREATOR = new Creator<CarModel>() {
        @Override
        public CarModel createFromParcel(Parcel in) {
            return new CarModel(in);
        }

        @Override
        public CarModel[] newArray(int size) {
            return new CarModel[size];
        }
    };

    public String getCarImagePath() {
        return carImagePath;
    }

    public void setCarImagePath(String carImagePath) {
        this.carImagePath = carImagePath;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getKmsRunned() {
        return kmsRunned;
    }

    public void setKmsRunned(String kmsRunned) {
        this.kmsRunned = kmsRunned;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(modelname);
        parcel.writeString(kmsRunned);
        parcel.writeString(year);
        parcel.writeString(price);
        parcel.writeString(carImagePath);
    }
}
