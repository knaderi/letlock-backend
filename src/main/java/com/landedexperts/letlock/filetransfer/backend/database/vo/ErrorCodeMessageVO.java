package com.landedexperts.letlock.filetransfer.backend.database.vo;

public class ErrorCodeMessageVO {
	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(final String errorCode) {
		this.errorCode = errorCode;
	}

	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}
}