package com.landedexperts.letlock.filetransfer.backend.database.result;

public class OrderResult extends ErrorCodeMessageResult {
	private Integer orderId;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
}
