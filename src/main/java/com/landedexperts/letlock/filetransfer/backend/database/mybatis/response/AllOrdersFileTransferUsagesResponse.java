package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderUsageVO;

public class AllOrdersFileTransferUsagesResponse extends ReturnCodeMessageResponse {


    private OrderUsageVO[]  result = new OrderUsageVO[] {};
    OrdersFileTransfersCountsResponse allOrdersFTCounts;
    
    public AllOrdersFileTransferUsagesResponse(OrderUsageVO[] result,
            OrdersFileTransfersCountsResponse allOrdersFTCounts) {
        if(null == result || result.length == 0) {
            setReturnCode("ORDER_NOT_FOUND");
            setReturnMessage("No user orders can be found.");
        }
        this.result = result;
        this.allOrdersFTCounts = allOrdersFTCounts;
    }
    
    public OrdersFileTransfersCountsResponse getAllOrdersFTCounts() {
        return allOrdersFTCounts;
    }



    public void setAllOrdersFTCounts(OrdersFileTransfersCountsResponse allOrdersFTCounts) {
        this.allOrdersFTCounts = allOrdersFTCounts;
    }



    public void setResult(OrderUsageVO[] result) {
        this.result = result;
    }

    public OrderUsageVO[] getResult() {
        return result;
    }
}
