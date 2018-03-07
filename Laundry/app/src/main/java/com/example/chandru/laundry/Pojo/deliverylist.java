package com.example.chandru.laundry.Pojo;
import com.google.gson.annotations.SerializedName;


public class deliverylist {

    @SerializedName("id")
    private String id;
    @SerializedName("order_id")
    private String order_id;
    @SerializedName("store_ref")
    private String store_ref;
    @SerializedName("order_date")
    private String order_date;
    @SerializedName("customer_name")
    private String customer_name;
    @SerializedName("customer_phone")
    private String customer_phone;
    @SerializedName("customer_address")
    private String customer_address;
    @SerializedName("delivery_status")
    private String delivery_status;
    @SerializedName("location")
    private String location;
    @SerializedName("laundry_for")
    private String laundry_for;
    @SerializedName("delivery_date")
    private String delivery_date;
    @SerializedName("total_laundry")
    private String total_laundry;
    @SerializedName("total_amount")
    private String total_amount;
    @SerializedName("advance_amount")
    private String advance_amount;
    @SerializedName("balance_amount")
    private String balance_amount;
    @SerializedName("paid")
    private String paid;
    @SerializedName("delivered_on")
    private String delivered_on;
    @SerializedName("laundry_")
    private String laundry_;
    @SerializedName("check_")
    private String check_;
    @SerializedName("summary")
    private String summary;

    public deliverylist(){

    }


    public deliverylist(String id, String order_id,String store_ref, String order_date, String customer_name
            ,String customer_phone, String customer_address,String delivery_status, String location, String laundry_for,
                               String delivery_date, String total_laundry,String total_amount, String advance_amount, String balance_amount,
                               String paid, String delivered_on,String laundry_, String check_, String summary) {
        this.id = id;
        this.order_id = order_id;
        this.store_ref = store_ref;
        this.order_date = order_date;
        this.customer_name = customer_name;

        this.customer_phone = customer_phone;
        this.customer_address = customer_address;
        this.delivery_status = delivery_status;
        this.location = location;
        this.laundry_for = laundry_for;

        this.delivery_date = delivery_date;
        this.total_laundry = total_laundry;
        this.total_amount = total_amount;
        this.advance_amount = advance_amount;
        this.balance_amount = balance_amount;

        this.paid = paid;
        this.delivered_on = order_id;
        this.laundry_ = store_ref;
        this.check_ = check_;
        this.summary = summary;



    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStore_ref() {
        return store_ref;
    }

    public void setStore_ref(String store_ref) {
        this.store_ref = store_ref;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLaundry_for() {
        return laundry_for;
    }

    public void setLaundry_for(String laundry_for) {
        this.laundry_for = laundry_for;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getTotal_laundry() {
        return total_laundry;
    }

    public void setTotal_laundry(String total_laundry) {
        this.total_laundry = total_laundry;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getAdvance_amount() {
        return advance_amount;
    }

    public void setAdvance_amount(String advance_amount) {
        this.advance_amount = advance_amount;
    }

    public String getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(String balance_amount) {
        this.balance_amount = balance_amount;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getDelivered_on() {
        return delivered_on;
    }

    public void setDelivered_on(String delivered_on) {
        this.delivered_on = delivered_on;
    }

    public String getLaundry_() {
        return laundry_;
    }

    public void setLaundry_(String laundry_) {
        this.laundry_ = laundry_;
    }

    public String getCheck_() {
        return check_;
    }

    public void setCheck_(String check_) {
        this.check_ = check_;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}




