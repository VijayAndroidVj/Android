package com.xr.vijay.tranzpost.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 5/12/17.
 */

public class DocumentModel {

    @SerializedName("error1")
    private String error1;

    @SerializedName("error2")
    private String error2;

    @SerializedName("error3")
    private String error3;

    @SerializedName("error4")
    private String error4;

    @SerializedName("pan_card_path")
    private String pan_card_path;

    @SerializedName("address_proof_path")
    private String address_proof_path;

    @SerializedName("rcbook_front_path")
    private String rcbook_front_path;


    @SerializedName("rcbook_back_path")
    private String rcbook_back_path;

    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    public String getError1() {
        return error1;
    }

    public void setError1(String error1) {
        this.error1 = error1;
    }

    public String getError2() {
        return error2;
    }

    public void setError2(String error2) {
        this.error2 = error2;
    }

    public String getError3() {
        return error3;
    }

    public void setError3(String error3) {
        this.error3 = error3;
    }

    public String getError4() {
        return error4;
    }

    public void setError4(String error4) {
        this.error4 = error4;
    }

    public String getPan_card_path() {
        return pan_card_path;
    }

    public void setPan_card_path(String pan_card_path) {
        this.pan_card_path = pan_card_path;
    }

    public String getAddress_proof_path() {
        return address_proof_path;
    }

    public void setAddress_proof_path(String address_proof_path) {
        this.address_proof_path = address_proof_path;
    }

    public String getRcbook_front_path() {
        return rcbook_front_path;
    }

    public void setRcbook_front_path(String rcbook_front_path) {
        this.rcbook_front_path = rcbook_front_path;
    }

    public String getRcbook_back_path() {
        return rcbook_back_path;
    }

    public void setRcbook_back_path(String rcbook_back_path) {
        this.rcbook_back_path = rcbook_back_path;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
