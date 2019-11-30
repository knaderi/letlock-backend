package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferInfoRecordVO;

public class FileTransferSessionsResponse extends ErrorCodeMessageResponse {
	public FileTransferSessionsResponse(final FileTransferInfoRecordVO[] value, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.value = value;
	}

	private final FileTransferInfoRecordVO[] value;

	public FileTransferInfoRecordVO[] getValue() {
		return value;
	}
}
