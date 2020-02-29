/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class UuidNameDateArrayResponse extends ReturnCodeMessageResponse {
	public UuidNameDateArrayResponse(final UuidNameDate[] result, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.result = result;
	}

	private final UuidNameDate[] result;

	public UuidNameDate[] getResult() {
		return result;
	}
}
