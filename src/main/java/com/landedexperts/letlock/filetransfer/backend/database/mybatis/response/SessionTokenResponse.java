package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class SessionTokenResponse extends ReturnCodeMessageResponse {
	private final String token;

	public SessionTokenResponse(final String token, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.token = token != null ? token : "";
	}

	public String getToken() { return this.token; }
}
