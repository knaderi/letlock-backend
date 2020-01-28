package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class BooleanResponse extends ReturnCodeMessageResponse {
	private final boolean result;

	public BooleanResponse(final boolean result, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.result = result;
	}

	public boolean getResult() { return result; }
}
