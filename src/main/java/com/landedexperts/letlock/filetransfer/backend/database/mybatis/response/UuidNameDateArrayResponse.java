package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class UuidNameDateArrayResponse extends ReturnCodeMessageResponse {
	public UuidNameDateArrayResponse(final UuidNameDate[] value, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.value = value;
	}

	private final UuidNameDate[] value;

	public UuidNameDate[] getValue() {
		return value;
	}
}
