package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

public class ProductVO {

    private int productId;
    private String productName;
    private String productDesc;
    private String productTypeName;
    private String productTypeUnit;
   

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductTypeUnit() {
        return productTypeUnit;
    }

    public void setProductTypeUnit(String productTypeUnit) {
        this.productTypeUnit = productTypeUnit;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return productName;
    }

    public void setName(String name) {
        this.productName = name;
    }

    public String getDescription() {
        return productDesc;
    }

    public void setDescription(String description) {
        this.productDesc = description;
    }
    
    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

}
