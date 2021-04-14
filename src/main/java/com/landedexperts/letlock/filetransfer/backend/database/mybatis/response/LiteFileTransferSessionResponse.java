/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.LiteFileTransferRecordVO;

public class LiteFileTransferSessionResponse extends ReturnCodeMessageResponse {
    public LiteFileTransferSessionResponse(final LiteFileTransferRecordVO result, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);
        this.result = result;
    }

    private final LiteFileTransferRecordVO result;

    public LiteFileTransferRecordVO getResult() {
        return result;
    }

}
