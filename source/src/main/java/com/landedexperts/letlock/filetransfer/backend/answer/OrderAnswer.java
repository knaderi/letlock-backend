package com.landedexperts.letlock.filetransfer.backend.answer;

public class OrderAnswer extends ErrorCodeMessageAnswer {
	private final int orderId;

	public OrderAnswer(final int orderId, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.orderId = orderId;
	}

	public int getOrderId() { return this.orderId; }
}
