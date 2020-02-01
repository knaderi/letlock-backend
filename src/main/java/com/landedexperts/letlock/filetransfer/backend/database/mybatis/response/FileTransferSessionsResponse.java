package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferInfoRecordVO;

public class FileTransferSessionsResponse extends ReturnCodeMessageResponse {
	public FileTransferSessionsResponse(final FileTransferInfoRecordVO[] result, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.result = result;
	}

	private final FileTransferInfoRecordVO[] result;

	public FileTransferInfoRecordVO[] getValue() {
		return result;
	}
}