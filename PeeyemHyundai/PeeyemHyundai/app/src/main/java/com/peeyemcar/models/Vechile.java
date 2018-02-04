package com.peeyemcar.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 4/2/18.
 */

public class Vechile {


    @SerializedName("aid")
    String aid;
    @SerializedName("message")
    String message;

    @SerializedName("result")
    String result;

    @SerializedName("date")
    String date;

    @SerializedName("address")
    String address;

    @SerializedName("pincode")
    String pincode;

    @SerializedName("financer")
    String financer;

    @SerializedName("model")
    String model;

    @SerializedName("color")
    String color;

    @SerializedName("fram_no")
    String fram_no;

    @SerializedName("engine_no")
    String engine_no;

    @SerializedName("gross_amt")
    String gross_amt;

    @SerializedName("others")
    String others;

    @SerializedName("net_amt")
    String net_amt;

    @SerializedName("teflon")
    String teflon;

    @SerializedName("extended_warrenty")
    String extended_warrenty;

    @SerializedName("mobile")
    String mobile;

    @SerializedName("email")
    String email;

    @SerializedName("refer_code")
    String refer_code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getFinancer() {
        return financer;
    }

    public void setFinancer(String financer) {
        this.financer = financer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFram_no() {
        return fram_no;
    }

    public void setFram_no(String fram_no) {
        this.fram_no = fram_no;
    }

    public String getEngine_no() {
        return engine_no;
    }

    public void setEngine_no(String engine_no) {
        this.engine_no = engine_no;
    }

    public String getGross_amt() {
        return gross_amt;
    }

    public void setGross_amt(String gross_amt) {
        this.gross_amt = gross_amt;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getNet_amt() {
        return net_amt;
    }

    public void setNet_amt(String net_amt) {
        this.net_amt = net_amt;
    }

    public String getTeflon() {
        return teflon;
    }

    public void setTeflon(String teflon) {
        this.teflon = teflon;
    }

    public String getExtended_warrenty() {
        return extended_warrenty;
    }

    public void setExtended_warrenty(String extended_warrenty) {
        this.extended_warrenty = extended_warrenty;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRefer_code() {
        return refer_code;
    }

    public void setRefer_code(String refer_code) {
        this.refer_code = refer_code;
    }
}
