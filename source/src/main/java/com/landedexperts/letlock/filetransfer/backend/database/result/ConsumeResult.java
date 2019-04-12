package com.landedexperts.letlock.filetransfer.backend.database.result;

import java.util.UUID;

public class ConsumeResult extends ErrorCodeMessageResult {
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
