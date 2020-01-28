package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class ReturnCodeMessageResponse {

    protected String returnCode;

    protected String returnMessage;

    public ReturnCodeMessageResponse(final String returnCode, final String returnMessage) {
        this.returnCode = returnCode != null ? returnCode : "";
        this.returnMessage = returnMessage != null ? returnMessage : "";
    }

    public ReturnCodeMessageResponse() {

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
