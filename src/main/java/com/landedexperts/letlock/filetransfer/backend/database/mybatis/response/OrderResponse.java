package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class OrderResponse extends ReturnCodeMessageResponse {
	private final long orderId;

	public OrderResponse(final long orderId, final String returnCode, final String returnMessage) {
		super(returnCode, returnMessage);
		this.orderId = orderId;
	}

	public long getOrderId() { return this.orderId; }
}
