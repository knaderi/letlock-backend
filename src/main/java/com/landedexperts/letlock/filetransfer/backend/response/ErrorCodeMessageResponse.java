package com.landedexperts.letlock.filetransfer.backend.response;

public class ErrorCodeMessageResponse {
	private final String errorCode;
	private final String errorMessage;

	public ErrorCodeMessageResponse(final String errorCode, final String errorMessage) {
		this.errorCode = errorCode != null ? errorCode : "";
		this.errorMessage = errorMessage != null ? errorMessage : "";
	}

	public String getErrorCode() { return this.errorCode; }
	public String getErrorMessage() { return this.errorMessage; }
}
