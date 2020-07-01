package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class OrdersFileTransfersCountsResponse  extends ReturnCodeMessageResponse  {

    private OrdersFileTransfersCountsVO result = new OrdersFileTransfersCountsVO();

    public OrdersFileTransfersCountsResponse(OrdersFileTransfersCountsVO fileTransferCounts, String returnCode, String returnMessage) {
        super(returnCode, returnMessage);
        result = fileTransferCounts;
    }

    public OrdersFileTransfersCountsVO getResult() {
        return result;
    }

    public void setResult(OrdersFileTransfersCountsVO result) {
        this.result = result;
    }

}
