package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class JsonResponse extends ReturnCodeMessageResponse {
	private final String result;

	public JsonResponse(final String result, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.result = result;
	}

	public String getResult() { return result; }
}
