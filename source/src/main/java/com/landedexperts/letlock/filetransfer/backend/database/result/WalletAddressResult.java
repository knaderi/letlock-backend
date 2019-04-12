package com.landedexperts.letlock.filetransfer.backend.database.result;

import java.util.UUID;

public class WalletAddressResult extends ErrorCodeMessageResult {
	private UUID walletAddress;

	public UUID getWalletAddress() {
		return walletAddress;
	}

	public void setWalletAddress(UUID walletAddress) {
		this.walletAddress = walletAddress;
	}
}
