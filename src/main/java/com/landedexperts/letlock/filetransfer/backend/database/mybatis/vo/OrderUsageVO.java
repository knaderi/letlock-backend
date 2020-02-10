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
