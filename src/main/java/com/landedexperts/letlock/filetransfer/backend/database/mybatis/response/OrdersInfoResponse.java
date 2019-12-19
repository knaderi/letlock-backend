package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderInfoRecordVO;

public class OrdersInfoResponse extends ErrorCodeMessageResponse {

    public OrdersInfoResponse(final OrderInfoRecordVO[] value, final String errorCode, final String errorMessage) {
        super(errorCode, errorMessage);
        this.value = value;
    }

    private final OrderInfoRecordVO[] value;

    public OrderInfoRecordVO[] getValue() {
        return value;
    }
}
