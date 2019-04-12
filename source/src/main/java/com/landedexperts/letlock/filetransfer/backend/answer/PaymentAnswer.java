package com.landedexperts.letlock.filetransfer.backend.answer;

public class PaymentAnswer {
	private final int paymentId;
	private final String message;

	public PaymentAnswer(final int paymentId, final String message) {
		this.paymentId = paymentId;
		this.message = message;
	}

	public int getPaymentId() {return this.paymentId; }
	public String getMessage() {return this.message;}
}
