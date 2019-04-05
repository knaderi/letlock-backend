package com.landedexperts.letlock.noec2.answer;

public class OrderAnswer {
	private int orderId;
	private String message;

	public OrderAnswer(int orderId, String message) {
		this.orderId = orderId;
		this.message = message;
	}

	public int getOrderId() {return this.orderId; }
	public String getMessage() {return this.message;}
}
