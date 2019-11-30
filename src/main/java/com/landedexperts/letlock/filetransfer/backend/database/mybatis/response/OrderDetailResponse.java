package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class OrderDetailResponse extends ErrorCodeMessageResponse {
	private final int orderDetailId;

	public OrderDetailResponse( final int orderDetailId, final String errorCode, final String errorMessage ) {
		super(errorCode, errorMessage);
		this.orderDetailId = orderDetailId;
	}

	public int getOrderDetailId() { return this.orderDetailId; }
}
