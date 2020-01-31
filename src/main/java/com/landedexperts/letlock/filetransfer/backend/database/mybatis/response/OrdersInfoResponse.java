package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderLineItemVO;

public class OrdersInfoResponse extends ReturnCodeMessageResponse {

    private final OrderLineItemVO[] result;
    
    public OrdersInfoResponse(final OrderLineItemVO[] result) {
        super("SUCCESS", "");
        if(null == result || result.length == 0) {
            setReturnCode("ORDER_NOT_FOUND");
            setReturnMessage("No user orders can be found.");
        }
        this.result = result;
    }

    public OrderLineItemVO[] getResult() {
        return result;
    }
}
