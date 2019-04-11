package com.landedexperts.letlock.filetransfer.backend.answer;

public class SessionTokenAnswer extends ErrorCodeMessage {
	private final String token;

	public SessionTokenAnswer(final String token, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.token = token != null ? token : "";
	}

	public String getToken() { return this.token; }
}
