package com.landedexperts.letlock.filetransfer.backend.answer;

public class OrderDetailAnswer {
	private int orderDetailId;
	private String message;

	public OrderDetailAnswer( int orderDetailId, String message ) {
		this.orderDetailId = orderDetailId;
		this.message = message;
	}

	public int getOrderDetailId() { return this.orderDetailId; }
	public String getMessage() { return this.message; }
}
