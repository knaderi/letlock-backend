package com.landedexperts.letlock.noec2.validator;

public class OrderDetail {
	private Integer productId;
	private Integer quantity;

	public OrderDetail(Integer productId, Integer quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public Integer getProductId() {
		return this.productId;
	}
	public Integer getQuantity() {
		return this.quantity;
	}
}
