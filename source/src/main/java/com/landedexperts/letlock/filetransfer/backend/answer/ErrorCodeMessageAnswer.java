package com.landedexperts.letlock.filetransfer.backend.answer;

public class ErrorCodeMessageAnswer {
	private final String errorCode;
	private final String errorMessage;

	public ErrorCodeMessageAnswer(String errorCode, String errorMessage) {
		this.errorCode = errorCode != null ? errorCode : "";
		this.errorMessage = errorMessage != null ? errorMessage : "";
	}

	public String getErrorCode() { return this.errorCode; }
	public String getErrorMessage() { return this.errorMessage; }
}
