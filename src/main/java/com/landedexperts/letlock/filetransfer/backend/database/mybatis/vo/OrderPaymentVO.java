/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import org.apache.commons.lang3.StringUtils;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class OrderPaymentVO  extends ReturnCodeMessageResponse {
    
    String orderId;
    String orderStatus;
    String taxAmount;
    String orderSubtotal;
    String orderTotal;
    String orderCreateDt;
    String paymentId;
    String paymentStatus;
    String paymentTransactionId;
    String orderDescription = "Letlock Order";
    
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getTaxAmount() {
        return taxAmount;
    }
    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }
    public String getOrderSubtotal() {
        return orderSubtotal;
    }
    public void setOrderSubtotal(String orderSubtotal) {
        this.orderSubtotal = orderSubtotal;
    }
    public String getOrderTotal() {
        return orderTotal;
    }
    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }
    public String getOrderCreateDt() {
        return orderCreateDt;
    }
    public void setOrderCreateDt(String orderCreateDt) {
        this.orderCreateDt = orderCreateDt;
    }
    public String getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }
    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }
    public String getOrderDescription() {
        return orderDescription;
    }
    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }
    
    public boolean isValid() {
        if(StringUtils.isBlank(orderId) || StringUtils.isBlank(orderTotal)|| StringUtils.isBlank(orderSubtotal) ||StringUtils.isBlank(orderTotal)|| StringUtils.isBlank(paymentId) )
            return false;
        return true;
            
    }

}
