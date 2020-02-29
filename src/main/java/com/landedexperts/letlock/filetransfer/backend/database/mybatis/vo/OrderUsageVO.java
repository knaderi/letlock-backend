/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

public class OrderUsageVO {
    
 //   private OrdersFileTransfersCountsResponse ordersFileTransferCounts;
    private FileTransferOrderLineItemUsageVO[] result = new FileTransferOrderLineItemUsageVO[] {};
    private long orderId;

    public OrderUsageVO(long orderId, FileTransferOrderLineItemUsageVO[] result) {
        super();
        this.result = result;
        this.orderId = orderId;
    }


    public FileTransferOrderLineItemUsageVO[] getResult() {
        return result;
    }

    public void setResult(FileTransferOrderLineItemUsageVO[] result) {
        this.result = result;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

}
