package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

public class OrderLineItemVO {
    
    private long user_id;
    private String user_status;
    private long order_id;
    private String order_status;
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
    private String tax_amount;
    private String  order_subtotal;
    private String  order_total;
    private String order_create_dt;
    
    public long getUserId() {
        return user_id;
    }
    public void setUserId(long user_id) {
        this.user_id = user_id;
    }
    public String getUserStatus() {
        return user_status;
    }
    public void setUserStatus(String user_status) {
        this.user_status = user_status;
    }
    public long getOrderId() {
        return order_id;
    }
    public void setOrderId(long order_id) {
        this.order_id = order_id;
    }
    public String getOrderStatus() {
        return order_status;
    }
    public void setOrderStatus(String orderStatus) {
        this.order_status = orderStatus;
    }
    public long getPackageId() {
        return package_id;
    }
    public void setPackageId(long package_id) {
        this.package_id = package_id;
    }
    public String getPackageName() {
        return package_name;
    }
    public void setPackageName(String package_name) {
        this.package_name = package_name;
    }
    public String getPackageDesc() {
        return package_desc;
    }
    public void setPackageDesc(String package_desc) {
        this.package_desc = package_desc;
    }
    public String  getPackagePrice() {
        return package_price;
    }
    public void setPackagePrice(String package_price) {
        this.package_price = package_price;
    }
    public short getPackageQuantity() {
        return package_quantity;
    }
    public void setPackageQuantity(short package_quantity) {
        this.package_quantity = package_quantity;
    }
    public long getOrderLineItemId() {
        return order_line_item_id;
    }
    public void setOrderLineItemId(long order_line_item_id) {
        this.order_line_item_id = order_line_item_id;
    }
    public short getItemQuantity() {
        return item_quantity;
    }
    public void setItemQuantity(short item_quantity) {
        this.item_quantity = item_quantity;
    }
    public String getItemTypeName() {
        return item_type_name;
    }
    public void setItemTypeName(String item_type_name) {
        this.item_type_name = item_type_name;
    }
    public String getItemTypeUnit() {
        return item_type_unit;
    }
    public void setItemTypeUnit(String item_type_unit) {
        this.item_type_unit = item_type_unit;
    }
    public int getLocationId() {
        return location_id;
    }
    public void setLocationId(int location_id) {
        this.location_id = location_id;
    }
    public String getCountryCode() {
        return country_code;
    }
    public void setCountryCode(String country_code) {
        this.country_code = country_code;
    }
    public String getCountryName() {
        return country_name;
    }
    public void setCountryName(String country_name) {
        this.country_name = country_name;
    }
    public String getProvinceName() {
        return province_name;
    }
    public void setProvinceName(String province_name) {
        this.province_name = province_name;
    }
    public String getProvinceCode() {
        return province_code;
    }
    public void setProvinceCode(String province_code) {
        this.province_code = province_code;
    }
    public String  getTaxPercentage() {
        return tax_percentage;
    }
    public void setTaxPercentage(String tax_percentage) {
        this.tax_percentage = tax_percentage;
    }
    public String getTaxType() {
        return tax_type;
    }
    public void setTaxType(String tax_type) {
        this.tax_type = tax_type;
    }
    public String getTaxAmount() {
        return tax_amount;
    }
    public void setTaxAmount(String tax_amount) {
        this.tax_amount = tax_amount;
    }
    public String  getOrderSubtotal() {
        return order_subtotal;
    }
    public void setOrderSubtotal(String order_subtotal) {
        this.order_subtotal = order_subtotal;
    }
    public String  getOrderTotal() {
        return order_total;
    }
    public void setOrderTotal(String order_total) {
        this.order_total = order_total;
    }
    public String getCreateDt() {
        return order_create_dt;
    }
    public void setCreateDt(String create_dt) {
        this.order_create_dt = create_dt;
    }    

}
