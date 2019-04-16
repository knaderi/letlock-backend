package com.landedexperts.letlock.filetransfer.backend.response;

public class OrderResponse extends ErrorCodeMessageResponse {
	private final int orderId;

	public OrderResponse(final int orderId, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.orderId = orderId;
	}

	public int getOrderId() { return this.orderId; }
}
