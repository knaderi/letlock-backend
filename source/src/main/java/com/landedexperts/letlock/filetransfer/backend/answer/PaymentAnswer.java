package com.landedexperts.letlock.filetransfer.backend.answer;

public class PaymentAnswer {
	private int paymentId;
	private String message;

	public PaymentAnswer(int paymentId, String message) {
		this.paymentId = paymentId;
		this.message = message;
	}

	public int getPaymentId() {return this.paymentId; }
	public String getMessage() {return this.message;}
}
