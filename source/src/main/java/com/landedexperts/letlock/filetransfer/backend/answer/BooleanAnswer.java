package com.landedexperts.letlock.filetransfer.backend.answer;

public class BooleanAnswer {
	private final boolean result;
	private final String message;

	public BooleanAnswer( boolean result, String message ) {
		this.result = result;
		this.message = message;
	}

	public String getResult() {
		return (result ? "Success" : "Failed");
	}

	public String getMessage() {
		return message;
	}
}
