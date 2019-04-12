package com.landedexperts.letlock.filetransfer.backend.answer;

public class AddressAnswer {
	private final String address;
	private final String message;

	public AddressAnswer( final String address, final String message ) {
		this.address = address;
		this.message = message;
	}

	public String getAddress() {
		return address;
	}

	public String getMessage() {
		return message;
	}
}
