package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import java.util.ArrayList;
import java.util.List;

public class PackageVO {

    private int packageId;
    private String packageName;
    private String packageDesc;
    private int packagePrice;
    private List<ProductVO> packageProducts= new ArrayList<ProductVO>();

    public List<ProductVO> getPackageProducts() {
        return packageProducts;
    }

    public void setPackageProducts(List<ProductVO> packageProducts) {
        this.packageProducts = packageProducts;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageDesc() {
        return packageDesc;
    }

    public void setPackageDesc(String packageDesc) {
        this.packageDesc = packageDesc;
    }

    public int getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(int packagePrice) {
        this.packagePrice = packagePrice;
    }

}
