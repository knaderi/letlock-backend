package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderLineItemVO;

public class OrdersInfoResponse extends ReturnCodeMessageResponse {

    public OrdersInfoResponse(final OrderLineItemVO[] result, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);
        this.result = result;
    }

    private final OrderLineItemVO[] result;

    public OrderLineItemVO[] getResult() {
        return result;
    }
}
