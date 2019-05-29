package com.landedexperts.letlock.filetransfer.backend.response;

public class BooleanResponse extends ErrorCodeMessageResponse {
	private final boolean result;

	public BooleanResponse(final boolean result, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.result = result;
	}

	public boolean getResult() { return result; }
}
