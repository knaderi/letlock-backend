package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class SessionTokenResponse extends ErrorCodeMessageResponse {
	private final String token;

	public SessionTokenResponse(final String token, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.token = token != null ? token : "";
	}

	public String getToken() { return this.token; }
}
