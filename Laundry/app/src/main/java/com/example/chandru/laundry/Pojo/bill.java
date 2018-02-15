package com.example.chandru.laundry.Pojo;



public class bill {

    private String item;
    private String qty;
    private String unit;
    private String amt;
    private String serviceId;
    private String serviceName;
    private String itemid;

    public bill(){

    }


    public bill(String item, String qty,String unit, String amt,String serviceId, String serviceName,String itemid) {
        this.item = item;
        this.qty = qty;
        this.unit = unit;
        this.amt = amt;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.itemid=itemid;




    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }
}
