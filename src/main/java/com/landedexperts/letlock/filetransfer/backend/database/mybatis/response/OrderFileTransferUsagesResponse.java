/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferOrderLineItemUsageVO;

public class OrderFileTransferUsagesResponse extends ReturnCodeMessageResponse {
    
    private long orderId;
    private int availableTransferCounts = 0;
    private int originalTransferCounts = 0;
    public int getAvailableTransferCounts() {
        return availableTransferCounts;
    }


    public void setAvailableTransferCounts(int availableTransferCounts) {
        this.availableTransferCounts = availableTransferCounts;
    }


    public int getOriginalTransferCounts() {
        return originalTransferCounts;
    }


    public void setOriginalTransferCounts(int originalTransferCounts) {
        this.originalTransferCounts = originalTransferCounts;
    }

    private FileTransferOrderLineItemUsageVO[] result = new FileTransferOrderLineItemUsageVO[] {};
        
    
    public OrderFileTransferUsagesResponse(long orderId, FileTransferOrderLineItemUsageVO[] result, OrdersFileTransfersCountsVO allOrdersFileTransferCounts) {
        super("SUCCESS", "");
        if(null == result || result.length == 0) {
            setReturnCode("SUCCESS");
            setReturnMessage("");
        }
        availableTransferCounts = allOrdersFileTransferCounts.getAvailableTransferCounts();
        originalTransferCounts =allOrdersFileTransferCounts.getOriginalTransferCounts();
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
