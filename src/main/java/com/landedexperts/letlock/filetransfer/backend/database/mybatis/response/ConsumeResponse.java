/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class ConsumeResponse extends ReturnCodeMessageResponse {
	private final String fileTransferUuid;
	private final String walletAddressUuid;

	public ConsumeResponse(final String fileTransferUuid, final String walletAddressUuid, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.fileTransferUuid = fileTransferUuid;
		this.walletAddressUuid = walletAddressUuid;
	}

	public String getFileTransferUuid() {
		return this.fileTransferUuid;
	}

	public String getWalletAddressUuid() {
		return this.walletAddressUuid;
	}
}
