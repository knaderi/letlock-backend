package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import java.util.UUID;

public class UuidResponse extends ReturnCodeMessageResponse {
	private final UUID uuid;

	public UuidResponse( final UUID uuid, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.uuid = uuid;
	}

	public UUID getUuid() { return uuid; }
}
