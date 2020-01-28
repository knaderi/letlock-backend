package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderInfoRecordVO;

public class OrdersInfoResponse extends ReturnCodeMessageResponse {

    public OrdersInfoResponse(final OrderInfoRecordVO[] value, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);
        this.value = value;
    }

    private final OrderInfoRecordVO[] value;

    public OrderInfoRecordVO[] getValue() {
        return value;
    }
}
