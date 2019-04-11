package com.landedexperts.letlock.filetransfer.backend.answer;

public class OrderDetailAnswer extends ErrorCodeMessage {
	private int orderDetailId;

	public OrderDetailAnswer( int orderDetailId, String errorCode, String errorMessage ) {
		super(errorCode, errorMessage);
		this.orderDetailId = orderDetailId;
	}

	public int getOrderDetailId() { return this.orderDetailId; }
}
