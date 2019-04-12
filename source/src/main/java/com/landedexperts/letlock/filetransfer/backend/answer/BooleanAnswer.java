package com.landedexperts.letlock.filetransfer.backend.answer;

public class BooleanAnswer extends ErrorCodeMessageAnswer {
	private final boolean result;

	public BooleanAnswer(final boolean result, final String errorCode, final String errorMessage) {
		super(errorCode, errorMessage);
		this.result = result;
	}

	public boolean getResult() { return result; }
}
