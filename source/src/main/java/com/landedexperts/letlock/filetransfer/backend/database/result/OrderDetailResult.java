package com.landedexperts.letlock.filetransfer.backend.database.result;

public class OrderDetailResult extends ErrorCodeMessageResult {
	private int orderDetailId;

	public int getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(int orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
}
