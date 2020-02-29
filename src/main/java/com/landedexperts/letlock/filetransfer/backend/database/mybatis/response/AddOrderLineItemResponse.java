/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class AddOrderLineItemResponse extends ReturnCodeMessageResponse {
	private final long orderLineItemId;

	public AddOrderLineItemResponse( final long orderDetailId, final String returnCode, final String returnMessage ) {
		super(returnCode, returnMessage);
		this.orderLineItemId = orderDetailId;
	}

	public long getOrderLineItemId() { return this.orderLineItemId; }
}
