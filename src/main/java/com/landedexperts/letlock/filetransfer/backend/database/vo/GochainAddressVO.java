package com.landedexperts.letlock.filetransfer.backend.database.vo;

import java.util.UUID;

public class GochainAddressVO extends ErrorCodeMessageVO {
	private UUID gochainAddress;

	public UUID getGochainAddress() {
		return gochainAddress;
	}

	public void setGochainAddress(final String gochainAddress) {
		this.gochainAddress = UUID.fromString(gochainAddress);
	}
}
