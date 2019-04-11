package com.landedexperts.letlock.filetransfer.backend.answer;

public class SessionTokenAnswer {
	private final String token;
	private final String errorCode;
	private final String errorMessage;

	public SessionTokenAnswer(final String token, final String errorCode, final String errorMessage) {
		this.token = token != null ? token : "";
		this.errorCode = errorCode != null ? errorCode : "";
		this.errorMessage = errorMessage != null ? errorMessage : "";
	}

	public String getToken() {
		return this.token;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
