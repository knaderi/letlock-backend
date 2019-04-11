package com.landedexperts.letlock.filetransfer.backend.answer;

public class BooleanAnswer extends ErrorCodeMessage {
	private final boolean result;

	public BooleanAnswer(boolean result, String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
		this.result = result;
	}

	public boolean getResult() { return result; }
}
