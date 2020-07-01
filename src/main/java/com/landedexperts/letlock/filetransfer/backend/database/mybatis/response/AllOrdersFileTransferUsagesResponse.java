/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import java.util.ArrayList;
import java.util.List;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderUsageVO;

public class AllOrdersFileTransferUsagesResponse extends ReturnCodeMessageResponse {


    private List<OrderUsageVO>  result = new ArrayList<OrderUsageVO>();
    OrdersFileTransfersCountsVO allOrdersFTCounts;
    
    public AllOrdersFileTransferUsagesResponse(List<OrderUsageVO> result,
            OrdersFileTransfersCountsVO allOrdersFTCounts) {
        if(null == result || result.size() == 0) {
            setReturnCode("ORDER_NOT_FOUND");
            setReturnMessage("No user orders can be found.");
        }
        this.result = result;
        this.allOrdersFTCounts = allOrdersFTCounts;
    }
    
    public OrdersFileTransfersCountsVO getAllOrdersFTCounts() {
        return allOrdersFTCounts;
    }



    public void setAllOrdersFTCounts(OrdersFileTransfersCountsVO allOrdersFTCounts) {
        this.allOrdersFTCounts = allOrdersFTCounts;
    }



    public void setResult(List<OrderUsageVO> result) {
        this.result = result;
    }

    public List<OrderUsageVO> getResult() {
        return result;
    }
}
