package com.landedexperts.letlock.filetransfer.backend.response;

public class AddressResponse {
	private final String address;
	private final String message;

	public AddressResponse( final String address, final String message ) {
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
