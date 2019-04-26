package com.landedexperts.letlock.filetransfer.backend.response;

public class TransactionHashResponse  extends ErrorCodeMessageResponse {
	private final String transactionHash;

	public TransactionHashResponse(final String transactionHash, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.transactionHash = transactionHash;
	}

	public String getTransactionHash() { return transactionHash; }
}
