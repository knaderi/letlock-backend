package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class OrderLineItemResponse extends ReturnCodeMessageResponse {
	private final long orderLineItemId;

	public OrderLineItemResponse( final long orderDetailId, final String returnCode, final String returnMessage ) {
		super(returnCode, returnMessage);
		this.orderLineItemId = orderDetailId;
	}

	public long getOrderLineItemId() { return this.orderLineItemId; }
}
