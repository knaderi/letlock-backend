package com.landedexperts.letlock.filetransfer.backend.response;

public class ErrorCodeMessageResponse {

    protected String errorCode;

    protected String errorMessage;

    public ErrorCodeMessageResponse(final String errorCode, final String errorMessage) {
        this.errorCode = errorCode != null ? errorCode : "";
        this.errorMessage = errorMessage != null ? errorMessage : "";
    }

    public ErrorCodeMessageResponse() {

    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
