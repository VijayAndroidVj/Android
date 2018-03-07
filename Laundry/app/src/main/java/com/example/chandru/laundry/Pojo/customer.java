package com.example.chandru.laundry.Pojo;


import com.google.gson.annotations.SerializedName;

public class customer {
    @SerializedName("error")
    private String error;
    @SerializedName("error_msg")
    private String error_msg;



    public customer(String error, String error_msg) {
        this.error = error;
        this.error_msg = error_msg;


    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
