package com.landedexperts.letlock.filetransfer.backend.response;

import java.util.UUID;

public class ConsumeResponse extends ErrorCodeMessageResponse {
	private final UUID fileTransferUuid;
	private final UUID walletAddressUuid;

	public ConsumeResponse(final UUID fileTransferUuid, final UUID walletAddressUuid, final String errorCode, final String errorMessage) {
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
