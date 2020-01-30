package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.ProductVO;

public class ProductsResponse extends ReturnCodeMessageResponse{

    public ProductsResponse(final ProductVO[] result, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);
        this.result = result;
    }

    private final ProductVO[] result;

    public ProductVO[] getResult() {
        return result;
    }
}
