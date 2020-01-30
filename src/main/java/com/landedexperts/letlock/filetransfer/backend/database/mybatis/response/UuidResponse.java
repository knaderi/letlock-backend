package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import java.util.UUID;

public class UuidResponse extends ReturnCodeMessageResponse {
	private final UUID result;

	public UuidResponse( final UUID result, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.result = result;
	}

	public UUID getResult() { return result; }
}
