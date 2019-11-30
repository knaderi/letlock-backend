package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class ConsumeResponse extends ErrorCodeMessageResponse {
	private final String fileTransferUuid;
	private final String walletAddressUuid;

	public ConsumeResponse(final String fileTransferUuid, final String walletAddressUuid, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.fileTransferUuid = fileTransferUuid;
		this.walletAddressUuid = walletAddressUuid;
	}

	public String getFileTransferUuid() {
		return this.fileTransferUuid;
	}

	public String getWalletAddressUuid() {
		return this.walletAddressUuid;
	}
}
