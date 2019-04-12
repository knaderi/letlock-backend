package com.landedexperts.letlock.filetransfer.backend.answer;

import java.util.UUID;

public class ConsumeAnswer extends ErrorCodeMessageAnswer {
	private final UUID fileTransferUuid;
	private final UUID walletAddressUuid;

	public ConsumeAnswer(final UUID fileTransferUuid, final UUID walletAddressUuid, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.fileTransferUuid = fileTransferUuid;
		this.walletAddressUuid = walletAddressUuid;
	}

	public UUID getFileTransferUuid() {
		return this.fileTransferUuid;
	}

	public UUID getWalletAddressUuid() {
		return this.walletAddressUuid;
	}
}
