package com.landedexperts.letlock.filetransfer.backend.response;

public class GetFileTransferSessionsForUserResponse extends ErrorCodeMessageResponse {
	public GetFileTransferSessionsForUserResponse(final FileTransferSession[] value, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.value = value;
	}

	private final FileTransferSession[] value;

	public FileTransferSession[] getValue() {
		return value;
	}
}
