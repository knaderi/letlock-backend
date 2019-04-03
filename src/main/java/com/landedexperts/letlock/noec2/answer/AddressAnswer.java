package com.landedexperts.letlock.noec2.answer;

public class AddressAnswer {
	private final String address;
	private final String message;

	public AddressAnswer( String address, String message ) {
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
