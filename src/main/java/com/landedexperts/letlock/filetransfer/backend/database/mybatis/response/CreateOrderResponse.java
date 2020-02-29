/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class CreateOrderResponse extends ReturnCodeMessageResponse {
	private final long orderId;

	public CreateOrderResponse(final long orderId, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.orderId = orderId;
	}

	public long getOrderId() { return this.orderId; }
}
