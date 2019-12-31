package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class OrderLineItemResponse extends ErrorCodeMessageResponse {
	private final int orderLineItemId;

	public OrderLineItemResponse( final int orderDetailId, final String errorCode, final String errorMessage ) {
		super(errorCode, errorMessage);
		this.orderLineItemId = orderDetailId;
	}

	public int getOrderLineItemId() { return this.orderLineItemId; }
}
