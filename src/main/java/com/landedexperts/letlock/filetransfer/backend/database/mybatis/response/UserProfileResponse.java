/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.UserVO;

public class UserProfileResponse extends ReturnCodeMessageResponse {

    private UserVO result;

    public UserVO getResult() {
        return result;
    }
    
    public UserProfileResponse(final UserVO resultValue, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);        
        this.result = resultValue;
    }
}
