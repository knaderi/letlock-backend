package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class JsonResponse extends ReturnCodeMessageResponse {
	private final String value;

	public JsonResponse(final String value, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.value = value;
	}

	public String getResult() { return value; }
}
