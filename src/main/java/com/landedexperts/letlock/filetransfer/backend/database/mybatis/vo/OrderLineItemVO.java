package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import java.math.BigDecimal;
import java.util.Date;

import software.amazon.ion.Decimal;

public class OrderLineItemVO {
    
    private long user_id;
    private String user_status;
    private long order_id;
    private String orderStatus;
    private long package_id;
    private String package_name;
    private String package_desc;
    private String package_price;
    private short package_quantity;
    private long order_line_item_id;
    private short item_quantity;
    private String item_type_name;
    private String item_type_unit;
    private int location_id;
    private String country_code;
    private String country_name;
    private String province_name;
    private String province_code;
    private String  tax_percentage;
    private String tax_type;
    private BigDecimal  order_subtotal;
    private BigDecimal  order_total;
    private Date create_dt;
    
    public long getUser_id() {
        return user_id;
    }
    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
    public String getUser_status() {
        return user_status;
    }
    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }
    public long getOrder_id() {
        return order_id;
    }
    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public long getPackage_id() {
        return package_id;
    }
    public void setPackage_id(long package_id) {
        this.package_id = package_id;
    }
    public String getPackage_name() {
        return package_name;
    }
    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }
    public String getPackage_desc() {
        return package_desc;
    }
    public void setPackage_desc(String package_desc) {
        this.package_desc = package_desc;
    }
    public String  getPackage_price() {
        return package_price;
    }
    public void setPackage_price(String package_price) {
        this.package_price = package_price;
    }
    public short getPackage_quantity() {
        return package_quantity;
    }
    public void setPackage_quantity(short package_quantity) {
        this.package_quantity = package_quantity;
    }
    public long getOrder_line_item_id() {
        return order_line_item_id;
    }
    public void setOrder_line_item_id(long order_line_item_id) {
        this.order_line_item_id = order_line_item_id;
    }
    public short getItem_quantity() {
        return item_quantity;
    }
    public void setItem_quantity(short item_quantity) {
        this.item_quantity = item_quantity;
    }
    public String getItem_type_name() {
        return item_type_name;
    }
    public void setItem_type_name(String item_type_name) {
        this.item_type_name = item_type_name;
    }
    public String getItem_type_unit() {
        return item_type_unit;
    }
    public void setItem_type_unit(String item_type_unit) {
        this.item_type_unit = item_type_unit;
    }
    public int getLocation_id() {
        return location_id;
    }
    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }
    public String getCountry_code() {
        return country_code;
    }
    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
    public String getCountry_name() {
        return country_name;
    }
    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
    public String getProvince_name() {
        return province_name;
    }
    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }
    public String getProvince_code() {
        return province_code;
    }
    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }
    public String  getTax_percentage() {
        return tax_percentage;
    }
    public void setTax_percentage(String tax_percentage) {
        this.tax_percentage = tax_percentage;
    }
    public String getTax_type() {
        return tax_type;
    }
    public void setTax_type(String tax_type) {
        this.tax_type = tax_type;
    }
    public BigDecimal  getOrder_subtotal() {
        return order_subtotal;
    }
    public void setOrder_subtotal(Decimal order_subtotal) {
        this.order_subtotal = order_subtotal;
    }
    public BigDecimal  getOrder_total() {
        return order_total;
    }
    public void setOrder_total(Decimal order_total) {
        this.order_total = order_total;
    }
    public Date getCreate_dt() {
        return create_dt;
    }
    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }    

}
