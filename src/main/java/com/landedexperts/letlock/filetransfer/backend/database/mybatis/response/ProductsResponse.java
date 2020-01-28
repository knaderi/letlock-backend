package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.ProductVO;

public class ProductsResponse extends ReturnCodeMessageResponse{

    public ProductsResponse(final ProductVO[] value, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);
        this.value = value;
    }

    private final ProductVO[] value;

    public ProductVO[] getValue() {
        return value;
    }
}
