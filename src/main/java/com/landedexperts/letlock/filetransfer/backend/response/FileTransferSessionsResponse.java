package com.landedexperts.letlock.filetransfer.backend.response;

import com.landedexperts.letlock.filetransfer.backend.database.vo.FileTransferInfoRecord;

public class FileTransferSessionsResponse extends ErrorCodeMessageResponse {
	public FileTransferSessionsResponse(final FileTransferInfoRecord[] value, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.value = value;
	}

	private final FileTransferInfoRecord[] value;

	public FileTransferInfoRecord[] getValue() {
		return value;
	}
}
