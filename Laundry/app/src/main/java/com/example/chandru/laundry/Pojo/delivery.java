package com.example.chandru.laundry.Pojo;



public class delivery {

    private String id;
    private String bill_id;
    private String service_name;
    private String service_id;
    private String item_name;
    private String item_id;
    private String price;
    private String quantity;
    private String total;
    private String date;
    private String count;


    public delivery(){

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public delivery(String id, String bill_id, String service_name, String service_id,
                    String item_name, String item_id, String price, String quantity,
                    String total, String date, String count) {
        this.id = id;
        this.bill_id = bill_id;
        this.service_name = service_name;
        this.service_id = service_id;
        this.item_name = item_name;
        this.item_id = item_id;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.date = date;
        this.count = count;






    }
}
