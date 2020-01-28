package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferInfoRecordVO;

public class FileTransferSessionsResponse extends ReturnCodeMessageResponse {
	public FileTransferSessionsResponse(final FileTransferInfoRecordVO[] value, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.value = value;
	}

	private final FileTransferInfoRecordVO[] value;

	public FileTransferInfoRecordVO[] getValue() {
		return value;
	}
}
