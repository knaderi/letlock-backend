package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.ProductVO;

public class ProductsResponse extends ErrorCodeMessageResponse{

    public ProductsResponse(final ProductVO[] value, final String errorCode, final String errorMessage) {
        super(errorCode, errorMessage);
        this.value = value;
    }

    private final ProductVO[] value;

    public ProductVO[] getValue() {
        return value;
    }
}
