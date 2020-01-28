package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

public class OrderInfoRecordVO {
    
    private long userId;
    private long order_id;
    private long order_line_item_id;
    private String order_status;
    private int product_id;
    private String product_name;
    private int order_quantity;
    private int order_price;
    
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public long getOrder_id() {
        return order_id;
    }
    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
    public long getOrder_line_item_id() {
        return order_line_item_id;
    }
    public void setOrder_detail_id(int order_line_item_id) {
        this.order_line_item_id = order_line_item_id;
    }
    public String getOrder_status() {
        return order_status;
    }
    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
    public int getProduct_id() {
        return product_id;
    }
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
    public String getProductName() {
        return product_name;
    }
    public void setProductName(String productName) {
        this.product_name = productName;
    }
    public int getOrder_quantity() {
        return order_quantity;
    }
    public void setOrder_quantity(int order_quantity) {
        this.order_quantity = order_quantity;
    }
    public int getOrder_price() {
        return order_price;
    }
    public void setOrder_price(int order_price) {
        this.order_price = order_price;
    }

}
