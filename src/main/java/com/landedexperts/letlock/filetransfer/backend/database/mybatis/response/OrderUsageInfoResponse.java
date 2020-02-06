package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FTOrderUsageVO;

public class OrderUsageInfoResponse extends ReturnCodeMessageResponse {

    private final FTOrderUsageVO[] result;
    
    public OrderUsageInfoResponse(final FTOrderUsageVO[] result) {
        super("SUCCESS", "");
        if(null == result || result.length == 0) {
            setReturnCode("ORDER_NOT_FOUND");
            setReturnMessage("No user orders can be found.");
        }
        this.result = result;
    }

    public FTOrderUsageVO[] getResult() {
        return result;
    }
}
