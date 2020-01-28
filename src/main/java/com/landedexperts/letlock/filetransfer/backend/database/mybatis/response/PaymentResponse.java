package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class PaymentResponse {
	private final long paymentId;
	private final String message;

	public PaymentResponse(final long paymentId, final String message) {
		this.paymentId = paymentId;
		this.message = message;
	}

	public long getPaymentId() {return this.paymentId; }
	public String getMessage() {return this.message;}
}
