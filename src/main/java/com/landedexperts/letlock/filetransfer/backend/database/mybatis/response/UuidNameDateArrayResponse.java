package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class UuidNameDateArrayResponse extends ErrorCodeMessageResponse {
	public UuidNameDateArrayResponse(final UuidNameDate[] value, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.value = value;
	}

	private final UuidNameDate[] value;

	public UuidNameDate[] getValue() {
		return value;
	}
}
