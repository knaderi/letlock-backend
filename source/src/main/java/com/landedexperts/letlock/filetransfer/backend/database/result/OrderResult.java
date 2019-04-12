package com.landedexperts.letlock.filetransfer.backend.database.result;

public class OrderResult extends ErrorCodeMessageResult {
	private int orderId;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
}
