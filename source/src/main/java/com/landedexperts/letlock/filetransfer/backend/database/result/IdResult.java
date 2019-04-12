package com.landedexperts.letlock.filetransfer.backend.database.result;

public class IdResult extends ErrorCodeMessageResult {
	private int id;

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}
}
