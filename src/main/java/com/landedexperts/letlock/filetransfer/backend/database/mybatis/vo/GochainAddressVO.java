package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import java.util.UUID;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class GochainAddressVO extends ReturnCodeMessageResponse {
	private UUID gochainAddress;

	public UUID getGochainAddress() {
		return gochainAddress;
	}

	public void setGochainAddress(final String gochainAddress) {
		this.gochainAddress = UUID.fromString(gochainAddress);
	}
}
