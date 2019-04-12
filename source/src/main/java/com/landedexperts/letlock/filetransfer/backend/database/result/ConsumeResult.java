package com.landedexperts.letlock.filetransfer.backend.database.result;

import java.util.UUID;

public class ConsumeResult extends ErrorCodeMessageResult {
	private UUID fileTransferUuid;

	public UUID getFileTransferUuid() {
		return fileTransferUuid;
	}

	public void setFileTransferUuid(UUID fileTransferUuid) {
		this.fileTransferUuid = fileTransferUuid;
	}

	private UUID walletAddressUuid;

	public UUID getWalletAddressUuid() {
		return walletAddressUuid;
	}

	public void setWalletAddressUuid(UUID walletAddressUuid) {
		this.walletAddressUuid = walletAddressUuid;
	}
}
