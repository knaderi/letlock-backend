package com.landedexperts.letlock.filetransfer.backend.answer;

public class SessionTokenAnswer {
	private String token;
	private String message;

	public SessionTokenAnswer(String token, String message) {
		this.token = token;
		this.message = message;
	}

	public String getToken() {
		return this.token;
	}

	public String getMessage() {
		return this.message;
	}
}
