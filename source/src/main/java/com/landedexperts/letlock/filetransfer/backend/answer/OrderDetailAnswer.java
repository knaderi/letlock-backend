package com.landedexperts.letlock.filetransfer.backend.answer;

public class OrderDetailAnswer extends ErrorCodeMessageAnswer {
	private final int orderDetailId;

	public OrderDetailAnswer( final int orderDetailId, final String errorCode, final String errorMessage ) {
		super(errorCode, errorMessage);
		this.orderDetailId = orderDetailId;
	}

	public int getOrderDetailId() { return this.orderDetailId; }
}
