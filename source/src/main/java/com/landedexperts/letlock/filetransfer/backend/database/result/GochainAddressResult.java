package com.landedexperts.letlock.filetransfer.backend.database.result;

import java.util.UUID;

public class GochainAddressResult extends ErrorCodeMessageResult {
	private UUID gochainAddress;

	public UUID getGochainAddress() {
		return gochainAddress;
	}

	public void setGochainAddress(final String gochainAddress) {
		this.gochainAddress = UUID.fromString(gochainAddress);
	}
}
