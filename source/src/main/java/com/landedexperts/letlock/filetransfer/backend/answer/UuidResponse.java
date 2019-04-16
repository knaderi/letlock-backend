package com.landedexperts.letlock.filetransfer.backend.answer;

import java.util.UUID;

public class UuidResponse extends ErrorCodeMessageResponse {
	private final UUID uuid;

	public UuidResponse( final UUID uuid, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.uuid = uuid;
	}

	public UUID getUuid() { return uuid; }
}
