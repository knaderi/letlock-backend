package com.landedexperts.letlock.filetransfer.backend.database.vo;

import java.util.UUID;

public class ConsumeVO extends ErrorCodeMessageVO {
	private UUID fileTransferUuid;

	public UUID getFileTransferUuid() {
		return fileTransferUuid;
	}

	public void setFileTransferUuid(final String fileTransferUuid) {
		this.fileTransferUuid = fileTransferUuid != null ? UUID.fromString(fileTransferUuid) : null;
	}

	private UUID walletAddressUuid;

	public UUID getWalletAddressUuid() {
		return walletAddressUuid;
	}

	public void setWalletAddressUuid(final String walletAddressUuid) {
		this.walletAddressUuid = walletAddressUuid != null ? UUID.fromString(walletAddressUuid) : null;
	}
}
