package com.landedexperts.letlock.filetransfer.backend.database.result;

public class BooleanVO extends ErrorCodeMessageVO {
	private boolean value;

	public boolean getValue() {
		return value;
	}

	public void setValue(final boolean value) {
		this.value = value;
	}
}
