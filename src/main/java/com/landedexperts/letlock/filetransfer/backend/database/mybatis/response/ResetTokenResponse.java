/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.ResetTokenVO;

public class ResetTokenResponse extends ReturnCodeMessageResponse {

    private ResetTokenVO result;

    public ResetTokenVO getResult() {
        return result;
    }
    
    public ResetTokenResponse(final ResetTokenVO resultValue, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);        
        this.result = resultValue;
    }
}
