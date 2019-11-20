package com.landedexperts.letlock.filetransfer.backend.database.vo;

import java.util.UUID;

import com.landedexperts.letlock.filetransfer.backend.response.ErrorCodeMessageResponse;

public class GochainAddressVO extends ErrorCodeMessageResponse {
	private UUID gochainAddress;

	public UUID getGochainAddress() {
		return gochainAddress;
	}

	public void setGochainAddress(final String gochainAddress) {
		this.gochainAddress = UUID.fromString(gochainAddress);
	}
}
