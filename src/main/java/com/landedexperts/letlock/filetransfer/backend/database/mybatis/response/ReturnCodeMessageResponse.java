/*******************************************************************************
\ * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;
import com.landedexperts.letlock.filetransfer.backend.utils.ResponseCode;

public class ReturnCodeMessageResponse {

    protected String returnCode = "SUCCESS";

    protected String returnMessage = "";

    public ReturnCodeMessageResponse(final String returnCode, final String returnMessage) {
        this.returnCode = returnCode != null ? returnCode : "";
        this.returnMessage = returnMessage != null ? returnMessage : "";
    }

    public ReturnCodeMessageResponse() {

    }

    public ReturnCodeMessageResponse(ResponseCode code) {
        this.returnCode = code.name();
        this.returnMessage = code.getMessage();
    }

    public String getReturnCode() {
        return this.returnCode;
    }

    public String getReturnMessage() {
        return this.returnMessage;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }
}
