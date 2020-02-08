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
        
    
    public OrderFileTransferUsagesResponse(long orderId, FileTransferOrderLineItemUsageVO[] result, OrdersFileTransfersCountsResponse allOrdersFileTransferCounts) {
        super("SUCCESS", "");
        if(null == result || result.length == 0) {
            setReturnCode("ORDER_NOT_FOUND");
            setReturnMessage("No user orders can be found.");
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
