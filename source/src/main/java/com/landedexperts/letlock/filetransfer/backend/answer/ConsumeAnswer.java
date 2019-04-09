package com.landedexperts.letlock.filetransfer.backend.answer;

public class ConsumeAnswer {
	private String fileTransferUuid;
	private String walletAddressUuid;
	private String errorMessage;

	public ConsumeAnswer(String fileTransferUuid, String walletAddressUuid, String errorMessage) {
		this.fileTransferUuid = fileTransferUuid;
		this.walletAddressUuid = walletAddressUuid;
		this.errorMessage = errorMessage;
	}

	public String getFileTransferUuid() {
		return this.fileTransferUuid;
	}

	public String getWalletAddressUuid() {
		return this.walletAddressUuid;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
